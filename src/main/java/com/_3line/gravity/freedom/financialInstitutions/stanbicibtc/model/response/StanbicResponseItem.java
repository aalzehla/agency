package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author Kwerenachi Utosu
 * @date 10/16/2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StanbicResponseItem {

    private String code;
    private String description;
    private String reference;
    private String trackingId;
    private String responseCode;
    private String responseDescription;
    private String details;
    private String currency;
    private String accountNumber;
    private String cif;
    private String accountType;
    private String status;


}
