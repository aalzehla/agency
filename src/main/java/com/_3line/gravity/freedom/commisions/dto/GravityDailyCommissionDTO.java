package com._3line.gravity.freedom.commisions.dto;


import com._3line.gravity.freedom.bankdetails.model.TransactionType;

public class GravityDailyCommissionDTO{

    private Long id;
    private String transactionId;
    private TransactionType transactionType;
    private String transactionAmount;
    private String agentName ;
    private String agentAccount ;
    private String agentBank ;
    private String agentBankCode;
    private String agentCommission;
    private String bankCommission ;
    private String _3lineCommission ;
    private String tranDate;
    boolean paid = false ;


    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentBank() {
        return agentBank;
    }

    public void setAgentBank(String agentBank) {
        this.agentBank = agentBank;
    }

    public String getAgentCommission() {
        return agentCommission;
    }

    public void setAgentCommission(String agentCommission) {
        this.agentCommission = agentCommission;
    }

    public String get_3lineCommission() {
        return _3lineCommission;
    }

    public void set_3lineCommission(String _3lineCommission) {
        this._3lineCommission = _3lineCommission;
    }

    public String getBankCommission() {
        return bankCommission;
    }

    public void setBankCommission(String bankCommission) {
        this.bankCommission = bankCommission;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getAgentAccount() {
        return agentAccount;
    }

    public void setAgentAccount(String agentAccount) {
        this.agentAccount = agentAccount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getAgentBankCode() {
        return agentBankCode;
    }

    public void setAgentBankCode(String agentBankCode) {
        this.agentBankCode = agentBankCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GravityDailyCommission{" +
                "transactionAmount='" + transactionAmount + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", agentName='" + agentName + '\'' +
                ", agentBank='" + agentBank + '\'' +
                ", agentCommission='" + agentCommission + '\'' +
                ", _3lineCommission='" + _3lineCommission + '\'' +
                ", bankCommission='" + bankCommission + '\'' +
                ", transactionType=" + transactionType +
                ", tranDate=" + tranDate +
                ", agentAccount='" + agentAccount + '\'' +
                ", paid=" + paid +
                '}';
    }
}
