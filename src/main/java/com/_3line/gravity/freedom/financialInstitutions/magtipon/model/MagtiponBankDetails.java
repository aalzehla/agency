package com._3line.gravity.freedom.financialInstitutions.magtipon.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MagtiponBankDetails {
    @JsonProperty("BankType")
    String bankType;
    @JsonProperty("bankCode")
    String bankCode ;
    @JsonProperty("AccountNumber")
    String accountNumber;
    @JsonProperty("AccountType")
    String accountType ;

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
