package com._3line.gravity.freedom.billpayment.dtos;

import com._3line.gravity.freedom.billpayment.models.BillCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author JoyU
 * @date 10/15/2018
 */
@ToString
public class BillServicesDto{
    @JsonProperty("Name")
    private String serviceName;
    @JsonProperty("IsRecharge")
    private Boolean isRecharge;
    @JsonProperty("Options")
    private List<BillOptionDto> options;
    private BillCategory serviceCategory;

    public BillCategory getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(BillCategory serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Boolean getRecharge() {
        return isRecharge;
    }

    public void setRecharge(Boolean recharge) {
        isRecharge = recharge;
    }

    public List<BillOptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<BillOptionDto> options) {
        this.options = options;
    }

}
