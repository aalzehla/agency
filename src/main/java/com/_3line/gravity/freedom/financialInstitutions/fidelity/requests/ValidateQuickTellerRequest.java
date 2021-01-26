package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;


import com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody.Customer;

public class ValidateQuickTellerRequest {

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
}
