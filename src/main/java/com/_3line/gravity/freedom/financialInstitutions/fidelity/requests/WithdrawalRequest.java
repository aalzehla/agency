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
public class WithdrawalRequest {

    @JsonProperty(value = "DebitAccountNumber")
    private String debitAcctNumber;

    @JsonIgnore
    @JsonProperty(value = "CreditAccountNumber")
    private String creditAcctNumber;

    @JsonProperty(value = "Amount")
    private String amount;

    @JsonProperty(value = "Narration")
    private String narration;

    @JsonIgnore
    @JsonProperty(value = "RequestTransactionID")
    private String requestTranId;

    @JsonIgnore
    @JsonProperty(value = "TransactionHash")
    private String transactionHash;

    @JsonProperty(value = "OTP")
    private String otp;

}
