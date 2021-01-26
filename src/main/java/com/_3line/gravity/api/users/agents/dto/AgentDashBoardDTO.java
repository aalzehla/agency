package com._3line.gravity.api.users.agents.dto;

import com._3line.gravity.api.users.aggregator.dto.CurrentStats;
import com._3line.gravity.api.users.aggregator.dto.TotalDaysStats;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.wallet.models.FreedomWalletTransaction;
import lombok.Data;

import java.util.List;

@Data
public class AgentDashBoardDTO {

    private List<FreedomWalletTransaction> transactions;
    private TotalDaysStats totalDaysStats;
    private CurrentStats currentStats;
    private WalletStatsDTO walletStatsDTO;
    private Agents agent;
}
