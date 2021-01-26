package com._3line.gravity.freedom.financialInstitutions.wemaapi.service;

import com._3line.gravity.freedom.financialInstitutions.wemaapi.model.*;
import com._3line.gravity.freedom.financialInstitutions.wemaapi.utils.DateFormatter;
import com._3line.gravity.freedom.financialInstitutions.wemaapi.utils.TestClient;
import com._3line.gravity.freedom.financialInstitutions.wemaapi.utils.WemaAESCipher;
import com._3line.gravity.freedom.utility.MessageSource;
import com._3line.gravity.freedom.utility.Utility;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author JoyU
 * @date 9/17/2018
 */

@Service
public class WemaApiServiceImpl implements WemaApiService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    WemaApiAuditRepo wemaApiAuditRepo;

    @Autowired
    TestClient testClient;

    @Autowired
    WemaAESCipher wemaAESCipher;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${wema.superagent}")
    private String superAgent;

    @Value("${wema.subagent}")
    private String subAgent;

    @Value("${wema.ussdenrol}")
    private String ussdEnrollment;

    @Value("${wema.branchcode}")
    private String branchCode;

    @Override
    public String encryptMessage(String message) {
        String result = "";
        try {
            //This is the Soap Xml Payload
            String payload = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <encrypt xmlns=\"http://tempuri.org/\">\n" +
                    "      <![CDATA[<vlaue>" + message + "</vlaue>]]>\n" +
                    "    </encrypt>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
            String soapAction = "http://tempuri.org/encrypt";
            //This method sends the HTTP post request
            String response = testClient.sendHttpRequest(payload,soapAction);

            boolean check = response.contains("<encryptResponse");
            if(check) {
                //This is helps to extract the response from the Xml tags
                result = StringUtils.substringBetween(response, "<encryptResult>", "</encryptResult>");
                logger.info("This is the encrypted message {} ", result);
            }else {
                result = MessageSource.getMessage("wema.encrypt.error");
            }
        }catch (Exception e){
            logger.error("Error Message {} ", e.getCause().getMessage());
        }
        return result;
    }

    @Override
    public String decryptMessage(String message) {
        String result = "";
        try {
            //This is the Soap Xml Payload
            String payload = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <decrypt xmlns=\"http://tempuri.org/\">\n" +
                    "      <![CDATA[<vlaue>"+ message +"</vlaue>]]>\n" +
                    "    </decrypt>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
            String soapAction = "http://tempuri.org/decrypt";
            //This method sends the HTTP post request
            String response = testClient.sendHttpRequest(payload,soapAction);

            boolean check = response.contains("<decryptResponse");
            if(check) {
                //This is helps to extract the response from the Xml tags
                result = StringUtils.substringBetween(response, "<decryptResult>", "</decryptResult>");
                logger.info("This is the decrypted message {} ", result);
            }else {
                result = MessageSource.getMessage("wema.decrypt.error");
            }
        }catch (Exception e){
            logger.error("Error Message {} ", e.getCause().getMessage());
        }
        return result;
    }

    public String getWhen(){
        Date date = new Date();
        String newDate = DateFormatter.wemaFormat(date);
        logger.info("THIS IS WHEN {} ", newDate);
        return newDate;
    }

    public String getTranRefrence(){
        Long ref = System.currentTimeMillis();
        String reference = String.valueOf(ref);
        return reference;
    }

    public String getAccountOpeningSHACode(AccOpeningWema accOpeningWema){
        String sHACode = "ACCOUNTOPENING" + accOpeningWema.getFirstName() + accOpeningWema.getMiddleName() + accOpeningWema.getLastName() + accOpeningWema.getDateOfBirth() + accOpeningWema.getGender() + accOpeningWema.getAddress() + accOpeningWema.getPhoneNo();
        logger.info("ACCOUNT OPENING sha code {} ", sHACode);
        return sHACode;
    }

    public String getOtherSHACode(WemaApiAudit wemaApiAudit){
        String sHACode = wemaApiAudit.getTranReference() +  wemaApiAudit.getWhenn() + superAgent + subAgent;
        logger.info("TRAN REF sha code {} ", sHACode);
        return sHACode;
    }

    @Override
    public AccOpeningWema openAccount(AccOpeningWema accOpeningWema) {
        AccOpeningWema accOpeningWemaResponse = new AccOpeningWema();
        //This is used to get the hash Code for Security Key using SHA512
        String tranReference = getTranRefrence();
        String when = getWhen();

        String sessionSecurityKey = Utility.sha512(getAccountOpeningSHACode(accOpeningWema));
        try{
            //This helps to Encrypt and Set the Class variables into the Xml payload
            Context context = new Context();
            context.setVariable("address", wemaAESCipher.encrypt(accOpeningWema.getAddress()));
            context.setVariable("amount", wemaAESCipher.encrypt(accOpeningWema.getAmount()));
            context.setVariable("branchcode", wemaAESCipher.encrypt(branchCode));
            context.setVariable("dateofbirth", wemaAESCipher.encrypt(accOpeningWema.getDateOfBirth()));
            context.setVariable("firstname", wemaAESCipher.encrypt(accOpeningWema.getFirstName()));
            context.setVariable("lastname", wemaAESCipher.encrypt(accOpeningWema.getLastName()));
            context.setVariable("middlename", wemaAESCipher.encrypt(accOpeningWema.getMiddleName()));
            context.setVariable("gender", wemaAESCipher.encrypt(accOpeningWema.getGender()));
            context.setVariable("phoneno", wemaAESCipher.encrypt(accOpeningWema.getPhoneNo()));
            context.setVariable("photo", wemaAESCipher.encrypt(accOpeningWema.getPhoto()));
            context.setVariable("securitykey", sessionSecurityKey);
            context.setVariable("signature", wemaAESCipher.encrypt(accOpeningWema.getSignature()));
            context.setVariable("subagentid", wemaAESCipher.encrypt(subAgent));
            context.setVariable("superagentid", wemaAESCipher.encrypt(superAgent));
            context.setVariable("tranref", wemaAESCipher.encrypt(tranReference));
            context.setVariable("ussdenrollment", ussdEnrollment);
            context.setVariable("when", wemaAESCipher.encrypt(when));

            //This is the Soap Xml Payload
            String payload = templateEngine.process("wemabankxmls/accopening.xml", context);
            String soapAction = "http://tempuri.org/AccountOpening";
            //This method sends the HTTP post request
            String response = testClient.sendHttpRequest(payload,soapAction);

            boolean check = response.contains("<AccountOpeningResponse");
            String responseCode= "";
            String responseDesc= "";
            String tranRef= "";
            String tranId= "";
            String tranType= "";
            String tranDate= "";
            String accountNo= "";
            if(check) {
                //These extracts the response, decrypts it and sets it into the class
                responseCode = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ReponseCode>", "</ReponseCode>"));
                accOpeningWemaResponse.setResponseCode(responseCode);
                responseDesc = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ResponsDescription>", "</ResponsDescription>"));
                accOpeningWemaResponse.setResponseDesc(responseDesc);
                boolean checkCode = responseCode.contains("00");
                if (checkCode) {
                    tranRef = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransReference>", "</TransReference>"));
                    accOpeningWemaResponse.setTranReference(tranRef);
                    tranId = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionID>", "</TransactionID>"));
                    accOpeningWemaResponse.setTranId(tranId);
                    tranType = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionType>", "</TransactionType>"));
                    accOpeningWemaResponse.setTranType(tranType);
                    tranDate = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionDate>", "</TransactionDate>"));
                    accOpeningWemaResponse.setTranDate(tranDate);
                    accountNo = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<AccountNumber>", "</AccountNumber>"));
                    accOpeningWemaResponse.setAccountNumber(accountNo);
                }

                logger.info("This is the Account Details Response {} ", accOpeningWemaResponse.getResponseCode() + accOpeningWemaResponse.getResponseDesc());
            }else {
                MessageSource.getMessage("wema.accopening.error");
            }
            WemaApiAudit wemaApiAudit= new WemaApiAudit();
            modelMapper.map(accOpeningWema, wemaApiAudit);
            wemaApiAudit.setWhenn(when);
            wemaApiAudit.setTranReference(tranReference);
            wemaApiAudit.setSecuritySessionKey(sessionSecurityKey);
            wemaApiAudit.setResponseCode(responseCode);
            wemaApiAudit.setResponseDesc(responseDesc);
            wemaApiAudit.setTranReference(tranRef);
            wemaApiAudit.setTranId(tranId);
            wemaApiAudit.setTranType(tranType);
            wemaApiAudit.setTranDate(tranDate);
            wemaApiAudit.setTranRefResponse(tranRef);
            wemaApiAudit.setAccountNumber(accountNo);
            wemaApiAudit.setXmlRequestPayload(payload);
            wemaApiAudit.setXmlResponsePayload(response);
            wemaApiAudit.setSuperAgentId(superAgent);
            wemaApiAudit.setSubAgentId(subAgent);
            wemaApiAudit.setUssdEnrollment(ussdEnrollment);
            wemaApiAudit.setWemaApiType(WemaApiType.ACCOUNT_OPENING);
            wemaApiAuditRepo.save(wemaApiAudit);
            logger.info("DONE !!!");

        }catch (Exception e){
            logger.error("The Error Message: {}", e.getCause().getMessage());
        }
        logger.info("This is the Wema Account Opening Request :  {} ",accOpeningWema.getDateOfBirth());
        return accOpeningWemaResponse;
    }

    public WemaApiAudit getAuditDetails(WemaApiAudit wemaApiAuditRequest){
        WemaApiAudit wemaApiAudit = new WemaApiAudit();
        modelMapper.map(wemaApiAuditRequest, wemaApiAudit);
        return wemaApiAudit;
    }

    @Override
    public List<String> getBranchList() {
        List<String> result = new ArrayList<>();
        try {
            //This is the Soap Xml Payload
            String payload = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <GetBanksList xmlns=\"http://tempuri.org/\" />\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
            String soapAction = "http://tempuri.org/GetBanksList";
            String response = testClient.sendHttpRequest(payload,soapAction);

            boolean check = response.contains("<GetBanksListResponse");
            String extractedBranches = "";
            if(check) {
                //This is helps to extract the response from the Xml tags
                extractedBranches = StringUtils.substringBetween(response, "<string>", "</string>");
                //This Decrypts the response and converts it to a List
                result = Arrays.asList(wemaAESCipher.decrypt(extractedBranches));
                logger.info("This is the List of Branches {} ", result);
            }else {
                MessageSource.getMessage("wema.getbranch.error");
            }
            WemaApiAudit wemaApiAudit = new WemaApiAudit();
            wemaApiAudit.setXmlRequestPayload(payload);
            wemaApiAudit.setXmlResponsePayload(response);
            wemaApiAudit.setBranchCode(extractedBranches);
            wemaApiAudit.setWemaApiType(WemaApiType.GET_BRANCH);
            wemaApiAuditRepo.save(wemaApiAudit);
        }catch (Exception e){
            logger.error("Error Message {} ", e.getCause().getMessage());
        }
        return result;
    }

    @Override
    public List<BasicDetailsAPI> branchList(BasicDetailsAPI basicDetailsAPI) {
        List<BasicDetailsAPI> basicDetailsAPIList = new ArrayList<>();
        BasicDetailsAPI basicDetailsAPIresponse = new BasicDetailsAPI();
        //This is used to get the hash Code for Security Key using SHA512
        String tranReference = getTranRefrence();
        String when = getWhen();
        WemaApiAudit wemaApiAud = new WemaApiAudit();
        wemaApiAud.setTranReference(tranReference);
        wemaApiAud.setWhenn(when);
        wemaApiAud.setSubAgentId(subAgent);
        wemaApiAud.setSuperAgentId(superAgent);
        String shaCode = getOtherSHACode(wemaApiAud);
        logger.info("This is the SHA CODE {} ", shaCode);
        String sessionSecurityKey = Utility.sha512(shaCode);
        try{
            //This helps to Encrypt and Set the Class variables into the Xml payload
            String payload = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <BranchList xmlns=\"http://tempuri.org/\">\n" +
                    "      <SuperAgentID>"+ wemaAESCipher.encrypt(superAgent) +"</SuperAgentID>\n" +
                    "      <SubAgentID>"+ wemaAESCipher.encrypt(subAgent) +"</SubAgentID>\n" +
                    "      <Transactionreference>"+wemaAESCipher.encrypt(tranReference)+"</Transactionreference>\n" +
                    "      <When>"+wemaAESCipher.encrypt(when)+"</When>\n" +
                    "      <SecuritySessionKey>"+sessionSecurityKey+"</SecuritySessionKey>\n" +
                    "    </BranchList>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
            //This is the Soap Xml Payload
            String soapAction = "http://tempuri.org/BranchList";
            //This method sends the HTTP post request
            String response = testClient.sendHttpRequest(payload,soapAction);

            boolean check = response.contains("<BranchListResponse");
            String responseCode= "";
            String responseDesc= "";
            String branchCode= "";
            String branchDesc= "";
            String tranRef= "";
            String eachBranch= "";
            if(check) {
                //These extracts the response, decrypts it and sets it into the class
                responseCode = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ReponseCode>", "</ReponseCode>"));
                basicDetailsAPIresponse.setResponseCode(responseCode);
                responseDesc = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ResponseDescription>", "</ResponseDescription>"));
                basicDetailsAPIresponse.setResponseDesc(responseDesc);
                boolean checkCode = responseCode.contains("00");
                if (checkCode) {
                    //To convert the branches to a list of Strings
                    List<String> branchList = Arrays.asList(StringUtils.substringBetween(response, "<WemaBranchList>", "</WemaBranchList>"));
                    int count = branchList.size();
                    //To Insert each Branch into a List of Basic Details Class
                    for (int i = 0; i < count; i++){
                        eachBranch = branchList.get(i);
                        branchCode = wemaAESCipher.decrypt(StringUtils.substringBetween(eachBranch, "<BranchCode>", "</BranchCode>"));
                        basicDetailsAPIresponse.setBranchCode(tranRef);
                        branchDesc = wemaAESCipher.decrypt(StringUtils.substringBetween(eachBranch, "<BranchDescription>", "</BranchDescription>"));
                        basicDetailsAPIresponse.setBranchDescription(tranRef);
                        tranRef = wemaAESCipher.decrypt(StringUtils.substringBetween(eachBranch, "<TransReference>", "</TransReference>"));
                        basicDetailsAPIresponse.setTranReference(tranRef);
                        basicDetailsAPIresponse.setResponseCode(responseCode);
                        basicDetailsAPIresponse.setResponseDesc(responseDesc);
                        basicDetailsAPIList.add(basicDetailsAPIresponse);
                    }

                }
                logger.info("This is the Branch List Response {} ", basicDetailsAPIresponse.getResponseCode() + basicDetailsAPIresponse.getResponseDesc());
            }else {
                MessageSource.getMessage("wema.branchlist.error");
            }
            WemaApiAudit wemaApiAudit= new WemaApiAudit();
            modelMapper.map(basicDetailsAPI, wemaApiAudit);
            wemaApiAudit.setWhenn(when);
            wemaApiAudit.setTranReference(tranReference);
            wemaApiAudit.setSecuritySessionKey(sessionSecurityKey);
            wemaApiAudit.setResponseCode(responseCode);
            wemaApiAudit.setResponseDesc(responseDesc);
            wemaApiAudit.setTranReference(tranRef);
            wemaApiAudit.setBranchCode(branchCode);
            wemaApiAudit.setBranchDescription(branchDesc);
            wemaApiAudit.setXmlRequestPayload(payload);
            wemaApiAudit.setXmlResponsePayload(response);
            wemaApiAudit.setSuperAgentId(superAgent);
            wemaApiAudit.setSubAgentId(subAgent);
            wemaApiAudit.setWemaApiType(WemaApiType.BRANCH_LIST);
            wemaApiAuditRepo.save(wemaApiAudit);

        }catch (Exception e){
            logger.error("The Error Message: {}", e.getCause().getMessage());
        }
        return basicDetailsAPIList;
    }

    @Override
    public AccNameEnquiryAPI getAccountName(AccNameEnquiryAPI accNameEnquiryAPI) {
        AccNameEnquiryAPI accNameEnquiryAPIresponse = new AccNameEnquiryAPI();
        //This is used to get the hash Code for Security Key using SHA512
        String tranReference = getTranRefrence();
        String when = getWhen();
        WemaApiAudit wemaApiAud = new WemaApiAudit();
        wemaApiAud.setTranReference(tranReference);
        wemaApiAud.setWhenn(when);
        wemaApiAud.setSubAgentId(subAgent);
        wemaApiAud.setSuperAgentId(superAgent);
        String shaCode = getOtherSHACode(wemaApiAud);
        logger.info("This is the SHA CODE {} ", shaCode);

        String sessionSecurityKey = Utility.sha512(shaCode);
        try{
            //This helps to Encrypt and Set the Class variables into the Xml payload
            Context context = new Context();
            context.setVariable("securitykey", sessionSecurityKey);
            context.setVariable("subagentid", wemaAESCipher.encrypt(subAgent));
            context.setVariable("superagentid", wemaAESCipher.encrypt(superAgent));
            context.setVariable("tranref", wemaAESCipher.encrypt(tranReference));
            context.setVariable("accountno", wemaAESCipher.encrypt(accNameEnquiryAPI.getAccountNumber()));
            context.setVariable("bankname", wemaAESCipher.encrypt(accNameEnquiryAPI.getBankName()));
            context.setVariable("bankcode", wemaAESCipher.encrypt(accNameEnquiryAPI.getBankCode()));
            context.setVariable("when", wemaAESCipher.encrypt(when));

            //This is the Soap Xml Payload
            String payload = templateEngine.process("wemabankxmls/nipnameenq.xml", context);
            String soapAction = "http://tempuri.org/NIPAccountNameEnquiry";
            //This method sends the HTTP post request
            String response = testClient.sendHttpRequest(payload,soapAction);

            boolean check = response.contains("<NIPAccountNameEnquiryResponse");
            String responseCode= "";
            String responseDesc= "";
            String refNumber= "";
            String accountName= "";
            String accountNo= "";
            if(check) {
                //These extracts the response, decrypts it and sets it into the class
                responseCode = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ReponseCode>", "</ReponseCode>"));
                accNameEnquiryAPIresponse.setResponseCode(responseCode);
                responseDesc = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ResponseDescription>", "</ResponseDescription>"));
                accNameEnquiryAPIresponse.setResponseDesc(responseDesc);

                boolean checkCode = responseCode.contains("00");
                if (checkCode) {
                    accountName = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<AccountName>", "</AccountName>"));
                    accNameEnquiryAPIresponse.setAccountName(accountName);
                    refNumber = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ReferenceNumber>", "</ReferenceNumber>"));
                    accNameEnquiryAPIresponse.setRefNumber(refNumber);
                    accountNo = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<AccountNumber>", "</AccountNumber>"));
                    accNameEnquiryAPIresponse.setAccountNumber(accountNo);
                }

                logger.info("This is the Name Enquiry Response {} ", accNameEnquiryAPIresponse.getResponseCode() +  accNameEnquiryAPIresponse.getResponseDesc());
            }else {
                MessageSource.getMessage("wema.nameenq.error");
            }

            WemaApiAudit wemaApiAudit= new WemaApiAudit();
            modelMapper.map(accNameEnquiryAPI, wemaApiAudit);
            wemaApiAudit.setTranReference(tranReference);
            wemaApiAudit.setWhenn(when);
            wemaApiAudit.setSecuritySessionKey(sessionSecurityKey);
            wemaApiAudit.setResponseCode(responseCode);
            wemaApiAudit.setResponseDesc(responseDesc);
            wemaApiAudit.setRefNumber(refNumber);
            wemaApiAudit.setAccountName(accountName);
            wemaApiAudit.setAccountNumber(accountNo);
            wemaApiAudit.setXmlRequestPayload(payload);
            wemaApiAudit.setXmlResponsePayload(response);
            wemaApiAudit.setSuperAgentId(superAgent);
            wemaApiAudit.setSubAgentId(subAgent);
            wemaApiAudit.setWemaApiType(WemaApiType.ACC_NAME_ENQUIRY);
            wemaApiAuditRepo.save(wemaApiAudit);

        }catch (Exception e){
            logger.error("The Error Message: {}", e.getCause().getMessage());
        }
        return accNameEnquiryAPIresponse;
    }

    @Override
    public NipFundsTransferAPI transferFund(NipFundsTransferAPI nipFundsTransferAPI) {
        NipFundsTransferAPI nipFundsTransferAPIresponse = new NipFundsTransferAPI();
        //This is used to get the hash Code for Security Key using SHA512
        String tranReference = getTranRefrence();
        String when = getWhen();
        WemaApiAudit wemaApiAud = new WemaApiAudit();
        wemaApiAud.setTranReference(tranReference);
        wemaApiAud.setWhenn(when);
        wemaApiAud.setSubAgentId(subAgent);
        wemaApiAud.setSuperAgentId(superAgent);
        String shaCode = getOtherSHACode(wemaApiAud);
        logger.info("This is the SHA CODE {} ", shaCode);
        String sessionSecurityKey = Utility.sha512(shaCode);
        try{
            //This helps to Encrypt and Set the Class variables into the Xml payload
            Context context = new Context();
            context.setVariable("securitykey", sessionSecurityKey);
            context.setVariable("subagentid", wemaAESCipher.encrypt(subAgent));
            context.setVariable("superagentid", wemaAESCipher.encrypt(superAgent));
            context.setVariable("tranref", wemaAESCipher.encrypt(tranReference));
            context.setVariable("amount", wemaAESCipher.encrypt(nipFundsTransferAPI.getAmount()));
            context.setVariable("destbankcode", wemaAESCipher.encrypt(nipFundsTransferAPI.getDestinationBankCode()));
            context.setVariable("channelcode", wemaAESCipher.encrypt(nipFundsTransferAPI.getChannelCode()));
            context.setVariable("destaccno", wemaAESCipher.encrypt(nipFundsTransferAPI.getDestinationAccountNumber()));
            context.setVariable("nameresponse", wemaAESCipher.encrypt(nipFundsTransferAPI.getNameResponse()));
            context.setVariable("when", wemaAESCipher.encrypt(when));

            //This is the Soap Xml Payload
            String payload = templateEngine.process("wemabankxmls/fundtransfer.xml", context);
            String soapAction = "http://tempuri.org/NIPFundTransfer";
            //This method sends the HTTP post request
            String response = testClient.sendHttpRequest(payload,soapAction);

            boolean check = response.contains("<NIPFundTransferResponse");
            String responseCode= "";
            String responseDesc= "";
            String tranRef= "";
            String tranId= "";
            String tranType= "";
            String tranDate= "";
            String sessionId= "";
            if(check) {
                //These extracts the response, decrypts it and sets it into the class
                responseCode = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ResponseCode>", "</ResponseCode>"));
                nipFundsTransferAPIresponse.setResponseCode(responseCode);
                responseDesc = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ResponseDescription>", "</ResponseDescription>"));
                nipFundsTransferAPIresponse.setResponseDesc(responseDesc);
                boolean checkCode = responseCode.contains("00");
                if (checkCode) {
                    tranRef = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransReference>", "</TransReference>"));
                    nipFundsTransferAPIresponse.setTranReference(tranRef);
                    tranId = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionID>", "</TransactionID>"));
                    nipFundsTransferAPIresponse.setTranId(tranId);
                    tranType = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionType>", "</TransactionType>"));
                    nipFundsTransferAPIresponse.setTranType(tranType);
                    tranDate = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionDate>", "</TransactionDate>"));
                    nipFundsTransferAPIresponse.setTranDate(tranDate);
                    sessionId = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<Sessionid>", "</Sessionid>"));
                    nipFundsTransferAPIresponse.setSessionId(sessionId);
                }

                logger.info("This is the Funds Transfer Response {} ", responseCode + responseDesc);
            }else {
                MessageSource.getMessage("wema.fundstrans.error");
            }
            WemaApiAudit wemaApiAudit= new WemaApiAudit();
            modelMapper.map(nipFundsTransferAPI, wemaApiAudit);
            wemaApiAudit.setWhenn(when);
            wemaApiAudit.setTranReference(tranReference);
            wemaApiAudit.setSecuritySessionKey(sessionSecurityKey);
            wemaApiAudit.setResponseCode(responseCode);
            wemaApiAudit.setResponseDesc(responseDesc);
            wemaApiAudit.setTranRefResponse(tranRef);
            wemaApiAudit.setTranId(tranId);
            wemaApiAudit.setTranType(tranType);
            wemaApiAudit.setTranDate(tranDate);
            wemaApiAudit.setSessionId(sessionId);
            wemaApiAudit.setXmlRequestPayload(payload);
            wemaApiAudit.setXmlResponsePayload(response);
            wemaApiAudit.setSuperAgentId(superAgent);
            wemaApiAudit.setSubAgentId(subAgent);
            wemaApiAudit.setWemaApiType(WemaApiType.NIP_FUNDS_TRANSFER);
            wemaApiAuditRepo.save(wemaApiAudit);

        }catch (Exception e){
            logger.error("The Error Message: {}", e.getCause().getMessage());
        }
        return nipFundsTransferAPIresponse;
    }

    @Override
    public BasicDetailsAPI getTransactionStatus(BasicDetailsAPI basicDetailsAPI, String tranRefStatus) {
        BasicDetailsAPI basicDetailsAPIresponse = new BasicDetailsAPI();
        String tranReference = getTranRefrence();
        String when = getWhen();
        WemaApiAudit wemaApiAud = new WemaApiAudit();
        wemaApiAud.setTranReference(tranRefStatus);
        wemaApiAud.setWhenn(when);
        wemaApiAud.setSubAgentId(subAgent);
        wemaApiAud.setSuperAgentId(superAgent);
        String shaCode = getOtherSHACode(wemaApiAud);
        logger.info("This is the SHA CODE {} ", shaCode);
        String sessionSecurityKey = Utility.sha512(shaCode);
        try {
            //This is the Soap Xml Payload
            String payload = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <TransactionStatus xmlns=\"http://tempuri.org/\">\n" +
                    "      <SuperAgentID>"+ wemaAESCipher.encrypt(superAgent) +"</SuperAgentID>\n" +
                    "      <SubAgentID>"+ wemaAESCipher.encrypt(subAgent) +"</SubAgentID>\n" +
                    "      <Transactionreference>"+wemaAESCipher.encrypt(tranRefStatus)+"</Transactionreference>\n" +
                    "      <When>"+wemaAESCipher.encrypt(when)+"</When>\n" +
                    "      <SecuritySessionKey>"+sessionSecurityKey+"</SecuritySessionKey>\n" +
                    "    </TransactionStatus>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
            String soapAction = "http://tempuri.org/TransactionStatus";
            String response = testClient.sendHttpRequest(payload,soapAction);

            boolean check = response.contains("<TransactionStatusResponse");
            String responseCode= "";
            String responseDesc= "";
            String tranRef= "";
            String tranId= "";
            String tranType= "";
            String tranDate= "";
            String sessionId= "";
            if(check) {
                //These extracts the response, decrypts it and sets it into the class
                responseCode = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ReponseCode>", "</ReponseCode>"));
                basicDetailsAPIresponse.setResponseCode(responseCode);
                responseDesc = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ResponsDescription>", "</ResponsDescription>"));
                basicDetailsAPIresponse.setResponseDesc(responseDesc);
                boolean checkCode = responseCode.contains("00");
                if (checkCode) {
                    tranRef = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransReference>", "</TransReference>"));
                    basicDetailsAPIresponse.setTranReference(tranRef);
                    tranId = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionID>", "</TransactionID>"));
                    basicDetailsAPIresponse.setTranId(tranId);
                    tranType = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionType>", "</TransactionType>"));
                    basicDetailsAPIresponse.setTranType(tranType);
                    tranDate = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionDate>", "</TransactionDate>"));
                    basicDetailsAPIresponse.setTranDate(tranDate);
                }

                logger.info("This is the Transaction Status {} ", basicDetailsAPIresponse.getResponseCode() + basicDetailsAPIresponse.getResponseDesc());
            }else {
                MessageSource.getMessage("wema.transtatus.error");
            }
            WemaApiAudit wemaApiAudit= new WemaApiAudit();
            modelMapper.map(basicDetailsAPI, wemaApiAudit);
            wemaApiAudit.setWhenn(when);
            wemaApiAudit.setTranReference(tranReference);
            wemaApiAudit.setSecuritySessionKey(sessionSecurityKey);
            wemaApiAudit.setResponseCode(responseCode);
            wemaApiAudit.setResponseDesc(responseDesc);
            wemaApiAudit.setTranRefResponse(tranRef);
            wemaApiAudit.setTranId(tranId);
            wemaApiAudit.setTranType(tranType);
            wemaApiAudit.setTranDate(tranDate);
            wemaApiAudit.setXmlRequestPayload(payload);
            wemaApiAudit.setXmlResponsePayload(response);
            wemaApiAudit.setSuperAgentId(superAgent);
            wemaApiAudit.setSubAgentId(subAgent);
            wemaApiAudit.setWemaApiType(WemaApiType.TRANSACTION_STATUS);
            wemaApiAuditRepo.save(wemaApiAudit);
        }catch (Exception e){
            logger.error("Error Message {} ", e.getCause().getMessage());
        }
        return basicDetailsAPIresponse;
    }

    @Override
    public DepositAndWithdrawal makeDeposit(DepositAndWithdrawal depositAndWithdrawal) {
        DepositAndWithdrawal depositresponse = new DepositAndWithdrawal();
        //This is used to get the hash Code for Security Key using SHA512
        String tranReference = getTranRefrence();
        String when = getWhen();
        WemaApiAudit wemaApiAud = new WemaApiAudit();
        wemaApiAud.setTranReference(tranReference);
        wemaApiAud.setWhenn(when);
        wemaApiAud.setSubAgentId(subAgent);
        wemaApiAud.setSuperAgentId(superAgent);
        String shaCode = getOtherSHACode(wemaApiAud);
        logger.info("This is the SHA CODE {} ", shaCode);
        String sessionSecurityKey = Utility.sha512(shaCode);
        try{
            //This helps to Encrypt and Set the Class variables into the Xml payload
            Context context = new Context();
            context.setVariable("securitykey", sessionSecurityKey);
            context.setVariable("subagentid", wemaAESCipher.encrypt(subAgent));
            context.setVariable("superagentid", wemaAESCipher.encrypt(superAgent));
            context.setVariable("tranref", wemaAESCipher.encrypt(tranReference));
            context.setVariable("amount", wemaAESCipher.encrypt(depositAndWithdrawal.getAmount()));
            context.setVariable("custacccrebit", wemaAESCipher.encrypt(depositAndWithdrawal.getCustomerAccToCredit()));
            context.setVariable("when", wemaAESCipher.encrypt(when));

            //This is the Soap Xml Payload
            String payload = templateEngine.process("wemabankxmls/deposit.xml", context);
            String soapAction = "http://tempuri.org/WemaDeposit";
            //This method sends the HTTP post request
            String response = testClient.sendHttpRequest(payload,soapAction);

            boolean check = response.contains("<WemaDepositResponse");
            String responseCode= "";
            String responseDesc= "";
            String tranRef= "";
            String tranId= "";
            String tranType= "";
            String tranDate= "";
            if(check) {
                //These extracts the response, decrypts it and sets it into the class
                responseCode = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ReponseCode>", "</ReponseCode>"));
                depositresponse.setResponseCode(responseCode);
                responseDesc = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ResponsDescription>", "</ResponsDescription>"));
                depositresponse.setResponseDesc(responseDesc);
                boolean checkCode = responseCode.contains("00");
                if (checkCode) {
                    tranRef = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransReference>", "</TransReference>"));
                    depositresponse.setTranReference(tranRef);
                    tranId = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionID>", "</TransactionID>"));
                    depositresponse.setTranId(tranId);
                    tranType = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionType>", "</TransactionType>"));
                    depositresponse.setTranType(tranType);
                    tranDate = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionDate>", "</TransactionDate>"));
                    depositresponse.setTranDate(tranDate);
                }

                logger.info("This is Desposit Detials {} ", depositresponse.getResponseCode() + depositresponse.getResponseDesc());
            }else {
                MessageSource.getMessage("wema.deposit.error");
            }
            WemaApiAudit wemaApiAudit= new WemaApiAudit();
            modelMapper.map(depositAndWithdrawal, wemaApiAudit);
            wemaApiAudit.setWhenn(when);
            wemaApiAudit.setTranReference(tranReference);
            wemaApiAudit.setSecuritySessionKey(sessionSecurityKey);
            wemaApiAudit.setResponseCode(responseCode);
            wemaApiAudit.setResponseDesc(responseDesc);
            wemaApiAudit.setTranRefResponse(tranRef);
            wemaApiAudit.setTranId(tranId);
            wemaApiAudit.setTranType(tranType);
            wemaApiAudit.setTranDate(tranDate);
            wemaApiAudit.setXmlRequestPayload(payload);
            wemaApiAudit.setXmlResponsePayload(response);
            wemaApiAudit.setSuperAgentId(superAgent);
            wemaApiAudit.setSubAgentId(subAgent);
            wemaApiAudit.setWemaApiType(WemaApiType.DEPOSIT);
            wemaApiAuditRepo.save(wemaApiAudit);

        }catch (Exception e){
            logger.error("The Error Message: {}", e.getCause().getMessage());
        }
        return depositresponse;
    }

    @Override
    public DepositAndWithdrawal makeWithdrawal(DepositAndWithdrawal depositAndWithdrawal, String password, String token) {
        DepositAndWithdrawal withdrawalresponse = new DepositAndWithdrawal();
        //This is used to get the hash Code for Security Key using SHA512
        String tranReference = getTranRefrence();
        String when = getWhen();
        WemaApiAudit wemaApiAud = new WemaApiAudit();
        wemaApiAud.setTranReference(tranReference);
        wemaApiAud.setWhenn(when);
        wemaApiAud.setSubAgentId(subAgent);
        wemaApiAud.setSuperAgentId(superAgent);
        String shaCode = getOtherSHACode(wemaApiAud);
        logger.info("This is the SHA CODE {} ", shaCode);
        String sessionSecurityKey = Utility.sha512(shaCode);
        try{
            //This helps to Encrypt and Set the Class variables into the Xml payload
            Context context = new Context();
            context.setVariable("securitykey", sessionSecurityKey);
            context.setVariable("subagentid", wemaAESCipher.encrypt(subAgent));
            context.setVariable("superagentid", wemaAESCipher.encrypt(superAgent));
            context.setVariable("tranref", wemaAESCipher.encrypt(tranReference));
            context.setVariable("amount", wemaAESCipher.encrypt(depositAndWithdrawal.getAmount()));
            context.setVariable("custaccdebit", wemaAESCipher.encrypt(depositAndWithdrawal.getCustomerAccToDebit()));
            context.setVariable("password", wemaAESCipher.encrypt(password));
            context.setVariable("token", wemaAESCipher.encrypt(token));
            context.setVariable("when", wemaAESCipher.encrypt(when));

            //This is the Soap Xml Payload
            String payload = templateEngine.process("wemabankxmls/withdrawal.xml", context);
            String soapAction = "http://tempuri.org/WemaWithdrawal";
            //This method sends the HTTP post request
            String response = testClient.sendHttpRequest(payload,soapAction);

            boolean check = response.contains("<WemaWithdrawalResponse");
            String responseCode= "";
            String responseDesc= "";
            String tranRef= "";
            String tranId= "";
            String tranType= "";
            String tranDate= "";
            if(check) {
                //These extracts the response, decrypts it and sets it into the class
                responseCode = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ReponseCode>", "</ReponseCode>"));
                withdrawalresponse.setResponseCode(responseCode);
                responseDesc = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ResponsDescription>", "</ResponsDescription>"));
                withdrawalresponse.setResponseDesc(responseDesc);
                boolean checkCode = responseCode.contains("00");
                if (checkCode) {
                    tranRef = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransReference>", "</TransReference>"));
                    withdrawalresponse.setTranReference(tranRef);
                    tranId = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionID>", "</TransactionID>"));
                    withdrawalresponse.setTranId(tranId);
                    tranType = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionType>", "</TransactionType>"));
                    withdrawalresponse.setTranType(tranType);
                    tranDate = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<TransactionDate>", "</TransactionDate>"));
                    withdrawalresponse.setTranDate(tranDate);
                }

                logger.info("This is Withdrawal Details {} ", withdrawalresponse.getResponseCode() + withdrawalresponse.getResponseDesc());
            }else {
                MessageSource.getMessage("wema.deposit.error");
            }
            WemaApiAudit wemaApiAudit= new WemaApiAudit();
            modelMapper.map(depositAndWithdrawal, wemaApiAudit);
            wemaApiAudit.setToken(token);
            wemaApiAudit.setResponseCode(responseCode);
            wemaApiAudit.setResponseDesc(responseDesc);
            wemaApiAudit.setTranRefResponse(tranRef);
            wemaApiAudit.setTranId(tranId);
            wemaApiAudit.setTranType(tranType);
            wemaApiAudit.setTranDate(tranDate);
            wemaApiAudit.setXmlRequestPayload(payload);
            wemaApiAudit.setXmlResponsePayload(response);
            wemaApiAudit.setWhenn(when);
            wemaApiAudit.setTranReference(tranReference);
            wemaApiAudit.setSecuritySessionKey(sessionSecurityKey);
            wemaApiAudit.setSuperAgentId(superAgent);
            wemaApiAudit.setSubAgentId(subAgent);
            wemaApiAudit.setWemaApiType(WemaApiType.WITHDRAWAL);
            wemaApiAuditRepo.save(wemaApiAudit);

        }catch (Exception e){
            logger.error("The Error Message: {}", e.getCause().getMessage());
        }
        return withdrawalresponse;
    }

    @Override
    public DepositAndWithdrawal getWithdrawalToken(DepositAndWithdrawal depositAndWithdrawal) {
        DepositAndWithdrawal depositAndWithdrawalResponse = new DepositAndWithdrawal();
        String tranReference = getTranRefrence();
        String when = getWhen();
        WemaApiAudit wemaApiAud = new WemaApiAudit();
        wemaApiAud.setTranReference(tranReference);
        wemaApiAud.setWhenn(when);
        wemaApiAud.setSubAgentId(subAgent);
        wemaApiAud.setSuperAgentId(superAgent);
        String shaCode = getOtherSHACode(wemaApiAud);
        logger.info("This is the SHA CODE {} ", shaCode);
        String sessionSecurityKey = Utility.sha512(shaCode);
        try {
            //This is the Soap Xml Payload
            String payload = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <WithdrawalToken xmlns=\"http://tempuri.org/\">\n" +
                    "      <SuperAgentID>"+ wemaAESCipher.encrypt(superAgent) +"</SuperAgentID>\n" +
                    "      <SubAgentID>"+ wemaAESCipher.encrypt(subAgent) +"</SubAgentID>\n" +
                    "      <Transactionreference>"+ wemaAESCipher.encrypt(tranReference) +"</Transactionreference>\n" +
                    "      <When>"+ wemaAESCipher.encrypt(when) +"</When>\n" +
                    "      <SecuritySessionKey>"+sessionSecurityKey +"</SecuritySessionKey>\n" +
                    "      <CustomerAccountNo>"+ wemaAESCipher.encrypt(depositAndWithdrawal.getCustomerAccToDebit()) +"</CustomerAccountNo>\n" +
                    "      <Amount>"+ wemaAESCipher.encrypt(depositAndWithdrawal.getAmount()) +"</Amount>\n" +
                    "    </WithdrawalToken>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";
            String soapAction = "http://tempuri.org/WithdrawalToken";
            String response = testClient.sendHttpRequest(payload,soapAction);

            boolean check = response.contains("<WithdrawalTokenResponse ");
            String responseCode= "";
            String responseDesc= "";
            String accountName= "";
            String accountNo= "";
            String amount= "";
            if(check) {
                //These extracts the response, decrypts it and sets it into the class
                responseCode = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ReponseCode>", "</ReponseCode>"));
                depositAndWithdrawalResponse.setResponseCode(responseCode);
                responseDesc = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<ResponseDescription>", "</ResponseDescription>"));
                depositAndWithdrawalResponse.setResponseDesc(responseDesc);
                boolean checkCode = responseCode.contains("00");
                if (checkCode) {
                    accountName = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<AccountName>", "</AccountName>"));
                    depositAndWithdrawalResponse.setTranReference(accountName);
                    accountNo = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<AccountNumber>", "</AccountNumber>"));
                    depositAndWithdrawalResponse.setTranId(accountNo);
                    amount = wemaAESCipher.decrypt(StringUtils.substringBetween(response, "<Amount>", "</Amount>"));
                    depositAndWithdrawalResponse.setTranType(amount);
                }

                logger.info("This is the Withdrawal Token {} ", responseCode + responseDesc);
            }else {
                MessageSource.getMessage("wema.token.error");
            }
            WemaApiAudit wemaApiAudit= new WemaApiAudit();
            modelMapper.map(depositAndWithdrawal, wemaApiAudit);
            wemaApiAudit.setWhenn(when);
            wemaApiAudit.setTranReference(tranReference);
            wemaApiAudit.setSecuritySessionKey(sessionSecurityKey);
            wemaApiAudit.setResponseCode(responseCode);
            wemaApiAudit.setResponseDesc(responseDesc);
            wemaApiAudit.setAccountName(accountName);
            wemaApiAudit.setAccountNumber(accountNo);
            wemaApiAudit.setAmount(amount);
            wemaApiAudit.setXmlRequestPayload(payload);
            wemaApiAudit.setXmlResponsePayload(response);
            wemaApiAudit.setSuperAgentId(superAgent);
            wemaApiAudit.setSubAgentId(subAgent);
            wemaApiAudit.setWemaApiType(WemaApiType.WITHDRAWAL_TOKEN);
            wemaApiAuditRepo.save(wemaApiAudit);
        }catch (Exception e){
            logger.error("Error Message {} ", e.getCause().getMessage());
        }
        return depositAndWithdrawalResponse;
    }
}
