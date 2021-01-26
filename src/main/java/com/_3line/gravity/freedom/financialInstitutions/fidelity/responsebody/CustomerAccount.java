package com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by JohnA on 17-Jan-18.
 */
public class CustomerAccount {

    @JsonProperty(value = "AccountNumber")
    private String AccountNumber;

    @JsonProperty(value = "AccountName")
    private String AccountName;

    @JsonProperty(value = "BVN")
    private String BVN;

    @JsonProperty(value = "isValid")
    private Boolean isValid;

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getBVN() {
        return BVN;
    }

    public void setBVN(String BVN) {
        this.BVN = BVN;
    }

    public Boolean isValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerAccount{");
        sb.append("AccountNumber='").append(AccountNumber).append('\'');
        sb.append(", AccountName='").append(AccountName).append('\'');
        sb.append(", BVN='").append(BVN).append('\'');
        sb.append(", isValid=").append(isValid);
        sb.append('}');
        return sb.toString();
    }
}
