package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author Kwerenachi Utosu
 * @date 10/15/2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StanbicOTPRequest {

    private String agentId;
    private String phoneNumber;

}
