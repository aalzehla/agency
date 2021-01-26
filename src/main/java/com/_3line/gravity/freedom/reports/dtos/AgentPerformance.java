package com._3line.gravity.freedom.reports.dtos;

import lombok.Data;

@Data
public class AgentPerformance {

    private String agentId;
    private String agentName;
    private String agreegator;
    private String terminal;
    private String totalDepositsValue;
    private String totalWithdrawalsValue;
    private String transfersValue;
    private String totalDeposits;
    private String totalWithdrawals;
    private String totalTransactions;
    private String billsPayments;
    private String billsPaymentsValue;
    private String walletBalance;
    private String incomeBalance;

}
