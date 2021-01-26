/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author NiyiO
 */
public class AccountStatementResponse extends BaseResponse {

    public AccountStatementResponse() {

    }

    @JsonProperty("acountStatement")
    private AccountStatement accountStatement;

    public AccountStatement getAccountStatement() {
        return accountStatement;
    }

    public void setAccountStatement(AccountStatement accountStatement) {
        this.accountStatement = accountStatement;
    }

}
