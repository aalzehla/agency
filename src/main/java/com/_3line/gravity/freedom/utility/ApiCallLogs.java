/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.utility;

import java.util.UUID;

/**
 *
 * @author OlalekanW
 */
public class ApiCallLogs {

    private Object type;
    private Object referenceid;
    private Object sourceId;
    private Object request;
    private Object response;

    public ApiCallLogs() {
    }

    public ApiCallLogs(Object type, Object request) {
        this.referenceid = UUID.randomUUID() + "-" + System.currentTimeMillis();
        this.type = type;
        this.request = request;
    }

    /**
     * @return the type
     */
    public Object getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Object type) {
        this.type = type;
    }

    /**
     * @return the referenceid
     */
    public Object getReferenceid() {
        return referenceid;
    }

    /**
     * @param referenceid the referenceid to set
     */
    public void setReferenceid(Object referenceid) {
        this.referenceid = referenceid;
    }

    /**
     * @return the sourceId
     */
    public Object getSourceId() {
        return sourceId;
    }

    /**
     * @param sourceId the sourceId to set
     */
    public void setSourceId(Object sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * @return the request
     */
    public Object getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(Object request) {
        this.request = request;
    }

    /**
     * @return the response
     */
    public Object getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(Object response) {
        this.response = response;
    }

}
