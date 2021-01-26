package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author NiyiO
 */
public class BillersListResponse extends BaseResponse {

    public BillersListResponse() {

    }

    @JsonProperty("billerList")
    private BillerList billerList;

    public BillerList getBillerList() {
        return billerList;
    }

    public void setBillerList(BillerList billerList) {
        this.billerList = billerList;
    }

}
