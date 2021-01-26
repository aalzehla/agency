package com._3line.gravity.core.verification.service.impl;

import com._3line.gravity.core.email.service.MailService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.usermgt.model.SystemUser;
import com._3line.gravity.core.usermgt.repository.ApplicationUserRepository;
import com._3line.gravity.core.verification.dtos.VerificationDto;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.core.verification.models.Verification;
import com._3line.gravity.core.verification.models.VerificationStatus;
import com._3line.gravity.core.verification.repository.VerificationRepository;
import com._3line.gravity.core.verification.service.VerificationService;
import com._3line.gravity.core.verification.service.VerificationState;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
//import com.netflix.discovery.converters.Auto;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Service
public class VerificationServiceImplementation implements VerificationService {


    @Autowired
    private VerificationRepository verificationRepository ;
    @Autowired
    private ApplicationUserRepository userRepository ;

    @Autowired
    private SettingService settingService ;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ModelMapper modelMapper;
    private Locale locale = LocaleContextHolder.getLocale();
    @Autowired
    private ApplicationContext _appContext;
    @Autowired
    private VerificationState verificationState ;
    @Autowired
    private MailService mailService;

    @Autowired
    AgentService agentService;

    @Autowired
    EntityManager entityManager;

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    @Override
    public VerificationDto getVerification(Long id) {
        return convertEntityToDTO(verificationRepository.findOne(id));
    }

    @Override
    public String verify(VerificationDto verificationDto) {

        System.out.println("fffff "+getCurrentUserName());

        verificationState.addToMode(getCurrentUserName());
        String responseMessage = "";

        Verification verification = verificationRepository.findOne(verificationDto.getId());
        String verifiedBy = getCurrentUserName();
        if (verifiedBy.equals(verification.getInitiatedBy())) {
            throw new VerificationException("You cannot verify what you initiated");
        }

        if (!VerificationStatus.PENDING.equals(verification.getStatus())) {
            throw new VerificationException("Verification is not pending for the operation");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));


        try {

            verification.setId(verificationDto.getId());
            verification.setVerifiedBy(getCurrentUserName());
            verification.setVerifiedOn(new Date());
            verification.setComments(verificationDto.getComments());
            verificationRepository.save(verification);

            try {
                Class<?> clazz;
                Object object;

                clazz = Class.forName(verification.getEntityName());

                Class c = Class.forName(verification.getClassName());
                Class bean = _appContext.getBean(c).getClass();

                Method methods = bean.getMethod(verification.getMethodName(), clazz);

                Class<?> tobeSent = Class.forName(verification.getEntityName());
                object = mapper.readValue(verification.getOriginalObject(), tobeSent);

                methods.invoke(_appContext.getBean(c), object);

                verification.setStatus(VerificationStatus.APPROVED);
                verificationRepository.save(verification);

            }catch (Exception e){
                System.out.println("issh 5 ::"+e.getMessage());
                e.printStackTrace();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                String cause = e.getCause()!=null?e.getCause().getMessage():e.getMessage();
                if(cause.length() > 30){
                    cause = cause.substring(0,30);
                }
                verification.setReason(exceptionAsString);
                verificationRepository.save(verification);
                throw new GravityException("Error Occurred Completing Verification: "+cause);
            }




            if (settingService.isSettingAvailable("OPERATION_VERIFICATION_SEND_EMAIL")) {
                SystemUser systemUser = userRepository.findByUserName(verification.getInitiatedBy());
                if(systemUser == null){
                    systemUser = userRepository.findByUserName("aggregatorrep");
                    logger.info("agent username now assigned as {}", systemUser.getUserName());
                }
                Map params = new HashMap();
                params.put("name", systemUser.getFirstName() + " " + systemUser.getLastName());
                params.put("operation", verificationDto.getCode());
                params.put("approver", getCurrentUserName());
                mailService.sendMail("Operation has been approved !", systemUser.getEmail(), null, params, "verification_approved");
            }


        } catch (Exception ibe) {
            ibe.printStackTrace();
            responseMessage = ibe.getMessage();
            throw new GravityException(responseMessage);
        }

        verificationState.removeFromMode(getCurrentUserName());
        return messageSource.getMessage("verification.verify", null, locale);
    }


    @Override
    public String decline(VerificationDto dto) throws VerificationException {

        Verification verification = verificationRepository.findOne(dto.getId());
        String verifiedBy = getCurrentUserName();

        if (verifiedBy.equals(verification.getInitiatedBy())) {
            throw new VerificationException("You cannot verify what you initiated");
        }

        if (!VerificationStatus.PENDING.equals(verification.getStatus())) {
            throw new VerificationException("Verification is not pending for the operation");
        }

        try {
            verification.setVersion(dto.getVersion());
            verification.setVerifiedBy(getCurrentUserName());
            verification.setComments(dto.getComments());
            verification.setVerifiedOn(new Date());
            verification.setStatus(VerificationStatus.DECLINED);
            verificationRepository.save(verification);

            if (settingService.isSettingAvailable("OPERATIONG_DECLINE_SEND_EMAIL")) {
                SystemUser systemUser = userRepository.findByUserName(dto.getInitiatedBy());
                if(systemUser == null){
                    systemUser = userRepository.findByUserName("aggregatorrep");
                    logger.info("agent username now assigned as {}", systemUser.getUserName());
                }
                Map params = new HashMap();
                params.put("name", systemUser.getFirstName() + " " + systemUser.getLastName());
                params.put("operation", dto.getCode());
                params.put("approver", getCurrentUserName());
                mailService.sendMail("Operation has been declined !", systemUser.getEmail(), null, params, "verification_declined");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageSource.getMessage("verification.decline", null, locale);
    }

    @Override
    public String cancel(Long id) throws VerificationException {

        Verification verification = verificationRepository.findOne(id);
        String verifiedBy = getCurrentUserName();

        if (!verifiedBy.equals(verification.getInitiatedBy())) {
            throw new VerificationException("You cannot cancel what you did not initiated");
        }

        try {

            verificationRepository.delete(verification);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageSource.getMessage("verification.deleted", null, locale);
    }

    @Override
    public long getTotalNumberPending() {
        return verificationRepository.countByInitiatedByAndStatus(getCurrentUserName(), VerificationStatus.PENDING);
    }


    @Override
    public int getTotalNumberForVerification() {

        try {
            List<String> permissions = new ArrayList<>();
            List<Verification> b = verificationRepository.findVerificationForUser(getCurrentUserName(), permissions);
            return b.size();
        } catch (Exception e) {
            logger.error("Error retrieving verification", e);
        }
        return 0;
    }

    @Override
    public Page<VerificationDto> getPendingForUser(Pageable pageable) {

        SystemUser user = userRepository.findByUserName(getCurrentUserName());
        List<String> usersactions = new ArrayList<>();
        logger.info("user role {}",user.getRole());
        for (String s:user.getRole().getApprovables()) {
            usersactions.add(s);
        }
        logger.info("actions {}", usersactions);
        Page<Verification> page = verificationRepository.findByInitiatedByNotLikeAndStatusAndCodeInOrderByInitiatedOnDesc(getCurrentUserName() ,VerificationStatus.PENDING,usersactions, pageable);
        List<VerificationDto> dtOs = convertEntitiesToDTOs(page.getContent());
        long t = page.getTotalElements();
        Page<VerificationDto> pageImpl = new PageImpl<VerificationDto>(dtOs, pageable, t);
        return pageImpl;

    }

    @Override
    public Page<VerificationDto> getInitiatedByUser(Pageable pageable) {

        Page<Verification> page = verificationRepository.findByInitiatedBy(getCurrentUserName() , pageable);
        List<VerificationDto> dtOs = convertEntitiesToDTOs(page.getContent());
        long t = page.getTotalElements();
        Page<VerificationDto> pageImpl = new PageImpl<VerificationDto>(dtOs, pageable, t);
        return pageImpl;

    }

    @Override
    public Page<VerificationDto> getApprovableActions(Pageable pageable,String opCode,String initiator,String from,String to) {

        Date startDate = new Date();
        Date endDate = new Date();

        logger.info("inputs {} {} {} {}",opCode,initiator,from,to);


        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Verification> q = cb.createQuery(Verification.class);
        Root<Verification> c = q.from(Verification.class);
        List<Predicate> predicates = new ArrayList<>();

        try {


            if (opCode != null && !opCode.equals("")) {
                predicates.add((cb.equal(c.get("code"), opCode)) );
            }

            if (initiator != null && !initiator.equals("")) {
                predicates.add((cb.equal(c.get("initiatedBy"), initiator)) );
            }


            if (startDate != null && endDate!=null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                startDate = dateFormat.parse(from);
                endDate = dateFormat.parse(to);
                predicates.add(cb.between(c.get("initiatedOn"), startDate, endDate) );
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new PageImpl<>(new ArrayList<>());
        }

        CriteriaQuery<Verification> baseQuery = null;
        CriteriaQuery<Long> qc = cb.createQuery(Long.class).orderBy();
        CriteriaQuery<Long> countQuery = null;
        if (predicates.size() > 0) {
            Predicate and = cb.and(predicates.toArray(new Predicate[0]));
            baseQuery = q.select(c).where(and).orderBy(cb.desc(c.get("id")));
            countQuery = qc.select(cb.count(qc.from(Verification.class))).where(and);
        } else {
            baseQuery = q.select(c);
            countQuery = qc.select(cb.count(qc.from(Verification.class)));
        }

        TypedQuery<Verification> query = entityManager.createQuery(baseQuery);
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Verification> list = query.getResultList();
        List<VerificationDto> dtOs = convertEntitiesToDTOs(list);
        return new PageImpl<>(dtOs, pageable, count);

    }




    private String getCurrentUserName() {
        try {
            String principal =  SecurityContextHolder.getContext().getAuthentication()
                    .getName();
            return principal;
        }catch (Exception d){
            return "SYSTEM" ;
        }
    }


    private VerificationDto convertEntityToDTO(Verification verification) {
        return modelMapper.map(verification, VerificationDto.class);
    }

    private List<VerificationDto> convertEntitiesToDTOs(Iterable<Verification> verifications) {
        List<VerificationDto> verificationDTOArrayList = new ArrayList<>();
        for (Verification verification : verifications) {
            VerificationDto verificationDTO = convertEntityToDTO(verification);
            verificationDTOArrayList.add(verificationDTO);
        }
        return verificationDTOArrayList;
    }



}
