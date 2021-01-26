package com._3line.gravity.freedom.financialInstitutions.gtbankapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FACValidate {

    private String facCode;
    private String mobileNumber;
    @JsonIgnore
    private String uniqueId;
    @JsonIgnore
    private String hash;


}
