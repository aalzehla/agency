package com._3line.gravity.freedom.financialInstitutions.magtipon.model;

import com._3line.gravity.core.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;


@Entity
public class MagtiponLogs extends AbstractEntity {

    private String service ;
    @Lob
    private String headers ;
    @Lob
    private String request ;
    @Lob
    private String response ;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
