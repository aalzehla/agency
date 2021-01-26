package com._3line.gravity.freedom.agents.models;

import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@ToString
@Entity
public class Agents implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;


    private Boolean isFirstTime = true;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "username")
    private String username;

    @Size(max = 50)
    @Column(name = "email")
    private String email;

    @Size(max = 165)
    @Column(name = "password")
    private String password;

    @Column(name = "BelongsTo")
    private long belongsTo;

    @Basic(optional = false)
    @NotNull
    @Column(name = "activated")
    private int activated;

    @Column(name = "createdby")
    private BigInteger createdby;

    @Column(name = "lastloggedin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastloggedin;

    @Basic(optional = false)
    @Column(name = "datecreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreated;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "firstName")
    private String firstName;

    @Size(max = 50)
    @Column(name = "middleName")
    private String middleName;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "lastName")
    private String lastName;

    @Size(max = 30)
    @Column(name = "City")
    private String city;

    @Size(max = 50)
    @Column(name = "lga")
    private String lga;

    @Size(max = 50)
    @Column(name = "state")
    private String state;

    @Size(max = 30)
    @Column(name = "country")
    private String country;

    @Size(max = 200)
    @Column(name = "address")
    private String address;

    @Basic(optional = false)
    @Size(min = 1, max = 15)
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "lastPasswordChangeDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordChangeDate;

    @Basic(optional = false)
    @Column(name = "unsuccessfulLoginCount")
    private int unsuccessfulLoginCount;

    @Column(name = "structureid")
    private long structureid;

    @Size(min = 1, max = 15)
    @Column(name = "accountNo")
    private String accountNo;

    @Size(max = 20)
    @Column(name = "agentCompanyId")
    private String agentCompanyId;

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Size(max = 255)
    @Column(name = "gender")
    private String gender;

    @Size(max = 255)
    @Column(name = "highestEducationalLevel")
    private String highestEducationalLevel;

    @Size(max = 255)
    @Column(name = "identificationType")
    private String identificationType;

    @Column(name = "presentAddressDateOfEntry")
    @Temporal(TemporalType.DATE)
    private Date presentAddressDateOfEntry;

    @Size(max = 15)
    @Column(name = "secondPhoneNumber")
    private String secondPhoneNumber;

    @Column(name = "bankCode")
    private String bankCode ;

    private String bvn;

    private String idNumber ;

    @Column(unique = true)
    private String terminalId;

    private String walletNumber ;

    private Long parentAgentId ;

    private Long subParentAgentId;

    private String comAccountNo;

    private String comAccountBank ;

    private String buisnessLocation;

    @Lob
    private String businessName;

    private String agentType ;

    private String wards;

    private String incomeWalletNumber;

    private String userPin;

    private String posTerminalTranFee = ".75";

    private String commissionFeePercentage = ".75";

    private String agentId;

    private String agentCode;

    private String sanefAgentCode;

    private String detachStatus;


    public Long getSubParentAgentId() {
        return subParentAgentId;
    }

    public void setSubParentAgentId(Long subParentAgentId) {
        this.subParentAgentId = subParentAgentId;
    }

    public String getDetachStatus() {
        return detachStatus;
    }

    public void setDetachStatus(String detachStatus) {
        this.detachStatus = detachStatus;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getPosTerminalTranFee() {
        return posTerminalTranFee;
    }

    public void setPosTerminalTranFee(String posTerminalTranFee) {
        this.posTerminalTranFee = posTerminalTranFee;
    }

    public String getBuisnessLocation() {
        return buisnessLocation;
    }

    public void setBuisnessLocation(String buisnessLocation) {
        this.buisnessLocation = buisnessLocation;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getWards() {
        return wards;
    }

    public void setWards(String wards) {
        this.wards = wards;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @PrePersist
    protected void onCreate() {
        this.setDatecreated(new Timestamp(new Date().getTime()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(long belongsTo) {
        this.belongsTo = belongsTo;
    }

    public int getActivated() {
        return activated;
    }

    public void setActivated(int activated) {
        this.activated = activated;
    }

    public BigInteger getCreatedby() {
        return createdby;
    }

    public void setCreatedby(BigInteger createdby) {
        this.createdby = createdby;
    }

    public Date getLastloggedin() {
        return lastloggedin;
    }

    public void setLastloggedin(Date lastloggedin) {
        this.lastloggedin = lastloggedin;
    }

    public Date getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Date datecreated) {
        this.datecreated = datecreated;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getLastPasswordChangeDate() {
        return lastPasswordChangeDate;
    }

    public void setLastPasswordChangeDate(Date lastPasswordChangeDate) {
        this.lastPasswordChangeDate = lastPasswordChangeDate;
    }

    public int getUnsuccessfulLoginCount() {
        return unsuccessfulLoginCount;
    }

    public void setUnsuccessfulLoginCount(int unsuccessfulLoginCount) {
        this.unsuccessfulLoginCount = unsuccessfulLoginCount;
    }

    public long getStructureid() {
        return structureid;
    }

    public void setStructureid(long structureid) {
        this.structureid = structureid;
    }

    public String getAgentCompanyId() {
        return agentCompanyId;
    }

    public void setAgentCompanyId(String agentCompanyId) {
        this.agentCompanyId = agentCompanyId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHighestEducationalLevel() {
        return highestEducationalLevel;
    }

    public void setHighestEducationalLevel(String highestEducationalLevel) {
        this.highestEducationalLevel = highestEducationalLevel;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public Date getPresentAddressDateOfEntry() {
        return presentAddressDateOfEntry;
    }

    public void setPresentAddressDateOfEntry(Date presentAddressDateOfEntry) {
        this.presentAddressDateOfEntry = presentAddressDateOfEntry;
    }

    public String getSecondPhoneNumber() {
        return secondPhoneNumber;
    }

    public void setSecondPhoneNumber(String secondPhoneNumber) {
        this.secondPhoneNumber = secondPhoneNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getWalletNumber() {
        return walletNumber;
    }

    public void setWalletNumber(String walletNumber) {
        this.walletNumber = walletNumber;
    }

    public String getIncomeWalletNumber() {
        return incomeWalletNumber;
    }

    public void setIncomeWalletNumber(String incomeWalletNumber) {
        this.incomeWalletNumber = incomeWalletNumber;
    }

    public Long getParentAgentId() {
        return parentAgentId;
    }

    public void setParentAgentId(Long parentAgentId) {
        this.parentAgentId = parentAgentId;
    }

    public String getUserPin() {
        return userPin;
    }

    public void setUserPin(String userPin) {
        this.userPin = userPin;
    }

    public String getComAccountNo() {
        return comAccountNo;
    }

    public void setComAccountNo(String comAccountNo) {
        this.comAccountNo = comAccountNo;
    }

    public String getComAccountBank() {
        return comAccountBank;
    }

    public void setComAccountBank(String comAccountBank) {
        this.comAccountBank = comAccountBank;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(Boolean firstTime) {
        isFirstTime = firstTime;
    }

    public String getSanefAgentCode() {
        return sanefAgentCode;
    }

    public void setSanefAgentCode(String sanefAgentCode) {
        this.sanefAgentCode = sanefAgentCode;
    }


    public String getCommissionFeePercentage() {
        return commissionFeePercentage;
    }

    public void setCommissionFeePercentage(String commissionFeePercentage) {
        this.commissionFeePercentage = commissionFeePercentage;
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
        if (!(object instanceof Agents)) {
            return false;
        }
        Agents other = (Agents) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }


}
