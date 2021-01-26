package com._3line.gravity.freedom.wallet.dto;

import com._3line.gravity.freedom.wallet.models.Status;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@ToString
@Data
public class CreditRequestDTO {

    private Long id;
    private String agentName ;
    private String agentEmail ;
    private String walletNumber ;
    private String amount ;
    private String bank ;
    private String accountNumber ;
    private String depositorName ;
    private String approvedBy ;
    private Status status ;
    private String requestDate;
    private String remark;
    private String bankReference;
    private Date approvalDate;
}
