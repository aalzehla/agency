package com._3line.gravity.freedom.financialInstitutions.gtbankapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class FACTranResponse extends FACTranRequest{

    private String senderName;
    private String transRef;

}
