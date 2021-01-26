package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BillerListResponse {

    @JsonProperty
    private int Count;
    @JsonProperty
    private List<Category> category;
    @JsonProperty
    private String ResponseCode;

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BillerListResponse{");
        sb.append("Count=").append(Count);
        sb.append(", category=").append(category);
        sb.append(", ResponseCode='").append(ResponseCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
