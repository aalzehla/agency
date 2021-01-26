/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author NiyiO
 */
public class BillerList {

    @JsonProperty("billers")
    private Biller[] billers;

    public Biller[] getBillers() {
        return billers;
    }

    public void setBillers(Biller[] billers) {
        this.billers = billers;
    }

}
