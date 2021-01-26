package com._3line.gravity.freedom.financialInstitutions.gtbankapi.models;


import com._3line.gravity.core.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import java.util.Date;

@Entity
public class GTBApiAudit extends AbstractEntity {

    private String customerId;
    private String password;
    private String userName;
    private String accountNumber;
    private String amount;
    private String paymentdate;
    private String reference;
    private String remarks;
    private String vendorcode;
    private String vendorname;
    private String vendoracctnumber;
    private String vendorbankcode;
    private String customeracctnumber;
    private String vendoraccttype;
    private String responseCode;
    private String responseMessage;
    private String currency;
    private String avaliableBalance;
    private String ledgerBalance;
    private String time;
    private Date dateCreated;
    private String firstName;
    private String lastName;
    private String gender;
    private String doB;
    private String address;
    private String mobileNo;
    private String motherMaiden;
    private String bvn;
    private String requestId;
    private String channel;
    private String userId;
    private String customerNumber;
    private String sessionId;
    private String email;
    private String nuban;
    private String responseUserId;
    private String code;
    private String message;
    private String error;
    @Lob
    private String hash;
    @Lob
    private String requestPayload;
    @Lob
    private String responsePayload;
    @Enumerated(value = EnumType.STRING)
    private ApiType apiType;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDoB() {
        return doB;
    }

    public void setDoB(String doB) {
        this.doB = doB;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMotherMaiden() {
        return motherMaiden;
    }

    public void setMotherMaiden(String motherMaiden) {
        this.motherMaiden = motherMaiden;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNuban() {
        return nuban;
    }

    public void setNuban(String nuban) {
        this.nuban = nuban;
    }

    public String getResponseUserId() {
        return responseUserId;
    }

    public void setResponseUserId(String responseUserId) {
        this.responseUserId = responseUserId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getVendorcode() {
        return vendorcode;
    }

    public void setVendorcode(String vendorcode) {
        this.vendorcode = vendorcode;
    }

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public String getVendoracctnumber() {
        return vendoracctnumber;
    }

    public void setVendoracctnumber(String vendoracctnumber) {
        this.vendoracctnumber = vendoracctnumber;
    }

    public String getVendorbankcode() {
        return vendorbankcode;
    }

    public void setVendorbankcode(String vendorbankcode) {
        this.vendorbankcode = vendorbankcode;
    }

    public String getCustomeracctnumber() {
        return customeracctnumber;
    }

    public void setCustomeracctnumber(String customeracctnumber) {
        this.customeracctnumber = customeracctnumber;
    }

    public String getVendoraccttype() {
        return vendoraccttype;
    }

    public void setVendoraccttype(String vendoraccttype) {
        this.vendoraccttype = vendoraccttype;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAvaliableBalance() {
        return avaliableBalance;
    }

    public void setAvaliableBalance(String avaliableBalance) {
        this.avaliableBalance = avaliableBalance;
    }

    public String getLedgerBalance() {
        return ledgerBalance;
    }

    public void setLedgerBalance(String ledgerBalance) {
        this.ledgerBalance = ledgerBalance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getRequestPayload() {
        return requestPayload;
    }

    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }

    public String getResponsePayload() {
        return responsePayload;
    }

    public void setResponsePayload(String responsePayload) {
        this.responsePayload = responsePayload;
    }

    public ApiType getApiType() {
        return apiType;
    }

    public void setApiType(ApiType apiType) {
        this.apiType = apiType;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "GTBApiAudit{" +
                "customerId='" + customerId + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", amount='" + amount + '\'' +
                ", paymentdate='" + paymentdate + '\'' +
                ", reference='" + reference + '\'' +
                ", remarks='" + remarks + '\'' +
                ", vendorcode='" + vendorcode + '\'' +
                ", vendorname='" + vendorname + '\'' +
                ", vendoracctnumber='" + vendoracctnumber + '\'' +
                ", vendorbankcode='" + vendorbankcode + '\'' +
                ", customeracctnumber='" + customeracctnumber + '\'' +
                ", vendoraccttype='" + vendoraccttype + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                ", currency='" + currency + '\'' +
                ", avaliableBalance='" + avaliableBalance + '\'' +
                ", ledgerBalance='" + ledgerBalance + '\'' +
                ", time='" + time + '\'' +
                ", dateCreated=" + dateCreated +
                ", hash='" + hash + '\'' +
                ", requestPayload='" + requestPayload + '\'' +
                ", responsePayload='" + responsePayload + '\'' +
                ", apiType=" + apiType +
                '}';
    }
}
