package com._3line.gravity.freedom.notifications.models;



import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.issuelog.models.IssueLog;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Where(clause = "del_flag='N'")
public class Notification extends AbstractEntity {


    @Column(nullable = false)
    String agentName;

    @Lob
    String message;

    Date loggedOn = new Date();

    String agentEmail ;
    String agentPhone ;
    String messageType ;

    private String category;

    private String sentBy;
    private String topic;

    @Lob
    private String image;

    private Boolean isRead;
    private Boolean isClosed=false;
    private Date closedDate;



}
