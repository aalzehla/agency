package com._3line.gravity.freedom.billpayment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ValidateCustomerRequest {

//    @JsonProperty("Amount")
    private String  amount ;
//    @JsonProperty("PaymentCode")
    private String paymentCode;
//    @JsonProperty("CustomerId")
    private String customerId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
