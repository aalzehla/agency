package com._3line.gravity.freedom.financialInstitutions.wemaapi.model;


import com._3line.gravity.core.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

/**
 * @author JoyU
 * @date 9/18/2018
 */

@Entity
public class WemaApiAudit extends AbstractEntity {

    private String superAgentId;
    private String subAgentId;
    private String tranReference;
    private String tranRefResponse;
    private String whenn;
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
    private String branchDescription;
    private String ussdEnrollment;
    private String bankName;
    private String bankCode;
    private String accountNumber;
    private String channelCode;
    private String destinationBankCode;
    private String destinationAccountNumber;
    private String nameResponse;
    private String responseCode;
    private String responseDesc;
    private String tranId;
    private String tranType;
    private String tranDate;
    private String sessionId;
    private String refNumber;
    private String accountName;
    private String customerAccToCredit;
    private String customerAccToDebit;
    private String password;
    private String token;
    @Lob
    private String xmlRequestPayload;
    @Lob
    private String xmlResponsePayload;
    @Enumerated(value = EnumType.STRING)
    private WemaApiType wemaApiType;

    public String getTranRefResponse() {
        return tranRefResponse;
    }

    public void setTranRefResponse(String tranRefResponse) {
        this.tranRefResponse = tranRefResponse;
    }

    public String getBranchDescription() {
        return branchDescription;
    }

    public void setBranchDescription(String branchDescription) {
        this.branchDescription = branchDescription;
    }

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

    public String getWhenn() {
        return whenn;
    }

    public void setWhenn(String whenn) {
        this.whenn = whenn;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getDestinationBankCode() {
        return destinationBankCode;
    }

    public void setDestinationBankCode(String destinationBankCode) {
        this.destinationBankCode = destinationBankCode;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public String getNameResponse() {
        return nameResponse;
    }

    public void setNameResponse(String nameResponse) {
        this.nameResponse = nameResponse;
    }

    public String getXmlRequestPayload() {
        return xmlRequestPayload;
    }

    public void setXmlRequestPayload(String xmlRequestPayload) {
        this.xmlRequestPayload = xmlRequestPayload;
    }

    public String getXmlResponsePayload() {
        return xmlResponsePayload;
    }

    public void setXmlResponsePayload(String xmlResponsePayload) {
        this.xmlResponsePayload = xmlResponsePayload;
    }

    public WemaApiType getWemaApiType() {
        return wemaApiType;
    }

    public void setWemaApiType(WemaApiType wemaApiType) {
        this.wemaApiType = wemaApiType;
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

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCustomerAccToCredit() {
        return customerAccToCredit;
    }

    public void setCustomerAccToCredit(String customerAccToCredit) {
        this.customerAccToCredit = customerAccToCredit;
    }

    public String getCustomerAccToDebit() {
        return customerAccToDebit;
    }

    public void setCustomerAccToDebit(String customerAccToDebit) {
        this.customerAccToDebit = customerAccToDebit;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "WemaApiAudit{" +
                "superAgentId='" + superAgentId + '\'' +
                ", subAgentId='" + subAgentId + '\'' +
                ", tranReference='" + tranReference + '\'' +
                ", tranRefResponse='" + tranRefResponse + '\'' +
                ", whenn='" + whenn + '\'' +
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
                ", branchDescription='" + branchDescription + '\'' +
                ", ussdEnrollment='" + ussdEnrollment + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", channelCode='" + channelCode + '\'' +
                ", destinationBankCode='" + destinationBankCode + '\'' +
                ", destinationAccountNumber='" + destinationAccountNumber + '\'' +
                ", nameResponse='" + nameResponse + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseDesc='" + responseDesc + '\'' +
                ", tranId='" + tranId + '\'' +
                ", tranType='" + tranType + '\'' +
                ", tranDate='" + tranDate + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", refNumber='" + refNumber + '\'' +
                ", accountName='" + accountName + '\'' +
                ", customerAccToCredit='" + customerAccToCredit + '\'' +
                ", customerAccToDebit='" + customerAccToDebit + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", xmlRequestPayload='" + xmlRequestPayload + '\'' +
                ", xmlResponsePayload='" + xmlResponsePayload + '\'' +
                ", wemaApiType=" + wemaApiType +
                '}';
    }
}
