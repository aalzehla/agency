package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IntraBankTransferResponse extends BaseResponse {

    public IntraBankTransferResponse() {

    }

    @JsonProperty(value = "TransactionReference")
    private String transactionReference;

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

}
