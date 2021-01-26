package com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody;

public class CardRequest {

    private String CardTypeID;
    private String AccountNumber;
    private String CustomerFirstName;
    private String CustomerSurnameName;
    private String CustomerOtherNames;

    public String getCardTypeID() {
        return CardTypeID;
    }

    public void setCardTypeID(String cardTypeID) {
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

    public String getCustomerSurnameName() {
        return CustomerSurnameName;
    }

    public void setCustomerSurnameName(String customerSurnameName) {
        CustomerSurnameName = customerSurnameName;
    }

    public String getCustomerOtherNames() {
        return CustomerOtherNames;
    }

    public void setCustomerOtherNames(String customerOtherNames) {
        CustomerOtherNames = customerOtherNames;
    }
}
