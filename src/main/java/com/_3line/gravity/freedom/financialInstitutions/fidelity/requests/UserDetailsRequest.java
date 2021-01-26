package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;

public class UserDetailsRequest {

    private String AccountNumber;

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDetailsRequest{");
        sb.append("AccountNumber='").append(AccountNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
