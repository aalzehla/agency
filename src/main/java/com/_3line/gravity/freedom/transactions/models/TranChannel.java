package com._3line.gravity.freedom.transactions.models;

import com._3line.gravity.core.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author NiyiO
 */
@Entity
@Table(name = "tran_channels")
@Data
public class TranChannel extends AbstractEntity {

    private String channelName;
    private String posTerminalPercentageFee;
    private Double minimumPosTerminalFee;
    private Double maxPosTerminalFee;
    private String acquirerPercentageFee;
    private Double minimumAcquirerFee;
    private Double maxAcquirerFee;
    private Double maxPerMiscAcquirerFee;

}
