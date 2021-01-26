/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.gravitymobile.response;

import com._3line.gravity.core.models.Response;

/**
 *
 * @author OlalekanW
 */
public class AgentSetupResponse extends Response {

    private String agentId, clientId, secretKey, latitude, longitude, name, belongsto, companyName, deviceSetupStatus, pin, balnce, geofenceradius, accountNo, structureid, address;

    /**
     * @return the agentId
     */
    public String getAgentId() {
        return agentId;
    }

    /**
     * @param agentId the agentId to set
     */
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the secretKey
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * @param secretKey the secretKey to set
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the deviceSetupStatus
     */
    public String getDeviceSetupStatus() {
        return deviceSetupStatus;
    }

    /**
     * @param deviceSetupStatus the deviceSetupStatus to set
     */
    public void setDeviceSetupStatus(String deviceSetupStatus) {
        this.deviceSetupStatus = deviceSetupStatus;
    }

    /**
     * @return the pin
     */
    public String getPin() {
        return pin;
    }

    /**
     * @param pin the pin to set
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * @return the balnce
     */
    public String getBalnce() {
        return balnce;
    }

    /**
     * @param balnce the balnce to set
     */
    public void setBalnce(String balnce) {
        this.balnce = balnce;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the belongsto
     */
    public String getBelongsto() {
        return belongsto;
    }

    /**
     * @param belongsto the belongsto to set
     */
    public void setBelongsto(String belongsto) {
        this.belongsto = belongsto;
    }

    public String getGeofenceradius() {
        return geofenceradius;
    }

    public void setGeofenceradius(String geofenceradius) {
        this.geofenceradius = geofenceradius;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getStructureid() {
        return structureid;
    }

    public void setStructureid(String structureid) {
        this.structureid = structureid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
