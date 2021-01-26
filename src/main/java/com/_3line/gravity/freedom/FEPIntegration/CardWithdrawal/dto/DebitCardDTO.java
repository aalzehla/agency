package com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.dto;

import com._3line.gravity.api.wallet.dto.CardDTO;
import lombok.Data;

@Data
public class DebitCardDTO {

    private String token;
    private String cardType;
    private String expDate;
    private String pan;
    private String meta;
    private String bank;
    private String tranId;

    public DebitCardDTO(CardDTO cardDTO) {
        this.cardType = cardDTO.getCardType();
        this.expDate = cardDTO.getExpDate();
        this.pan = cardDTO.getPan();
        this.bank = cardDTO.getBank();
    }

    public DebitCardDTO() {
    }
}
