package com._3line.gravity.freedom.wallet.models;

import com._3line.gravity.core.entity.AbstractEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;


@ToString
@Entity
@Data
public class CreditRequest extends AbstractEntity {


    private String agentName ;
    private String agentEmail ;
    private String walletNumber ;
    private String amount ;
    private String bank ;
    private String accountNumber ;
    private String depositorName ;
    private String approvedBy ;
    @Enumerated(EnumType.STRING)
    private Status status ;

    private String bankReference ;
    private String remark ;

    private Date requestDate  = new Date();
    private Date approvalDate;
}
