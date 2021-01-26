package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LeanAccountResponse {

    @JsonProperty(value = "GravityResponse")
    private String Response;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        Response = Response;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LeanAccountResponse{");
        sb.append("GravityResponse=").append(Response);
        sb.append('}');
        return sb.toString();
    }
}
