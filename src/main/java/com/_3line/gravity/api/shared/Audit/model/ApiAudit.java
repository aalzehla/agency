package com._3line.gravity.api.shared.Audit.model;

import com._3line.gravity.core.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

/**
 * @author Kwerenachi Utosu
 * @date 4/2/2019
 */
@Entity
public class ApiAudit extends AbstractEntity {

    @Enumerated(value = EnumType.STRING)
    private ApiType apiType;
    private String tranRef;
    private String responseCode;
    private String responseDescription;
    @Lob
    private String requestPayload;
    @Lob
    private String responsePayload;

    public String getTranRef() {
        return tranRef;
    }

    public void setTranRef(String tranRef) {
        this.tranRef = tranRef;
    }

    public ApiType getApiType() {
        return apiType;
    }

    public void setApiType(ApiType apiType) {
        this.apiType = apiType;
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

    public String getRequestPayload() {
        return requestPayload;
    }

    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }

    public String getResponsePayload() {
        return responsePayload;
    }

    public void setResponsePayload(String responsePayload) {
        this.responsePayload = responsePayload;
    }
}
