/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;

import com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody.ErrorMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author  owolabi
 */
@Data
public class AcctCreationResponse {

    @JsonProperty(value = "retVal")
    private String retVal;

    @JsonProperty(value = "AccountNumber")
    private String AccountNumber;

    @JsonProperty(value = "successMessage")
    private SuccessMessage successMessage;

    @JsonProperty(value = "errorMessages")
    private List<ErrorMessage> errorMessages;

    private String Message;


}
