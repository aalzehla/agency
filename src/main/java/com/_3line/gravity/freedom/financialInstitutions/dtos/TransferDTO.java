package com._3line.gravity.freedom.financialInstitutions.dtos;

public class TransferDTO {
    String amount ;
    String cardCVC;
    String instrumentType ;
    String cardNumber;
    String cardDateMonth;
    String cardDateYear;
    String token;
    String bankCode;
    String destinationAccountNumber ;
    String destinationBankCode;
    String customerName;
    String customerEmail;
    String customerPhone;
    String bankName;
    String customerPin;
    String agentPin;


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

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public String getDestinationBankCode() {
        return destinationBankCode;
    }

    public void setDestinationBankCode(String destinationBankCode) {
        this.destinationBankCode = destinationBankCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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
}
