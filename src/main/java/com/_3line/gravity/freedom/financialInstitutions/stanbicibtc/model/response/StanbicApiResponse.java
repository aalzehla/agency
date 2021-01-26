package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author Kwerenachi Utosu
 * @date 10/15/2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StanbicApiResponse {

    private String responseCode;
    private String responseDescription;
    private String friendlyMessage;

}
