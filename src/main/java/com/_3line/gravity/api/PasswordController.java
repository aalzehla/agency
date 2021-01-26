package com._3line.gravity.api;

import com._3line.gravity.api.auth.dto.LoginDto;
import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.api.shared.utility.UserAgent;
import com._3line.gravity.api.users.agents.dto.AgentSetupDto;
import com._3line.gravity.api.users.agents.dto.AgentSetupResponse;
import com._3line.gravity.core.exceptions.AgencyBankingException;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.otp.model.OtpRequest;
import com._3line.gravity.core.otp.services.OtpService;
import com._3line.gravity.core.security.jwt.AuthenticationRequest;
import com._3line.gravity.core.security.jwt.JwtTokenUtil;
import com._3line.gravity.core.security.service.LoginService;
import com._3line.gravity.core.usermgt.exception.UserNotFoundException;
import com._3line.gravity.core.usermgt.model.SystemUser;
import com._3line.gravity.core.utils.ResponseUtils;
import com._3line.gravity.freedom.agents.dtos.PassWordResetDto;
import com._3line.gravity.freedom.agents.dtos.PasswordUpdateDto;
import com._3line.gravity.freedom.agents.dtos.PinUpdateDTO;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.gravitymobile.oauth.model.OAuth2Response;
import com._3line.gravity.freedom.utility.PropertyResource;
import com._3line.gravity.freedom.utility.Utility;
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
import java.util.HashMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/gravity/api")
public class PasswordController {

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


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> changePassword(HttpServletRequest request, @RequestBody PasswordUpdateDto passwordUpdateDto) {

        Response response = new Response();
        Agents agent = jwtUtility.getCurrentAgent();
        if(agent==null){
            throw new UserNotFoundException("Invalid Token Specified");
        }
        passwordUpdateDto.setAgentUsername(agent.getUsername());
        String resp = agentService.updatePassword(passwordUpdateDto);
        response.setRespDescription(resp);
        response.setRespCode("00");

        return new ResponseEntity<>(response,HttpStatus.OK) ;

    }

    @RequestMapping(value = "/pinChange", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> changePin(HttpServletRequest request, @RequestBody PinUpdateDTO pinUpdateDTO) {

        Agents agent = jwtUtility.getCurrentAgent();
        if(agent==null){
            throw new UserNotFoundException("Invalid Token Specified");
        }
        pinUpdateDTO.setAgentName(agent.getUsername());

        String resp = agentService.updatePin(pinUpdateDTO);
        Response response = new Response();
        response.setRespDescription(resp);
        response.setRespCode("00");

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
