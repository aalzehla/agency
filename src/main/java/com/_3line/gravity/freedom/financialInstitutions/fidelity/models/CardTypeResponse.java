package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CardTypeResponse extends BaseResponse {

    public CardTypeResponse() {

    }

    @JsonProperty("paymentCardType")
    private List<PaymentCardType> paymentCardType;

    public List<PaymentCardType> getPaymentCardType() {
        return paymentCardType;
    }

    public void setPaymentCardType(List<PaymentCardType> paymentCardType) {
        this.paymentCardType = paymentCardType;
    }

}
