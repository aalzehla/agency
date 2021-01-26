package com._3line.gravity.api.auth.controller;

import com._3line.gravity.api.auth.dto.LoginDto;
import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.api.shared.utility.UserAgent;
import com._3line.gravity.api.users.agents.dto.AgentSetupDto;
import com._3line.gravity.api.users.agents.dto.AgentSetupResponse;
import com._3line.gravity.core.exceptions.AgencyBankingException;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.exceptions.UnAuthorizedUserException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.otp.model.OtpRequest;
import com._3line.gravity.core.otp.services.OtpService;
import com._3line.gravity.core.security.jwt.AuthenticationRequest;
import com._3line.gravity.core.security.jwt.JwtTokenUtil;
import com._3line.gravity.core.security.service.LoginService;
import com._3line.gravity.core.setting.dto.SettingDTO;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.usermgt.model.SystemUser;
import com._3line.gravity.core.utils.ResponseUtils;
import com._3line.gravity.freedom.agents.dtos.PassWordResetDto;
import com._3line.gravity.freedom.agents.dtos.PasswordUpdateDto;
import com._3line.gravity.freedom.agents.dtos.PinResetDto;
import com._3line.gravity.freedom.agents.dtos.PinUpdateDTO;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.gravitymobile.oauth.model.OAuth2Response;
import com._3line.gravity.freedom.mediaintegration.dto.AppDeviceDTO;
import com._3line.gravity.freedom.mediaintegration.service.AppDeviceService;
import com._3line.gravity.freedom.utility.PropertyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/gravity/api/oauth")
public class AuthController {

    @Autowired
    LoginService loginService;

    @Qualifier("jwtservice")
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    PropertyResource pr;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    OtpService otpService;

    @Autowired
    AgentService agentService;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    AppDeviceService appDeviceService;

    @Autowired
    SettingService settingService;

    @Autowired
    BankDetailsService bankDetailsService;


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(value = "/token", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> getBearerToken(HttpServletRequest request, @RequestBody AuthenticationRequest authRequest
    ) {
        OAuth2Response response = new OAuth2Response();
        try {

            SystemUser systemUser = new SystemUser();

            systemUser.setUserName(authRequest.getUsername());
            systemUser.setPassword(authRequest.getPassword());
            Agents ag = loginService.validateLoginCredentials(systemUser);
            if(ag==null){
                return new ResponseEntity<>(new Response("97","UnAuthorized",null),HttpStatus.UNAUTHORIZED);
            }
            final UserDetails userDetails = userDetailsService.loadUserByUsername(ag.getAgentId());
            final String token = jwtTokenUtil.generateToken(userDetails, null);

            response.setAccess_token(token);
            response.setRespCode("00");
            response.setRespDescription(pr.getV("00", "response.properties"));
            logger.warn(response.getRespDescription());
        } catch (GravityException e) {
            response = (OAuth2Response) e.getObj();
        }

        return new ResponseEntity<>(response, HttpStatus.OK);


    }


    @RequestMapping(value = "/validateOTP", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> validateOTP(@RequestBody OtpRequest otpRequest){
        try {
            otpService.validateOTP(otpRequest, true);
            return ResponseEntity.ok(ResponseUtils.createDefaultSuccessResponse());

        }catch (AgencyBankingException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(ResponseUtils.createFailureResponse(e.getMessage()));

        }
    }


    @RequestMapping(value = "/logon", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> logon(@RequestBody LoginDto loginDto, HttpServletRequest httpServletRequest) {
        ResponseEntity<?>  responseEntity;
        logger.info("incoming request is :: {} ",loginDto);

        if(loginDto.getDeviceId()==null || loginDto.getDeviceId().equals("")){
            UserAgent userAgent = new UserAgent().extractUserAgent(httpServletRequest);
            System.out.println("Device id  generated is :: "+userAgent.getDeviceId());
            loginDto.setDeviceId(userAgent.getDeviceId());

        }

        Response response = agentService.logon(loginDto);

        responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        return responseEntity;

    }

    @RequestMapping(value = "/agentSetup", method = RequestMethod.POST)//todo revert back to smallcase
    @ResponseBody
    ResponseEntity<?> agentSetup(@RequestBody AgentSetupDto agentSetupDto, HttpServletRequest  httpServletRequest) {
        if(agentSetupDto.getDeviceId()==null || agentSetupDto.getDeviceId().equals("")){
            UserAgent userAgent = new  UserAgent().extractUserAgent(httpServletRequest);
            agentSetupDto.setDeviceId(userAgent.getDeviceId());
        }
        AgentSetupResponse agentSetupResponse = agentService.agentSetup(agentSetupDto);
        ResponseEntity<?> responseEntity = new ResponseEntity<>(agentSetupResponse,HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> changePassword(HttpServletRequest request, @RequestBody PasswordUpdateDto passwordUpdateDto) {

        Response response = new Response();
        Agents agent = jwtUtility.getCurrentAgent();
        if(agent==null){
            throw new UnAuthorizedUserException();
        }
        passwordUpdateDto.setAgentUsername(agent.getUsername());
        String resp = agentService.updatePassword(passwordUpdateDto);
        response.setRespCode("00");
        response.setRespDescription(resp);

        return new ResponseEntity<>(response,HttpStatus.OK) ;

    }

    @RequestMapping(value = "/forgotpassword", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> forgotPassword(@RequestBody PassWordResetDto passWordResetDto) {
        Response response = new Response();

        try{
            String resp = agentService.resetPassword(passWordResetDto);
            response.setRespDescription(resp);
            response.setRespCode("00");
            return new ResponseEntity<>(response,HttpStatus.OK) ;
        }catch(Exception e) {
            e.printStackTrace();
            response.setRespDescription(e.getMessage());
            response.setRespCode("00");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }


    }



    @RequestMapping(value = "/resetpin", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> resetPIN(HttpServletRequest request, @RequestBody PinResetDto pinResetDto) {

        String resp = agentService.resetPin(pinResetDto);
        Response response = new Response();
        response.setRespCode("00");
        response.setRespDescription(resp);

        return new ResponseEntity<>(response,HttpStatus.OK) ;

    }


    @RequestMapping(value = "/changepin", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> changePin(HttpServletRequest request, @RequestBody PinUpdateDTO pinUpdateDTO) {

        Agents agent = jwtUtility.getCurrentAgent();
        if(agent==null){
            throw new UnAuthorizedUserException();
        }
        pinUpdateDTO.setAgentName(agent.getUsername());

        String resp = agentService.updatePin(pinUpdateDTO);
        Response response = new Response();
        response.setRespCode("00");
        response.setRespDescription(resp);

        return new ResponseEntity<>(response,HttpStatus.OK) ;

    }

    @RequestMapping(value = "/mediatypes", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getMediaTypes(HttpServletRequest request) {



        List<BankDetails> bankDetails = bankDetailsService.findCardEnabledBanks();

        Map<String,String> stringMap = new HashMap<>();
        bankDetails.forEach(bankDetail -> stringMap.put(bankDetail.getBankCode(),bankDetail.getCardRequestFee()));

        Map<String,Object> cardRequestFee = new HashMap<>();
        cardRequestFee.put("card_request_fee",stringMap);

        List<Object> objectList = new ArrayList<>();
        objectList.add(cardRequestFee);

        Map<String,Object> objectMap = new HashMap<>();
        List<AppDeviceDTO> mediaTypeDTOList = appDeviceService.fetchMediaTypes();

        objectMap.put("medias",mediaTypeDTOList);
        objectMap.put("configs",objectList);

        Response response = new Response();
        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(objectMap);
        return new ResponseEntity<>(response,HttpStatus.OK) ;

    }

    @RequestMapping(value = "/mediatypes/{devicetype}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getMediaByType(HttpServletRequest request,@PathVariable String devicetype) {


        AppDeviceDTO mediaTypeDTO = appDeviceService.findByMediaType(devicetype.toUpperCase());
        Response response = new Response();
        if(mediaTypeDTO!=null){
            response.setRespCode("00");
            response.setRespDescription("success");

            List<BankDetails> bankDetails = bankDetailsService.findCardEnabledBanks();

            Map<String,String> stringMap = new HashMap<>();
            bankDetails.forEach(bankDetail -> stringMap.put(bankDetail.getBankCode(),bankDetail.getCardRequestFee()));

            Map<String,Object> cardRequestFee = new HashMap<>();
            cardRequestFee.put("card_request_fee",stringMap);

            List<Object> objectList = new ArrayList<>();
            objectList.add(cardRequestFee);

            List<AppDeviceDTO> appDeviceDTOS = new ArrayList<>();
            appDeviceDTOS.add(mediaTypeDTO);

            Map<String,Object> objectMap = new HashMap<>();
            objectMap.put("medias",appDeviceDTOS);
            objectMap.put("configs",objectList);

//            response.setRespBody(mediaTypeDTO);
            response.setRespBody(objectMap);
        }else{
            response.setRespCode("96");
            response.setRespDescription("No record found");
        }

        return new ResponseEntity<>(response,HttpStatus.OK) ;

    }

    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }



}
