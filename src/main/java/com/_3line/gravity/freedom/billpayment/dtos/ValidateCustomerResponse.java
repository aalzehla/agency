package com._3line.gravity.freedom.billpayment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ValidateCustomerResponse {

    private String customerName;
    private String totalamount;
    private String charge;
    private String responseCode;
    private String responseDescription;

}
