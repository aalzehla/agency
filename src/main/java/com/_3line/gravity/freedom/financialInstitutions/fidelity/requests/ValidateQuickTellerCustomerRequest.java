package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;


import com._3line.gravity.freedom.financialInstitutions.fidelity.models.Customer;

/**
 * Created by JohnA on 16-Jan-18.
 */
public class ValidateQuickTellerCustomerRequest {

    private Customer customers;
    private String terminalId;

    public Customer getCustomers() {
        return customers;
    }

    public void setCustomers(Customer customers) {
        this.customers = customers;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ValidateQuickTellerCustomerRequest{");
        sb.append("customers=").append(customers);
        sb.append(", terminalId='").append(terminalId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
