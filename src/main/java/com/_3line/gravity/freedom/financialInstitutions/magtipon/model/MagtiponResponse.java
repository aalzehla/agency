package com._3line.gravity.freedom.financialInstitutions.magtipon.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MagtiponResponse {

    @JsonProperty("TransactionRef")
    public String TransactionRef ;

    @JsonProperty("ResponseCode")
    public String ResponseCode ;

    @JsonProperty("ResponseDescription")
    public String  ResponseDescription ;


}
