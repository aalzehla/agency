package com._3line.gravity.api.bills.dto;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class PayBills {

    @Min(value = 0, message = "Amount must be greater than 0")
    private String amount;
    private String requestRef;
    private String paymentCode;
    private String customerId;
    private String signature;
    private String transactionRef;
    private String responseCode;
    private String pin;
    private String customerName ;
    private String phoneNumber ;
    private String email ;
    private String agentName ;

}
