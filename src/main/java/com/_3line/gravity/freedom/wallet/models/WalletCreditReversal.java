package com._3line.gravity.freedom.wallet.models;

import com._3line.gravity.core.entity.AbstractEntity;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;

/**
 * @author FortunatusE
 * @date 4/24/2019
 */

@Entity
@Data
@Where(clause = "del_flag='N'")
public class WalletCreditReversal extends AbstractEntity {

    String ptspId ;
    String walletNumber ;
    String channel ;
    Double amount;
    String remark ;
    String status ;
}
