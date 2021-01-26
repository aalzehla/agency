package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response;


import com._3line.gravity.freedom.financialInstitutions.fidelity.models.BaseResponse;
import com._3line.gravity.freedom.financialInstitutions.fidelity.models.UserDetails;

public class StanbicUserDetailsResonse extends BaseResponse {

    private UserDetails userDetails;

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}
