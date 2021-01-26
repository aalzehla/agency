package com._3line.gravity.core.sms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Kwerenachi Utosu
 * @date 4/2/2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendSmsResponse {

    private String responseCode;
    private String responseDescription;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }
}
