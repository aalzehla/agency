package com._3line.gravity.freedom.gravitymobile.service;


import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.model.TransactionType;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.commisioncharge.service.CommissionChargeService;
import com._3line.gravity.freedom.commisions.services.GravityDailyCommissionService;
import com._3line.gravity.freedom.financialInstitutions.dtos.DepositRequest;
import com._3line.gravity.freedom.financialInstitutions.fidelity.models.IntraBankTransferResponse;
import com._3line.gravity.freedom.financialInstitutions.fidelity.requests.IntraBankTransferRequest;
import com._3line.gravity.freedom.financialInstitutions.fidelity.service.FidelityWebServices;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.models.SingleTransferAPI;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.service.GTBankService;
import com._3line.gravity.freedom.financialInstitutions.service.BankProcessor;
import com._3line.gravity.freedom.financialInstitutions.wemaapi.model.DepositAndWithdrawal;
import com._3line.gravity.freedom.financialInstitutions.wemaapi.service.WemaApiService;
import com._3line.gravity.freedom.institution.dto.InstitutionDTO;
import com._3line.gravity.freedom.institution.service.InstitutionService;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.wallet.dto.WalletDTO;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BankSavingService {

    @Autowired
    BankDetailsService bankDetailsService;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    @Qualifier("magtipon")
    BankProcessor magtiponProcessor;

    @Autowired
    GravityDailyCommissionService commissionService;


    @Autowired
    GTBankService gtBankService ;
    @Autowired
    AgentsRepository applicationUsersRepository;
    @Autowired
    FidelityWebServices fidelityWebServices;
    @Autowired
    WalletService walletService ;
    @Autowired
    WemaApiService wemaApiService;
    @Autowired
    SettingService settingService ;

    @Autowired
    InstitutionService  institutionService;

    @Autowired
    private CommissionChargeService commissionChargeService ;

    private Logger logger = LoggerFactory.getLogger(this.getClass()) ;

    /**
     * Service form peforming saving / deposit request on core
     * Takes in @depositRequest , @tranId and @agentName
     * Gets @fee , transfer amount + @fee
     * debit agent account to get the money
     * send money to coresponding bank
     * on success , generate commission , and credit agent,s wallet with commission
     *
     */
    public Response doSaving(DepositRequest depositRequest, Long tranId, String agentName) {
        Agents agent = applicationUsersRepository.findByUsername(agentName) ;
        logger.info("########################################### -- BEGINING SAVINGS -- ######################################");
        logger.info("deposit request ---- {}",depositRequest);

        Response response;
        /*
         * Get Transaction fee from commission Service
         */
        Double fee;

        logger.info("agent id :: "+agent.getParentAgentId());
        InstitutionDTO institutionDTO = institutionService.getInstitutionByAgentId(agent.getParentAgentId());
        if(institutionDTO!=null){
            fee = commissionChargeService.getInstCommissionForAmount(Double.parseDouble(depositRequest.getAmount()),TransactionType.SAVING,institutionDTO.getName());
        }else{
            fee = commissionChargeService.getCommissionForAmount(Double.parseDouble(depositRequest.getAmount()), TransactionType.SAVING);
        }

        /*
         * Get Receiving bank to receive amount
         */
        BankDetails bankDetails = bankDetailsService.findByCode(depositRequest.getBankCode());


        /*
         * Securing fund with added fee from agent account or wallet
         * Move money from agent account to 3lines disposition with fee added after commission generation
         */
        try {
            logger.info("securing funds from agent account -------------");
            moveFromAgentTo3line(agentName, Double.valueOf(Double.parseDouble(depositRequest.getAmount()) + fee).toString(),String.valueOf(tranId));


        }catch (Exception e){
            response = new Response() ;
            logger.info("Error debiting agent account {}", e.getMessage());
            response.setRespCode("999");
            response.setRespDescription(e.getMessage());
            return response ;
        }

        /*
         * After funds has been secured ...
         * complete saving using Bank Service or magtipon
         */
        switch (bankDetails.getBankCode()){
//            case "GTB":
//                logger.info("initiating gt-bank savings service------------------------");
//                try {
//                    response = doGTBSavings(depositRequest);
//                    logger.info("--- GTB saving service done calling response {}", response);
//                }catch (Exception e){
//                    e.printStackTrace();
//                    logger.info("exception occured while performing gtbank savings , completing savings with magtipon");
//                    depositRequest.setBankCode(bankDetails.getCbnCode());
//                    response = magtiponProcessor.performDeposit(depositRequest);
//                }
//                break;
//            case "FBP":
//                logger.info("initiating fidelity-bank savings service------------------------");
//                try {
//                    response = doFBPSavings(depositRequest);
//                    logger.info("--- FIDELITY saving service done calling response {}", response);
//                }catch (Exception e){
//                    e.printStackTrace();
//                    logger.info("exception occured while performing fidelity savings , completing savings with magtipon");
//                    depositRequest.setBankCode(bankDetails.getCbnCode());
//                    response = magtiponProcessor.performDeposit(depositRequest);
//                }
//                break;
//
//            case "WEMA":
//                logger.info("initiating WEMA-bank savings service------------------------");
//                try {
//                    response = doWEMASavings(depositRequest);
//                    logger.info("--- WEMA saving service done calling response {}", response);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    logger.info("exception occured while performing wema savings , completing savings with magtipon");
//                    depositRequest.setBankCode(bankDetails.getCbnCode());
//                    response = magtiponProcessor.performDeposit(depositRequest);
//                }
//                break;
            default:
                logger.info("completing transaction with magtipon funds transfer", bankDetails.getBankCode());
                depositRequest.setBankCode(bankDetails.getCbnCode());
                logger.info("deposit is  {}", depositRequest);
                response = magtiponProcessor.performDeposit(depositRequest,tranId);
        }

        logger.info("tran response -- > {}", response);

        Transactions transactions = transactionRepository.getOne(tranId) ;
        transactions.setFee(fee.toString());

        if(response.getRespCode().equals("00")) {

            /*
             * Split commission after successfull tranaction
             */
            logger.info("successfull transaction , splitting commission ------");
            if(institutionDTO!=null){
                commissionService.generateInstDepositCommission(bankDetails.getBankCode(), depositRequest.getAmount(),fee, tranId, agent, TransactionType.SAVING,institutionDTO);
            }else{
                commissionService.generateDepositCommission(bankDetails.getBankCode(), depositRequest.getAmount(),fee, tranId, agent, TransactionType.SAVING);
            }

            //get wallet balance and attach to response body

            Map  balance = new  HashMap();
            WalletDTO trading  = walletService.getWalletByNumber(agent.getWalletNumber());
            balance.put("trading", trading.getAvailableBalance());
            WalletDTO income  = walletService.getWalletByNumber(agent.getIncomeWalletNumber());
            balance.put("income", income.getAvailableBalance());
            response.setRespBody(balance);

//            transactions.setStatusdescription("SUCCESSFUL");
//            transactions.setStatus((short) 1);
//            transactionRepository.save(transactions);


        }else {
            logger.info("transaction not successful , funds to be returned to agent account");
            // reversing transaction , debit gl and credit wallet
            walletService.creditWallet(String.valueOf(transactions.getTranId()),agent.getWalletNumber(), Double.parseDouble(depositRequest.getAmount()) + fee , "MOBILE", "DEPOSIT TRAN SECURE(REFUND)");
//
//            transactions.setStatusdescription("FAILED");
//            transactions.setStatus((short) 2);
//            transactionRepository.save(transactions);
        }
        /*
         * Updating fee on transaction
         */

//        transactionRepository.save(transactions);

        logger.info("########################################### -- ENDED SAVINGS -- ######################################");
        return response ;

    }


    public Response doSavingWithoutCommission(DepositRequest depositRequest, Long tranId, String agentName) {
        Agents agent = applicationUsersRepository.findByUsername(agentName) ;
        logger.info("########################################### -- BEGINING SAVINGS -- ######################################");
        logger.info("deposit request ---- {}",depositRequest);

        Response response;

        /*
         * Securing fund with added fee from agent account or wallet
         * Move money from agent account to 3lines disposition with fee added after commission generation
         */
        try {
            logger.info("securing funds from agent account -------------");
            moveFromAgentTo3line(agentName, Double.valueOf(Double.parseDouble(depositRequest.getTotalAmount())).toString(),String.valueOf(tranId));


        }catch (Exception e){
            response = new Response() ;
            logger.info("Error debiting agent account {}", e.getMessage());
            response.setRespCode("999");
            response.setRespDescription(e.getMessage());
            return response ;
        }


        logger.info("completing transaction with magtipon funds transfer using cbn code {}", depositRequest.getBankCode());
        depositRequest.setBankCode(depositRequest.getBankCode());
        logger.info("deposit is  {}", depositRequest);
        response = magtiponProcessor.performDeposit(depositRequest,tranId);


        logger.info("tran response -- > {}", response);

        Transactions transactions = transactionRepository.getOne(tranId) ;
        transactions.setFee(String.valueOf(Double.valueOf(depositRequest.getTotalAmount()) - Double.valueOf(depositRequest.getAmount())));

        if(response.getRespCode().equals("00")) {

            Map  balance = new  HashMap();
            WalletDTO trading  = walletService.getWalletByNumber(agent.getWalletNumber());
            balance.put("trading", trading.getAvailableBalance());
            WalletDTO income  = walletService.getWalletByNumber(agent.getIncomeWalletNumber());
            balance.put("income", income.getAvailableBalance());
            response.setRespBody(balance);
        }else {
            logger.info("transaction not successful , funds to be returned to agent account");
            // reversing transaction , debit gl and credit wallet
            walletService.creditWallet(String.valueOf(transactions.getTranId()), agent.getWalletNumber(), Double.parseDouble(depositRequest.getTotalAmount()), "MOBILE", "DEPOSIT TRAN SECURE(REFUND)");

        }

        logger.info("########################################### -- ENDED SAVINGS -- ######################################");
        return response ;

    }


    public Response doGTBSavings(DepositRequest depositRequest){

        //TODO move money from 3line account to customers account ;
        BankDetails bankDetails = bankDetailsService.findByCode("GTB");
        SingleTransferAPI singleTransferAPI = new SingleTransferAPI();
        singleTransferAPI.setVendoracctnumber(depositRequest.getAccountNumber());
        singleTransferAPI.setCustomeracctnumber(bankDetails.get_3lineAccount());
        singleTransferAPI.setAmount(depositRequest.getAmount());

        SingleTransferAPI res = gtBankService.performSingleTransfer(singleTransferAPI) ;
        Response response = new Response() ;
        if(res.getResponseCode().equals("1000")){
            response.setRespCode("00");
            response.setRespDescription(res.getResponseMessage());
            return response ;
        }else{
            throw new GravityException(res.getResponseMessage()) ;
        }

    }

    public Response doFBPSavings(DepositRequest depositRequest ){

        //TODO move money from 3line account to customers account ;
        BankDetails bankDetails = bankDetailsService.findByCode("FBP");
        IntraBankTransferRequest intraBankTransferRequest = new IntraBankTransferRequest();
        intraBankTransferRequest.setAmount(Double.valueOf(depositRequest.getAmount()));
        intraBankTransferRequest.setSourceAccount(bankDetails.get_3lineAccount());
        intraBankTransferRequest.setDestinationAccount(depositRequest.getAccountNumber());
        intraBankTransferRequest.setNaration("Freedom network agency banking deposit request");

        IntraBankTransferResponse intraBankTransferResponse = fidelityWebServices.intraBankTransfer(intraBankTransferRequest, bankDetails.get_3lineAccount());
        Response response = new Response() ;
        if(intraBankTransferResponse.getResponseCode().equals("00")){
            response.setRespCode("00");
            response.setRespDescription(intraBankTransferResponse.getDescription());
            return response ;
        }else{
            throw new GravityException(intraBankTransferResponse.getDescription()) ;
        }

    }

    public Response doWEMASavings(DepositRequest depositRequest) {

        //TODO move money from 3line account to customers account ;
        BankDetails bankDetails = bankDetailsService.findByCode("FBP");

        DepositAndWithdrawal depositAndWithdrawal = new DepositAndWithdrawal();
        depositAndWithdrawal.setCustomerAccToCredit(depositRequest.getAccountNumber());
        depositAndWithdrawal.setAmount(depositRequest.getAmount());
        DepositAndWithdrawal depositAndWithdrawalResponse = wemaApiService.makeDeposit(depositAndWithdrawal);

        Response response = new Response();
        if (depositAndWithdrawalResponse.getResponseCode().equals("00")) {
            response.setRespCode("00");
            response.setRespDescription(depositAndWithdrawalResponse.getResponseCode());
            return response;
        } else {
            throw new GravityException(depositAndWithdrawalResponse.getResponseCode());
        }

    }


    /**
     *  Method debits agent wallet to secure funds for transaction
     * @param agentUsername
     * @param amount
     */

    private void  moveFromAgentTo3line(String agentUsername , String amount,String tranId){
        Agents agent = applicationUsersRepository.findByUsername(agentUsername) ;
        // debit agent
        walletService.debitWallet(tranId,agent.getWalletNumber(), Double.valueOf(amount), "MOBILE", "DEPOSIT TRAN SECURE FUND");

    }
}
