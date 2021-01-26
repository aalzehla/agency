package com._3line.gravity.freedom.financialInstitutions.dtos;

public class TransferRequest {
    private String customerAccount;
    private String customerBankCode;
    private String receivingBankCode;
    private String receivingBankAccount;
    private String customerPin;
    private String agentPin;
    private String amount;
    private String customerName ;
    private String phoneNumber ;
    private String customerEmail;
    private String agentUsername;
    private String longitude;
    private String latitude ;
    private String deviceId;
    private String narration;
    private  String media;

    public String getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(String customerAccount) {
        this.customerAccount = customerAccount;
    }

    public String getCustomerBankCode() {
        return customerBankCode;
    }

    public void setCustomerBankCode(String customerBankCode) {
        this.customerBankCode = customerBankCode;
    }

    public String getReceivingBankCode() {
        return receivingBankCode;
    }

    public void setReceivingBankCode(String receivingBankCode) {
        this.receivingBankCode = receivingBankCode;
    }

    public String getReceivingBankAccount() {
        return receivingBankAccount;
    }

    public void setReceivingBankAccount(String receivingBankAccount) {
        this.receivingBankAccount = receivingBankAccount;
    }

    public String getCustomerPin() {
        return customerPin;
    }

    public void setCustomerPin(String customerPin) {
        this.customerPin = customerPin;
    }

    public String getAgentPin() {
        return agentPin;
    }

    public void setAgentPin(String agentPin) {
        this.agentPin = agentPin;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getAgentUsername() {
        return agentUsername;
    }

    public void setAgentUsername(String agentUsername) {
        this.agentUsername = agentUsername;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return "TransferRequest{" +
                "customerAccount='" + customerAccount + '\'' +
                ", customerBankCode='" + customerBankCode + '\'' +
                ", receivingBankCode='" + receivingBankCode + '\'' +
                ", receivingBankAccount='" + receivingBankAccount + '\'' +
                ", customerPin='" + customerPin + '\'' +
                ", agentPin='" + agentPin + '\'' +
                ", amount='" + amount + '\'' +
                ", customerName='" + customerName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", agentUsername='" + agentUsername + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
