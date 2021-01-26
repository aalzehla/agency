package com._3line.gravity.freedom.NIBBS;

import com._3line.gravity.freedom.NIBBS.dto.AgentDataReportDTO;
import com._3line.gravity.freedom.NIBBS.dto.ResetReqDTO;
import com._3line.gravity.freedom.NIBBS.dto.TransactionReportDTO;
import com._3line.gravity.freedom.NIBBS.dto.SummaryReportDTO;
import com._3line.gravity.freedom.NIBBS.service.NIBBSReportService;
import com._3line.gravity.freedom.NIBBS.service.implementation.NIBBSReportServiceImpl;
import com._3line.gravity.freedom.financialInstitutions.wemaapi.utils.DateFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class Exec {


    private NIBBSReportService nibbsReportService;



    public static void main(String[] args) {
        //get Calendar instance
//        Calendar now = Calendar.getInstance();

        //get current TimeZone using getTimeZone method of Calendar class
//        TimeZone timeZone = now.getTimeZone();

        //display current TimeZone using getDisplayName() method of TimeZone class
//        System.out.println("Current TimeZone is : " + timeZone.getDisplayName());
//        Exec exec = new Exec();
//        exec.doUpdateAgent();

//        String naturalDateString = "Wed Jul 10 00:00:00 WAT 2019";
//        System.out.println(naturalDateString.length());
//        String newString = naturalDateString.substring(24,28)+"-"+naturalDateString.substring(4,7)+"-"+naturalDateString.substring(8,10);
//        System.out.println(newString);
//        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MMM-dd");
//        try {
//            Date dt = simpleDateFormat2.parse(newString);
//            System.out.println(dt);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

    }

    private void ping(){
        nibbsReportService = new NIBBSReportServiceImpl();
        nibbsReportService.ping();
    }

    private void reset(){
        ResetReqDTO resetReqDTO = new ResetReqDTO();
        resetReqDTO.setEmail("softwaredev@3lineng.com");
        resetReqDTO.setInstitutionCode("00907");
        nibbsReportService = new NIBBSReportServiceImpl();
        nibbsReportService.reset(resetReqDTO);
    }

    private void doCreateAgent(){
        nibbsReportService = new NIBBSReportServiceImpl();
        AgentDataReportDTO dataReportDTO = new AgentDataReportDTO();
        String plainText ="{\n" +
                "   \"title\":\"CHIEF\",\n" +
                "   \"firstName\":\"labi\",\n" +
                "   \"lastName\":\"owolabi\",\n" +
                "   \"middleName\":\"Ken\",\n" +
                "   \"bvn\":\"68317900913\",\n" +
                "   \"phoneList\":[\n" +
                "      \"08024578990\",\n" +
                "      \"07012345678\"\n" +
                "   ],\n" +
                "   \"servicesProvided\":[\n" +
                "      \"ACCOUNT_OPENING\",\n" +
                "      \"CASH_OUT\",\n" +
                "      \"FUNDS_TRANSFER\"\n" +
                "   ],\n" +
                "   \"city\":\"Greenfield Est, Ago Palace Way\",\n" +
                "   \"lga\":\"Oshodi-Isolo\",\n" +
                "   \"state\":\"Lagos\",\n" +
                "   \"emailAddress\":\"timothy.owolabi@3lineng.com\",\n" +
                "   \"latitude\":5.3856,\n" +
                "   \"longitude\":5.3456,\n" +
                "   \"username\":\"labi007\",\n" +
                "   \"streetNumber\":\"4\",\n" +
                "   \"streetName\":\"Oshodi Apapa Way\",\n" +
                "   \"streetDescription\":\"After Royal Palace\",\n" +
                "   \"ward\":\"xxxx\",\n" +
                "   \"password\":\"passWord_123\",\n" +
                "   \"additionalService2Count\":2\n" +
                "}";

        Gson g = new Gson();
        AgentDataReportDTO p = g.fromJson(plainText, AgentDataReportDTO.class);
        nibbsReportService.createAgent(p);
    }

    private void doUpdateAgent(){
        nibbsReportService = new NIBBSReportServiceImpl();
        String plainText ="{\n" +
                "   \"title\":\"CHIEF\",\n" +
                "   \"firstName\":\"labi\",\n" +
                "   \"lastName\":\"owolabi\",\n" +
                "   \"middleName\":\"Ibukun\",\n" +
                "   \"bvn\":\"68317900913\",\n" +
                "   \"agentCode\":\"99988584\",\n" +
                "   \"phoneList\":[\n" +
                "      \"08067578990\",\n" +
                "      \"07082345678\"\n" +
                "   ],\n" +
                "   \"servicesProvided\":[\n" +
                "      \"ACCOUNT_OPENING\",\n" +
                "      \"CASH_OUT\",\n" +
                "      \"FUNDS_TRANSFER\"\n" +
                "   ],\n" +
                "   \"city\":\"Greenfield Est, Ago Palace Way\",\n" +
                "   \"lga\":\"Oshodi-Isolo\",\n" +
                "   \"state\":\"Lagos\",\n" +
                "   \"emailAddress\":\"johnson.adebiyi@3lineng.com\",\n" +
                "   \"latitude\":5.3856,\n" +
                "   \"longitude\":5.3456,\n" +
                "   \"username\":\"labi007\",\n" +
                "   \"streetNumber\":\"4\",\n" +
                "   \"streetName\":\"Oshodi Apapa Way\",\n" +
                "   \"streetDescription\":\"After Royal Palace\",\n" +
                "   \"ward\":\"xxxx\",\n" +
                "   \"password\":\"passWord_123\",\n" +
                "   \"additionalService2Count\":2\n" +
                "}";

        Gson g = new Gson();
        AgentDataReportDTO p = g.fromJson(plainText, AgentDataReportDTO.class);
        p.setAgentCode("99988584");
        nibbsReportService.updateAgent(p);
    }

    private void pushTranSummary(){
        nibbsReportService = new NIBBSReportServiceImpl();
        String plainText ="{\n" +
                "        \"transactionDate\":\"2018-07-10\",\n" +
                "            \"cashInCount\":\"1000\",\n" +
                "            \"cashInValue\":\"20000.00\",\n" +
                "            \"cashOutCount\":\"1000\",\n" +
                "            \"cashOutValue\":\"2000.00\",\n" +
                "            \"accountOpeningCount\":\"2000\",\n" +
                "            \"accountOpeningValue\":\"4000.00\",\n" +
                "            \"billsPaymentCount\":\"2000\",\n" +
                "            \"billsPaymentValue\":\"3000.00\",\n" +
                "            \"airtimeRechargeCount\":\"20000\",\n" +
                "            \"airtimeRechargeValue\":\"4000.00\",\n" +
                "            \"fundTransferCount\":\"2000\",\n" +
                "            \"fundTransferValue\":\"3000.00\",\n" +
                "            \"bvnEnrollmentCount\":\"1000\",\n" +
                "            \"bvnEnrollmentValue\":\"3000.00\",\n" +
                "            \"othersCount\":\"4000\",\n" +
                "            \"othersValue\":\"30000.00\",\n" +
                "            \"additionalService1Count\":\"\",\n" +
                "            \"additionalService1Value\":\"\",\n" +
                "            \"additionalService2Count\":\"\",\n" +
                "            \"additionalService2Value\":\"\"\n" +
                "    }";

        Gson g = new Gson();


        SummaryReportDTO p =  g.fromJson(plainText, SummaryReportDTO.class);
//        nibbsReportService.createTransactionSummaryReport(p);
    }

    private void pushSingleTranReport(){
        nibbsReportService = new NIBBSReportServiceImpl();
        String plainText ="{\n" +
                "   \"transactionDate\":\"2019-06-26\",\n" +
//                "   \"agentCode\":\"99988547\",\n" +
                "   \"agentCode\":\"99988584\",\n" +
                "   \"cashInCount\":\"0\",\n" +
                "   \"cashInValue\":\"0\",\n" +
                "   \"cashOutCount\":\"1\",\n" +
                "   \"cashOutValue\":\"5750000\",\n" +
                "   \"accountOpeningCount\":\"1\",\n" +
                "   \"accountOpeningValue\":\"50000\",\n" +
                "   \"billsPaymentCount\":\"0\",\n" +
                "   \"billsPaymentValue\":\"0\",\n" +
                "   \"airtimeRechargeCount\":\"0\",\n" +
                "   \"airtimeRechargeValue\":\"0\",\n" +
                "   \"fundTransferCount\":\"1\",\n" +
                "   \"fundTransferValue\":\"4500000\",\n" +
                "   \"bvnEnrollmentCount\":\"0\",\n" +
                "   \"bvnEnrollmentValue\":\"0\",\n" +
                "   \"othersCount\":\"0\",\n" +
                "   \"othersValue\":\"0.00\",\n" +
                "   \"additionalService1Count\":\"0\",\n" +
                "   \"additionalService1Value\":\"0\",\n" +
                "   \"additionalService2Count\":\"0\",\n" +
                "   \"additionalService2Value\":\"0\"\n" +
                "}";

        Gson g = new Gson();


        TransactionReportDTO p =  g.fromJson(plainText, TransactionReportDTO.class);
        nibbsReportService.createReport(p);
    }

    private void pushBulkTranReport(){
        nibbsReportService = new NIBBSReportServiceImpl();
        String plainText ="[\n" +
                "   {\n" +
                "      \"transactionDate\":\"2019-06-27\",\n" +
                "      \"agentCode\":\"99988584\",\n" +
                "      \"cashInCount\":\"0\",\n" +
                "      \"cashInValue\":\"0\",\n" +
                "      \"cashOutCount\":\"1\",\n" +
                "      \"cashOutValue\":\"5750000\",\n" +
                "      \"accountOpeningCount\":\"1\",\n" +
                "      \"accountOpeningValue\":\"50000\",\n" +
                "      \"billsPaymentCount\":\"0\",\n" +
                "      \"billsPaymentValue\":\"0\",\n" +
                "      \"airtimeRechargeCount\":\"0\",\n" +
                "      \"airtimeRechargeValue\":\"0\",\n" +
                "      \"fundTransferCount\":\"1\",\n" +
                "      \"fundTransferValue\":\"4500000\",\n" +
                "      \"bvnEnrollmentCount\":\"0\",\n" +
                "      \"bvnEnrollmentValue\":\"0\",\n" +
                "      \"othersCount\":\"0\",\n" +
                "      \"othersValue\":\"0.00\",\n" +
                "      \"additionalService1Count\":\"0\",\n" +
                "      \"additionalService1Value\":\"0\",\n" +
                "      \"additionalService2Count\":\"0\",\n" +
                "      \"additionalService2Value\":\"0\"\n" +
                "   }" +
                ",\n" +
                "   {\n" +
                "      \"transactionDate\":\"2019-06-27\",\n" +
                "      \"agentCode\":\"99988584\",\n" +
                "      \"cashInCount\":\"0\",\n" +
                "      \"cashInValue\":\"0\",\n" +
                "      \"cashOutCount\":\"1\",\n" +
                "      \"cashOutValue\":\"25750000\",\n" +
                "      \"accountOpeningCount\":\"1\",\n" +
                "      \"accountOpeningValue\":\"750000\",\n" +
                "      \"billsPaymentCount\":\"0\",\n" +
                "      \"billsPaymentValue\":\"0\",\n" +
                "      \"airtimeRechargeCount\":\"0\",\n" +
                "      \"airtimeRechargeValue\":\"0\",\n" +
                "      \"fundTransferCount\":\"1\",\n" +
                "      \"fundTransferValue\":\"7500000\",\n" +
                "      \"bvnEnrollmentCount\":\"0\",\n" +
                "      \"bvnEnrollmentValue\":\"0\",\n" +
                "      \"othersCount\":\"0\",\n" +
                "      \"othersValue\":\"0.00\",\n" +
                "      \"additionalService1Count\":\"0\",\n" +
                "      \"additionalService1Value\":\"0\",\n" +
                "      \"additionalService2Count\":\"0\",\n" +
                "      \"additionalService2Value\":\"0\"\n" +
                "   }\n" +
                ",\n" +
                "   {\n" +
                "      \"transactionDate\":\"2019-06-27\",\n" +
                "      \"agentCode\":\"99988584\",\n" +
                "      \"cashInCount\":\"0\",\n" +
                "      \"cashInValue\":\"0\",\n" +
                "      \"cashOutCount\":\"1\",\n" +
                "      \"cashOutValue\":\"25750000\",\n" +
                "      \"accountOpeningCount\":\"1\",\n" +
                "      \"accountOpeningValue\":\"750000\",\n" +
                "      \"billsPaymentCount\":\"0\",\n" +
                "      \"billsPaymentValue\":\"0\",\n" +
                "      \"airtimeRechargeCount\":\"0\",\n" +
                "      \"airtimeRechargeValue\":\"0\",\n" +
                "      \"fundTransferCount\":\"1\",\n" +
                "      \"fundTransferValue\":\"7500000\",\n" +
                "      \"bvnEnrollmentCount\":\"0\",\n" +
                "      \"bvnEnrollmentValue\":\"0\",\n" +
                "      \"othersCount\":\"0\",\n" +
                "      \"othersValue\":\"0.00\",\n" +
                "      \"additionalService1Count\":\"0\",\n" +
                "      \"additionalService1Value\":\"0\",\n" +
                "      \"additionalService2Count\":\"0\",\n" +
                "      \"additionalService2Value\":\"0\"\n" +
                "   }\n" +
                ",\n" +
                "   {\n" +
                "      \"transactionDate\":\"2019-06-27\",\n" +
                "      \"agentCode\":\"99988584\",\n" +
                "      \"cashInCount\":\"0\",\n" +
                "      \"cashInValue\":\"0\",\n" +
                "      \"cashOutCount\":\"1\",\n" +
                "      \"cashOutValue\":\"25750000\",\n" +
                "      \"accountOpeningCount\":\"1\",\n" +
                "      \"accountOpeningValue\":\"750000\",\n" +
                "      \"billsPaymentCount\":\"0\",\n" +
                "      \"billsPaymentValue\":\"0\",\n" +
                "      \"airtimeRechargeCount\":\"0\",\n" +
                "      \"airtimeRechargeValue\":\"0\",\n" +
                "      \"fundTransferCount\":\"1\",\n" +
                "      \"fundTransferValue\":\"7500000\",\n" +
                "      \"bvnEnrollmentCount\":\"0\",\n" +
                "      \"bvnEnrollmentValue\":\"0\",\n" +
                "      \"othersCount\":\"0\",\n" +
                "      \"othersValue\":\"0.00\",\n" +
                "      \"additionalService1Count\":\"0\",\n" +
                "      \"additionalService1Value\":\"0\",\n" +
                "      \"additionalService2Count\":\"0\",\n" +
                "      \"additionalService2Value\":\"0\"\n" +
                "   }\n" +
                ",\n" +
                "   {\n" +
                "      \"transactionDate\":\"2019-06-27\",\n" +
                "      \"agentCode\":\"99988584\",\n" +
                "      \"cashInCount\":\"0\",\n" +
                "      \"cashInValue\":\"0\",\n" +
                "      \"cashOutCount\":\"1\",\n" +
                "      \"cashOutValue\":\"25750000\",\n" +
                "      \"accountOpeningCount\":\"1\",\n" +
                "      \"accountOpeningValue\":\"750000\",\n" +
                "      \"billsPaymentCount\":\"0\",\n" +
                "      \"billsPaymentValue\":\"0\",\n" +
                "      \"airtimeRechargeCount\":\"0\",\n" +
                "      \"airtimeRechargeValue\":\"0\",\n" +
                "      \"fundTransferCount\":\"1\",\n" +
                "      \"fundTransferValue\":\"7500000\",\n" +
                "      \"bvnEnrollmentCount\":\"0\",\n" +
                "      \"bvnEnrollmentValue\":\"0\",\n" +
                "      \"othersCount\":\"0\",\n" +
                "      \"othersValue\":\"0.00\",\n" +
                "      \"additionalService1Count\":\"0\",\n" +
                "      \"additionalService1Value\":\"0\",\n" +
                "      \"additionalService2Count\":\"0\",\n" +
                "      \"additionalService2Value\":\"0\"\n" +
                "   }\n" +
                ",\n" +
                "   {\n" +
                "      \"transactionDate\":\"2019-06-27\",\n" +
                "      \"agentCode\":\"99988584\",\n" +
                "      \"cashInCount\":\"0\",\n" +
                "      \"cashInValue\":\"0\",\n" +
                "      \"cashOutCount\":\"1\",\n" +
                "      \"cashOutValue\":\"25750000\",\n" +
                "      \"accountOpeningCount\":\"1\",\n" +
                "      \"accountOpeningValue\":\"750000\",\n" +
                "      \"billsPaymentCount\":\"0\",\n" +
                "      \"billsPaymentValue\":\"0\",\n" +
                "      \"airtimeRechargeCount\":\"0\",\n" +
                "      \"airtimeRechargeValue\":\"0\",\n" +
                "      \"fundTransferCount\":\"1\",\n" +
                "      \"fundTransferValue\":\"7500000\",\n" +
                "      \"bvnEnrollmentCount\":\"0\",\n" +
                "      \"bvnEnrollmentValue\":\"0\",\n" +
                "      \"othersCount\":\"0\",\n" +
                "      \"othersValue\":\"0.00\",\n" +
                "      \"additionalService1Count\":\"0\",\n" +
                "      \"additionalService1Value\":\"0\",\n" +
                "      \"additionalService2Count\":\"0\",\n" +
                "      \"additionalService2Value\":\"0\"\n" +
                "   }\n" +
                ",\n" +
                "   {\n" +
                "      \"transactionDate\":\"2019-06-27\",\n" +
                "      \"agentCode\":\"99988584\",\n" +
                "      \"cashInCount\":\"0\",\n" +
                "      \"cashInValue\":\"0\",\n" +
                "      \"cashOutCount\":\"1\",\n" +
                "      \"cashOutValue\":\"25750000\",\n" +
                "      \"accountOpeningCount\":\"1\",\n" +
                "      \"accountOpeningValue\":\"750000\",\n" +
                "      \"billsPaymentCount\":\"0\",\n" +
                "      \"billsPaymentValue\":\"0\",\n" +
                "      \"airtimeRechargeCount\":\"0\",\n" +
                "      \"airtimeRechargeValue\":\"0\",\n" +
                "      \"fundTransferCount\":\"1\",\n" +
                "      \"fundTransferValue\":\"7500000\",\n" +
                "      \"bvnEnrollmentCount\":\"0\",\n" +
                "      \"bvnEnrollmentValue\":\"0\",\n" +
                "      \"othersCount\":\"0\",\n" +
                "      \"othersValue\":\"0.00\",\n" +
                "      \"additionalService1Count\":\"0\",\n" +
                "      \"additionalService1Value\":\"0\",\n" +
                "      \"additionalService2Count\":\"0\",\n" +
                "      \"additionalService2Value\":\"0\"\n" +
                "   }\n" +
                ",\n" +
                "   {\n" +
                "      \"transactionDate\":\"2019-06-27\",\n" +
                "      \"agentCode\":\"99988584\",\n" +
                "      \"cashInCount\":\"0\",\n" +
                "      \"cashInValue\":\"0\",\n" +
                "      \"cashOutCount\":\"1\",\n" +
                "      \"cashOutValue\":\"25750000\",\n" +
                "      \"accountOpeningCount\":\"1\",\n" +
                "      \"accountOpeningValue\":\"750000\",\n" +
                "      \"billsPaymentCount\":\"0\",\n" +
                "      \"billsPaymentValue\":\"0\",\n" +
                "      \"airtimeRechargeCount\":\"0\",\n" +
                "      \"airtimeRechargeValue\":\"0\",\n" +
                "      \"fundTransferCount\":\"1\",\n" +
                "      \"fundTransferValue\":\"7500000\",\n" +
                "      \"bvnEnrollmentCount\":\"0\",\n" +
                "      \"bvnEnrollmentValue\":\"0\",\n" +
                "      \"othersCount\":\"0\",\n" +
                "      \"othersValue\":\"0.00\",\n" +
                "      \"additionalService1Count\":\"0\",\n" +
                "      \"additionalService1Value\":\"0\",\n" +
                "      \"additionalService2Count\":\"0\",\n" +
                "      \"additionalService2Value\":\"0\"\n" +
                "   }\n" +
                ",\n" +
                "   {\n" +
                "      \"transactionDate\":\"2019-06-27\",\n" +
                "      \"agentCode\":\"99988584\",\n" +
                "      \"cashInCount\":\"0\",\n" +
                "      \"cashInValue\":\"0\",\n" +
                "      \"cashOutCount\":\"1\",\n" +
                "      \"cashOutValue\":\"25750000\",\n" +
                "      \"accountOpeningCount\":\"1\",\n" +
                "      \"accountOpeningValue\":\"750000\",\n" +
                "      \"billsPaymentCount\":\"0\",\n" +
                "      \"billsPaymentValue\":\"0\",\n" +
                "      \"airtimeRechargeCount\":\"0\",\n" +
                "      \"airtimeRechargeValue\":\"0\",\n" +
                "      \"fundTransferCount\":\"1\",\n" +
                "      \"fundTransferValue\":\"7500000\",\n" +
                "      \"bvnEnrollmentCount\":\"0\",\n" +
                "      \"bvnEnrollmentValue\":\"0\",\n" +
                "      \"othersCount\":\"0\",\n" +
                "      \"othersValue\":\"0.00\",\n" +
                "      \"additionalService1Count\":\"0\",\n" +
                "      \"additionalService1Value\":\"0\",\n" +
                "      \"additionalService2Count\":\"0\",\n" +
                "      \"additionalService2Value\":\"0\"\n" +
                "   }\n" +
                ",\n" +
                "   {\n" +
                "      \"transactionDate\":\"2019-06-27\",\n" +
                "      \"agentCode\":\"99988584\",\n" +
                "      \"cashInCount\":\"0\",\n" +
                "      \"cashInValue\":\"0\",\n" +
                "      \"cashOutCount\":\"1\",\n" +
                "      \"cashOutValue\":\"25750000\",\n" +
                "      \"accountOpeningCount\":\"1\",\n" +
                "      \"accountOpeningValue\":\"750000\",\n" +
                "      \"billsPaymentCount\":\"0\",\n" +
                "      \"billsPaymentValue\":\"0\",\n" +
                "      \"airtimeRechargeCount\":\"0\",\n" +
                "      \"airtimeRechargeValue\":\"0\",\n" +
                "      \"fundTransferCount\":\"1\",\n" +
                "      \"fundTransferValue\":\"7500000\",\n" +
                "      \"bvnEnrollmentCount\":\"0\",\n" +
                "      \"bvnEnrollmentValue\":\"0\",\n" +
                "      \"othersCount\":\"0\",\n" +
                "      \"othersValue\":\"0.00\",\n" +
                "      \"additionalService1Count\":\"0\",\n" +
                "      \"additionalService1Value\":\"0\",\n" +
                "      \"additionalService2Count\":\"0\",\n" +
                "      \"additionalService2Value\":\"0\"\n" +
                "   }\n" +
                ",\n" +
                "   {\n" +
                "      \"transactionDate\":\"2019\",\n" +
                "      \"agentCode\":\"99988584\",\n" +
                "      \"cashInCount\":\"0\",\n" +
                "      \"cashInValue\":\"0\",\n" +
                "      \"cashOutCount\":\"1\",\n" +
                "      \"cashOutValue\":\"25750000\",\n" +
                "      \"accountOpeningCount\":\"1\",\n" +
                "      \"accountOpeningValue\":\"750000\",\n" +
                "      \"billsPaymentCount\":\"0\",\n" +
                "      \"billsPaymentValue\":\"0\",\n" +
                "      \"airtimeRechargeCount\":\"0\",\n" +
                "      \"airtimeRechargeValue\":\"0\",\n" +
                "      \"fundTransferCount\":\"1\",\n" +
                "      \"fundTransferValue\":\"7500000\",\n" +
                "      \"bvnEnrollmentCount\":\"0\",\n" +
                "      \"bvnEnrollmentValue\":\"0\",\n" +
                "      \"othersCount\":\"0\",\n" +
                "      \"othersValue\":\"0.00\",\n" +
                "      \"additionalService1Count\":\"0\",\n" +
                "      \"additionalService1Value\":\"0\",\n" +
                "      \"additionalService2Count\":\"0\",\n" +
                "      \"additionalService2Value\":\"0\"\n" +
                "   }\n" +
                "]";

        Gson g = new Gson();

        Type listType = new TypeToken<List<TransactionReportDTO>>() {}.getType();

        List<TransactionReportDTO> ps =  g.fromJson(plainText, listType);
        System.out.println("wow : : "+ps.size());
        nibbsReportService.createReportInBulk(ps,"34134");
    }



}
