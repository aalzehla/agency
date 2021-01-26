package com._3line.gravity.freedom.notifications.service;

import com._3line.gravity.api.notifications.ImageTypes;
import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.email.service.MailService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.usermgt.service.ApplicationUserService;
import com._3line.gravity.core.utils.AppUtility;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.dispute.models.DisputeLog;
import com._3line.gravity.freedom.issuelog.models.IssueLog;
import com._3line.gravity.freedom.issuelog.service.IssueLogService;
import com._3line.gravity.freedom.notifications.dtos.ComplaintDTO;
import com._3line.gravity.freedom.notifications.dtos.DisputeResponseDTO;
import com._3line.gravity.freedom.notifications.dtos.NotificationContentDTO;
import com._3line.gravity.freedom.notifications.dtos.NotificationDTO;
import com._3line.gravity.freedom.notifications.models.Notification;
import com._3line.gravity.freedom.notifications.repository.NotificationRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class MobileNotificationService {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private MailService mailService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SettingService settingService;

    @Autowired
    private AgentService agentService;

    @Autowired
    ApplicationUserService userService;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    IssueLogService  issueLogService;


    private final Locale locale = LocaleContextHolder.getLocale() ;


    Logger logger = LoggerFactory.getLogger(this.getClass());

    public Notification getNotificationById(Long id){

        return notificationRepository.findById(id).orElse(null);
    }


    public Page<NotificationDTO> fetchAgentsNotifications(Pageable pageable){

        List<NotificationDTO>  notificationDTOS = new ArrayList<>();

        Agents agents = jwtUtility.getCurrentAgent();

        Page<Notification> notifications = notificationRepository.findByAgentNameAndIsClosedOrderByCreatedOnDesc(agents.getUsername(),false,pageable);
        List<Notification> notificationList = notifications.getContent();
        for(int a=0;a<notificationList.size();a++){
            Notification  notification = notificationList.get(a);
            NotificationDTO notificationDTO = modelMapper.map(notification,NotificationDTO.class);
            if(notificationDTO.getMessageType().equalsIgnoreCase("complaint")){

                IssueLog issueLog = issueLogService.findIssueLogByNotificationId(notificationDTO.getId());
                if(issueLog!=null){
                    notificationDTO.setMessage(issueLog.getComment());
                    if(issueLog.getCategory().equals("DISPUTE_RESOLUTION")){
                        DisputeResponseDTO disputeDto = new DisputeResponseDTO();

                        DisputeLog disputeLog = issueLog.getDisputeLog();

                        disputeDto.setTranAmount(disputeLog.getTranAmount());
                        disputeDto.setTranId(disputeLog.getTranId());
                        disputeDto.setTranType(disputeLog.getTranType());
                        disputeDto.setTranDate(disputeLog.getTranDate());

                        disputeDto.setComplaint(issueLog.getComplaint());
                        disputeDto.setStatus(issueLog.getStatus().toString());
                        disputeDto.setTreatedBy(issueLog.getTreatedBy());
                        disputeDto.setTreatedOn(issueLog.getTreatedDate());
                        notificationDTO.setDisputeDTO(disputeDto);
                    }else{
                        ComplaintDTO complaintDTO = new ComplaintDTO();
                        complaintDTO.setComplaint(issueLog.getComplaint());
                        complaintDTO.setStatus(issueLog.getStatus().toString());
                        complaintDTO.setTreatedBy(issueLog.getTreatedBy());
                        complaintDTO.setTreatedOn(issueLog.getTreatedDate());
                        notificationDTO.setComplaintDto(complaintDTO);
                    }


                    notificationDTO.setImage(issueLog.getImage());
                    notificationDTO.setTicketId(issueLog.getTicketId());
                    if(issueLog.getImage()!=null && issueLog.getImage().contains("core")){
                        notificationDTO.setImage_type(ImageTypes.URL);
                    }
                }
            }else{
                NotificationContentDTO contentDTO = new NotificationContentDTO();
                contentDTO.setMessage(notification.getMessage());
                contentDTO.setTopic(notification.getTopic());
                notificationDTO.setNotificationDto(contentDTO);
                notificationDTO.setMessage(notification.getMessage());
            }
            notificationDTOS.add(notificationDTO);
        }

        return new PageImpl<>(notificationDTOS,pageable,notifications.getTotalElements());
    }


    public NotificationDTO logNotification(NotificationDTO notificationDTO){

        logger.info("logging notification {}", notificationDTO);

        Notification notification = modelMapper.map(notificationDTO, Notification.class);

        notification.setIsRead(false);
        notification.setLoggedOn(new Date());
        notification.setTopic(notification.getCategory());
        notification.setSentBy(AppUtility.getCurrentUserName());

        notification = notificationRepository.save(notification);
        notificationDTO.setId(notification.getId());

        return notificationDTO;
    }

    public NotificationDTO readNotification(NotificationDTO notificationDTO){

        Agents agents = jwtUtility.getCurrentAgent();

        Notification notification = notificationRepository.findByAgentNameAndIsClosedAndId(agents.getUsername(),false,notificationDTO.getId());

        if(notification==null){
            throw new GravityException("Invalid Id entered for Notification");
        }
        notification.setIsRead(true);
        notificationRepository.save(notification);

        notificationDTO = modelMapper.map(notification, NotificationDTO.class);
        return notificationDTO;
    }

    public NotificationDTO markNotificationAsUnread(NotificationDTO notificationDTO){

//        Agents agents = jwtUtility.getCurrentAgent();

        Notification notification = notificationRepository.findByAgentNameAndIsClosedAndId(notificationDTO.getAgentName(),false,notificationDTO.getId());

        if(notification==null){
            throw new GravityException("Invalid Id entered for Notification");
        }
        notification.setIsRead(false);
        notificationRepository.save(notification);

        notificationDTO = modelMapper.map(notification, NotificationDTO.class);
        return notificationDTO;
    }

    public NotificationDTO closeNotification(NotificationDTO notificationDTO){

        Agents agents = jwtUtility.getCurrentAgent();

        Notification notification = notificationRepository.findByAgentNameAndIsClosedAndId(agents.getUsername(),false,notificationDTO.getId());
        if(notification==null){
            throw new GravityException("Invalid Id entered for Notification");
        }
        notification.setIsClosed(true);
        notification.setClosedDate(new Date());
        notificationRepository.save(notification);

        notificationDTO = modelMapper.map(notification, NotificationDTO.class);
        return notificationDTO;
    }


//    public String logIssue(IssueLogDTO logDTO) {
//        logger.info("logging issue {}", logDTO.toString());
//
//       Agents agents= validateAgent(logDTO.getAgentName());
//        IssueLog issueLog = modelMapper.map(logDTO, IssueLog.class);
//        issueLog.setStatus(IssueStatus.PENDING);
//        issueLog.setRead(false);
//        issueLog.setAgentEmail(agents.getEmail());
//        notificationRepository.save(issueLog);
//
//        SettingDTO settingDTO = settingService.getSettingByCode("ISSUE_LOG_EMAIL");
//        Map<String , Object> params = new HashMap<>();
//        params.put("agent", issueLog.getAgentName());
//        params.put("email", issueLog.getAgentEmail());
//        params.put("complaint", issueLog.getComplaint());
//        mailService.sendMail("Complaint from Agent !", settingDTO.getValue(),null,params,"logged_issue");
//
//        return messageSource.getMessage("issuelog.add.success", null, locale);
//    }
//
//
//    public String logResolvedDispute(IssueLogDTO logDTO) {
//
//        logger.info("logging issue {}", logDTO.toString());
//
//        Agents agents= validateAgent(logDTO.getAgentName());
//        IssueLog issueLog = modelMapper.map(logDTO, IssueLog.class);
//
//        issueLog.setStatus(IssueStatus.RESOLVED);
//        issueLog.setRead(false);
//        issueLog.setAgentEmail(agents.getEmail());
//        issueLog.setComment(logDTO.getComment());
//        issueLog.setLoggedOn(new Date());
//        issueLog.setTreatedDate(new Date());
//        issueLog.setTreatedBy(AppUtility.getCurrentUserName());
//
//        notificationRepository.save(issueLog);
//
//        return messageSource.getMessage("issuelog.add.success", null, locale);
//    }
//
//    public IssueLog findIssueLog(Long id){
//        IssueLog issueLog = notificationRepository.findOne(id);
//        return issueLog;
//    }
//
//    public String updateCommentAndStatus(IssueLogDTO issueLogDTO){
//        String response = "";
//        logger.info("logging issue update comment {}", issueLogDTO.getComment());
//        logger.info("comment id: {}",issueLogDTO.getId());
//        try{
//            IssueLog issueLog = notificationRepository.findOne(issueLogDTO.getId());
//            issueLog.setComment(issueLogDTO.getComment());
//            issueLog.setStatus(IssueStatus.RESOLVED);
//            issueLog.setTreatedDate(new Date());
//            issueLog.setTreatedBy(AppUtility.getCurrentUserName());
//
//             notificationRepository.save(issueLog);
//             response = "Successful";
//
//            SettingDTO settingDTO = settingService.getSettingByCode("ISSUE_LOG_EMAIL");
//            Map<String , Object> params = new HashMap<>();
//            params.put("agent", issueLog.getAgentName());
//            params.put("email", issueLog.getAgentEmail());
//            params.put("complaint", issueLog.getComplaint());
//            params.put("comment", issueLog.getComment());
//            mailService.sendMail("Update on your complaint !", issueLog.getAgentEmail(),(String[]) Arrays.asList(settingDTO.getValue(),"").toArray(),null,"updated_issue");
//
//        }
//        catch (Exception e){
//            logger.error("This the error {} ", e.getCause().getMessage());
//            response = e.getCause().getMessage();
//        }
//        return response;
//    }
//
//    public Page<IssueLogDTO> getTransactions(Pageable pageDetails) throws GravityException {
//
//        Page<IssueLog> page = notificationRepository.findAllByOrderByIdDesc(pageDetails);
//        List<IssueLogDTO> dtOs = convertEntitiesToDTOs(page.getContent());
//        long t = page.getTotalElements();
//        Page<IssueLogDTO> pageImpl = new PageImpl<IssueLogDTO>(dtOs, pageDetails, t);
//        return pageImpl;
//    }
//
//
//    public Page<IssueLogDTO> findTransaction(String pattern, Pageable pageDetails) {
//
//        Page<IssueLog> page = notificationRepository.findByStatus(IssueStatus.valueOf(pattern), pageDetails);
//        List<IssueLogDTO> dtOs = convertEntitiesToDTOs(page.getContent());
//        long t = page.getTotalElements();
//        return new PageImpl<>(dtOs, pageDetails, t);
//
//    }
//
//
//    public List<IssueLogDTO> convertEntitiesToDTOs(List<IssueLog> IssueLogs){
//        List<IssueLogDTO> IssueLogDTOList = new ArrayList<>();
//        for(com._3line.gravity.freedom.notifications.models.issuelog.models.IssueLog IssueLog: IssueLogs){
//            IssueLogDTO IssueLogDTO = convertEntityToDTO(IssueLog);
//            IssueLogDTOList.add(IssueLogDTO);
//        }
//        return IssueLogDTOList;
//    }
//
//
//    public IssueLogDTO convertEntityToDTO(IssueLog IssueLog){
//        IssueLogDTO IssueLogDTO = modelMapper.map(IssueLog, com._3line.gravity.freedom.notifications.models.issuelog.dtos.IssueLogDTO.class);
//        IssueLogDTO.setStatus(IssueLog.getStatus().name());
//        IssueLogDTO.setLoggedOn(IssueLog.getCreatedOn());
//        return IssueLogDTO;
//    }
//
//    public IssueLog convertDTOToEntity(IssueLogDTO IssueLogDTO){
//        IssueLog IssueLog = modelMapper.map(IssueLogDTO, com._3line.gravity.freedom.notifications.models.issuelog.models.IssueLog.class);
//        IssueLog.setLoggedOn(IssueLog.getCreatedOn());
//        return IssueLog;
//    }
//
//
//    public List<IssueLogDTO> fetchComplaints() {
//        List<IssueLogDTO> complaintsDTOS = new ArrayList<>();
//        notificationRepository.findAll().forEach(complaint -> {
//            IssueLogDTO complaintsDTO = convertEntityToDTO(complaint);
//            complaintsDTOS.add(complaintsDTO);
//        });
//        return complaintsDTOS;
//    }
//    public IssueLogDTO closeComplaint(IssueLogDTO complaintsDTO) {
//        validateAgent(complaintsDTO.getAgentName());
//        IssueLog complaint = notificationRepository.findByAgentNameAndIdAndIsRead
//                (complaintsDTO.getAgentName(),Long.valueOf(complaintsDTO.getId()),false);
//
//        if(complaint!=null){
//            complaint.setRead(true);
//            complaint.setStatus(IssueStatus.RESOLVED);
//            notificationRepository.save(complaint);
//        }else{
//            throw new GravityException("Record Not Found");
//        }
//        return convertEntityToDTO(complaint);
//    }
//    public IssueLogDTO fetchAgentUnreadComplaint(String agentName, Long complaintId) {
//        validateAgent(agentName);
//        IssueLog complaint = notificationRepository.findByAgentNameAndIdAndIsRead(agentName,complaintId,false);
//        IssueLogDTO complaintsDTO = convertEntityToDTO(complaint);
//
//        return complaintsDTO;
//    }
//    public List<IssueLogDTO> fetchAgentUnreadComplaints(String agentName) {
//        validateAgent(agentName);
//        List<IssueLogDTO> complaintsDTOS = new ArrayList<>();
//        notificationRepository.findByAgentNameAndIsRead(agentName,false).forEach(complaint -> {
//            IssueLogDTO complaintsDTO = convertEntityToDTO(complaint);
//            complaintsDTOS.add(complaintsDTO);
//        });
//        return complaintsDTOS;
//    }
//
//    private Agents validateAgent(String agentName){
//        Agents agents=agentService.fetchAgentByAgentName(agentName);
//        if(agents == null){
//            throw new GravityException("Invalid Agent username passed");
//        }
//        return agents;
//    }
//
//    public Page<IssueLogDTO> findUsingPattern(String status, Pageable pageable) {
//        Page<IssueLog> page = notificationRepository.findUsingPattern(status, pageable);
//        List<IssueLogDTO> dtOs = convertEntitiesToDTOs(page.getContent());
//        long t = page.getTotalElements();
//        return new PageImpl<>(dtOs, pageable, t);
//    }


}
