package com._3line.gravity.freedom.dashboard.models;

import com._3line.gravity.freedom.agents.dtos.AgentDto;
import lombok.ToString;

import java.util.List;

@ToString
public class Dashboard {

    private List<TopAgent> topAgents;
    private Long totalTransactions= Long.valueOf(0);
    private Long totalDeposits= Long.valueOf(0);
    private Long totalWithdrawals= Long.valueOf(0);
    private Long totalTransfers= Long.valueOf(0);
    private Long totalBillPayments= Long.valueOf(0);
    private List<TopBiller> topBillers ;
    private Double totalAgentEarnings= Double.valueOf(0);
    private Double totalAgentMonthEarnings= Double.valueOf(0);
    private Double totalAgentpreviousMonthEarnings= Double.valueOf(0);
    private Double total3lineEarnings= Double.valueOf(0);
    private Double total3lineEarningMonth= Double.valueOf(0);
    private Double total3lineEarningPrevMonth= Double.valueOf(0);
    private Double incomeToday= Double.valueOf(0);
    private Double incomeYesterday= Double.valueOf(0);
    private Long dailyDeposit= Long.valueOf(0);
    private Long dailyWithdrawals= Long.valueOf(0);
    private Long dailyBillPayments= Long.valueOf(0);
    private Long dailyTransactions= Long.valueOf(0);
    private Long dailyRecharge = Long.valueOf(0);


    public Long getDailyRecharge() {
        return dailyRecharge;
    }

    public void setDailyRecharge(Long dailyRecharge) {
        this.dailyRecharge = dailyRecharge;
    }

    public Long getDailyDeposit() {
        return dailyDeposit;
    }

    public void setDailyDeposit(Long dailyDeposit) {
        this.dailyDeposit = dailyDeposit;
    }

    public Long getDailyWithdrawals() {
        return dailyWithdrawals;
    }

    public void setDailyWithdrawals(Long dailyWithdrawals) {
        this.dailyWithdrawals = dailyWithdrawals;
    }

    public Long getDailyBillPayments() {
        return dailyBillPayments;
    }

    public void setDailyBillPayments(Long dailyBillPayments) {
        this.dailyBillPayments = dailyBillPayments;
    }

    public Long getDailyTransactions() {
        return dailyTransactions;
    }

    public void setDailyTransactions(Long dailyTransactions) {
        this.dailyTransactions = dailyTransactions;
    }

    public List<TopAgent> getTopAgents() {
        return topAgents;
    }

    public void setTopAgents(List<TopAgent> topAgents) {
        this.topAgents = topAgents;
    }

    public Long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public Long getTotalDeposits() {
        return totalDeposits;
    }

    public void setTotalDeposits(Long totalDeposits) {
        this.totalDeposits = totalDeposits;
    }

    public Long getTotalWithdrawals() {
        return totalWithdrawals;
    }

    public void setTotalWithdrawals(Long totalWithdrawals) {
        this.totalWithdrawals = totalWithdrawals;
    }

    public Long getTotalTransfers() {
        return totalTransfers;
    }

    public void setTotalTransfers(Long totalTransfers) {
        this.totalTransfers = totalTransfers;
    }

    public Long getTotalBillPayments() {
        return totalBillPayments;
    }

    public void setTotalBillPayments(Long totalBillPayments) {
        this.totalBillPayments = totalBillPayments;
    }

    public List<TopBiller> getTopBillers() {
        return topBillers;
    }

    public void setTopBillers(List<TopBiller> topBillers) {
        this.topBillers = topBillers;
    }

    public Double getTotalAgentEarnings() {
        return totalAgentEarnings;
    }

    public void setTotalAgentEarnings(Double totalAgentEarnings) {
        this.totalAgentEarnings = totalAgentEarnings;
    }

    public Double getTotalAgentMonthEarnings() {
        return totalAgentMonthEarnings;
    }

    public void setTotalAgentMonthEarnings(Double totalAgentMonthEarnings) {
        this.totalAgentMonthEarnings = totalAgentMonthEarnings;
    }

    public Double getTotalAgentpreviousMonthEarnings() {
        return totalAgentpreviousMonthEarnings;
    }

    public void setTotalAgentpreviousMonthEarnings(Double totalAgentpreviousMonthEarnings) {
        this.totalAgentpreviousMonthEarnings = totalAgentpreviousMonthEarnings;
    }

    public Double getTotal3lineEarnings() {
        return total3lineEarnings;
    }

    public void setTotal3lineEarnings(Double total3lineEarnings) {
        this.total3lineEarnings = total3lineEarnings;
    }

    public Double getTotal3lineEarningMonth() {
        return total3lineEarningMonth;
    }

    public void setTotal3lineEarningMonth(Double total3lineEarningMonth) {
        this.total3lineEarningMonth = total3lineEarningMonth;
    }

    public Double getTotal3lineEarningPrevMonth() {
        return total3lineEarningPrevMonth;
    }

    public void setTotal3lineEarningPrevMonth(Double total3lineEarningPrevMonth) {
        this.total3lineEarningPrevMonth = total3lineEarningPrevMonth;
    }

    public Double getIncomeToday() {
        return incomeToday;
    }

    public void setIncomeToday(Double incomeToday) {
        this.incomeToday = incomeToday;
    }

    public Double getIncomeYesterday() {
        return incomeYesterday;
    }

    public void setIncomeYesterday(Double incomeYesterday) {
        this.incomeYesterday = incomeYesterday;
    }
}
