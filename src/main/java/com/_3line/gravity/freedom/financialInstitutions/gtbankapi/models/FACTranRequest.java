package com._3line.gravity.freedom.financialInstitutions.gtbankapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class FACTranRequest extends FACValidate{

    @JsonIgnore
    private String agentId;

    private String agentPin;
    @JsonIgnore
    private String terminalId;
    private String amount;
}
