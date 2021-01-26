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
public class ConfirmAccountCreationRequest extends BaseRequest {

    private Integer revalidationID;

    public Integer getRevalidationID() {
        return revalidationID;
    }

    public void setRevalidationID(Integer revalidationID) {
        this.revalidationID = revalidationID;
    }

}
