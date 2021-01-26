package com._3line.gravity.core.security.jwt.service;

//import _3line.agencybankingintegration.config.security.jwt.JwtUserFactory;
//import _3line.agencybankingintegration.config.security.utils.IpAddressUtils;
//import client.agent.models.Agent;
//import client.agent.repository.AgentRepository;
import com._3line.gravity.core.security.jwt.JwtUserFactory;
import com._3line.gravity.core.security.utils.IpAddressUtils;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("jwtservice")
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(JwtUserDetailsServiceImpl.class);

    @Autowired
    private AgentsRepository agentRepository;

    @Autowired
    private IpAddressUtils addressUtils;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("Agent with ID [{}] logging in", username);
        Agents agent = agentRepository.findByAgentId(username);
        final String ip = addressUtils.getClientIP();
        if (agent == null) {
            logger.info("Could not find agentId [{}]", username);
            throw new UsernameNotFoundException(String.format("No agent found with username '%s'.", username));
        } else {
            logger.info("Found agentId [{}]", username);
            return JwtUserFactory.create(agent, ip);
        }
    }


}
