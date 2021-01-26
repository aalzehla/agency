package com._3line.gravity.freedom.financialInstitutions.gtbankapi.service.implementation;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.bankdetails.model.TransactionType;
import com._3line.gravity.freedom.commisioncharge.service.CommissionChargeService;
import com._3line.gravity.freedom.commisions.services.GravityDailyCommissionService;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.models.*;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.service.GTBApiAuditRepo;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.service.GTBankService;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.utils.DateFormatterGt;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.utils.GTBankHttpClient;
import com._3line.gravity.freedom.transactions.dtos.TransactionsDto;
import com._3line.gravity.freedom.transactions.service.TransactionService;
import com._3line.gravity.freedom.utility.Hash;
import com._3line.gravity.freedom.utility.Utility;
import com._3line.gravity.freedom.wallet.dto.WalletDTO;
import com._3line.gravity.freedom.wallet.repository.FreedomWalletRepository;
import com._3line.gravity.freedom.wallet.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class GTBankServiceImpl implements GTBankService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    com._3line.gravity.freedom.financialInstitutions.gtbankapi.utils.GTBankHttpClient GTBankHttpClient;

    @Autowired
    GTBApiAuditRepo gtbApiAuditRepo;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    TransactionService  transactionService;

    @Autowired
    WalletService walletService;

    @Qualifier("gravityDailyCommissionServiceImpl")
    @Autowired
    GravityDailyCommissionService dailyCommissionService;

    @Autowired
    CommissionChargeService commissionChargeService;

    @Value("${gtbank.accopening}")
    private String gtbAccOpenUrl;

    @Value("${gtbank.accopeningbvn}")
    private String gtbAccOpenBvnUrl;

    @Value("${gtbank.checkbvn}")
    private String gtbcheckbvn;

    @Value("${gtbank.channel}")
    private String channel;

    @Value("${gtbank.accesscode}")
    private String customerid;

    @Value("${gtbank.username}")
    private String username;

    @Value("${gtbank.password}")
    private String password;

    @Value("${gtbank.remark}")
    private String remarks;

    @Value("${gtbank.vendor}")
    private String vendor;

    @Value("${gtbank.vendorbank}")
    private String vendorBankCode;


    public String getAccountBalanceSHACode(AccBalRetrievalAPI accBalRetrievalAPI){
        String sHACode = customerid +username+password+ accBalRetrievalAPI.getAccountNumber();
        return sHACode;
    }


    @Override
    public AccBalRetrievalAPI retrieveAccountBalance(AccBalRetrievalAPI accBalRetrievalAPI) {
        //This Object is used to return the response message gotten
        AccBalRetrievalAPI accountBalRetrieval = new AccBalRetrievalAPI();

        //Used to generate the hash with the details passed
        String hashCode = Utility.sha512(getAccountBalanceSHACode(accBalRetrievalAPI));

        //Used to Format the XML tags
        boolean replaceTags = true;
        try{
            Context context = new Context();
            context.setVariable("customerid", customerid);
            context.setVariable("username", username);
            context.setVariable("password", password);
            context.setVariable("accountnumber", accBalRetrievalAPI.getAccountNumber());
            context.setVariable("hash", hashCode);

            String result = templateEngine.process("gtbankxmls/balinquiry.xml",context);
            String soapActionValue = "http://tempuri.org/GAPS_Uploader/FileUploader/AccountBalanceRetrieval";

            String responseMessage = GTBankHttpClient.sendHttpRequest(result,soapActionValue,replaceTags);
            logger.info("responseMessage: " + responseMessage);
            String charSequence = "<AccountBalanceRetrievalResult>";

            String avalBalance = "";
            String ledgerBalance = "";
            String resCode = "";
            String resMessage = "";
            boolean isSuccessful = responseMessage.contains(charSequence);
            if (isSuccessful){
                String responseBody = StringUtils.substringBetween(responseMessage, "<AccountBalanceRetrievalResponse>", "</AccountBalanceRetrievalResponse>");
                logger.info("this is the account Response XML {} ", responseBody);
                avalBalance = StringUtils.substringBetween(responseBody, "<Avail_Bal>","</Avail_Bal>");
                ledgerBalance = StringUtils.substringBetween(responseBody, "<Ledger_Bal>","</Ledger_Bal>");
                //Converting the retrieved Avaliable and Ledger Balance to Type Double
                String avalBal = avalBalance;
                String ledgerBal = ledgerBalance;
                accountBalRetrieval.setAvaliableBalance(avalBal);
                accountBalRetrieval.setLedgerBalance(ledgerBal);
                accountBalRetrieval.setCurrency(StringUtils.substringBetween(responseBody, "<Curr>","</Curr>"));
                accountBalRetrieval.setResponseCode(StringUtils.substringBetween(responseBody, "<responseCode>","</responseCode>"));
                accountBalRetrieval.setResponseDesc(StringUtils.substringBetween(responseBody, "<responseDesc>","</responseDesc>"));
                resCode = StringUtils.substringBetween(responseBody, "<responseCode>","</responseCode>");
                resMessage = StringUtils.substringBetween(responseBody, "<responseDesc>","</responseDesc>");
                accountBalRetrieval.setTime(StringUtils.substringBetween(responseBody, "<Time>","</Time>"));
                accountBalRetrieval.setAccountNumber(StringUtils.substringBetween(responseBody, "<accountnumber>","</accountnumber>"));
                accountBalRetrieval.setHash(StringUtils.substringBetween(responseBody, "<hash>","</hash>"));

            }else if(!isSuccessful){
                logger.error("Error Getting Account Bal");
            }
            GTBApiAudit gtbApiAudit = new GTBApiAudit();
            gtbApiAudit.setDateCreated(new Date());
            gtbApiAudit.setCustomerId(customerid);
            gtbApiAudit.setUserName(username);
            gtbApiAudit.setPassword(password);
            gtbApiAudit.setAccountNumber(accBalRetrievalAPI.getAccountNumber());
            gtbApiAudit.setCurrency(accBalRetrievalAPI.getCurrency());
            gtbApiAudit.setAvaliableBalance(avalBalance);
            gtbApiAudit.setLedgerBalance(ledgerBalance);
            gtbApiAudit.setResponseCode(resCode);
            gtbApiAudit.setResponseMessage(resMessage);
            gtbApiAudit.setTime(accBalRetrievalAPI.getTime());
            gtbApiAudit.setHash(hashCode);
            gtbApiAudit.setRequestPayload(result);
            gtbApiAudit.setResponsePayload(responseMessage);
            gtbApiAudit.setApiType(ApiType.ACCBALRETRIEVAL);
            gtbApiAuditRepo.save(gtbApiAudit);

            logger.info("This is the Accountbalance {} ", accountBalRetrieval.toString());

        } catch (Exception e) {
            logger.error("Error getting account statement", e);
        }

        return accountBalRetrieval;
    }


    public String getSingleTransferSHACode(SingleTransferAPI singleTransferAPI){
        String paymentDate = DateFormatterGt.format(new Date());
        String transactionDetails = "&lt;transactions&gt;&lt;transaction&gt;&lt;amount&gt;"+singleTransferAPI.getAmount()+"&lt;/amount&gt;&lt;paymentdate&gt;"+paymentDate+"&lt;/paymentdate&gt;&lt;reference&gt;"+singleTransferAPI.getReference()+"&lt;/reference&gt;&lt;remarks&gt;"+remarks+"&lt;/remarks&gt;&lt;vendorcode&gt;"+vendor+"&lt;/vendorcode&gt;&lt;vendorname&gt;"+vendor+"&lt;/vendorname&gt;&lt;vendoracctnumber&gt;"+singleTransferAPI.getVendoracctnumber()+"&lt;/vendoracctnumber&gt;&lt;vendorbankcode&gt;"+vendorBankCode+"&lt;/vendorbankcode&gt;&lt;customeracctnumber&gt;"+singleTransferAPI.getCustomeracctnumber()+"&lt;/customeracctnumber&gt;&lt;/transaction&gt;&lt;/transactions&gt;";
        String formattedTransDetail = transactionDetails.replace("&lt;","<").replace("&gt;",">");
        String sHACode = formattedTransDetail+customerid+username+password;
        logger.info("This is the String {} ", sHACode);
        return sHACode;
    }

//    public static void main(String[] args) {
//
//        String hashCode = Utility.sha512(new GTBankServiceImpl().getFACSHACode());
//        System.out.println("SHA string is :: "+hashCode);
//    }
//
//    private String getFACSHACode(){
//        String formattedTransDetail = "1030164258" + "08036277233" + "1563363";
//        String sHACode = formattedTransDetail+customerid+username+password;
//        logger.info("This is the String {} ", formattedTransDetail);
//        return formattedTransDetail;
//    }



    @Override
    public SingleTransferAPI performSingleTransfer(SingleTransferAPI singleTransferAPI) {
        Long ref = System.currentTimeMillis();
        String reference = String.valueOf(ref);
        singleTransferAPI.setReference(reference);
        SingleTransferAPI singleTransfer = new SingleTransferAPI();
        //Used to generate the hash with the details passed
        String hashCode = Utility.sha512(getSingleTransferSHACode(singleTransferAPI));
        logger.info("This is Original {} ", hashCode);
        String paymentDate = DateFormatterGt.format(new Date());
        //Used to Format the XML tags
        boolean replaceTags = false;
        try{
            Context context = new Context();
            context.setVariable("amount", singleTransferAPI.getAmount());
            context.setVariable("paymentdate", paymentDate);
            context.setVariable("reference", reference);
            context.setVariable("remarks", remarks);
            context.setVariable("vendorcode", vendor);
            context.setVariable("vendorname", vendor);
            context.setVariable("vendoracctnumber", singleTransferAPI.getVendoracctnumber());
            context.setVariable("vendorbankcode", vendorBankCode);
            context.setVariable("customeracctnumber", singleTransferAPI.getCustomeracctnumber());

            context.setVariable("hash", hashCode);

            String result = templateEngine.process("gtbankxmls/singletransfer.xml",context);
            String soapActionValue = "http://tempuri.org/GAPS_Uploader/FileUploader/SingleTransfers";

            //This is to stringify the raw data gotten
            String tranDets = StringUtils.substringBetween(result, "<transdetails>","</transdetails>");
            String tranDetails = tranDets.replace("<","&lt;").replace(">","&gt;");

            //The payload with transaction details in string format
            String payload = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:fil=\"http://tempuri.org/GAPS_Uploader/FileUploader\">\n" +
                    "<soapenv:Header/>\n" +
                    "<soapenv:Body>\n" +
                    "    <fil:SingleTransfers>\n" +
                    "        <fil:xmlRequest>\n" +
                    "                <![CDATA[" + "<SingleTransfers>" + "<transdetails>"+tranDetails+"</transdetails>"+ "<accesscode>"+customerid+"</accesscode>"+ "<username>"+username+"</username>"+ "<password>"+password+"</password>"+ "<hash>"+hashCode+"</hash>"+ "</SingleTransfers>" + "]]>\n" +
                    "        </fil:xmlRequest>\n" +
                    "    </fil:SingleTransfers>\n" +
                    "</soapenv:Body>\n" +
                    "</soapenv:Envelope>";

            String responseMessage = GTBankHttpClient.sendHttpRequest(payload,soapActionValue,replaceTags);
            logger.info("responseMessage: " + responseMessage);
            String charSequence = "<SingleTransfersResult>";
            String responseCode = "";
            String resMessage = "";

            boolean isSuccessful = responseMessage.contains(charSequence);
            if (isSuccessful){
                String responseBody = StringUtils.substringBetween(responseMessage, "<SingleTransfersResult>", "</SingleTransfersResult>");
                logger.info("this is the account Response XML {} ", responseBody);
                singleTransfer.setResponseCode(StringUtils.substringBetween(responseBody, "<Code>","</Code>"));
                responseCode = StringUtils.substringBetween(responseBody, "<Code>","</Code>");
                singleTransfer.setResponseMessage(StringUtils.substringBetween(responseBody, "<Message>","</Message>"));
                resMessage = StringUtils.substringBetween(responseBody, "<Message>","</Message>");
            }
            logger.info("This is the Response Message {} ", singleTransfer.toString());
            if(responseCode.equalsIgnoreCase("1100")){
                TransactionRequery transactionReq = new TransactionRequery();
                transactionReq.setReference(reference);
                TransactionRequery transactionRequery = getTransactionRequery(transactionReq);
                singleTransfer.setResponseCode(transactionRequery.getResponseCode());
                singleTransfer.setResponseMessage(transactionRequery.getResponseMessage());
            }
            GTBApiAudit gtbApiAudit = new GTBApiAudit();
            gtbApiAudit.setDateCreated(new Date());
            gtbApiAudit.setCustomerId(customerid);
            gtbApiAudit.setUserName(username);
            gtbApiAudit.setPassword(password);
            gtbApiAudit.setRemarks(remarks);
            gtbApiAudit.setReference(reference);
            gtbApiAudit.setVendoracctnumber(singleTransferAPI.getVendoracctnumber());
            gtbApiAudit.setVendoraccttype(vendor);
            gtbApiAudit.setVendorbankcode(vendorBankCode);
            gtbApiAudit.setVendorcode(vendor);
            gtbApiAudit.setVendorname(vendor);
            gtbApiAudit.setAmount(singleTransferAPI.getAmount());
            gtbApiAudit.setPaymentdate(paymentDate);
            gtbApiAudit.setCustomeracctnumber(singleTransferAPI.getCustomeracctnumber());
            gtbApiAudit.setResponseCode(responseCode);
            gtbApiAudit.setResponseMessage(resMessage);
            gtbApiAudit.setHash(hashCode);
            gtbApiAudit.setRequestPayload(payload);
            gtbApiAudit.setResponsePayload(responseMessage);
            gtbApiAudit.setApiType(ApiType.SINGLETRANSFER);
            gtbApiAuditRepo.save(gtbApiAudit);

        } catch (Exception e) {
            logger.error("Error doing Single GT Bank Transfer", e);
        }

        return singleTransfer;
    }

    @Override
    public FreedomGTB performFreedomTransfer(FreedomGTB freedomGTB) {

        try {
            SingleTransferAPI singleTransferAPI = new SingleTransferAPI();
            singleTransferAPI.setAmount(freedomGTB.getAmount());
            singleTransferAPI.setVendoracctnumber(freedomGTB.getAccountTo());
            singleTransferAPI.setCustomeracctnumber(freedomGTB.getAccountFrom());
            SingleTransferAPI singleTransfer = performSingleTransfer(singleTransferAPI);
            FreedomGTB freedom = new FreedomGTB();
            freedom.setResponseCode(singleTransfer.getResponseCode());
            freedom.setResponseMessage(singleTransfer.getResponseMessage());
        }catch (Exception e) {
            logger.error("Error doing Freedom GT Bank Transfer", e);
        }
        return freedomGTB;
    }

    @Override
    public TransactionRequery getTransactionRequery(TransactionRequery transactionRequery) {
        //This Object is used to return the response message gotten
        TransactionRequery transactionReq = new TransactionRequery();

        boolean replaceTags = false;
        try{
            String soapActionValue = "http://tempuri.org/GAPS_Uploader/FileUploader/TransactionRequery";

            String payload = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:fil=\"http://tempuri.org/GAPS_Uploader/FileUploader\">\n" +
                    "   <soapenv:Header/>\n" +
                    "   <soapenv:Body>\n" +
                    "      <fil:TransactionRequery>\n" +
                    "         <fil:xmlstring>&lt;TransactionRequeryRequest&gt;&lt;TransRef&gt;"+transactionRequery.getReference()+"&lt;/TransRef&gt;&lt;/TransactionRequeryRequest&gt;</fil:xmlstring>\n" +
                    "         <fil:customerid>"+customerid+"</fil:customerid>\n" +
                    "         <fil:username>"+username+"</fil:username>\n" +
                    "         <fil:password>"+password+"</fil:password>\n" +
                    "      </fil:TransactionRequery>\n" +
                    "   </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

            String responseMessage = GTBankHttpClient.sendHttpRequest(payload,soapActionValue,replaceTags);
            logger.info("responseMessage: " + responseMessage);
            String charSequence = "<TransactionRequeryResult>";
            String resCode = "";
            String resMessage = "";

            boolean isSuccessful = responseMessage.contains(charSequence);
            if (isSuccessful){
                String responseBody = StringUtils.substringBetween(responseMessage, "<TransactionRequeryResult>", "</TransactionRequeryResult>");
                logger.info("this is the account Response XML {} ", responseBody);
                transactionReq.setResponseCode(StringUtils.substringBetween(responseBody, "<Code>","</Code>"));
                transactionReq.setResponseMessage(StringUtils.substringBetween(responseBody, "<Message>","</Message>"));
                resCode = StringUtils.substringBetween(responseBody, "<Code>","</Code>");
                resMessage = StringUtils.substringBetween(responseBody, "<Message>","</Message>");

            }else if(!isSuccessful){
                logger.error("Error Getting Account Bal");
            }
            GTBApiAudit gtbApiAudit = new GTBApiAudit();
            gtbApiAudit.setDateCreated(new Date());
            gtbApiAudit.setCustomerId(customerid);
            gtbApiAudit.setUserName(username);
            gtbApiAudit.setPassword(password);
            gtbApiAudit.setReference(transactionRequery.getReference());
            gtbApiAudit.setResponseCode(resCode);
            gtbApiAudit.setResponseMessage(resMessage);
            gtbApiAudit.setRequestPayload(payload);
            gtbApiAudit.setResponsePayload(responseMessage);
            gtbApiAudit.setApiType(ApiType.TRANSACTIONREQUERY);
            gtbApiAuditRepo.save(gtbApiAudit);
        } catch (Exception e) {
            logger.error("Error getting Tran Requery", e);
        }

        return transactionReq;
    }

    @Override
    public GetBVNDetailsResponse checkBVN(GetBVNDetailsRequest getBVNDetailsRequest) {
        GetBVNDetailsResponse getBVNDetailsResponse = new GetBVNDetailsResponse();
        logger.info("This is the BVN entered {} ", getBVNDetailsRequest.getBvn());
        Gson gson = new Gson() ;
        String payload = gson.toJson(getBVNDetailsRequest);
        logger.info("payload {}", payload);
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpEntity httpEntity = new ByteArrayEntity(payload.getBytes(StandardCharsets.UTF_8));
            logger.info("Received http Entity is " + httpEntity);
            logger.info("The GTB Check Bvn Url {}", gtbcheckbvn);
            HttpPost httpPost = new HttpPost(gtbcheckbvn);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(httpEntity);
            HttpResponse response = client.execute(httpPost);
            logger.info("Received response Check BVN {}", response);
            String responseMessage = EntityUtils.toString(response.getEntity());
            logger.info("Received response Check BVN Message is " + responseMessage);

            getBVNDetailsResponse = objectMapper.readValue(responseMessage , GetBVNDetailsResponse.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return getBVNDetailsResponse;
    }

    @Override
    public String checkBVNResponse(AccOpeningGTB accOpeningGTB) {
        GetBVNDetailsRequest getBVNDetailsRequest = new GetBVNDetailsRequest();
        getBVNDetailsRequest.setBvn(accOpeningGTB.getBvn());
        getBVNDetailsRequest.setChannel(channel);
        getBVNDetailsRequest.setUserID(accOpeningGTB.getUserId());

        GetBVNDetailsResponse getBVNDetailsResponse = checkBVN(getBVNDetailsRequest);
        String response = getBVNDetailsResponse.getResponseCode();
        logger.info("The Response From get BVN {}", response);
        return response;
    }

    public String checkForBvn(String bvn, String bvnResponse){
        String gtbUrl = "";
        if (bvn == null || bvn.isEmpty()) {
            gtbUrl = gtbAccOpenUrl;
        }else {
            if(bvnResponse!=null && bvnResponse.equals("00")){
                gtbUrl = gtbAccOpenBvnUrl;
            }else{
                gtbUrl = gtbAccOpenUrl;
            }
        }
        return gtbUrl;
    }

    public String gtDateTime(){
        Date date = new Date();
        String newDate = DateFormatterGt.gtFormat(date);
        logger.info("THIS IS Date {} ", newDate);
        return newDate;
    }

    @Override
    public AccOpeningGTB openGTBAccount(AccOpeningGTB accOpeningGTB) {
        AccOpeningGTB accOpeningGTBresponse = new AccOpeningGTB();
        String bvn = accOpeningGTB.getBvn();
        logger.info("This is the Account Details {}", accOpeningGTB.toString());
        String checkBVNResponse = null;
        if(accOpeningGTB.getBvn()!=null && accOpeningGTB.getBvn().equals(""))
        {
            checkBVNResponse = checkBVNResponse(accOpeningGTB);
        }
        String requestId = channel + gtDateTime();
        Gson gson = new Gson() ;
        accOpeningGTB.setChannel(channel);
        accOpeningGTB.setUserId(channel);
        accOpeningGTB.setCustomerNumber(channel);
        accOpeningGTB.setRequestId(requestId);
        String payload = gson.toJson(accOpeningGTB);
        logger.info("payload before json casting {}", accOpeningGTB);
        logger.info("payload after {}", payload);
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpEntity httpEntity = new ByteArrayEntity(payload.getBytes(StandardCharsets.UTF_8));
            logger.info("Received http Entity is " + httpEntity);
            //To Switch Url if Bvn Exists or Doesn't Exist
            String gtbUrl = checkForBvn(bvn, checkBVNResponse);
            logger.info("The GTB Url {}", gtbUrl);
            HttpPost httpPost = new HttpPost(gtbUrl);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(httpEntity);
            HttpResponse response = client.execute(httpPost);
            logger.info("Received response1 GTB Account Opening {}", response);
            String responseMessage = EntityUtils.toString(response.getEntity());
            logger.info("Received response Message is " + responseMessage);

            accOpeningGTBresponse = objectMapper.readValue(responseMessage , AccOpeningGTB.class);
            //Auditing the details
            GTBApiAudit gtbApiAudit = new GTBApiAudit();
            gtbApiAudit.setDateCreated(new Date());
            gtbApiAudit.setFirstName(accOpeningGTB.getFirstName());
            gtbApiAudit.setLastName(accOpeningGTB.getLastName());
            gtbApiAudit.setGender(accOpeningGTB.getGender());
            gtbApiAudit.setDoB(accOpeningGTB.getDoB());
            gtbApiAudit.setAddress(accOpeningGTB.getAddress());
            gtbApiAudit.setMotherMaiden(accOpeningGTB.getMotherMaiden());
            gtbApiAudit.setBvn(accOpeningGTB.getBvn());
            gtbApiAudit.setChannel(channel);
            gtbApiAudit.setUserId(channel);
            gtbApiAudit.setCustomerNumber(channel);
            gtbApiAudit.setSessionId(accOpeningGTB.getSessionId());
            gtbApiAudit.setEmail(accOpeningGTB.getEmail());
            gtbApiAudit.setNuban(accOpeningGTBresponse.getNuban());
            gtbApiAudit.setMobileNo(accOpeningGTB.getMobileNo());
            gtbApiAudit.setPassword(accOpeningGTBresponse.getPassword());
            gtbApiAudit.setRequestId(requestId);
            gtbApiAudit.setResponseCode(accOpeningGTBresponse.getResponseCode());
            gtbApiAudit.setResponseMessage(accOpeningGTBresponse.getResponseDescription());
            gtbApiAudit.setRequestPayload(payload);
            gtbApiAudit.setResponsePayload(responseMessage);
            gtbApiAudit.setApiType(ApiType.ACCOUNTOPENING);
            gtbApiAuditRepo.save(gtbApiAudit);
        }catch (Exception e){
            e.printStackTrace();
        }
        return accOpeningGTBresponse;
    }

    @Override
    public FACValidateResponse validateFacCode(FACValidate facValidate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        FACValidateResponse facTranDetail = null;
        Hash hash = new Hash();
        String hashedParams = null;
        String responseMessage="";
        facValidate.setUniqueId("312667");
        try {
            hashedParams = hash.generateSHA512(facValidate.getFacCode()+facValidate.getMobileNumber()+facValidate.getUniqueId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        facValidate.setHash(hashedParams);
        String payload = "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <Body>\n" +
                "        <FACValidityRequest xmlns=\"http://tempuri.org/\">\n" +
                "            <!-- Optional -->\n" +
                "            <facreq>\n" +
                "                <FACCode>"+facValidate.getFacCode()+"</FACCode>\n" +
                "                <MobileNumber>"+facValidate.getMobileNumber()+"</MobileNumber>\n" +
                "                <UniqueId>"+facValidate.getUniqueId()+"</UniqueId>\n" +
                "                <Hash>"+facValidate.getHash()+"</Hash>\n" +
                "            </facreq>\n" +
                "        </FACValidityRequest>\n" +
                "    </Body>\n" +
                "</Envelope>";
        logger.info("Payload is {}", payload);
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpEntity httpEntity = new ByteArrayEntity(payload.getBytes(StandardCharsets.UTF_8));

            HttpPost httpPost = new HttpPost("http://gtweb.gtbank.com/chuks/GTRescueAgentCashout_WebService/WebService.asmx");
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.setEntity(httpEntity);
            HttpResponse response = client.execute(httpPost);
            logger.info("Received response1 GTB FAC validation {}", response);
            responseMessage = EntityUtils.toString(response.getEntity());

            logger.info("Received response Message is " + responseMessage);

        }catch (Exception e){
            e.printStackTrace();
        }

        String responseCode = StringUtils.substringBetween(responseMessage,"<ResponseCode>","</ResponseCode>");
        if(responseCode.equals("00")){
            facTranDetail = new FACValidateResponse();
            facTranDetail.setFacCode(facValidate.getFacCode());
        }else{
            throw new GravityException(StringUtils.substringBetween(responseMessage,"<ResponseDesc>","</ResponseDesc>"));
        }

        return facTranDetail;
    }

    @Override
    public FACTranResponse cashOut(FACTranRequest facTranRequest) throws GravityException {

        Agents agent = jwtUtility.getCurrentAgent();

        WalletDTO walletDTO = walletService.getWalletByNumber(agent.getWalletNumber());

        Double fee = 0.00;//commissionChargeService.getCommissionForAmount(Double.valueOf(facTranRequest.getAmount()),TransactionType.WITHDRAWAL)

        TransactionsDto transactionsDto = new TransactionsDto();
        transactionsDto.setTranDate(new Date());
        transactionsDto.setStatus((short) 0);
        transactionsDto.setInnitiatorId(agent.getId());
        transactionsDto.setApproval(0);
        transactionsDto.setAmount(Double.valueOf(facTranRequest.getAmount()) + fee);
        transactionsDto.setBalancebefore(walletDTO.getAvailableBalance());
        transactionsDto.setBeneficiary(facTranRequest.getMobileNumber());
        transactionsDto.setTransactionType("Withdrawal");
        transactionsDto.setTransactionTypeDescription("Cardless Withdrawal");
        transactionsDto.setStatusdescription("PENDING");
        transactionsDto.setDescription("GT Cashout withdrawal");
        transactionsDto.setAgentName(agent.getUsername());
        transactionsDto.setCustomerName("");
        transactionsDto.setMedia("MOBILE");

        transactionsDto = transactionService.addTransaction(transactionsDto);


        Hash hash = new Hash();
        String hashedParams = null;
        FACTranResponse facTranResponse = null;
        String responseMessage = "";
        try {
            hashedParams = hash.generateSHA512("Agent100"+"Agent100"+"100"+"1001"
                    +facTranRequest.getFacCode()+facTranRequest.getMobileNumber()+facTranRequest.getAmount());
        } catch (Exception e) {
            e.printStackTrace();
        }

        facTranRequest.setHash(hashedParams);
        String payload = "<Envelope xmlns=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <Body>\n" +
                "        <CashOutPaymentRequest xmlns=\"http://tempuri.org/\">\n" +
                "            <!-- Optional -->\n" +
                "            <req>\n" +
                "                <MerchName>LINE</MerchName>\n" +
                "                <UniqueID>312681</UniqueID>\n" +
                "                <ServiceUserID>Agent100</ServiceUserID>\n" +
                "                <ServicePassword>Agent100</ServicePassword>\n" +
                "                <AgentID>100</AgentID>\n" +
                "                <TerminalID>1001</TerminalID>\n" +
                "                <FACCode>"+facTranRequest.getFacCode()+"</FACCode>\n" +
                "                <MobileNumber>"+facTranRequest.getMobileNumber()+"</MobileNumber>\n" +
                "                <Amount>"+facTranRequest.getAmount()+"</Amount>\n" +
                "                <Hash>"+facTranRequest.getHash()+"</Hash>\n" +
                "            </req>\n" +
                "        </CashOutPaymentRequest>\n" +
                "    </Body>\n" +
                "</Envelope>";
        logger.info("Payload is {}", payload);
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpEntity httpEntity = new ByteArrayEntity(payload.getBytes(StandardCharsets.UTF_8));

            HttpPost httpPost = new HttpPost("http://gtweb.gtbank.com/chuks/GTRescueAgentCashout_WebService/WebService.asmx");

            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.setEntity(httpEntity);
            HttpResponse response = client.execute(httpPost);
            logger.info("Received response1 GTB Cash Out {}", response);
            responseMessage = EntityUtils.toString(response.getEntity());
            logger.info("Received Message is " + responseMessage);



        }catch (Exception e){
            e.printStackTrace();
        }

        String responseCode = StringUtils.substringBetween(responseMessage,"<ResponseCode>","</ResponseCode>");

        if(responseCode.equals("00")){

            facTranResponse = new FACTranResponse();
            facTranResponse.setSenderName(StringUtils.substringBetween(responseMessage,"<SenderName>","</SenderName>"));
            facTranResponse.setAmount(StringUtils.substringBetween(responseMessage,"<Amount>","</Amount>"));
            facTranResponse.setTransRef(StringUtils.substringBetween(responseMessage,"<TransRef>","</TransRef>"));

            //update transaction
            transactionsDto.setStatus((short) 1);
            transactionsDto.setStatusdescription("SUCCESS");
            transactionsDto.setTransactionReference(facTranResponse.getTransRef());

            transactionService.updateTransaction(transactionsDto);

            walletService.creditWallet(String.valueOf(transactionsDto.getTranId()),agent.getWalletNumber() , transactionsDto.getAmount() ,"MOBILE","WITHDRAWAL");
//            dailyCommissionService.generateDepositCommission(agent.getBankCode(), transactionsDto.getAmount().toString(), transactionsDto.getTranId(), agent.getUsername(), TransactionType.WITHDRAWAL);

        }else{
            //handle if responsemessage is empty
            //update transaction
            transactionsDto.setStatus((short) 2);
            transactionService.updateTransaction(transactionsDto);

            throw new GravityException(StringUtils.substringBetween(responseMessage,"<ResponseDesc>","</ResponseDesc>"));
        }


        return facTranResponse;
    }


}
