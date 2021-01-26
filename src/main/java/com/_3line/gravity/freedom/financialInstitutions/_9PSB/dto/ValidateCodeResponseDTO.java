package com._3line.gravity.freedom.financialInstitutions._9PSB.dto;

import lombok.Data;

@Data
public class ValidateCodeResponseDTO {

    private Double amount;
    private String firstName;
    private String lastName;
    private String walletNumber;
    private String trackingId;
    private String ban="9PSB";
    private String displayName;

}
