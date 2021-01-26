package com._3line.gravity.freedom.dashboard.models;

import lombok.ToString;

@ToString
public class TopAgent {

    private String name ;
    private String type ;
    private Long agentId ;
    private String totalTran ;
    private String image ;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getTotalTran() {
        return totalTran;
    }

    public void setTotalTran(String totalTran) {
        this.totalTran = totalTran;
    }
}
