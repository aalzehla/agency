/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author NiyiO
 */
public class ValidateAccount {

    @JsonProperty(value = "AccountNumber")
    private String accountNumber;

    @JsonProperty(value = "AccountName")
    private String accountName;

    @JsonProperty(value = "BVN")
    private String bvn;

    @JsonProperty(value = "IsValid")
    private boolean valid;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "ValidateAccount{" + "accountNumber=" + accountNumber + ", accountName=" + accountName + ", bvn=" + bvn + ", valid=" + valid + '}';
    }

}
