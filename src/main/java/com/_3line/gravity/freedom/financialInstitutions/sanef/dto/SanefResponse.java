package com._3line.gravity.freedom.financialInstitutions.sanef.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SanefResponse {
    @JsonProperty(value = "Data")
    private String data;
}
