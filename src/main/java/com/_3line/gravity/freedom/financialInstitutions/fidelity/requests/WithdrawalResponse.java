/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author  owolabi
 */
@Data
public class WithdrawalResponse {

    @JsonProperty(value = "retVal")
    private String retVal;

    @JsonProperty(value = "TransactionID")
    private String transactionId;

    @JsonProperty(value = "TransactionDate")
    private String transactionDate;

    @JsonProperty(value = "successMessage")
    private List<SuccessMessage> successMessage;

    @JsonProperty(value = "errorMessages")
    private String errorMessages;

}
