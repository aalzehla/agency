package com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageFlowInfo {

    @JsonProperty
    private Boolean allowRetry;

    @JsonProperty
    private String finishButtonName;

    @JsonProperty
    private Boolean performInquiry;

    @JsonProperty
    private String startPage;

    @JsonProperty
    private Boolean usesPaymentItems;

    public Boolean getAllowRetry() {
        return allowRetry;
    }

    public void setAllowRetry(Boolean allowRetry) {
        this.allowRetry = allowRetry;
    }

    public String getFinishButtonName() {
        return finishButtonName;
    }

    public void setFinishButtonName(String finishButtonName) {
        this.finishButtonName = finishButtonName;
    }

    public Boolean getPerformInquiry() {
        return performInquiry;
    }

    public void setPerformInquiry(Boolean performInquiry) {
        this.performInquiry = performInquiry;
    }

    public String getStartPage() {
        return startPage;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }

    public Boolean getUsesPaymentItems() {
        return usesPaymentItems;
    }

    public void setUsesPaymentItems(Boolean usesPaymentItems) {
        this.usesPaymentItems = usesPaymentItems;
    }

    @Override
    public String toString() {
        return "PageFlowInfo{" + "allowRetry=" + allowRetry + ", finishButtonName=" + finishButtonName + ", performInquiry=" + performInquiry + ", startPage=" + startPage + ", usesPaymentItems=" + usesPaymentItems + '}';
    }

}
