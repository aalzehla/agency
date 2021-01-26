package com._3line.gravity.freedom.utility;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class _3lineSMSService {

    Logger logger = LoggerFactory.getLogger(this.getClass()) ;

    public void SendSms(String message , String... phones){
        RestTemplate restTemplate = new RestTemplate();

        SMSRequest smsRequest = new SMSRequest();
        smsRequest.setMessageId("");
        smsRequest.setMessage(message);
        smsRequest.setTitle("FREEDOM CARD");
        List<String> p = Arrays.asList(phones);
        ArrayList<String> ar = new ArrayList<>() ;
        for (String s:p) {
            ar.add(s) ;
        }
        smsRequest.setMobileNumbers(ar);

        logger.info("message is {}" , smsRequest.toString());
        String response = restTemplate.postForObject("http://10.2.2.36:9181/sms3line/service/sms",smsRequest,String.class) ;

        logger.info("response {}" , response);




    }
}
