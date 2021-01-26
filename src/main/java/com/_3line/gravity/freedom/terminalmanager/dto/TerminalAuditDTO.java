package com._3line.gravity.freedom.terminalmanager.dto;

import com._3line.gravity.freedom.agents.models.Agents;
import lombok.Data;

import java.util.Date;

@Data
public class TerminalAuditDTO {
    private Long id;
    private int version;
    private String assignToAgent;
    private String aggregator;
    private String terminalId;
    private String dateAssigned;
    private String description;
}
