package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InterBankTransferResponse extends BaseResponse {

    public InterBankTransferResponse() {

    }

    @JsonProperty(value = "ReferenceNo")
    private String ReferenceNo;

    public String getReferenceNo() {
        return ReferenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        ReferenceNo = referenceNo;
    }

}
