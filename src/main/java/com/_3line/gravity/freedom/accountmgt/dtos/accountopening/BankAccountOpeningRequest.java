package com._3line.gravity.freedom.accountmgt.dtos.accountopening;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author JoyU
 * @date 11/14/2018
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BankAccountOpeningRequest {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("CreatedOn")
    private String createdOn;
    @JsonProperty("AppId")
    private String appId;
    @JsonProperty("AppKey")
    private String appKey;
    @JsonProperty("ReferenceNo")
    private String referenceNo;
    @JsonProperty("BVNNumber")
    private String bvnNumber;
    @JsonProperty("PreferredBranch")
    private String preferredBranch;
    @NotEmpty(message = "First name is required")
    @JsonProperty("FirstName")
    private String firstName;
    @JsonProperty("MiddleName")
    private String middleName;
    @NotEmpty(message = "Last name is required")
    @JsonProperty("LastName")
    private String lastName;
    @NotEmpty(message = "Marital status is required")
    @JsonProperty("MaritalStatus")
    private String maritalStatus;
    @NotEmpty(message = "Date of birth is required")
    @JsonProperty("DateOfBirth")
    private String dateOfBirth;
    @JsonProperty("DateOfBirth2")
    private String dateOfBirth2;
    @NotEmpty(message = "Email address is required")
    @JsonProperty("Email")
    private String email;
    @NotEmpty(message = "Gender is required")
    @JsonProperty("Gender")
    private String gender;
    @NotEmpty(message = "Phone number is required")
    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("PhoneNumber")
    private List<String> phoneNumber;

    @NotEmpty(message = "Mother's maiden name is required")
    @JsonProperty("MotherMaidenName")
    private String motherMaidenName;
    @JsonProperty("NextOfKin")
    private String nextOfKin;
    @NotEmpty(message = "Nationality is required")
    @JsonProperty("Nationality")
    private String nationality;
    @NotEmpty(message = "Residential address is required")
    @JsonProperty("ResidentialAddress")
    private String residentialAddress;
    @NotEmpty(message = "State of origin is required")
    @JsonProperty("StateOfOrigin")
    private String stateOfOrigin;
    @NotEmpty(message = "Photo is required")
    @JsonProperty("Photo")
    private String photo;
    @NotEmpty(message = "Signature is required")
    @JsonProperty("Signature")
    private String signature;
    @NotEmpty(message = "State of residence is required")
    @JsonProperty("StateOfResidence")
    private String stateOfResidence;
    @JsonProperty("LevelOfAccount")
    private String levelOfAccount;
    @JsonProperty("LgaOfOrigin")
    private String lgaOfOrigin;
    @JsonProperty("LgaOfResidence")
    private String lgaOfResidence;
    @JsonProperty("Resident")
    private String resident;
    @JsonProperty("CountryCode")
    private String countryCode;
    @JsonProperty("BirthCert")
    private String birthCert;
    @JsonProperty("ParentPhoto")
    private String parentPhoto;
    @JsonProperty("How_You_Heard")
    private String howYouHeard;
    @JsonProperty("Complete")
    private String complete;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("TrusteeTitle")
    private String trusteeTitle;
    @JsonProperty("TrusteeCountry")
    private String trusteeCountry;
    @JsonProperty("TrusteeCity")
    private String trusteeCity;
    @JsonProperty("TrusteeMaritalStatus")
    private String trusteeMaritalStatus;
    @JsonProperty("TrusteeEmail")
    private String trusteeEmail;
    @JsonProperty("TrusteeAddr1")
    private String trusteeAddr1;
    @JsonProperty("TrusteeAddr2")
    private String trusteeAddr2;
    @JsonProperty("TrusteeCntry")
    private String trusteeCntry;
    @JsonProperty("TrusteeDob")
    private String trusteeDob;
    @JsonProperty("TrusteeFname")
    private String trusteeFname;
    @JsonProperty("TrusteeLname")
    private String trusteeLname;
    @JsonProperty("TrusteeMname")
    private String trusteeMname;
    @JsonProperty("TrusteePhoneNo")
    private String trusteePhoneNo;
    @JsonProperty("TrusteeState")
    private String trusteeState;
    @JsonProperty("City")
    private String city;
    @JsonProperty("State")
    private String state;
    @JsonProperty("CifId")
    private String cifId;
    @JsonProperty("SchemeCode")
    private String schemeCode;
    @NotEmpty(message = "Account type is required")
    @JsonProperty("AccountType")
    private String accountType;
    @JsonProperty("Channel_Id")
    private String channelId;

    @JsonProperty("AccountOfficerCode")
    private String accountOfficerCode;
    @JsonProperty("BrokerCode")
    private String brokerCode;
    @JsonProperty("Occupation")
    private String occupation;

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
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

    public String getCifId() {
        return cifId;
    }

    public void setCifId(String cifId) {
        this.cifId = cifId;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(List<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
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


    @Override
    public String toString() {
        return "BankAccountOpeningRequest{" +
                "id='" + id + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", appId='" + appId + '\'' +
                ", appKey='" + appKey + '\'' +
                ", referenceNo='" + referenceNo + '\'' +
                ", bvnNumber='" + bvnNumber + '\'' +
                ", preferredBranch='" + preferredBranch + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", dateOfBirth2='" + dateOfBirth2 + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", mobile='" + mobile + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", motherMaidenName='" + motherMaidenName + '\'' +
                ", nextOfKin='" + nextOfKin + '\'' +
                ", nationality='" + nationality + '\'' +
                ", residentialAddress='" + residentialAddress + '\'' +
                ", stateOfOrigin='" + stateOfOrigin + '\'' +
                ", photo='" + photo + '\'' +
                ", signature='" + signature + '\'' +
                ", stateOfResidence='" + stateOfResidence + '\'' +
                ", levelOfAccount='" + levelOfAccount + '\'' +
                ", lgaOfOrigin='" + lgaOfOrigin + '\'' +
                ", lgaOfResidence='" + lgaOfResidence + '\'' +
                ", resident='" + resident + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", birthCert='" + birthCert + '\'' +
                ", parentPhoto='" + parentPhoto + '\'' +
                ", howYouHeard='" + howYouHeard + '\'' +
                ", complete='" + complete + '\'' +
                ", title='" + title + '\'' +
                ", trusteeTitle='" + trusteeTitle + '\'' +
                ", trusteeCountry='" + trusteeCountry + '\'' +
                ", trusteeCity='" + trusteeCity + '\'' +
                ", trusteeMaritalStatus='" + trusteeMaritalStatus + '\'' +
                ", trusteeEmail='" + trusteeEmail + '\'' +
                ", trusteeAddr1='" + trusteeAddr1 + '\'' +
                ", trusteeAddr2='" + trusteeAddr2 + '\'' +
                ", trusteeCntry='" + trusteeCntry + '\'' +
                ", trusteeDob='" + trusteeDob + '\'' +
                ", trusteeFname='" + trusteeFname + '\'' +
                ", trusteeLname='" + trusteeLname + '\'' +
                ", trusteeMname='" + trusteeMname + '\'' +
                ", trusteePhoneNo='" + trusteePhoneNo + '\'' +
                ", trusteeState='" + trusteeState + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", cifId='" + cifId + '\'' +
                ", schemeCode='" + schemeCode + '\'' +
                ", accountType='" + accountType + '\'' +
                ", channelId='" + channelId + '\'' +
                ", accountOfficerCode='" + accountOfficerCode + '\'' +
                ", brokerCode='" + brokerCode + '\'' +
                '}';
    }
}
