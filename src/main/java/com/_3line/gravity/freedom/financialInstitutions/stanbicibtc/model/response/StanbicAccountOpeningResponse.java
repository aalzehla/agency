package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author Kwerenachi Utosu
 * @date 10/15/2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StanbicAccountOpeningResponse {

    private StanbicApiResponse apiResponse;
    private Object responseItem;
    private String token;
    private String session;

}
