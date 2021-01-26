package com._3line.gravity.core.sms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Kwerenachi Utosu
 * @date 4/2/2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendSmsRequest {

    private String mobileNumber;
    private String message;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SendSmsRequest{" +
                "mobileNumber='" + mobileNumber + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
