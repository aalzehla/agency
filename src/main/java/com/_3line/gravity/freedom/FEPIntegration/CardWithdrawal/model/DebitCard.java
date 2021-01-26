package com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.model;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.agents.models.Agents;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.jdo.annotations.Unique;
import javax.persistence.*;


@Entity
@Data
@Where(clause ="del_Flag='N'" )
public class DebitCard extends AbstractEntity  {

    private String token;
    private String cardType;
    private String expDate;
    @Column(unique = true)
    private String pan;
    private String meta;
    private String bank;
    @ManyToOne
    private Agents agent;


}
