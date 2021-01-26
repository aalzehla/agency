package com._3line.gravity.freedom.commisions.models;



import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.bankdetails.model.TransactionType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Entity
public class GravityDailyCommission extends AbstractEntity {

    String transactionAmount ;
    String transactionId;
    String transactionChannel;
    String agentName;
    String agentBank;
    String agentBankCode;
    String agentCommission;
    String parentAgentCommission;
    String _3lineCommission;
    String bankCommission;

    TransactionType transactionType;

    Date tranDate = new Date() ;
    String agentAccount ;
    boolean paid = false ;
    String reversed = "FALSE";


    public String getReversed() {
        return reversed;
    }

    public void setReversed(String reversed) {
        this.reversed = reversed;
    }

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

    public Date getTranDate() {
        return tranDate;
    }

    public void setTranDate(Date tranDate) {
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

    public String getParentAgentCommission() {
        return parentAgentCommission;
    }

    public void setParentAgentCommission(String parentAgentCommission) {
        this.parentAgentCommission = parentAgentCommission;
    }

    public String getTransactionChannel() {
        return transactionChannel;
    }

    public void setTransactionChannel(String transactionChannel) {
        this.transactionChannel = transactionChannel;
    }

    @Override
    public String toString() {
        return "GravityDailyCommission{" +
                "transactionAmount='" + transactionAmount + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", transactionChannel='" + transactionChannel + '\'' +
                ", agentName='" + agentName + '\'' +
                ", agentBank='" + agentBank + '\'' +
                ", agentBankCode='" + agentBankCode + '\'' +
                ", agentCommission='" + agentCommission + '\'' +
                ", parentAgentCommission='" + parentAgentCommission + '\'' +
                ", _3lineCommission='" + _3lineCommission + '\'' +
                ", bankCommission='" + bankCommission + '\'' +
                ", transactionType=" + transactionType +
                ", tranDate=" + tranDate +
                ", agentAccount='" + agentAccount + '\'' +
                ", paid=" + paid +
                ", reversed='" + reversed + '\'' +
                '}';
    }
}
