package com._3line.gravity.api.users.agents.dto;

import com._3line.gravity.api.users.aggregator.dto.AgentStats;
import com._3line.gravity.api.users.aggregator.dto.CurrentStats;
import com._3line.gravity.freedom.wallet.dto.WalletDTO;
import lombok.Data;

@Data
public class WalletStatsDTO {

   private WalletDTO incomeWalletDTO;
   private WalletDTO tradingWalletDTO;

}
