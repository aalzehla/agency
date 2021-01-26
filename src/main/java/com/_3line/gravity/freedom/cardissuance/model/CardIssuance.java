package com._3line.gravity.freedom.cardissuance.model;

import com._3line.gravity.core.entity.AbstractEntity;
import com._3line.gravity.freedom.agents.models.Agents;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Arrays;
import java.util.List;

/**
 * @author FortunatusE
 * @date 12/10/2018
 */

@Data
@Entity
public class CardIssuance extends AbstractEntity {

    @ManyToOne
    private Agents agent;
    private String cardSerial;
    private String accountNumber;
    private String cardType;
    private String createdBy;
    private boolean issued;
    private boolean linked;
    private String statusMessage;
    private String statusCode;
    private String header;


    @JsonIgnore
    @Override
    public List<String> getDefaultSearchFields(){
        return Arrays.asList("cardSerial", "accountNumber", "cardType", "createdBy");
    }

}
