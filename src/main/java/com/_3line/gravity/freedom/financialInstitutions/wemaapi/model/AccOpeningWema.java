package com._3line.gravity.freedom.financialInstitutions.wemaapi.model;

/**
 * @author JoyU
 * @date 9/18/2018
 */
public class AccOpeningWema {

    private String superAgentId;
    private String subAgentId;
    private String tranReference;
    private String when;
    private String securitySessionKey;
    private String firstName;
    private String lastName;
    private String middleName;
    private String gender;
    private String dateOfBirth;
    private String address;
    private String phoneNo;
    private String photo;
    private String signature;
    private String amount;
    private String branchCode;
    private String ussdEnrollment;
    private String responseCode;
    private String responseDesc;
    private String tranId;
    private String tranType;
    private String tranDate;
    private String accountNumber;

    public String getSuperAgentId() {
        return superAgentId;
    }

    public void setSuperAgentId(String superAgentId) {
        this.superAgentId = superAgentId;
    }

    public String getSubAgentId() {
        return subAgentId;
    }

    public void setSubAgentId(String subAgentId) {
        this.subAgentId = subAgentId;
    }

    public String getTranReference() {
        return tranReference;
    }

    public void setTranReference(String tranReference) {
        this.tranReference = tranReference;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getSecuritySessionKey() {
        return securitySessionKey;
    }

    public void setSecuritySessionKey(String securitySessionKey) {
        this.securitySessionKey = securitySessionKey;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getUssdEnrollment() {
        return ussdEnrollment;
    }

    public void setUssdEnrollment(String ussdEnrollment) {
        this.ussdEnrollment = ussdEnrollment;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDesc() {
        return responseDesc;
    }

    public void setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return "AccountOpeningAPI{" +
                "superAgentId='" + superAgentId + '\'' +
                ", subAgentId='" + subAgentId + '\'' +
                ", tranReference='" + tranReference + '\'' +
                ", when='" + when + '\'' +
                ", securitySessionKey='" + securitySessionKey + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", address='" + address + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", photo='" + photo + '\'' +
                ", signature='" + signature + '\'' +
                ", amount='" + amount + '\'' +
                ", branchCode='" + branchCode + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseDesc='" + responseDesc + '\'' +
                ", tranDate='" + tranDate + '\'' +
                ", tranType='" + tranType + '\'' +
                ", tranId='" + tranId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", ussdEnrollment='" + ussdEnrollment + '\'' +
                '}';
    }
}
