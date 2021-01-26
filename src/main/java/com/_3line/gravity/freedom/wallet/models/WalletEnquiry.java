package com._3line.gravity.freedom.wallet.models;

import com._3line.gravity.core.entity.AbstractEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;


@Entity
@Data
public class WalletEnquiry extends AbstractEntity {

    private String agentName ;

    @Column(unique = true)
    private String enquiryRequestId;
    private String transactionId;
    private String status;
    private String walletNumber;
    private String accountNumber;
    private Boolean hasTransaction;
    private Date requestDate  = new Date();
}
