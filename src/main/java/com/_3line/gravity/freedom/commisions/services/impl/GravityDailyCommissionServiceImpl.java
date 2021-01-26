package com._3line.gravity.freedom.commisions.services.impl;


import com._3line.gravity.core.email.service.MailService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.setting.dto.SettingDTO;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.model.TransactionType;
import com._3line.gravity.freedom.bankdetails.repository.BankDetailsRepository;
import com._3line.gravity.freedom.billpayment.models.BillOption;
import com._3line.gravity.freedom.billpayment.repository.BillOptionsRepo;
import com._3line.gravity.freedom.commisioncharge.service.CommissionChargeService;
import com._3line.gravity.freedom.commisions.dto.GravityDailyCommissionDTO;
import com._3line.gravity.freedom.commisions.models.*;
import com._3line.gravity.freedom.commisions.repositories.AgentTotalCommissionRepo;
import com._3line.gravity.freedom.commisions.repositories.GravityDailyCommssionRepo;
import com._3line.gravity.freedom.commisions.repositories.GravityDailyTotalCommissionRepo;
import com._3line.gravity.freedom.commisions.services.GravityDailyCommissionService;
import com._3line.gravity.freedom.institution.dto.InstitutionDTO;
import com._3line.gravity.freedom.utility.ExportUtility;
import com._3line.gravity.freedom.utility.Utility;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GravityDailyCommissionServiceImpl implements GravityDailyCommissionService, InitializingBean {

    @Autowired
    private BankDetailsRepository bankDetailsRepository;

    @Autowired
    private GravityDailyCommssionRepo commssionRepo;

    @Autowired
    private GravityDailyTotalCommissionRepo gravityDailyTotalCommissionRepo;

    @Autowired
    BillOptionsRepo billOptionsRepo;

    @Autowired
    private AgentsRepository AgentsRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private AgentTotalCommissionRepo agentTotalCommissionRepo;

    @Autowired
    private SettingService settingService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommissionChargeService commissionChargeService ;

    private DecimalFormat df = new DecimalFormat("#");

    @Autowired
    WalletService walletService;



    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /*
       Generates Commission splitting for transation returns amount to pay customer
     */
    @Override
    public String generateDepositCommission(String bankCode, String amount,Double fee, Long tranId, Agents agent, TransactionType transactionType) {
        BankDetails bank = bankDetailsRepository.findByBankCode(bankCode);



        logger.info("transaction type {}", transactionType);
        logger.info(" fee for amount {} is {}" , amount , fee);


        /*
         * Generate Commission Splitting using formula setup for bank
         */

        Double agentAmount = 0.0;
        Double parentagentAmount = 0.0;
        Double _3lineAmount = 0.0;
        Double bankAmount = 0.0;

        SettingDTO agentCom = settingService.getSettingByCode("AGENT_COMMISSION_FEE_PERCENTAGE");
        SettingDTO subagentCom = settingService.getSettingByCode("SUB_AGENT_COMMISSION_FEE_PERCENTAGE");
        SettingDTO parentAgentCom = settingService.getSettingByCode("PARENT_AGENT_COMMISSION_FEE_PERCENTAGE");
        SettingDTO _3lineCom =  settingService.getSettingByCode("3LINE_COMMISSION_FEE_PERCENTAGE");

        GravityDailyCommission dailyCommission = new GravityDailyCommission();

        if(Objects.isNull(agent.getParentAgentId()) || agent.getParentAgentId() == 0){
            logger.info("agent does not have parent agent ");
            logger.info("commssion will be {}%", agentCom.getValue());
            agentAmount = (Double.parseDouble(agentCom.getValue()) / Double.parseDouble("100")) * fee;
            _3lineAmount = (Double.parseDouble(_3lineCom.getValue()) / Double.parseDouble("100")) * fee;

            /**Since parent agent does not exist add income to that of 3line*/
            _3lineAmount = (Double.parseDouble(parentAgentCom.getValue()) / Double.parseDouble("100")) * fee + _3lineAmount;
            parentagentAmount = 0.00;

        }else {
            logger.info("agent is a sub agent ");
            logger.info("commssion will be {}%", subagentCom.getValue());
            logger.info("parent commssion will be {}%", parentAgentCom.getValue());
            agentAmount = (Double.parseDouble(subagentCom.getValue()) / Double.parseDouble("100")) * fee;
            _3lineAmount = (Double.parseDouble(_3lineCom.getValue()) / Double.parseDouble("100")) * fee;
            parentagentAmount = (Double.parseDouble(parentAgentCom.getValue()) / Double.parseDouble("100")) * fee;


        }



        logger.info("agent will get {} , _3line will get {}", df.format(agentAmount), df.format(_3lineAmount));

        dailyCommission.set_3lineCommission(_3lineAmount.toString());
        dailyCommission.setAgentBank(bank.getBankName());
        dailyCommission.setAgentBankCode(bank.getBankCode());
        dailyCommission.setAgentCommission(agentAmount.toString());
        dailyCommission.setParentAgentCommission(parentagentAmount.toString());
        dailyCommission.setAgentName(agent.getUsername());
        dailyCommission.setAgentAccount(agent.getAccountNo());
        dailyCommission.setTransactionAmount( amount );
        dailyCommission.setTransactionId(tranId.toString());
        dailyCommission.setTransactionType(transactionType);

        logger.info("this is how commission now looks like {}", dailyCommission);

        commssionRepo.save(dailyCommission);

        // credit agent wallet as well as parent agent wallet
         walletService.creditWallet(String.valueOf(tranId),agent.getIncomeWalletNumber() , agentAmount,"FREEDOM NETWORK","AGENT COMMISION "+transactionType+" {"+tranId+"}");

         //credit parent agent if necessary
        try {
            if (Objects.nonNull(agent.getParentAgentId()) && agent.getParentAgentId() != 0) {
                Agents parent = AgentsRepository.getOne(agent.getParentAgentId());
                /// SUB AGGREGATOR IMPLEMENTATION
                if(parent!=null && parent.getAgentType().equals("SUBAGGREGATOR")){

                    logger.info("parent agent has a super aggregator");

                    SettingDTO superAgregatorCom = settingService.getSettingByCode("SUPER_AGGREGATOR_COMISSION_PERCENTAGE");
                    Double superagentCommission  = Double.parseDouble(superAgregatorCom.getValue())/ Double.parseDouble("100") * parentagentAmount ;

                    if(Objects.nonNull(parent.getParentAgentId()) && parent.getParentAgentId() > 0) {
                        logger.info("super  aggregator  is not 0");
                        parentagentAmount = parentagentAmount - superagentCommission;

                        Agents superaggregator = AgentsRepository.getOne(parent.getParentAgentId());
                        walletService.creditWallet(String.valueOf(tranId),superaggregator.getIncomeWalletNumber(), superagentCommission, "FREEDOM NETWORK", "SUPER AGGREGATOR COMMISSION"+transactionType+" {"+tranId+"}");

                    }
                }
                walletService.creditWallet(String.valueOf(tranId),parent.getIncomeWalletNumber(), parentagentAmount, "FREEDOM NETWORK", "PARENT AGENT COMMISSION "+transactionType+" {"+tranId+"}");


            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //credit 3line with commsision
        try {
            SettingDTO _3lineFinance = settingService.getSettingByCode("MAGTIPON_GL_WALLET");

            walletService.creditWallet(String.valueOf(tranId),_3lineFinance.getValue(), _3lineAmount, "FREEDOM NETWORK", "SUPER AGENT COMMISSION "+transactionType+" {"+tranId+"}");

        }catch (Exception e){
            e.printStackTrace();
        }




        return df.format(fee);
    }

    @Override
    public String generateInstDepositCommission(String bankCode, String amount,Double fee, Long tranId, Agents agent, TransactionType transactionType,InstitutionDTO institutionDTO) {

        BankDetails bank = bankDetailsRepository.findByBankCode(bankCode);

        logger.info(" fee for transaction type {}, and amount {} is {}" , amount , fee);
        /*
         * Generate Commission Splitting using formula setup for bank
         */
        Agents parent = AgentsRepository.getOne(agent.getParentAgentId());


        Double agentAmount = institutionDTO.getAgent_deposit_commission() * fee;
        Double parentagentAmount = institutionDTO.getAggregator_deposit_commission() * fee;
        Double _3lineAmount = institutionDTO.getLine_deposit_commission() * fee;

        GravityDailyCommission dailyCommission = new GravityDailyCommission();

        logger.info("agent will get {} , _3line will get {}", df.format(agentAmount), df.format(_3lineAmount));

        dailyCommission.set_3lineCommission(_3lineAmount.toString());
        dailyCommission.setAgentBank(bank.getBankName());
        dailyCommission.setAgentBankCode(bank.getBankCode());
        dailyCommission.setAgentCommission(agentAmount.toString());
        dailyCommission.setParentAgentCommission(parentagentAmount.toString());
        dailyCommission.setAgentName(agent.getUsername());
        dailyCommission.setAgentAccount(agent.getAccountNo());
        dailyCommission.setTransactionAmount( amount );
        dailyCommission.setTransactionId(tranId.toString());
        dailyCommission.setTransactionType(transactionType);

        logger.info("this is how commission now looks like {}", dailyCommission);

        commssionRepo.save(dailyCommission);

        // credit agent wallet as well as parent agent wallet
        walletService.creditWallet(String.valueOf(tranId),agent.getIncomeWalletNumber() , agentAmount,"FREEDOM NETWORK","AGENT COMMISION "+transactionType+" {"+tranId+"}");

        //credit parent agent if necessary
        try{
            walletService.creditWallet(String.valueOf(tranId),parent.getIncomeWalletNumber(), parentagentAmount, "FREEDOM NETWORK", "PARENT AGENT COMMISSION "+transactionType+" {"+tranId+"}");
        }catch(Exception e){
            e.printStackTrace();
        }
        //credit 3line with commsision
        try {
            SettingDTO _3lineFinance = settingService.getSettingByCode("MAGTIPON_GL_WALLET");

            walletService.creditWallet(String.valueOf(tranId),_3lineFinance.getValue(), _3lineAmount, "FREEDOM NETWORK", "SUPER AGENT COMMISSION "+transactionType+" {"+tranId+"}");

        }catch (Exception e){
            e.printStackTrace();
        }




        return df.format(fee);
    }



    @Override
    public String generateItexCommission(String bankCode, String amount, Long tranId, Agents agent, TransactionType transactionType) {
        BankDetails bank = bankDetailsRepository.findByBankCode(bankCode);

        /*
         * First find agent information on system
         */
//        Agents agent = agentRepo.findByUsername(agentName);


        /*
         * Get Fee for transaction amount
         */

//        Double fee = commissionChargeService.getCommissionForAmount(Double.parseDouble(amount), transactionType);

        logger.info("transaction type {}", transactionType);
//        logger.info(" fee for amount {} is {}" , amount , fee);


        /*
         * Generate Commission Splitting using itex setup percentage
         */

        Double agentAmount = 0.0;
        Double parentagentAmount = 0.0;
        Double _3lineAmount = 0.0;
        Double bankAmount = 0.0;

        SettingDTO itex = settingService.getSettingByCode("ITEX_TERMINAL_TRAN_PERCENTAGE");
        SettingDTO agentCom = settingService.getSettingByCode("AGENT_COMMISSION_FEE_PERCENTAGE");
        SettingDTO subagentCom = settingService.getSettingByCode("SUB_AGENT_COMMISSION_FEE_PERCENTAGE");
        SettingDTO parentAgentCom = settingService.getSettingByCode("PARENT_AGENT_COMMISSION_FEE_PERCENTAGE");
        SettingDTO _3lineCom =  settingService.getSettingByCode("3LINE_COMMISSION_FEE_PERCENTAGE");

        Double  _3lineAmountForItex = (Double.parseDouble(itex.getValue()) / Double.parseDouble("100")) * Double.parseDouble(amount);

        logger.info("#### after deduction - {}% amount meant for 3line will be {}", itex.getValue() , _3lineAmountForItex);

        GravityDailyCommission dailyCommission = new GravityDailyCommission();

            logger.info("agent does not have parent agent ");
            logger.info("commssion will be {}", _3lineAmountForItex);
            agentAmount = 0.0;
            _3lineAmount = _3lineAmountForItex;




        logger.info("agent will get {} , _3line will get {}", df.format(agentAmount), df.format(_3lineAmount));

        dailyCommission.set_3lineCommission(_3lineAmount.toString());
        dailyCommission.setAgentBank(bank.getBankName());
        dailyCommission.setAgentBankCode(bank.getBankCode());
        dailyCommission.setAgentCommission("0");
        dailyCommission.setParentAgentCommission("0");
        dailyCommission.setAgentName(agent.getUsername());
        dailyCommission.setAgentAccount(agent.getAccountNo());
        dailyCommission.setTransactionAmount( amount );
        dailyCommission.setTransactionId(tranId.toString());
        dailyCommission.setTransactionType(transactionType);

        logger.info("this is how commission now looks like {}", dailyCommission);

        commssionRepo.save(dailyCommission);

         walletService.creditWallet(null,agent.getIncomeWalletNumber() , agentAmount,"FREEDOM NETWORK","AGENT COMMISSION "+transactionType+" {"+tranId+"}");



        return df.format(_3lineAmountForItex);
    }

    @Override
    public void generateThriftCommission( Double amount,Agents agent, TransactionType transactionType) {


        BankDetails bankDetails = bankDetailsRepository.findByBankCode(agent.getBankCode());

        logger.info("transaction type {}", transactionType);
        logger.info("  amount is {}" , amount );


        /*
         * Generate Commission Splitting using formula setup for bank
         */

        Double agentAmount = 0.0;
        Double parentagentAmount = 0.0;
        Double _3lineAmount = 0.0;

        SettingDTO agentCom = settingService.getSettingByCode("AGENT_THRIFT_COMMISSION_FEE_PERCENTAGE");
        Double bankComm = Double.valueOf(bankDetails.getThriftCommissionPerc());
        SettingDTO _3lineCom =  settingService.getSettingByCode("3LINE_THRIFT_COMMISSION_FEE_PERCENTAGE");

        GravityDailyCommission dailyCommission = new GravityDailyCommission();

        bankComm     = (bankComm / Double.parseDouble("100")) * amount;
        agentAmount  = (Double.parseDouble(agentCom.getValue()) / Double.parseDouble("100")) * amount;
        _3lineAmount = (Double.parseDouble(_3lineCom.getValue()) / Double.parseDouble("100")) * amount;



        logger.info("agent will get {} , _3line will get {}", df.format(agentAmount), df.format(_3lineAmount));

        dailyCommission.set_3lineCommission(_3lineAmount.toString());
        dailyCommission.setBankCommission(bankComm.toString());
        dailyCommission.setAgentCommission(agentAmount.toString());
        dailyCommission.setParentAgentCommission(parentagentAmount.toString());
        dailyCommission.setAgentName(agent.getUsername());
        dailyCommission.setAgentAccount(agent.getAccountNo());
        dailyCommission.setTransactionAmount( amount.toString() );

        dailyCommission.setTransactionType(transactionType);

        logger.info("this is commission now looks like {}", dailyCommission);

        commssionRepo.save(dailyCommission);

        String tranId = Utility.generateID();

        // credit agent wallet as well as parent agent wallet
        walletService.creditWallet(tranId,agent.getIncomeWalletNumber() , agentAmount,"FREEDOM NETWORK","AGENT THRIFT COMMISION");


        try {
            SettingDTO _3lineFinance = settingService.getSettingByCode("MAGTIPON_GL_WALLET");

            walletService.creditWallet(tranId,_3lineFinance.getValue(), _3lineAmount, "FREEDOM NETWORK", "SUPER AGENT COMMISSION "+transactionType+" {"+tranId+"}");

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    @Override
    public void generateBillPaymentCommission(Double amount,Double fee, Agents agent, boolean isRecharge , String remark) {


        logger.info("transaction type {}", remark);
        logger.info("  amount is {}" , amount );


        /*
         * Generate Commission Splitting using formula setup for bank
         */

        Double agentAmount = 0.0;
        Double parentagentAmount = 0.0;
        Double payeable = 0.0 ;

        SettingDTO agentCom ;
        /*
         * if payment is recharge , change amount payeable to recharge set amount
         */

        if(isRecharge){
            agentCom = settingService.getSettingByCode("BILL_PAYMENT_RECHARGE_PERCENTAGE_COMMISSION");
            payeable = (Double.parseDouble(agentCom.getValue()) / Double.parseDouble("100")) * amount ;
        }else {
            agentCom = settingService.getSettingByCode("BILL_PAYMENT_COMMISSION_FLAT_FEE");
            payeable =  Double.parseDouble(agentCom.getValue()) ;
        }

        logger.info("###### AMOUNT PAYEABLE AS COMMISSION IS {}" , payeable);
        SettingDTO subagentCom = settingService.getSettingByCode("BILL_PAYMENT_SUBAGENT_PERCENTAGE");
        SettingDTO parentAgentCom = settingService.getSettingByCode("BILL_PAYMENT_SUPERAGENT_PERCENTAGE");
        GravityDailyCommission dailyCommission = new GravityDailyCommission();


        if(Objects.isNull(agent.getParentAgentId()) || agent.getParentAgentId() == 0){
            logger.info("agent does not have parent agent ");
            logger.info("commssion will be {}%", agentCom.getValue());
            agentAmount = payeable;


        }else {
            logger.info("agent is a sub agent ");
            logger.info("sub_agent commission will be {}%", subagentCom.getValue());
            logger.info("parent commission will be {}%", parentAgentCom.getValue());
            agentAmount = (Double.parseDouble(subagentCom.getValue()) / Double.parseDouble("100")) * payeable;
            parentagentAmount = (Double.parseDouble(parentAgentCom.getValue()) / Double.parseDouble("100")) * payeable;
            logger.info("agent commission will be {}", agentAmount);
        }



        logger.info("agent will get {} ", df.format(agentAmount));

        dailyCommission.set_3lineCommission("0");
        dailyCommission.setAgentCommission(agentAmount.toString());
        dailyCommission.setParentAgentCommission(parentagentAmount.toString());
        dailyCommission.setAgentName(agent.getUsername());
        dailyCommission.setAgentAccount(agent.getAccountNo());
        dailyCommission.setTransactionAmount( amount.toString() );

        dailyCommission.setTransactionType(TransactionType.BILL_PAYMENT);

        logger.info("this is commission now looks like {}", dailyCommission);

        commssionRepo.save(dailyCommission);


        String tranId = Utility.generateID();

        // credit agent wallet as well as parent agent wallet
        if(isRecharge) {
            walletService.creditWallet(tranId,agent.getIncomeWalletNumber(), agentAmount, "FREEDOM NETWORK", "AGENT AIRTIME COMMISION");
        }else {
            walletService.creditWallet(tranId,agent.getIncomeWalletNumber(), agentAmount, "FREEDOM NETWORK", "AGENT BILL PAYMENT COMMISION");
        }

        //credit parent agent if necessary
        if(Objects.nonNull(agent.getParentAgentId()) && agent.getParentAgentId()!=0){
            Agents parent  = AgentsRepository.getOne(agent.getParentAgentId());

            /// SUB AGGREGATOR IMPLEMENTATION
            if(parent.getAgentType().equals("SUBAGGREGATOR")){
                logger.info("parent agent has a super aggregator");

                SettingDTO superAgregatorCom = settingService.getSettingByCode("SUPER_AGGREGATOR_COMMISSION_PERCENTAGE");
                Double superagentCommission  = Double.parseDouble(superAgregatorCom.getValue())/ Double.parseDouble("100") * parentagentAmount ;

                if(Objects.nonNull(parent.getParentAgentId()) && parent.getParentAgentId() > 0) {
                    logger.info("super aggregator is not 0");
                    parentagentAmount = parentagentAmount - superagentCommission;

                    Agents superaggregator = AgentsRepository.getOne(parent.getParentAgentId());
                    walletService.creditWallet(tranId,superaggregator.getIncomeWalletNumber(), superagentCommission, "FREEDOM NETWORK", "SUPER AGGREGATOR  COMMISSION "+TransactionType.BILL_PAYMENT+" {"+tranId+"}");

                }
            }


            if(isRecharge) {
                walletService.creditWallet(tranId,parent.getIncomeWalletNumber() , parentagentAmount,"FREEDOM NETWORK","AGGREGATOR AIRTIME COMMISSION");
            }else {
                walletService.creditWallet(tranId,parent.getIncomeWalletNumber() , parentagentAmount,"FREEDOM NETWORK","AGGREGATOR BILL PAYMENT COMMISSION");
            }
        }

    }

    @Override
    public void generateInstBillPaymentCommission(Double amount, Double charge,Agents agent, boolean isRecharge, String remark,InstitutionDTO institutionDTO) {

        logger.info("transaction type {} and amount is {}", remark,amount);
        /*
         * Generate Commission Splitting using formula setup for bank
         */
        Agents parentAgent = AgentsRepository.getOne(agent.getParentAgentId());


        Double agentAmount;
        Double parentagentAmount;
        Double _3line_Income;

        SettingDTO agentCom ;
        /*
         * if payment is recharge , change amount payeable to recharge set amount
         */

        if(isRecharge){
            agentAmount = institutionDTO.getAgent_recharge_commission() * amount;
            parentagentAmount = institutionDTO.getAggregator_recharge_commission() * amount;
            _3line_Income = institutionDTO.getLine_recharge_commission() * amount ;
        }else {
            agentAmount = institutionDTO.getAgent_bill_commission() * charge;
            parentagentAmount = institutionDTO.getAggregator_bill_commission() * charge;
            _3line_Income = institutionDTO.getLine_bill_commission() * charge;
        }


        GravityDailyCommission dailyCommission = new GravityDailyCommission();

        logger.info("agent commssion will be {}", agentAmount);
        logger.info("parent commssion will be {}", parentagentAmount);
        logger.info("3line commssion will be {}", _3line_Income);


        dailyCommission.set_3lineCommission(String.valueOf(_3line_Income));
        dailyCommission.setAgentCommission(agentAmount.toString());
        dailyCommission.setParentAgentCommission(parentagentAmount.toString());
        dailyCommission.setAgentName(agent.getUsername());
        dailyCommission.setAgentAccount(agent.getAccountNo());
        dailyCommission.setTransactionAmount( amount.toString() );

        dailyCommission.setTransactionType(TransactionType.BILL_PAYMENT);

        logger.info("commission now looks like {}", dailyCommission);

        commssionRepo.save(dailyCommission);


        String tranId = Utility.generateID();

        // credit agent wallet as well as parent agent wallet
        String transactionType;
        if(isRecharge) {
            transactionType = "Recharge";
            walletService.creditWallet(tranId,agent.getIncomeWalletNumber(), agentAmount, "FREEDOM NETWORK", "AGENT AIRTIME COMMISION");
            walletService.creditWallet(tranId,parentAgent.getIncomeWalletNumber() , parentagentAmount,"FREEDOM NETWORK","AGGREGATOR AIRTIME COMMISSION");
        }else {
            walletService.creditWallet(tranId,agent.getIncomeWalletNumber(), agentAmount, "FREEDOM NETWORK", "AGENT BILL PAYMENT COMMISION");
            walletService.creditWallet(tranId,parentAgent.getIncomeWalletNumber() , parentagentAmount,"FREEDOM NETWORK","AGGREGATOR BILL PAYMENT COMMISSION");
            transactionType = "Bills";
        }

        //credit 3line with commsision
        try {
            SettingDTO _3lineFinance = settingService.getSettingByCode("MAGTIPON_GL_WALLET");

            walletService.creditWallet(String.valueOf(tranId),_3lineFinance.getValue(), _3line_Income, "FREEDOM NETWORK", "SUPER AGENT COMMISSION "+transactionType+" {"+tranId+"}");

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public GravityDailyTotalCommission getTodaysTotalUnpaidCommission() {

        logger.info("Getting daily commission summary");

        DateRange todaysDateRange = getTodaysDateRange();

        List<GravityDailyCommission> todaysCommission = fetchUnpaidCommissions(todaysDateRange);

        GravityDailyTotalCommission dailyTotalCommission = computeTotalCommission(todaysCommission);
        dailyTotalCommission.setDateRange(todaysDateRange);
        return dailyTotalCommission;
    }

    @Override
    public GravityDailyTotalCommission computeTodaysTotalCommission() {

        logger.info("Computing today's total commission");

        DateRange todaysDateRange = getTodaysDateRange();

        List<GravityDailyCommission> todaysCommission = fetchCommissions(todaysDateRange);

        try {
            GravityDailyTotalCommission dailyTotalCommission = computeTotalCommission(todaysCommission);
            dailyTotalCommission.setDateRange(todaysDateRange);
            return dailyTotalCommission;
        }
        catch (Exception e){
            throw new GravityException(e.getMessage(),e);
        }
    }


    @Override
    public List<AgentTotalCommission> extractAgentsTodaysTotalCommissions(){

        logger.debug("Extracting agents today's total commission");

        GravityDailyTotalCommission todaysTotalCommission = computeTodaysTotalCommission();

        List<AgentTotalCommission> agentTotalCommissions = new ArrayList<>();

        todaysTotalCommission.getAgentTotalCommission().forEach((accountNumber, amount) ->{
            Agents agent = AgentsRepository.findFirstByAccountNo(accountNumber);

            AgentTotalCommission agentTotalCommission = new AgentTotalCommission();
            agentTotalCommission.setAgentName(agent.getFirstName()+" "+agent.getLastName());
            agentTotalCommission.setAccountNumber(accountNumber);
            agentTotalCommission.setBank(agentTotalCommission.getBank());
            agentTotalCommission.setTotalAmount(amount);
            agentTotalCommission.setTranDate(todaysTotalCommission.getDateComputed());
            agentTotalCommission.setPaid(agentTotalCommission.isPaid());
            agentTotalCommissions.add(agentTotalCommission);
        } );
        return agentTotalCommissions;
    }


    @Override
    public List<BankTotalCommission> extractBanksTodaysTotalCommissions(){

        logger.debug("Extracting banks today's total commission");

        GravityDailyTotalCommission todaysTotalCommission = computeTodaysTotalCommission();

        List<BankTotalCommission> bankTotalCommissions = new ArrayList<>();

        todaysTotalCommission.getAgentBankTotalCommission().forEach((bank, amount) ->{
            BankTotalCommission bankTotalCommission = new BankTotalCommission();
            bankTotalCommission.setBankName(bank);
            bankTotalCommission.setTotalAmount(amount);
            bankTotalCommission.setTranDate(todaysTotalCommission.getDateComputed());
            bankTotalCommissions.add(bankTotalCommission);
        } );
        return bankTotalCommissions;
    }

    @Override
    public _3lineTotalCommission extract3lineTodaysTotalCommission(){

        logger.debug("Extracting 3line today's total commission");

        GravityDailyTotalCommission todaysTotalCommission = computeTodaysTotalCommission();

        _3lineTotalCommission _3lineCommission = new _3lineTotalCommission();
        todaysTotalCommission.get_3lineTotalCommission().forEach((name, amount) ->{

            _3lineCommission.setTotalAmount(amount);
            _3lineCommission.setTranDate(todaysTotalCommission.getDateComputed());
        } );
        return _3lineCommission;

    }

    @Override
    public Map<String,String> getBillAmountWithCharge(Double amount, BillOption billOption,InstitutionDTO institutionDTO) {
        if(institutionDTO==null){
            return getInstBillAmountWithCharge(amount,billOption,"DEFAULT");
        }else{
            return getInstBillAmountWithCharge(amount,billOption,institutionDTO.getName());
        }

    }

    @Override
    public Map<String, String> getInstBillAmountWithCharge(Double amount, BillOption billOption, InstitutionDTO institutionDTO) {
        Map<String,String> stringMap = new HashMap<>();
        Double payeable;
        Double charge  = 0.0;
        BillOption billOptionCheck = billOption;


        if(!billOptionCheck.getServiceOption().getRecharge()){
            charge = commissionChargeService.getInstCommissionForAmount(amount,TransactionType.BILL_PAYMENT,institutionDTO.getName());
        }

        payeable =  amount + charge;

        stringMap.put("charge", String.valueOf(charge));
        stringMap.put("totalAmount", String.valueOf(payeable));

        return stringMap;
    }


    public Map<String,String> getInstBillAmountWithCharge(Double amount, BillOption billOption, String instName) {

        Map<String,String> stringMap = new HashMap<>();
        Double payeable;
        Double charge;
        BillOption billOptionCheck = billOption;


        if(billOptionCheck.getServiceOption().getRecharge()){
            charge = 0.0;
        }else {
            charge = commissionChargeService.getInstCommissionForAmount(amount,TransactionType.BILL_PAYMENT,instName);
        }

        payeable =  amount + charge;

        stringMap.put("charge", String.valueOf(charge));
        stringMap.put("totalAmount", String.valueOf(payeable));

        return stringMap;
    }


    private GravityDailyTotalCommission computeTotalCommission(List<GravityDailyCommission> todaysCommission){

        logger.debug("Starting computation for daily commission");

        Map<String, BigDecimal> agentTotalCommission = new HashMap<>();
        Map<String, BigDecimal> agentBankTotalCommission = new HashMap<>();
        Map<String, BigDecimal> __3lineTotalCommission = new HashMap<>();

        todaysCommission.stream().forEach(commission -> {
            if (agentTotalCommission.containsKey(commission.getAgentAccount())) {
                BigDecimal totalAmount = agentTotalCommission.get(commission.getAgentAccount());
                totalAmount = totalAmount.add(new BigDecimal(commission.getAgentCommission()));
                agentTotalCommission.put(commission.getAgentAccount(), totalAmount);
            } else {
                agentTotalCommission.put(commission.getAgentAccount(), new BigDecimal(commission.getAgentCommission()));
            }

            if (agentBankTotalCommission.containsKey(commission.getAgentBank())) {
                BigDecimal totalAmount = agentBankTotalCommission.get(commission.getAgentBank());
                totalAmount = totalAmount.add(new BigDecimal(commission.getBankCommission()));
                agentBankTotalCommission.put(commission.getAgentBank(), totalAmount);
            } else {
                agentBankTotalCommission.put(commission.getAgentBank(), new BigDecimal(commission.getBankCommission()));
            }

            if (__3lineTotalCommission.containsKey("3line")) {

                BigDecimal totalAmount = __3lineTotalCommission.get("3line");
                totalAmount = totalAmount.add(new BigDecimal(commission.get_3lineCommission()));
                __3lineTotalCommission.put("3line", totalAmount);
            } else {
                __3lineTotalCommission.put("3line", new BigDecimal(commission.get_3lineCommission()));
            }

        });
        GravityDailyTotalCommission totalCommission = new GravityDailyTotalCommission();
        totalCommission.setAgentTotalCommission(agentTotalCommission);
        totalCommission.setAgentBankTotalCommission(agentBankTotalCommission);
        totalCommission.set_3lineTotalCommission(__3lineTotalCommission);

        logger.debug("Finished computation for daily commission");
        return totalCommission;

    }



    private List<GravityDailyCommission> fetchTodaysCommissions(){

        DateRange todaysDateRange = getTodaysDateRange();

        logger.debug("Fetching commissions for dates: {}", todaysDateRange);

        List<GravityDailyCommission> todaysCommission = commssionRepo.findByTranDateGreaterThanEqualAndTranDateLessThan(todaysDateRange.getStartDate(), todaysDateRange.getEndDate());
        logger.debug("Returned today's commissions: {}", todaysCommission);
        return todaysCommission;

    }


    private List<GravityDailyCommission> fetchCommissions(DateRange dateRange){

        logger.debug("Fetching commissions for dates: {}", dateRange);

        List<GravityDailyCommission> commissions = commssionRepo.findByTranDateGreaterThanEqualAndTranDateLessThan(dateRange.getStartDate(), dateRange.getEndDate());
        logger.debug("Returned commissions: {}", commissions);
        return commissions;

    }

    private List<GravityDailyCommission> fetchUnpaidCommissions(DateRange dateRange){

        logger.debug("Fetching unpaid commissions for dates: {}", dateRange);

        List<GravityDailyCommission> commissions = commssionRepo.findByTranDateGreaterThanEqualAndTranDateLessThanAndPaid(dateRange.getStartDate(), dateRange.getEndDate(), false);
        return commissions;

    }

    private DateRange getTodaysDateRange(){

        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        Date todayBeginning = date.getTime();
        date.add(Calendar.DAY_OF_MONTH, 1);
        Date todayEnding = date.getTime();
        return new DateRange(todayBeginning, todayEnding);
    }


    @Override
//    @Scheduled(cron = "${cron.commission.report}")
    public void generateDailyReport(){

        logger.info("Generating daily summary report");

        GravityDailyTotalCommission totalCommission = getTodaysTotalUnpaidCommission();
        gravityDailyTotalCommissionRepo.save(totalCommission);
        File file = ExportUtility.exportToExcel(totalCommission);

        logger.info("Successfully generated daily summary report");

      //  creditAllAgentsAccounts(totalCommission);
        sendReport(file);
    }



    private void sendReport(File file) {

        SettingDTO operationsEmailAddress = settingService.getSettingByCode("OPERATIONS_EMAIL_ADDRESS");
        SettingDTO emailCopy = settingService.getSettingByCode("COMMISSION_COPY_ADDRESS");
        if(operationsEmailAddress!=null && operationsEmailAddress.isEnabled()){

            String emailAddress =  operationsEmailAddress.getValue();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
            String date = dateFormat.format(new Date());

            logger.info("Sending daily commission report to {}", emailAddress);
//            Email email = new Email();
//            email.setRecipientEmailAddress(emailAddress);
//            if(Objects.nonNull(emailCopy)){
//                email.setCcList(emailCopy.getValue().split(","));
//            }
//            email.setSubject("Commission Report");
//            email.setBody("<html><p>Dear Team, </p><p>Please find attached the daily commission summary report for "+date+".</p><p>Thank you.</p><html>");
//            EmailAttachment attachment = new EmailAttachment();
//            attachment.setName(file.getName());
//            attachment.setFile(file);
//            email.setAttachment(attachment);
//            mailService.sendMail(email);

        }

    }

    private void creditAllAgentsAccounts(GravityDailyTotalCommission totalCommission) {

        for (Map.Entry<String, BigDecimal> entry : totalCommission.getAgentTotalCommission().entrySet()) {

            String agentAccountNumber = entry.getKey();
            BigDecimal amount = entry.getValue();

            logger.info("Crediting agent account [{}] with total commission [{}]",agentAccountNumber, amount);

//            AgentTransferRequest transferRequest = transferCommissionToAgentAccount(agentAccountNumber, amount);
//
//            AgentTotalCommission agentTotalCommission = new AgentTotalCommission();
//            agentTotalCommission.setAccountNumber(agentAccountNumber);
//            agentTotalCommission.setTotalAmount(amount);
//            agentTotalCommission.setTranDate(transferRequest.getTranDate());
//            agentTotalCommission.setBank(transferRequest.getBankCode());
//            agentTotalCommission.setPaid(transferRequest.isPaid());
//
//            try {
//                updateAgentCommissionPaidStatus(transferRequest, totalCommission.getDateRange());
//                agentTransferRequestRepository.save(transferRequest);
//                agentTotalCommissionRepo.save(agentTotalCommission);
//                logger.info("Updated agent transfer operations");
//            } catch (Exception exception) {
//                logger.error("Failed to update entities", exception);
//            }
        }

    }

    @Override
    public Page<GravityDailyCommissionDTO> getDailyCommissions(Pageable pageable) {

        logger.debug("Getting paged daily commissions");

        Page<GravityDailyCommission> commissions = commssionRepo.findAll(pageable);
        List<GravityDailyCommissionDTO> commissionDTOs = converEntitiesToDtos(commissions.getContent());
        return new PageImpl<>(commissionDTOs,pageable, commissions.getTotalElements());
    }


    private GravityDailyCommissionDTO convertEntityToDto(GravityDailyCommission dailyCommission){

        return modelMapper.map(dailyCommission,GravityDailyCommissionDTO.class);
    }

    private List<GravityDailyCommissionDTO> converEntitiesToDtos(List<GravityDailyCommission> dailyCommissions){
        return dailyCommissions.stream().map(dailyCommission -> convertEntityToDto(dailyCommission)).collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() {
        df.setMaximumFractionDigits(12);
//        generateDailyReport();
    }
}
