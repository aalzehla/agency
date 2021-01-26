package com._3line.gravity.api.shared.utility;

import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUtility {

    @Autowired
    AgentsRepository agentsRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Agents getCurrentAgent(){
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        Agents agents = null;
        if(authentication.getName()!=null){
            agents = agentsRepository.findByAgentId(authentication.getName());
        }
        return agents;
    }


    public boolean isValidAgentPin(String requestPin,Agents agents){
        if(!passwordEncoder.matches(requestPin,agents.getUserPin())){
            return false;
        }else{
            return true;
        }
    }

}
