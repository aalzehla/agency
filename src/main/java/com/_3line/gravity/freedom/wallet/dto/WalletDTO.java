package com._3line.gravity.freedom.wallet.dto;

import lombok.Data;

@Data
public class WalletDTO {

    Long id;

    String walletNumber ;

    Double availableBalance ;

    Double ledgerBalance  ;

    String  lastTran ;

    String openingDate ;

    String purpose ;

}
