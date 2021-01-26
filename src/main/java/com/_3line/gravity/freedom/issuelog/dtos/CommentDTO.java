package com._3line.gravity.freedom.issuelog.dtos;


import java.util.Date;

/**
 * Created by Sylvester on 16/10/2019.
 */
public class CommentDTO {

    private Long id;

    private int version;

    private String comment;

    private Date madeOn;

    private String username;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Date getMadeOn()
    {
        return madeOn;
    }

    public void setMadeOn(Date madeOn)
    {
        this.madeOn = madeOn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
