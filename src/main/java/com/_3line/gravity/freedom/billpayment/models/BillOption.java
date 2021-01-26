package com._3line.gravity.freedom.billpayment.models;

import com._3line.gravity.core.entity.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author JoyU
 * @date 10/15/2018
 */
@Entity
public class BillOption extends AbstractEntity {
    private String optionName;
    private String code;
    private String amount;
    private Boolean isFixedAmount;
    @JsonIgnore
    @ManyToOne
    private BillServices serviceOption;


    public BillServices getServiceOption() {
        return serviceOption;
    }

    public void setServiceOption(BillServices serviceOption) {
        this.serviceOption = serviceOption;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Boolean getFixedAmount() {
        return isFixedAmount;
    }

    public void setFixedAmount(Boolean fixedAmount) {
        isFixedAmount = fixedAmount;
    }
}
