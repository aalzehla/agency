package com._3line.gravity.freedom.financialInstitutions.magtipon.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MagtiponCustomerDetails {
    @JsonProperty("Fullname")
    String fullname;
    @JsonProperty("MobilePhone")
    String mobilePhone;
    @JsonProperty("Email")
    String email = "test@email.com";


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
