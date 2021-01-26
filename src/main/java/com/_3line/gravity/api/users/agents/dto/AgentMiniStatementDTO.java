package com._3line.gravity.api.users.agents.dto;

import com._3line.gravity.api.users.aggregator.dto.CurrentStats;
import com._3line.gravity.api.users.aggregator.dto.TotalDaysStats;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.wallet.models.FreedomWalletTransaction;
import lombok.Data;

import java.util.List;

@Data
public class AgentMiniStatementDTO {


    private String successfulDepositVol;
    private String successfulDepositVal;
    private String failedDepositVol;
    private String failedDepositVal;

    private String successfulWithVol;
    private String successfulWithVal;
    private String failedWithVol;
    private String failedWithVal;

    private String successfulBillPaymntVol;
    private String successfulBillPaymntVal;
    private String failedBillPaymntVol;
    private String failedBillPaymntVal;

    private String successfulRechargeVol;
    private String successfulRechargeVal;
    private String failedRechargeVol;
    private String failedRechargeVal;



    private String successfulAcctOpening;
}
