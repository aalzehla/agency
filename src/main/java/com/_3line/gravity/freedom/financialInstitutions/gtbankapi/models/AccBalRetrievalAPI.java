package com._3line.gravity.freedom.financialInstitutions.gtbankapi.models;

public class AccBalRetrievalAPI {

    private String customerId;
    private String password;
    private String userName;

    private String accountNumber;
    private String currency;
    private String avaliableBalance;
    private String ledgerBalance;
    private String responseCode;
    private String responseDesc;
    private String time;
    private String hash;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAvaliableBalance() {
        return avaliableBalance;
    }

    public void setAvaliableBalance(String avaliableBalance) {
        this.avaliableBalance = avaliableBalance;
    }

    public String getLedgerBalance() {
        return ledgerBalance;
    }

    public void setLedgerBalance(String ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDesc() {
        return responseDesc;
    }

    public void setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "AccBalRetrieval{" +
                "customerId='" + customerId + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", currency='" + currency + '\'' +
                ", avaliableBalance=" + avaliableBalance +
                ", ledgerBalance=" + ledgerBalance +
                ", responseCode='" + responseCode + '\'' +
                ", responseDesc='" + responseDesc + '\'' +
                ", time='" + time + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}
