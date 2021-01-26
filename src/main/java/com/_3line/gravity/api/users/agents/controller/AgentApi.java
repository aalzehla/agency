/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.api.users.agents.controller;

import com._3line.gravity.api.auth.dto.LoginRespDto;
import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.api.users.agents.dto.AgentDashBoardDTO;
import com._3line.gravity.api.users.agents.dto.AgentMiniStatementDTO;
import com._3line.gravity.api.utility.GeneralUtil;
import com._3line.gravity.core.cryptography.SecretKeyCrypt;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.usermgt.exception.UserNotFoundException;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.gravitymobile.service.MobileService;
import com._3line.gravity.freedom.reports.service.ReportService;
import com._3line.gravity.freedom.utility.ApiCallLogs;
import com._3line.gravity.freedom.utility.PropertyResource;
import com._3line.gravity.freedom.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/gravity/api/agents")
public class AgentApi {


    @Autowired
    private AgentService agentService;


    @Autowired
    AgentsRepository agentsRepository;

    @Qualifier("jwtservice")
    @Autowired
    UserDetailsService userDetailsService;


    @Autowired
    JwtUtility jwtUtility;


    @Autowired
    PropertyResource pr;

    @Autowired
    GeneralUtil generalUtil;

    @Autowired
    ReportService reportService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String returnEncryptedResponse(String securedResponse) {
        return securedResponse;
    }



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



//    @RequestMapping(value = "/pinChange", method = RequestMethod.POST, consumes = {"text/plain"}, produces = "text/plain")
//    @ResponseBody
//    String pinChange(HttpServletRequest request) {
//
//        String securedResponse = service.pinChange(request);
//        return returnEncryptedResponse(securedResponse);
//
//    }
//
//
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

    @RequestMapping(value = "/dashboard/{agentid}", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> fetchAgentDashboard(@PathVariable String agentid) {
        Response response = new Response();
        AgentDashBoardDTO agentDashBoardDTO = agentService.fetchAgentDashBoard(agentid);
        response.setRespDescription("success");
        response.setRespCode("00");
        response.setRespBody(agentDashBoardDTO);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/fetchprofile", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> fetchprofile() {
        Agents agent = jwtUtility.getCurrentAgent();
        if(agent==null){
            throw new UserNotFoundException("Session does not exist for user");
        }
        LoginRespDto respDto = generalUtil.converttodto(agent);

        Response response = new Response();
        response.setRespBody(respDto);
        response.setRespCode("00");
        response.setRespDescription("Success");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/fetchsubagents", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> fetchSubAgent() {
        Response response = new Response();
        Agents aggregator = jwtUtility.getCurrentAgent();
        List<Agents> subAgents = agentService.getAggregatorsAgent(aggregator);
        response.setRespBody(subAgents);
        response.setRespCode("00");
        response.setRespDescription("Success");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }



    @RequestMapping(value = "/fetchsubagents/{subagentsid}", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> fetchSubAgentDetail(@PathVariable String subagentsid) {
        Response response = new Response();
        Agents aggregator = jwtUtility.getCurrentAgent();
        Agents subAgent = agentService.fetchAgentByParentAgentAndAgentId(aggregator,subagentsid);
        if(subAgent!=null){
            response.setRespBody(subAgent);
            response.setRespCode("00");
            response.setRespDescription("Success");
        }else{
            response.setRespCode("99");
            response.setRespDescription("No record found for id");
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @RequestMapping(value = "/mini_statement", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> generateMiniStatement() {
        Response response = new Response();
        Agents agent = jwtUtility.getCurrentAgent();
        Calendar calendar = Calendar.getInstance();
        Date fromDate = calendar.getTime();
        calendar.add(Calendar.HOUR,24);
        Date tomorrow = calendar.getTime();


        AgentMiniStatementDTO miniStatementDTO = reportService.agentsMiniStatement(agent,fromDate,tomorrow);

        if(miniStatementDTO!=null){
            response.setRespBody(miniStatementDTO);
            response.setRespDescription("Success");
            response.setRespCode("00");
        }else{
            response.setRespCode("99");
            response.setRespDescription("No record found for id");
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }



    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
