package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response;


import com._3line.gravity.freedom.financialInstitutions.fidelity.models.BaseResponse;

public class StanbicAccountCreationResponse extends BaseResponse {

    private String accountNumber;


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
