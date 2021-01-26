package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;

/**
 * Created by JohnA on 16-Jan-18.
 */
public class BillPaymentRequest extends BaseRequest {

    private Double amount;

    private String customerEmail;

    private String customerMobile;

    private String customerId;

    private String paymentCode;

    private String requestReference3D;

    private String productId;

    private String billerId;

    private String customerPin;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

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

    public String getRequestReference3D() {
        return requestReference3D;
    }

    public void setRequestReference3D(String requestReference3D) {
        this.requestReference3D = requestReference3D;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBillerId() {
        return billerId;
    }

    public void setBillerId(String billerId) {
        this.billerId = billerId;
    }

    public String getCustomerPin() {
        return customerPin;
    }

    public void setCustomerPin(String customerPin) {
        this.customerPin = customerPin;
    }

}
