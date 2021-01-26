package com._3line.gravity.api.transaction.dto;


import lombok.Data;

@Data
public class CardRequestDTO {

    public String qty;
    public String bankName;
    public String agentPin;
    public String longitude ;
    public String latitude;
    public String deviceId;
    public String narration;
    public Double totalAmount;

}
