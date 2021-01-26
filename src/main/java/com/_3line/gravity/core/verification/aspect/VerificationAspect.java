package com._3line.gravity.core.verification.aspect;


import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.usermgt.model.SystemUser;
import com._3line.gravity.core.usermgt.repository.ApplicationUserRepository;
import com._3line.gravity.core.utils.AppUtility;
import com._3line.gravity.core.verification.annotations.RequireApproval;
import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;
import com._3line.gravity.core.verification.exceptions.PendingVerificationException;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.core.verification.models.Verification;
import com._3line.gravity.core.verification.models.VerificationStatus;
import com._3line.gravity.core.verification.repository.VerificationRepository;
import com._3line.gravity.core.verification.service.VerifiableActionService;
import com._3line.gravity.core.verification.service.VerificationState;
import com._3line.gravity.core.verification.utility.PrettySerializer;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

@Aspect
@Component
public class VerificationAspect {


    @Autowired
    private SettingService settingService ;
    @Autowired
    private VerificationRepository verificationRepository;
    @Autowired
    private ApplicationUserRepository userRepository ;

    private Logger logger = LoggerFactory.getLogger(this.getClass()) ;
    @Autowired
    private ApplicationContext applicationContext ;
    @Autowired
    private ObjectMapper mapper ;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private VerificationState verificationState ;
    @Autowired
    private VerifiableActionService actionService ;

    @Autowired
    AgentService agentService;

    @Around("@annotation(com._3line.gravity.core.verification.annotations.RequireApproval)")
    @Transactional
    public Object sendForVerification(ProceedingJoinPoint joinPoint ) throws Throwable {

        SystemUser doneBy = null;
        String user =  "";
        try {
            user = SecurityContextHolder.getContext().getAuthentication().getName();
            logger.info("name {}", user);
            doneBy = userRepository.findByUserName(user);
            if(doneBy == null){
                Agents agent = agentService.fetchAgentById(user);
                if(agent!=null){
                    //assign approval to user for agents
                    doneBy = userRepository.findByUserName("aggregatorrep");
                    user = agent.getUsername();//doneBy.getUserName();
                    logger.info("agent username now assigned as {}", doneBy.getUserName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            doneBy = userRepository.findByUserName("test1");
        }
        logger.info(verificationState.usersInVerificationMode.toString());
        logger.info("user -> {}" , doneBy.getUserName());
        logger.info("CURRENT STATE   - >   {}" , verificationState.isInVerificationMode(doneBy.getUserName()));


        if(!verificationState.isInVerificationMode(doneBy.getUserName())) {
            logger.info("#########################  calling !!!");
            logger.info("method been called is {}  ", joinPoint.getSignature().getName());
            logger.info("class been called is {}  ", joinPoint.getSignature().toLongString());
            String code;
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            logger.info("method {}", signature.getName());
            Method method = signature.getMethod();
            Annotation[] methodAnnotations = method.getDeclaredAnnotations();

            for ( Annotation a: methodAnnotations) {
                if (a instanceof RequireApproval) {
                    logger.info("annotation found");
                    logger.info("code is {}", ((RequireApproval) a).code());
                    RequireApproval rv = ((RequireApproval) a);

                    Object[] args = joinPoint.getArgs();
                    if(actionService.isEnabled(rv.code())){
                        // action takes place here


                        if (args.length > 1) {
                            throw new VerificationException("Method to be registered for verification cannot have more than one argument");
                        }


                        if (args[0] instanceof AbstractVerifiableDto) {
                            AbstractVerifiableDto verifiableDto = (AbstractVerifiableDto) args[0];
                            logger.info("datat {}", args[0]);

                            if (Objects.nonNull(verifiableDto.getId())) {

                                Verification check = verificationRepository.findByCodeAndEntityIdAndStatus(rv.code(), verifiableDto.getId(), VerificationStatus.PENDING);
                                if (Objects.nonNull(check)) {
                                    throw new PendingVerificationException("Object has a pending approval");
                                }
                            }


                            try {

                                Verification verification = new Verification();
                                verification.setInitiatedBy(user);
                                verification.setInitiatedOn(new Date());
                                verification.setOriginalObject(mapper.writeValueAsString(args[0]));
                                verification.setOperation(rv.code());
                                verification.setDescription(rv.code());
                                verification.setCode(rv.code());
                                verification.setMethodName(joinPoint.getSignature().getName());
                                verification.setClassName(joinPoint.getSignature().getDeclaringType().getName());
                                verification.setEntityName(args[0].getClass().getName());
                                verification.setEntityPackage(args[0].getClass().getPackage().getName());
                                ObjectMapper prettyMapper = new ObjectMapper();

                                if (args[0] instanceof PrettySerializer) {
                                    JsonSerializer<Object> serializer = ((PrettySerializer) (args[0])).getSerializer();
                                    SimpleModule module = new SimpleModule();
                                    module.addSerializer(args[0].getClass(), serializer);
                                    prettyMapper.registerModule(module);
                                    logger.debug("Registering Pretty serializer for " + args[0].getClass().getName());
                                }

                                if (Objects.nonNull(verifiableDto.getId())) {

                                    Object originalEntity;

                                    if (!joinPoint.getSignature().getName().equalsIgnoreCase("raiseWalletDispute") && verifiableDto.getId()!=null) {
                                        originalEntity = entityManager.find(rv.entityType(), verifiableDto.getId());
                                        if(Objects.nonNull(originalEntity)){
                                            logger.info("found entity !!!!");
                                            logger.info("original is >>>>. {}", originalEntity.toString());
                                            verification.setBeforeObject(prettyMapper.writeValueAsString(originalEntity));
                                            verification.setEntityId(verifiableDto.getId());
                                        }

                                    }

                                }

                                verification.setAfterObject(prettyMapper.writeValueAsString(args[0]));
                                verification.setStatus(VerificationStatus.PENDING);
                                verificationRepository.save(verification);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            throw new VerificationException("Action {" + rv.code() + "} has gone for approval ensure the right authorities are notified");


                        } else {
                            throw new VerificationException("Object going for verification must extend AbstractVerifiableDto");
                        }

                    }else {
                        logger.info("{} is not enabled for approval",rv.code());
                        try {

                            Verification verification = new Verification();
                            verification.setInitiatedBy(user);
                            verification.setInitiatedOn(new Date());
                            verification.setOriginalObject(mapper.writeValueAsString(args[0]));
                            verification.setOperation(rv.code());
                            verification.setDescription(rv.code());
                            verification.setCode(rv.code());
                            verification.setMethodName(joinPoint.getSignature().getName());
                            try{
                                SystemUser appUser = userRepository.findByUserName(AppUtility.getCurrentUserName());
                                if(appUser!=null){
                                    verification.setIpInitiatedFrom(appUser.getLastIpAddress());
                                }else{
                                    verification.setIpInitiatedFrom(" ");
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                                verification.setIpInitiatedFrom(" ");
                            }

                            verification.setClassName(joinPoint.getSignature().getDeclaringType().getName());
                            verification.setEntityName(args[0].getClass().getName());
                            verification.setEntityPackage(args[0].getClass().getPackage().getName());
                            ObjectMapper prettyMapper = new ObjectMapper();

                            if (args[0] instanceof PrettySerializer) {
                                JsonSerializer<Object> serializer = ((PrettySerializer) (args[0])).getSerializer();
                                SimpleModule module = new SimpleModule();
                                module.addSerializer(args[0].getClass(), serializer);
                                prettyMapper.registerModule(module);
                                logger.debug("Registering Pretty serializer for " + args[0].getClass().getName());
                            }

//                            if (Objects.nonNull(verifiableDto.getId())) {
//
//                                Object originalEntity = entityManager.find(rv.entityType(), verifiableDto.getId());
//
//                                if (Objects.nonNull(originalEntity)) {
//                                    logger.info("found entity !!!!");
//                                    logger.info("original is >>>>. {}", originalEntity.toString());
//                                    verification.setBeforeObject(prettyMapper.writeValueAsString(originalEntity));
////                                    verification.setEntityId(verifiableDto.getId());
//                                }
//
//                            }

                            verification.setAfterObject(prettyMapper.writeValueAsString(args[0]));
                            verification.setStatus(VerificationStatus.APPROVED);
                            verificationRepository.save(verification);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return joinPoint.proceed() ;
                    }
                }
            }

            joinPoint.getTarget();

            return null;

        }else {
            logger.info("IN APPROVAL  MODE !");
            return joinPoint.proceed() ;
        }
    }
}
