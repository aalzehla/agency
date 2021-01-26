/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.fidelity.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 *
 * @author NiyiO
 */
public class GetAccountResponse extends BaseResponse {

    @JsonProperty("AccountSummaryProfile")
    private AccountSummary[] accountSummaryProfile;

    public AccountSummary[] getAccountSummaryProfile() {
        return accountSummaryProfile;
    }

    public void setAccountSummaryProfile(AccountSummary[] accountSummaryProfile) {
        this.accountSummaryProfile = accountSummaryProfile;
    }

    @Override
    public String toString() {

        JsonObject jo = new JsonObject();

        JsonArray ja = new JsonArray();
        for (int i = 0; i < accountSummaryProfile.length; i++) {
            AccountSummary accountSummary = accountSummaryProfile[i];
            JsonObject jo1 = new JsonObject();
            jo1.addProperty("AccountNumber", accountSummary.getAccountNumber());
            jo1.addProperty("AccountName", accountSummary.getAccountName());
            ja.add(jo1);
        }

        jo.add("AccountSummaryProfile", ja);
        jo.addProperty("ResponseCode", getResponseCode());
        jo.addProperty("Description", getDescription());

        return jo.toString();
    }

}
