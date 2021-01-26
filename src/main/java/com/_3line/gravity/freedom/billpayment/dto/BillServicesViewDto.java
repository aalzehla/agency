package com._3line.gravity.freedom.billpayment.dto;

import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;
import com._3line.gravity.freedom.billpayment.models.BillCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.ToString;

import java.util.List;

@ToString
public class BillServicesViewDto extends AbstractVerifiableDto {

    private String serviceName;
    private Boolean isRecharge;
    @JsonIgnore
    private List<BillOptionViewDto> options;
    private Long category ;


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

    public List<BillOptionViewDto> getOptions() {
        return options;
    }

    public void setOptions(List<BillOptionViewDto> options) {
        this.options = options;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }
}
