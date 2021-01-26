package com._3line.gravity.api.transaction.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ReverseTransactionDTO {

    private String transactionId ;
    private String comment;
}
