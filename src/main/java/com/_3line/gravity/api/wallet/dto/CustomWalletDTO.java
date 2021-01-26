package com._3line.gravity.api.wallet.dto;

import com._3line.gravity.freedom.wallet.dto.WalletDTO;
import lombok.Data;

@Data
public class CustomWalletDTO {

    private String availableBalance;
    private String ledgerBalance;
    private String id;
    private String walletNumber ;
    private String lastTran ;
    private String openingDate ;
    private String purpose ;

}
