package com._3line.gravity.api.shared.dto;

public class ApiResponse {
    private String respCode ;
    private String respDesc;
    private Object body ;


    public ApiResponse() {
    }

    public ApiResponse(String respCode, String respDesc, Object body) {
        this.respCode = respCode;
        this.respDesc = respDesc;
        this.body = body;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
