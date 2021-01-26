package com._3line.gravity.freedom.financialInstitutions.sanef.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class SanefAgentCreation {
    @JsonProperty(value = "RequestId")
    private String requestId;
    @JsonProperty(value = "AgentCode")
    private String agentCode;

    //             "AgentType": 1,SuperAgent, SubAgent
    @JsonProperty(value = "AgentType")
    private int agentType;


    @JsonProperty(value = "LastName")
    private String lastName;

    @JsonProperty(value = "FirstName")
    private String firstName;
    @JsonProperty(value = "MiddleName")
    private String middleName;
    @JsonProperty(value = "BusinessName")
    private String businessName;

    //             "Gender": 1,Male, Female
    @JsonProperty(value = "Gender")
    private int gender;

    @JsonProperty(value = "PhoneNumber1")
    private String phoneNumber1;
    @JsonProperty(value = "PhoneNumber2")
    private String phoneNumber2;
    @JsonProperty(value = "AgentAddress")
    private String agentAddress;
    @JsonProperty(value = "ClosestLandmark")
    private String closestLandmark;
    @JsonProperty(value = "EmailAddress")
    private String emailAddress;

    @JsonProperty(value = "BusinessAddress")
    private String businessAddress;
    @JsonProperty(value = "BankVerificationNumber")
    private String bankVerificationNumber;
    @JsonProperty(value = "TaxIdentificationNumber")
    private String taxIdentificationNumber;
    @JsonProperty(value = "DateOfBirth")
    private String dateOfBirth;


    //             "AgentBusiness": 0,
    @JsonProperty(value = "AgentBusiness")
    private int agentBusiness;

    @JsonProperty(value = "LocalGovernmentCode")
    private String localGovernmentCode;
    @JsonProperty(value = "Latitude")
    private Double latitude;
    @JsonProperty(value = "Longitude")
    private Double longitude;
    @JsonProperty(value = "UserName")
    private String userName;
    @JsonProperty(value = "Password")
    private String password;
    @JsonProperty(value = "TransactionPin")
    private String transactionPin;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public int getAgentType() {
        return agentType;
    }

    public void setAgentType(int agentType) {
        this.agentType = agentType;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public String getClosestLandmark() {
        return closestLandmark;
    }

    public void setClosestLandmark(String closestLandmark) {
        this.closestLandmark = closestLandmark;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBankVerificationNumber() {
        return bankVerificationNumber;
    }

    public void setBankVerificationNumber(String bankVerificationNumber) {
        this.bankVerificationNumber = bankVerificationNumber;
    }

    public String getTaxIdentificationNumber() {
        return taxIdentificationNumber;
    }

    public void setTaxIdentificationNumber(String taxIdentificationNumber) {
        this.taxIdentificationNumber = taxIdentificationNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getAgentBusiness() {
        return agentBusiness;
    }

    public void setAgentBusiness(int agentBusiness) {
        this.agentBusiness = agentBusiness;
    }

    public String getLocalGovernmentCode() {
        return localGovernmentCode;
    }

    public void setLocalGovernmentCode(String localGovernmentCode) {
        this.localGovernmentCode = localGovernmentCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTransactionPin() {
        return transactionPin;
    }

    public void setTransactionPin(String transactionPin) {
        this.transactionPin = transactionPin;
    }

    @Override
    public String toString() {
        return "SanefAgentCreation{" +
                "requestId='" + requestId + '\'' +
                ", agentCode='" + agentCode + '\'' +
                ", agentType='" + agentType + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", businessName='" + businessName + '\'' +
                ", gender='" + gender + '\'' +
                ", phoneNumber1='" + phoneNumber1 + '\'' +
                ", phoneNumber2='" + phoneNumber2 + '\'' +
                ", agentAddress='" + agentAddress + '\'' +
                ", closestLandmark='" + closestLandmark + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", businessAddress='" + businessAddress + '\'' +
                ", bankVerificationNumber='" + bankVerificationNumber + '\'' +
                ", taxIdentificationNumber='" + taxIdentificationNumber + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", agentBusiness='" + agentBusiness + '\'' +
                ", localGovernmentCode='" + localGovernmentCode + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", transactionPin='" + transactionPin + '\'' +
                '}';
    }
}
