package com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody;

public class Beneficiary {

    private Double Amount;
    private String DestinationAccount;

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public String getDestinationAccount() {
        return DestinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        DestinationAccount = destinationAccount;
    }
}
