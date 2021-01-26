package com._3line.gravity.freedom.accountmgt.dtos.walletopening;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

/**
 * @author JoyU
 * @date 11/14/2018
 */

@ToString
public class WalletAccountOpeningResponse{

    @JsonProperty("Remark")
    private  String remark;
    @JsonProperty("RespMsg")
    private String responseMessage;
    @JsonProperty("TransStatus")
    private String responseCode;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
