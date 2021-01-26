package com._3line.gravity.freedom.financialInstitutions.dtos;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response.StanbicAccountOpeningResponse;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response.StanbicApiResponse;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response.StanbicResponseItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author JoyU
 * @date 10/8/2018
 */


@Data
public class AccOpeningGeneral {

    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private String motherMaiden;
    private String bvn;
    private String email;
    private String superAgentId;
    private String subAgentId;
    @JsonIgnore
    private String tranReference;
    private String middleName;
    private String dateOfBirth;
    private String phoneNo;
    private String photo;
    private String signature;
    private String amount;
    private String branchCode;

    @JsonIgnore
    private String responseCode;
    @JsonIgnore
    private String responseDesc;

    @JsonIgnore
    private String tranId;

    @JsonIgnore
    private String tranType;
    @JsonIgnore
    private String tranDate;

    private String accountNumber;
    private String bankCode;

    @JsonIgnore
    private String nuban;

    @JsonIgnore
    private String password;
    @JsonIgnore
    private String responseUserId;
    @JsonIgnore
    private String code;
    private String message;
    @JsonIgnore
    private String error;
    @JsonIgnore
    private String responseDescription;
    @JsonIgnore
    private String agentName;
    @JsonIgnore
    private String teamNo;
    private String title;
    private String maritalStatus;
    private String lga;
    private String state;
    private String occupation;
    private String phoneNumber;
    private String image;
    private String city;
    private String referalCode;
    private String otp;
    private String otpReference;
    private String agentId;
    private String pin;
    private String postedBy;

    private StanbicApiResponse apiResponse;
    private Object responseItem;
    private String token;
    private String session;

}
