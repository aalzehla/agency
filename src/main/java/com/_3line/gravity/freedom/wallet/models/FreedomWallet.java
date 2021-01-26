package com._3line.gravity.freedom.wallet.models;

import com._3line.gravity.core.entity.AbstractEntity;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


@ToString
@Entity
public class FreedomWallet {


    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    Long id;

    @Column(unique = true)
    String walletNumber ;


    protected String delFlag = "N";

    protected Date deletedOn;

    protected final Date createdOn = new Date();


    Double availableBalance = 0.0 ;

    Double ledgerBalance = 0.0  ;

    Date lastTranDate ;


    String purpose ;

    String isGeneralLedger = "NO";


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(Date deletedOn) {
        this.deletedOn = deletedOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public String getIsGeneralLedger() {
        return isGeneralLedger;
    }

    public void setIsGeneralLedger(String isGeneralLedger) {
        this.isGeneralLedger = isGeneralLedger;
    }

    public String getWalletNumber() {
        return walletNumber;
    }

    public void setWalletNumber(String walletNumber) {
        this.walletNumber = walletNumber;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public Double getLedgerBalance() {
        return ledgerBalance;
    }

    public void setLedgerBalance(Double ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }

    public Date getLastTranDate() {
        return lastTranDate;
    }

    public void setLastTranDate(Date lastTranDate) {
        this.lastTranDate = lastTranDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FreedomWallet wallet = (FreedomWallet) o;
        return Objects.equals(id, wallet.id) &&
                Objects.equals(walletNumber, wallet.walletNumber);
    }

    @Override
    public int hashCode() {
        return Math.toIntExact(id);
    }
}
