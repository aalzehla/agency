package com._3line.gravity.freedom.issuelog.dtos;

import lombok.Data;

@Data
public class IssueLogRequestDTO {


//    private String id;
    private String category;
    private String complaint;   //message
    private String comment;   //reply
    private String agentName;
    private String agentEmail;
    private String treatedBy;
//    private String loggedOn;    //treatedOn
    private String image;
    private Boolean isRead;

}
