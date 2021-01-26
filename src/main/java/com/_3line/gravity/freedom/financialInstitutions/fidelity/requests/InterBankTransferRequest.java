package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;

public class InterBankTransferRequest extends BaseRequest {

    private String surc_acct;

    private String dest_account;

    private String dest_bankCode;

    private Double trans_amount;

    private Double charge_amount;

    private String narration;

    private String beneficiaryName;

    private Integer refno;

    private String payment_ref;

    private Integer productId;

    private String customerPin;

    public String getSurc_acct() {
        return surc_acct;
    }

    public void setSurc_acct(String surc_acct) {
        this.surc_acct = surc_acct;
    }

    public String getDest_account() {
        return dest_account;
    }

    public void setDest_account(String dest_account) {
        this.dest_account = dest_account;
    }

    public String getDest_bankCode() {
        return dest_bankCode;
    }

    public void setDest_bankCode(String dest_bankCode) {
        this.dest_bankCode = dest_bankCode;
    }

    public Double getTrans_amount() {
        return trans_amount;
    }

    public void setTrans_amount(Double trans_amount) {
        this.trans_amount = trans_amount;
    }

    public Double getCharge_amount() {
        return charge_amount;
    }

    public void setCharge_amount(Double charge_amount) {
        this.charge_amount = charge_amount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public Integer getRefno() {
        return refno;
    }

    public void setRefno(Integer refno) {
        this.refno = refno;
    }

    public String getPayment_ref() {
        return payment_ref;
    }

    public void setPayment_ref(String payment_ref) {
        this.payment_ref = payment_ref;
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
