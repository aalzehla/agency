package com._3line.gravity.api.notifications;

import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.issuelog.dtos.IssueLogDTO;
import com._3line.gravity.freedom.issuelog.service.IssueLogService;
import com._3line.gravity.freedom.notifications.dtos.NotificationDTO;
import com._3line.gravity.freedom.notifications.dtos.NotificationRequestDTO;
import com._3line.gravity.freedom.notifications.service.MobileNotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/gravity/api/notifications/")
public class NotificationsAPI {

    @Autowired
    MobileNotificationService notificationService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    IssueLogService issueLogService ;

    @RequestMapping(value="/",method = RequestMethod.POST)
    public ResponseEntity<?> fetchComplaints(@RequestBody NotificationRequestDTO requestDTO){
        Response response = new Response();
        PageRequest  pageRequest;

        if(requestDTO==null || requestDTO.getPage() < 0 || requestDTO.getSize()==0){
            pageRequest = PageRequest.of(0,10);
        }else{
            pageRequest = PageRequest.of(requestDTO.getPage(),requestDTO.getSize());
        }

        Page<NotificationDTO> notificationDTOS = notificationService.fetchAgentsNotifications(pageRequest);
        response.setRespCode("00");
        response.setRespDescription("success");
        Map<String,Object> respBody = new HashMap<>();
        respBody.put("hasNextRecord",notificationDTOS.hasNext());
        respBody.put("totalCount",notificationDTOS.getTotalElements());
        respBody.put("message",notificationDTOS.getContent());
        response.setRespBody(respBody);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value="/{notificationId}/read",method = RequestMethod.POST)
    public ResponseEntity<?> readNotification(@PathVariable("notificationId") Long notificationId){
        Response response = new Response();
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notificationId);
        notificationDTO = notificationService.readNotification(notificationDTO);

        if(notificationDTO!=null){
            response.setRespBody(notificationDTO);
            response.setRespCode("00");
            response.setRespDescription("success");
        }else{
            response.setRespCode("96");
            response.setRespDescription("failed");
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value="/{notificationId}/close",method = RequestMethod.POST)
    public ResponseEntity<?> closeNotification(@PathVariable("notificationId") Long notificationId){
        Response response = new Response();
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notificationId);
        notificationDTO  = notificationService.closeNotification(notificationDTO);
        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(notificationDTO);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
