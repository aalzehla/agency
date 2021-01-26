package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.services;


import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.request.StanbicAgentCashInRequest;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.request.StanbicAgentCashOutRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StanbicSOAP {

    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");


    public static String getAccountEnquiryPayload(String acctNum) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soap.request.manager.redbox.stanbic.com/\">\n" +
                "<soapenv:Header/>\n" +
                "<soapenv:Body>\n" +
                "<soap:request>\n" +
                "<channel>MOBILE_APP</channel>\n" +
                "<type>FETCH_CUSTOMER</type>\n" +
                "<customerId>"+acctNum+"</customerId>\n" +
                "<customerIdType>ACCOUNT_NUMBER</customerIdType>\n" +
                "<body><![CDATA[\n" +
                "<otherRequestDetails>\n" +
                "\n" +
                "</otherRequestDetails>]]>\n" +
                "</body></soap:request>\n" +
                "</soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public static String getAccountBalancePayload(String acctNum){
        return "";
    }

    public static String getOTPGenerationPayload(String acctNum){
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soap.request.manager.redbox.stanbic.com/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <soap:request>\n" +
                "         <!--Optional:-->\n" +
                "         <channel>MOBILE_APP</channel>\n" +
                "         <type>OTP_REQUEST</type>\n" +
                "         <customerId>"+acctNum+"</customerId>\n" +
                "         <customerIdType>ACCOUNT_NUMBER</customerIdType><!--OPTIONS - CIF_ID,ACCOUNT_NUMBER ,EBANK_LOGIN_ID,PHONE_NUMBER,BVN-->\n" +
                "         <body>perform request</body>\n" +
                "         <submissionTime>"+dateFormat.format(new Date())+"</submissionTime>\n" +
                "         <!--Optional:-->\n" +
                "      </soap:request>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    public static String getperformAgentCashOutPayload (StanbicAgentCashOutRequest transferRequest){
        return "<soapenv:Envelope \n" +
                "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" +
                "xmlns:soap=\"http://soap.request.manager.redbox.stanbic.com/\">\n" +
                "<soapenv:Header/>\n" +
                "<soapenv:Body>\n" +
                "<soap:request>\n" +
                "<channel>AGENT_NAME</channel><!-- AGENT SOURCE IS MODULE_ID-->\n" +
                "<type>AGENT_CASH_OUT</type>\n" +
                "<customerId>"+transferRequest.getSourceAccount()+"</customerId>\n" +
                "<customerIdType>ACCOUNT_NUMBER</customerIdType>\n" +
                "<submissionTime>"+dateFormat.format(new Date())+"</submissionTime>\n" +
                "<reqTranId>"+System.currentTimeMillis()+"</reqTranId>\n" +
                "<body><![CDATA[\n" +
                "      <otherRequestDetails>\n" +
                "         <sourceAccountNo>"+transferRequest.getSourceAccount()+"</sourceAccountNo>\n" +
                "         <destinationAccountNo>"+transferRequest.getDestinationAccount()+"</destinationAccountNo>\n" +
                "         <amount>"+transferRequest.getAmount()+"</amount>\n" +
                " <secret>"+transferRequest.getCustomerPin()+"</secret>\n" +
                " <passCode>"+transferRequest.getOtp()+"</passCode>\n" +
                "         <beneficiaryReference>TRANSFER-NARRATION-VIA-QT</beneficiaryReference>\n" +
                "         <customerReference>TRANSFER-NARRATION-VIA-QT</customerReference>\n" +
                "      </otherRequestDetails>\n" +
                "]]></body>\n" +
                "</soap:request>\n" +
                "</soapenv:Body>\n" +
                "</soapenv:Envelope>" ;
    }

    public static String getAgentCashInPayload(StanbicAgentCashInRequest stanbicAgentCashInRequest){
        return "<soapenv:Envelope \n" +
                "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" +
                "xmlns:soap=\"http://soap.request.manager.redbox.stanbic.com/\">\n" +
                "<soapenv:Header/>\n" +
                "<soapenv:Body>\n" +
                "<soap:request>\n" +
                "<channel>AGENT_NAME</channel><!-- AGENT SOURCE IS MODULE_ID-->\n" +
                "<type>AGENT_CASH_IN</type>\n" +
                "<customerId>"+stanbicAgentCashInRequest.getDestinationAccount()+"</customerId>\n" +
                "<customerIdType>ACCOUNT_NUMBER</customerIdType>\n" +
                "<submissionTime>"+dateFormat.format(new Date())+"</submissionTime>\n" +
                "<reqTranId>"+System.currentTimeMillis()+"</reqTranId>\n" +
                "<body><![CDATA[\n" +
                "      <otherRequestDetails>\n" +
                "         <destinationAccountNo>"+stanbicAgentCashInRequest.getDestinationAccount()+"</destinationAccountNo>\n" +
                "         <destinationBankCode>221</destinationBankCode>\n" +
                "         <transferMedium>0</transferMedium><!--0 FOR STANBIC, 1 FOR NIBSS AND 2 FOR QUICKTELLER TRANSFERS -->\n" +
                "         <sourceAccountNo>"+stanbicAgentCashInRequest.getSourceAccount()+"</sourceAccountNo>\n" +
                "         <amount>"+stanbicAgentCashInRequest.getAmount()+"</amount>\n" +
                "         <beneficiaryReference>TRANSFER-NARRATION-VIA-QT</beneficiaryReference>\n" +
                "         <customerReference>TRANSFER-NARRATION-VIA-QT</customerReference>\n" +
                "      </otherRequestDetails>\n" +
                "]]></body>\n" +
                "</soap:request>\n" +
                "</soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

}
