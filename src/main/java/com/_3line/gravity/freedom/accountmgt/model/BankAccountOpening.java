package com._3line.gravity.freedom.accountmgt.model;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.agents.models.Agents;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.Arrays;
import java.util.List;

@ToString
@Entity
public class BankAccountOpening extends AbstractEntity {


    @ManyToOne
    private Agents agent ;
    private String referenceNo;
    private String bvnNumber;
    private String preferredBranch;
    private String firstName;
    private String middleName;
    private String lastName;
    private String maritalStatus;
    private String dateOfBirth;
    private String dateOfBirth2;
    private String email;
    private String gender;
    private String mobile;
    private String motherMaidenName;
    private String nextOfKin;
    private String nationality;
    private String residentialAddress;
    private String stateOfOrigin;
    @Lob
    private String photo;
    @Lob
    private String signature;
    private String stateOfResidence;
    private String levelOfAccount;
    private String lgaOfOrigin;

    private String lgaOfResidence;
    private String resident;
    private String countryCode;
    private String birthCert;
    private String parentPhoto;
    private String howYouHeard;
    private String complete;
    private String title;
    private String trusteeTitle;
    private String trusteeCountry;
    private String trusteeCity;
    private String trusteeMaritalStatus;
    private String trusteeEmail;
    private String trusteeAddr1;
    private String trusteeAddr2;
    private String trusteeCntry;
    private String trusteeDob;
    private String trusteeFname;
    private String trusteeLname;
    private String trusteeMname;
    private String trusteePhoneNo;
    private String trusteeState;
    private String city;
    private String state;
    private String schemeCode;
    private String accountType;
    private String channelId;
    private String accountNumber;
    private String cifId;
    private String responseCode;
    private String errorMessage;
    private String responseMessage;
    private String remarks;
    private String accountOfficerCode;
    private String brokerCode;
    private String occupation;
    private String operatorId;
    private String dsaBrokerCode;

    public String getDsaBrokerCode() {
        return dsaBrokerCode;
    }

    public void setDsaBrokerCode(String dsaBrokerCode) {
        this.dsaBrokerCode = dsaBrokerCode;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Agents getAgent() {
        return agent;
    }

    public void setAgent(Agents agent) {
        this.agent = agent;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getBvnNumber() {
        return bvnNumber;
    }

    public void setBvnNumber(String bvnNumber) {
        this.bvnNumber = bvnNumber;
    }

    public String getPreferredBranch() {
        return preferredBranch;
    }

    public void setPreferredBranch(String preferredBranch) {
        this.preferredBranch = preferredBranch;
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

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfBirth2() {
        return dateOfBirth2;
    }

    public void setDateOfBirth2(String dateOfBirth2) {
        this.dateOfBirth2 = dateOfBirth2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public String getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(String nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }


    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getStateOfResidence() {
        return stateOfResidence;
    }

    public void setStateOfResidence(String stateOfResidence) {
        this.stateOfResidence = stateOfResidence;
    }

    public String getLevelOfAccount() {
        return levelOfAccount;
    }

    public void setLevelOfAccount(String levelOfAccount) {
        this.levelOfAccount = levelOfAccount;
    }

    public String getLgaOfOrigin() {
        return lgaOfOrigin;
    }

    public void setLgaOfOrigin(String lgaOfOrigin) {
        this.lgaOfOrigin = lgaOfOrigin;
    }

    public String getLgaOfResidence() {
        return lgaOfResidence;
    }

    public void setLgaOfResidence(String lgaOfResidence) {
        this.lgaOfResidence = lgaOfResidence;
    }

    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getBirthCert() {
        return birthCert;
    }

    public void setBirthCert(String birthCert) {
        this.birthCert = birthCert;
    }

    public String getParentPhoto() {
        return parentPhoto;
    }

    public void setParentPhoto(String parentPhoto) {
        this.parentPhoto = parentPhoto;
    }

    public String getHowYouHeard() {
        return howYouHeard;
    }

    public void setHowYouHeard(String howYouHeard) {
        this.howYouHeard = howYouHeard;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrusteeTitle() {
        return trusteeTitle;
    }

    public void setTrusteeTitle(String trusteeTitle) {
        this.trusteeTitle = trusteeTitle;
    }

    public String getTrusteeCountry() {
        return trusteeCountry;
    }

    public void setTrusteeCountry(String trusteeCountry) {
        this.trusteeCountry = trusteeCountry;
    }

    public String getTrusteeCity() {
        return trusteeCity;
    }

    public void setTrusteeCity(String trusteeCity) {
        this.trusteeCity = trusteeCity;
    }

    public String getTrusteeMaritalStatus() {
        return trusteeMaritalStatus;
    }

    public void setTrusteeMaritalStatus(String trusteeMaritalStatus) {
        this.trusteeMaritalStatus = trusteeMaritalStatus;
    }

    public String getTrusteeEmail() {
        return trusteeEmail;
    }

    public void setTrusteeEmail(String trusteeEmail) {
        this.trusteeEmail = trusteeEmail;
    }

    public String getTrusteeAddr1() {
        return trusteeAddr1;
    }

    public void setTrusteeAddr1(String trusteeAddr1) {
        this.trusteeAddr1 = trusteeAddr1;
    }

    public String getTrusteeAddr2() {
        return trusteeAddr2;
    }

    public void setTrusteeAddr2(String trusteeAddr2) {
        this.trusteeAddr2 = trusteeAddr2;
    }

    public String getTrusteeCntry() {
        return trusteeCntry;
    }

    public void setTrusteeCntry(String trusteeCntry) {
        this.trusteeCntry = trusteeCntry;
    }

    public String getTrusteeDob() {
        return trusteeDob;
    }

    public void setTrusteeDob(String trusteeDob) {
        this.trusteeDob = trusteeDob;
    }

    public String getTrusteeFname() {
        return trusteeFname;
    }

    public void setTrusteeFname(String trusteeFname) {
        this.trusteeFname = trusteeFname;
    }

    public String getTrusteeLname() {
        return trusteeLname;
    }

    public void setTrusteeLname(String trusteeLname) {
        this.trusteeLname = trusteeLname;
    }

    public String getTrusteeMname() {
        return trusteeMname;
    }

    public void setTrusteeMname(String trusteeMname) {
        this.trusteeMname = trusteeMname;
    }

    public String getTrusteePhoneNo() {
        return trusteePhoneNo;
    }

    public void setTrusteePhoneNo(String trusteePhoneNo) {
        this.trusteePhoneNo = trusteePhoneNo;
    }

    public String getTrusteeState() {
        return trusteeState;
    }

    public void setTrusteeState(String trusteeState) {
        this.trusteeState = trusteeState;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCifId() {
        return cifId;
    }

    public void setCifId(String cifId) {
        this.cifId = cifId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccountOfficerCode() {
        return accountOfficerCode;
    }

    public void setAccountOfficerCode(String accountOfficerCode) {
        this.accountOfficerCode = accountOfficerCode;
    }

    public String getBrokerCode() {
        return brokerCode;
    }

    public void setBrokerCode(String brokerCode) {
        this.brokerCode = brokerCode;
    }

    @JsonIgnore
    @Override
    public List<String> getDefaultSearchFields(){
        return Arrays.asList("firstName", "lastName", "email", "mobile", "accountNumber", "accountType");
    }

}
