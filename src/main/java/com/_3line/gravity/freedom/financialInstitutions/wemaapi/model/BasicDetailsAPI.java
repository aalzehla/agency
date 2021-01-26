package com._3line.gravity.freedom.financialInstitutions.wemaapi.model;

public class BasicDetailsAPI {

    private String superAgentId;
    private String subAgentId;
    private String tranReference;
    private String when;
    private String securitySessionKey;
    private String responseCode;
    private String responseDesc;
    private String tranId;
    private String tranType;
    private String tranDate;
    private String branchDescription;
    private String branchCode;

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

    public String getBranchDescription() {
        return branchDescription;
    }

    public void setBranchDescription(String branchDescription) {
        this.branchDescription = branchDescription;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    @Override
    public String toString() {
        return "BasicDetailsAPI{" +
                "superAgentId='" + superAgentId + '\'' +
                ", subAgentId='" + subAgentId + '\'' +
                ", tranReference='" + tranReference + '\'' +
                ", when='" + when + '\'' +
                ", securitySessionKey='" + securitySessionKey + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseDesc='" + responseDesc + '\'' +
                ", tranId='" + tranId + '\'' +
                ", tranType='" + tranType + '\'' +
                ", tranDate='" + tranDate + '\'' +
                ", branchDescription='" + branchDescription + '\'' +
                ", branchCode='" + branchCode + '\'' +
                '}';
    }
}
