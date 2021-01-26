/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.agents.dtos;


import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;
import com._3line.gravity.core.verification.utility.PrettySerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.ToString;

import javax.persistence.Lob;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.IOException;

/**
 *
 * @author OlalekanW
 */
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentDto extends AbstractVerifiableDto implements PrettySerializer {

    @Size(min = 7, max = 50,message = "Username should be between 7 and 50")
    private String username;
    @Pattern(regexp = "^(?:(?=.{0,50}$)[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,}))?$",
            message = "Enter valid email eg. agent.account@email.com or agent@email.com")
    private String email;
    private String password;
    private int activated;
    private int createdby;
    private String lastloggedin;
    @Size(min = 1, max = 50,message = "First name should be between 1 and 50")
    private String firstName;
    @Size(min = 1, max = 50,message = "Last name should be between 1 and 50")
    private String lastName;
    private String middleName;
    private String addressState;
    private String addressCountry;
    @Pattern(regexp = "[0-9]{11,11}",message = "Phone number should be a valid number of length 11")
    private String phoneNumber;
    private String address;
    private String addressCity;
    private String structureid;
    private String country ;
    private int geofenceoption;
    private double geofenceradius;
    private long parentAgent;
    private long subParentAgent;
    private int transactFromSelfWallet; //( <option value="1">TRANSACTS FROM SELF'S WALLET</option> <option value="0">TRANSACTS FROM SUPERPARENT'S WALLET </option> )
//    private double agentscommissionpercent;
    private String latitude, longitude, agentLocation, geofencedLocation;
    private String AgentCompanyId;

    @Pattern(regexp = "[0-9]{10,10}",message = "Account number should be a valid number of length 10")
    private String accountNo;
    private String picture;
    private String signature;
    private String utilityBill;
    private String gender;
    private String dateOfBirth;
    private String buisnessLocation;
    @Lob
    private String businessName;
    private String buisnessLocationEntryDate;
    private String highestEducationalLevel;
    private String identificationType;
    private String secondPhoneNumber;
    private String bankCode ;

    @Pattern(regexp = "[0-9]{11,11}",message = "BVN should be valid number of length 11")
    private String bvn;
    private String idNumber ;
    private String comAccountNo;
    private String comAccountBank ;
    private String terminalId;
    private String state ;
    private String lga ;
    private String agentType ;
    private String idcard ;
    private String aggregatorName ;
    private String walletNumber ;
    private String incomeWalletNumber;
    private String posTerminalTranFee;
    private String commissionFeePercentage;
    private String agentId;
    private String datecreated;
    private String agentName;
    private String nibssAgentCode;
    private String sanefAgentCode;
    private String detachStatus;

    public String getDetachStatus() {
        return detachStatus;
    }

    public void setDetachStatus(String detachStatus) {
        this.detachStatus = detachStatus;
    }

    public String getNibssAgentCode() {
        return nibssAgentCode;
    }

    public void setNibssAgentCode(String nibssAgentCode) {
        this.nibssAgentCode = nibssAgentCode;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }


    public String getIncomeWalletNumber() {
        return incomeWalletNumber;
    }

    public void setIncomeWalletNumber(String incomeWalletNumber) {
        this.incomeWalletNumber = incomeWalletNumber;
    }
    public String getPosTerminalTranFee() {
        return posTerminalTranFee;
    }

    public void setPosTerminalTranFee(String posTerminalTranFee) {
        this.posTerminalTranFee = posTerminalTranFee;
    }

    public String getWalletNumber() {
        return walletNumber;
    }

    public void setWalletNumber(String walletNumber) {
        this.walletNumber = walletNumber;
    }

    public String getAggregatorName() {
        return aggregatorName;
    }

    public void setAggregatorName(String aggregatorName) {
        this.aggregatorName = aggregatorName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /**
     * @param belongsTo the belongsTo to set
     */

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the middleName
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @param middleName the middleName to set
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the addressState
     */
    public String getAddressState() {
        return addressState;
    }

    /**
     * @param addressState the addressState to set
     */
    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    /**
     * @return the addressCountry
     */
    public String getAddressCountry() {
        return addressCountry;
    }

    /**
     * @param addressCountry the addressCountry to set
     */
    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the addressCity
     */
    public String getAddressCity() {
        return addressCity;
    }

    /**
     * @param addressCity the addressCity to set
     */
    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the activated
     */
    public int getActivated() {
        return activated;
    }

    /**
     * @param activated the activated to set
     */
    public void setActivated(int activated) {
        this.activated = activated;
    }

    /**
     * @return the createdby
     */
    public int getCreatedby() {
        return createdby;
    }

    /**
     * @param createdby the createdby to set
     */
    public void setCreatedby(int createdby) {
        this.createdby = createdby;
    }

    /**
     * @return the lastloggedin
     */
    public String getLastloggedin() {
        return lastloggedin;
    }

    /**
     * @param lastloggedin the lastloggedin to set
     */
    public void setLastloggedin(String lastloggedin) {
        this.lastloggedin = lastloggedin;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getBuisnessLocation() {
        return buisnessLocation;
    }

    public void setBuisnessLocation(String buisnessLocation) {
        this.buisnessLocation = buisnessLocation;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUtilityBill() {
        return utilityBill;
    }

    public void setUtilityBill(String utilityBill) {
        this.utilityBill = utilityBill;
    }

    /**
     * @return the geofenceoption
     */
    public int getGeofenceoption() {
        return geofenceoption;
    }

    /**
     * @param geofenceoption the geofenceoption to set
     */
    public void setGeofenceoption(int geofenceoption) {
        this.geofenceoption = geofenceoption;
    }

    /**
     * @return the geofenceradius
     */
    public double getGeofenceradius() {
        return geofenceradius;
    }

    /**
     * @param geofenceradius the geofenceradius to set
     */
    public void setGeofenceradius(double geofenceradius) {
        this.geofenceradius = geofenceradius;
    }


    /**
     * @return the parentAgent
     */
    public long getParentAgent() {
        return parentAgent;
    }

    /**
     * @param parentAgent the parentAgent to set
     */
    public void setParentAgent(long parentAgent) {
        this.parentAgent = parentAgent;
    }

    /**
     * @return the transactFromSelfWallet
     */
    public int getTransactFromSelfWallet() {
        return transactFromSelfWallet;
    }

    /**
     * @param transactFromSelfWallet the transactFromSelfWallet to set
     */
    public void setTransactFromSelfWallet(int transactFromSelfWallet) {
        this.transactFromSelfWallet = transactFromSelfWallet;
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
     * @return the agentLocation
     */
    public String getAgentLocation() {
        return agentLocation;
    }

    /**
     * @param agentLocation the agentLocation to set
     */
    public void setAgentLocation(String agentLocation) {
        this.agentLocation = agentLocation;
    }

    /**
     * @return the geofencedLocation
     */
    public String getGeofencedLocation() {
        return geofencedLocation;
    }

    /**
     * @param geofencedLocation the geofencedLocation to set
     */
    public void setGeofencedLocation(String geofencedLocation) {
        this.geofencedLocation = geofencedLocation;
    }

    public String getStructureid() {
        return structureid;
    }

    public void setStructureid(String structureid) {
        this.structureid = structureid;
    }

    public String getAgentCompanyId() {
        return AgentCompanyId;
    }

    public void setAgentCompanyId(String AgentCompanyId) {
        this.AgentCompanyId = AgentCompanyId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getBuisnessLocationEntryDate() {
        return buisnessLocationEntryDate;
    }

    public void setBuisnessLocationEntryDate(String buisnessLocationEntryDate) {
        this.buisnessLocationEntryDate = buisnessLocationEntryDate;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getCommissionFeePercentage() {
        return commissionFeePercentage;
    }

    public void setCommissionFeePercentage(String commissionPercentage) {
        this.commissionFeePercentage = commissionPercentage;
    }

    public String getSanefAgentCode() {
        return sanefAgentCode;
    }

    public void setSanefAgentCode(String sanefAgentCode) {
        this.sanefAgentCode = sanefAgentCode;
    }

    public long getSubParentAgent() {
        return subParentAgent;
    }

    public void setSubParentAgent(long subParentAgent) {
        this.subParentAgent = subParentAgent;
    }

    @Override
    public JsonSerializer<AgentDto> getSerializer() {
        return new JsonSerializer<AgentDto>() {
            @Override
            public void serialize(AgentDto value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeStartObject();
                gen.writeStringField("First name",value.getFirstName());
                gen.writeStringField("Last name",value.getLastName());
                gen.writeStringField("User name",value.getUsername());
                gen.writeStringField("Email",value.getEmail());
                gen.writeStringField("Gender",value.getGender());
                gen.writeStringField("Phone number",value.getPhoneNumber());
                gen.writeStringField("State",value.getAddressState());
                gen.writeStringField("Terminal",value.getTerminalId());
                gen.writeStringField("Bank",value.getComAccountBank());
                gen.writeStringField("Account",value.getAccountNo());
                gen.writeStringField("PosTerminalTranFee",value.getPosTerminalTranFee());
                gen.writeStringField("CommissionFeePercentage",value.getCommissionFeePercentage());
                gen.writeStringField("ComAccountBank",value.getComAccountBank());
                gen.writeStringField("ComAccountNo",value.getComAccountNo());
                gen.writeStringField("HighestEducationalLevel",value.getHighestEducationalLevel());

                //TODO
                //TO be completed !!
                //TO be completed !!
                gen.writeEndObject();
            }
        };
    }

    @Override
    public String toString() {
        return "AgentDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", activated=" + activated +
                ", createdby=" + createdby +
                ", lastloggedin='" + lastloggedin + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", addressState='" + addressState + '\'' +
                ", addressCountry='" + addressCountry + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", addressCity='" + addressCity + '\'' +
                ", structureid='" + structureid + '\'' +
                ", country='" + country + '\'' +
                ", geofenceoption=" + geofenceoption +
                ", geofenceradius=" + geofenceradius +
                ", parentAgent=" + parentAgent +
                ", transactFromSelfWallet=" + transactFromSelfWallet +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", agentLocation='" + agentLocation + '\'' +
                ", geofencedLocation='" + geofencedLocation + '\'' +
                ", AgentCompanyId='" + AgentCompanyId + '\'' +
                ", accountNo='" + accountNo + '\'' +
                ", picture='" + picture + '\'' +
                ", signature='" + signature + '\'' +
                ", utilityBill='" + utilityBill + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", buisnessLocation='" + buisnessLocation + '\'' +
                ", businessName='" + businessName + '\'' +
                ", buisnessLocationEntryDate='" + buisnessLocationEntryDate + '\'' +
                ", highestEducationalLevel='" + highestEducationalLevel + '\'' +
                ", identificationType='" + identificationType + '\'' +
                ", secondPhoneNumber='" + secondPhoneNumber + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bvn='" + bvn + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", comAccountNo='" + comAccountNo + '\'' +
                ", comAccountBank='" + comAccountBank + '\'' +
                ", terminalId='" + terminalId + '\'' +
                ", state='" + state + '\'' +
                ", lga='" + lga + '\'' +
                ", agentType='" + agentType + '\'' +
                ", idcard='" + idcard + '\'' +
                ", aggregatorName='" + aggregatorName + '\'' +
                ", walletNumber='" + walletNumber + '\'' +
                ", incomeWalletNumber='" + incomeWalletNumber + '\'' +
                ", posTerminalTranFee='" + posTerminalTranFee + '\'' +
                ", agentId='" + agentId + '\'' +
                ", datecreated='" + datecreated + '\'' +
                ", agentName='" + agentName + '\'' +
                '}';
    }
}
