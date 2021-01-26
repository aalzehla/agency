package com._3line.gravity.freedom.financialInstitutions._9PSB.service.implementation;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.setting.dto.SettingDTO;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.utils.HttpCustomClient;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.financialInstitutions._9PSB.dto.*;
import com._3line.gravity.freedom.financialInstitutions._9PSB.service._9psbService;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class _9psbServiceImpl implements _9psbService {

    @Autowired
    SettingService settingService;

    HttpCustomClient httpCustomClient;

    HashMap<String,String> headerContents = new HashMap<>();

    @Value("${_9psb.url}")
    private String baseUrl;

    @Value("${_9psb.login.path}")
    private String loginPath;

    @Value("${_9psb.validate_code.deposit}")
    private String validateDepositCode;

    @Value("${_9psb.deposit.with_code}")
    private String depositWithCode;


    @Value("${_9psb.validate_code.withdrawal}")
    private String validateWithdrawalCode;

    @Value("${_9psb.client.secret}")
    private String clientSecret;

    @Value("${_9psb.client.key}")
    private String clientKey;

    private static String token;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ValidateCodeResponseDTO validateCode(ValidateCodeRequestDTO requestDTO) {
        setToken();
        httpCustomClient = new HttpCustomClient();

        if(requestDTO.getTranType().equals("DEPOSIT")){
            httpCustomClient.setReuestURL(baseUrl+validateDepositCode);
        }else{
            httpCustomClient.setReuestURL(baseUrl+validateWithdrawalCode);
        }
        String reqPayload = new Gson().toJson(requestDTO);
        headerContents.put("Content-Type","application/json");
        headerContents.put("Authorization","Bearer "+token);
        httpCustomClient.setHeaderToken(headerContents);
        httpCustomClient.setHasHeaderToken(true);
        httpCustomClient.setRequestType("POST");
        httpCustomClient.setRequestBody(reqPayload);
        String respPayload = httpCustomClient.sendRequest();
        ValidateCodeResponseDTO codeResponseDTO = new Gson().fromJson(respPayload,ValidateCodeResponseDTO.class);
        if(codeResponseDTO.getAmount() ==null){
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) new JSONParser().parse(respPayload);
                String errorMessage = jsonObject.get("message").toString();
                throw new GravityException(errorMessage);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return codeResponseDTO;
    }

    @Override
    public DepositResponseDTO depositWithCode(DepositRequestDTO requestDTO) {

        DepositResponseDTO codeResponseDTO = modelMapper.map(requestDTO,DepositResponseDTO.class);

        Agents agent  = jwtUtility.getCurrentAgent();

        boolean isValidPin = jwtUtility.isValidAgentPin(requestDTO.getPin(),agent);
        if(!isValidPin){
            throw new GravityException("Invalid Agent PIN");
        }

        setToken();
        httpCustomClient = new HttpCustomClient();
        httpCustomClient.setReuestURL(baseUrl+depositWithCode);

        String reqPayload = "";
        String payload = new Gson().toJson(requestDTO);
        try {
            JSONObject jsonObjet = (JSONObject) new JSONParser().parse(payload);
            jsonObjet.remove("requestId");
            jsonObjet.remove("customerPhone");
            jsonObjet.remove("depositorPhone");
            jsonObjet.remove("pin");

            jsonObjet.put("customerMobileNumber",requestDTO.getCustomerPhone());
            jsonObjet.put("depositorPhoneNumber",requestDTO.getDepositorPhone());
            jsonObjet.put("AgentId",agent.getAgentId());
            jsonObjet.put("city",agent.getCity());
            jsonObjet.put("state",agent.getState());
            jsonObjet.put("address",agent.getAddress());

            reqPayload = jsonObjet.toString();
            System.out.println("sending::  "+reqPayload);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        headerContents.put("Content-Type","application/json");
        headerContents.put("Authorization","Bearer "+token);
        httpCustomClient.setHeaderToken(headerContents);
        httpCustomClient.setHasHeaderToken(true);
        httpCustomClient.setRequestType("POST");
        httpCustomClient.setRequestBody(reqPayload);
        String respPayload = httpCustomClient.sendRequest();
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(respPayload);
            if(jsonObject.get("transactionRef") != null ){
                codeResponseDTO.setPin(null);
                codeResponseDTO.setTransactionRef("txn-"+new Date().getTime());
                codeResponseDTO.setTranDate(new Date());
            }else{
                String error = jsonObject.get("message").toString();
                throw new GravityException(error);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return codeResponseDTO;
    }

    @Override
    public DepositWithoutCodeRespDTO depositWithoutCode(DepositWithoutCodeReqDTO requestDTO) {

        DepositWithoutCodeRespDTO codeResponseDTO = modelMapper.map(requestDTO,DepositWithoutCodeRespDTO.class);

        Agents agent  = jwtUtility.getCurrentAgent();

        boolean isValidPin = jwtUtility.isValidAgentPin(requestDTO.getPin(),agent);
        if(!isValidPin){
            throw new GravityException("Invalid Agent PIN");
        }

        setToken();
        httpCustomClient = new HttpCustomClient();
        httpCustomClient.setReuestURL(baseUrl+depositWithCode);

        String reqPayload = "";
        String payload = new Gson().toJson(requestDTO);
        try {
            JSONObject jsonObjet = (JSONObject) new JSONParser().parse(payload);
            jsonObjet.remove("requestId");
            jsonObjet.remove("customerPhone");
            jsonObjet.remove("depositorPhone");
            jsonObjet.remove("pin");
            jsonObjet.remove("walletNumber");


            jsonObjet.put("customerWalletNumber",requestDTO.getWalletNumber());
            jsonObjet.put("customerMobileNumber",requestDTO.getCustomerPhone());
            jsonObjet.put("depositorPhoneNumber",requestDTO.getDepositorPhone());
            jsonObjet.put("AgentId",agent.getAgentId());
            jsonObjet.put("city",agent.getCity());
            jsonObjet.put("state",agent.getState());
            jsonObjet.put("address",agent.getAddress());

            reqPayload = jsonObjet.toString();
            System.out.println("sending::  "+reqPayload);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        headerContents.put("Content-Type","application/json");
        headerContents.put("Authorization","Bearer "+token);
        httpCustomClient.setHeaderToken(headerContents);
        httpCustomClient.setHasHeaderToken(true);
        httpCustomClient.setRequestType("POST");
        httpCustomClient.setRequestBody(reqPayload);
        String respPayload = httpCustomClient.sendRequest();
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(respPayload);
            if(jsonObject.get("transactionRef") == null ){
                String error = jsonObject.get("message").toString();
                throw new GravityException(error);
            }else{
                codeResponseDTO.setPin(null);
                codeResponseDTO.setTransactionRef("txn-"+new Date().getTime());
                codeResponseDTO.setTranDate(new Date());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return codeResponseDTO;
    }

    @Override
    public InitiateWithdrawalRespDTO initiateWithdrawal(InitiateWithdrawalReqDTO requestDTO) {
        return null;
    }

    @Override
    public WithdrawalResponseDTO withdraw(WithdrawalRequestDTO requestDTO) {
        return null;
    }

    private void setToken() {


//        SettingDTO settingDTO = settingService.getSettingByCode("9psb_token_timeout");
//        if(settingDTO == null ){
//            settingDTO = new SettingDTO();
//            settingDTO.setCode("9psb_token_timeout");
//            settingDTO.setValue("1000000");
//            settingService.addSetting(settingDTO);
//        }

//        long expTime = Long.parseLong(settingDTO.getValue());
//        long currentTime = new Date().getTime();
//        if(currentTime >= expTime){
            httpCustomClient = new HttpCustomClient();
            headerContents.put("Content-Type","application/json");
            httpCustomClient.setHeaderToken(headerContents);
            httpCustomClient.setHasHeaderToken(true);
            httpCustomClient.setReuestURL(baseUrl+loginPath);
            httpCustomClient.setRequestType("POST");



            Map<String,String> reqPayload = new HashMap<>();
            reqPayload.put("clientSecret",clientSecret);
            reqPayload.put("clientKey",clientKey);
            String payLoadStr = new Gson().toJson(reqPayload);

            httpCustomClient.setRequestBody(payLoadStr);
            String resp = httpCustomClient.sendRequest();
            try {
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(resp);
                String respCode = String.valueOf(jsonObject.get("responseCode"));
                if(respCode.equals("00")){
                    JSONObject result   = (JSONObject) jsonObject.get("result");
                    token =  result.get("accessToken").toString();

//                    settingDTO.setValue();
//                    settingDTO.setDescription(token);
//                    settingService.updateSetting(settingDTO);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

//        }else{
//            token = settingDTO.getDescription();
//        }

        System.out.println("final token is :: "+token);

    }
}
