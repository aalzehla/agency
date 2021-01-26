package com._3line.gravity.freedom.financialInstitutions.sanef.dto;


public class SanefAccountRequest {
    private String superagentCode;
    private String agentCode;
    private String bankCode;
    private String requestId;
    private String bankVerificationNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String houseNumber;
    private String streetName;
    private String city;
    private String lgaCode;
    private String emailAddress;
    private String phoneNumber;
    private String customerImage;
    private String customerSignature;
    private String accountOpeningBalance;

    public String getSuperagentCode() {
        return superagentCode;
    }

    public void setSuperagentCode(String superagentCode) {
        this.superagentCode = superagentCode;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getBankVerificationNumber() {
        return bankVerificationNumber;
    }

    public void setBankVerificationNumber(String bankVerificationNumber) {
        this.bankVerificationNumber = bankVerificationNumber;
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

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLgaCode() {
        return lgaCode;
    }

    public void setLgaCode(String lgaCode) {
        this.lgaCode = lgaCode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public String getCustomerSignature() {
        return customerSignature;
    }

    public void setCustomerSignature(String customerSignature) {
        this.customerSignature = customerSignature;
    }

    public String getAccountOpeningBalance() {
        return accountOpeningBalance;
    }

    public void setAccountOpeningBalance(String accountOpeningBalance) {
        this.accountOpeningBalance = accountOpeningBalance;
    }

    @Override
    public String toString() {
        return "SanefAccountRequest{" +
                "superagentCode='" + superagentCode + '\'' +
                ", agentCode='" + agentCode + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", requestId='" + requestId + '\'' +
                ", bankVerificationNumber='" + bankVerificationNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", streetName='" + streetName + '\'' +
                ", city='" + city + '\'' +
                ", lgaCode='" + lgaCode + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", customerImage='" + customerImage + '\'' +
                ", customerSignature='" + customerSignature + '\'' +
                ", accountOpeningBalance='" + accountOpeningBalance + '\'' +
                '}';
    }
}
