package com._3line.gravity.freedom.financialInstitutions._9PSB.dto;

import lombok.Data;

@Data
public class DepositRequestDTO {

    private String requestId;
    private String depositCode;
    private String customerPhone;
    private String lastName;
    private String firstName;
    private String narration;
    private String depositorPhone;
    private String pin;

}
