package com._3line.gravity.freedom.itexintegration.model;

import lombok.Data;

@Data
public class PtspDto {

    private Long id;
    private String delFlag;
    private String terminalId;
    private Double amount;
    private String transactionTime;
    private String stan;
    private String rrn;
    private String cardNumber;
    private String productId;
    private String statusCode;
    private String pan;
    private String reversal;
    private String bank;
    private String transactionType;
    private String ptsp ;
    private String verifiedBy ;
    private String uploadedBy ;
    private Boolean isVerified ;
    private String processor_status;

}
