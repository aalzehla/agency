package com._3line.gravity.freedom.accountmgt.dtos.walletopening;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author JoyU
 * @date 11/14/2018
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletAccountOpeningRequest {

    @JsonProperty("RequestID")
    private String requestID;
    @JsonProperty("Channel")
    private String channel;
    @JsonProperty("PaymentMode")
    private String paymentMode;
    @NotEmpty(message = "Mobile phone is required")
    @JsonProperty("MobilePhone")
    private String mobilePhone;
    @NotEmpty(message = "First name is required")
    @JsonProperty("FirstName")
    private String firstName;
    @NotEmpty(message = "Last name is required")
    @JsonProperty("LastName")
    private String lastName;
    @NotEmpty(message = "Gender is required")
    @JsonProperty("Gender")
    private String gender;
    @NotEmpty(message = "Birth date is required")
    @JsonProperty("BirthDate")
    private String birthDate;
    @JsonProperty("Pin")
    private String pin;
    @JsonProperty("SchemeCode")
    private String schemeCode;
    @JsonIgnore
    private String aoCode;
    @JsonIgnore
    private String brokerCode;



}
