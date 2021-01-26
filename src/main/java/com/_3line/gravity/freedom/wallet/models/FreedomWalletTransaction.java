package com._3line.gravity.freedom.wallet.models;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.utility.Utility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@Entity
public class FreedomWalletTransaction extends AbstractEntity {

    @JsonIgnore
    @ManyToOne
    private FreedomWallet wallet;

    @Enumerated(EnumType.STRING)
    private TranType tranType;

    private Double amount ;

    private String channel ;

    private String remark ;

    private String tranID ;


    private Double balanceBefore ;

    private Double balanceAfter ;

    private Double balanceBefore_reconcilled ;

    private Double balanceAfter_reconcilled ;

    private String walletNumber ;

    private boolean reversed = false ;

    private Long reversalTranId ;

    private Date  tranDate = new Date();
}
