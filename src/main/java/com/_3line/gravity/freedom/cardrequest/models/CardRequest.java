package com._3line.gravity.freedom.cardrequest.models;

import com._3line.gravity.core.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
public class CardRequest  extends AbstractEntity {

    public String qty;
    public String bankName;
    public String agentName;
    public String agentPin;
    public String longitude ;
    public String latitude;
    public String deviceId;
    public String narration;
    public String status;
    public Date deliveredOn;
    public Double totalAmount;
}
