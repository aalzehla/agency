package com._3line.gravity.freedom.terminalmanager.model;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.agents.models.Agents;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@Data
public class TerminalAudit extends AbstractEntity {
    @OneToOne
    private Agents assignToAgent;
    @OneToOne
    private Agents aggregator;
    private String terminalId;
    private Date dateAssigned;
    private String description;
}
