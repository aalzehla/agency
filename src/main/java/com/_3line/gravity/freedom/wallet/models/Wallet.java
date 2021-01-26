package com._3line.gravity.freedom.wallet.models;

import com._3line.gravity.core.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;

/**
 * @author FortunatusE
 * @date 4/24/2019
 */

@Entity
@Data
public class Wallet extends AbstractEntity {

    private String operatorId;
    private double balance;
}
