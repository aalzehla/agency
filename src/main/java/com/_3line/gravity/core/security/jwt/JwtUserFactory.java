package com._3line.gravity.core.security.jwt;

//import client.agent.models.Agent;
import com._3line.gravity.freedom.agents.models.Agents;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;


public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(Agents user, String ipAddress) {
        return new JwtUser(
                user.getId(),
                user.getAgentId(),
                user.getPassword(),
                ipAddress,
                mapToGrantedAuthorities(user),
                true
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Agents user) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.NO_AUTHORITIES;
        return grantedAuthorities;
    }
}
