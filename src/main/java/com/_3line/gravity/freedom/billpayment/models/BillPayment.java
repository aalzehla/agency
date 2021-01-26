package com._3line.gravity.freedom.billpayment.models;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.billpayment.dtos.CustomerDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Entity
public class BillPayment extends AbstractEntity {

    private String agentName;
    private String amount;
    private String requestRef;
    private String paymentCode;
    private String biller ;
    private String customerId;
    private String signature;
    private String transactionId;
    private String tranRespPayload;
    private String transactionRef;
    private String responseCode;
    private String responseDescription;
    private String pin;
    private String customerPhone ;
    private String customerName ;
    private String customerEmail;
    @Enumerated(EnumType.STRING)
    private Recharge recharge = Recharge.NO ;
    @Enumerated(EnumType.STRING)
    private Status status  =  Status.PENDING ;


    public enum Status {
        PENDING , DONE , FAILED
    }

    public enum Recharge {
        YES , NO
    }

    private String billType;


}
