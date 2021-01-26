package com._3line.gravity.freedom.transactions.dtos;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class DepositDTO {

    private Double amount;
    private Date tranDate;
    private String agentCommission;
    private Double freedomCharge;
    private String transactionRef;
    private String recipientAccount;
    private String recipientBank;
    private String recipientName;

}
