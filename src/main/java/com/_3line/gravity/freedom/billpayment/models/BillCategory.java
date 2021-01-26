package com._3line.gravity.freedom.billpayment.models;


import com._3line.gravity.core.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author JoyU
 * @date 10/15/2018
 */
/**
 * This is actually billServiceProvider
 */
@Entity
public class BillCategory extends AbstractEntity {
    private String categoryName;
    private String responseCode;
    private String responseDescription;
    @OneToMany(mappedBy = "serviceCategory")
    private List<BillServices> services;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public List<BillServices> getServices() {
        return services;
    }

    public void setServices(List<BillServices> services) {
        this.services = services;
    }
}
