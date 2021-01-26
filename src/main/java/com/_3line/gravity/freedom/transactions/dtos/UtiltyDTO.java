package com._3line.gravity.freedom.transactions.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class UtiltyDTO {

    private Double amount;
    private Date tranDate;
    private String transactionRef;
    private String accountName;
    private String cardNumber;
    private String paymentPlan;
    private String electricityToken;
    private String paidWith;

}
