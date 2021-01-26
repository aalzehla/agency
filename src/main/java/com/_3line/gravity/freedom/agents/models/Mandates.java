package com._3line.gravity.freedom.agents.models;


import com._3line.gravity.core.entity.AbstractEntity;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

@ToString
@Entity
public class Mandates extends AbstractEntity {

    @OneToOne
    Agents applicationUsers ;

    @Lob
    String signature;
    @Lob
    String idcard;
    @Lob
    String picture;
    @Lob
    String utilityBill;


    public Agents getApplicationUsers() {
        return applicationUsers;
    }

    public void setApplicationUsers(Agents applicationUsers) {
        this.applicationUsers = applicationUsers;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUtilityBill() {
        return utilityBill;
    }

    public void setUtilityBill(String utilityBill) {
        this.utilityBill = utilityBill;
    }
}
