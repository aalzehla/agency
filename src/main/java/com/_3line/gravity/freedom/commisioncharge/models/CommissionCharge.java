package com._3line.gravity.freedom.commisioncharge.models;



import com._3line.gravity.core.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class CommissionCharge extends AbstractEntity {

    private Double upperBound;

    private Double lowerBound;

    private String transactionType ;

    private Double amount;

    private String institution;


}
