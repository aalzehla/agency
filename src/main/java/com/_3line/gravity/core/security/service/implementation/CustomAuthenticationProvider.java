package com._3line.gravity.core.security.service.implementation;

import com._3line.gravity.core.security.activedirectory.ActiveDirectoryAuthenticationService;
import com._3line.gravity.core.setting.dto.SettingDTO;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.usermgt.model.SystemUser;
import com._3line.gravity.core.usermgt.service.ApplicationUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author FortunatusE
 * @date 11/19/2018
 */

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    private SystemUser user;
    @Autowired
    private SettingService settingService;
    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ActiveDirectoryAuthenticationService activeDirectoryAuthenticationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String loginId = authentication.getName();
        String password = authentication.getCredentials().toString();
        String[] usernameAndToken = StringUtils.split(loginId, ":");
        if (usernameAndToken == null || usernameAndToken.length != 2) {
            throw new UsernameNotFoundException("Username and token are required");
        }
        String username = usernameAndToken[0];
        String token = usernameAndToken[1];
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

        logger.debug("Authenticating user [{}]", username);

        user = applicationUserService.findUserByUsername(username);

        if (user == null) {
            logger.error("Username [{}] not found", username);
            throw new BadCredentialsException("Invalid credentials");
        }

        boolean authenticated;

        if (authenticateAgainstActiveDirectory()) {
            authenticated = activeDirectoryAuthenticationService.authenticate(username, password, token);

        } else {
            authenticated = authenticateWithDatabase(user, password, token);
        }

        if (!authenticated) {
            logger.error("Invalid credentials");
            throw new BadCredentialsException("Invalid credentials");
        }

        if (!user.isEnabled()) {
            logger.error("User [{}] is disabled", username);
            throw new DisabledException("User [" + username + "] is disabled");
        }


        logger.info("Authentication successful for user [{}]", username);
        authorities = getAuthorities(user);
        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }


    boolean authenticateAgainstActiveDirectory() {

        logger.debug("Checking if Active Directory authentication is available");
        SettingDTO activeDirectorySetting = settingService.getSettingByCode("ACTIVE_DIRECTORY_INTEGRATION");

        if (settingService.isSettingAvailable("ACTIVE_DIRECTORY_INTEGRATION")) {
            logger.debug("Active directory authentication is available");
            return true;
        }
        logger.debug("Active directory authentication is not available");
        return false;
    }

    boolean authenticateWithDatabase(SystemUser user, String password, String token) {

        logger.debug("Authenticating user against internal database");

        if (passwordEncoder.matches(password, user.getPassword())) {
            return true;

            //todo: validate token if service is available
        }
        return false;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(SystemUser user) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        getPermissions(user).forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));
        return authorities;
    }

    private List<String> getPermissions(SystemUser user) {

        List<String> permissions = new ArrayList<>();

        if (user.getRole() != null) {
            user.getRole().getPermissions().forEach(permission -> permissions.add(permission.getName()));
        }
        return permissions;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return this.user.isEnabled();
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
