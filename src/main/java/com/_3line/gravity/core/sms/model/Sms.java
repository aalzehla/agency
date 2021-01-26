package com._3line.gravity.core.sms.model;

import com._3line.gravity.core.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
public class Sms extends AbstractEntity {

    private String sendToPhone ;

    private String content ;

    private Date createdOn;

    private Date lastSent ;

    public boolean isSent ;


    public String failureReason ;


    public String getSendToPhone() {
        return sendToPhone;
    }

    public void setSendToPhone(String sendToPhone) {
        this.sendToPhone = sendToPhone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastSent() {
        return lastSent;
    }

    public void setLastSent(Date lastSent) {
        this.lastSent = lastSent;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
