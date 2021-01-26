package com._3line.gravity.freedom.wallet.dto;

import lombok.Data;

@Data
public class MagtiponCreditWalletRequest {

    Double amount;
    String description;
    String originatorAccountName;
    String originatorAccountNumber;
    String requestId;
    String tranType;
    String walletNumber;
}
