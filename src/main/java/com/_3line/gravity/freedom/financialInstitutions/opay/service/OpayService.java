package com._3line.gravity.freedom.financialInstitutions.opay.service;


import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.financialInstitutions.dtos.GravityWithdrawalRequest;
import com._3line.gravity.freedom.utility.RestTemplateHttp;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpayService {

    @Autowired
    RestTemplateHttp restTemplateHttp ;
    @Value("${opayservice.url}")
    private String opayEndpoint;
    @Value("${opayservice.publicKey}")
    private String publicKey;
    @Value("${opayservice.privateKey}")
    private String privateKey;

    private String accessCode;

    private String accessToken;

    private Gson gson = new Gson();

    private Logger logger = LoggerFactory.getLogger(this.getClass()) ;

    public String createTransaction(GravityWithdrawalRequest request){
        resetKeys();

        Map reqeustBody = new HashMap();
        reqeustBody.put("amount", request.getAmount());
        reqeustBody.put("cardCVC",request.getCardCVC());
        reqeustBody.put("instrumentType","card");
        reqeustBody.put("publicKey",publicKey);
        reqeustBody.put("cardNumber",request.getCardNumber());
        reqeustBody.put("currency","NGN");
        reqeustBody.put("tokenize",false);
        reqeustBody.put("cardDateMonth",request.getCardDateMonth());
        reqeustBody.put("cardDateYear",request.getCardDateYear());
        reqeustBody.put("countryCode","NG");
        Map body = new HashMap();
        body.put("input", reqeustBody) ;

        Map headers = new HashMap<String, Object>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer "+accessToken);
        String data = gson.toJson(body);
        logger.info("about getting start transaction... body {}", data);
        String response = restTemplateHttp.post(opayEndpoint + "/gateway/create", "Application/json", data, headers);
        logger.info("response from  opay {}", response);
        HashMap<String, Object> requestMap ;

        requestMap = gson.fromJson(response, HashMap.class);
        Map<String, Object> result = (Map) requestMap.get("data");

        Map gateWayResponse ;
        gateWayResponse = (Map)result.get("gatewayCreate") ;

        String status = (String) gateWayResponse.get("status") ;

        String token  = (String) gateWayResponse.get("token") ;

        if(status.equals("input-pin")){
            logger.info("user needs to enter card pin");
            return token ;
        }


        return token;
    }

    public String input_pin(String token , String pin){
        Map body = new HashMap();
        body.put("token", token) ;
        body.put("pin", pin) ;

        Map headers = new HashMap<String, Object>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer "+accessToken);
        String data = gson.toJson(body);
        logger.info("about getting pin transaction... body {}", data);
        String response = restTemplateHttp.post(opayEndpoint + "/gateway/input-pin", "Application/json", data, headers);
        logger.info("response from  opay {}", response);
        HashMap<String, Object> requestMap ;

        requestMap = gson.fromJson(response, HashMap.class);
        Map<String, Object> result = (Map) requestMap.get("data");
        Map gateWayResponse ;
        gateWayResponse = (Map)result.get("gatewayInputPIN") ;

        String status = (String) gateWayResponse.get("status") ;

        String t  = (String) gateWayResponse.get("token") ;


        return status ;
    }

    public String input_otp(String token , String otp){
        Map body = new HashMap();
        body.put("token", token) ;
        body.put("otp", otp) ;

        Map headers = new HashMap<String, Object>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer "+accessToken);
        String data = gson.toJson(body);
        logger.info("about getting otp transaction... body {}", data);
        String response = restTemplateHttp.post(opayEndpoint + "/gateway/input-otp", "Application/json", data, headers);
        logger.info("response from  opay {}", response);
        HashMap<String, Object> requestMap ;

        requestMap = gson.fromJson(response, HashMap.class);
        Map<String, Object> result = (Map) requestMap.get("data");
        Map gateWayResponse ;
        gateWayResponse = (Map)result.get("gatewayInputOTP") ;

        String status = (String) gateWayResponse.get("status") ;

        String t  = (String) gateWayResponse.get("5b5b06e58178670001d8fae2") ;

        if(!status.equals("successful")){
            String error = (String) gateWayResponse.get("failureReason");
            throw new GravityException(error) ;
        }

        return status ;
    }

    public synchronized void resetKeys(){

        /*
          * Setup AccessCode Using Primary key
         */

        {
            Map headers = new HashMap<String, Object>();
            headers.put("Content-Type", "application/json");

            Map body = new HashMap();
            body.put("publicKey", publicKey);

            String data = gson.toJson(body);
            logger.info("about getting accessCode body {}", data);
            String response = restTemplateHttp.post(opayEndpoint + "/access/code", "Application/json", data, headers);
            logger.info("response from  opay {}", response);

            HashMap<String, Object> requestMap ;

            requestMap = gson.fromJson(response, HashMap.class);
            Map<String, Object> result = (Map) requestMap.get("data");
            accessCode = (String) result.get("accessCode");
            logger.info("accessCode now set {}", accessCode);

        }

        /*
        * Setup accessToken Using accessCode
         */
        {

            Map headers = new HashMap<String, Object>();
            headers.put("Content-Type", "application/json");

            Map body = new HashMap();
            body.put("privateKey", privateKey);
            body.put("accessCode", accessCode);

            String data = gson.toJson(body);
            logger.info("about getting accessToken body {}", data);
            String response = restTemplateHttp.post(opayEndpoint + "/access/token", "Application/json", data, headers);
            logger.info("response from  opay {}", response);
            HashMap<String, Object> requestMap ;

            requestMap = gson.fromJson(response, HashMap.class);
            Map<String, Object> result = (Map) requestMap.get("data");
            Map<String, Object> token = (Map)  result.get("accessToken");
            accessToken = (String) token.get("value");
            logger.info("accessToken now set {}", accessToken);


        }

    }
}
