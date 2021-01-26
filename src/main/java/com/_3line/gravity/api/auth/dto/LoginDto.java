package com._3line.gravity.api.auth.dto;

import lombok.Data;

@Data
public class LoginDto {

    private String username;
    private String password;
    private String deviceId;
    private String latitude;
    private String longitude;
    private String otp;

}
