package com._3line.gravity.freedom.wallet.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
public class WalletTransferDTO {

    @Min(value = 0,message = "value must not be less than zero (0)")
    Double amount ;
    String pin ;
    String fromWallet ;
    String toWallet ;
    String remark ;
    String requestId ;
}
