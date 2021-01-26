package com._3line.gravity.freedom.bankdetails.dtos;

public class FreedomCommisionDTO {

    private Long id;
    private boolean percentageDriven;
    private String amount;
    private String _3lineCommission ;
    private String agentCommission;
    private String bankCommission;
    private String transactionType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public boolean isPercentageDriven() {
        return percentageDriven;
    }

    public void setPercentageDriven(boolean percentageDriven) {
        this.percentageDriven = percentageDriven;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String get_3lineCommission() {
        return _3lineCommission;
    }

    public void set_3lineCommission(String _3lineCommission) {
        this._3lineCommission = _3lineCommission;
    }

    public String getAgentCommission() {
        return agentCommission;
    }

    public void setAgentCommission(String agentCommission) {
        this.agentCommission = agentCommission;
    }

    public String getBankCommission() {
        return bankCommission;
    }

    public void setBankCommission(String bankCommission) {
        this.bankCommission = bankCommission;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
