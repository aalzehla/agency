package com._3line.gravity.freedom.financialInstitutions.dtos;

public class GravityWithdrawalRequest {

    String amount ;
    String cardCVC;
    String cardCVV;
    String instrumentType ;
    String cardNumber;
    String cardDateMonth;
    String cardDateYear;
    String token;
    String bankCode;
    String customerPin ;
    String agentPin ;
    String username;
    String latitude ;
    String longitude ;
    Long tranId;
    String media;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCardCVC() {
        return cardCVC;
    }

    public void setCardCVC(String cardCVC) {
        this.cardCVC = cardCVC;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardDateMonth() {
        return cardDateMonth;
    }

    public void setCardDateMonth(String cardDateMonth) {
        this.cardDateMonth = cardDateMonth;
    }

    public String getCardDateYear() {
        return cardDateYear;
    }

    public void setCardDateYear(String cardDateYear) {
        this.cardDateYear = cardDateYear;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
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

    public String getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Long getTranId() {
        return tranId;
    }

    public void setTranId(Long tranId) {
        this.tranId = tranId;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}
