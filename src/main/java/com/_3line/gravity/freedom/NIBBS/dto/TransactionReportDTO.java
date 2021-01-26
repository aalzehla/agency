package com._3line.gravity.freedom.NIBBS.dto;

import lombok.Data;

@Data
public class TransactionReportDTO {

    private String transactionDate;
    private String cashInCount;
    private String cashInValue;
    private String cashOutCount;
    private String cashOutValue;
    private String accountOpeningCount;
    private String accountOpeningValue;
    private String billsPaymentCount;
    private String billsPaymentValue;
    private String airtimeRechargeCount;
    private String airtimeRechargeValue;
    private String fundTransferCount;
    private String fundTransferValue;
    private String bvnEnrollmentCount;
    private String bvnEnrollmentValue;
    private String othersCount;
    private String othersValue;
    private String additionalService1Count;
    private String additionalService1Value;
    private String additionalService2Count;
    private String additionalService2Value;
    private String agentCode;


}
