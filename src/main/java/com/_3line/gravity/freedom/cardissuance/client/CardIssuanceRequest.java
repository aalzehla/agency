package com._3line.gravity.freedom.cardissuance.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author FortunatusE
 * @date 12/10/2018
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardIssuanceRequest {

    @NotEmpty(message = "Card serial number is required")
    private String cardSerial;
    @NotEmpty(message = "Account number is required")
    private String accountNumber;
    private String cardType;
    private String cardFeeType;
    private String channelPass;
    private String channelId;
    private String createdBy;
    private String header;
}
