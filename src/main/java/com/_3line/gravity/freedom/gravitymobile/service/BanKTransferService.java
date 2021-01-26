package com._3line.gravity.freedom.gravitymobile.service;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.model.TransactionType;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.commisioncharge.service.CommissionChargeService;
import com._3line.gravity.freedom.commisions.services.GravityDailyCommissionService;
import com._3line.gravity.freedom.financialInstitutions.dtos.DepositRequest;
import com._3line.gravity.freedom.financialInstitutions.dtos.TransferRequest;
import com._3line.gravity.freedom.financialInstitutions.fidelity.models.IntraBankTransferResponse;
import com._3line.gravity.freedom.financialInstitutions.fidelity.requests.IntraBankTransferRequest;
import com._3line.gravity.freedom.financialInstitutions.fidelity.service.FidelityWebServices;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.models.SingleTransferAPI;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.service.GTBankService;
import com._3line.gravity.freedom.financialInstitutions.magtipon.MagtiponBankProcessor;
import com._3line.gravity.freedom.financialInstitutions.magtipon.service.MagtiponService;
import com._3line.gravity.freedom.institution.dto.InstitutionDTO;
import com._3line.gravity.freedom.institution.service.InstitutionService;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BanKTransferService {

    @Autowired
    BankDetailsService bankDetailsService;
    @Autowired
    AgentsRepository applicationUsersRepository;
    @Autowired
    MagtiponService magtiponService ;
    @Autowired
    GTBankService gtBankService;
    @Qualifier("gravityDailyCommissionServiceImpl")
    @Autowired
    GravityDailyCommissionService commissionService;

    @Qualifier("institution")
    @Autowired
    GravityDailyCommissionService instCommissionService;

    @Autowired
    MagtiponBankProcessor magtiponProcessor;
    @Autowired
    FidelityWebServices fidelityWebServices;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    private CommissionChargeService commissionChargeService ;


    @Autowired
    AgentService  agentService;

    @Autowired
    InstitutionService institutionService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //TODO transfer from integrated bank to another bank
    // take money from customers account into 3line account
    // get fee , generate commission and credit agent
    // if receveing bank is integrated , take cash from 3line account into receivng bank customer account
    // if receveing bank s not integrated , transfer with magtipon

    public Response perFormTransfer(TransferRequest transferRequest, Long tranId, String agentName) {

        Response response ;

        Agents agent = agentService.fetchAgentByAgentName(agentName);
        logger.info("################################# --  TRANSFER REQUEST STARTING --  ##################");
        logger.info(" transferRequest -- > {} tran id {}", transferRequest , tranId);

        BankDetails bankDetails = bankDetailsService.findByCode(transferRequest.getCustomerBankCode());
        BankDetails recevingBankDetails = bankDetailsService.findByCode(transferRequest.getReceivingBankCode());

        if(bankDetails == null){
            throw new GravityException("Customer bank "+ transferRequest.getCustomerBankCode() + "Not recognized");
        }

        if(recevingBankDetails == null){
            throw  new GravityException("Receiving bank "+ transferRequest.getReceivingBankCode() + "Not recognized");
        }

        /*
         * Get Transaction fee from commission Service
         */
        Double fee = commissionChargeService.getCommissionForAmount(Double.parseDouble(transferRequest.getAmount()), TransactionType.TRANSFER);


        /*
         * add transaction fee to amount to debit customer
         */

        Double amountToDebitCustomer = Double.parseDouble(transferRequest.getAmount()) + fee  ;
        Double amountToCreditCustomer = Double.parseDouble(transferRequest.getAmount()) ;

        logger.info("customer will be debited {}", amountToDebitCustomer);
        logger.info("customer will be credited {}", amountToCreditCustomer);

        Transactions transactions = transactionRepository.getOne(tranId) ;
        transactions.setFee(fee.toString());
        transactionRepository.save(transactions);

        /*
         * Moving funds from customer account to 3line account in @bankDetails
         */
        switch (bankDetails.getBankCode()){
            case "GTB":
                logger.info("initiating gt-bank transfer service------------------------");
                 moveFromCustomerTo3lineGTB(amountToDebitCustomer.toString() , transferRequest.getAmount());
                 break;
            case "FBP":
                logger.info("initiating fidelity transfer service------------------------");
                moveFromCustomerTo3lineFidelity(amountToDebitCustomer.toString() , transferRequest.getAmount());
                break;
            default:
               throw  new GravityException("Bank "+ bankDetails.getBankName()+ " has not been enrolled !" );
        }

        /*
         * Do commission splitting here and get amount customer should get
         */


        /*
         * Moving funds from 3line account to customer bank account using bank api or magtipon passing amount after deducting commission
         */
        switch (recevingBankDetails.getBankCode()){
            case "GTB":
                logger.info("initiating gt-bank transfer service------------------------");
               response = moveFrom3lineGTBtoCustomerGTB(transferRequest.getReceivingBankAccount() , amountToCreditCustomer.toString());
               break;
            case "FBP":
                logger.info("initiating gt-bank transfer service------------------------");
                response = moveFrom3lineFidelityToCustomerFidelity(transferRequest.getReceivingBankAccount(), amountToCreditCustomer.toString());
                break;
            default:
                DepositRequest depositRequest = new DepositRequest() ;
                depositRequest.setAccountNumber(transferRequest.getReceivingBankAccount());
                depositRequest.setBankCode(transferRequest.getReceivingBankCode());
                depositRequest.setAmount(amountToCreditCustomer.toString());
                depositRequest.setCustomerEmail(transferRequest.getCustomerEmail());
                depositRequest.setCustomerName(transferRequest.getCustomerName());
                depositRequest.setCustomerPhone(transferRequest.getPhoneNumber());
                response = magtiponProcessor.performDeposit(depositRequest,tranId);
        }

        if(response.getRespCode().equals("00")){
            logger.info("successful transfer splitting commission ");
            InstitutionDTO  institutionDTO = institutionService.getInstitutionByAgentId(agent.getParentAgentId());
            if(institutionDTO!=null){
                instCommissionService.generateDepositCommission(transferRequest.getCustomerBankCode(), transferRequest.getAmount(),fee, tranId, agent, TransactionType.TRANSFER);
            }else{
                commissionService.generateDepositCommission(transferRequest.getCustomerBankCode(), transferRequest.getAmount(),fee, tranId, agent, TransactionType.TRANSFER);
            }
        }

        return response ;

    }



    private void moveFromCustomerTo3lineGTB(String customerAccount , String amount){

        // customeracctnumber = account to debit , vendor account to credit
        BankDetails bankDetails = bankDetailsService.findByCode("GTB");
        SingleTransferAPI singleTransferAPI = new SingleTransferAPI();
        singleTransferAPI.setVendoracctnumber(bankDetails.get_3lineAccount());
        singleTransferAPI.setCustomeracctnumber(customerAccount);
        singleTransferAPI.setAmount(amount);
        SingleTransferAPI res = gtBankService.performSingleTransfer(singleTransferAPI) ;
        if(res.getResponseCode().equals("1000")){
            logger.info("customer {} has been debited {} from gtbank",customerAccount,amount);
        }else{
            throw new GravityException(res.getResponseMessage()) ;
        }
    }

    private void moveFromCustomerTo3lineFidelity(String customerAccount , String amount){


        BankDetails bankDetails = bankDetailsService.findByCode("FBP");

        IntraBankTransferRequest intraBankTransferRequest = new IntraBankTransferRequest();
        intraBankTransferRequest.setAmount(Double.valueOf(amount));
        intraBankTransferRequest.setSourceAccount(customerAccount);
        intraBankTransferRequest.setDestinationAccount(bankDetails.get_3lineAccount());
        intraBankTransferRequest.setNaration("Freedom network agency banking transfer request");

        IntraBankTransferResponse intraBankTransferResponse = fidelityWebServices.intraBankTransfer(intraBankTransferRequest, bankDetails.get_3lineAccount());
        Response response = new Response() ;
        if(intraBankTransferResponse.getResponseCode().equals("00")){
            response.setRespCode("00");
            response.setRespDescription(intraBankTransferResponse.getDescription());

        }else{
            throw new GravityException(intraBankTransferResponse.getDescription()) ;
        }
    }

    private Response moveFrom3lineGTBtoCustomerGTB(String customerAccount , String amount){

        // customeracctnumber = 3line account to debit , customer account to credit
        BankDetails bankDetails = bankDetailsService.findByCode("GTB");
        SingleTransferAPI singleTransferAPI = new SingleTransferAPI();
        singleTransferAPI.setVendoracctnumber(customerAccount);
        singleTransferAPI.setCustomeracctnumber(bankDetails.get_3lineAccount());
        singleTransferAPI.setAmount(amount);
        SingleTransferAPI res = gtBankService.performSingleTransfer(singleTransferAPI) ;
        if(res.getResponseCode().equals("1000")){
            logger.info("_3line account {} has been debited {} from gtbank",customerAccount,amount);

            Response response = new Response() ;
            response.setRespCode("00");
            response.setRespDescription("Transfer successfull");
            return response ;
        }else{
            throw new GravityException(res.getResponseMessage()) ;
        }
    }

    /*
     *  Move funds from 3line fidelity account to @customerAccount in fidelity
     *  @param customerAccount
     */
    private Response moveFrom3lineFidelityToCustomerFidelity(String customerAccount , String amount){
        BankDetails bankDetails = bankDetailsService.findByCode("FBP");
        IntraBankTransferRequest intraBankTransferRequest = new IntraBankTransferRequest();
        intraBankTransferRequest.setAmount(Double.valueOf(amount));
        intraBankTransferRequest.setSourceAccount(bankDetails.get_3lineAccount());
        intraBankTransferRequest.setDestinationAccount(customerAccount);
        intraBankTransferRequest.setNaration("Freedom network agency banking transfer request");

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


//    public Response performTransferToAgent(AgentTransferRequest transferRequest){
//
//        Response response;
//        switch (transferRequest.getBankCode()){
//            case "GTB":
//                logger.info("Initiating transfer to agent's GTBank account");
//                response = moveFrom3lineGTBtoCustomerGTB(transferRequest.getAccountNumber(), transferRequest.getAmount());
//                logger.info("Response returned: {}",response);
//                break;
//            case "FBP":
//                logger.info("Initiating transfer to agent's Fidelity account");
//                response = moveFrom3lineFidelityToCustomerFidelity(transferRequest.getAccountNumber(), transferRequest.getAmount());
//                logger.info("Response returned: {}",response);
//                break;
//            default:
//                throw  new GravityException("Bank "+ transferRequest.getBankCode()+ " has not been enrolled !" );
//        }
//        return response;
//    }
}
