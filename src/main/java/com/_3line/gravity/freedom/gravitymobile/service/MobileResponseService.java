package com._3line.gravity.freedom.gravitymobile.service;

import com._3line.gravity.api.transaction.dto.TransactionHistoryDTO;
import com._3line.gravity.core.cryptography.SecretKeyCrypt;
import com._3line.gravity.core.cryptography.exceptions.CryptographyException;
import com._3line.gravity.core.cryptography.rsa.RSACrypt;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.model.TransactionType;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.billpayment.dtos.ValidateCustomerRequest;
import com._3line.gravity.freedom.commisioncharge.service.CommissionChargeService;
import com._3line.gravity.freedom.commisions.services.GravityDailyCommissionService;
import com._3line.gravity.freedom.financialInstitutions.dtos.AccOpeningGeneral;
import com._3line.gravity.freedom.financialInstitutions.dtos.DepositRequest;
import com._3line.gravity.freedom.financialInstitutions.dtos.NameEnquiryDTO;
import com._3line.gravity.freedom.financialInstitutions.dtos.TransferRequest;
import com._3line.gravity.freedom.financialInstitutions.opay.service.OpayService;
import com._3line.gravity.freedom.financialInstitutions.service.BankProcessor;
import com._3line.gravity.freedom.agents.service.SecurityDao;
import com._3line.gravity.api.bills.dto.PayBills;
import com._3line.gravity.api.users.agents.dto.AgentSetupResponse;
import com._3line.gravity.freedom.issuelog.dtos.IssueLogDTO;
import com._3line.gravity.freedom.issuelog.service.IssueLogService;
import com._3line.gravity.freedom.thirftmgt.dtos.ContributionDTO;
import com._3line.gravity.freedom.thirftmgt.dtos.ThriftDTO;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.utility.ApiCallLogs;
import com._3line.gravity.freedom.utility.PropertyResource;
import com._3line.gravity.freedom.wallet.dto.CreditRequestDTO;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author OlalekanW
 */
@Service
public class MobileResponseService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Qualifier("gravityDailyCommissionServiceImpl")
    @Autowired
    GravityDailyCommissionService commissionService;
    @Autowired
    BankDetailsService bankDetailsService;
    @Autowired
    @Qualifier("magtipon")
    BankProcessor magtiponProcessor;
    @Autowired
    OpayService opayService;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AgentsRepository applicationUsersRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PropertyResource pr ;

    private Gson json = new Gson();
    @Autowired
    private SecurityDao dao;

    @Autowired
    private BankSavingService savingService ;
    @Autowired
    private BanKTransferService banKTransferService;
    @Autowired
    BankBalanceEnquiryService bankBalanceEnquiryService;
    @Autowired
    NameEnquriyService nameEnquriyService ;
    @Autowired
    IssueLogService issueLogService;
    @Autowired
    private CommissionChargeService commissionChargeService;
    @Autowired
    private SecretKeyCrypt secretKeyCrypt;
    @Autowired
    private RSACrypt rsaCrypt;
    @Autowired
    private WithdrawalService withdrawalService;
    @Autowired
    private MobileWalletService mobileWalletService;
    @Autowired
    private MobileThriftService mobileThriftService;
    @Autowired
    private AccountOpeningService accountOpeningService ;
    @Autowired
    private BillerService billerService ;
    @Autowired
    private TransactionService transactionService;

    public Response nameEnquiry(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        NameEnquiryDTO nameEnquiryDTO = null;

        try {
            nameEnquiryDTO = json.fromJson(jsonRequest, NameEnquiryDTO.class);
        } catch (Exception e) {
            response.setRespCode("117");
            response.setRespDescription(pr.getV("117", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

            return nameEnquriyService.getName(nameEnquiryDTO);

    }

    public Response agentBalance(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");
        String username = param.get("username");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }


        try {

            return  bankBalanceEnquiryService.getAgentBalance(username) ;
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespCode("117");
            response.setRespDescription(pr.getV("117", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }



    }

    public Response savings(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");
        String Username = param.get("username");
        String agentId = param.get("userid");
        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(authorisation);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        DepositRequest depositRequest = json.fromJson(jsonRequest, DepositRequest.class);
        depositRequest.setTotalAmount(depositRequest.getAmount());
        AgentSetupResponse setupresponse = dao.getAgentDetailsByUsername(Username);
        Agents agent  = applicationUsersRepository.findByUsername(Username);
        try {
            String pin = rsaCrypt.decrypt(depositRequest.getAgentPin());
            if (!passwordEncoder.matches(pin, setupresponse.getPin())) {
                throw new GravityException("Invalid Agent Pin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespCode("154");
            response.setRespDescription(pr.getV("154", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }


        Transactions transactions = new Transactions();
        transactions.setTranDate(new Date());
        transactions.setStatus((short) 0);
        transactions.setInnitiatorId(agent.getId());
        transactions.setApproval(0);
        transactions.setAmount(Double.valueOf(depositRequest.getAmount()));
        transactions.setBeneficiary(depositRequest.getAccountNumber());
        transactions.setLatitude(depositRequest.getLatitude());
        transactions.setLongitude(depositRequest.getLongitude());
        transactions.setTerminalId(depositRequest.getDeviceId());
        transactions.setTransactionType("Deposit");
        transactions.setTransactionTypeDescription("Deposit");
        transactions.setStatusdescription("SUCCESSFUL");
        transactions.setDescription("Processing");
        transactions.setBankFrom(agent.getBankCode());
        transactions.setBankTo(depositRequest.getBankCode());
        transactions.setAccountNumber(agent.getAccountNo());
        transactions.setAccountNumberTo(depositRequest.getAccountNumber());
        transactions.setAgentName(Username);
        transactions.setCustomerName("");
        transactions.setDepositorName(depositRequest.getCustomerName());
        transactions.setDepositorEmail(depositRequest.getCustomerEmail());
        transactions.setDepositorPhone(depositRequest.getCustomerPhone());
        transactions.setDescription(depositRequest.getNarration());
        transactions.setMedia(depositRequest.getMedia());
        Transactions transaction = transactionRepository.save(transactions);

        try {

           Response response1 = savingService.doSaving(depositRequest , transaction.getTranId() , Username);

           if(response1.getRespCode().equals("00")) {
            transaction.setStatusdescription("SUCCESSFUL");
            transaction.setStatus((short) 1);
            transactionRepository.save(transaction);
           }else {
               transaction.setStatusdescription("FAILED");
               transactions.setStatus((short) 2);
               transactionRepository.save(transaction);
           }
            return response1 ;
        } catch (Exception e) {
            transaction.setStatusdescription("FAILED");
            transactions.setStatus((short) 2);
            transactionRepository.save(transaction);
            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription(e.getMessage());
            logger.warn(response.getRespDescription());
            return response;
        }

    }

    public Response cardWithdrawals(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");
        String Username = param.get("Username");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

//        return withdrawalService.cardWithdrawal(param, Username, jsonRequest);
        return withdrawalService.cardWithdrawal(param, Username, null);

    }

    public Response cardWithdrawalsOTP(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }


//        return withdrawalService.otp(jsonRequest);
        return withdrawalService.otp(null);

    }

    private String decryptRequest(String requestbody) throws CryptographyException {
        String jsonRequest;

        jsonRequest = secretKeyCrypt.decrypt(requestbody);

        return jsonRequest;
    }

    public Response interBankTransfer(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        TransferRequest transferRequest = null;

        try {

            transferRequest = json.fromJson(jsonRequest, TransferRequest.class);
        } catch (Exception e) {
            response.setRespCode("117");
            response.setRespDescription(pr.getV("117", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        Agents agent  = applicationUsersRepository.findByUsername(transferRequest.getAgentUsername());

        try {

           if(agent == null){
               throw new GravityException("Agent does not exist");
           }
        } catch (Exception e) {
            response.setRespCode("117");
            response.setRespDescription(e.getMessage());
            logger.warn(response.getRespDescription());
            return response;
        }
        /*
         *  validate customer pin and agent pin here
         */

        try {
            AgentSetupResponse setupresponse = dao.getAgentDetailsByUsername(agent.getUsername());

            String agentPin = rsaCrypt.decrypt(transferRequest.getAgentPin()) ;
            if (!passwordEncoder.matches(agentPin, setupresponse.getPin())) {
                throw new GravityException("Invalid Agent Pin");
            }

        }catch (Exception e){

            e.printStackTrace();
            response.setRespCode("154");
            response.setRespDescription(pr.getV("154", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;

        }



        /*
         * log transaction and pass agentName
         */
        Transactions transactions = new Transactions();
        transactions.setTranDate(new Date());
        transactions.setStatus((short) 0);
        transactions.setInnitiatorId(agent.getId());
        transactions.setApproval(0);
        transactions.setAmount(Double.valueOf(transferRequest.getAmount()));
        transactions.setBeneficiary(transferRequest.getReceivingBankAccount());
        transactions.setLatitude(transferRequest.getLatitude());
        transactions.setLongitude(transferRequest.getLongitude());
        transactions.setTerminalId(transferRequest.getDeviceId());
        transactions.setTransactionType("Transfer");
        transactions.setTransactionTypeDescription("Bank transfer");
        transactions.setStatusdescription("SUCCESSFUL");
        transactions.setDescription("Processing");
        transactions.setBankFrom(transferRequest.getCustomerBankCode());
        transactions.setBankTo(transferRequest.getReceivingBankCode());
        transactions.setAccountNumber(transferRequest.getCustomerAccount());
        transactions.setAccountNumberTo(transferRequest.getReceivingBankAccount());
        transactions.setAgentName(agent.getUsername());
//        transactions.setCustomerName(setupresponse.getName());
        transactions.setDescription(transferRequest.getNarration());
        transactions.setMedia(transferRequest.getMedia());
        Transactions transaction = transactionRepository.save(transactions);
        try {
            logger.info("sending transfer to transferservice {}", transferRequest.toString());
            Response response1 = banKTransferService.perFormTransfer(transferRequest, transaction.getTranId() , agent.getUsername());
            transaction.setStatusdescription("SUCCESSFUL");
            transaction.setStatus((short) 1);
            transactionRepository.save(transaction);
            return response1 ;
        } catch (Exception e) {
            e.printStackTrace();
            transaction.setStatusdescription("FAILED");
            transaction.setStatus((short) 2);
            transactionRepository.save(transaction);
            response.setRespCode("999");
            response.setRespDescription(e.getMessage());
            return response;
        }

    }

    public Response getAllBanks(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {

            List<BankDetails> details = bankDetailsService.findAllBankDetails();

            response.setRespCode("00");
            response.setRespDescription("bank details");
            response.setRespBody(details.toArray());

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody("Error Ocured");

        }

        logger.info("response going out");

        return response;
    }

    public Response getBankDetails(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {

            Map data = json.fromJson(jsonRequest, HashMap.class);
            String code = (String)data.get("bankCode") ;
            logger.info("about getting bank details {}", code);
            BankDetails details = bankDetailsService.findByCode(code);
            logger.info("details is {}", details);
            response.setRespCode("00");
            response.setRespDescription("bank details");
            response.setRespBody(details);

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody("Error Ocured");

        }

        logger.info("response going out");

        return response;
    }

    public Response logIssues(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {

            IssueLogDTO logDTO = json.fromJson(jsonRequest, IssueLogDTO.class);

            logger.info("about logging issue ");
            Agents agents = applicationUsersRepository.findByUsername(logDTO.getAgentName());
            logDTO.setAgentEmail(agents.getEmail());
            issueLogService.logIssue(logDTO);
            response.setRespCode("00");
            response.setRespDescription("issuelog.add.success");
        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody("Error Ocured");

        }

        logger.info("response going out");

        return response;
    }

    public Response getCommissionFee(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {

            Map data = json.fromJson(jsonRequest, HashMap.class);
            Double amount = (Double) data.get("amount") ;
            TransactionType transactionType = TransactionType.valueOf((String) data.get("type")) ;
            logger.info("about to get fee for amount {}", amount);

            Double fee = commissionChargeService.getCommissionForAmount(amount , transactionType) ;
            response.setRespCode("00");
            response.setRespDescription("Tran Fee");
            Map res = new HashMap();
            res.put("fee", fee);
            response.setRespBody(res);

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response getWalletBalance(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {

            Map data = json.fromJson(jsonRequest, HashMap.class);

            return mobileWalletService.getWalletBalance((String)data.get("agentName"));

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response getWalletTranHistory(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {

            Map data = json.fromJson(jsonRequest, HashMap.class);

            return mobileWalletService.getWalletTransactionHistory((String)data.get("agentName"),(String)data.get("from"),(String)data.get("to"));

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response getInWalletTranHistory(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {

            Map data = json.fromJson(jsonRequest, HashMap.class);

            return mobileWalletService.getIncomeWalletTransactionHistory((String)data.get("agentName"),(String)data.get("from"),(String)data.get("to"));

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response disburseWallet(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }



        Map data = json.fromJson(jsonRequest, HashMap.class);

        logger.info("request {}", data);

        Agents agent = applicationUsersRepository.findByUsername((String)data.get("agentName"));


        try {
            String pin = rsaCrypt.decrypt((String) data.get("agentPin"));
            if (!passwordEncoder.matches(pin, agent.getUserPin())) {
                throw new GravityException("Invalid Agent Pin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespCode("154");
            response.setRespDescription(pr.getV("154", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {


            return mobileWalletService.disburseFromWallet((String)data.get("agentName"),(String)data.get("amount"));

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response disburseIncomeWallet(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }



        Map data = json.fromJson(jsonRequest, HashMap.class);

        logger.info("request {}", data);

        Agents agent = applicationUsersRepository.findByUsername((String)data.get("agentName"));


        try {
            String pin = rsaCrypt.decrypt((String) data.get("agentPin"));
            if (!passwordEncoder.matches(pin, agent.getUserPin())) {
                throw new GravityException("Invalid Agent Pin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespCode("154");
            response.setRespDescription(pr.getV("154", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {


            return mobileWalletService.disburseFromInWallet((String)data.get("agentName"),(String)data.get("amount"));

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response transferBetweenWallet(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }



        Map data = json.fromJson(jsonRequest, HashMap.class);

        Agents agent = applicationUsersRepository.findByUsername((String)data.get("agentName"));


        try {
            String pin = rsaCrypt.decrypt((String) data.get("agentPin"));
            if (!passwordEncoder.matches(pin, agent.getUserPin())) {
                throw new GravityException("Invalid Agent Pin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespCode("154");
            response.setRespDescription(pr.getV("154", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {


            return mobileWalletService.transferBetweenWallets((String)data.get("agentName"),(String)data.get("amount"),(String)data.get("fromWallet"),(String)data.get("toWallet"),"","");

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription(e.getMessage());
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response creditRequest(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }



        CreditRequestDTO data = json.fromJson(jsonRequest, CreditRequestDTO.class);


        try {


            return mobileWalletService.creditRequest(data);

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response creditRequestHistory(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }



        Map data = json.fromJson(jsonRequest, HashMap.class);


        try {


            return mobileWalletService.getCreditRequesthistory((String)data.get("agentName"));

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response registerForThrift(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }



        Map data = json.fromJson(jsonRequest, HashMap.class);

        ThriftDTO thriftDTO = json.fromJson(jsonRequest, ThriftDTO.class);

        Agents agent = applicationUsersRepository.findByUsername((String)data.get("agentName"));


        try {
            String pin = rsaCrypt.decrypt((String) data.get("agentPin"));
            if (!passwordEncoder.matches(pin, agent.getUserPin())) {
                throw new GravityException("Invalid Agent Pin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespCode("154");
            response.setRespDescription(pr.getV("154", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {


            return mobileThriftService.registerForThrift(thriftDTO);

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response contributeThrift(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }



        Map data = json.fromJson(jsonRequest, HashMap.class);

        ContributionDTO contributionDTO = json.fromJson(jsonRequest, ContributionDTO.class);

        Agents agent = applicationUsersRepository.findByUsername((String)data.get("agentName"));


        try {
            String pin = rsaCrypt.decrypt((String) data.get("agentPin"));
            if (!passwordEncoder.matches(pin, agent.getUserPin())) {
                throw new GravityException("Invalid Agent Pin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespCode("154");
            response.setRespDescription(pr.getV("154", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {


            return mobileThriftService.contribute(contributionDTO);

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response thriftDetails(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }



        Map data = json.fromJson(jsonRequest, HashMap.class);

        Agents agent = applicationUsersRepository.findByUsername((String)data.get("agentName"));


        try {
            String pin = rsaCrypt.decrypt((String) data.get("agentPin"));
            if (!passwordEncoder.matches(pin, agent.getUserPin())) {
                throw new GravityException("Invalid Agent Pin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespCode("154");
            response.setRespDescription(pr.getV("154", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {


            return mobileThriftService.getThriftDetails((String)data.get("cardNumber"));

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response prematureLiquidation(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        Map data = json.fromJson(jsonRequest, HashMap.class);
        Agents agent = applicationUsersRepository.findByUsername((String)data.get("agentName"));

        try {
            String pin = rsaCrypt.decrypt((String) data.get("agentPin"));
            if (!passwordEncoder.matches(pin, agent.getUserPin())) {
                throw new GravityException("Invalid Agent Pin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespCode("154");
            response.setRespDescription(pr.getV("154", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {

            return mobileThriftService.preMatureLiquidation((String)data.get("cardNumber"));

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response openAccount(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        AccOpeningGeneral accOpeningGeneral = json.fromJson(jsonRequest, AccOpeningGeneral.class);


        try {

            return accountOpeningService.openAccount(accOpeningGeneral);

        } catch (Exception e){

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response getBillerCategory(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted


        try {

            return billerService.getCategories() ;

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response getBillServiceForCategory(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

       Map data = json.fromJson(jsonRequest, HashMap.class);


        try {

            return billerService.getServiceForCategory(Long.parseLong((String)data.get("categoryId")));

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response getBillOptionsForService(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        Map data = json.fromJson(jsonRequest, HashMap.class);


        try {

            return billerService.getOptionsForService((Long.parseLong((String)data.get("serviceId"))));

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response payBills(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        PayBills data = json.fromJson(jsonRequest, PayBills.class);
        Map req = json.fromJson(jsonRequest, HashMap.class);

        Agents agent = applicationUsersRepository.findByUsername((String)req.get("agentName"));

        try {
            String pin = rsaCrypt.decrypt((String) req.get("agentPin"));
            if (!passwordEncoder.matches(pin, agent.getUserPin())) {
                throw new GravityException("Invalid Agent Pin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespCode("154");
            response.setRespDescription(pr.getV("154", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }


        try {

            return billerService.payBills(data);

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }


    public Response validateBillerCustomer(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");

        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");

        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        ValidateCustomerRequest data = json.fromJson(jsonRequest, ValidateCustomerRequest.class);

        try {

            return billerService.validate(data,null);

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return response;
    }

    public Response getTranHistory(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");
        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");
        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {

            TransactionHistoryDTO data = json.fromJson(jsonRequest, TransactionHistoryDTO.class);
            Response response1 = new Response();
            response1.setRespBody(transactionService.getAgentsTransactionHistory(data));
            response1.setRespCode("00");
            response1.setRespCode("success");
        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());
        }
        logger.info("response going out");
        return response;
    }

    public Response getTranHistorySendEmail(String authorisation, Map<String, String> param, ApiCallLogs callLog) {

        String belongsto = param.get("belongsto");
        callLog.setSourceId(belongsto);
        param.put("currentCallReferenceId", callLog.getReferenceid() + "");
        Response response = new Response();
        String requestbody = authorisation;
        String jsonRequest;
        //request decrypted
        try {
            jsonRequest = decryptRequest(requestbody);
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        try {

            TransactionHistoryDTO data = json.fromJson(jsonRequest, TransactionHistoryDTO.class);
            return transactionService.getSendEmail(data) ;
        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());
        }
        logger.info("response going out");
        return response;
    }

}
