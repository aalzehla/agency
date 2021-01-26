package com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody;

/**
 * Created by JohnA on 16-Jan-18.
 */
public class Customer {

    private String customerId;
    private String paymentCode;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Customer{");
        sb.append("customerId='").append(customerId).append('\'');
        sb.append(", paymentCode='").append(paymentCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
