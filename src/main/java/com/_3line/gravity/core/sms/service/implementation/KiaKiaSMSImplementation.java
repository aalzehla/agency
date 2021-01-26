package com._3line.gravity.core.sms.service.implementation;


import com._3line.gravity.core.email.repository.MailRepository;
import com._3line.gravity.core.sms.model.Sms;
import com._3line.gravity.core.sms.repository.SmsRepository;
import com._3line.gravity.core.sms.service.SmsService;
import com._3line.gravity.core.utils.SMSClientProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class KiaKiaSMSImplementation implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SMSClientProvider.class);

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private TemplateEngine templateEngine;


    @Value("${sms.from}")
    private String smsFrom ;

    @Autowired
    SMSClientProvider smsClientProvider ;

    private final ExecutorService executors = Executors.newFixedThreadPool(100);


    @Async
    @Override
    public void sendSms(String recepient, Map<String, Object> params, String templateName) {

        try {

            executors.submit(() -> {
                Sms sms = new Sms();
                sms.setSendToPhone(recepient);
                sms.setLastSent(new Date());

                IContext context = new Context();
                ((Context) context).setVariables(params);
                sms.setContent(templateEngine.process("/sms/" + templateName, context));
                logger.info("sending sms to -> .....", recepient);
                try {
                    logger.debug("template -> {}", templateName);
                    logger.debug("params- > {}", params);
                    logger.debug("send sms -> {}", sms.toString());

                    smsClientProvider.sendMessage(sms.getSendToPhone(), sms.getContent());
                    sms.isSent = true;
                    smsRepository.save(sms);
                    logger.info("sent-> ...");
                } catch (Exception e) {
                    e.printStackTrace();
                    sms.setSent(false);
                    sms.setFailureReason(e.getMessage());
                    smsRepository.save(sms);
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void sendPlainSms(String recepient, String message) {

    }
}
