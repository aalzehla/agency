package com._3line.gravity.freedom.financialInstitutions.gtbankapi.models;

public class SingleTransferAPI {

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

    @Override
    public String toString() {
        return "SingleTransferAPI{" +
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
                '}';
    }
}
