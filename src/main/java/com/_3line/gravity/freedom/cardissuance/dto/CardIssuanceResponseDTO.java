package com._3line.gravity.freedom.cardissuance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author FortunatusE
 * @date 12/10/2018
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardIssuanceResponseDTO {

    private boolean successful;
    private String statusCode;
    private String statusMessage;
}
