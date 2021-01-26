package com._3line.gravity.freedom.billpayment.dto;


import lombok.ToString;

import java.util.List;

@ToString
public class BillCategoryViewDto {


    private String categoryName;
    private List<BillServicesViewDto> services;
    private String responseCode;
    private String responseDescription;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<BillServicesViewDto> getServices() {
        return services;
    }

    public void setServices(List<BillServicesViewDto> services) {
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
