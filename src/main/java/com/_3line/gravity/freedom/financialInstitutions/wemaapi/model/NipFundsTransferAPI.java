package com._3line.gravity.freedom.financialInstitutions.wemaapi.model;

/**
 * @author JoyU
 * @date 9/18/2018
 */
public class NipFundsTransferAPI {

    private String superAgentId;
    private String subAgentId;
    private String tranReference;
    private String when;
    private String securitySessionKey;
    private String channelCode;
    private String destinationBankCode;
    private String destinationAccountNumber;
    private String nameResponse;
    private String amount;
    private String responseCode;
    private String responseDesc;
    private String tranId;
    private String tranType;
    private String tranDate;
    private String sessionId;

    public String getSuperAgentId() {
        return superAgentId;
    }

    public void setSuperAgentId(String superAgentId) {
        this.superAgentId = superAgentId;
    }

    public String getSubAgentId() {
        return subAgentId;
    }

    public void setSubAgentId(String subAgentId) {
        this.subAgentId = subAgentId;
    }

    public String getTranReference() {
        return tranReference;
    }

    public void setTranReference(String tranReference) {
        this.tranReference = tranReference;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getSecuritySessionKey() {
        return securitySessionKey;
    }

    public void setSecuritySessionKey(String securitySessionKey) {
        this.securitySessionKey = securitySessionKey;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getDestinationBankCode() {
        return destinationBankCode;
    }

    public void setDestinationBankCode(String destinationBankCode) {
        this.destinationBankCode = destinationBankCode;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public String getNameResponse() {
        return nameResponse;
    }

    public void setNameResponse(String nameResponse) {
        this.nameResponse = nameResponse;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "NipFundsTransferAPI{" +
                "superAgentId='" + superAgentId + '\'' +
                ", subAgentId='" + subAgentId + '\'' +
                ", tranReference='" + tranReference + '\'' +
                ", when='" + when + '\'' +
                ", securitySessionKey='" + securitySessionKey + '\'' +
                ", channelCode='" + channelCode + '\'' +
                ", destinationBankCode='" + destinationBankCode + '\'' +
                ", destinationAccountNumber='" + destinationAccountNumber + '\'' +
                ", nameResponse='" + nameResponse + '\'' +
                ", amount='" + amount + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseDesc='" + responseDesc + '\'' +
                ", tranId='" + tranId + '\'' +
                ", tranType='" + tranType + '\'' +
                ", tranDate='" + tranDate + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
