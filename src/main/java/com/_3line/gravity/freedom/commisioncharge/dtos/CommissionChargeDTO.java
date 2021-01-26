package com._3line.gravity.freedom.commisioncharge.dtos;

import lombok.Data;

@Data
public class CommissionChargeDTO {

    private Long id;

    private Double upperBound;

    private Double lowerBound;

    private String type;

    private Double amount;

    private String institution;


}
