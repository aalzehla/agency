package com._3line.gravity.api.config.controlleradvice;


import com._3line.gravity.core.email.service.MailService;
import com._3line.gravity.core.exceptions.AgencyBankingException;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.usermgt.exception.UserNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorControlller {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${dev.members.mail}")
    private String devMembersMails;

    @Autowired
    private MailService mailService;

    @ExceptionHandler({ GravityException.class, UserNotFoundException.class})
    public final ResponseEntity<?> handleException(Exception ex, WebRequest request) {

        if (ex instanceof UserNotFoundException) {
            Response response = new Response();
            response.setRespCode("96");
            response.setRespDescription(ex.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        } else if (ex instanceof GravityException) {
            Response response = new Response();
            response.setRespCode("96");
            response.setRespDescription(ex.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        } else if (ex instanceof AgencyBankingException) {
            Response response = new Response();
            response.setRespCode("96");
            response.setRespDescription(ex.getMessage());
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        } else {

            Response response = new Response();
            response.setRespCode("99");
            response.setRespDescription(ex.getMessage());

            logger.info("About sending mail");
            sendNotification(response);
            logger.info("Mail sending should be completed now!!");

            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendNotification(Response errorDetails) {
        String time = String.valueOf(new Date());
        String statusCode = errorDetails.getRespCode();
        String error = errorDetails.getRespDescription();
        String exception = errorDetails.getRespDescription();//(String) errorDetails.get("exception");
        String message = errorDetails.getRespDescription();//errorDetails.get("message").toString();
        String trace = errorDetails.getRespDescription();//(String) errorDetails.get("trace");
        String path = errorDetails.getRespDescription();//(String) errorDetails.get("path");

//        if (!"405".equals(statusCode)) {
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("Time: " + time + "\n")
                    .append("Path: " + path + "\n")
                    .append("Status Code: " + statusCode + "\n")
                    .append("Error: " + error + "\n")
                    .append("Exception: " + exception + "\n")
                    .append("Message: " + message + "\n")
                    .append("Trace: " + trace + "\n");
            Map<String, Object> params = new HashMap<>();
            params.put("Time", time);
            params.put("Path", path);
            params.put("StatusCode", statusCode);
            params.put("Error", error);
            params.put("Exception:",exception);
            params.put("Message:",message);
            params.put("Trace:",trace);

            if(devMembersMails!=null) {
                String[] mailAddresses = StringUtils.split(devMembersMails, ",");
                mailService.sendMail(error,"softwaredev@3lineng.com",mailAddresses,params,"error_mail");

            }
//        }
    }

}
