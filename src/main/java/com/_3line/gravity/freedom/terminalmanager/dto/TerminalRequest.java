package com._3line.gravity.freedom.terminalmanager.dto;

import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;
import com._3line.gravity.freedom.agents.models.Agents;
import lombok.Data;

@Data
public class TerminalRequest extends AbstractVerifiableDto {
    private String username; //username of the agent
    private String terminalId; //terminal id to assign to
    private String action; // A for assign and D for Detach

    private Agents agents;
}
