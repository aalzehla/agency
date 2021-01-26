package com._3line.gravity.core.integration.service;

import com._3line.gravity.core.integration.dto.activedirectory.ActiveDirectoryRequest;
import com._3line.gravity.core.integration.dto.activedirectory.ActiveDirectoryResponse;
import com._3line.gravity.core.integration.exception.ActiveDirectoryException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * @author FortunatusE
 * @date 11/19/2018
 */


@Service
public class IntegrationServiceImpl implements IntegrationService {

    private static final Logger logger = LoggerFactory.getLogger(IntegrationServiceImpl.class);

    private final RestTemplate restTemplate;

    @Value("${base.url}")
    private String BASE_URL;

    @Value("${endpoint.active.directory}")
    private String ACTIVE_DIRECTORY_ENDPOINT;

    @Autowired
    public IntegrationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ActiveDirectoryResponse performActiveDirectoryAuthentication(ActiveDirectoryRequest activeDirectoryRequest) {

        logger.debug("Performing active directory authentication");
        ActiveDirectoryResponse response;

        try {
            String url = BASE_URL + ACTIVE_DIRECTORY_ENDPOINT;
            logger.debug("Making API call to {}", url);
            response = restTemplate.postForObject(url, activeDirectoryRequest, ActiveDirectoryResponse.class);
            logger.debug("Response received: {}", response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ActiveDirectoryException("Error performing authentication", e);
        }

        if (response == null) {
            throw new ActiveDirectoryException("Failed to perform authentication");
        }
        return response;

    }

    @Override
    public Response doPost(String url, Object jsonBody) {
        logger.info("Sending to integration layer on url......... {}", url);
        Response response = validateRequestFields(jsonBody);
        if (response != null) {
            return response;
        }
        log(url, jsonBody);
        try {
            response = restTemplate.postForObject(url, jsonBody, Response.class);
            logger.info("response  {}", response.toString());
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response = new Response();
            response.setRespCode("999");
            response.setRespDescription(e.getMessage());
            return response;

        }
    }

    private void log(String url, Object request) {
        logger.debug("Making API call to {} with request {}", url, request);
    }

    private Response validateRequestFields(Object object) {
        logger.debug("Validating request fields");
        Response response = null;
        String errorMessage = ValidationUtils.validateObject(object);
        if (!errorMessage.isEmpty()) {
            response = new Response();
            response.setRespCode("999");
            response.setRespDescription(errorMessage);
        }
        return response;
    }
}
