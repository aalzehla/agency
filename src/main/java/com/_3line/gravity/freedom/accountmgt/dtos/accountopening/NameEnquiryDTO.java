package com._3line.gravity.freedom.accountmgt.dtos.accountopening;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class NameEnquiryDTO {

    @JsonProperty("AccountNumber")
    private String accountNumber;
    @JsonProperty("AccountType")
    private String accountType;
    @JsonProperty("BirthDate")
    private String birthDate;
    @JsonProperty("CifCode")
    private String cifCode;
    @JsonProperty("Currency")
    private String currency;
    @JsonProperty("DateOpen")
    private String dateOpen;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("FirstName")
    private String firstName;
    @JsonProperty("FrezeCode")
    private String freezeCode;
    @JsonProperty("FrezeReason")
    private String freezeReason;
    @JsonProperty("FullName")
    private String fullName;
    @JsonProperty("MobileNumber")
    private String mobileNumber;
    @JsonProperty("OtherName")
    private String otherName;
    @JsonProperty("Remark")
    private String remark;
    @JsonProperty("RequestData")
    private String requestData;
    @JsonProperty("RequestID")
    private String requestID;
    @JsonProperty("ResponseMsg")
    private String responseMsg;
    @JsonProperty("SchmCode")
    private String schemeCode;
    @JsonProperty("Surnane")
    private String surname;
    @JsonProperty("TransStatus")
    private String transStatus;
    private String responseCode;
    private String responseMessage;



}
