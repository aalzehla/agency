package com._3line.gravity.freedom._3linecms.service;

/**
 * Created by FortunatusE on 8/27/2018.
 */
public class CreditResponse {

    private String statusCode;
    private String statusMessage;
    private String balance;


    public CreditResponse() {
    }

    public CreditResponse(String statusCode, String statusMessage, String balance) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.balance = balance;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "CreditResponse{" +
                "statusCode=" + statusCode +
                ", statusMessage='" + statusMessage + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}


