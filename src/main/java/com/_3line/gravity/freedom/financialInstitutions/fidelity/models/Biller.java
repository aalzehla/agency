/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody.PageFlowInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author NiyiO
 */
public class Biller {

    @JsonProperty
    private String categoryid;

    @JsonProperty
    private String categoryname;

    @JsonProperty
    private String categorydescription;

    @JsonProperty
    private String billerid;

    @JsonProperty
    private String billername;

    @JsonProperty
    private String customerfield1;

    @JsonProperty
    private String customerfield2;

    @JsonProperty
    private String supportemail;

    @JsonProperty
    private String paydirectProductId;

    @JsonProperty
    private String paydirectInstitutionId;

    @JsonProperty
    private String narration;

    @JsonProperty
    private String shortName;

    @JsonProperty
    private String surcharge;

    @JsonProperty
    private String currencyCode;

    @JsonProperty
    private String quickTellerSiteUrlName;

    @JsonProperty
    private String riskCategoryId;

    @JsonProperty
    private String amountType;

    @JsonProperty
    private PageFlowInfo pageFlowInfo;

    @JsonProperty
    private String currencySymbol;

    @JsonProperty
    private String customMessageUrl;

    @JsonProperty
    private String customSectionUrl;

    @JsonProperty
    private String logoUrl;

    @JsonProperty
    private String networkId;

    @JsonProperty
    private String productCode;

    @JsonProperty
    private String type;

    @JsonProperty
    private String url;

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCategorydescription() {
        return categorydescription;
    }

    public void setCategorydescription(String categorydescription) {
        this.categorydescription = categorydescription;
    }

    public String getBillerid() {
        return billerid;
    }

    public void setBillerid(String billerid) {
        this.billerid = billerid;
    }

    public String getBillername() {
        return billername;
    }

    public void setBillername(String billername) {
        this.billername = billername;
    }

    public String getCustomerfield1() {
        return customerfield1;
    }

    public void setCustomerfield1(String customerfield1) {
        this.customerfield1 = customerfield1;
    }

    public String getCustomerfield2() {
        return customerfield2;
    }

    public void setCustomerfield2(String customerfield2) {
        this.customerfield2 = customerfield2;
    }

    public String getSupportemail() {
        return supportemail;
    }

    public void setSupportemail(String supportemail) {
        this.supportemail = supportemail;
    }

    public String getPaydirectProductId() {
        return paydirectProductId;
    }

    public void setPaydirectProductId(String paydirectProductId) {
        this.paydirectProductId = paydirectProductId;
    }

    public String getPaydirectInstitutionId() {
        return paydirectInstitutionId;
    }

    public void setPaydirectInstitutionId(String paydirectInstitutionId) {
        this.paydirectInstitutionId = paydirectInstitutionId;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(String surcharge) {
        this.surcharge = surcharge;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getQuickTellerSiteUrlName() {
        return quickTellerSiteUrlName;
    }

    public void setQuickTellerSiteUrlName(String quickTellerSiteUrlName) {
        this.quickTellerSiteUrlName = quickTellerSiteUrlName;
    }

    public String getRiskCategoryId() {
        return riskCategoryId;
    }

    public void setRiskCategoryId(String riskCategoryId) {
        this.riskCategoryId = riskCategoryId;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public PageFlowInfo getPageFlowInfo() {
        return pageFlowInfo;
    }

    public void setPageFlowInfo(PageFlowInfo pageFlowInfo) {
        this.pageFlowInfo = pageFlowInfo;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCustomMessageUrl() {
        return customMessageUrl;
    }

    public void setCustomMessageUrl(String customMessageUrl) {
        this.customMessageUrl = customMessageUrl;
    }

    public String getCustomSectionUrl() {
        return customSectionUrl;
    }

    public void setCustomSectionUrl(String customSectionUrl) {
        this.customSectionUrl = customSectionUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
