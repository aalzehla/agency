package com._3line.gravity.freedom.service.settlementreport.model;


import lombok.Data;

@Data
public class SettlementReportDto {

    private String settlementDate;
    private String acquiringBank ;
    private String acquirerFee;
    private String totalAcquirerFee;
    private String transactionCount;
    private String totalFailed;
    private String totalSuccessful;
    private String totalTransactionAmount;
    private String expectedSettlementAmount;

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getAcquiringBank() {
        return acquiringBank;
    }

    public void setAcquiringBank(String acquiringBank) {
        this.acquiringBank = acquiringBank;
    }

    public String getAcquirerFee() {
        return acquirerFee;
    }

    public void setAcquirerFee(String acquirerFee) {
        this.acquirerFee = acquirerFee;
    }

    public String getTotalAcquirerFee() {
        return totalAcquirerFee;
    }

    public void setTotalAcquirerFee(String totalAcquirerFee) {
        this.totalAcquirerFee = totalAcquirerFee;
    }

    public String getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(String transactionCount) {
        this.transactionCount = transactionCount;
    }

    public String getTotalFailed() {
        return totalFailed;
    }

    public void setTotalFailed(String totalFailed) {
        this.totalFailed = totalFailed;
    }

    public String getTotalSuccessful() {
        return totalSuccessful;
    }

    public void setTotalSuccessful(String totalSuccessful) {
        this.totalSuccessful = totalSuccessful;
    }

    public String getTotalTransactionAmount() {
        return totalTransactionAmount;
    }

    public void setTotalTransactionAmount(String totalTransactionAmount) {
        this.totalTransactionAmount = totalTransactionAmount;
    }

    public String getExpectedSettlementAmount() {
        return expectedSettlementAmount;
    }

    public void setExpectedSettlementAmount(String expectedSettlementAmount) {
        this.expectedSettlementAmount = expectedSettlementAmount;
    }
}
