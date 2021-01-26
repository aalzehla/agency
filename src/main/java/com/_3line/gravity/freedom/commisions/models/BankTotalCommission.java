package com._3line.gravity.freedom.commisions.models;


import com._3line.gravity.core.entity.AbstractEntity;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by FortunatusE on 8/9/2018.
 */

@Entity
public class BankTotalCommission extends AbstractEntity {

    private String bankCode;
    private String bankName;
    private BigDecimal totalAmount;
    private Date tranDate;
    private boolean paid;


    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BankTotalCommission that = (BankTotalCommission) o;

        return bankCode.equals(that.bankCode);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + bankCode.hashCode();
        return result;
    }
}
