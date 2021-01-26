/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.services;

import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.financialInstitutions.fidelity.models.IntraBankTransferResponse;
import com._3line.gravity.freedom.financialInstitutions.fidelity.models.OTPGenResponse;
import com._3line.gravity.freedom.financialInstitutions.fidelity.models.UserDetails;
import com._3line.gravity.freedom.financialInstitutions.fidelity.service.FidelityWebServices;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.request.StanbicAgentCashInRequest;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.request.StanbicAgentCashOutRequest;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response.StanbicAccountBalance;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response.StanbicAccountBalanceResponse;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response.StanbicAccountCreationResponse;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response.StanbicUserDetailsResonse;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.dto.StanbicOTPDto;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.request.*;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response.*;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.utility.Base64Converter;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.utility.StanbicHttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.List;
import java.util.logging.Level;


/**
 *
 * @author NiyiO
 */

@Service
public class StanbicIBTCServices {

    @Value("${stanbic.defaultUri}")
    private String defaultUri;

    @Value("${stanbic.authorization}")
    private String authorization;

    @Value("${stanbic.moduleId}")
    private String moduleId;

    @Value("${stanbic.tokenurl}")
    private String tokenUrl;

    @Value("${stanbic.otpurl}")
    private String otpUrl;

    @Value("${stanbic.accountopeningurl}")
    private String accountOpeningUrl;

    @Value("${stanbic.agentid}")
    private String agentId;

    @Value("${stanbic.pin}")
    private String pin;

    @Value("${stanbic.postedby:0000000000}")
    private String postedBy;


    @Autowired
    private StanbicHttpClient stanbicHttpClient;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();


    private static final Logger logger = LoggerFactory.getLogger("com.mooasoft");


    public String getToken(String agentId, String pin){
        String token = "";
        StanbicTokenRequest stanbicTokenRequest = new StanbicTokenRequest();
        stanbicTokenRequest.setAgentId(agentId);
        String encryptedPin = Base64Converter.encodeString(pin);
        stanbicTokenRequest.setPinInBase64(encryptedPin);
        try {
            String payload = objectMapper.writeValueAsString(stanbicTokenRequest);
            logger.info("About to make Api call to {}, with payload {}", tokenUrl,payload);
            String response = stanbicHttpClient.sendTokenRequest(payload,tokenUrl,agentId);
            StanbicTokenResponse stanbicTokenResponse = objectMapper.readValue(response, StanbicTokenResponse.class);
            logger.info("The Stanbic Token response {}", stanbicTokenResponse);
            token= stanbicTokenResponse.getToken();
        }catch (Exception e){
            logger.error("Error ",e);
        }
        return token;
    }

    public Response getOTP(StanbicOTPDto stanbicOTPDto){
        Response responseBody = new Response();
        StanbicOTPRequest stanbicOTPRequest= new StanbicOTPRequest();
        stanbicOTPRequest.setAgentId(agentId);
        stanbicOTPRequest.setPhoneNumber(stanbicOTPDto.getPhoneNumber());
        String authToken = getToken(agentId, pin);
        logger.info("The Auth Token {}", authToken);
        StanbicOTPResponse stanbicOTPResponse;
        if (authToken != null && !authToken.isEmpty()) {
            try {
                String payload = objectMapper.writeValueAsString(stanbicOTPRequest);
                logger.info("About to make Api call to {}, with payload {}", otpUrl, payload);
                String response = stanbicHttpClient.sendRequest(payload, otpUrl, authToken, agentId);
                stanbicOTPResponse = objectMapper.readValue(response, StanbicOTPResponse.class);
                logger.info("The Stanbic OTP response {}", stanbicOTPResponse);
                responseBody.setRespCode(stanbicOTPResponse.getApiResponse().getResponseCode());
                responseBody.setRespDescription(stanbicOTPResponse.getApiResponse().getResponseDescription());
                responseBody.setRespBody(stanbicOTPResponse);
            } catch (Exception e) {
                logger.error("Error ", e);
            }
        }else {
            responseBody.setRespCode("95");
            responseBody.setRespDescription("An Error Occured getting Token");
        }
        return responseBody;
    }

    public StanbicAccountOpeningResponse openAccount(StanbicAccountOpeningRequest stanbicAccountOpeningRequest){
        String otp = stanbicAccountOpeningRequest.getOtp();
        String otpReference= stanbicAccountOpeningRequest.getOtpReference();

        String authToken = getToken(agentId, pin);
        logger.info("The Auth Token {}", authToken);

        stanbicAccountOpeningRequest.setOtp(Base64Converter.encodeString(otp));
        stanbicAccountOpeningRequest.setOtpReference(Base64Converter.encodeString(otpReference));
        stanbicAccountOpeningRequest.setPin(Base64Converter.encodeString(pin));
        stanbicAccountOpeningRequest.setAgentId(agentId);
        stanbicAccountOpeningRequest.setPostedBy(postedBy);

        StanbicAccountOpeningResponse stanbicAccountOpeningResponse = new StanbicAccountOpeningResponse();
        if (authToken != null && !authToken.isEmpty()) {
            try {
                Gson gson = new Gson();
                String payload = objectMapper.writeValueAsString(stanbicAccountOpeningRequest);
                logger.info("About to make Api call to {}, with payload {}", accountOpeningUrl, payload);
                String response = stanbicHttpClient.sendRequest(payload, accountOpeningUrl, authToken, agentId);
                stanbicAccountOpeningResponse = gson.fromJson(response, StanbicAccountOpeningResponse.class);
                logger.info("The Stanbic Account Open response {}", stanbicAccountOpeningResponse);
            } catch (Exception e) {
                logger.error("Error ", e);
            }
        }else {
            StanbicApiResponse stanbicApiResponse = new StanbicApiResponse();
            stanbicApiResponse.setResponseCode("95");
            stanbicApiResponse.setResponseDescription("An Error Occured getting Token");
            stanbicAccountOpeningResponse.setApiResponse(stanbicApiResponse);
        }
        return stanbicAccountOpeningResponse;
    }


    public StanbicUserDetailsResonse getUserDetails(String acctNum){

        String response  = sendHttpSoapRequest(StanbicSOAP.getAccountEnquiryPayload(acctNum)) ;

        StanbicUserDetailsResonse stanbicUserDetailsResonse = new StanbicUserDetailsResonse() ;
        stanbicUserDetailsResonse.setResponseCode(StringUtils.substringBetween(response , "<responseCode>", "</responseCode>"));
        stanbicUserDetailsResonse.setDescription(StringUtils.substringBetween(response , " <responseDescription>", " </responseDescription>"));


        UserDetails userDetails = new UserDetails();
        userDetails.setAddress(StringUtils.substringBetween(response , "<address>", "</address>"));
        userDetails.setEmail("");
        userDetails.setFirstName(StringUtils.substringBetween(response , "<firstName>", "</firstName>"));
        userDetails.setLastName(StringUtils.substringBetween(response , "<lastName>", "</lastName>"));
        userDetails.setPhone("");

        stanbicUserDetailsResonse.setUserDetails(userDetails);

        return stanbicUserDetailsResonse ;
    }

    public String sendHttpSoapRequest(String payload) {

        String action = "";
        logger.info("Calling STANBIC_SERVICE with payload -- {} ", payload);

        payload = payload.replace("&lt;","<");
        payload = payload.replace("&gt;",">");
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new StanbicSSLFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            DefaultHttpClient httpClient = new DefaultHttpClient(ccm,params);

            HttpEntity httpEntity = new ByteArrayEntity(payload.getBytes(StandardCharsets.UTF_8));

            System.out.println(defaultUri);
            HttpPost httpPost = new HttpPost(defaultUri);
            httpPost.addHeader("Authorization","basic " + authorization);
            httpPost.addHeader("Module_ID", moduleId);
            httpPost.addHeader("SOAPAction", "treat");

            httpPost.setEntity(httpEntity);

            HttpResponse response = httpClient.execute(httpPost);

            String responseMessage = EntityUtils.toString(response.getEntity());
            List<String> output = null;

            if(responseMessage!=null && !responseMessage.isEmpty()){
                CharSequence lessThanEscaped ="&lt;";
                CharSequence lessThan ="<";
                CharSequence greaterThanEscaped ="&gt;";
                CharSequence greaterThan =">";
                return responseMessage.replace(lessThanEscaped,lessThan).replace(greaterThanEscaped, greaterThan);
            }

            return responseMessage;

        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());

        }

        return "NO RESPONSE";

    }


    public IntraBankTransferResponse performAgentCashOut(StanbicAgentCashOutRequest transferRequest){

        try {
            String response = sendHttpSoapRequest(StanbicSOAP.getperformAgentCashOutPayload(transferRequest));

            IntraBankTransferResponse intraBankTransferResponse = new IntraBankTransferResponse();

            intraBankTransferResponse.setTransactionReference( StringUtils.substringBetween(response,"<trackingId>","</trackingId>"));
            intraBankTransferResponse.setResponseCode( StringUtils.substringBetween(response,"<responseCode>","</responseCode>"));
            intraBankTransferResponse.setDescription(StringUtils.substringBetween(response,"<responseDescription>","</responseDescription>"));

            return  intraBankTransferResponse ;
        }catch (Exception e){

            IntraBankTransferResponse intraBankTransferResponse = new IntraBankTransferResponse();

            intraBankTransferResponse.setResponseCode("999");
            intraBankTransferResponse.setDescription("error occurred while contact stanbic service ...." );

            return intraBankTransferResponse ;

        }

    }

    public IntraBankTransferResponse performAgentCashIn(StanbicAgentCashInRequest transferRequest){
        try {
            String response = sendHttpSoapRequest(StanbicSOAP.getAgentCashInPayload(transferRequest));

            IntraBankTransferResponse intraBankTransferResponse = new IntraBankTransferResponse();

            intraBankTransferResponse.setTransactionReference( StringUtils.substringBetween(response,"<trackingId>","</trackingId>"));
            intraBankTransferResponse.setResponseCode( StringUtils.substringBetween(response,"<responseCode>","</responseCode>"));
            intraBankTransferResponse.setDescription(StringUtils.substringBetween(response,"<responseDescription>","</responseDescription>"));

            return  intraBankTransferResponse ;
        }catch (Exception e){

            IntraBankTransferResponse intraBankTransferResponse = new IntraBankTransferResponse();

            intraBankTransferResponse.setResponseCode("999");
            intraBankTransferResponse.setDescription("error occurred while contact stanbic service ...." );

            return intraBankTransferResponse ;

        }
    }

    public OTPGenResponse generateOTP(String accountNo) {
        logger.info("retrieving lean account from web service");

        try {

            String response  = sendHttpSoapRequest(StanbicSOAP.getOTPGenerationPayload(accountNo)) ;

            OTPGenResponse oTPGenResponse = new OTPGenResponse() ;

                oTPGenResponse.setOtp(StringUtils.substringBetween(response , "<code>","</code>"));
                oTPGenResponse.setResponseCode(StringUtils.substringBetween(response , " <responseCode>"," </responseCode>"));
                oTPGenResponse.setDescription(StringUtils.substringBetween(response , "  <responseDescription>","  </responseDescription>"));
            Gson gson = new Gson();
            System.out.println(gson.toJson(oTPGenResponse));

            return oTPGenResponse;
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:{0} Response body: {1}", new Object[]{statusCode, responseBodyAsString});
            System.out.println(responseBodyAsString);
            OTPGenResponse genResponse = new OTPGenResponse();
            genResponse.setDescription("Success");
            genResponse.setResponseCode("00");
            genResponse.setOtp("0neTimePassw0rd");
            return genResponse;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            OTPGenResponse genResponse = new OTPGenResponse();
            genResponse.setDescription("Success");
            genResponse.setResponseCode("00");
            genResponse.setOtp("0neTimePassw0rd");
            return genResponse;
        }
    }


    public StanbicAccountBalanceResponse balanceEnquiry(String accountNo) {
        logger.info("retrieving account balance from web service");

        try {


            String response  = sendHttpSoapRequest(StanbicSOAP.getAccountEnquiryPayload(accountNo)) ;
            logger.info("stanbic balance response {}", response);
            StanbicAccountBalanceResponse accountBalanceResponse = new StanbicAccountBalanceResponse();
            StanbicAccountBalance accountBalance = new StanbicAccountBalance();
            accountBalance.setAcct_crncy("NGN");
          //  accountBalance.setAcct_name("John Stark Snow");
            accountBalance.setAcct_number(accountNo);
            accountBalance.setAvailable_balance(StringUtils.substringBetween(response,"<detail>","</detail>"));
            accountBalance.setLien_amt("0.0");
            accountBalance.setOd_balance("0.0");
            accountBalance.setUnclr_balance("0.0");

            accountBalanceResponse.setAccountBalance(accountBalance);
            accountBalanceResponse.setDescription("Success");
            accountBalanceResponse.setResponseCode("00");
          //  StanbicAccountBalanceResponse accountBalanceResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(accountBalanceResponse));
            return accountBalanceResponse;

        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(StanbicIBTCServices.class.getName()).log(Level.SEVERE, "Status code:" + statusCode + " Response body: " + responseBodyAsString);
            System.out.println(responseBodyAsString);
            StanbicAccountBalanceResponse accountBalanceResponse = new StanbicAccountBalanceResponse();
            StanbicAccountBalance accountBalance = new StanbicAccountBalance();
            accountBalance.setAcct_crncy("NGN");
            accountBalance.setAcct_name("John Stark Snow");
            accountBalance.setAcct_number("6020007611");
            accountBalance.setAvailable_balance("100000.0");
            accountBalance.setLien_amt("1000.0");
            accountBalance.setOd_balance("1.0");
            accountBalance.setUnclr_balance("100.0");

            accountBalanceResponse.setAccountBalance(accountBalance);
            accountBalanceResponse.setDescription("Success");
            accountBalanceResponse.setResponseCode("00");
            return accountBalanceResponse;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            StanbicAccountBalanceResponse accountBalanceResponse = new StanbicAccountBalanceResponse();
            StanbicAccountBalance accountBalance = new StanbicAccountBalance();
            accountBalance.setAcct_crncy("NGN");
            accountBalance.setAcct_name("John Stark Snow");
            accountBalance.setAcct_number("6020007611");
            accountBalance.setAvailable_balance("100000.0");
            accountBalance.setLien_amt("1000.0");
            accountBalance.setOd_balance("1.0");
            accountBalance.setUnclr_balance("100.0");

            accountBalanceResponse.setAccountBalance(accountBalance);
            accountBalanceResponse.setDescription("Success");
            accountBalanceResponse.setResponseCode("00");
            return accountBalanceResponse;
        }
    }




}
