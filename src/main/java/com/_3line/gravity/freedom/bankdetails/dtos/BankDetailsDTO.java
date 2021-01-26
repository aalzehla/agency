package com._3line.gravity.freedom.bankdetails.dtos;

import com._3line.gravity.core.verification.dtos.AbstractVerifiableDto;

import java.util.List;

public class BankDetailsDTO extends AbstractVerifiableDto {


    private String bankName;
    private String bankCode;
    private String magtiponCode;
    private String opayCode;
    private String cbnCode;
    private String _3lineAccount;
    List<FreedomCommisionDTO> commissions ;
    private String parameter;
    private Boolean isIntegrated;
    private String comissionPercentage ;
    private String acquirePercentage;

    public String getAcquirePercentage() {
        return acquirePercentage;
    }

    public void setAcquirePercentage(String acquirePercentage) {
        this.acquirePercentage = acquirePercentage;
    }

    public String getComissionPercentage() {
        return comissionPercentage;
    }

    public void setComissionPercentage(String comissionPercentage) {
        this.comissionPercentage = comissionPercentage;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getMagtiponCode() {
        return magtiponCode;
    }

    public void setMagtiponCode(String magtiponCode) {
        this.magtiponCode = magtiponCode;
    }

    public String getOpayCode() {
        return opayCode;
    }

    public void setOpayCode(String opayCode) {
        this.opayCode = opayCode;
    }

    public String getCbnCode() {
        return cbnCode;
    }

    public void setCbnCode(String cbnCode) {
        this.cbnCode = cbnCode;
    }

    public String get_3lineAccount() {
        return _3lineAccount;
    }

    public void set_3lineAccount(String _3lineAccount) {
        this._3lineAccount = _3lineAccount;
    }

    public List<FreedomCommisionDTO> getCommissions() {
        return commissions;
    }

    public void setCommissions(List<FreedomCommisionDTO> commissions) {
        this.commissions = commissions;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Boolean getIntegrated() {
        return isIntegrated;
    }

    public void setIntegrated(Boolean integrated) {
        isIntegrated = integrated;
    }
}
