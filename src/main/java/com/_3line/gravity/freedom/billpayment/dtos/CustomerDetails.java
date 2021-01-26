package com._3line.gravity.freedom.billpayment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

/**
 * @author JoyU
 * @date 10/15/2018
 */

@ToString
public class CustomerDetails {
    @JsonProperty("Fullname")
    private String fullname;
    @JsonProperty("MobilePhone")
    private String mobilePhone;
    @JsonProperty("Email")
    private String email;

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
