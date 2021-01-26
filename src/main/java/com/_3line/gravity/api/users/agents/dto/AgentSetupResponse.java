/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.api.users.agents.dto;

import com._3line.gravity.core.models.Response;
import lombok.Data;

@Data
public class AgentSetupResponse extends Response {

    private String agentId,
            clientId,
            secretKey,
            latitude,
            longitude,
            name,
            belongsto,
            companyName,
            deviceSetupStatus,
            pin,
            password,
            balnce,
            geofenceradius,
            accountNo,
            structureid,
            address;
    private boolean isFirstTime;


}
