package com._3line.gravity.freedom.issuelog.models;

import com._3line.gravity.core.entity.AbstractEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by sylvester on 16/10/2019.
 */
@Entity
@Where(clause ="del_Flag='N'" )
public class Comments extends AbstractEntity {

    @Column(name = "remarks")
    private String comment;

    private Date madeOn;

    private String username;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getMadeOn() {
        return madeOn;
    }

    public void setMadeOn(Date madeOn) {
        this.madeOn = madeOn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
