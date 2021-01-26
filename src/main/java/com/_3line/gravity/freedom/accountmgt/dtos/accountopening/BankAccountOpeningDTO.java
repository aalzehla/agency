package com._3line.gravity.freedom.accountmgt.dtos.accountopening;


import com._3line.gravity.freedom.agents.models.Agents;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@ToString
@Data
public class BankAccountOpeningDTO {


    private String agentIdentifier;
    private String bvnNumber;
    private String preferredBranch;
    private String firstName;
    private String middleName;
    private String lastName;
    private String maritalStatus;
    private String dateOfBirth;
    private String dateOfBirth2;
    private String email;
    private String gender;
    private String phoneNumber;
    private String mobile;
    private String motherMaidenName;
    private String nextOfKin;
    private String nationality;
    private String phoneNumber2;
    private String residentialAddress;
    private String stateOfOrigin;
    private String stateOfResidence;
    private String levelOfAccount;
    private String lgaOfOrigin;

    private String lgaOfResidence;
    private String resident;
    private String countryCode;
    private String birthCert;
    private String parentPhoto;
    private String howYouHeard;
    private String complete;
    private String title;
    private String trusteeTitle;
    private String trusteeCountry;
    private String trusteeCity;
    private String trusteeMaritalStatus;
    private String trusteeEmail;
    private String trusteeAddr1;
    private String trusteeAddr2;
    private String trusteeCntry;
    private String trusteeDob;
    private String trusteeFname;
    private String trusteeLname;
    private String trusteeMname;
    private String trusteePhoneNo;
    private String trusteeState;
    private String city;
    private String state;
    private String schemeCode;
    private String accountType;
    private String channelId;
    private String accountNumber;
    private String cifId;
    private String responseCode;
    private String errorMessage;
    private String responseMessage;
    private String remarks;
    private Date dateCreated;
    private String brokerCode;
    private String accountOfficerCode;
    private String photo;
    private String signature;
    private String referenceNo;
    private String occupation;
    private String operatorId;
    private Agents agentDTO;
    private String dsaBrokerCode;
    private String agentBrokerCode;
    private String region;
    private String branch;


}
