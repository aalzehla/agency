package com._3line.gravity.freedom.billpayment.dto;

public class BillOptionViewDto {


    private String optionName;

    private String code;

    private String amount;

    private Boolean isFixedAmount;
    private BillServicesViewDto serviceOption;

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

    public BillServicesViewDto getServiceOption() {
        return serviceOption;
    }

    public void setServiceOption(BillServicesViewDto serviceOption) {
        this.serviceOption = serviceOption;
    }
}
