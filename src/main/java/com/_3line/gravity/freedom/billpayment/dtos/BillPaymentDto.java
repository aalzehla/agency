package com._3line.gravity.freedom.billpayment.dtos;


import com._3line.gravity.core.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author JoyU
 * @date 10/15/2018
 */

@Data
public class BillPaymentDto extends AbstractEntity {

    private String Amount;
    private String RequestRef;
    private String PaymentCode;
    private String CustomerId;
    private String Signature;
    private String TransactionRef;
    private String ResponseCode;
    private String ResponseDescription;
    private String Pin;
    private CustomerDetails CustomerDetails;

}
