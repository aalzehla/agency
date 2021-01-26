package com._3line.gravity.freedom.financialInstitutions.fidelity.service;

import com._3line.gravity.freedom.financialInstitutions.dtos.AccOpeningGeneral;
import com._3line.gravity.freedom.financialInstitutions.fidelity.models.*;
import com._3line.gravity.freedom.financialInstitutions.fidelity.requests.*;
import com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody.Beneficiary;
import com._3line.gravity.freedom.financialInstitutions.fidelity.responsebody.ErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Service
public class FidelityWebServices {

    private static final Logger logger = LoggerFactory.getLogger(FidelityWebServices.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${fidelityapi.baseUrl}")
    private String baseUrl;

    @Value("${fidelityapi.create_account.url}")
    private String accountCreateURL;

    @Autowired
    private FidelityHeaders fidelityHeaders;

    public AcctCreationResponse openAccount(AccOpeningGeneral accOpeningGeneral) {
        logger.info("retrieving account using bvn from web service");

        try {
            String url ="https://test-api.fidelitybank.ng/SANEFMiddleware2/api/RetCustAdd";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type","application/json");
            headers.add("Authorization","Basic M0xJTkU6UGFzc3dvcmQxMCE=");
            System.out.println("header ---> " + headers);




            String payLoad = "{   \n" +
                    "\t\"TransReference\":\""+generateUIID()+"\",\n" +
                    "\t\"FirstName\":\""+accOpeningGeneral.getFirstName()+"\",\n" +
                    "\t\"LastName\":\""+accOpeningGeneral.getLastName()+"\",\n" +
                    "\t\"OtherName\":\""+accOpeningGeneral.getMiddleName()+"\",\n" +
                    "\t\"Title\":\""+accOpeningGeneral.getTitle()+"\",\n" +
                    "\t\"DateOfBirth\":\""+accOpeningGeneral.getDateOfBirth()+"\",\n" +
                    "\t\"Gender\":\""+accOpeningGeneral.getGender()+"\", \n" +
                    "\t\"Marital_Status\":\""+accOpeningGeneral.getMaritalStatus()+"\",\n" +
                    "\t\"Address\":\""+accOpeningGeneral.getAddress()+"\",\n" +
                    "\t\"MobileNumber\":\""+accOpeningGeneral.getPhoneNo()+"\",\n" +
                    "\t\"AccountType\":\"1\",\n" +
                    "\t\"BankCode\":\"\",\n" +
                    "\t\"BankName\":\"Fidelity Bank\",\n" +
                    "\t\"LGA\": \""+accOpeningGeneral.getLga()+"\",\n" +
                    "\t\"State\": \""+accOpeningGeneral.getState()+"\",\n" +
                    "\t\"Country\": \"NG\",\n" +
                    "\t\"CooperateBankCode\": \"\",\n" +
                    "\t\"BVN\": \""+accOpeningGeneral.getBvn()+"\",\n" +
                    "\t\"MinimumAccount\": \"\",\n" +
                    "\t\"Email\": \""+accOpeningGeneral.getEmail()+"\",\n" +
                    "\t\"Occupation\": \""+accOpeningGeneral.getOccupation()+"\",\n" +
                    "\t\"CustomerMandate\": \"/9j/4AAQSkZJRgABAQAAAAAAAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0a HBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIy==\"\n" +
                    "\n" +
                    "}";
            HttpEntity<?> entity = new HttpEntity<>(payLoad, headers);
            System.out.println("Body" + entity.getBody().toString());

            ResponseEntity<AcctCreationResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, AcctCreationResponse.class);

            System.out.println("my ishh is :: "+response.getBody());

            AcctCreationResponse acctCreationResponse = response.getBody();
            Gson gson = new Gson();
            logger.info(gson.toJson(acctCreationResponse));

            return acctCreationResponse;
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:" + statusCode + " GravityResponse body: " + responseBodyAsString);
            logger.error(responseBodyAsString);

            Gson gson = new Gson();
            AcctCreationResponse creationResponse = gson.fromJson(responseBodyAsString,AcctCreationResponse.class);

            return creationResponse;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }

    }

    public GetAccountResponse getAccountByBVN(String bvn, String agentAccount) {
        logger.info("retrieving account using bvn from web service");

        try {
            String url = baseUrl + "/GetAccountByBVN";

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            System.out.println("header ---> " + headers);

            JsonObject jo = new JsonObject();
            jo.addProperty("bvn", bvn);

            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);
            System.out.println("Body" + entity.getBody().toString());

            ResponseEntity<GetAccountResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, GetAccountResponse.class);
            GetAccountResponse getAccountResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(getAccountResponse));

            return getAccountResponse;
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:" + statusCode + " GravityResponse body: " + responseBodyAsString);
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }

    }

    public GetAccountResponse getAccountByMobileNo(String mobileNo, String agentAccount) {
        logger.info("retrieving account using bvn from web service");

//        RestTemplate restTemplate = new RestTemplate();
        try {
            String url = baseUrl + "/GetAccountByMobileNo";

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            System.out.println("header ---> " + headers);

            JsonObject jo = new JsonObject();
            jo.addProperty("mobileNo", mobileNo);

            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);
            System.out.println("Body" + entity.getBody().toString());

            ResponseEntity<GetAccountResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, GetAccountResponse.class);
            GetAccountResponse getAccountResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(getAccountResponse));

            return getAccountResponse;

        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:" + statusCode + " GravityResponse body: " + responseBodyAsString);
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }

    }

//
    public ValidateAccountResponse validateBankAccountNo(String accountNo, String agentAccount) {
        logger.info("retrieving account using account no. from web service");

        try {
            String url = baseUrl + "/ValidateFidelityCustomerAccount";

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            System.out.println("header --> " + headers);

            JsonObject jo = new JsonObject();
            jo.addProperty("accountNo", accountNo);

            HttpEntity<String> entity = new HttpEntity<>(jo.toString(), headers);

            System.out.println(entity.getBody());

            ResponseEntity<ValidateAccountResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, ValidateAccountResponse.class);

            ValidateAccountResponse validateAccountResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(validateAccountResponse));

            return validateAccountResponse;

        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:" + statusCode + " GravityResponse body: " + responseBodyAsString);
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }
    }
//

    public ValidateAccountResponse validateOtherBankAccountNo(String accountNo, String bankCode, String agentAccount) {
        logger.info("retrieving account from other bank from web service");

        try {
            String url = baseUrl + "/ValidateOtherBankCustomerAccount";

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            System.out.println("header --> " + headers);

            JsonObject jo = new JsonObject();
            jo.addProperty("accountNo", accountNo);
            jo.addProperty("bankCode", bankCode);

            HttpEntity<String> entity = new HttpEntity<>(jo.toString(), headers);
            System.out.println(entity.getBody());

            ResponseEntity<ValidateAccountResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, ValidateAccountResponse.class);

            ValidateAccountResponse validateAccountResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(validateAccountResponse));

            return validateAccountResponse;
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:" + statusCode + " GravityResponse body: " + responseBodyAsString);
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }
    }
//

    public AccountBalanceResponse balanceEnquiry(String accountNo, String agentAccount) {
        logger.info("retrieving account balance from web service");

        try {
            String url = baseUrl + "/GetBalanceInquiryv2";

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            System.out.println("header --> " + headers);

            JsonObject jo = new JsonObject();
            jo.addProperty("accountNo", accountNo);

            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);
            System.out.println(entity.getBody().toString());

            ResponseEntity<AccountBalanceResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, AccountBalanceResponse.class);
            AccountBalanceResponse accountBalanceResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(accountBalanceResponse));
            return accountBalanceResponse;

        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:" + statusCode + " GravityResponse body: " + responseBodyAsString);
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }
    }

    public BaseResponse leanAccount(LeanAccountRequest leanAccountRequest, String agentAccount) {
        logger.info("retrieving account balance from web service");

        try {
            String url = baseUrl + "/LeanAccountv2";

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            System.out.println("header --> " + headers);

            JsonObject jo = new JsonObject();
            jo.addProperty("account_no", leanAccountRequest.getAccount_no());
            jo.addProperty("lien_amount", leanAccountRequest.getLien_amount());
            jo.addProperty("tran_ref", leanAccountRequest.getTran_ref());
            jo.addProperty("lien_reason_code", leanAccountRequest.getLien_reason_code());
            jo.addProperty("lien_narration", leanAccountRequest.getLien_narration());

            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);
            System.out.println(entity.getBody().toString());

            ResponseEntity<BaseResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, BaseResponse.class);
            BaseResponse baseResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(baseResponse));

            return baseResponse;

        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:" + statusCode + " GravityResponse body: " + responseBodyAsString);
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }
    }

    public BaseResponse unleanAccount(LeanAccountRequest leanAccountRequest, String agentAccount) {
        logger.info("retrieving account balance from web service");

        try {
            String url = baseUrl + "/UnLeanAccountv2";

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            System.out.println("header --> " + headers);

            JsonObject jo = new JsonObject();
            jo.addProperty("account_no", leanAccountRequest.getAccount_no());
            jo.addProperty("lien_amount", leanAccountRequest.getLien_amount());
            jo.addProperty("tran_ref", leanAccountRequest.getTran_ref());
            jo.addProperty("lien_reason_code", leanAccountRequest.getLien_reason_code());
            jo.addProperty("lien_narration", leanAccountRequest.getLien_narration());

            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);
            System.out.println(entity.getBody().toString());

            ResponseEntity<BaseResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, BaseResponse.class);
            BaseResponse baseResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(baseResponse));

            return baseResponse;

        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:" + statusCode + " GravityResponse body: " + responseBodyAsString);
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }
    }

    public AccountStatementResponse getAccountStatement(AccountStatementRequest request, String agentAccount) {
        logger.info("retrieving lean account from web service");

        try {
            String url = baseUrl + "/GetStatmentOfAccountv2";

            JsonObject jo = new JsonObject();
            jo.addProperty("accountNo", request.getAccountNo());
            jo.addProperty("startdate", request.getStartdate());
            jo.addProperty("enddate", request.getEnddate());

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);
            System.out.println(entity.getBody().toString());

            logger.info("about to hit the end point {}", url);
            ResponseEntity<AccountStatementResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, AccountStatementResponse.class);

            AccountStatementResponse statementResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(statementResponse));

            return statementResponse;

        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:" + statusCode + " GravityResponse body: " + responseBodyAsString);
            System.out.println(responseBodyAsString);

            return null;
        }
    }

    public UserDetailsResponse getUserDetails(String accountNumber, String agentAccount) {
        logger.info("retrieving userdetails details from web service");

        try {
            String url = baseUrl + "/GetUserDetails";

            JsonObject jo = new JsonObject();
            jo.addProperty("AccountNumber", accountNumber);

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);

            System.out.println(entity.getBody().toString());

            ResponseEntity<UserDetailsResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, UserDetailsResponse.class);
            UserDetailsResponse userDetailsResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(userDetailsResponse));

            return userDetailsResponse;
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:" + statusCode + " GravityResponse body: " + responseBodyAsString);
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }
    }


    public InterBankTransferResponse interBankTransfer(InterBankTransferRequest transferRequest, String agentAccount) {
        logger.info("retrieving lean account from web service");

        try {
            String url = baseUrl + "/InterBankTransfer";

            JsonObject jo = new JsonObject();
            jo.addProperty("surc_acct", transferRequest.getSurc_acct());
            jo.addProperty("dest_account", transferRequest.getDest_account());
            jo.addProperty("dest_bankCode", transferRequest.getDest_bankCode());
            jo.addProperty("trans_amount", transferRequest.getTrans_amount());
            jo.addProperty("charge_amount", transferRequest.getCharge_amount());
            jo.addProperty("narration", transferRequest.getNarration());
            jo.addProperty("beneficiaryName", transferRequest.getBeneficiaryName());
            jo.addProperty("refno", transferRequest.getRefno());
            jo.addProperty("payment_ref", transferRequest.getPayment_ref());

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);

            System.out.println(entity.getBody());

            logger.info("about to hit the end point {}", url);
            ResponseEntity<InterBankTransferResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, InterBankTransferResponse.class);

            InterBankTransferResponse transferResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(transferResponse));
            return transferResponse;

        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:{0}, GravityResponse body: {1}", new Object[]{statusCode, responseBodyAsString});
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }

    }

    public IntraBankTransferResponse intraBankTransfer(IntraBankTransferRequest transferRequest, String agentAccount) {
        logger.info("retrieving lean account from web service");

        try {
            String url = baseUrl + "/Transfer";

            JsonObject jo = new JsonObject();
            jo.addProperty("sourceAccount", transferRequest.getSourceAccount());
            jo.addProperty("destinationAccount", transferRequest.getDestinationAccount());
            jo.addProperty("amount", transferRequest.getAmount());
            jo.addProperty("naration", transferRequest.getNaration());

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);
            System.out.println(entity.getBody());

            logger.info("about to hit the end point {}", url);
            ResponseEntity<IntraBankTransferResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, IntraBankTransferResponse.class);

            IntraBankTransferResponse transferResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(transferResponse));

            return transferResponse;

        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:{0} GravityResponse body: {1}", new Object[]{statusCode, responseBodyAsString});
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }

    }

    public IntraBankTransferResponse deposit(IntraBankTransferRequest transferRequest, String agentAccount) {
        logger.info("retrieving lean account from web service");

        try {
            String url = baseUrl + "/Deposit";

            JsonObject jo = new JsonObject();
            jo.addProperty("sourceAccount", transferRequest.getSourceAccount());
            jo.addProperty("destinationAccount", transferRequest.getDestinationAccount());
            jo.addProperty("amount", transferRequest.getAmount());
            jo.addProperty("naration", transferRequest.getNaration());

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);
            System.out.println(entity.getBody());

            logger.info("about to hit the end point {}", url);
            ResponseEntity<IntraBankTransferResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, IntraBankTransferResponse.class);

            IntraBankTransferResponse transferResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(transferResponse));

            return transferResponse;

        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:{0} GravityResponse body: {1}", new Object[]{statusCode, responseBodyAsString});
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }

    }

    public IntraBankTransferResponse postFidelitySingleDebitMultipleCredit(SingleDebitMultipleCreditRequest multipleCreditRequest, String agentAccount) {
        logger.info("retrieving lean account from web service");

        try {
            String url = baseUrl + "/PostFidelitySingleDebitMultipleCredit";

            JsonObject jo = new JsonObject();
            jo.addProperty("SourceAccountNumber", multipleCreditRequest.getSourceAccountNumber());
            jo.addProperty("Narration", multipleCreditRequest.getNarration());

            List<Beneficiary> beneficiaryList = multipleCreditRequest.getBeneficiaryList();
            JsonArray ja = new JsonArray();

            beneficiaryList.forEach((Beneficiary b) -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("Amount", b.getAmount());
                jsonObject.addProperty("DestinationAccount", b.getDestinationAccount());
                ja.add(jsonObject);
            });
            jo.add("BeneficiaryList", ja);

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);

            System.out.println(entity.getBody());

            ResponseEntity<IntraBankTransferResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, IntraBankTransferResponse.class);

            IntraBankTransferResponse singleDebitMultipleCreditResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(singleDebitMultipleCreditResponse));

            return singleDebitMultipleCreditResponse;

        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:{0} GravityResponse body: {1}", new Object[]{statusCode, responseBodyAsString});
            System.out.println(responseBodyAsString);

            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }

    }

    public BaseResponse sendSMS(String mobile, String message, String agentAccount) {
        logger.info("about to send sms from web service");

        try {
            String url = baseUrl + "/SendSMS";

            JsonObject jo = new JsonObject();
            jo.addProperty("mobile", mobile);
            jo.addProperty("message", message);

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);

            logger.info("about to hit the end point {}", url);
            ResponseEntity<BaseResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, BaseResponse.class);

            BaseResponse baseResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(baseResponse));

            return baseResponse;
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:{0} GravityResponse body: {1}", new Object[]{statusCode, responseBodyAsString});
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }

    }

    public OTPGenResponse generateOTP(String accountNo, String agentAccount) {
        logger.info("retrieving lean account from web service");

        try {
            String url = baseUrl + "/GenerateOTP";

            JsonObject jo = new JsonObject();
            jo.addProperty("account", accountNo);

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);
            System.out.println(entity.getBody().toString());

            logger.info("about to hit the end point {}", url);
            ResponseEntity<OTPGenResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, OTPGenResponse.class);

            OTPGenResponse oTPGenResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(oTPGenResponse));

            return oTPGenResponse;
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:{0} GravityResponse body: {1}", new Object[]{statusCode, responseBodyAsString});
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }
    }

    public BaseResponse validateOTP(String accountNo, String otp, String agentAccount) {
        logger.info("retrieving lean account from web service");

        try {
            String url = baseUrl + "/ValidateOTP";

            JsonObject jo = new JsonObject();
            jo.addProperty("accountNo", accountNo);
            jo.addProperty("otp", otp);

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);

            System.out.println(entity.getBody().toString());

            logger.info("about to hit the end point {}", url);
            ResponseEntity<BaseResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, BaseResponse.class);

            BaseResponse baseResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(baseResponse));

            return baseResponse;
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:{0} GravityResponse body: {1}", new Object[]{statusCode, responseBodyAsString});
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }
    }

    public BaseResponse requestCard(CardRequest cardRequest, String agentAccount) {
        logger.info("retrieving request card details from web service");

        try {
            String url = baseUrl + "/RequestCard";

            JsonObject jo = new JsonObject();
            jo.addProperty("CardTypeID", cardRequest.getCardTypeID());
            jo.addProperty("AccountNumber", cardRequest.getAccountNumber());
            jo.addProperty("CustomerFirstName", cardRequest.getCustomerFirstName());
            jo.addProperty("CustomerSurnameName", cardRequest.getCustomerSurname());
            jo.addProperty("CustomerOtherNames", cardRequest.getCustomerOtherNames());

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);

            System.out.println(entity.getBody().toString());

            ResponseEntity<BaseResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, BaseResponse.class);

            BaseResponse baseResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(baseResponse));

            return baseResponse;
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:{0} GravityResponse body: {1}", new Object[]{statusCode, responseBodyAsString});
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }
    }

    public CardTypeResponse getCardTypes(String agentAccount) {
        logger.info("retrieving card types from web service");

        try {
            String url = baseUrl + "/GetCardTypes";

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            logger.info("about to hit the end point {}", url);
            ResponseEntity<CardTypeResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, CardTypeResponse.class);

            CardTypeResponse cardTypeResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(cardTypeResponse));

            return cardTypeResponse;

        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:{0} GravityResponse body: {1}", new Object[]{statusCode, responseBodyAsString});
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }
    }

    public BillerCategoriesResponse getBillerCategories(String agentAccount) {

        logger.info("retrieving biller categories from web service");

        try {
            String url = baseUrl + "/GetBillerCategories";

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            logger.info("about to hit the end point {}", url);
            ResponseEntity<BillerCategoriesResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, BillerCategoriesResponse.class);

            BillerCategoriesResponse billerCategoriesResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(billerCategoriesResponse));

            return billerCategoriesResponse;

        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:{0} GravityResponse body: {1}", new Object[]{statusCode, responseBodyAsString});
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }
    }

    public BillersListResponse getBillerCategoriesById(int id, String agentAccount) {

        logger.info("retrieving biller categories by id {} from web service", id);

        try {
            String url = baseUrl + "/GetBillersByCategoryID?id=" + id;

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            System.out.println("entity --> " + entity);

            logger.info("about to hit the end point {}", url);
            ResponseEntity<BillersListResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, BillersListResponse.class);
            BillersListResponse billersByCategoryIDResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(billersByCategoryIDResponse));

            return billersByCategoryIDResponse;

        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:{0} GravityResponse body: {1}", new Object[]{statusCode, responseBodyAsString});
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }
    }

    public BillersListResponse getBillers(String agentAccount) {

        logger.info("retrieving biller from web service");

        try {
            String url = baseUrl + "/GetBillers";

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            System.out.println("entity --> " + entity);

            logger.info("about to hit the end point {}", url);
            ResponseEntity<BillersListResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, BillersListResponse.class);

            BillersListResponse allBillersResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(allBillersResponse));

            return allBillersResponse;
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:{0} GravityResponse body: {1}", new Object[]{statusCode, responseBodyAsString});
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }
    }

    public BaseResponse makeBillPayment(BillPaymentRequest paymentRequest, String agentAccount) {
        logger.info("validating quick teller customer details from web service");

        try {
            String url = baseUrl + "/MakeBillsPayment";

            JsonObject jo = new JsonObject();
            jo.addProperty("amount", paymentRequest.getAmount());
            jo.addProperty("customerEmail", paymentRequest.getCustomerEmail());
            jo.addProperty("customerMobile", paymentRequest.getCustomerMobile());
            jo.addProperty("customerId", paymentRequest.getCustomerId());
            jo.addProperty("paymentCode", paymentRequest.getPaymentCode());
            jo.addProperty("requestReference3D", paymentRequest.getRequestReference3D());

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);

            System.out.println(entity.getBody().toString());

            ResponseEntity<BaseResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, BaseResponse.class);
            BaseResponse baseResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(baseResponse));

            return baseResponse;
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:{0} GravityResponse body: {1}", new Object[]{statusCode, responseBodyAsString});
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }

    }

    public ValidateAccountByNibssBvnResponse validateAccountByNibssBvn(ValidateAccountByNibssBvnRequest nibssBvnRequest, String agentAccount) {
        logger.info("retrieving account using bvn from web service");

        try {
            String url = baseUrl + "/NIBSSValidateBVN";

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            System.out.println("header ---> " + headers);

            JsonObject jo = new JsonObject();
            jo.addProperty("bvn_id", nibssBvnRequest.getBvn_id());
            jo.addProperty("account_nr", nibssBvnRequest.getAccount_nr());

            HttpEntity<?> entity = new HttpEntity<>(jo.toString(), headers);
            System.out.println("Body" + entity.getBody().toString());

            ResponseEntity<ValidateAccountByNibssBvnResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, ValidateAccountByNibssBvnResponse.class);
            ValidateAccountByNibssBvnResponse nibssBvnResponse = response.getBody();
            Gson gson = new Gson();
            System.out.println(gson.toJson(nibssBvnResponse));

            return nibssBvnResponse;
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {

            String responseBodyAsString = httpException.getResponseBodyAsString();
            HttpStatus statusCode = httpException.getStatusCode();

            java.util.logging.Logger.getLogger(FidelityWebServices.class.getName()).log(Level.SEVERE, "Status code:" + statusCode + " GravityResponse body: " + responseBodyAsString);
            System.out.println(responseBodyAsString);
            return null;
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
            return null;
        }

    }

    public BillerListResponse getBillersById(long id, String agentAccount) {

        logger.info("retrieving biller from web service");

        try {
            String url = baseUrl + "/GetBillersByID";

            Map<String, Long> request = new HashMap<>();
            request.put("id", id);

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(request, headers);

            System.out.println("entity --> " + entity);

            logger.info("about to hit the end point {}", url);
            ResponseEntity<BillerListResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, BillerListResponse.class);

            logger.info("######################## response from web service imp:: ", response);
            return response.getBody();
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
        }
        return null;
    }

    public BillerListResponse getBillersByOptionId(long id, String agentAccount) {

        logger.info("retrieving biller by option id {} from web service", id);

        try {
            String url = baseUrl + "/GetBillersByOptionID";

            Map<String, Long> request = new HashMap<>();
            request.put("id", id);

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(request, headers);

            System.out.println("entity --> " + entity);

            logger.info("about to hit the end point {}", url);
            ResponseEntity<BillerListResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, BillerListResponse.class);

            logger.info("######################## response from web service imp:: ", response);
            return response.getBody();
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
        }
        return null;
    }

    public String validateQuickTellerCustomer(ValidateQuickTellerCustomerRequest validateQuickTellerCustomerRequest, String agentAccount) {
        logger.info("validating quick teller customer details from web service");

        try {
            String url = baseUrl + "/ValidateQuickTellerCustomer";

            Map<String, Object> request = new HashMap<>();
            request.put("customersId", validateQuickTellerCustomerRequest.getCustomers().getCustomerId());
            request.put("paymentCode", validateQuickTellerCustomerRequest.getCustomers().getPaymentCode());
            request.put("terminalId", validateQuickTellerCustomerRequest.getTerminalId());

            HttpHeaders headers = fidelityHeaders.create(agentAccount);
            HttpEntity<?> entity = new HttpEntity<>(request, headers);

            System.out.println(entity.getBody().toString());

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            logger.info("######################## response from web service imp:: ", response);
            return response.getBody();
        } catch (ResourceAccessException e) {
            logger.info("error--> ", e.getMostSpecificCause());
            e.getMessage();
        }
        return null;
    }


    private String generateUIID(){
        Long uuid =  new Date().getTime();
        return String.valueOf(uuid);
    }

}
