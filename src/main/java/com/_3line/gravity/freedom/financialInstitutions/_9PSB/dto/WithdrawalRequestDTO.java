package com._3line.gravity.freedom.financialInstitutions._9PSB.dto;

import lombok.Data;

@Data
public class WithdrawalRequestDTO {

    private String requestId;
    private String otp;
    private String withdrawalTrackingId;
    private String otpTrackingId;
    private String narration;
    private String pin;



}
