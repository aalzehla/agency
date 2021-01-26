package com._3line.gravity.api.issuelog;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.dispute.dtos.DisputeLogDTO;
import com._3line.gravity.freedom.dispute.models.DisputeLog;
import com._3line.gravity.freedom.dispute.service.DisputeLogService;
import com._3line.gravity.freedom.issuelog.dtos.IssueLogDTO;
import com._3line.gravity.freedom.issuelog.dtos.IssueLogRequestDTO;
import com._3line.gravity.freedom.issuelog.models.IssueTypes;
import com._3line.gravity.freedom.issuelog.service.IssueLogService;
import com._3line.gravity.freedom.notifications.dtos.NotificationDTO;
import com._3line.gravity.freedom.notifications.service.MobileNotificationService;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.service.TransactionService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "Issue/Complaint API",description = "APIs regarding complaints")
@RequestMapping(value = "/gravity/api/complaints/")
public class IssueLogAPI {

    @Autowired
    IssueLogService issueLogService;

    @Autowired
    MobileNotificationService mobileNotificationService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    DisputeLogService  disputeLogService;

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value="/",method = RequestMethod.GET)
    public ResponseEntity<?> fetchComplaints(){
        Response response = new Response();
        List<IssueLogDTO> complaintsDTOList =  issueLogService.fetchComplaints();
        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(complaintsDTOList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value="/log",method = RequestMethod.POST)
    public ResponseEntity<?> logComplaints(@RequestBody IssueLogRequestDTO issueLogRequestDTO, HttpServletRequest httpServletRequest ){
        IssueLogDTO complaintsDTO=modelMapper.map(issueLogRequestDTO,IssueLogDTO.class);
        Response response = new Response();

        try{

            issueLogService.logIssue(complaintsDTO);
            NotificationDTO notificationDTO = setNotificationDTO(complaintsDTO.getComplaint(), complaintsDTO.getCategory(), complaintsDTO.getAgentEmail(), complaintsDTO.getAgentName(), complaintsDTO.getAgentPhone());
            mobileNotificationService.logNotification(notificationDTO);
            issueLogService.linkToNotification(complaintsDTO.getId(),notificationDTO.getId());
            response.setRespDescription("success");
            response.setRespCode("00");
            response.setRespBody(complaintsDTO);
        }catch(Exception e){
            e.printStackTrace();
            response.setRespDescription("failed");
            response.setRespCode("96");
        }


        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value="/log/dispute",method = RequestMethod.POST)
    public ResponseEntity<?> logDispute(@RequestBody DisputeLogDTO  disputeLogDTO){
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        Response response = new Response();

        try{


            if(disputeLogDTO.getTranId()!=null){
                disputeLogService.validateDisputeLog(disputeLogDTO.getTranId());
            }

            if(disputeLogDTO.getTranType().equalsIgnoreCase("WITHDRAWAL")){
                DisputeLog  complaints = modelMapper.map(disputeLogDTO,DisputeLog.class);

                Agents agent = jwtUtility.getCurrentAgent();

                Transactions transactions = transactionService.getTransactionBytranIdAndAgent(disputeLogDTO.getTranId(),agent.getId());

                if(transactions==null){
                    throw new GravityException("Can only raise dispute for transactions initiated by you");
                }

                IssueLogDTO issueLogDTO = getIssueLogDTO(disputeLogDTO, agent);

                issueLogService.logIssue(issueLogDTO);

                disputeLogService.logDisputeComplaint(complaints,issueLogDTO.getId());

                NotificationDTO notificationDTO = setNotificationDTO(disputeLogDTO.getComplaint(), IssueTypes.DISPUTE_RESOLUTION.toString(), agent.getEmail(), agent.getUsername(), agent.getPhoneNumber());

                mobileNotificationService.logNotification(notificationDTO);

                issueLogService.linkToNotification(issueLogDTO.getId(),notificationDTO.getId());

                response.setRespDescription("success");
                response.setRespCode("00");
                response.setRespBody(complaints);

            }else{
                IssueLogDTO complaintsDTO=new IssueLogDTO();
                complaintsDTO.setImage(disputeLogDTO.getImage());
                complaintsDTO.setCategory(IssueTypes.COMPLAINT.toString());
                complaintsDTO.setComplaint(disputeLogDTO.getComplaint());

                issueLogService.logIssue(complaintsDTO);
                NotificationDTO notificationDTO = setNotificationDTO(complaintsDTO.getComplaint(), complaintsDTO.getCategory(), complaintsDTO.getAgentEmail(), complaintsDTO.getAgentName(), complaintsDTO.getAgentPhone());
                mobileNotificationService.logNotification(notificationDTO);
                issueLogService.linkToNotification(complaintsDTO.getId(),notificationDTO.getId());


                response.setRespCode("00");
                response.setRespDescription("success");
                response.setRespBody(complaintsDTO);
            }


        }catch(GravityException e){
            e.printStackTrace();
            response.setRespDescription(e.getMessage());
            response.setRespCode("96");
        }catch(Exception e){
            e.printStackTrace();
            response.setRespDescription("System error occurred");
            response.setRespCode("96");
        }


        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    private NotificationDTO setNotificationDTO(String complaint, String s, String email, String username, String phoneNumber) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage(complaint);
        notificationDTO.setCategory(s);
        notificationDTO.setMessageType("Complaint");
        notificationDTO.setAgentEmail(email);
        notificationDTO.setAgentName(username);
        notificationDTO.setAgentPhone(phoneNumber);
        notificationDTO.setCreatedOn(new Date());
        return notificationDTO;
    }

    private IssueLogDTO getIssueLogDTO(@RequestBody DisputeLogDTO disputeLogDTO, Agents agent) {
        IssueLogDTO issueLogDTO = new IssueLogDTO();
        issueLogDTO.setComplaint(disputeLogDTO.getComplaint());
        issueLogDTO.setAgentPhone(agent.getPhoneNumber());
        issueLogDTO.setAgentEmail(agent.getEmail());
        issueLogDTO.setAgentName(agent.getUsername());
        issueLogDTO.setCategory(IssueTypes.DISPUTE_RESOLUTION.toString());
        issueLogDTO.setImage(disputeLogDTO.getImage());
        return issueLogDTO;
    }


    @RequestMapping(value="/{complaintId}/agents",method = RequestMethod.POST)
    public ResponseEntity<?> fetchAgentComplaint( @PathVariable("complaintId") Long complaintId ){
        Response response = new Response();
        Agents agent = jwtUtility.getCurrentAgent();
        IssueLogDTO complaintsDTO1 = issueLogService.fetchAgentComplaint(agent.getUsername(),complaintId);

        if(complaintsDTO1!=null){
            response.setRespBody(complaintsDTO1);
            response.setRespCode("00");
            response.setRespDescription("success");
        }else{
            response.setRespDescription("failed");
            response.setRespCode("96");
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
