package com._3line.gravity.freedom.financialInstitutions._9PSB.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DepositWithoutCodeRespDTO extends DepositResponseDTO{

    private String walletNumber;
    private Double amount;


}
