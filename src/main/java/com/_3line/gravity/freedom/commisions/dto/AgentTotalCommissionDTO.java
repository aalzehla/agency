package com._3line.gravity.freedom.commisions.dto;

import java.math.BigDecimal;

/**
 * Created by FortunatusE on 8/9/2018.
 */

public class AgentTotalCommissionDTO{

    private Long id;
    private String agentName;
    private String accountNumber;
    private String bank;
    private BigDecimal totalAmount;
    private String tranDate;
    private boolean paid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
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

        AgentTotalCommissionDTO that = (AgentTotalCommissionDTO) o;

        return accountNumber.equals(that.accountNumber);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + accountNumber.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AgentTotalCommissionDTO{" +
                "id=" + id +
                ", agentName='" + agentName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", bank='" + bank + '\'' +
                ", totalAmount=" + totalAmount +
                ", tranDate=" + tranDate +
                ", paid=" + paid +
                '}';
    }
}
