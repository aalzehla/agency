/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.api.users.subaggregator.controller;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.api.transaction.dto.TransactionHistoryDTO;
import com._3line.gravity.api.users.agents.dto.AgentDashBoardDTO;
import com._3line.gravity.api.users.aggregator.dto.AggregatorAgentDTO;
import com._3line.gravity.api.users.aggregator.dto.AggregatorDashBoardDTO;
import com._3line.gravity.core.cryptography.SecretKeyCrypt;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.usermgt.repository.ApplicationUserRepository;
import com._3line.gravity.core.utils.DateUtil;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.freedom.agents.dtos.AgentDto;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.gravitymobile.service.MobileService;
import com._3line.gravity.freedom.reports.dtos.AgentPerformance;
import com._3line.gravity.freedom.reports.service.ReportService;
import com._3line.gravity.freedom.utility.PropertyResource;
import com._3line.gravity.freedom.wallet.dto.WalletHistoryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/gravity/api/subaggregator")
public class SubAggregatorAPI {

    @Autowired
    private MobileService service;

    @Autowired
    private AgentService agentService;

    @Autowired
    SecretKeyCrypt secretKeyCrypt;

    @Autowired
    AgentsRepository agentsRepository;

    @Qualifier("jwtservice")
    @Autowired
    UserDetailsService userDetailsService;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtility jwtUtility;


    @Autowired
    PropertyResource pr;

    @Autowired
    ReportService  reportService;

    @Autowired
    private ApplicationUserRepository userRepository ;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String returnEncryptedResponse(String securedResponse) {
        return securedResponse;
    }


    @RequestMapping(value = "/dashboard", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> dashboard() {

        Agents aggregatorRec = jwtUtility.getCurrentAgent();
        AggregatorDashBoardDTO aggregatorDashBoardDTO = agentService.fetchAggregatorDashborad(aggregatorRec);
        ResponseEntity<?> responseEntity = new ResponseEntity<>(aggregatorDashBoardDTO,HttpStatus.OK);
        return responseEntity;
    }

    @RequestMapping(value = "/dashboard/{agentid}", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> fetchAgentDashboard(@PathVariable String agentid) {

        AgentDashBoardDTO agentDashBoardDTO = agentService.fetchAgentDashBoard(agentid);

        Response response = new Response();
        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(agentDashBoardDTO);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/agent_performance/", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> fetchAggregatorsAgentPerformance(@RequestBody WalletHistoryDto walletHistoryDto) {

        Agents agent = jwtUtility.getCurrentAgent();

        List<AgentPerformance> codes = reportService.aggregatorsAgentPerformance(true,agent, DateUtil.dateFullFormat(walletHistoryDto.getFrom()), DateUtil.AddDays(DateUtil.dateFullFormat(walletHistoryDto.getTo()),1));

        Response response = new Response();
        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(codes);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @RequestMapping(value = "/fetch_agent", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> fetchAgents(@RequestBody TransactionHistoryDTO agenthistory){

        Agents agent = jwtUtility.getCurrentAgent();
        Pageable pageable = PageRequest.of(agenthistory.getPage(),agenthistory.getSize());
        Page<AggregatorAgentDTO> agentsList = agentService.getPagedSubAggregatorsAgent(agent,pageable);


        Map<String,Object> stringMap = new HashMap<>();
        stringMap.put("agents",agentsList.getContent());
        stringMap.put("totalRecord",agentsList.getTotalElements());
        stringMap.put("hasNext",agentsList.hasNext());
        
        Response response = new Response();
        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(stringMap);

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
