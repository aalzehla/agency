package com._3line.gravity.core.email.model;

import com._3line.gravity.core.entity.AbstractEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ToString
@Entity
public class Mail extends AbstractEntity {

    private String mailHeader ;

    @Column(nullable = false)
    private String mailTo ;

    @Lob
    private String mailContent;

    @ElementCollection
    private List<String> copy ;

    @ElementCollection
    private Map<String,String> attachements;

    private boolean sent = false ;

    @Lob
    private String failureReason ;

    private Date createdOn = new Date();

    private Date lastSent ;


    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getMailHeader() {
        return mailHeader;
    }

    public void setMailHeader(String mailHeader) {
        this.mailHeader = mailHeader;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public List<String> getCopy() {
        return copy;
    }

    public void setCopy(List<String> copy) {
        this.copy = copy;
    }

    public Map<String, String> getAttachements() {
        return attachements;
    }

    public void setAttachements(Map<String, String> attachements) {
        this.attachements = attachements;
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
}
