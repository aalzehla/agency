package com._3line.gravity.freedom.financialInstitutions.wemaapi.model;

/**
 * @author JoyU
 * @date 9/18/2018
 */
public class DepositAndWithdrawal {

    private String superAgentId;
    private String subAgentId;
    private String tranReference;
    private String when;
    private String securitySessionKey;
    private String customerAccToCredit;
    private String customerAccToDebit;
    private String amount;
    private String password;
    private String token;
    private String responseCode;
    private String responseDesc;
    private String tranId;
    private String tranType;
    private String tranDate;

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

    public String getCustomerAccToCredit() {
        return customerAccToCredit;
    }

    public void setCustomerAccToCredit(String customerAccToCredit) {
        this.customerAccToCredit = customerAccToCredit;
    }

    public String getCustomerAccToDebit() {
        return customerAccToDebit;
    }

    public void setCustomerAccToDebit(String customerAccToDebit) {
        this.customerAccToDebit = customerAccToDebit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    @Override
    public String toString() {
        return "DepositAndWithdrawal{" +
                "superAgentId='" + superAgentId + '\'' +
                ", subAgentId='" + subAgentId + '\'' +
                ", tranReference='" + tranReference + '\'' +
                ", when='" + when + '\'' +
                ", securitySessionKey='" + securitySessionKey + '\'' +
                ", customerAccToCredit='" + customerAccToCredit + '\'' +
                ", customerAccToDebit='" + customerAccToDebit + '\'' +
                ", amount='" + amount + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseDesc='" + responseDesc + '\'' +
                ", tranId='" + tranId + '\'' +
                ", tranType='" + tranType + '\'' +
                ", tranDate='" + tranDate + '\'' +
                '}';
    }
}
