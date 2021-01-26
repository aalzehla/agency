package com._3line.gravity.freedom.NIBBS.model;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.agents.models.Agents;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@Data
@Entity
public class AgentDataReport extends AbstractEntity {
    private String nibbsAgentCode;
    private String nibbsAgentEmail;
    private String status;
    private Date updatedOn;

    @OneToOne
    private Agents agent;
}
