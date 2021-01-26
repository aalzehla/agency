package com._3line.gravity.freedom.institution.dto;

import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;
import lombok.Data;

@Data
public class InstitutionDTO extends AbstractVerifiableDto {

    private String del_flag;
    private String name;
    private Double agent_withdrawal_commission;
    private Double aggregator_withdrawal_commission;
    private Double agent_deposit_commission;
    private Double aggregator_deposit_commission;
    private Double line_deposit_commission;


    private Double agent_recharge_commission;
    private Double aggregator_recharge_commission;
    private Double line_recharge_commission;
    private Double agent_bill_commission;
    private Double aggregator_bill_commission;
    private Double line_bill_commission;

    private String agentUsername;


}
