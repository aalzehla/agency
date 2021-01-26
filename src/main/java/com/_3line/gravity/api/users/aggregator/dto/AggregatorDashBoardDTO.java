package com._3line.gravity.api.users.aggregator.dto;

import com._3line.gravity.freedom.reports.dtos.AgentPerformance;
import lombok.Data;

import java.util.List;

@Data
public class AggregatorDashBoardDTO {

    private TotalDaysStats totalDaysStats;
    private CurrentStats currentStats;
    private List<AgentPerformance> agentPerformances;


}
