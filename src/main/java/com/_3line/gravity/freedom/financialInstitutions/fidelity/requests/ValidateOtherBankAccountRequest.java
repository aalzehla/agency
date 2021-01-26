package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;

/**
 * Created by JohnA on 17-Jan-18.
 */
public class ValidateOtherBankAccountRequest extends BaseRequest {

    private String otherBankCode;

    private String accountNo;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getOtherBankCode() {
        return otherBankCode;
    }

    public void setOtherBankCode(String otherBankCode) {
        this.otherBankCode = otherBankCode;
    }

}
