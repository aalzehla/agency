package com._3line.gravity.freedom.thirftmgt.models;


import com._3line.gravity.core.entity.AbstractEntity;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class LiquidationHistory extends AbstractEntity {


    String refNum ;

    @ManyToOne
    FreedomThrift thrift ;

    String amountPayed ;

    String commissionSplit ;

    String totalBalanceBefore ;

    Date  perFormedOn ;


    @Lob
    String cmsResponse ;


    Boolean wasSuccessful = false ;

    public Boolean getWasSuccessful() {
        return wasSuccessful;
    }

    public void setWasSuccessful(Boolean wasSuccessful) {
        this.wasSuccessful = wasSuccessful;
    }

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    public FreedomThrift getThrift() {
        return thrift;
    }

    public void setThrift(FreedomThrift thrift) {
        this.thrift = thrift;
    }

    public String getAmountPayed() {
        return amountPayed;
    }

    public void setAmountPayed(String amountPayed) {
        this.amountPayed = amountPayed;
    }

    public String getCommissionSplit() {
        return commissionSplit;
    }

    public void setCommissionSplit(String commissionSplit) {
        this.commissionSplit = commissionSplit;
    }

    public String getTotalBalanceBefore() {
        return totalBalanceBefore;
    }

    public void setTotalBalanceBefore(String totalBalanceBefore) {
        this.totalBalanceBefore = totalBalanceBefore;
    }

    public Date getPerFormedOn() {
        return perFormedOn;
    }

    public void setPerFormedOn(Date perFormedOn) {
        this.perFormedOn = perFormedOn;
    }

    public String getCmsResponse() {
        return cmsResponse;
    }

    public void setCmsResponse(String cmsResponse) {
        this.cmsResponse = cmsResponse;
    }


    @Override
    public String toString() {
        return "LiquidationHistory{" +
                "refNum='" + refNum + '\'' +
                ", amountPayed='" + amountPayed + '\'' +
                ", commissionSplit='" + commissionSplit + '\'' +
                ", totalBalanceBefore='" + totalBalanceBefore + '\'' +
                ", perFormedOn=" + perFormedOn +
                ", cmsResponse='" + cmsResponse + '\'' +
                ", wasSuccessful=" + wasSuccessful +
                '}';
    }
}
