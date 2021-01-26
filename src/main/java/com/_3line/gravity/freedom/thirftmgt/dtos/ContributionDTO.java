package com._3line.gravity.freedom.thirftmgt.dtos;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class ContributionDTO {

    String cardNumber ;
    @Min(value = 1,message = "amount should be greater than 1")
    Double amount ;

    String longitude ;
    String latitude ;
    String agentName ;
    String agentPin ;

}
