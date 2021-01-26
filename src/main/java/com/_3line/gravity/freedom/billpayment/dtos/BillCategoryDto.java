package com._3line.gravity.freedom.billpayment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.List;

/**
 * @author JoyU
 * @date 10/15/2018
 */
@ToString
public class BillCategoryDto {
    @JsonProperty("Name")
    private String categoryName;
    @JsonProperty("Services")
    private List<BillServicesDto> services;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<BillServicesDto> getServices() {
        return services;
    }

    public void setServices(List<BillServicesDto> services) {
        this.services = services;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }
}
