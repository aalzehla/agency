package com._3line.gravity.freedom.gravitymobile.service;

import com._3line.gravity.core.cryptography.SecretKeyCrypt;
import com._3line.gravity.core.cryptography.exceptions.CryptographyException;
import com._3line.gravity.core.cryptography.rsa.RSACrypt;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.agents.service.SecurityDao;
import com._3line.gravity.api.users.agents.dto.AgentSetupResponse;
import com._3line.gravity.freedom.device.repository.DeviceAuditRepository;
import com._3line.gravity.freedom.utility.ApiCallLogs;
import com._3line.gravity.freedom.utility.PropertyResource;
import com._3line.gravity.freedom.utility.Utility;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Service
public class LoginAndSetupService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PropertyResource pr;
    private Gson json = new Gson();

    @Autowired
    private SecurityDao dao;

    @Autowired
    private SecretKeyCrypt secretKeyCrypt ;
    @Autowired
    private SettingService settingService;

    @Autowired
    AgentsRepository applicationUsersRepository;

    @Autowired
    DeviceAuditRepository  deviceAuditRepository;

    @Autowired
    RSACrypt rsaCrypt ;

//    public AgentSetupResponse agentSetup(AgentSetupDto request) {
//
//        AgentSetupResponse response = new AgentSetupResponse();
//
//
//        String Username = request.getUsername();
//        String Pin = request.getAuthData();
//        String newPin = request.getNewAuthData();
//        String password = request.getPassword();
//        String deviceid = request.getDeviceId();
//
//        if (Username == null || Pin == null || deviceid == null) {
//            response.setRespCode("118");
//            response.setRespDescription(pr.getV("118", "response.properties"));
//            logger.warn(response.getRespDescription());
//            return response;
//        }
//
//
//
//        AgentSetupResponse setupresponse = null;
//        try {
//            setupresponse = dao.getAgentDetailsByUsername(Username);
//        } catch (Exception e) {
//            response.setRespCode("110");
//            response.setRespDescription(pr.getV("110", "response.properties"));
//            logger.warn(response.getRespDescription());
//            return response;
//        }
//
//        System.out.println("comparing PIN " + Pin + " hashed with db hashed " + setupresponse.getPin());
//
//        if (setupresponse == null || !passwordEncoder.matches(Pin, setupresponse.getPin())) {
//            response.setRespCode("119");
//            response.setRespDescription(pr.getV("119", "response.properties"));
//            logger.warn(response.getRespDescription());
//            return response;
//        }
//
//        if (setupresponse.getDeviceSetupStatus().equals("1")) {
//            response.setRespCode("121");
//            response.setRespDescription(pr.getV("121", "response.properties"));
//            logger.warn(response.getRespDescription());
//            return response;
//        }
//        try {
//            if (dao.UpdateAgentStatus(setupresponse.getAgentId(), deviceid)) {
//                setupresponse.setDeviceSetupStatus("1");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.setRespCode("110");
//            response.setRespDescription(pr.getV("110", "response.properties"));
//            logger.warn(response.getRespDescription());
//            return response;
//        }
//        boolean changesuccessful = false;
//        try {
//            changesuccessful = dao.savePassword(Username, password);
//            changesuccessful = dao.savePin_(Username, newPin);
//        } catch (Exception e) {
//            response.setRespCode("110");
//            response.setRespDescription(pr.getV("110", "response.properties"));
//            logger.warn(response.getRespDescription());
//            return response;
//        }
//        if (!changesuccessful) {
//            response.setRespCode("123");
//            response.setRespDescription(pr.getV("123", "response.properties"));
//            logger.warn(response.getRespDescription());
//            return response;
//        }
//        setupresponse.setDeviceSetupStatus("1");
//        setupresponse.setPin(null); //removes from being display to user
//        System.out.println("########################### NAME : " + setupresponse.getName());
//        System.out.println("########################### BELONGSTO: " + setupresponse.getBelongsto());
//        response = setupresponse;
//        response.setRespCode("00");
//        response.setRespDescription(pr.getV("00", "response.properties"));
//
//        return response;
//    }

    public Response changePassword(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String http_verb = "POST";
        String timestamp = param.get("timestamp");
        String nonce = param.get("nonce");
        String client_id = param.get("clientid");
        String belongsto = param.get("belongsto");
        String Username = param.get("username");
        String agentId = param.get("userid");

        Response response = new Response();
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(authorisation);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }


        HashMap<String, String> requestMap = null;


        try {
            requestMap = json.fromJson(jsonRequest, HashMap.class);
        } catch (Exception e) {
            response.setRespCode("117");
            response.setRespDescription(pr.getV("117", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        String authdata = requestMap.get("authData");
        String oldPassword = null;
        String newPassword = null;
        String pin = null;

        if (authdata == null) {
            response.setRespCode("118");
            response.setRespDescription(pr.getV("118", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {
            authdata = rsaCrypt.decrypt(authdata);
        } catch (Exception e) {
            response.setRespCode("120");
            response.setRespDescription(pr.getV("120", "response.properties"));
            logger.warn(response.getRespDescription() + e.getMessage());
            return response;
        }

        try {
            requestMap = json.fromJson(authdata, HashMap.class);
            oldPassword = requestMap.get("oldPassword");
            newPassword = requestMap.get("newPassword");
            pin = requestMap.get("pin");
            if (newPassword == null || oldPassword == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            response.setRespCode("122");
            response.setRespDescription(pr.getV("122", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        AgentSetupResponse setupresponse = null;
        try {
            setupresponse = dao.getAgentDetailsByUsername(Username);
        } catch (Exception e) {
            response.setRespCode("110");
            response.setRespDescription(pr.getV("110", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        if (setupresponse == null || !passwordEncoder.matches(pin, setupresponse.getPin())) {
            response.setRespCode("119");
            response.setRespDescription(pr.getV("119", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        Agents applicationUsers = applicationUsersRepository.findByUsername(Username);

        if (setupresponse == null || !passwordEncoder.matches(oldPassword, applicationUsers.getPassword())) {
            response.setRespCode("119");
            response.setRespDescription(pr.getV("119", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        if (!setupresponse.getDeviceSetupStatus().equals("1")) {
            response.setRespCode("141");
            response.setRespDescription(pr.getV("141", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        boolean changesuccessful = false;
        try {
            changesuccessful = dao.savePassword(Username, newPassword);
        } catch (Exception e) {
            response.setRespCode("110");
            response.setRespDescription(pr.getV("110", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }
        if (!changesuccessful) {
            response.setRespCode("123");
            response.setRespDescription(pr.getV("123", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        response.setRespCode("00");
        response.setRespDescription(pr.getV("00", "response.properties"));

        return response;
    }

    private String decryptRequest(String requestbody) throws CryptographyException {
        String jsonRequest;

        jsonRequest = secretKeyCrypt.decrypt(requestbody);

        return jsonRequest;
    }

    public Response forgotPassword(HttpServletRequest request, Map<String, String> param) {

        Response response = new Response();
        String requestbody = Utility.getRequestBody(request);
        String jsonRequest;

        if (requestbody == null) {
            response.setRespCode("106");
            response.setRespDescription(pr.getV("106", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }
        //request decrypted
        try {

            jsonRequest = secretKeyCrypt.decrypt(requestbody);

        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        HashMap<String, String> requestMap = null;

        try {
            requestMap = json.fromJson(jsonRequest, HashMap.class);
        } catch (Exception e) {
            response.setRespCode("117");
            response.setRespDescription(pr.getV("117", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        String userName = requestMap.get("userName");

//        EditUserResponse userResponse = securityService.generateReset(userName) ;
//
//        if(userResponse.getSuccessMessage()!= null && !userResponse.getSuccessMessage().equals("")) {
//            response.setRespCode("00");
//            response.setRespDescription(pr.getV("00", "response.properties"));
//            response.setRespBody(userResponse);
//
//        }else {
//            response.setRespCode("999");
//            response.setRespDescription(pr.getV("999", "response.properties"));
//            response.setRespBody(userResponse);
//
//        }
//
//        return response;
        //TODO reset password
        return null ;
    }

    public Response pinChange(HttpServletRequest request, Map<String, String> param) {

        String http_verb = "POST";
        String timestamp = param.get("timestamp");
        String nonce = param.get("nonce");
        String client_id = param.get("clientid");
        String Username = param.get("username");
        String agentId = param.get("userid");

        Response response = new Response();
        String requestbody = Utility.getRequestBody(request);
        String jsonRequest;

        if (requestbody == null) {
            response.setRespCode("106");
            response.setRespDescription(pr.getV("106", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }
        //request decrypted
        try {

            jsonRequest = secretKeyCrypt.decrypt(requestbody);

        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        HashMap<String, String> requestMap = null;

        try {
            requestMap = json.fromJson(jsonRequest, HashMap.class);
        } catch (Exception e) {
            response.setRespCode("117");
            response.setRespDescription(pr.getV("117", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        String authdata = requestMap.get("authData");
        String newPin = null;
        String oldPin = null;

        if (authdata == null) {
            response.setRespCode("118");
            response.setRespDescription(pr.getV("118", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {
            authdata = rsaCrypt.decrypt(authdata);
        } catch (Exception e) {
            response.setRespCode("120");
            response.setRespDescription(pr.getV("120", "response.properties"));
            logger.warn(response.getRespDescription() + e.getMessage());
            return response;
        }

        try {
            requestMap = json.fromJson(authdata, HashMap.class);
            newPin = requestMap.get("newPin");
            oldPin = requestMap.get("oldPin");
            if (newPin == null || oldPin == null) {
                throw new Exception();
            }

        } catch (Exception e) {
            response.setRespCode("122");
            response.setRespDescription(pr.getV("122", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        AgentSetupResponse setupresponse = null;
        try {
            setupresponse = dao.getAgentDetailsByUsername(Username);
        } catch (Exception e) {
            response.setRespCode("110");
            response.setRespDescription(pr.getV("110", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }
        String shared_secret_key = setupresponse.getSecretKey();

        if (param.get("signature") != null) {
            String stringToBeSigned = http_verb + "&" + timestamp + "&" + nonce + "&" + client_id + "&" + shared_secret_key;
            String hashes = Base64.encodeBase64String(Utility.sha512Byte(stringToBeSigned));
            logger.info(String.format(" \r\n############\r\n Comparing signature :: RECIEVED IS - %s \r\n CALCULATED IS- %s \r\n Signature Correct :%b", param.get("signature"), hashes, param.get("signature").equalsIgnoreCase(hashes)));
            if (!param.get("signature").equalsIgnoreCase(hashes)) {
                response.setRespCode("126");
                response.setRespDescription(pr.getV("126", "response.properties"));
                logger.warn(response.getRespDescription());
                return response;
            }
        } else {
            response.setRespCode("116");
            response.setRespDescription(pr.getV("116", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        if (setupresponse == null || !passwordEncoder.matches(oldPin, setupresponse.getPin())) {
            response.setRespCode("119");
            response.setRespDescription(pr.getV("119", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        if (!setupresponse.getDeviceSetupStatus().equals("1")) {
            response.setRespCode("141");
            response.setRespDescription(pr.getV("141", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        boolean changesuccessful = false;
        try {
            changesuccessful = dao.savePin_(agentId, newPin);
        } catch (Exception e) {
            response.setRespCode("110");
            response.setRespDescription(pr.getV("110", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }
        if (!changesuccessful) {
            response.setRespCode("123");
            response.setRespDescription(pr.getV("123", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        response.setRespCode("00");
        response.setRespDescription(pr.getV("00", "response.properties"));

        return response;
    }

//    public Response logon(LoginDto request) {

//        Response response = new Response();
//
//
//        Map<String, String> sresponse;
//        try {
//            sresponse = dao.getAgentLogonDetailsByUsername(request.getUsername());
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.setRespCode("110");
//            response.setRespDescription(pr.getV("110", "response.properties"));
//            logger.warn(response.getRespDescription());
//            return response;
//        }
//
//        if (sresponse == null || !passwordEncoder.matches(request.getPassword(), sresponse.get("password"))) {
//            response.setRespCode("119");
//            response.setRespDescription(pr.getV("119", "response.properties"));
//            logger.warn(response.getRespDescription());
//            return response;
//        }
//
//        if (!(sresponse.get("activated").equals("1") || sresponse.get("activated").equals("2"))) {
//            response.setRespCode("119");
//            response.setRespDescription(pr.getV("119", "response.properties") + "#");
//            logger.warn(response.getRespDescription());
//            return response;
//        }
//
//
//        DeviceAudit deviceAudit = deviceAuditRepository.findByAgentIdentifier("");
//        if(deviceAudit==null){
//            //then send otp if null and set first login as 1
//            //send otp
//            sresponse.put("isFirstLogin","true");
//        }else{
//            //set first login as 0
//            sresponse.put("isFirstLogin","false");
//        }
//
//        response.setRespBody(sresponse);
//        response.setRespCode("00");
//        response.setRespDescription(pr.getV("00", "response.properties"));
//
//        return response;
//    }



}
