package com._3line.gravity.freedom.transactions.dtos;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class TransactionHistoryRespDTO {
    private Long tranId;
    private Date tranDate;
    private String maskedPan;
    private String media;
    private String description;
    private DepositDTO depositDTO;
    private UtiltyDTO utilityDTO;
    private WithdrawalDTO withdrawalDTO;
    private short status;
    private String statusdescription;
    private Double amount;
    private String transactionType;
    private String billerType;
    private BigInteger walletid;
    private String transactionTypeDescription;
    private long innitiatorId;
    private String webpayTranReference;
    private String stan;
    private String terminalId;
    private String merchantLoc;
    private String beneficiary;
    private String hashedpan;
    private BigInteger productid;
    private Double balancebefore;
    private long approval;
    private String latitude;
    private String longitude;
    private String bankFrom ;
    private String bankTo ;
    private String accountNumber ;
    private String accountNumberTo;
    private String agentName ;
    private String customerName;
    private String fee;
    private String depositorName;
    private String depositorEmail;
    private String depositorPhone ;
    private String itexTranId ;
    private String statcode ;


}
