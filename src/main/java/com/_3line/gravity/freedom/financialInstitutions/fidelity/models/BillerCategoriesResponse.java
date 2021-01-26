package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BillerCategoriesResponse extends BaseResponse {

    public BillerCategoriesResponse() {

    }

    @JsonProperty("billerCategoryService")
    private BillerCategoryService billerCategoryService;

    public BillerCategoryService getBillerCategoryService() {
        return billerCategoryService;
    }

    public void setBillerCategoryService(BillerCategoryService billerCategoryService) {
        this.billerCategoryService = billerCategoryService;
    }

}
