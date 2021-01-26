package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by JohnA on 16-Jan-18.
 */
public class CreateAccountResponse extends BaseResponse {

    public CreateAccountResponse() {

    }

    @JsonProperty("AccountNumber")
    private String accountNumber;

    @JsonProperty("RevalidationID")
    private Integer revalidationId;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getRevalidationId() {
        return revalidationId;
    }

    public void setRevalidationId(Integer revalidationId) {
        this.revalidationId = revalidationId;
    }

}
