package com._3line.gravity.core.thirdparty.model;

import com._3line.gravity.core.entity.AbstractEntity;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ThirdPartySystem extends AbstractEntity {

    @Column(unique = true)
    private String clientName ;

    @Column(unique = true)
    private String appId ;

    private byte [] appKey ;

    private boolean enabled = true;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public byte[] getAppKey() {
        return appKey;
    }

    public void setAppKey(byte[] appKey) {
        this.appKey = appKey;
    }

    public SecretKey getKey() throws Exception {
        SecretKey originalKey = new SecretKeySpec(this.appKey, 0, this.appKey.length, "AES");
        return  originalKey ;
    }
}
