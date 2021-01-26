package com._3line.gravity.freedom.issuelog.models;



import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.dispute.models.DisputeLog;
import com._3line.gravity.freedom.notifications.models.Notification;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Where(clause = "del_Flag = 'N'")
public class IssueLog extends AbstractEntity {


    @Lob

    String complaint;  // message

    @Enumerated(EnumType.STRING)
    IssueStatus status = IssueStatus.PENDING;

    private String category;

    Date loggedOn = new Date();

    String comment ;
    String ticketId ;



    private String treatedBy;

    @Lob
    private String image;

    private Boolean isRead;
    private Boolean isClosed;
    private Date closedDate;

    private Date treatedDate;

    @ManyToMany(cascade ={ CascadeType.MERGE})
    private List<Comments> comments = new ArrayList<>();

    @OneToOne
    private Notification notification;

    @OneToOne
    private Agents agent;

    @OneToOne
    private DisputeLog disputeLog;



}
