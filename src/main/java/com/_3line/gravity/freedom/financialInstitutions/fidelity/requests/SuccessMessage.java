/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 *
 * @author  owolabi
 */
@Data
public class SuccessMessage extends BaseRequest {

    @JsonProperty(value = "successCode")
    private String successCode;

    @JsonProperty(value = "successMessage")
    private String successMessage;

}
