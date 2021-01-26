package com._3line.gravity.freedom.commisions.models;


import com._3line.gravity.core.entity.AbstractEntity;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by FortunatusE on 8/9/2018.
 */

@Entity
public class AgentTotalCommission extends AbstractEntity {

    private String accountNumber;
    private String agentName;
    private String bank;
    private BigDecimal totalAmount;
    private Date tranDate;
    private boolean paid;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getTranDate() {
        return tranDate;
    }

    public void setTranDate(Date tranDate) {
        this.tranDate = tranDate;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AgentTotalCommission that = (AgentTotalCommission) o;

        return accountNumber.equals(that.accountNumber);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + accountNumber.hashCode();
        return result;
    }
}
