package com._3line.gravity.api.auth.dto;

import lombok.Data;

@Data
public class LoginRespDto {

    private String username;
    private String fullName;
    private String password;
    private String deviceId;
    private String terminalId;
    private String latitude;
    private String longitude;
    private String otp;

    private String auth_token;
    private String isNewDevice;
    private String isFirstTime;
    private String secretKey;
    private String activated;
    private String accountNo;
    private String agentName;
    private String agentId;
    private String phoneNumber;
    private String address;
    private String lga;
    private String state;
    private String city;
    private String walletBalance;
    private String walletNumber;
    private String incomeBalance;
    private String incomeNumber;
    private String userType;
}
