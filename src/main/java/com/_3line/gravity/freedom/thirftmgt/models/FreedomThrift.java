package com._3line.gravity.freedom.thirftmgt.models;


import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.agents.models.Agents;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;


@Data
@ToString
@Entity
public class FreedomThrift extends AbstractEntity {

    @Column(unique = true)
    private String cardNumber ;

    private String customerName ;

    private String customerEmail;

    @Column(unique = true)
    private String customerPhone ;

    private String customerAddress;

    private String customerDOB;
    private String idNumber;
    private String userId;

    @Lob
    private String customerPic ;

    @Column(unique = true)
    private String walletNumber ;

    private Double savingAmount ;

    @Enumerated(EnumType.STRING)
    private SavingCycle savingCycle ;

    @Enumerated(EnumType.STRING)
    private ThriftStatus status = ThriftStatus.IDLE;

    private Date lastContributionDate;

    private Date registrationDate ;

    private Date lastLiquidationDate ;

    private Date nextliquidationDate ;

    @Column(unique = true)
    private String cycleUID;

    @JsonIgnore
    @ManyToOne
    private Agents agent ;


}
