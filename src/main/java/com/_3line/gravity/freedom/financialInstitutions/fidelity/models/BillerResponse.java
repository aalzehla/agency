package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BillerResponse {

    @JsonProperty
    private BillerListResponse billerList;
    @JsonProperty
    private List<Category> category;
    @JsonProperty
    private String ResponseCode;

    public BillerListResponse getBillerList() {
        return billerList;
    }

    public void setBillerList(BillerListResponse billerList) {
        this.billerList = billerList;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }
}
