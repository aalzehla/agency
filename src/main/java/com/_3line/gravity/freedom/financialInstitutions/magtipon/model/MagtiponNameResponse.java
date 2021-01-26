package com._3line.gravity.freedom.financialInstitutions.magtipon.model;


public class MagtiponNameResponse {

    String AccountName;

    String ResponseCode;


    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    @Override
    public String toString() {
        return "MagtiponNameResponse{" +
                "AccountName='" + AccountName + '\'' +
                ", ResponseCode='" + ResponseCode + '\'' +
                '}';
    }
}
