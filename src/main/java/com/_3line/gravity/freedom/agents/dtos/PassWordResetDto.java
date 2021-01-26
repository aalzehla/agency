package com._3line.gravity.freedom.agents.dtos;

import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;

public class PassWordResetDto extends AbstractVerifiableDto {


    private Long agentId;
    private String agentName;


    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
