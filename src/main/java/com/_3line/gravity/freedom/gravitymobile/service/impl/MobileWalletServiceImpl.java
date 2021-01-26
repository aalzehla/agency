package com._3line.gravity.freedom.gravitymobile.service.impl;

import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.setting.dto.SettingDTO;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.financialInstitutions.dtos.DepositRequest;
import com._3line.gravity.freedom.financialInstitutions.magtipon.MagtiponBankProcessor;
import com._3line.gravity.freedom.gravitymobile.service.MobileWalletService;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.utility.DateUtil;
import com._3line.gravity.freedom.utility.Utility;
import com._3line.gravity.freedom.wallet.dto.CreditRequestDTO;
import com._3line.gravity.freedom.wallet.dto.WalletDTO;
import com._3line.gravity.freedom.wallet.models.FreedomWallet;
import com._3line.gravity.freedom.wallet.models.FreedomWalletTransaction;
import com._3line.gravity.freedom.wallet.repository.FreedomWalletRepository;
import com._3line.gravity.freedom.wallet.repository.WalletTransactionRepository;
import com._3line.gravity.freedom.wallet.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MobileWalletServiceImpl implements MobileWalletService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WalletService walletService;
    @Autowired
    private AgentsRepository applicationUsersRepository;

    private Gson json = new Gson();
    @Autowired
    WalletTransactionRepository walletTransactionRepository ;
    @Autowired
    FreedomWalletRepository freedomWalletRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MagtiponBankProcessor magtiponProcessor;

    @Autowired
    SettingService settingService ;
    @Autowired
    BankDetailsService bankDetailsService;

    @Autowired
    TransactionRepository transactionRepository;


    @Override
    public Response getWalletBalance(String agentName) {
        Response response = new Response();
        Agents agent = applicationUsersRepository.findByUsername(agentName);
        if(Objects.isNull(agent)){
            response.setRespCode("999");
            response.setRespDescription("Agent with user name not found");
        }
        WalletDTO wallet = walletService.getWalletByNumber(agent.getWalletNumber());
        WalletDTO income = walletService.getWalletByNumber(agent.getIncomeWalletNumber());

        Map<String ,Object> res = new HashMap<>();

        res.put("trading" , wallet) ;
        res.put("income" , income) ;

        response.setRespCode("00");
        response.setRespBody(res);
        return response;
    }

    @Override
    public Response getWalletTransactionHistory(String agentName, String from, String to) {
        Response response = new Response();
        Agents agent = applicationUsersRepository.findByUsername(agentName);
        if(Objects.isNull(agent)){
            response.setRespCode("999");
            response.setRespDescription("Agent with user name not found");
            return response;
        }
        Date fromDate = DateUtil.dateFullFormat(from);
        Date toDate = DateUtil.AddDays(DateUtil.dateFullFormat(to),1);
        logger.info("i gat :: {} {} {} ",agent.getWalletNumber(),fromDate,toDate);
        List<FreedomWalletTransaction> history = walletTransactionRepository.findByWalletNumberAndTranDateBetweenOrderByTranDateDesc(agent.getWalletNumber() ,fromDate ,toDate);
        response.setRespCode("00");
        response.setRespBody(history);

        return response;
    }

    @Override
    public Response getIncomeWalletTransactionHistory(String agentName, String from, String to) {
        Response response = new Response();
        Agents agent = applicationUsersRepository.findByUsername(agentName);
        if(Objects.isNull(agent)){
            response.setRespCode("999");
            response.setRespDescription("Agent with user name not found");
            return response;
        }

        System.out.println("income wallet checked :: "+agent.getIncomeWalletNumber());
        List<FreedomWalletTransaction> history = walletTransactionRepository.findByWalletNumberAndTranDateBetweenOrderByTranDateDesc(agent.getIncomeWalletNumber() , DateUtil.dateFullFormat(from),DateUtil.AddDays(DateUtil.dateFullFormat(to),1));
        response.setRespCode("00");

        response.setRespBody(history);

        return response;
    }

    @Override
    public Response disburseFromWallet(String agentName, String amount) {
        Response response = new Response();
        Agents agent = applicationUsersRepository.findByUsername(agentName);
        if(Objects.isNull(agent)){
            response.setRespCode("999");
            response.setRespDescription("Agent with user name not found");
        }

        SettingDTO magtiponFee = settingService.getSettingByCode("WALLET_TRANSFER_MAGTIPON_FEE");

        Double amountodebit =  Double.parseDouble(amount) + Double.valueOf(magtiponFee.getValue()) ;

        WalletDTO walletDTO = walletService.getWalletByNumber(agent.getWalletNumber());

        if(walletDTO.getAvailableBalance() < amountodebit ){
            response.setRespCode("999");
            response.setRespDescription("Insuffient Balance ");

            return response ;
        }

        Transactions transactions = new Transactions();
        transactions.setTranDate(new Date());
        transactions.setStatus((short) 0);
        transactions.setInnitiatorId(agent.getId());
        transactions.setApproval(0);
        transactions.setAmount(Double.valueOf(amount));
        transactions.setBeneficiary(agent.getComAccountBank());
        transactions.setLatitude("");
        transactions.setLongitude("");
        transactions.setTerminalId("");

        transactions.setStatusdescription("SUCCESSFUL");
        transactions.setBankFrom(agent.getBankCode());
        transactions.setDescription("Disburse From wallet");
        transactions.setMedia("MOBILE");
        transactions.setTransactionType("Wallet Disbursement");


        transactions.setTransactionTypeDescription("Wallet Disbursement to "+agent.getComAccountBank());


        Transactions transaction = transactionRepository.save(transactions);


        logger.info("beginning debit..............");


        walletService.debitWallet(String.valueOf(transaction.getTranId()),agent.getWalletNumber(), Double.valueOf(amount) , "MOBILE", "TRANSFER REQUEST FROM WALLET ");

        try {

            // complete leg with magtipon

            BankDetails bankDetails = bankDetailsService.findByCode(agent.getComAccountBank());
            DepositRequest depositRequest = new DepositRequest();
            depositRequest.setAccountNumber(agent.getComAccountNo());
            depositRequest.setBankCode(bankDetails.getCbnCode());

            logger.info("crediting agent with {}", amount);
            depositRequest.setAmount(amount);
            depositRequest.setCustomerEmail(agent.getEmail());
            depositRequest.setCustomerName(agent.getFirstName() + " " + agent.getLastName());
            depositRequest.setCustomerPhone(agent.getPhoneNumber());
            logger.info("payload to magtipon ?{}", depositRequest.toString());

            Response resp = magtiponProcessor.performDeposit(depositRequest,transaction.getTranId());

            if(resp.getRespCode().equals("00")) {
                try {
                    walletService.debitWallet(String.valueOf(transaction.getTranId()),agent.getWalletNumber(), Double.valueOf(magtiponFee.getValue()), "MOBILE", "TRANSFER REQUEST FROM WALLET FEE ");
                }catch (Exception e){
                    e.printStackTrace();
                }
                return resp ;
            }else {
                response.setRespDescription(resp.getRespDescription());
                response.setRespCode("999");
                walletService.creditWallet(String.valueOf(transaction.getTranId()),agent.getWalletNumber(), Double.valueOf(amount), "MOBILE", "TRANSFER REQUEST FROM WALLET REFUND");
                return response ;
            }

        }catch (Exception e) {
            e.printStackTrace();
            response.setRespDescription(e.getMessage());
            response.setRespCode("999");

        }

        return  response ;
    }

    @Override
    public Response disburseFromInWallet(String agentName, String amount) {
        // disbursement from income wallet not allowed !
        return disburseFromWallet(agentName , amount) ;
    }

    @Override
    public Response transferBetweenWallets(String agentName, String amount, String fromWallet, String toWallet,String remark,String requestId) {
        Response response = new Response();

        Transactions previousTran = transactionRepository.findByWebpayTranReference(requestId);
        if(previousTran!=null){
            response.setRespCode("96");
            response.setRespDescription("Duplicate Transaction");
            response.setRespBody(previousTran);
            return response;
        }

        Agents agent = applicationUsersRepository.findByUsername(agentName);
        if(Objects.isNull(agent)){
            response.setRespCode("96");
            response.setRespDescription("Agent with user name not found");
        }

        Transactions transactions = new Transactions();
        transactions.setTranDate(new Date());
        transactions.setStatus((short) 0);

        transactions.setInnitiatorId(agent.getId());
        transactions.setApproval(0);
        transactions.setAmount(Double.valueOf(amount));
        transactions.setBeneficiary(toWallet);
        transactions.setLatitude("");
        transactions.setLongitude("");
        transactions.setTerminalId("");
        transactions.setAgentName(agentName);


        transactions.setStatusdescription("SUCCESSFUL");
        transactions.setBankFrom("");
        transactions.setDescription(remark==null?"Transfer Between Wallets":remark);
        transactions.setMedia("MOBILE");
        transactions.setTransactionType("Wallet Transfer");
        transactions.setWebpayTranReference(requestId);

        Agents agents = applicationUsersRepository.findByWalletNumber(toWallet);
        if(agents!=null){
            transactions.setCustomerName(agents.getUsername());
        }





        transactions.setTransactionTypeDescription("Wallets Transfer to "+toWallet);


        Transactions transaction = transactionRepository.save(transactions);

        try {

            walletService.transferWalletFunds(String.valueOf(transaction.getTranId()),fromWallet, toWallet, Double.valueOf(amount), "MOBILE", "TRANSFER TO WALLET { "+toWallet +"}");
            transactions.setStatus((short)1);
            transactions.setStatusdescription("SUCCESSFUL");
            transaction = transactionRepository.save(transactions);
            response.setRespCode("00");
            response.setRespDescription("successfull");
            response.setRespBody(transaction);

        }catch (Exception e){
            transactions.setStatus((short)2);
            transactions.setStatusdescription("FAILED");
            transactionRepository.save(transactions);
            response.setRespCode("96");
            response.setRespDescription(e.getMessage());
        }

        return response;
    }

    @Override
    public Response creditRequest(CreditRequestDTO creditRequestDTO) {
        Response response = new Response();

        try{
            Agents agent  = applicationUsersRepository.findByUsername(creditRequestDTO.getAgentName());
            creditRequestDTO.setWalletNumber(agent.getWalletNumber());
            creditRequestDTO.setAgentEmail(agent.getEmail());
            walletService.createCreditRequest(creditRequestDTO);
            response.setRespCode("00");
        }catch (Exception e){
            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getCreditRequesthistory(String agentName) {
        Response response = new Response();

        try{

           List<CreditRequestDTO> data = walletService.getAllAgentRequest(agentName) ;
            response.setRespCode("00");
            response.setRespBody(json.toJson(data));
        }catch (Exception e){
            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription(e.getMessage());
        }
        return response;
    }
}
