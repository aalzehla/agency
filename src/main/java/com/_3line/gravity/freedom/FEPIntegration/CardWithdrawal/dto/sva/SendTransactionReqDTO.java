package com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.dto.sva;

import lombok.Data;

@Data
public class SendTransactionReqDTO {

    public Double amount;
    public String bankId;
    public String cardBin;
    public String customerEmail;
    public String customerId;
    public String customerMobile;
    public String cvv;
    public String destinationAccountNumber;
    public String destinationAccountType;
    public String expDate;
    public String msisdn;
    public String pan;
    public String paymentCode;
    public String pin;
    public String reciepientName;
    public String reciepientPhone;
    public String transactionRef;

}
