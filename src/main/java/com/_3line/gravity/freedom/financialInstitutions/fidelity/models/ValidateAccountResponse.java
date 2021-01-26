/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody.ValidateAccount;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author NiyiO
 */
public class ValidateAccountResponse extends BaseResponse {

    public ValidateAccountResponse() {
    }

    @JsonProperty("validateAccount")
    private ValidateAccount validateAccount;

    public ValidateAccount getValidateAccount() {
        return validateAccount;
    }

    public void setValidateAccount(ValidateAccount validateAccount) {
        this.validateAccount = validateAccount;
    }

}
