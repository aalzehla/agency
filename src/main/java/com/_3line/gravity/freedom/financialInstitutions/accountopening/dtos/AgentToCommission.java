package com._3line.gravity.freedom.financialInstitutions.accountopening.dtos;


import lombok.ToString;

@ToString
public class AgentToCommission {

    private Long id ;
    private String agentName ;
    private String noOfAccounts ;
    private String commission ;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getNoOfAccounts() {
        return noOfAccounts;
    }

    public void setNoOfAccounts(String noOfAccounts) {
        this.noOfAccounts = noOfAccounts;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }
}
