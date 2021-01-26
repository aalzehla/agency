package com._3line.gravity.freedom.NIBBS.model;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.agents.models.Agents;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@Data
@Entity
public class TransationReportAudit extends AbstractEntity {
    private String uploadProcessId;
    private int batch;
    private String status;
    private Date updatedOn;
}
