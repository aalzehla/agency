package com._3line.gravity.freedom.thirftmgt.models;


import com._3line.gravity.core.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class SavingHistory extends AbstractEntity {

    private Double amountSaved ;

    private Date collectionDate ;

    private String locationLongitude ;

    private String locationLatitude ;

    @ManyToOne
    private FreedomThrift thrift ;

    private String cycleUID;


    public Double getAmountSaved() {
        return amountSaved;
    }

    public void setAmountSaved(Double amountSaved) {
        this.amountSaved = amountSaved;
    }

    public Date getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(Date collectionDate) {
        this.collectionDate = collectionDate;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(String locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(String locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public FreedomThrift getThrift() {
        return thrift;
    }

    public void setThrift(FreedomThrift thrift) {
        this.thrift = thrift;
    }

    public String getCycleUID() {
        return cycleUID;
    }

    public void setCycleUID(String cycleUID) {
        this.cycleUID = cycleUID;
    }
}
