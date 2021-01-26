package com._3line.gravity.freedom.agents.dtos;

import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;

public class DeviceRefreshDto extends AbstractVerifiableDto {

    private Long deviceId ;

    private String agentName;


    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
