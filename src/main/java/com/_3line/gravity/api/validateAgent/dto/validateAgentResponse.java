package com._3line.gravity.api.validateAgent.dto;

import lombok.Data;

/**
 * @author JoshuaO
 */

@Data
public class validateAgentResponse {
    private String agentBusinessName;
    private String agentName;
    private String phoneNumber;
    private String city;
    private String state;
    private boolean verified = true;
}
