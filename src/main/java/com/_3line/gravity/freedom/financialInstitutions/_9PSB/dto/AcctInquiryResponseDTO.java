package com._3line.gravity.freedom.financialInstitutions._9PSB.dto;

import lombok.Data;

@Data
public class AcctInquiryResponseDTO extends AcctInquiryRequestDTO {

    private String bankCode;
    private String accountNumber;
    private String firstName;
    private String lastName;

}
