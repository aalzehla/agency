package com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.dto.sva;

import lombok.Data;

@Data
public class SendTransactionRespDTO {
    
    public ErrorsDTO error;
    
    public String responseCode;
    public String respCode;

    public String shortTransactionRef;
    
    public String responseDescription;
    
    public Object rechargePin;
    
    public String transactionRef;
    
    public String transactionResponseCode;
    
    public String transactionResponseDesc;
    
    public String responseCodeGrouping;
    
}
