package com._3line.gravity.freedom.cardissuance.dto;

import lombok.Data;

import java.util.Date;


/**
 * @author FortunatusE
 * @date 12/10/2018
 */

@Data
public class CardIssuanceDTO{

    private String agentId;
    private String cardSerial;
    private String accountNumber;
    private String cardType;
    private String createdBy;
    private boolean issued;
    private boolean linked;
    private String statusMessage;
    private String statusCode;
    private String header;
    private Date dateIssued;
    private String operatorId;
}
