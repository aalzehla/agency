package com._3line.gravity.core.utils;


import com._3line.gravity.core.email.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sylvester on 17/10/2019.
 */
@Component
public class SmartShutDownAndStartUp {

    private MailService mailService;

    @Value("${host.ip}")
    private String hostIp;

    @Value("${dev.members.mail}")
    private String sendTo;

    @Autowired
    public SmartShutDownAndStartUp(MailService mailService) {
        this.mailService = mailService;
    }

    public SmartShutDownAndStartUp() {
    }

    @PostConstruct
    public void init() {
//        sendStartUpMail();
    }

    @PreDestroy
    public void destroy() {
        sendShutDownMail();
    }

    public void sendStartUpMail(){
        sendMail("APP_STARTUP", "Freedom Network Application Start Up");
    }

    public void sendShutDownMail(){
        sendMail("APP_SHUTDOWN", "Freedom Network Application Shut Down!");
    }

    private void sendMail(String code,String subject) {

        Map<String, Object> params = new HashMap<>();
        params.put("name", "All");
        params.put("date", new Date());
        params.put("ip", hostIp);
        params.put("code", code);
        mailService.sendMail(subject, sendTo,null,params,"smart_shut_down");
    }


}
