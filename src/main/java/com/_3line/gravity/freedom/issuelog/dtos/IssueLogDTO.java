package com._3line.gravity.freedom.issuelog.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class IssueLogDTO {

    String agentName;
    String agentPhone;

    String complaint;

    String agentEmail ;

    String comment ;

    String status;
    String ticketId;

    Long id;

    Date loggedOn ;

    private String buttonAction;

    private Boolean isRead;

    private String category; //category

    private String treatedBy; //treated by

    private String image;

    private Date treatedDate;  //treated Date

    private boolean mailIndicatior;

    private String compose;

}
