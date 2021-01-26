package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Kwerenachi Utosu
 * @date 10/15/2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StanbicOTPResponse {

    private StanbicApiResponse apiResponse;
    private StanbicResponseItem responseItem;
    private String token;
    private String session;

    public StanbicApiResponse getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(StanbicApiResponse apiResponse) {
        this.apiResponse = apiResponse;
    }

    public StanbicResponseItem getResponseItem() {
        return responseItem;
    }

    public void setResponseItem(StanbicResponseItem responseItem) {
        this.responseItem = responseItem;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

}
