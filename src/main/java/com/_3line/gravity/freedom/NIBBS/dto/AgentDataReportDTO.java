package com._3line.gravity.freedom.NIBBS.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AgentDataReportDTO {

    private String additionalInfo1;
    private String additionalInfo2;
    private String bvn;
    private String city;
    private String emailAddress;
    private double latitude;
    private double longitude;
    private String lga;
    private String state;
    private String firstName;
    private String lastName;
    private String middleName;
    private String title;
    private String[] phoneList = new String[]{};
    private String[] servicesProvided = new String[]{};
    private String username;
    private String streetNumber;
    private String streetName;
    private String streetDescription;
    private String ward;
    private String password;
    private String responseCode;
    private String responseMsg;
    private String agentCode;
    private String[] errors;

}
