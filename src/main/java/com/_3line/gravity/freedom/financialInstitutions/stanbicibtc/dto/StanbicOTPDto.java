package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.dto;

/**
 * @author Kwerenachi Utosu
 * @date 10/16/2019
 */
public class StanbicOTPDto {

    private String agentId;
    private String phoneNumber;
    private String pin;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
