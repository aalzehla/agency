package com._3line.gravity.api.users.agents.dto;

import lombok.Data;

@Data
public class AgentSetupDto {

    private String username;
    private String password;
    private String deviceId;
    private String agentId;
    private String newPassword;
    private String pin;
    private String newPin;
    private String latitude;
    private String longitude;

}
