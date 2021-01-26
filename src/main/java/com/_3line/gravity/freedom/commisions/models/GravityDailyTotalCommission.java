package com._3line.gravity.freedom.commisions.models;



import com._3line.gravity.core.entity.AbstractEntity;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class GravityDailyTotalCommission extends AbstractEntity {


    @ElementCollection
    private Map<String, BigDecimal> agentTotalCommission = new HashMap<>();
    @ElementCollection
    private Map<String, BigDecimal> _3lineTotalCommission =  new HashMap<>();
    @ElementCollection
    private Map<String, BigDecimal> agentBankTotalCommission = new HashMap<>();
    private final Date dateComputed = new Date();
    @Embedded
    private DateRange dateRange;


    public Map<String, BigDecimal> getAgentTotalCommission() {
        return agentTotalCommission;
    }

    public void setAgentTotalCommission(Map<String, BigDecimal> agentTotalCommission) {
        this.agentTotalCommission = agentTotalCommission;
    }


    public Map<String, BigDecimal> get_3lineTotalCommission() {
        return _3lineTotalCommission;
    }

    public void set_3lineTotalCommission(Map<String, BigDecimal> _3lineTotalCommission) {
        this._3lineTotalCommission = _3lineTotalCommission;
    }

    public Map<String, BigDecimal> getAgentBankTotalCommission() {
        return agentBankTotalCommission;
    }

    public void setAgentBankTotalCommission(Map<String, BigDecimal> agentBankTotalCommission) {
        this.agentBankTotalCommission = agentBankTotalCommission;
    }

    public Date getDateComputed() {
        return dateComputed;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }
}
