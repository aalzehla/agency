package com._3line.gravity.freedom.bankdetails.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;


@Data
@Entity
@Table(name = "bankdetails")
public class BankDetails {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String bankName;
    @Column(unique = true)
    private String bankCode;
    @Column(unique = true)
    private String magtiponCode;
    @Column(unique = true)
    private String opayCode;
    @Column(unique = true)
    private String cbnCode;

    private String cardBinCode;

    private String _3lineAccount;

    private String comissionPercentage ;

    private String thriftCommissionPerc ;

    private String acquirePercentage;

//    @JsonIgnore
//    @OneToMany( mappedBy = "bankDetails", cascade = CascadeType.ALL , fetch = FetchType.EAGER)
//    Collection<FreedomCommision> commissions ;

    @Lob
    private String parameter;

    private Boolean isIntegrated;
    private String cardRequestFee;
    private String cardRequestCharge;
    private String cardRequestGlAcct;


    private String sanefBankCode;

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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

//    public Collection<FreedomCommision> getCommissions() {
//        return commissions;
//    }
//
//    public void setCommissions(Collection<FreedomCommision> commissions) {
//        this.commissions = commissions;
//    }

    public String get_3lineAccount() {
        return _3lineAccount;
    }

    public void set_3lineAccount(String _3lineAccount) {
        this._3lineAccount = _3lineAccount;
    }

    public String getSanefBankCode() {
        return sanefBankCode;
    }

    public void setSanefBankCode(String sanefBankCode) {
        this.sanefBankCode = sanefBankCode;
    }

    public String getThriftCommissionPerc() {
        return thriftCommissionPerc;
    }

    public void setThriftCommissionPerc(String thriftCommissionPerc) {
        this.thriftCommissionPerc = thriftCommissionPerc;
    }
}
