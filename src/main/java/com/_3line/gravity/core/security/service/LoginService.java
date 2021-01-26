package com._3line.gravity.core.security.service;

import com._3line.gravity.core.usermgt.model.SystemUser;
import com._3line.gravity.core.usermgt.repository.ApplicationUserRepository;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author FortunatusE
 * @date 11/28/2018
 */

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private ApplicationUserRepository userRepository;

    @Autowired
    private AgentsRepository agentsRepository;

    @Autowired
    private PasswordEncoder  passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;


    public void updateUserLoginAttempt(SystemUser user){

        int numOfLoginAttempts = user.getNoOfWrongLoginCount();
        user.setNoOfWrongLoginCount(++numOfLoginAttempts);
        userRepository.save(user);
        logger.info("Updated failed login count for user [{}]", user.getUserName());

    }


    public void updateLastLogin(SystemUser user){

        user.setLastLoginDate(new Date());
        userRepository.save(user);
        logger.info("Updated last login date for user [{}]", user.getUserName());
    }

    public Agents validateLoginCredentials(SystemUser authRequest){

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(),authRequest.getPassword()));
            Agents ag = agentsRepository.findByUsername(authRequest.getUserName());
            if(ag ==null){
                return null;
            }else{
                return ag;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
