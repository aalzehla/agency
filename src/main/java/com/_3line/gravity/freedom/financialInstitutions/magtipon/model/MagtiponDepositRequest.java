package com._3line.gravity.freedom.financialInstitutions.magtipon.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MagtiponDepositRequest {

    @JsonProperty("Amount")
    String amount;
    @JsonProperty("RequestRef")
    String requestRef;
    @JsonProperty("CustomerDetails")
    MagtiponCustomerDetails customerDetails;
    @JsonProperty("BeneficiaryDetails")
    MagtiponBeneficiaryDetails beneficiaryDetails;
    @JsonProperty("BankDetails")
    MagtiponBankDetails bankDetails;
    @JsonProperty("Signature")
    String signature;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRequestRef() {
        return requestRef;
    }

    public void setRequestRef(String requestRef) {
        this.requestRef = requestRef;
    }

    public MagtiponCustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(MagtiponCustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }



    public MagtiponBeneficiaryDetails getBeneficiaryDetails() {
        return beneficiaryDetails;
    }

    public void setBeneficiaryDetails(MagtiponBeneficiaryDetails beneficiaryDetails) {
        this.beneficiaryDetails = beneficiaryDetails;
    }

    public MagtiponBankDetails getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(MagtiponBankDetails bankDetails) {
        this.bankDetails = bankDetails;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
