package com._3line.gravity.api.thrift;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.thirftmgt.dtos.ContributionDTO;
import com._3line.gravity.freedom.thirftmgt.dtos.ThriftDTO;
import com._3line.gravity.freedom.thirftmgt.dtos.ThriftEnquiryDTO;
import com._3line.gravity.freedom.thirftmgt.dtos.ThriftLiquidationDTO;
import com._3line.gravity.freedom.thirftmgt.services.ThriftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/gravity/api/thrift")
public class ThriftAPI {

    private Logger logger = LoggerFactory.getLogger(ThriftAPI.class);

    @Autowired
    AgentService agentService;

    @Autowired
    ThriftService thriftService;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;





    @RequestMapping(value = "registerForThrift", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> registerForThrift(@RequestBody ThriftDTO thriftDTO) {

        Response response = new Response();
        Agents agent = jwtUtility.getCurrentAgent();
        thriftDTO.setAgentName(agent.getUsername());
        String resp = thriftService.register(thriftDTO);
        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(resp);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "contributeThrift", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> contributeThrift(@RequestBody  @Valid ContributionDTO contributionDTO) {

        Response response = new Response();
        Agents agent = validateAgentPin(contributionDTO.getAgentPin());
        contributionDTO.setAgentName(agent.getUsername());

        String resp = thriftService.contribute(contributionDTO);
        response.setRespBody(resp);
        response.setRespCode("00");
        response.setRespDescription("success");
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "thriftDetails", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> thriftDetails(@RequestBody @Valid ThriftEnquiryDTO enquiryDTO) {

        validateAgentPin(enquiryDTO.getAgentPin());
        Response response = new Response();
        ThriftDTO resp = thriftService.checkContribution(enquiryDTO.getCardNumber());
        response.setRespBody(resp);
        response.setRespCode("00");
        response.setRespDescription("success");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "prematureLiquidation", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> prematureLiquidation(@RequestBody @Valid ThriftLiquidationDTO liquidationDTO) {

        Response response = new Response();
        validateAgentPin(liquidationDTO.getAgentPin());
        String resp = thriftService.prematureLiquidation(liquidationDTO);
        response.setRespBody(resp);
        response.setRespCode("00");
        response.setRespDescription("success");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    private Agents validateAgentPin(String enteredPin){
        Agents agent =  jwtUtility.getCurrentAgent();
        if(agent!=null){
            boolean  matches = passwordEncoder.matches(enteredPin,agent.getUserPin());
            if(!matches){
                throw new GravityException("Agent pin is not valid");
            }
        }else{
            throw new GravityException("Invalid Session");
        }
        return agent;
    }

}
