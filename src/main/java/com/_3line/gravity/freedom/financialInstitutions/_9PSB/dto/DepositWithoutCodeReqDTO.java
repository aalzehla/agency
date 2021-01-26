package com._3line.gravity.freedom.financialInstitutions._9PSB.dto;

import lombok.Data;

@Data
public class DepositWithoutCodeReqDTO extends DepositRequestDTO{

    private Double amount;
    private String walletNumber;

}

