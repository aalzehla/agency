package com._3line.gravity.freedom.gravitymobile.service.impl;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.bankdetails.model.TransactionType;
import com._3line.gravity.freedom.commisioncharge.service.CommissionChargeService;
import com._3line.gravity.freedom.commisions.services.GravityDailyCommissionService;
import com._3line.gravity.freedom.financialInstitutions.dtos.GravityWithdrawalRequest;
import com._3line.gravity.freedom.financialInstitutions.opay.models.OTPRequest;
import com._3line.gravity.freedom.financialInstitutions.opay.service.OpayService;
import com._3line.gravity.freedom.gravitymobile.service.WithdrawalService;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.utility.PropertyResource;
import com._3line.gravity.freedom.wallet.service.WalletService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
public class OpayCardWithdrawalService implements WithdrawalService {
    private Gson json = new Gson();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PropertyResource pr ;

    @Autowired
    AgentsRepository applicationUsersRepository;

    @Autowired
    TransactionRepository transactionRepository ;

    @Qualifier("gravityDailyCommissionServiceImpl")
    @Autowired
    GravityDailyCommissionService commissionService;

    @Autowired
    OpayService opayService;

    @Autowired
    WalletService walletService ;

    @Autowired
    private CommissionChargeService commissionChargeService ;

    public Response cardWithdrawal(Map<String, String> param, String username, GravityWithdrawalRequest withdrawalsRequest) {
        logger.info("##############################-- BEGINING OPAY CARD WITHDRAWAL-- ########");

        Response response = new Response() ;


        Double amnt;
        try{
            amnt = Double.valueOf(withdrawalsRequest.getAmount());
            if(amnt<=0){
                throw new GravityException("Invalid Amount specified");
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new GravityException("");
        }


        try {
            boolean isVisa = false;
            if (withdrawalsRequest.getCardCVV().equals("")) {
                logger.info("card is not a visa card");
            } else {
                isVisa = true;
                withdrawalsRequest.setCardCVC(withdrawalsRequest.getCardCVV());
            }


            Agents applicationUsers = applicationUsersRepository.findByUsername(withdrawalsRequest.getUsername());
            logger.info("agent is  {}", applicationUsers.toString());
            Transactions transactions = new Transactions();
            transactions.setTranDate(new Date());
            transactions.setStatus((short) 0);
            transactions.setInnitiatorId(applicationUsers.getId());
            transactions.setApproval(0);
            transactions.setAmount(Double.valueOf(withdrawalsRequest.getAmount()));
            transactions.setBalancebefore(Objects.nonNull(param.get("balance")) ? Double.valueOf(param.get("balance")) : 0.0);
            transactions.setBeneficiary(withdrawalsRequest.getCardNumber());
            transactions.setLatitude(withdrawalsRequest.getLatitude());
            transactions.setLongitude(withdrawalsRequest.getLongitude());
            transactions.setMaskedPan(withdrawalsRequest.getCardNumber());
            transactions.setTransactionType("Withdrawal");
            transactions.setTransactionTypeDescription("Card Withdrawal");
            transactions.setStatusdescription("PENDING");
            transactions.setDescription("Opay Card withdrawal");
            transactions.setAgentName(username);
            transactions.setCustomerName("");
            transactions.setMedia(withdrawalsRequest.getMedia());
            Transactions transaction = transactionRepository.save(transactions);

            if (withdrawalsRequest.getTranId() == null || withdrawalsRequest.getTranId() == 0) {
                logger.info("new opay tran");
                logger.info("getting fee fo transaction from service");
                /*
                 * Get Transaction fee from commission Service
                 */
                Double fee = commissionChargeService.getCommissionForAmount(Double.parseDouble(withdrawalsRequest.getAmount()), TransactionType.WITHDRAWAL);

                logger.info("fee is {}", fee);
                transaction.setFee(fee.toString());
                transactionRepository.save(transaction);
                logger.info("would add fee to transaction amount for opay");
                withdrawalsRequest.setAmount( Double.valueOf((Double.parseDouble(withdrawalsRequest.getAmount()) + fee)).toString());
                logger.info("transaction amount will now be {}", withdrawalsRequest.getAmount());
            } else {
                logger.info("seems ran has already had commison split {}", withdrawalsRequest.getTranId());
            }

            String token = opayService.createTransaction(withdrawalsRequest);
            Map result = new HashMap();
            result.put("tranId", transaction.getTranId());
            if (isVisa) {
                String tokenSend = opayService.input_pin(token, withdrawalsRequest.getCustomerPin());
                response.setRespCode("00");
                response.setRespDescription("Input otp");
                result.put("token", tokenSend);
                response.setRespBody(result);

            } else {
                response.setRespCode("00");
                response.setRespDescription("Input otp");
                result.put("token", token);
                response.setRespBody(result);
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("Opay error : Please make sure your entering a valid card number");
            return response;
        }
    }

    public Response otp( OTPRequest otpRequest) {
//        OTPRequest otpRequest = null;
        Response response = new Response() ;

//        try {
//
//            otpRequest = json.fromJson(jsonRequest, OTPRequest.class);
//        } catch (Exception e) {
//            response.setRespCode("117");
//            response.setRespDescription(pr.getV("117", "response.properties"));
//            logger.warn(response.getRespDescription());
//            return response;
//        }

        Transactions transactions = transactionRepository.getOne(otpRequest.getTranId());

        try {
            String token = opayService.input_otp(otpRequest.getToken(), otpRequest.getOtp());
            response.setRespCode("00");
            response.setRespDescription(token);
            response.setRespBody(token);
            transactions.setStatusdescription("SUCCESSFUL");
            transactions.setStatus((short) 1);
            transactionRepository.save(transactions);

            //Credit agent wallet with amount parted with '

            Agents agent = applicationUsersRepository.getOne(transactions.getInnitiatorId());

            walletService.creditWallet(String.valueOf(transactions.getTranId()),agent.getWalletNumber() , transactions.getAmount() ,"MOBILE","WITHDRAWAL ");
//            commissionService.generateDepositCommission(agent.getBankCode(), transactions.getAmount().toString(), transactions.getTranId(), agent.getUsername(), TransactionType.WITHDRAWAL);

            return response;
        } catch (Exception e) {
            transactions.setStatusdescription("FAILED");
            transactions.setStatus((short) 2);
            transactionRepository.save(transactions);
            response.setRespCode("999");
            response.setRespDescription(e.getMessage());
            return response;
        }
    }
}
