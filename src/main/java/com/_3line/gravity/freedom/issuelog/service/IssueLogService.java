package com._3line.gravity.freedom.issuelog.service;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.email.service.MailService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.files.service.FileService;
import com._3line.gravity.core.notification.NotificationService;
import com._3line.gravity.core.security.service.implementation.CustomUserPrincipal;
import com._3line.gravity.core.setting.dto.SettingDTO;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.usermgt.service.ApplicationUserService;
import com._3line.gravity.core.utils.AppUtility;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.dispute.dtos.DisputeLogDTO;
import com._3line.gravity.freedom.dispute.models.Dispute;
import com._3line.gravity.freedom.dispute.models.DisputeLog;
import com._3line.gravity.freedom.dispute.repository.DisputeRepository;
import com._3line.gravity.freedom.dispute.service.DisputeLogService;
import com._3line.gravity.freedom.issuelog.dtos.CommentDTO;
import com._3line.gravity.freedom.issuelog.dtos.IssueLogDTO;
import com._3line.gravity.freedom.issuelog.models.Comments;
import com._3line.gravity.freedom.issuelog.models.IssueLog;
import com._3line.gravity.freedom.issuelog.models.IssueStatus;
import com._3line.gravity.freedom.issuelog.repository.IssueLogRepository;
import com._3line.gravity.freedom.notifications.dtos.NotificationDTO;
import com._3line.gravity.freedom.notifications.models.Notification;
import com._3line.gravity.freedom.notifications.service.MobileNotificationService;
import com._3line.gravity.freedom.utility.DateUtil;
import com._3line.gravity.freedom.wallet.models.FreedomWalletTransaction;
import com._3line.gravity.freedom.wallet.repository.FreedomWalletRepository;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class IssueLogService {

    private final Locale locale = LocaleContextHolder.getLocale();
    @Autowired
    IssueLogRepository issueLogRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ApplicationUserService userService;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MailService mailService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private SettingService settingService;
    @Autowired
    private AgentService agentService;

    @Autowired
    MobileNotificationService notificationService;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    EntityManager entityManager;

    @Autowired
    FileService fileService;

    @Autowired
    DisputeRepository disputeRepository;


    @Autowired
    DisputeLogService disputeLogService;

    @Autowired
    WalletService walletService;



    public String linkToNotification(Long issueLogId,Long notificationId){
        IssueLog issueLog = issueLogRepository.findOne(issueLogId);
        if(issueLog!=null){
            Notification notification = notificationService.getNotificationById(notificationId);
            issueLog.setNotification(notification);
            issueLogRepository.save(issueLog);
        }

        return null;
    }

    public String linkToDisputeLog(Long issueLogId, DisputeLog disputeLog){
        IssueLog issueLog = issueLogRepository.findOne(issueLogId);
        if(issueLog!=null){
            issueLog.setDisputeLog(disputeLog);
            issueLogRepository.save(issueLog);
        }

        return null;
    }


    public IssueLogDTO logIssue(IssueLogDTO logDTO) {
        logger.debug("logging issue {}", logDTO.toString());

        Agents agent = jwtUtility.getCurrentAgent();//validateAgent(logDTO.getAgentName());
        IssueLog issueLog = modelMapper.map(logDTO, IssueLog.class);
        issueLog.setStatus(IssueStatus.PENDING);
        issueLog.setIsRead(false);
        issueLog.setAgent(agent);

//        if(issueLog.getImage()!=null && !issueLog.getImage().equals("")){
//
//            String base64Encoded = issueLog.getImage().split(",")[1];
//            String fileExtension = issueLog.getImage().split(";")[0].split("/")[1];
//
//            String fileName = agent.getAgentId().toLowerCase()+new Date().getTime()+"."+fileExtension;
//
//            String imageUrl = fileService.storeFile(base64Encoded,fileName,"IMAGE_CONVERTER");
//
//            issueLog.setImage(imageUrl);
//            logDTO.setImage(imageUrl);
//        }

        String ticketId = generateTicketID();
        issueLog.setTicketId(ticketId);
        logDTO.setTicketId(ticketId);

        issueLog = issueLogRepository.save(issueLog);

        logDTO.setId(issueLog.getId());
        logDTO.setAgentName(agent.getUsername());
        logDTO.setAgentEmail(agent.getEmail());
        logDTO.setAgentPhone(agent.getPhoneNumber());

        SettingDTO settingDTO = settingService.getSettingByCode("ISSUE_LOG_EMAIL");
        Map<String, Object> params = new HashMap<>();
        params.put("agent", agent.getUsername());
        params.put("email", agent.getEmail());
        params.put("complaint", issueLog.getComplaint());
        mailService.sendMail("Complaint from Agent !", settingDTO.getValue(), null, params, "logged_issue");

        return logDTO;
    }

    public IssueLog findIssueLog(Long id) {
        return issueLogRepository.findOne(id);
    }

    public IssueLog findByDisputeLogId(Long id) {
        return issueLogRepository.findByDisputeLog_Id(id);
    }


    public IssueLog findIssueLogByNotificationId(Long id) {
        return issueLogRepository.findIssueLogByNotification_Id(id);
    }

    public IssueLog updateIssueLog(IssueLog issueLog) {
        return issueLogRepository.save(issueLog);
    }

    public String updateCommentAndStatus(IssueLogDTO issueLogDTO) {
        String response = "";
        logger.info("logging issue update comment {}", issueLogDTO);
        try {
            IssueLog issueLog = addComments(issueLogDTO);
            issueLog.setComment(issueLogDTO.getComment());

            if (issueLogDTO.getButtonAction().equals("RESOLVE")) {
                issueLog.setTreatedDate(new Date());
                issueLog.setTreatedBy(AppUtility.getCurrentUserName());
                issueLog.setStatus(IssueStatus.RESOLVED);
            }else if (issueLogDTO.getButtonAction().equals("RAISE_DISPUTE")) {

                DisputeLogDTO logDTO = modelMapper.map(issueLog.getDisputeLog(),DisputeLogDTO.class);

                List<FreedomWalletTransaction> transactions =  walletService.getWalletTransactionsByTranId(String.valueOf(logDTO.getTranId()));

                if(transactions.size() > 0){
                    throw new GravityException("Credit has already been done");
                }

                issueLog.setStatus(IssueStatus.PENDING);
                issueLogRepository.save(issueLog);


                Dispute dispute = issueLog.getDisputeLog().getDispute();

                dispute.setRaisedBy(AppUtility.getCurrentUserName());
                disputeRepository.save(dispute);
                issueLog.getDisputeLog().setDispute(dispute);

                logDTO.setLoggedBy(dispute.getLoggedBy());
                logDTO.setRaisedBy(dispute.getRaisedBy());
                logDTO.setRemark(issueLogDTO.getComment());


                String respMessage = disputeLogService.raiseWalletDispute(logDTO);
                return respMessage;
            } else {
                issueLog.setTreatedDate(new Date());
                issueLog.setTreatedBy(AppUtility.getCurrentUserName());
                issueLog.setStatus(IssueStatus.REJECTED);
            }


            logger.info("get the mail indicator {}", issueLogDTO.isMailIndicatior());

            IssueLog saveLog = issueLogRepository.save(issueLog);
            if(issueLog.getStatus().equals(IssueStatus.RESOLVED) || issueLog.getStatus().equals(IssueStatus.REJECTED)){
                NotificationDTO notification = new NotificationDTO();
                notification.setAgentName(saveLog.getAgent().getUsername());
                notification.setId(saveLog.getNotification().getId());
                notificationService.markNotificationAsUnread(notification);
            }
            response = "Successful";
            if (issueLogDTO.isMailIndicatior()) {
                try{
                    sendOutMail(issueLogDTO, saveLog);
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
            response = e.getCause()==null?e.getMessage():e.getCause().getMessage();

        }
        return response;
    }

    private void sendOutMail(IssueLogDTO issueLogDTO, IssueLog saveLog) {
        SettingDTO settingDTO = settingService.getSettingByCode("ISSUE_LOG_EMAIL");
        Map<String, Object> params = new HashMap<>();
        params.put("agent", saveLog.getAgent().getUsername());
        params.put("email", saveLog.getAgent().getEmail());
        params.put("complaint", saveLog.getComplaint());
        params.put("comment", saveLog.getComment());
        params.put("status", saveLog.getStatus());
        params.put("message", issueLogDTO.getCompose());
        mailService.sendMail("Update on your complaint !", saveLog.getAgent().getEmail(), (String[]) Arrays.asList(settingDTO.getValue(), "").toArray(), null, "updated_issue");
    }

    public Page<IssueLogDTO> getTransactions(Pageable pageDetails) throws GravityException {

        Page<IssueLog> page = issueLogRepository.findAllByOrderByIdDesc(pageDetails);
        logger.info("page details {}", page.getContent());
        List<IssueLogDTO> dtOs = convertEntitiesToDTOs(page.getContent());
        long t = page.getTotalElements();
        Page<IssueLogDTO> pageImpl = new PageImpl<IssueLogDTO>(dtOs, pageDetails, t);
        return pageImpl;
    }

    public Page<IssueLog> getTransactionsEntity(Pageable pageDetails) throws GravityException {
        Page<IssueLog> page = issueLogRepository.findAllByOrderByIdDesc(pageDetails);
        logger.info("page details {}", page.getContent());
        return page;
    }


    public Page<IssueLogDTO> findTransaction(String pattern, Pageable pageDetails) {

        Page<IssueLog> page = issueLogRepository.findByStatusOrderByCreatedOnDesc(IssueStatus.valueOf(pattern), pageDetails);
        logger.info("page details {}", page.getContent());
        List<IssueLogDTO> dtOs = convertEntitiesToDTOs(page.getContent());
        long t = page.getTotalElements();
        return new PageImpl<>(dtOs, pageDetails, t);

    }


    public List<IssueLogDTO> convertEntitiesToDTOs(List<IssueLog> issueLogs) {
        return issueLogs.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }


    public IssueLogDTO convertEntityToDTO(IssueLog issueLog) {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        IssueLogDTO issueLogDTO = modelMapper.map(issueLog, IssueLogDTO.class);
        issueLogDTO.setStatus(issueLog.getStatus().name());
        issueLogDTO.setAgentName(issueLog.getAgent().getUsername());
        issueLogDTO.setLoggedOn(issueLog.getCreatedOn());
        return issueLogDTO;
    }

    public IssueLog convertDTOToEntity(IssueLogDTO issueLogDTO) {
        IssueLog issueLog = modelMapper.map(issueLogDTO, IssueLog.class);
        issueLog.setLoggedOn(issueLog.getCreatedOn());
        return issueLog;
    }


    public List<IssueLogDTO> fetchComplaints() {
        List<IssueLogDTO> complaintsDTOS = new ArrayList<>();
        issueLogRepository.findByOrderByCreatedOnDesc().forEach(complaint -> {
            IssueLogDTO complaintsDTO = convertEntityToDTO(complaint);
            complaintsDTOS.add(complaintsDTO);
        });
        return complaintsDTOS;
    }

    public IssueLogDTO closeComplaint(IssueLogDTO complaintsDTO) {
        validateAgent(complaintsDTO.getAgentName());
        IssueLog complaint = issueLogRepository.findByAgent_UsernameAndId
                (complaintsDTO.getAgentName(), Long.valueOf(complaintsDTO.getId()));

        if (complaint != null) {
            complaint.setIsClosed(true);
            complaint.setClosedDate(new Date());
            issueLogRepository.save(complaint);
        } else {
            throw new GravityException("Record Not Found");
        }
        return convertEntityToDTO(complaint);
    }

    public IssueLogDTO markAsRead(IssueLogDTO complaintsDTO) {
//        validateAgent(complaintsDTO.getAgentName());
        IssueLog complaint = issueLogRepository.findByAgent_UsernameAndId
                (complaintsDTO.getAgentName(), Long.valueOf(complaintsDTO.getId()));

        if (complaint != null) {
            complaint.setIsRead(true);
            issueLogRepository.save(complaint);
        } else {
            throw new GravityException("Record Not Found");
        }
        return convertEntityToDTO(complaint);
    }

    public IssueLogDTO fetchAgentUnreadComplaint(String agentName, Long complaintId) {
        validateAgent(agentName);
        IssueLog complaint = issueLogRepository.findByAgent_UsernameAndIdAndIsRead(agentName, complaintId, false);
        return convertEntityToDTO(complaint);
    }

    public List<IssueLogDTO> fetchAgentUnreadComplaints(String agentName) {
        validateAgent(agentName);
        return issueLogRepository.findByAgent_UsernameAndIsRead(agentName, false)
                .stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());

    }

    public IssueLogDTO fetchAgentComplaint(String agentName, Long complaintId) {
        validateAgent(agentName);
        IssueLog complaint = issueLogRepository.findByAgent_UsernameAndId(agentName, complaintId);
        if(complaint==null){
            throw new GravityException("No Record found");
        }
        return convertEntityToDTO(complaint);
    }

    public List<IssueLogDTO> fetchAgentComplaints(String agentName) {
        validateAgent(agentName);
        return issueLogRepository.findByAgent_Username(agentName)
                .stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());

    }


    public Agents validateAgent(String agentName) {
        Agents agents = agentService.fetchAgentByAgentName(agentName);
        if (agents == null) {
            throw new GravityException("Invalid Agent username passed");
        }
        return agents;
    }

    public Page<IssueLogDTO> findUsingPattern(String status, Pageable pageable) {
        Page<IssueLog> page = issueLogRepository.findUsingPattern(status, pageable);
        List<IssueLogDTO> dtOs = convertEntitiesToDTOs(page.getContent());
        long t = page.getTotalElements();
        return new PageImpl<>(dtOs, pageable, t);
    }


    public  IssueLog addComments(IssueLogDTO issueLogDTO) {
        IssueLog issueLog = issueLogRepository.findOne(issueLogDTO.getId());
        Comments comments = new Comments();
        comments.setMadeOn(new Date());
        comments.setComment(issueLogDTO.getComment());
        if (issueLogDTO.getButtonAction().equals("REJECT")) {
            comments.setStatus(IssueStatus.REJECTED.toString());
        } else if (issueLogDTO.getButtonAction().equals("RESOLVE")) {
            comments.setStatus(IssueStatus.RESOLVED.toString());
        } else if (issueLogDTO.getButtonAction().equals("RAISE_DISPUTE")) {
            comments.setStatus(IssueStatus.PENDING.toString());
        } else {
            comments.setStatus("VIEWED");
        }
        comments.setUsername(AppUtility.getCurrentUserName());

        List<Comments> oldComment = issueLog.getComments();
        oldComment.add(comments);
        issueLog.setComments(oldComment);

        issueLogRepository.save(issueLog);
        return issueLog;
    }

    public List<CommentDTO> getComments(Long id) {
        IssueLog issueLog = issueLogRepository.findOne(id);
        if (issueLog == null) {
            return new ArrayList<>();
        }
        return convertEntitiestoDTOsComment(issueLog.getComments());
    }

    private List<CommentDTO> convertEntitiestoDTOsComment(List<Comments> commentDTOS) {
        return commentDTOS.stream().map(this::convertEntityToDTOComment).collect(Collectors.toList());
    }

    private CommentDTO convertEntityToDTOComment(Comments comments) {
        return modelMapper.map(comments, CommentDTO.class);
    }

    public Page<IssueLog> getTransactionsCustom(String search, String status, String from, String to,String category, String ticketId,Pageable pageable) {
        Date startDate = new Date();
        Date endDate = new Date();

        System.out.println(search+" and "+category+" and "+status+" "+from+" "+to);


        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<IssueLog> q = cb.createQuery(IssueLog.class);
        Root<IssueLog> c = q.from(IssueLog.class);
        List<Predicate> predicates = new ArrayList<>();

        try {

            if( ticketId!=null &&  !ticketId.equals("")){
                predicates.add((cb.equal(c.get("ticketId"), ticketId)) );
            }else{
                if (search != null && !search.equals("")) {
                    //subquery
                    Subquery<Agents> agentsSubquery = q.subquery(Agents.class);
                    Root<Agents> agentRoot = agentsSubquery.from(Agents.class);
                    agentsSubquery.select(agentRoot)//subquery selection
                            .where(cb.equal(agentRoot.get("username"),search));
                    predicates.add((cb.equal(c.get("agent"), agentsSubquery)) );
                }

                if (category != null && !category.equals("")) {
                    predicates.add((cb.equal(c.get("category"), category)) );
                }



                if (status != null && !status.equals("")) {
                    IssueStatus enumString = IssueStatus.valueOf(status);
                    logger.info("Enum values:{}", enumString);
                    predicates.add(cb.equal(c.get("status"), enumString));
                }

                if (startDate != null && endDate!=null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    startDate = dateFormat.parse(from);
                    endDate = dateFormat.parse(to);
                    predicates.add(cb.between(c.get("createdOn"), startDate, endDate) );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new PageImpl<>(new ArrayList<>());
        }

        CriteriaQuery<IssueLog> baseQuery = null;
        CriteriaQuery<Long> qc = cb.createQuery(Long.class).orderBy();
        CriteriaQuery<Long> countQuery = null;
        if (predicates.size() > 0) {
            Predicate and = cb.and(predicates.toArray(new Predicate[0]));
            baseQuery = q.select(c).where(and).orderBy(cb.desc(c.get("id")));
            countQuery = qc.select(cb.count(qc.from(IssueLog.class))).where(and);
        } else {
            baseQuery = q.select(c);
            countQuery = qc.select(cb.count(qc.from(IssueLog.class)));
        }

        TypedQuery<IssueLog> query = entityManager.createQuery(baseQuery);
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<IssueLog> list = query.getResultList();
        return new PageImpl<>(list, pageable, count);
    }

    private String generateTicketID(){
        String tickId = new StringBuilder().append("T")
                .append(String.valueOf(new Date().getTime()).substring(1,10)).toString();
        return tickId;
    }
}
