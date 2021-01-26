package com._3line.gravity.freedom.billpayment.models;

import com._3line.gravity.core.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author JoyU
 * @date 10/15/2018
 */
@ToString
@Entity
public class BillServices extends AbstractEntity {

    private String serviceName;
    private Boolean recharge;
    @OneToMany(mappedBy = "serviceOption")
    private List<BillOption> options;
    @JsonIgnore
    @ManyToOne
    private BillCategory serviceCategory;

    private Long category ;


    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Boolean getRecharge() {
        return recharge;
    }

    public void setRecharge(Boolean recharge) {
        this.recharge = recharge;
    }

    public List<BillOption> getOptions() {
        return options;
    }

    public void setOptions(List<BillOption> options) {
        this.options = options;
    }

    public BillCategory getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(BillCategory serviceCategory) {
        this.serviceCategory = serviceCategory;
    }
}
