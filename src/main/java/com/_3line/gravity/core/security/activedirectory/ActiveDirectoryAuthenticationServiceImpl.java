package com._3line.gravity.core.security.activedirectory;


import com._3line.gravity.core.integration.dto.activedirectory.ActiveDirectoryRequest;
import com._3line.gravity.core.integration.dto.activedirectory.ActiveDirectoryResponse;
import com._3line.gravity.core.integration.exception.ActiveDirectoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.stereotype.Service;
import com._3line.gravity.core.integration.service.IntegrationService;


/**
 * @author FortunatusE
 * @date 11/19/2018
 */

@Service
public class ActiveDirectoryAuthenticationServiceImpl implements ActiveDirectoryAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(ActiveDirectoryAuthenticationServiceImpl.class);

    private final IntegrationService integrationService;

    @Autowired
    public ActiveDirectoryAuthenticationServiceImpl(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @Override
    public boolean authenticate(String username, String password, String token) {

        logger.debug("Authenticating user [{}] against Active Directory", username);

        ActiveDirectoryRequest activeDirectoryRequest = new ActiveDirectoryRequest();
        activeDirectoryRequest.setStaffId(username);
        activeDirectoryRequest.setPassword(password);
        activeDirectoryRequest.setRequestNumber(token);

        try {
            ActiveDirectoryResponse activeDirectoryResponse = integrationService.performActiveDirectoryAuthentication(activeDirectoryRequest);
            //todo check if successful;
            return true;
        }
        catch (ActiveDirectoryException e){
            throw new AuthenticationServiceException("Authentication service unavailable");
        }

    }
}
