package com._3line.gravity.freedom.financialInstitutions._9PSB.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DepositResponseDTO extends DepositRequestDTO{

    private String transactionRef;
    private Date tranDate;


}
