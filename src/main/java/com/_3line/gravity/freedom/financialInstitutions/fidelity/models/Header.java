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
public class Header {

    @JsonProperty("acct_nr")
    private String acct_nr;

    @JsonProperty("start_date")
    private String start_date;

    @JsonProperty("end_date")
    private String end_date;

    @JsonProperty("opening_balance")
    private String opening_balance;

    @JsonProperty("closing_balance")
    private String closing_balance;

    public String getAcct_nr() {
        return acct_nr;
    }

    public void setAcct_nr(String acct_nr) {
        this.acct_nr = acct_nr;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getOpening_balance() {
        return opening_balance;
    }

    public void setOpening_balance(String opening_balance) {
        this.opening_balance = opening_balance;
    }

    public String getClosing_balance() {
        return closing_balance;
    }

    public void setClosing_balance(String closing_balance) {
        this.closing_balance = closing_balance;
    }

}
