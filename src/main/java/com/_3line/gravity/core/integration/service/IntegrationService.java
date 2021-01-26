package com._3line.gravity.core.integration.service;


import com._3line.gravity.core.integration.dto.activedirectory.ActiveDirectoryRequest;
import com._3line.gravity.core.integration.dto.activedirectory.ActiveDirectoryResponse;
import com._3line.gravity.core.models.Response;

/**
 * @author FortunatusE
 * @date 11/19/2018
 */
public interface IntegrationService {


    ActiveDirectoryResponse performActiveDirectoryAuthentication(ActiveDirectoryRequest activeDirectoryRequest);

    Response doPost(String url , Object jsonBody );


}
