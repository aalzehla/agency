package com._3line.gravity.core.sms.service.implementation;


import com._3line.gravity.core.sms.model.Sms;
import com._3line.gravity.core.sms.repository.SmsRepository;
import com._3line.gravity.core.sms.service.SmsService;
import com._3line.gravity.core.utils.HttpCustomClient;
import com._3line.gravity.core.utils.SMSClientProvider;
import com._3line.gravity.freedom.utility.PropertyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class IPIntgratedSMSImplementation implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SMSClientProvider.class);

    @Autowired
    private SmsRepository smsRepository;

    @Value("${ipint.sms.host}")
    private String host;

    @Value("${ipint.sms.path}")
    private String path;

    @Value("${ipint.sms.user}")
    private String user;

    @Value("${ipint.sms.password}")
    private String password;

    @Value("${ipint.sms.smsFrom}")
    private String smsFrom;

    @Autowired
    PropertyResource pr;


    @Autowired
    SMSClientProvider smsClientProvider ;

    private final ExecutorService executors = Executors.newFixedThreadPool(100);


    @Async
    @Override
    public void sendSms(String recepien, Map<String, Object> params, String templateName) {

        try {
            final String recepient = "234"+recepien.substring(1);
            executors.submit(() -> {
                Sms sms = new Sms();

                sms.setSendToPhone(recepient);
                sms.setLastSent(new Date());


                sms.setContent(pr.getV(templateName, "sms_messages.properties"));

                logger.info("sending  sms to -> .....", recepient);
                try {
                    logger.debug("template -> {}", templateName);
                    logger.debug("params- > {}", params);
                    logger.debug("send  sms -> {}", sms.toString());


                    String recipient = recepient;
                    String message = sms.getContent();

                    String senderId = smsFrom;
//                    message = URLEncoder.encode(message, "UTF-8");
                    senderId = URLEncoder.encode(senderId, "UTF-8");

                    String query= String.format("u=%s&p=%s&s=%s&r=t&f=f&d=%s&t=%s",user,password,senderId,recipient,message);
                    System.out.println("original query:: "+query);

                    String url =String.format("%s/%s?%s", host,path,query);

//                    System.out.println(url);

                    HttpCustomClient httpCustomClient = new HttpCustomClient(url,null,"GET",false,null);

//
                    httpCustomClient.sendRequest();

//                    smsClientProvider.sendMessage(sms.getSendToPhone(), sms.getContent());

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

    @Async
    @Override
    public void sendPlainSms(String recepient, String message) {
        try {
            executors.submit(() -> {

                final String phoneNumber = "234"+recepient.substring(1);
                Sms sms = new Sms();
                sms.setSendToPhone(phoneNumber);
                sms.setLastSent(new Date());
                sms.setContent(message);

                try {


                    String recipient = phoneNumber;
                    String finalMessage = URLEncoder.encode(message, "UTF-8");
                    String senderId = URLEncoder.encode(smsFrom, "UTF-8");
                    user = URLEncoder.encode(user, "UTF-8");
                    String query= String.format("u=%s&p=%s&s=%s&r=t&f=f&d=%s&t=%s",user,password,senderId,recipient,finalMessage);

                    String url =String.format("%s/%s?%s", host,path,query);
                    System.out.println(url);

                    HttpCustomClient httpCustomClient = new HttpCustomClient(url,null,"GET",false,null);

//
                    httpCustomClient.sendRequest();

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
}
