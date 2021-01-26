package com._3line.gravity.core.security.jwt;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String username;
    private String password;


}
