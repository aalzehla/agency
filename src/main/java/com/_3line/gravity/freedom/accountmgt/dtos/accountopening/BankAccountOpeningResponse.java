package com._3line.gravity.freedom.accountmgt.dtos.accountopening;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

/**
 * @author JoyU
 * @date 11/14/2018
 */
@ToString
public class BankAccountOpeningResponse{

    @JsonProperty("accountNumberField")
    private String accountNumber;
    @JsonProperty("cifIdField")
    private String cifId;
    @JsonProperty("ReferenceNo")
    private String referenceNo;
    @JsonProperty("responseCodeField")
    private String responseCode;
    @JsonProperty("errorMessageField")
    private String errorMessage;
    @JsonProperty("responseMessageField")
    private String responseMessage;


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCifId() {
        return cifId;
    }

    public void setCifId(String cifId) {
        this.cifId = cifId;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
