//package com._3line.gravity.freedom.commisions.services.impl;
//
//import com._3line.gravity.core.setting.dto.SettingDTO;
//import com._3line.gravity.core.setting.service.SettingService;
//import com._3line.gravity.freedom.agents.models.Agents;
//import com._3line.gravity.freedom.agents.repository.AgentsRepository;
//import com._3line.gravity.freedom.agents.service.AgentService;
//import com._3line.gravity.freedom.bankdetails.model.BankDetails;
//import com._3line.gravity.freedom.bankdetails.model.TransactionType;
//import com._3line.gravity.freedom.bankdetails.repository.BankDetailsRepository;
//import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
//import com._3line.gravity.freedom.billpayment.models.BillOption;
//import com._3line.gravity.freedom.commisioncharge.service.CommissionChargeService;
//import com._3line.gravity.freedom.commisions.dto.GravityDailyCommissionDTO;
//import com._3line.gravity.freedom.commisions.models.*;
//import com._3line.gravity.freedom.commisions.repositories.GravityDailyCommssionRepo;
//import com._3line.gravity.freedom.commisions.services.GravityDailyCommissionService;
//import com._3line.gravity.freedom.institution.dto.InstitutionDTO;
//import com._3line.gravity.freedom.institution.service.InstitutionService;
//import com._3line.gravity.freedom.itexintegration.model.PtspModel;
//import com._3line.gravity.freedom.transactions.models.TranChannel;
//import com._3line.gravity.freedom.transactions.repositories.TranChannelsRepository;
//import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
//import com._3line.gravity.freedom.utility.Utility;
//import com._3line.gravity.freedom.wallet.service.WalletService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.text.DecimalFormat;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//
//@Service
//@Qualifier(value = "institution")
//public class InstitutionDailyCommissionServiceImpl implements GravityDailyCommissionService {
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Autowired
//    CommissionChargeService chargeService;
//
//    @Autowired
//    TranChannelsRepository tranChannelsRepository;
//
//    @Autowired
//    BankDetailsService bankDetailsService;
//
//    @Autowired
//    TransactionRepository transactionRepository;
//
//    @Autowired
//    SettingService settingService;
//
//    @Autowired
//    WalletService walletService;
//
//    @Autowired
//    InstitutionService institutionService;
//
//    @Autowired
//    AgentService agentService;
//
//    @Autowired
//    GravityDailyCommssionRepo commssionRepo;
//
//    @Autowired
//    BankDetailsRepository bankDetailsRepository;
//
//    @Autowired
//    private AgentsRepository agentsRepository;
//
//
//    private DecimalFormat df = new DecimalFormat("#");
//
//
//    @Override
//    public String generateDepositCommission(String bankCode, String amount,Double fee, Long tranId, Agents agent, TransactionType transactionType) {
//
//        BankDetails bank = bankDetailsRepository.findByBankCode(bankCode);
//
//        logger.info(" fee for transaction type {}, and amount {} is {}" , amount , fee);
//        /*
//         * Generate Commission Splitting using formula setup for bank
//         */
//
//        InstitutionDTO institutionDTO = institutionService.getInstitutionByAgentId(agent.getParentAgentId());
//
//
//        Agents parent = agentsRepository.getOne(agent.getParentAgentId());
//
//
//        Double agentAmount = institutionDTO.getAgent_deposit_commission() * fee;
//        Double parentagentAmount = institutionDTO.getAggregator_deposit_commission() * fee;
//        Double _3lineAmount = institutionDTO.getLine_deposit_commission() * fee;
//
//        GravityDailyCommission dailyCommission = new GravityDailyCommission();
//
//        logger.info("agent will get {} , _3line will get {}", df.format(agentAmount), df.format(_3lineAmount));
//
//        dailyCommission.set_3lineCommission(_3lineAmount.toString());
//        dailyCommission.setAgentBank(bank.getBankName());
//        dailyCommission.setAgentBankCode(bank.getBankCode());
//        dailyCommission.setAgentCommission(agentAmount.toString());
//        dailyCommission.setParentAgentCommission(parentagentAmount.toString());
//        dailyCommission.setAgentName(agent.getUsername());
//        dailyCommission.setAgentAccount(agent.getAccountNo());
//        dailyCommission.setTransactionAmount( amount );
//        dailyCommission.setTransactionId(tranId.toString());
//        dailyCommission.setTransactionType(transactionType);
//
//        logger.info("this is how commission now looks like {}", dailyCommission);
//
//        commssionRepo.save(dailyCommission);
//
//        // credit agent wallet as well as parent agent wallet
//        walletService.creditWallet(String.valueOf(tranId),agent.getIncomeWalletNumber() , agentAmount,"FREEDOM NETWORK","AGENT COMMISION "+transactionType+" {"+tranId+"}");
//
//        //credit parent agent if necessary
//        try{
//            walletService.creditWallet(String.valueOf(tranId),parent.getIncomeWalletNumber(), parentagentAmount, "FREEDOM NETWORK", "PARENT AGENT COMMISSION "+transactionType+" {"+tranId+"}");
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        //credit 3line with commsision
//        try {
//            SettingDTO _3lineFinance = settingService.getSettingByCode("MAGTIPON_GL_WALLET");
//
//            walletService.creditWallet(String.valueOf(tranId),_3lineFinance.getValue(), _3lineAmount, "FREEDOM NETWORK", "SUPER AGENT COMMISSION "+transactionType+" {"+tranId+"}");
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//
//
//
//        return df.format(fee);
//    }
//
//    @Override
//    public String generateItexCommission(String bankCode, String amount, Long tranId, Agents agent, TransactionType transactionType) {
//        return null;
//    }
//
//    @Override
//    public void generateThriftCommission(Double amount, Agents agent, TransactionType transactionType) {
//
//    }
//
//    @Override
//    public void generateBillPaymentCommission(Double amount, Double charge,Agents agent, boolean isRecharge, String remark) {
//
//        logger.info("transaction type {} and amount is {}", remark,amount);
//
//        /*
//         * Generate Commission Splitting using formula setup for bank
//         */
//
//        InstitutionDTO institutionDTO = institutionService.getInstitutionByAgentId(agent.getParentAgentId());
//
//        Agents parentAgent = agentService.fetchAgentByAgentName(institutionDTO.getAgentUsername());
//
//
//        Double agentAmount;
//        Double parentagentAmount;
//        Double _3line_Income;
//
//        SettingDTO agentCom ;
//        /*
//         * if payment is recharge , change amount payeable to recharge set amount
//         */
//
//        if(isRecharge){
//            agentAmount = institutionDTO.getAgent_recharge_commission() * amount;
//            parentagentAmount = institutionDTO.getAggregator_recharge_commission() * amount;
//            _3line_Income = institutionDTO.getLine_recharge_commission() * amount ;
//        }else {
//            agentAmount = institutionDTO.getAgent_bill_commission() * charge;
//            parentagentAmount = institutionDTO.getAggregator_bill_commission() * charge;
//            _3line_Income = institutionDTO.getLine_bill_commission() * charge;
//        }
//
//
//        GravityDailyCommission dailyCommission = new GravityDailyCommission();
//
//        logger.info("agent commssion will be {}", agentAmount);
//        logger.info("parent commssion will be {}", parentagentAmount);
//        logger.info("3line commssion will be {}", _3line_Income);
//
//
//        dailyCommission.set_3lineCommission(String.valueOf(_3line_Income));
//        dailyCommission.setAgentCommission(agentAmount.toString());
//        dailyCommission.setParentAgentCommission(parentagentAmount.toString());
//        dailyCommission.setAgentName(agent.getUsername());
//        dailyCommission.setAgentAccount(agent.getAccountNo());
//        dailyCommission.setTransactionAmount( amount.toString() );
//
//        dailyCommission.setTransactionType(TransactionType.BILL_PAYMENT);
//
//        logger.info("commission now looks like {}", dailyCommission);
//
//        commssionRepo.save(dailyCommission);
//
//
//        String tranId = Utility.generateID();
//
//        // credit agent wallet as well as parent agent wallet
//        String transactionType;
//        if(isRecharge) {
//            transactionType = "Recharge";
//            walletService.creditWallet(tranId,agent.getIncomeWalletNumber(), agentAmount, "FREEDOM NETWORK", "AGENT AIRTIME COMMISION");
//            walletService.creditWallet(tranId,parentAgent.getIncomeWalletNumber() , parentagentAmount,"FREEDOM NETWORK","AGGREGATOR AIRTIME COMMISSION");
//        }else {
//            walletService.creditWallet(tranId,agent.getIncomeWalletNumber(), agentAmount, "FREEDOM NETWORK", "AGENT BILL PAYMENT COMMISION");
//            walletService.creditWallet(tranId,parentAgent.getIncomeWalletNumber() , parentagentAmount,"FREEDOM NETWORK","AGGREGATOR BILL PAYMENT COMMISSION");
//            transactionType = "Bills";
//        }
//
//        //credit 3line with commsision
//        try {
//            SettingDTO _3lineFinance = settingService.getSettingByCode("MAGTIPON_GL_WALLET");
//
//            walletService.creditWallet(String.valueOf(tranId),_3lineFinance.getValue(), _3line_Income, "FREEDOM NETWORK", "SUPER AGENT COMMISSION "+transactionType+" {"+tranId+"}");
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public Page<GravityDailyCommissionDTO> getDailyCommissions(Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public GravityDailyTotalCommission getTodaysTotalUnpaidCommission() {
//        return null;
//    }
//
//    @Override
//    public GravityDailyTotalCommission computeTodaysTotalCommission() {
//        return null;
//    }
//
//    @Override
//    public List<AgentTotalCommission> extractAgentsTodaysTotalCommissions() {
//        return null;
//    }
//
//    @Override
//    public List<BankTotalCommission> extractBanksTodaysTotalCommissions() {
//        return null;
//    }
//
//    @Override
//    public _3lineTotalCommission extract3lineTodaysTotalCommission() {
//        return null;
//    }
//
////    @Override
////    public Map<String, String> getBillAmountWithCharge(Double amount, BillOption billOption, InstitutionDTO institutionDTO) {
////        Map<String,String> stringMap = new HashMap<>();
////        Double payeable;
////        Double charge  = 0.0;
////        BillOption billOptionCheck = billOption;
////
////
////        if(!billOptionCheck.getServiceOption().getRecharge()){
////            charge = chargeService.getInstCommissionForAmount(amount,TransactionType.BILL_PAYMENT,institutionDTO.getName());
////        }
////
////        payeable =  amount + charge;
////
////        stringMap.put("charge", String.valueOf(charge));
////        stringMap.put("totalAmount", String.valueOf(payeable));
////
////        return stringMap;
////    }
//
//
//
//    @Override
//    public void generateDailyReport() {
//
//    }
//
//}
