/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.api.users.aggregator.dto;


import com._3line.gravity.freedom.agents.models.Agents;
import lombok.Data;

import java.util.List;

@Data
public class AgentStats {

    private List<Agents> agentsList;

}
