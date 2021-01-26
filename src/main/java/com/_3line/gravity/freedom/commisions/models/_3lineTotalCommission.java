package com._3line.gravity.freedom.commisions.models;


import com._3line.gravity.core.entity.AbstractEntity;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by FortunatusE on 8/9/2018.
 */

@Entity
public class _3lineTotalCommission extends AbstractEntity {

    private BigDecimal totalAmount;
    private Date tranDate;
    private boolean paid;


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getTranDate() {
        return tranDate;
    }

    public void setTranDate(Date tranDate) {
        this.tranDate = tranDate;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

}
