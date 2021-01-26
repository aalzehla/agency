package com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardResponse {

    @JsonProperty
    private boolean IsSuccessful;
    @JsonProperty
    private String ErrorMessage;

    public boolean isSuccessful() {
        return IsSuccessful;
    }

    public void setSuccessful(boolean successful) {
        IsSuccessful = successful;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
