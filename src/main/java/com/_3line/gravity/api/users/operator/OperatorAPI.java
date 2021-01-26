//package com._3line.gravity.api.users.controller;
//
//import com._3line.gravity.api.users.aggregator.dto.AgentSetupDto;
//import com._3line.gravity.api.users.aggregator.dto.AgentSetupResponse;
//import com._3line.gravity.api.auth.dto.LoginDto;
//import com._3line.gravity.api.shared.utility.JwtUtility;
//import com._3line.gravity.api.shared.utility.UserAgent;
//import com._3line.gravity.core.cryptography.SecretKeyCrypt;
//import com._3line.gravity.core.models.Response;
//import com._3line.gravity.freedom.agents.models.Agents;
//import com._3line.gravity.freedom.agents.repository.AgentsRepository;
//import com._3line.gravity.freedom.agents.service.AgentService;
//import com._3line.gravity.freedom.gravitymobile.service.MobileService;
//import com._3line.gravity.freedom.utility.ApiCallLogs;
//import com._3line.gravity.freedom.utility.PropertyResource;
//import com._3line.gravity.freedom.utility.Utility;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//import java.util.Map;
//
//
//@CrossOrigin(origins = "*", maxAge = 3600)
//@RestController
//@RequestMapping(value = "/gravity/api")
//public class OperatorAPI {
//
//    @Autowired
//    private MobileService service;
//
//    @Autowired
//    private AgentService agentService;
//
//    @Autowired
//    SecretKeyCrypt secretKeyCrypt;
//
//    @Autowired
//    AgentsRepository agentsRepository;
//
//    @Qualifier("jwtservice")
//    @Autowired
//    UserDetailsService userDetailsService;
//
//
//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    @Autowired
//    JwtUtility jwtUtility;
//
//
//    @Autowired
//    PropertyResource pr;
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private String returnEncryptedResponse(String securedResponse) {
//        return securedResponse;
//    }
//
//
//    @RequestMapping(value = "/agentSetup", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<?> agentSetup(@RequestBody AgentSetupDto agentSetupDto, HttpServletRequest  httpServletRequest) {
//        if(agentSetupDto.getDeviceId()==null || agentSetupDto.getDeviceId().equals("")){
//            UserAgent userAgent = new  UserAgent().extractUserAgent(httpServletRequest);
//            agentSetupDto.setDeviceId(userAgent.getDeviceId());
//        }
//        AgentSetupResponse agentSetupResponse = agentService.agentSetup(agentSetupDto);
//        ResponseEntity<?> responseEntity = new ResponseEntity<>(agentSetupResponse,HttpStatus.OK);
//        return responseEntity;
//    }
//
//    @RequestMapping(value = "/changePassword", method = RequestMethod.POST, consumes = {"text/plain"}, produces = "text/plain")
//    @ResponseBody
//    String changePassword(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.changePassword(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//        return returnEncryptedResponse(securedResponse);
//
//    }
//
//    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST, consumes = {"text/plain"}, produces = "text/plain")
//    @ResponseBody
//    String forgotPassword(HttpServletRequest request) {
//
//        String securedResponse = service.forgotPassword(request);
//        return returnEncryptedResponse(securedResponse);
//
//    }
//
//    @RequestMapping(value = "/pinChange", method = RequestMethod.POST, consumes = {"text/plain"}, produces = "text/plain")
//    @ResponseBody
//    String pinChange(HttpServletRequest request) {
//
//        String securedResponse = service.pinChange(request);
//        return returnEncryptedResponse(securedResponse);
//
//    }
//
//    @RequestMapping(value = "/logon", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<?> logon(@RequestBody LoginDto loginDto,HttpServletRequest httpServletRequest) {
//        ResponseEntity<?>  responseEntity;
//        logger.info("incoming request is :: {} ",loginDto);
//        if(loginDto.getDeviceId()==null || loginDto.getDeviceId().equals("")){
//            UserAgent userAgent = new UserAgent().extractUserAgent(httpServletRequest);
//            loginDto.setDeviceId(userAgent.getDeviceId());
//
//        }
//        Response response = agentService.logon(loginDto);
//        responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
//        return responseEntity;
//
//    }
//
//
//    @RequestMapping(value = "/agentBalance", method = RequestMethod.POST)
//    @ResponseBody
//    String agentBalance(HttpServletRequest request) {
//
//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.agentBalance(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//
//    }
//
//    @RequestMapping(value = "/agents/fetchsubagents", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<?> fetchSubAgent() {
//        Response response = new Response();
//        Agents aggregator = jwtUtility.getCurrentAgent();
//        List<Agents> subAgents = agentService.getAggregatorsAgent(aggregator);
//        response.setRespBody(subAgents);
//        response.setRespCode("00");
//        response.setRespDescription("Success");
//        return new ResponseEntity<>(response,HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/agents/fetchsubagents/{subagentsid}", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<?> fetchSubAgentDetail(@PathVariable String subagentsid) {
//        Response response = new Response();
//        Agents aggregator = jwtUtility.getCurrentAgent();
//        Agents subAgent = agentService.fetchAgentByParentAgentAndAgentId(aggregator,subagentsid);
//        if(subAgent!=null){
//            response.setRespBody(subAgent);
//            response.setRespCode("00");
//            response.setRespDescription("Success");
//        }else{
//            response.setRespCode("99");
//            response.setRespDescription("No record found for id");
//        }
//        return new ResponseEntity<>(response,HttpStatus.OK);
//    }
//
//
//
//    @RequestMapping(
//            value = "/**",
//            method = RequestMethod.OPTIONS
//    )
//    public ResponseEntity handle() {
//        return new ResponseEntity(HttpStatus.OK);
//    }
//}
