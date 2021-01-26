package com._3line.gravity.freedom.accountmgt.dtos.walletopening;

import lombok.ToString;

import java.util.Date;

/**
 * @author FortunatusE
 * @date 12/2/2018
 */

@ToString
public class WalletAccountOpeningDTO {


    private String agentIdentifier;
    private String requestID;
    private String channel;
    private String paymentMode;
    private String mobilePhone;
    private String firstName;
    private String lastName;
    private String gender;
    private String birthDate;
    private String pin;
    private String schemeCode;
    private String responseCode;
    private String errorMessage;
    private String responseMessage;
    private String remark;
    private Date dateCreated;
    private String dsaBrokerCode;

    public String getDsaBrokerCode() {
        return dsaBrokerCode;
    }

    public void setDsaBrokerCode(String dsaBrokerCode) {
        this.dsaBrokerCode = dsaBrokerCode;
    }

    public String getAgentIdentifier() {
        return agentIdentifier;
    }

    public void setAgentIdentifier(String agentIdentifier) {
        this.agentIdentifier = agentIdentifier;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
