package com._3line.gravity.freedom.reports.dtos;

public class AgentNibbsActivity {

    private String agentId;
    private String date;
    private String cashInVolume;
    private String cashInValue;
    private String cashOutVolume;
    private String cashOutValue;
    private String acctVolume;
    private String billsPaymentValue;
    private String billsPaymentVolume;
    private String airtimeVolume;
    private String airtimeValue;
    private String ftVolume;
    private String ftValue;
    private String agentCode;

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCashInVolume() {
        return cashInVolume;
    }

    public void setCashInVolume(String cashInVolume) {
        this.cashInVolume = cashInVolume;
    }

    public String getCashInValue() {
        return cashInValue;
    }

    public void setCashInValue(String cashInValue) {
        this.cashInValue = cashInValue;
    }

    public String getCashOutVolume() {
        return cashOutVolume;
    }

    public void setCashOutVolume(String cashOutVolume) {
        this.cashOutVolume = cashOutVolume;
    }

    public String getCashOutValue() {
        return cashOutValue;
    }

    public void setCashOutValue(String cashOutValue) {
        this.cashOutValue = cashOutValue;
    }

    public String getAcctVolume() {
        return acctVolume;
    }

    public void setAcctVolume(String acctVolume) {
        this.acctVolume = acctVolume;
    }

    public String getBillsPaymentVolume() {
        return billsPaymentVolume;
    }

    public void setBillsPaymentVolume(String billsPaymentVolume) {
        this.billsPaymentVolume = billsPaymentVolume;
    }

    public String getAirtimeVolume() {
        return airtimeVolume;
    }

    public void setAirtimeVolume(String airtimeVolume) {
        this.airtimeVolume = airtimeVolume;
    }

    public String getAirtimeValue() {
        return airtimeValue;
    }

    public void setAirtimeValue(String airtimeValue) {
        this.airtimeValue = airtimeValue;
    }

    public String getFtVolume() {
        return ftVolume;
    }

    public void setFtVolume(String ftVolume) {
        this.ftVolume = ftVolume;
    }

    public String getFtValue() {
        return ftValue;
    }

    public void setFtValue(String ftValue) {
        this.ftValue = ftValue;
    }

    public String getBillsPaymentValue() {
        return billsPaymentValue;
    }

    public void setBillsPaymentValue(String billsPaymentValue) {
        this.billsPaymentValue = billsPaymentValue;
    }

    @Override
    public String toString() {
        return "AgentNibbsActivity{" +
                "agentId='" + agentId + '\'' +
                ", date='" + date + '\'' +
                ", cashInVolume='" + cashInVolume + '\'' +
                ", cashInValue='" + cashInValue + '\'' +
                ", cashOutVolume='" + cashOutVolume + '\'' +
                ", cashOutValue='" + cashOutValue + '\'' +
                ", acctVolume='" + acctVolume + '\'' +
                ", billsPaymentValue='" + billsPaymentValue + '\'' +
                ", billsPaymentVolume='" + billsPaymentVolume + '\'' +
                ", airtimeVolume='" + airtimeVolume + '\'' +
                ", airtimeValue='" + airtimeValue + '\'' +
                ", ftVolume='" + ftVolume + '\'' +
                ", ftValue='" + ftValue + '\'' +
                '}';
    }
}
