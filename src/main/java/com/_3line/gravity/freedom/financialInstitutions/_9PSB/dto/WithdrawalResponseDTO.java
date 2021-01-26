package com._3line.gravity.freedom.financialInstitutions._9PSB.dto;

import lombok.Data;

import java.util.Date;

@Data
public class WithdrawalResponseDTO extends WithdrawalRequestDTO{

    private Date tranDate;
    private String transactionRef;
    private Double amount;

}
