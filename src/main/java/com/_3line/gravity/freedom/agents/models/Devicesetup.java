/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.agents.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author NiyiO
 */
@Entity
@Table(name = "devicesetup")
public class Devicesetup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "agentId")
    private long agentId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "agentParentId")
    private long agentParentId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hierarchy")
    private int hierarchy;
    @Size(max = 256)
    @Column(name = "clientid")
    private String clientid;
    @Size(max = 256)
    @Column(name = "secretkey")
    private String secretkey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "geofenceoption")
    private boolean geofenceoption;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "geofenceradius")
    private Double geofenceradius;
    @Basic(optional = false)
    @NotNull
    @Column(name = "agenttype")
    private boolean agenttype;
    @Basic(optional = false)
    @NotNull
    @Column(name = "transactFromSelfWallet")
    private boolean transactFromSelfWallet;
    @Size(max = 30)
    @Column(name = "latitude")
    private String latitude;
    @Size(max = 30)
    @Column(name = "longitude")
    private String longitude;
    @Size(max = 500)
    @Column(name = "agentLocation")
    private String agentLocation;
    @Size(max = 500)
    @Column(name = "geofencedLocation")
    private String geofencedLocation;

    private Date datecreated = new Date();

    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @Size(max = 50)
    @Column(name = "deviceid")
    private String deviceid;

    private String appVersion = "0";

    @Size(max = 256)
    @Column(name = "agentGroupId")
    private String agentGroupId;

    public Devicesetup() {
    }

    public Devicesetup(Long id) {
        this.id = id;
    }

    public Devicesetup(Long id, long agentId, long agentParentId, int hierarchy, boolean geofenceoption, boolean agenttype, boolean transactFromSelfWallet, Date datecreated, int status, String appVersion) {
        this.id = id;
        this.agentId = agentId;
        this.agentParentId = agentParentId;
        this.hierarchy = hierarchy;
        this.geofenceoption = geofenceoption;
        this.agenttype = agenttype;
        this.transactFromSelfWallet = transactFromSelfWallet;
        this.datecreated = datecreated;
        this.status = status;
        this.appVersion = appVersion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAgentId() {
        return agentId;
    }

    public void setAgentId(long agentId) {
        this.agentId = agentId;
    }

    public long getAgentParentId() {
        return agentParentId;
    }

    public void setAgentParentId(long agentParentId) {
        this.agentParentId = agentParentId;
    }

    public int getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(int hierarchy) {
        this.hierarchy = hierarchy;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public boolean getGeofenceoption() {
        return geofenceoption;
    }

    public void setGeofenceoption(boolean geofenceoption) {
        this.geofenceoption = geofenceoption;
    }

    public Double getGeofenceradius() {
        return geofenceradius;
    }

    public void setGeofenceradius(Double geofenceradius) {
        this.geofenceradius = geofenceradius;
    }

    public boolean getAgenttype() {
        return agenttype;
    }

    public void setAgenttype(boolean agenttype) {
        this.agenttype = agenttype;
    }

    public boolean getTransactFromSelfWallet() {
        return transactFromSelfWallet;
    }

    public void setTransactFromSelfWallet(boolean transactFromSelfWallet) {
        this.transactFromSelfWallet = transactFromSelfWallet;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAgentLocation() {
        return agentLocation;
    }

    public void setAgentLocation(String agentLocation) {
        this.agentLocation = agentLocation;
    }

    public String getGeofencedLocation() {
        return geofencedLocation;
    }

    public void setGeofencedLocation(String geofencedLocation) {
        this.geofencedLocation = geofencedLocation;
    }

    public Date getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Date datecreated) {
        this.datecreated = datecreated;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAgentGroupId() {
        return agentGroupId;
    }

    public void setAgentGroupId(String agentGroupId) {
        this.agentGroupId = agentGroupId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Devicesetup)) {
            return false;
        }
        Devicesetup other = (Devicesetup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com._3line.app.agencybanking.models.Devicesetup[ id=" + id + " ]";
    }

}
