package com._3line.gravity.freedom.dispute.models;



import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.issuelog.models.IssueLog;
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
public class DisputeLog extends AbstractEntity {


    private Long tranId;
    private Double tranAmount;
    private String tranType;
    private Date tranDate;
    private String complaint;
    private String image;
    private String rrn;
    private String terminalId;
    private String pan;
    private String remark;
    private String loggedBy;
    private String tranStatus;

    @OneToOne
    private Dispute dispute;


}
