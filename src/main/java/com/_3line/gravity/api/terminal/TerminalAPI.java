package com._3line.gravity.api.terminal;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.terminalmanager.dto.TerminalRequest;
import com._3line.gravity.freedom.terminalmanager.service.TerminalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/gravity/api/terminal")
public class TerminalAPI {
    private static Logger logger = LoggerFactory.getLogger(Object.class);


    private AgentService agentService;
    private TerminalService terminalService;
    private JwtUtility jwtUtility;

    @Autowired
    public TerminalAPI(AgentService agentService, TerminalService terminalService, JwtUtility jwtUtility) {
        this.agentService = agentService;
        this.terminalService = terminalService;
        this.jwtUtility = jwtUtility;
    }

    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(value = "/detach", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> detachTerminal(@RequestBody TerminalRequest terminalRequest) {
        Response response = new Response();
        Agents agents = jwtUtility.getCurrentAgent();
        logger.info("agents:{} " , agents);
        if (agents == null) {
            throw new GravityException("Invalid Agent Id");
        } else {
            Agents aggregator = agentService.fetchAgentByAgentName(terminalRequest.getUsername());
            if (aggregator == null) {
                throw new GravityException("Agent not found for Aggregator");
            }
        }
        terminalRequest.setAgents(agents);
        try {
            response = terminalService.detachTerminal(terminalRequest);
        } catch(VerificationException e){
            response.setRespCode("00");
            response.setRespDescription(e.getMessage());
        }catch (Exception e){
            response.setRespCode("96");
            response.setRespDescription("failed");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/list/terminal", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> detachTerminalAll() {

        Agents agents = jwtUtility.getCurrentAgent();
        System.out.println("agents: " + agents);
        return new ResponseEntity<>(terminalService.fetchAllTerminal(), HttpStatus.OK);
    }


}
