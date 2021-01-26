/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.fidelity.requests;

/**
 *
 * @author NiyiO
 */
public class ValidateAccountByNibssBvnRequest extends BaseRequest {

    private String bvn_id;

    private String account_nr;

    private String dob;

    public String getBvn_id() {
        return bvn_id;
    }

    public void setBvn_id(String bvn_id) {
        this.bvn_id = bvn_id;
    }

    public String getAccount_nr() {
        return account_nr;
    }

    public void setAccount_nr(String account_nr) {
        this.account_nr = account_nr;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

}
