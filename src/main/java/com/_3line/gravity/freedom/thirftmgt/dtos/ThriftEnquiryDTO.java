package com._3line.gravity.freedom.thirftmgt.dtos;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class ThriftEnquiryDTO {
    @Min(value = 1,message = "Card number cannot be empty")
    private String cardNumber ;

    @Min(value = 1,message = "Agent pin cannot be empty")
    private String agentPin ;
}
