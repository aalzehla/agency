/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody.AccountBalance;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author NiyiO
 */
public class AccountBalanceResponse extends BaseResponse {

    public AccountBalanceResponse() {

    }

    @JsonProperty
    private AccountBalance accountBalance;

    public AccountBalance getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(AccountBalance accountBalance) {
        this.accountBalance = accountBalance;
    }

}
