package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;

public class LeanAccountRequest extends BaseRequest {

    private String account_no;

    private String lien_amount;

    private Integer tran_ref;

    private String lien_reason_code;

    private String lien_narration;

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getLien_amount() {
        return lien_amount;
    }

    public void setLien_amount(String lien_amount) {
        this.lien_amount = lien_amount;
    }

    public Integer getTran_ref() {
        return tran_ref;
    }

    public void setTran_ref(Integer tran_ref) {
        this.tran_ref = tran_ref;
    }

    public String getLien_reason_code() {
        return lien_reason_code;
    }

    public void setLien_reason_code(String lien_reason_code) {
        this.lien_reason_code = lien_reason_code;
    }

    public String getLien_narration() {
        return lien_narration;
    }

    public void setLien_narration(String lien_narration) {
        this.lien_narration = lien_narration;
    }

}
