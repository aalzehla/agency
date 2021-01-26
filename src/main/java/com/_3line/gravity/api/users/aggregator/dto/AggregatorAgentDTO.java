package com._3line.gravity.api.users.aggregator.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class AggregatorAgentDTO {

    private Boolean isFirstTime = true;

    private String username;
    private String email;
    private String password;

    
    private long belongsTo;

    private int activated;

    
    private BigInteger createdby;

    
    private Date lastloggedin;

    
    
    
    private Date datecreated;

    
    
    
    
    private String firstName;

    
    
    private String middleName;

    
    
    
    
    private String lastName;

    
    
    private String city;

    
    
    private String lga;

    
    
    private String state;

    
    
    private String country;

    
    
    private String address;

    
    
    
    private String phoneNumber;

    
    
    private Date lastPasswordChangeDate;

    
    
    private int unsuccessfulLoginCount;

    
    private long structureid;

    
    
    private String accountNo;

    
    
    private String agentCompanyId;

    
    
    private Date dob;

    
    
    private String gender;

    
    
    private String highestEducationalLevel;

    
    
    private String identificationType;

    
    
    private Date presentAddressDateOfEntry;

    
    
    private String secondPhoneNumber;

    
    private String bankCode ;

    private String bvn;

    private String idNumber ;

    
    private String terminalId;

    private String walletNumber ;

    private Long parentAgentId ;

    private String comAccountNo;

    private String comAccountBank ;

    private String buisnessLocation;

    
    private String businessName;

    private String agentType ;

    private String wards;

    private String incomeWalletNumber;

    private String userPin;

    private String posTerminalTranFee = "0.75";

    private String agentId;

    private String agentCode;
    private String statusOfCreations;
}
