package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by JohnA on 16-Jan-18.
 */
public class BeneficiaryList {

    @JsonProperty("Amount")
    private Double  Amount;
    @JsonProperty("DestinationAccount")
    private String DestinationAccount;

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double Amount) {
        Amount = Amount;
    }

    public String getDestinationAccount() {
        return DestinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        DestinationAccount = destinationAccount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BeneficiaryList{");
        sb.append("Amount=").append(Amount);
        sb.append(", DestinationAccount='").append(DestinationAccount).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
