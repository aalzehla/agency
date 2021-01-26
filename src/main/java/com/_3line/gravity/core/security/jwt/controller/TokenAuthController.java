package com._3line.gravity.core.security.jwt.controller;


import com._3line.gravity.core.security.jwt.AuthenticationRequest;
import com._3line.gravity.core.security.jwt.JwtTokenUtil;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TokenAuthController {

    @Qualifier("jwtservice")
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AgentsRepository agentsRepository;


    @RequestMapping(value = "/auth",method = RequestMethod.POST)
    public ResponseEntity<?> getAuthToken(@RequestBody AuthenticationRequest authenticationRequest){


        // Reload password post-security so we can generate token
        System.out.println("checking with "+authenticationRequest.getUsername());
        Agents ag = agentsRepository.findByUsername(authenticationRequest.getUsername());
        if(ag ==null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        System.out.println(authenticationRequest.getPassword()+" and "+ag.getPassword());

        String newEncoded = passwordEncoder.encode(authenticationRequest.getPassword());
        System.out.println(authenticationRequest.getPassword()+" and "+newEncoded);
        if(!passwordEncoder.matches(authenticationRequest.getPassword(),ag.getPassword())){
            System.out.println("does not matched");
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }else{
            System.out.println("matched");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(ag.getAgentId());
        final String token = jwtTokenUtil.generateToken(userDetails, null);

        // Return the token
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
