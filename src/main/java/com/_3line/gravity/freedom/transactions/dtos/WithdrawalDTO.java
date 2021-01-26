package com._3line.gravity.freedom.transactions.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class WithdrawalDTO {

    private Date tranDate;
    private String transactionRef;
    private String cardNumber;
    private Double amount;
    private String stan;
    private String sourceBank;
    private String sourceAcctName;
    private String paidWith;

}
