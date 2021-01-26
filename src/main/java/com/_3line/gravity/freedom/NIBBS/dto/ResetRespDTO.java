package com._3line.gravity.freedom.NIBBS.dto;

import lombok.Data;

@Data
public class ResetRespDTO {

    private String email;
    private String institutionCode;
    private String password;
    private String ivKey;
    private String apiKey;
    private String responseCode;

}
