package com._3line.gravity.freedom.wallet.dto;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WalletHistoryDto {
    String from ;
    String to ;
    int page;
    int size;

}
