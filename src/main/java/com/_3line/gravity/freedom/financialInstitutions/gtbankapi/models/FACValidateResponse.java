package com._3line.gravity.freedom.financialInstitutions.gtbankapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FACValidateResponse {

    private String facCode;
    private String amount;
    private String tranRef;
    private String customerName;


}
