package com._3line.gravity.freedom.bankdetails.model;


import javax.persistence.*;

@Entity
public class FreedomCommision {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private BankDetails bankDetails ;
    @Column(nullable = false)
    private boolean percentageDriven = true;

    private String amount = "0" ;

    private String _3lineCommission = "0";

    private String agentCommission = "0";

    private String bankCommission = "0";
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType = TransactionType.SAVING ;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BankDetails getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(BankDetails bankDetails) {
        this.bankDetails = bankDetails;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "FreedomCommision{" +
                "id=" + id +
                ", bankDetails=" + bankDetails +
                ", percentageDriven=" + percentageDriven +
                ", amount='" + amount + '\'' +
                ", _3lineCommission='" + _3lineCommission + '\'' +
                ", agentCommission='" + agentCommission + '\'' +
                ", bankCommission='" + bankCommission + '\'' +
                ", transactionType=" + transactionType +
                '}';
    }
}
