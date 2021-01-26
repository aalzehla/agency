/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;

import lombok.Data;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author  owolabi
 */
@Data
public class GenerateOTPResponse extends BaseRequest {

    private String retVal;
    private List<SuccessMessage> successMessage;
    private String errorMessages;

}
