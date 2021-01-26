package com._3line.gravity.freedom.institution.model;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.agents.models.Agents;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Data
@Where(clause = "del_flag='N'")
public class Institution extends AbstractEntity {

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

    @OneToOne
    private Agents institutionAgent;


}
