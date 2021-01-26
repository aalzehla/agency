package com._3line.gravity.freedom.billpayment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author JoyU
 * @date 10/15/2018
 */
public class BillOptionDto {
    @JsonProperty("Name")
    private String optionName;
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Amount")
    private String amount;
    @JsonProperty("IsFixedAmount")
    private Boolean isFixedAmount;
    private BillServicesDto serviceOption;

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

    public BillServicesDto getServiceOption() {
        return serviceOption;
    }

    public void setServiceOption(BillServicesDto serviceOption) {
        this.serviceOption = serviceOption;
    }
}
