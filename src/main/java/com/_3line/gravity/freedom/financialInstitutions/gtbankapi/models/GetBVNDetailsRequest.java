package com._3line.gravity.freedom.financialInstitutions.gtbankapi.models;

/**
 * @author JoyU
 * @date 1/9/2019
 */
public class GetBVNDetailsRequest {

    private String bvn;
    private String channel;
    private String userID;

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
