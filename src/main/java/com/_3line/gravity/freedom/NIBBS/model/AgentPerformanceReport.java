package com._3line.gravity.freedom.NIBBS.model;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.agents.models.Agents;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@Data
@Entity
public class AgentPerformanceReport extends AbstractEntity {

    private String updateProcess;
    private String status;
    private int numOfFailedPush;
    private int numOfRecordsPushed;
    private Date updatedOn;
    private String fileUploaded;
}
