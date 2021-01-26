package com._3line.gravity.freedom.financialInstitutions.gtbankapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author JoyU
 * @date 10/4/2018
 */
@Data
public class AccOpeningGTB {

    @JsonProperty("FirstName")
    private String firstName;
    @JsonProperty("LastName")
    private String lastName;
    @JsonProperty("Gender")
    private String gender;
    @JsonProperty("DoB")
    private String doB;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("MobileNo")
    private String mobileNo;
    @JsonProperty("MotherMaiden")
    private String motherMaiden;
    @JsonProperty("Bvn")
    private String bvn;
    private String requestId;
    @JsonProperty("Channel")
    private String channel;
    @JsonProperty("UserId")
    private String userId;
    @JsonProperty("customerNumber")
    private String customerNumber;
    @JsonProperty("SessionId")
    private String sessionId;
    @JsonProperty("Email")
    private String email;
    private String nuban;
    private String password;
    @JsonProperty("userId")
    private String responseUserId;
    @JsonProperty("TeamNo")
    private String teamNo;
    private String code;
    private String message;
    private String error;
    private String responseCode;
    private String responseDescription;

    @Override
    public String toString() {
        return "AccOpeningGTB{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", doB='" + doB + '\'' +
                ", address='" + address + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", motherMaiden='" + motherMaiden + '\'' +
                ", bvn='" + bvn + '\'' +
                ", requestId='" + requestId + '\'' +
                ", channel='" + channel + '\'' +
                ", userId='" + userId + '\'' +
                ", customerNumber='" + customerNumber + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", email='" + email + '\'' +
                ", nuban='" + nuban + '\'' +
                ", password='" + password + '\'' +
                ", responseUserId='" + responseUserId + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseDescription='" + responseDescription + '\'' +
                '}';
    }
}
