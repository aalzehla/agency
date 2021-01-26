package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;

/**
 * Created by JohnA on 16-Jan-18.
 */
public class CardRequest extends BaseRequest {

    private Integer CardTypeID;

    private String AccountNumber;

    private String CustomerFirstName;

    private String CustomerSurname;

    private String CustomerOtherNames;

    private Integer productId;

    private String customerPin;

    public Integer getCardTypeID() {
        return CardTypeID;
    }

    public void setCardTypeID(Integer cardTypeID) {
        CardTypeID = cardTypeID;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getCustomerFirstName() {
        return CustomerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        CustomerFirstName = customerFirstName;
    }

    public String getCustomerSurname() {
        return CustomerSurname;
    }

    public void setCustomerSurname(String customerSurnameName) {
        CustomerSurname = customerSurnameName;
    }

    public String getCustomerOtherNames() {
        return CustomerOtherNames;
    }

    public void setCustomerOtherNames(String customerOtherNames) {
        CustomerOtherNames = customerOtherNames;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getCustomerPin() {
        return customerPin;
    }

    public void setCustomerPin(String customerPin) {
        this.customerPin = customerPin;
    }

}
