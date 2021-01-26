package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response;

import com._3line.gravity.freedom.financialInstitutions.fidelity.models.BaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StanbicAccountBalanceResponse extends BaseResponse {

    public StanbicAccountBalanceResponse() {

    }

    @JsonProperty
    private StanbicAccountBalance accountBalance;

    public StanbicAccountBalance getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(StanbicAccountBalance accountBalance) {
        this.accountBalance = accountBalance;
    }

}