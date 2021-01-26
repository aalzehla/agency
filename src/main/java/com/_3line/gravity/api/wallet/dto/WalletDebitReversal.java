package com._3line.gravity.api.wallet.dto;


import lombok.Data;

@Data
public class WalletDebitReversal {

    String ptspId ;
    String walletNumber ;
    String channel ;
    Double amount;
    String remark ;

}
