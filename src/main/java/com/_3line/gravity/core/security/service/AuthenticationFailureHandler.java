package com._3line.gravity.core.security.service;

import com._3line.gravity.core.usermgt.model.SystemUser;
import com._3line.gravity.core.usermgt.repository.ApplicationUserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author FortunatusE
 * @date 11/28/2018
 */

@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private Logger logger = LoggerFactory.getLogger(AuthenticationFailureHandler.class);

    @Autowired
    private ApplicationUserRepository userRepository;

    @Autowired
    private LoginService loginService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        setDefaultFailureUrl("/core/login?failed=true");
        String usernameAndToken = request.getParameter("username");
        String[] strings = StringUtils.split(usernameAndToken, ":");


        if(strings != null && strings.length==2) {
            String username = strings[0];
            logger.info("user {}",username);
            SystemUser systemUser = userRepository.findByUserName(username);
            if (systemUser != null) {
                loginService.updateUserLoginAttempt(systemUser);
                logger.error("Failed authentication for user [{}] due to {}", username, exception.getMessage());
            }
        }

        logger.error("Failed authentication using credentials: {} due to", usernameAndToken, exception.getMessage());
        super.onAuthenticationFailure(request, response, exception);
        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception.getMessage());
    }




}
