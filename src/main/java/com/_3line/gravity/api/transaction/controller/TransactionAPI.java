package com._3line.gravity.api.transaction.controller;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.api.transaction.dto.CardRequestDTO;
import com._3line.gravity.api.transaction.dto.ReverseTransactionDTO;
import com._3line.gravity.api.transaction.dto.TransactionHistoryDTO;
import com._3line.gravity.core.code.dto.CodeDTO;
import com._3line.gravity.core.code.service.CodeService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.sms.service.SmsService;
import com._3line.gravity.core.thirdparty.dto.ThirdPartyDto;
import com._3line.gravity.core.thirdparty.service.ThirdPartyService;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.cardrequest.service.CardRequestService;
import com._3line.gravity.freedom.financialInstitutions.dtos.*;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.service.GTBankService;
import com._3line.gravity.freedom.gravitymobile.service.*;
import com._3line.gravity.freedom.itexintegration.service.PtspService;
import com._3line.gravity.freedom.transactions.dtos.DepositDTO;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.utility.PropertyResource;
import com._3line.gravity.freedom.utility.Utility;
import com._3line.gravity.freedom.dispute.dtos.DisputeDto;
import com._3line.gravity.freedom.wallet.models.WalletCreditReversal;
import com._3line.gravity.freedom.wallet.repository.WalletCreditReversalRepository;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/gravity/api/transactions")
public class TransactionAPI {

    @Autowired
    TransactionRepository transactionRepository;

//    @Autowired
//    MobileService service;

    @Autowired
    private BankSavingService savingService ;

    @Autowired
    WithdrawalService withdrawalService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    BanKTransferService banKTransferService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    NameEnquriyService nameEnquriyService;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    AgentService agentService;

    @Autowired
    GTBankService gtBankService;

    @Autowired
    BankDetailsService  bankDetailsService;

    @Autowired
    WalletService walletService;

    @Qualifier("IPIntgratedSMSImplementation")
    @Autowired
    SmsService  smsService;

    @Autowired
    PropertyResource pr;

    @Autowired
    ThirdPartyService  thirdPartyService;

    @Autowired
    PtspService ptspService;

    @Autowired
    CardRequestService cardRequestService;

    @Autowired
    WalletCreditReversalRepository walletCreditReversalRepository;

    @Autowired
    CodeService codeService;

    @Autowired
    SettingService settingService;


    private Logger logger = LoggerFactory.getLogger(TransactionAPI.class);


    @RequestMapping(value = "/savings", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> savings(HttpServletRequest request, @RequestBody DepositRequest depositRequest) {

        Response response = new Response();
        depositRequest.setTotalAmount(depositRequest.getAmount());
        Agents agent  = jwtUtility.getCurrentAgent();



        List<CodeDTO> codeDTOS = codeService.getCodesByType("EXEMPTED_USERS");

        for (CodeDTO s : codeDTOS) {
            if(s.getCode().equalsIgnoreCase(agent.getUsername()) ){
                throw new GravityException("Error occurred Processing Transaction");
            }
        }

        boolean isValidPin = jwtUtility.isValidAgentPin(depositRequest.getAgentPin(),agent);
        if(!isValidPin){
            throw new GravityException("Invalid Agent PIN");
        }

        Transactions transactions = new Transactions();
        transactions.setTranDate(new Date());
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
        transactions.setStatus((short) 1);
        transactions.setDescription("Processing");
        transactions.setBankFrom(agent.getBankCode());
        transactions.setBankTo(depositRequest.getBankCode());
        transactions.setAccountNumber(agent.getAccountNo());
        transactions.setAccountNumberTo(depositRequest.getAccountNumber());
        transactions.setAgentName(agent.getUsername());
        transactions.setCustomerName(depositRequest.getCustomerName());
        transactions.setDepositorName(depositRequest.getDepositor());
        transactions.setDepositorEmail(depositRequest.getCustomerEmail());
        transactions.setDepositorPhone(depositRequest.getCustomerPhone());
        transactions.setDescription(depositRequest.getNarration());
        transactions.setMedia(depositRequest.getMedia());
        Transactions transaction = transactionRepository.save(transactions);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {

            Response response1 = savingService.doSaving(depositRequest ,transaction.getTranId() , agent.getUsername());

            DepositDTO depositDTO = new DepositDTO();
            if(response1.getRespCode().equals("00")) {
                transaction.setStatusdescription("SUCCESSFUL");
                transaction.setStatus((short) 1);
                transaction.setTransactionReference(response1.getRespDescription());
                transactionRepository.save(transaction);

                String template = pr.getV("agent.deposit.success", "sms_messages.properties");
                String message = String.format(template,
                        depositRequest.getCustomerName(),transaction.getAmount(),
                        transaction.getBankTo(),transaction.getAccountNumberTo(),
                        transaction.getDepositorName(),transaction.getTransactionReference());
                try{
                    smsService.sendPlainSms(depositRequest.getCustomerPhone(),message);
                }catch(Exception e){

                }


                depositDTO.setAgentCommission("0.85");
                depositDTO.setAmount(Double.valueOf(depositRequest.getAmount()));
                depositDTO.setFreedomCharge(1.40);
                depositDTO.setRecipientAccount(depositRequest.getAccountNumber());
                depositDTO.setRecipientBank(depositRequest.getBankName());
                depositDTO.setRecipientName(depositRequest.getCustomerName() );
                depositDTO.setTranDate(transaction.getTranDate());
                depositDTO.setTransactionRef(response1.getRespDescription());
                response.setRespBody(depositDTO);
            }else {
                transaction.setStatusdescription("FAILED");
                transactions.setStatus((short) 2);
                transactionRepository.save(transaction);
            }
            return new ResponseEntity<>(response1, HttpStatus.OK);
        } catch (Exception e) {
            //transaction.setStatusdescription("FAILED");
            //transactions.setStatus((short) 2);
            //transactionRepository.save(transaction);
            e.printStackTrace();
            response.setRespCode("96");
            response.setRespDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }


    @RequestMapping(value = "/card_request", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> initiateCreditRequest(HttpServletRequest request, @RequestBody CardRequestDTO cardRequestDTO) {

        Response response = new Response();
        Agents agent  = jwtUtility.getCurrentAgent();

        boolean isValidPin = jwtUtility.isValidAgentPin(cardRequestDTO.getAgentPin(),agent);
        if(!isValidPin){
            throw new GravityException("Invalid Agent PIN");
        }

        cardRequestDTO.setAgentPin("****");

        BankDetails bankDetails = bankDetailsService.findByCode(cardRequestDTO.getBankName());

        DepositRequest depositRequest = new DepositRequest(cardRequestDTO,agent);


        Transactions transactions = new Transactions();
        transactions.setTranDate(new Date());
        transactions.setInnitiatorId(agent.getId());
        transactions.setApproval(0);
        transactions.setAmount(Double.valueOf(depositRequest.getAmount()));
        transactions.setBeneficiary(bankDetails.getCardRequestGlAcct());
        transactions.setLatitude(depositRequest.getLatitude());
        transactions.setLongitude(depositRequest.getLongitude());
        transactions.setTerminalId(depositRequest.getDeviceId());
        transactions.setTransactionType("Card_Request");
        transactions.setTransactionTypeDescription("Card_Request for "+agent.getUsername());
        transactions.setStatusdescription("PENDING");
        transactions.setStatus((short) 0);
        transactions.setDescription("Processing");
        transactions.setBankFrom(agent.getBankCode());
        transactions.setBankTo(bankDetails.getBankName());
        transactions.setAccountNumber(agent.getAccountNo());
        transactions.setAccountNumberTo(bankDetails.getCardRequestGlAcct()); // 3line account
        transactions.setAgentName(agent.getUsername());
        transactions.setCustomerName(agent.getFirstName()+" "+agent.getLastName());
        transactions.setDepositorName(agent.getUsername());
        transactions.setDepositorEmail(agent.getEmail());
        transactions.setDepositorPhone(agent.getPhoneNumber());
        transactions.setDescription(depositRequest.getNarration());
        transactions.setMedia("MOBILE");
        Transactions transaction = transactionRepository.save(transactions);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {


            if(bankDetails.getCardRequestFee()==null || bankDetails.getCardRequestFee().trim().equals("")
                    || Double.valueOf(bankDetails.getCardRequestFee()) <=0 ){
                throw new GravityException("Bank Cards not currently not available");
            }

            depositRequest.setAmount(String.valueOf(Double.valueOf(bankDetails.getCardRequestFee()) * Double.valueOf(cardRequestDTO.getQty())));
            depositRequest.setTotalAmount(String.valueOf(Double.valueOf(depositRequest.getAmount()) +
                    Double.valueOf(bankDetails.getCardRequestCharge())));
            depositRequest.setAccountNumber(bankDetails.getCardRequestGlAcct());
            depositRequest.setBankCode(bankDetails.getCbnCode());

            transaction.setAmount(Double.valueOf(depositRequest.getTotalAmount()));
            cardRequestDTO.setTotalAmount(transaction.getAmount());

            Response response1 = savingService.doSavingWithoutCommission(depositRequest ,transaction.getTranId() , agent.getUsername());

            DepositDTO depositDTO = new DepositDTO();
            if(response1.getRespCode().equals("00")) {
                //create card request
                cardRequestService.addCardRequest(cardRequestDTO);

                transaction.setStatusdescription("SUCCESSFUL");
                transaction.setTransactionReference(response1.getRespDescription());
                transaction.setStatus((short) 1);
                transactionRepository.save(transaction);

                String template = pr.getV("agent.deposit.success", "sms_messages.properties");
                String message = String.format(template,
                        depositRequest.getCustomerName(),transaction.getAmount(),
                        transaction.getBankTo(),transaction.getAccountNumberTo(),
                        transaction.getDepositorName(),transaction.getTransactionReference());
                try{
                    smsService.sendPlainSms(depositRequest.getCustomerPhone(),message);
                }catch(Exception e){

                }
                depositDTO.setAgentCommission("0.85");
                depositDTO.setAmount(Double.valueOf(depositRequest.getAmount()));
                depositDTO.setFreedomCharge(Double.valueOf(bankDetails.getCardRequestFee()));
                depositDTO.setRecipientAccount(depositRequest.getAccountNumber());
                depositDTO.setRecipientBank(depositRequest.getBankName());
                depositDTO.setRecipientName(depositRequest.getCustomerName() );
                depositDTO.setTranDate(transaction.getTranDate());
                depositDTO.setTransactionRef(response1.getRespDescription());
                response.setRespBody(depositDTO);
            }else {
                transaction.setStatusdescription("FAILED");
                transactions.setStatus((short) 2);
                transactionRepository.save(transaction);
            }
            return new ResponseEntity<>(response1, HttpStatus.OK);
        } catch (Exception e) {
            transactions.setStatus((short) 2);
            transaction.setStatusdescription("FAILED");
            transactionRepository.save(transaction);
            e.printStackTrace();
            response.setRespCode("96");
            response.setRespDescription(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }

    @RequestMapping(value = "/reverse", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> reverse(HttpServletRequest request, @RequestBody ReverseTransactionDTO reverseTransactionDTO) {

        Response response = new Response();

        Map<String,String> requestHeaders  = Utility.getHeaders(request);

        String appId = requestHeaders.get("app_id");
        String appKey = requestHeaders.get("app_key");


        if(appId==null || appKey==null){
            throw new GravityException("Invalid Header Tokens");
        }

        thirdPartyService.checkAppId(appId);

        ThirdPartyDto thirdPartyDto = thirdPartyService.getThridParty(appId);
        if(thirdPartyDto!=null && thirdPartyDto.getKey().equals(appKey)){
            try {

                Transactions tran  = transactionService.getAgentsTransaction(reverseTransactionDTO.getTransactionId());

                if(tran == null){
                    response.setRespCode("96");
                }else{
                        if(tran.getStatus()!=1){
                            throw new GravityException("Cannot Reverse a non-successful transaction");
                        }
                        DisputeDto depositDTO = new DisputeDto();
                        depositDTO.setTranId(Long.valueOf(tran.getTranId()));
                        depositDTO.setAction("REVERSAL");
                        depositDTO.setRaisedBy("MAGTIPON");
                        depositDTO.setApprovedBy("SYSTEM");
                        if(reverseTransactionDTO.getComment()!=null && !reverseTransactionDTO.getComment().equals("") ){
                            depositDTO.setComment(reverseTransactionDTO.getComment());
                        }else{
                            depositDTO.setComment("Reversal for: "+reverseTransactionDTO.getTransactionId());
                        }
                        walletService.manualRaiseDispute(depositDTO);

                    tran.setStatusdescription("FAILED");
                    tran.setStatus((short) 2);
                    transactionService.updateTransaction(tran);

                    response.setRespDescription("success");
                    response.setRespCode("00");
                }

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch(GravityException  e){
                e.printStackTrace();
                response.setRespCode("96");
                response.setRespDescription(e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            catch (Exception e) {
                e.printStackTrace();
                response.setRespCode("96");
                response.setRespDescription(e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }else{
            throw new GravityException("Invalid Header Tokens");
        }

    }


    @RequestMapping(value = "/reverse/manual", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> manualReversal(HttpServletRequest request) {

        Response response = new Response();

            List<WalletCreditReversal> walletCreditReversals = walletCreditReversalRepository.findAll();
            for(WalletCreditReversal creditReversal : walletCreditReversals){
                try {

                    Transactions tran  = transactionService.getAgentsTransaction(creditReversal.getRemark());

                    if(tran == null){
                        response.setRespCode("96");
                        creditReversal.setStatus("Transaction does not exist");
                        walletCreditReversalRepository.save(creditReversal);
                    }else{
                        if(tran.getStatus()!=1){
                            creditReversal.setStatus("Cannot Reverse a non-successful transaction");
                            walletCreditReversalRepository.save(creditReversal);
                            continue;
//                            throw new GravityException("Cannot Reverse a non-successful transaction");
                        }
                        DisputeDto depositDTO = new DisputeDto();
                        depositDTO.setTranId(Long.valueOf(tran.getTranId()));
                        depositDTO.setAction("REVERSAL");
                        depositDTO.setRaisedBy("MAGTIPON");
                        depositDTO.setApprovedBy("SYSTEM");
                        depositDTO.setComment("Reversal for: "+creditReversal.getRemark());

                        walletService.manualRaiseDispute(depositDTO);
//                    }
                        response.setRespDescription("success");
                        response.setRespCode("00");

                        creditReversal.setStatus("success");
                        creditReversal.setDelFlag("Y");
                        walletCreditReversalRepository.save(creditReversal);
                    }

//                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch(GravityException  e){
                    e.printStackTrace();
                    creditReversal.setStatus(e.getMessage());
                    walletCreditReversalRepository.save(creditReversal);
//                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    creditReversal.setStatus(e.getMessage());
                    walletCreditReversalRepository.save(creditReversal);
//                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }




//    @RequestMapping(value = "/fidelity/generate/otp", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<?> generateWithdrawalOTP(HttpServletRequest request, @RequestBody GenerateOTPRequest otpRequest) {
//
//        Response response = new Response();
//        GenerateOTPResponse otpResponse = new GenerateOTPResponse();
//        List<SuccessMessage>  successMessages = new ArrayList<>();
//        SuccessMessage successMessage = new SuccessMessage();
//        successMessage.setSuccessMessage("OTP Generated For Account: 0012345678");
//        successMessages.add(successMessage);
//
//        otpResponse.setSuccessMessage(successMessages);
//
//        if(!otpRequest.getAccountNumber().equals("0158955198")){
//            response.setRespDescription("Failed to send OTP");
//            response.setRespCode("96");
//        }else{
//            response.setRespDescription("success");
//            response.setRespCode("00");
//            response.setRespBody(otpResponse.getSuccessMessage().get(0).getSuccessMessage());
//        }
//
//        return new ResponseEntity<>(response,HttpStatus.OK);
//    }

//    @RequestMapping(value = "/fidelity/cashout", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<?> fidelityCashOut(HttpServletRequest request, @RequestBody WithdrawalRequest withdrawalRequest) {
//
//        Response response = new Response();
//        GenerateOTPResponse otpResponse = new GenerateOTPResponse();
//        List<SuccessMessage>  successMessages = new ArrayList<>();
//        SuccessMessage successMessage = new SuccessMessage();
//        successMessage.setSuccessMessage("OTP Generated For Account: "+withdrawalRequest.getDebitAcctNumber());
//        successMessages.add(successMessage);
//
//        otpResponse.setSuccessMessage(successMessages);
//
//        if(!withdrawalRequest.getDebitAcctNumber().equals("0158955198")){
//            response.setRespDescription("Failed to send OTP");
//            response.setRespCode("96");
//        }else{
//            Map<String,String> stringStringMap = new HashMap<>();
//            stringStringMap.put("tranId","14141");
//            stringStringMap.put("tranDate","03-09-2019");
//            response.setRespDescription("success");
//            response.setRespCode("00");
//            response.setRespBody(stringStringMap);
//        }
//
//        return new ResponseEntity<>(response,HttpStatus.OK);
//    }


//    @RequestMapping(value = "/gtb/validate/fac", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<?> validateFACCode(HttpServletRequest request, @RequestBody FACValidate facValidty) {
//
//        Response response = new Response();
//        FACValidateResponse facTranDetail = gtBankService.validateFacCode(facValidty);
//        if(facTranDetail==null){
//            response.setRespDescription("invalid faccode");
//            response.setRespCode("96");
//        }else{
//            response.setRespDescription("success");
//            response.setRespCode("00");
//            response.setRespBody(facTranDetail);
//        }
//
//        return new ResponseEntity<>(response,HttpStatus.OK);
//    }



//    @RequestMapping(value = "/savings/gtb", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<?> doGTBSaving(HttpServletRequest request, @RequestBody DepositRequest depositRequest) {
//
//        Response response = new Response();
//        depositRequest.setTotalAmount(depositRequest.getAmount());
//        Agents agent  = jwtUtility.getCurrentAgent();
//
//        try {
//
//            Response response1 = savingService.doGTBSavings(depositRequest);
//
//            DepositDTO depositDTO = new DepositDTO();
//            if(response1.getRespCode().equals("00")) {
//                depositDTO.setAgentCommission("0.85");
//                depositDTO.setAmount(Double.valueOf(depositRequest.getAmount()));
//                depositDTO.setFreedomCharge(1.40);
//                depositDTO.setRecipientBank(depositRequest.getBankName());
//                depositDTO.setRecipientName(depositRequest.getCustomerName() );
//                depositDTO.setRecipientAccount(depositRequest.getAccountNumber());
////                depositDTO.setTranDate(transaction.getTranDate());
//                depositDTO.setTransactionRef(response1.getRespDescription());
//                response.setRespBody(depositDTO);
//            }else {
//            }
//            return new ResponseEntity<>(response1, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.setRespCode("999");
//            response.setRespDescription(e.getMessage());
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        }
//
//    }

//    @RequestMapping(value = "/gtb/cashout", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<?> cashOut(HttpServletRequest request, @RequestBody  FACTranRequest  facTranRequest) {
//        Response response ;
//        Agents agent = jwtUtility.getCurrentAgent();
//        boolean isValidPin = jwtUtility.isValidAgentPin(facTranRequest.getAgentPin(),agent);
//        if(!isValidPin){
//            throw new GravityException("Invalid Agent PIN");
//        }
//
//        FACTranResponse tranResponse = gtBankService.cashOut(facTranRequest);
//
//        response = new Response();
//        response.setRespBody(tranResponse);
//        response.setRespCode("00");
//        response.setRespDescription("success");
//        return new ResponseEntity<>(response,HttpStatus.OK);
//
//    }

//    @RequestMapping(value = "/cardwithdrawals", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<?> cardWithdrawals(HttpServletRequest request, GravityWithdrawalRequest  withdrawalRequest) {
//        Response response ;
//        Agents agent = jwtUtility.getCurrentAgent();
//        boolean isValidPin = jwtUtility.isValidAgentPin(withdrawalRequest.getAgentPin(),agent);
//        if(!isValidPin){
//            throw new GravityException("Invalid Agent PIN");
//        }
//        response = withdrawalService.cardWithdrawal(new HashMap<>(), agent.getUsername(), withdrawalRequest);
//        return new ResponseEntity<>(response,HttpStatus.OK);
//
//    }
//
//    @RequestMapping(value = "/cardwithdrawals/otp", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<?> cardWithdrawalsOtp(HttpServletRequest request, OTPRequest otpRequest) {
//
//        Response response = withdrawalService.otp(otpRequest);
//        return new ResponseEntity<>(response,HttpStatus.OK);
//
//    }


    @RequestMapping(value = "/maketransfer", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> interBankTransfer(@RequestBody TransferRequest transferRequest) {

//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);

        Response response = new Response();


        Agents agent  = jwtUtility.getCurrentAgent();
        if(agent == null){
            throw new GravityException("Agent does not exist");
        }

        if (!passwordEncoder.matches(transferRequest.getAgentPin(), agent.getUserPin())) {
            throw new GravityException("Invalid Agent Pin");
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
            logger.info("sending transfer  to  transferservice {}", transferRequest.toString());
            Response response1 = banKTransferService.perFormTransfer(transferRequest, transaction.getTranId() , agent.getUsername());
            transaction.setStatusdescription("SUCCESSFUL");
            transaction.setStatus((short) 1);
            transactionRepository.save(transaction);
            return new ResponseEntity<>(response1,HttpStatus.OK) ;
        } catch (Exception e) {
            e.printStackTrace();
            //transaction.setStatusdescription("FAILED");
            //transaction.setStatus((short) 2);
            //transactionRepository.save(transaction);
            response.setRespCode("999");
            response.setRespDescription(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.OK) ;
        }

    }

    @RequestMapping(value = "/agenttransactions", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> transactionHistory( @RequestBody TransactionHistoryDTO transactionHistory)
    {
        Response response = new Response();
        Agents agent = jwtUtility.getCurrentAgent();
        transactionHistory.setAgentName(agent.getUsername());

        response.setRespBody(transactionService.getAgentsTransactionHistory(transactionHistory));
        response.setRespCode("00");
        response.setRespDescription("Success");
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "/agenttransactions/{agentId}", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> agentTransactionHistory(@RequestBody TransactionHistoryDTO transactionHistory, @PathVariable String agentId)
    {
        Response response;
        Agents agents = agentService.fetchAgentById(agentId);
        if(agents==null){
            throw new GravityException("Invalid Agent Id");
        }else{
            Agents aggregator = jwtUtility.getCurrentAgent();
            if(agents.getParentAgentId()!=aggregator.getId()){
                throw new GravityException("Agent not found for Aggregator");
            }
        }
        transactionHistory.setAgentName(agents.getUsername());
        response =  new Response();
        response.setRespBody(transactionService.getAgentsTransactionHistory(transactionHistory));
        response.setRespCode("00");
        response.setRespDescription("success");
        return new ResponseEntity<>(response,HttpStatus.OK);

    }


    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> transactionHistorySendMail(HttpServletRequest request,@RequestBody TransactionHistoryDTO transactionHistory)
    {
        Response response = transactionService.getSendEmail(transactionHistory) ;
        return new ResponseEntity<>(response,HttpStatus.OK);
    }



    @RequestMapping(value = "/nameenquiry", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> doNameEnquiry(@RequestBody NameEnquiryDTO jsonRequest){
        Response response;
        response =  nameEnquriyService.getName(jsonRequest);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/banks", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> getFinancialInstitutions(){
        Response response =  new Response();//nameEnquriyService.getName(jsonRequest);
        List<BankDetails> banksEnquiryDTO = bankDetailsService.findAllBankDetails();
        response.setRespBody(banksEnquiryDTO);
        response.setRespCode("00");
        response.setRespDescription("SUCCESS");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }






}
