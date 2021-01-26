package com._3line.gravity.core.security.service.implementation;


import com._3line.gravity.core.usermgt.exception.AppUserServiceException;
import com._3line.gravity.core.usermgt.model.SystemUser;
import com._3line.gravity.core.usermgt.service.ApplicationUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author FortunatusE
 * @date 9/22/2018
 */


@Service("appUserDetailsService")
public class ApplicationUserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationUserDetailsServiceImpl.class);

    private final ApplicationUserService applicationUserService;

    @Autowired
    public ApplicationUserDetailsServiceImpl(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws AppUserServiceException {

        logger.debug("User [{}] logging in", username);

        SystemUser systemUser = applicationUserService.findUserByUsername(username);

        if (systemUser == null) {
            throw new UsernameNotFoundException("Username not found: " + username);
        }
        return new CustomUserPrincipal(systemUser);
    }
}
