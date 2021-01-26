package com._3line.gravity.freedom.dispute.models;

import com._3line.gravity.core.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Entity
public class Dispute extends AbstractEntity {

    private String agentName ;

    private Long tranId ;

    private String type ;

    private String comment ;

    private String action ;

    private String amount ;

    private String walletNumber ;

    private String raisedBy ;

    private String approvedBy ;

    private String loggedBy ;

//    @Enumerated(EnumType.STRING)
//    private DisputeType disputeType ;

}
