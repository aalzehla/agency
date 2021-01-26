package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountSummary {

    @JsonProperty(value = "AccountNumber")
    private String accountNumber;

    @JsonProperty(value = "AccountName")
    private String accountName;

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

    @Override
    public String toString() {
        return "{" + "accountNumber=" + accountNumber + ", accountName=" + accountName + '}';
    }

}
