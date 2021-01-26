package com._3line.gravity.freedom.financialInstitutions.gtbankapi.models;

/**
 * @author JoyU
 * @date 1/17/2019
 */
public class GetBVNDetailsResponse {

    private String requestId;
    private String responseCode;
    private String responseDescription;
    private ResponseObject responseObject;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

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

    public ResponseObject getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(ResponseObject responseObject) {
        this.responseObject = responseObject;
    }
}
