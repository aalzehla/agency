package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;


import com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody.Beneficiary;

import java.util.List;

public class SingleDebitMultipleCreditRequest extends BaseRequest {

    private String sourceAccountNumber;

    private String narration;

    private List<Beneficiary> beneficiaryList;

    private Integer productId;

    private String customerPin;

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public List<Beneficiary> getBeneficiaryList() {
        return beneficiaryList;
    }

    public void setBeneficiaryList(List<Beneficiary> beneficiaryList) {
        this.beneficiaryList = beneficiaryList;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getCustomerPin() {
        return customerPin;
    }

    public void setCustomerPin(String customerPin) {
        this.customerPin = customerPin;
    }

}
