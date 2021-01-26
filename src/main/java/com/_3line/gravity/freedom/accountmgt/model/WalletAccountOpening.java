package com._3line.gravity.freedom.accountmgt.model;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.agents.models.Agents;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Arrays;
import java.util.List;

@ToString
@Entity
public class WalletAccountOpening extends AbstractEntity {



    @ManyToOne
    private Agents agent ;

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
    private String aoCode;
    private String brokerCode;
    private String dsaBrokerCode;

    public String getDsaBrokerCode() {
        return dsaBrokerCode;
    }

    public void setDsaBrokerCode(String dsaBrokerCode) {
        this.dsaBrokerCode = dsaBrokerCode;
    }

    public String getAoCode() {
        return aoCode;
    }

    public void setAoCode(String aoCode) {
        this.aoCode = aoCode;
    }

    public String getBrokerCode() {
        return brokerCode;
    }

    public void setBrokerCode(String brokerCode) {
        this.brokerCode = brokerCode;
    }

    public Agents getAgent() {
        return agent;
    }

    public void setAgent(Agents agent) {
        this.agent = agent;
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

    @JsonIgnore
    @Override
    public List<String> getDefaultSearchFields(){
        return Arrays.asList("firstName", "lastName", "mobilePhone");
    }

}
