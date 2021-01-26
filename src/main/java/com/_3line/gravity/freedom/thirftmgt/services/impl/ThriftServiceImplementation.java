package com._3line.gravity.freedom.thirftmgt.services.impl;


import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.setting.dto.SettingDTO;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.freedom._3linecms.dtos.CMSRequest;
import com._3line.gravity.freedom._3linecms.dtos.CMSResponse;
import com._3line.gravity.freedom._3linecms.service.CMSService;
import com._3line.gravity.freedom._3linecms.service.CreditRequest;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.bankdetails.model.TransactionType;
import com._3line.gravity.freedom.commisions.services.GravityDailyCommissionService;
import com._3line.gravity.freedom.thirftmgt.dtos.ContributionDTO;
import com._3line.gravity.freedom.thirftmgt.dtos.ThriftDTO;
import com._3line.gravity.freedom.thirftmgt.dtos.ThriftLiquidationDTO;
import com._3line.gravity.freedom.thirftmgt.models.*;
import com._3line.gravity.freedom.thirftmgt.repositories.FreedomThriftRepository;
import com._3line.gravity.freedom.thirftmgt.repositories.LiquidationHistoryRepository;
import com._3line.gravity.freedom.thirftmgt.repositories.SavingHistoryRepository;
import com._3line.gravity.freedom.thirftmgt.services.ThriftService;
import com._3line.gravity.freedom.utility.DateUtil;
import com._3line.gravity.freedom.utility.Utility;
import com._3line.gravity.freedom.wallet.dto.WalletDTO;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ThriftServiceImplementation implements ThriftService {

    @Autowired
    private FreedomThriftRepository freedomThriftRepository;
    @Autowired
    private WalletService walletService;
    @Autowired
    private SavingHistoryRepository savingHistoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AgentsRepository applicationUsersRepository;
    @Autowired
    private SettingService settingService;
    @Qualifier("gravityDailyCommissionServiceImpl")
    @Autowired
    private GravityDailyCommissionService commissionService;
    @Autowired
    private CMSService cmsService;

    @Autowired
    private LiquidationHistoryRepository liquidationHistoryRepository;
    @Value("${cms.card.comapanyreg}")
    private String cmscardcompany;

    @Autowired
    AgentService agentService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public String register(ThriftDTO registrationDTO) throws GravityException{

        if (Objects.nonNull(freedomThriftRepository.findByCardNumber(registrationDTO.getCardNumber()))) {
            throw new GravityException("Card Number Already registered for thrift");
        }

        if (Objects.nonNull(freedomThriftRepository.findByCustomerPhone(registrationDTO.getCustomerPhone()))) {
            throw new GravityException("Phone Number Already registered for thrift");
        }

        FreedomThrift thrift = convertDtoToEntity(registrationDTO);

        thrift.setRegistrationDate(new Date());
        String subbed = StringUtils.substring(registrationDTO.getCardNumber(), registrationDTO.getCardNumber().length() - 6, registrationDTO.getCardNumber().length());

        logger.info("pan gotten is {}", subbed);

        thrift.setWalletNumber(registrationDTO.getCardNumber());
        thrift.setNextliquidationDate(nextLiqDate(new Date(), thrift.getSavingCycle()));
//        logger.info("thrift obj {}", thrift.toString());
        String userId = this.generateUniqueUserId();

        thrift.setUserId(userId);
        thrift.setIdNumber(userId);
//        thrift.setCycleUID(Long.valueOf(System.currentTimeMillis()).toString());
        thrift.setLastLiquidationDate(new Date());

        /// should send to cms for card linking
        CMSRequest cmsRequest = new CMSRequest();

        cmsRequest.setAccountNum(registrationDTO.getCardNumber().substring(registrationDTO.getCardNumber().length()-10));
        cmsRequest.setAddress(registrationDTO.getCustomerAddress());
        cmsRequest.setDob(registrationDTO.getCustomerDOB());
        cmsRequest.setEmail(registrationDTO.getCustomerEmail());
        if(registrationDTO.getCustomerName().split("").length>1){
            cmsRequest.setFirstname(registrationDTO.getCustomerName().split(" ")[1]);
            cmsRequest.setSurname(registrationDTO.getCustomerName().split(" ")[0]);
        }else{
            cmsRequest.setFirstname(registrationDTO.getCustomerName().split(" ")[0]);
            cmsRequest.setSurname(registrationDTO.getCustomerName().split(" ")[0]);
        }
        cmsRequest.setIdnumber(userId);
        cmsRequest.setPan(subbed);
        cmsRequest.setUserid(userId);
        cmsRequest.setPhone(registrationDTO.getCustomerPhone());
        cmsRequest.setResidence(registrationDTO.getCustomerAddress());
        cmsRequest.setAddress(registrationDTO.getCustomerAddress());
        cmsRequest.setRegnum(cmscardcompany);

        CMSResponse response = cmsService.uploadCardDetails(cmsRequest);

        if (response.getRespcode().contains("ERR")) {
//            throw new GravityException(response.getRespdesc(), response);
            throw new GravityException(response.getRespdesc());
        }

        String wallet = walletService.createWallet(registrationDTO.getCardNumber(), "FREEDOM THRIFT WALLET(CURRENT)");
        logger.info("wallet has ben setup for thrift {}", wallet);

        freedomThriftRepository.save(thrift);

        return "Registration successful";
    }

    @Override
    public ThriftDTO checkContribution(String cardNumber) {

        FreedomThrift thrift = freedomThriftRepository.findByCardNumber(cardNumber);

        if (Objects.isNull(thrift)) {
            thrift = freedomThriftRepository.findByCustomerPhone(cardNumber);
        }
        if(thrift==null){
            throw new GravityException("Thrift Record Not Found");
        }
        return convertEntityToDto(thrift);
    }

    /**
     * For contributing into thrift wallet
     * method would set cycleUID if is null on contribution
     * and save as new cycle , also set next liquidation for the cycle from cycles first contribution date is set
     * method saves saving history for each contribution
     *
     * @param contributionDTO
     * @return
     */
    @Override
    public String contribute(ContributionDTO contributionDTO) {

        if(contributionDTO.getAmount() <= 0){
            throw new GravityException("Invalid amount entered");
        }



        FreedomThrift thrift = freedomThriftRepository.findByCardNumber(contributionDTO.getCardNumber().trim());

        if (thrift == null) {
            thrift = freedomThriftRepository.findByCustomerPhone(contributionDTO.getCardNumber());
            if(thrift == null){
                throw new GravityException("Thrift Record not found for number: "+contributionDTO.getCardNumber());
            }

            if (thrift.getCycleUID() == null) {
                logger.info("thrift starting first cycle ");
                if(contributionDTO.getAmount() < thrift.getSavingAmount()){
                    throw new GravityException("First Contribution must be greater than or equal to: "+thrift.getSavingAmount());
                }
                thrift.setStatus(ThriftStatus.TRANSACTING);
                thrift.setCycleUID(Long.valueOf(System.currentTimeMillis()).toString());
                thrift.setLastLiquidationDate(new Date());
                thrift.setNextliquidationDate(nextLiqDate(new Date(), thrift.getSavingCycle()));
            }
        }

        Agents agent = agentService.fetchAgentByAgentName(contributionDTO.getAgentName());
        //validate agent wallet against amount being credit to customer's wallet
        if(!walletService.validateWalletTransactionAmount(agent.getWalletNumber(),
                BigDecimal.valueOf(contributionDTO.getAmount()))){
            throw new GravityException("Insufficient Balance");
        }

        String tranId = Utility.generateID();

        /**
         * Debit Agent first of amount contributed
         */
        walletService.debitWallet(tranId,agent.getWalletNumber(), contributionDTO.getAmount(), "FREEDOM THRIFT ", "THRIFT CONTRIBUTION Debit Agent");
        /**
         * Credit Customer value of amount contributed
         */
        walletService.creditWallet(tranId,thrift.getWalletNumber(), contributionDTO.getAmount(), "FREEDOM THRIFT ", "THRIFT CONTRIBUTION Credit Customer");

        SavingHistory savingHistory = new SavingHistory();
        savingHistory.setAmountSaved(contributionDTO.getAmount());
        savingHistory.setCollectionDate(new Date());
        savingHistory.setThrift(thrift);
        savingHistory.setLocationLatitude(contributionDTO.getLatitude());
        savingHistory.setLocationLongitude(contributionDTO.getLongitude());
        savingHistory.setCycleUID(thrift.getCycleUID());
        savingHistoryRepository.save(savingHistory);

        thrift.setLastContributionDate(new Date());
        freedomThriftRepository.save(thrift);
        return "Contribution successful";
    }

    /**
     * Liquidate thrift for given card number
     * saves liquidation history , gets Saving History for current thrift cycleUID
     * confirms if savings was mad for period , calculates amount payeble and credits card
     * using quickteller cms service
     *
     * @param cardNumber
     * @return
     */
    @Override
    public String liquidate(String cardNumber) {

        logger.info("Beginning thrift liquidation for ---> {}", cardNumber);
        logger.info("****************************************************************************************************************************");

        FreedomThrift thrift = freedomThriftRepository.findByCardNumber(cardNumber);

        Integer daysBetween = DateUtil.DaysBetween(thrift.getLastLiquidationDate(), new Date());

        WalletDTO walletDTO = walletService.getWalletByNumber(cardNumber);

        //Begining liquidation process
        List<SavingHistory> savingHistories = savingHistoryRepository.findByCycleUID(thrift.getCycleUID());
        logger.info("----> savings history --> {}", savingHistories.toString());

        String tranId = Utility.generateID();

        if (savingHistories.size() > 0) {

            logger.info("days between {}", daysBetween);

            logger.info("----> sending request to debit wallet ->");
            walletService.debitWallet(tranId,cardNumber, walletDTO.getAvailableBalance(), "FREEDOM THRIFT", "THRIFT LIQUIDATION");
            logger.info("----> commission been generated -->");
            Double commission = 0.0;

            //Calculating commission to be split
            Double calculated = commissionDue(walletDTO.getAvailableBalance(), thrift.getSavingCycle(), savingHistories.size(), daysBetween);

            if (thrift.getSavingAmount() > calculated) {
                commission = thrift.getSavingAmount();
            } else {
                commission = calculated;
            }
            logger.info("commission for thrift is {}", commission);
            // TODO split commission and credit all actors

            commissionService.generateThriftCommission(commission, thrift.getAgent(), TransactionType.THRIFT);

            //calculate amount due customer
            Double amountDueToPayCustomer = walletDTO.getAvailableBalance() - commission;
            logger.info("amount to be credited to customers card {}", amountDueToPayCustomer);

            // convert amount payable to kobo
            amountDueToPayCustomer = amountDueToPayCustomer * 100;

            LiquidationHistory liquidationHistory = new LiquidationHistory();
            liquidationHistory.setThrift(thrift);
            liquidationHistory.setPerFormedOn(new Date());
            liquidationHistory.setAmountPayed(amountDueToPayCustomer.toString());
            liquidationHistory.setTotalBalanceBefore(walletDTO.getAvailableBalance().toString());
            liquidationHistory.setCommissionSplit(commission.toString());

            LiquidationHistory savedHistory = liquidationHistoryRepository.save(liquidationHistory);

            String amountDueToPayCustomerStr = String.format("%.2f",amountDueToPayCustomer);
            // send credit request to cms
            CreditRequest creditRequest = new CreditRequest();
            creditRequest.setAmount(amountDueToPayCustomerStr);
            creditRequest.setNarration("thrift liquidation payment");
            creditRequest.setPan(thrift.getCardNumber());
            creditRequest.setRefNumber(Long.valueOf(System.currentTimeMillis()).toString());
            creditRequest.setRegNum(cmscardcompany);
            creditRequest.setSenderInfo(thrift.getUserId());
            CMSResponse response = cmsService.fundCard(creditRequest);

            if(response.getRespdesc().equals("OK")){

                savedHistory.setCmsResponse(response.toString());
                savedHistory.setWasSuccessful(true);
                logger.info("----> log liquidation history continue -> {}", liquidationHistory.toString());

                //Audit cms response
                liquidationHistoryRepository.save(savedHistory);

                thrift.setLastLiquidationDate(new Date());
                thrift.setNextliquidationDate(nextLiqDate(new Date(), thrift.getSavingCycle()));
                thrift.setCycleUID(Long.valueOf(System.currentTimeMillis()).toString());

            }else {
                savedHistory.setWasSuccessful(false);
                liquidationHistoryRepository.save(savedHistory);
                logger.info("Error Occurred while liquidating cycle {} for thrift {}", thrift.getSavingCycle(), thrift.getCardNumber());

                walletService.creditWallet(tranId,cardNumber, walletDTO.getAvailableBalance(), "FREEDOM THRIFT", "THRIFT LIQUIDATION REVERSAL");

                throw new GravityException(response.getRespdesc());

            }

            logger.info("----> log liquidation history done -> {}", liquidationHistory.toString());
            logger.info("----> liquidation done {}", cardNumber);

            logger.info("****************************************************************************************************************************");
            //Begin new Payment cycle

//            thrift.setCycleUID(Long.valueOf(System.currentTimeMillis()).toString());

            freedomThriftRepository.save(thrift);
        } else {
            throw new GravityException("NO saving was made for this period");
        }

//        FreedomThrift thrift = freedomThriftRepository.findByCardNumber(cardNumber);
//
//        Integer daysBetween = DateUtil.DaysBetween(thrift.getLastLiquidationDate(), new Date());
//
//        thrift.setLastLiquidationDate(new Date());
//        logger.info("----> updated liquidation date ");
//
//        // Auditing liquidation
//        LiquidationHistory liquidationHistory = new LiquidationHistory();
//        liquidationHistory.setThrift(thrift);
//        liquidationHistory.setPerFormedOn(new Date());
//
//        logger.info("----> log liquidation history begin -> {}", liquidationHistory.toString());
//        WalletDTO walletDTO = walletService.getWalletByNumber(cardNumber);
//
//        liquidationHistory.setTotalBalanceBefore(walletDTO.getAvailableBalance().toString());

//        LiquidationHistory savedHistory = liquidationHistoryRepository.save(liquidationHistory);
//        //TODO take commission
//        logger.info("----> log liquidation history continue -> {}", liquidationHistory.toString());
//        //Begining liquidation process
//        List<SavingHistory> savingHistories = savingHistoryRepository.findByCycleUID(thrift.getCycleUID());
//        logger.info("----> savings history --> {}", savingHistories.toString());
//
//        String tranId = Utility.generateID();

//        if (savingHistories.size() > 0) {
//
//            logger.info("days between {}", daysBetween);
//
//            logger.info("----> sending request to debit wallet ->");
//            walletService.debitWallet(tranId,cardNumber, walletDTO.getAvailableBalance(), "FREEDOM THRIFT", "THRIFT LIQUIDATION");
//            logger.info("----> commission been generated -->");
//            Double commission = 0.0;
//
//            //Calculating commission to be split
//            Double calculated = commissionDue(walletDTO.getAvailableBalance(), thrift.getSavingCycle(), savingHistories.size(), daysBetween);
//
//            if (thrift.getSavingAmount() > calculated) {
//                commission = thrift.getSavingAmount();
//            } else {
//                commission = calculated;
//            }
//            logger.info("commission for thrift is {}", commission);
//            // TODO split commission and credit all actors
//            //Audit liqidation after commission has been split
//            savedHistory.setCommissionSplit(commission.toString());
//            liquidationHistoryRepository.save(savedHistory);
//            logger.debug("----> log liquidation history continue -> {}", liquidationHistory.toString());
//            logger.info("----> sending commission request to wallets -> ");
//            commissionService.generateThriftCommission(commission, thrift.getAgent().getUsername(), TransactionType.THRIFT);
//
//            //calculate amount due customer
//            Double amountDueToPayCustomer = walletDTO.getAvailableBalance() - commission;
//
//            logger.info("amount to be credited to customers card {}", amountDueToPayCustomer);
//
//
//            //set next liquidation and cycleUID
//            thrift.setNextliquidationDate(nextLiqDate(new Date(), thrift.getSavingCycle()));
//            thrift.setCycleUID(Long.valueOf(System.currentTimeMillis()).toString());
//            //Auditing amount to be payed
//            liquidationHistory.setAmountPayed(amountDueToPayCustomer.toString());
//            liquidationHistoryRepository.save(savedHistory);
//            logger.info("----> log liquidation history continue -> {}", liquidationHistory.toString());
//            // send credit request to cms
//
//            // convert amount payable to kobo
//            amountDueToPayCustomer = amountDueToPayCustomer * 100;
//
//            CreditRequest creditRequest = new CreditRequest();
//            creditRequest.setAmount(amountDueToPayCustomer.toString());
//            creditRequest.setNarration("thrift liquidation payment");
//            creditRequest.setPan(thrift.getCardNumber());
//            creditRequest.setRefNumber(Long.valueOf(System.currentTimeMillis()).toString());
//            creditRequest.setRegNum(cmscardcompany);
//            creditRequest.setSenderInfo(thrift.getUserId());
//            CMSResponse response = cmsService.fundCard(creditRequest);
//            //Audit cms response
//            savedHistory.setCmsResponse(response.toString());
//            liquidationHistoryRepository.save(savedHistory);
//            if (response.getRespcode().equals("0")) {
//                logger.info("liquidation pushed to card success !");
//
//                // if successfull update history to success
//                savedHistory.setWasSuccessful(true);
//                liquidationHistoryRepository.save(savedHistory);
//            } else {
//                savedHistory.setWasSuccessful(false);
//                liquidationHistoryRepository.save(savedHistory);
//                logger.info("Error Occurred while liquidating cycle {} for thrift {}", thrift.getSavingCycle(), thrift.getCardNumber());
//
//                walletService.creditWallet(tranId,cardNumber, walletDTO.getAvailableBalance(), "FREEDOM THRIFT", "THRIFT LIQUIDATION REVERSAL");
//
//                throw new GravityException(response.getRespdesc());
//            }
//
//            logger.info("----> log liquidation history done -> {}", liquidationHistory.toString());
//            logger.info("----> liquidation done {}", cardNumber);
//
//            logger.info("****************************************************************************************************************************");
//            //Begin new Payment cycle
//
//            thrift.setCycleUID(Long.valueOf(System.currentTimeMillis()).toString());
//
//            freedomThriftRepository.save(thrift);
//        } else {
//            throw new GravityException("NO saving was made for this period");
//        }
        return "Liquidation successful";
    }

    @Override
    public String prematureLiquidation(ThriftLiquidationDTO liquidationDTO) {
        // beginning premature liquidation

        String cardNumber = liquidationDTO.getCardNumber();
        logger.info("Begining thrift !premature! liquidation for ---> {}", cardNumber);
        logger.info("****************************************************************************************************************************");

        // get thrift , set last liquidation as current date create liquidation history

        FreedomThrift thrift = freedomThriftRepository.findByCardNumber(cardNumber);
        if (Objects.isNull(thrift)) {
            thrift = freedomThriftRepository.findByCustomerPhone(cardNumber);
            if(thrift == null){
                throw new GravityException("Thrift Record Not Found");
            }
        }
        Integer daysBetween = DateUtil.DaysBetween(thrift.getLastLiquidationDate(), new Date());

        logger.info("----> log liquidation history begin ");
        //get wallet information for thrift
        WalletDTO walletDTO = walletService.getWalletByNumber(thrift.getWalletNumber());

        /**
         * Beginning proper liquidation process , get saving history for given cycle if history size is 0 throw exception as
         * no savings was made for cycle else calculate days between first savings and liquidation date
         * calculate penalty for premature liquidation , add penalty to commission for liquidation
         * debit wallet with available balance calculate amount to pay customer by removing commission due from available balance
         * call card service to fund thrift card
         */
        List<SavingHistory> savingHistories = savingHistoryRepository.findByCycleUID(thrift.getCycleUID());
        logger.info("----> savings history size --> {}", savingHistories.size());

        if (savingHistories.size() > 0) {

            String tranId = Utility.generateID();

            //debit wallet
            logger.info("----> sending request to debit wallet ->");
            walletService.debitWallet(tranId,thrift.getWalletNumber(), walletDTO.getAvailableBalance(), "FREEDOM THRIFT", "THRIFT PREMATURE LIQUIDATION");
            logger.info("last liquidation {}", thrift.getLastLiquidationDate());
            logger.info("days between start of saving and now {}", daysBetween);

            Double commission = 0.0;
            //Calculating commission to be split

            Double calculated = commissionDue(walletDTO.getAvailableBalance(), thrift.getSavingCycle(), savingHistories.size(),daysBetween);
//            Double calculated = prematureCommissionDue(walletDTO.getAvailableBalance(), thrift.getSavingCycle(), daysBetween);

            if (thrift.getSavingAmount() > calculated) {
                commission = thrift.getSavingAmount();
            } else {
                commission = calculated;
            }
            logger.info("----> commission been generated -->");
            logger.info("commission is {}", commission);

            // get amount to be payed to customer
            Double amountDueToPayCustomer = walletDTO.getAvailableBalance() - commission;

            // get penalty for payeable amount
            Double prematureLiquidationPenalty = prematureLiquidationPenalty(amountDueToPayCustomer, savingHistories.get(0).getAmountSaved());

            logger.info("amount due customer is {}", amountDueToPayCustomer);
            logger.info("penalty for premature liquidation is {}", prematureLiquidationPenalty);

            commission = commission + prematureLiquidationPenalty;

            amountDueToPayCustomer = amountDueToPayCustomer - prematureLiquidationPenalty;

            logger.info("amount payable to customer after oenalty now {}", amountDueToPayCustomer);
            logger.info("combining commission and penalty {}", commission);

            // perform commission using service here
            commissionService.generateThriftCommission(commission, thrift.getAgent(), TransactionType.THRIFT);

            logger.info("amount to be credited to customers card {}", amountDueToPayCustomer);

            LiquidationHistory liquidationHistory = new LiquidationHistory();
            liquidationHistory.setThrift(thrift);
            liquidationHistory.setPerFormedOn(new Date());
            liquidationHistory.setAmountPayed(amountDueToPayCustomer.toString());
            liquidationHistory.setTotalBalanceBefore(walletDTO.getAvailableBalance().toString());
            liquidationHistory.setCommissionSplit(commission.toString());

            LiquidationHistory savedHistory = liquidationHistoryRepository.save(liquidationHistory);

            // convert amount payable to kobo since card service uses kobo
            amountDueToPayCustomer = amountDueToPayCustomer * 100;

            String amountDueToPayCustomerStr = String.format("%.2f",amountDueToPayCustomer);

            //send credit request to cms
            CreditRequest creditRequest = new CreditRequest();
            creditRequest.setAmount(amountDueToPayCustomerStr);
            creditRequest.setNarration("thrift liquidation payment");
            creditRequest.setPan(thrift.getCardNumber());
            creditRequest.setRefNumber(Long.valueOf(System.currentTimeMillis()).toString());
            creditRequest.setRegNum(cmscardcompany);
            creditRequest.setSenderInfo(thrift.getUserId());

            CMSResponse response = cmsService.fundCard(creditRequest);
            //Audit cms response
            if (response.getRespdesc().equals("OK")) {
                logger.info("liquidation pushed to card success !");

                savedHistory.setCmsResponse(response.toString());
                savedHistory.setWasSuccessful(true);
                logger.info("----> log liquidation history continue -> {}", liquidationHistory.toString());

                //Audit cms response
                liquidationHistoryRepository.save(savedHistory);

                thrift.setLastLiquidationDate(new Date());
                thrift.setNextliquidationDate(nextLiqDate(new Date(), thrift.getSavingCycle()));
                thrift.setCycleUID(Long.valueOf(System.currentTimeMillis()).toString());


            } else {
                savedHistory.setWasSuccessful(false);
                liquidationHistoryRepository.save(savedHistory);
                logger.info("Error Occurred while liquidating cycle {} for thrift {}", thrift.getSavingCycle(), thrift.getCardNumber());

                walletService.creditWallet(tranId,thrift.getWalletNumber(), walletDTO.getAvailableBalance(), "FREEDOM  THRIFT", "THRIFT LIQUIDATION REVERSAL");

                throw new GravityException(response.getRespdesc());
            }

            logger.debug("----> log liquidation history done -> {}", liquidationHistory.toString());
            logger.info("----> !premature! liquidation done {}", cardNumber);

            logger.info("****************************************************************************************************************************");


            //Begin new Payment cycle
//            thrift.setCycleUID(Long.valueOf(System.currentTimeMillis()).toString());

            freedomThriftRepository.save(thrift);



//        String cardNumber = liquidationDTO.getCardNumber();
//        logger.info("Begining thrift !premature! liquidation for ---> {}", cardNumber);
//        logger.info("****************************************************************************************************************************");
//
//        // get thrift , set last liquidation as current date create liquidation history
//
//        FreedomThrift thrift = freedomThriftRepository.findByCardNumber(cardNumber);
//        if (Objects.isNull(thrift)) {
//            thrift = freedomThriftRepository.findByCustomerPhone(cardNumber);
//            if(thrift == null){
//                throw new GravityException("Thrift Record Not Found");
//            }
//        }
//        Integer daysBetween = DateUtil.DaysBetween(thrift.getLastLiquidationDate(), new Date());
//
//        thrift.setLastLiquidationDate(new Date());
//        logger.info("----> updated liquidation date ");
//        // Auditing liquidation
//        LiquidationHistory liquidationHistory = new LiquidationHistory();
//        liquidationHistory.setThrift(thrift);
//        liquidationHistory.setPerFormedOn(new Date());
//
//        logger.info("----> log liquidation history begin ");
//        cardNumber = thrift.getCardNumber();
//        //get wallet information for thrift
//        WalletDTO walletDTO = walletService.getWalletByNumber(cardNumber);
//
//
//        // update liquidation history and saved history
//        liquidationHistory.setTotalBalanceBefore(walletDTO.getAvailableBalance().toString());
//        LiquidationHistory savedHistory = liquidationHistoryRepository.save(liquidationHistory);
//        logger.info("----> log liquidation history continue -> {}", liquidationHistory.toString());
//
//
//        /**
//         * Beginning proper liquidation process , get saving history for given cycle if history size is 0 throw exception as
//         * no savings was made for cycle else calculate days between first savings and liquidation date
//         * calculate penalty for premature liquidation , add penalty to commission for liquidation
//         * debit wallet with available balance calculate amount to pay customer by removing commission due from available balance
//         * call card service to fund thrift card
//         */
//        List<SavingHistory> savingHistories = savingHistoryRepository.findByCycleUID(thrift.getCycleUID());
//        logger.info("----> savings history size --> {}", savingHistories.size());
//
//        if (savingHistories.size() > 0) {
//
//            String tranId = Utility.generateID();
//
//            //debit wallet
//            logger.info("----> sending request to debit wallet ->");
//            walletService.debitWallet(tranId,cardNumber, walletDTO.getAvailableBalance(), "FREEDOM THRIFT", "THRIFT PREMATURE LIQUIDATION");
//            logger.info("last liquidation {}", thrift.getLastLiquidationDate());
//            logger.info("days between start of saving and now {}", daysBetween);
//
//            Double commission = 0.0;
//            //Calculating commission to be split
//
//            Double calculated = prematureCommissionDue(walletDTO.getAvailableBalance(), thrift.getSavingCycle(), daysBetween);
//
//            if (thrift.getSavingAmount() > calculated) {
//                commission = thrift.getSavingAmount();
//            } else {
//                commission = calculated;
//            }
//            logger.info("----> commission been generated -->");
//            logger.info("commission is {}", commission);
//
//
//            // get amount to be payed to customer
//            Double amountDueToPayCustomer = walletDTO.getAvailableBalance() - commission;
//
//
//            // get penalty for payeable amount
//            Double prematureLiquidationPenalty = prematureLiquidationPenalty(amountDueToPayCustomer, savingHistories.get(0).getAmountSaved());
//
//            logger.info("amount due customer is {}", amountDueToPayCustomer);
//            logger.info("penalty for premature liquidation is {}", prematureLiquidationPenalty);
//
//            commission = commission + prematureLiquidationPenalty;
//
//            amountDueToPayCustomer = amountDueToPayCustomer - prematureLiquidationPenalty;
//
//            logger.info("amount payable to customer after oenalty now {}", amountDueToPayCustomer);
//            logger.info("combining commission and penalty {}", commission);
//
//
//            // perform commission using service here
//            commissionService.generateThriftCommission(commission, thrift.getAgent().getUsername(), TransactionType.THRIFT);
//
//            //Audit liquidation after commission has been split
//            savedHistory.setCommissionSplit(commission.toString());
//            liquidationHistoryRepository.save(savedHistory);
//            logger.info("----> log liquidation history continue -> {}", liquidationHistory.toString());
//            logger.info("----> sending commission request to wallets -> ");
//
//
//            logger.info("amount to be credited to customers card {}", amountDueToPayCustomer);
//
//
//            thrift.setNextliquidationDate(nextLiqDate(new Date(), thrift.getSavingCycle()));
//            //Auditing amount to be payed
//            liquidationHistory.setAmountPayed(amountDueToPayCustomer.toString());
//            liquidationHistoryRepository.save(savedHistory);
//            logger.info("----> log liquidation history continue -> {}", liquidationHistory.toString());
//            //send credit request to cms
//
//            // convert amount payable to kobo since card service uses kobo
//            amountDueToPayCustomer = amountDueToPayCustomer * 100;
//
//            CreditRequest creditRequest = new CreditRequest();
//            creditRequest.setAmount(amountDueToPayCustomer.toString());
//            creditRequest.setNarration("thrift liquidation payment");
//            creditRequest.setPan(thrift.getCardNumber());
//            creditRequest.setRefNumber(Long.valueOf(System.currentTimeMillis()).toString());
//            creditRequest.setRegNum(cmscardcompany);
//            creditRequest.setSenderInfo(thrift.getUserId());
//
//            CMSResponse response = cmsService.fundCard(creditRequest);
//            //Audit cms response
//            savedHistory.setCmsResponse(response.toString());
//            liquidationHistoryRepository.save(savedHistory);
//            if (response.getRespcode().equals("0")) {
//                logger.info("liquidation pushed to card success !");
//
//                // if successfull update history to success
//                savedHistory.setWasSuccessful(true);
//                liquidationHistoryRepository.save(savedHistory);
//            } else {
//                savedHistory.setWasSuccessful(false);
//                liquidationHistoryRepository.save(savedHistory);
//                logger.info("Error Occurred while liquidating cycle {} for thrift {}", thrift.getSavingCycle(), thrift.getCardNumber());
//
//                walletService.creditWallet(tranId,cardNumber, walletDTO.getAvailableBalance(), "FREEDOM  THRIFT", "THRIFT LIQUIDATION REVERSAL");
//
//                throw new GravityException(response.getRespdesc());
//            }
//
//            logger.debug("----> log liquidation history done -> {}", liquidationHistory.toString());
//            logger.info("----> !premature! liquidation done {}", cardNumber);
//
//            logger.info("****************************************************************************************************************************");
//
//
//            //Begin new Payment cycle
//
//            thrift.setCycleUID(Long.valueOf(System.currentTimeMillis()).toString());
//
//            freedomThriftRepository.save(thrift);

        } else {
            throw new GravityException("No saving was made for this period");
        }
        return "Liquidation successful";
    }

    private FreedomThrift convertDtoToEntity(ThriftDTO thriftDTO) {

        FreedomThrift thrift = modelMapper.map(thriftDTO, FreedomThrift.class);
        Agents agent = applicationUsersRepository.findByUsername(thriftDTO.getAgentName());
        thrift.setSavingAmount(thrift.getSavingAmount());
        thrift.setAgent(agent);
        thrift.setSavingCycle(SavingCycle.valueOf(thriftDTO.getCycle()));

        return thrift;
    }

    @Override
    public ThriftDTO convertEntityToDto(FreedomThrift thrift) {
        ThriftDTO thriftDTO = new ThriftDTO();

        thriftDTO.setAgentName(thrift.getAgent().getUsername());
        thriftDTO.setCardNumber(thrift.getCardNumber());
        thriftDTO.setCustomerPhone(thrift.getCustomerPhone());
        thriftDTO.setCustomerAddress(thrift.getCustomerAddress());
        thriftDTO.setCustomerEmail(thrift.getCustomerEmail());
        thriftDTO.setCustomerPic(thrift.getCustomerPic());
        thriftDTO.setSavingAmount(thrift.getSavingAmount());
        thriftDTO.setCustomerName(thrift.getCustomerName());
        thriftDTO.setCustomerDOB(thrift.getCustomerDOB());
        thriftDTO.setWalletNumber(thrift.getWalletNumber());
        thriftDTO.setRegDate(DateUtil.formatDateToreadable(thrift.getRegistrationDate()));
        if (Objects.nonNull(thrift.getLastLiquidationDate())) {
            thriftDTO.setLastLiqDate(DateUtil.formatDateToreadable(thrift.getLastLiquidationDate()));
            thriftDTO.setNextLiqDate(DateUtil.formatDateToreadable(thrift.getNextliquidationDate()));
        }

        if (Objects.nonNull(thrift.getLastContributionDate())) {
            thriftDTO.setLastContDate(DateUtil.formatDateToreadable(thrift.getLastContributionDate()));
        }

        WalletDTO walletDTO = walletService.getWalletByNumber(thrift.getWalletNumber());
        thriftDTO.setCycle(thrift.getSavingCycle().name());
        thriftDTO.setSavingBalance(walletDTO.getAvailableBalance());

        return thriftDTO;
    }

    @Override
    public ThriftDTO getByCardOrPhone(String search) {

        FreedomThrift thrift = freedomThriftRepository.findByCustomerPhone(search);

        if (Objects.isNull(thrift)) {
            thrift = freedomThriftRepository.findByCardNumber(search);
        }
        return convertEntityToDto(thrift);
    }

    @Override
    public List<ThriftDTO> getAll() {
        return freedomThriftRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }


    private Date nextLiqDate(Date fromDate, SavingCycle cycle) {
        Date result = null;

        switch (cycle) {

            case WEEKLY:
                result = DateUtil.AddDays(fromDate, 7);
                break;
            case MONTHLY:
                result = DateUtil.AddDays(fromDate, 30);
                break;
            case CALENDAR_MONTH:
                result = DateUtil.getLastDayOfTheMonthFromDate(fromDate);
                break;
            case BI_ANNUAL:
                DateUtil.AddDays(fromDate, 180);
                break;
            case YEARLY:
                DateUtil.getLastDateOfTheYear(fromDate);
                break;


        }

        logger.info("date has been set to {}", result);


        return result;
    }

    /**
     * Calcualtes commission due for a saying cycle
     * If no of contribution is 1 or o , automatically commission if @contributedAmount
     * if contribution is more that 1 average over cycle is calculated
     *
     * @param contributedAmount
     * @param cycle
     * @param noOfContribution
     * @param daysBetweenConAndLiq
     * @return
     */
    private Double commissionDue(Double contributedAmount, SavingCycle cycle, Integer noOfContribution, Integer daysBetweenConAndLiq) {

        Double result = 0.0;
        switch (cycle) {

            case WEEKLY:
                if (noOfContribution > 1) {
                    result = contributedAmount / Double.valueOf("7");
                } else {
                    result = contributedAmount;
                }
                break;
            case MONTHLY:
                if (noOfContribution > 1) {
                    result = contributedAmount / Double.valueOf("30");
                } else {
                    result = contributedAmount;
                }
                break;
            case CALENDAR_MONTH:
                if (noOfContribution > 1) {
                    result = contributedAmount / Double.valueOf(daysBetweenConAndLiq);
                } else {
                    result = contributedAmount;
                }
                break;
            case BI_ANNUAL:
                if (noOfContribution > 1) {
                    result = contributedAmount / Double.valueOf("180");
                } else {
                    result = contributedAmount;
                }
                break;
            case YEARLY:
                if (noOfContribution > 1) {
                    result = contributedAmount / Double.valueOf("365");
                } else {
                    result = contributedAmount;
                }
                break;

        }

        return result;

    }

    private Double prematureCommissionDue(Double contributedAmount, SavingCycle cycle, Integer amountOfDaysSaved) {
        Double result = 0.0;
        if (amountOfDaysSaved > 1) {
            result = contributedAmount / Double.valueOf(amountOfDaysSaved);
        } else {
            result = contributedAmount;
        }

        logger.info("from calculationg methos result is {}", result);
        return result;
    }


    /**
     * Calcualte penalty for premature liquidation  , using system setup parameters
     *
     * @param amountDueCustomer
     * @param firstDaySavings
     * @return
     */
    private Double prematureLiquidationPenalty(Double amountDueCustomer, Double firstDaySavings) {

        Double result = 0.0;
        SettingDTO perc = settingService.getSettingByCode("FREEDOM_THRIFT_PREMATURE_AMOUNT");
        result = ((Double.parseDouble(perc.getValue())) / Double.parseDouble("100")) * amountDueCustomer;

        return result;
    }

//    public static void main(String[] args) {
//        ThriftServiceImplementation r = new ThriftServiceImplementation();
//        Random random = new Random();
//        String curr = String.valueOf(random.nextInt(100000));
//        String outComeSTring = StringUtils.leftPad(curr,5,"0");
//        System.out.println("res:: "+curr+" then :: "+outComeSTring);
//    }

    private String generateUniqueUserId(){

        Random random = new Random();
        String curr = String.valueOf(random.nextInt(100000));
        String generatedId = StringUtils.leftPad(curr,5,"0");
        FreedomThrift thrift = freedomThriftRepository.findByUserId(generatedId);
        while(thrift!=null){
            curr = String.valueOf(random.nextInt(100000));
            generatedId = StringUtils.leftPad(curr,5,"0");
            thrift = freedomThriftRepository.findByUserId(generatedId);
        }
        logger.info("User ID generated is :: {}",generatedId);
        return generatedId;
    }
}
