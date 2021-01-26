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
public class Detail {

    @JsonProperty("pstd_date")
    private String pstd_date;

    @JsonProperty("value_date")
    private String value_date;

    @JsonProperty("tran_id")
    private String tran_id;

    @JsonProperty("tran_sub_type")
    private String tran_sub_type;

    @JsonProperty("part_tran_type")
    private String part_tran_type;

    @JsonProperty("tran_amt")
    private String tran_amt;

    @JsonProperty("tran_particular")
    private String tran_particular;

    @JsonProperty("tran_rmks")
    private String tran_rmks;

    @JsonProperty("tran_crncy_code")
    private String tran_crncy_code;

    @JsonProperty("rate_code")
    private String rate_code;

    @JsonProperty("rate")
    private String rate;

    public String getPstd_date() {
        return pstd_date;
    }

    public void setPstd_date(String pstd_date) {
        this.pstd_date = pstd_date;
    }

    public String getValue_date() {
        return value_date;
    }

    public void setValue_date(String value_date) {
        this.value_date = value_date;
    }

    public String getTran_id() {
        return tran_id;
    }

    public void setTran_id(String tran_id) {
        this.tran_id = tran_id;
    }

    public String getTran_sub_type() {
        return tran_sub_type;
    }

    public void setTran_sub_type(String tran_sub_type) {
        this.tran_sub_type = tran_sub_type;
    }

    public String getPart_tran_type() {
        return part_tran_type;
    }

    public void setPart_tran_type(String part_tran_type) {
        this.part_tran_type = part_tran_type;
    }

    public String getTran_amt() {
        return tran_amt;
    }

    public void setTran_amt(String tran_amt) {
        this.tran_amt = tran_amt;
    }

    public String getTran_particular() {
        return tran_particular;
    }

    public void setTran_particular(String tran_particular) {
        this.tran_particular = tran_particular;
    }

    public String getTran_rmks() {
        return tran_rmks;
    }

    public void setTran_rmks(String tran_rmks) {
        this.tran_rmks = tran_rmks;
    }

    public String getTran_crncy_code() {
        return tran_crncy_code;
    }

    public void setTran_crncy_code(String tran_crncy_code) {
        this.tran_crncy_code = tran_crncy_code;
    }

    public String getRate_code() {
        return rate_code;
    }

    public void setRate_code(String rate_code) {
        this.rate_code = rate_code;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

}
