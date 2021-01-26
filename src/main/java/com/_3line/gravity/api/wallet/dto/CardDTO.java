package com._3line.gravity.api.wallet.dto;

import lombok.Data;

@Data
public class CardDTO {

    public String cardType;
    public String expDate;
    public String pan;
    public String bank;

}
