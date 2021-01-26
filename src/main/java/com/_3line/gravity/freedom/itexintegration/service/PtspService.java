//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com._3line.gravity.freedom.itexintegration.service;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.setting.dto.SettingDTO;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.model.TransactionType;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.commisioncharge.service.CommissionChargeService;
import com._3line.gravity.freedom.commisions.models.GravityDailyCommission;
import com._3line.gravity.freedom.commisions.repositories.GravityDailyCommssionRepo;
import com._3line.gravity.freedom.commisions.services.GravityDailyCommissionService;
import com._3line.gravity.freedom.institution.dto.InstitutionDTO;
import com._3line.gravity.freedom.institution.service.InstitutionService;
import com._3line.gravity.freedom.itexintegration.model.PtspDto;
import com._3line.gravity.freedom.itexintegration.model.PtspModel;
import com._3line.gravity.freedom.itexintegration.repository.PtspRepository;
import com._3line.gravity.freedom.transactions.models.TranChannel;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TranChannelsRepository;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.wallet.models.FreedomWalletTransaction;
import com._3line.gravity.freedom.wallet.repository.WalletTransactionRepository;
import com._3line.gravity.freedom.wallet.service.WalletService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PtspService {
    @Autowired
    private PtspRepository ptspRepository;
    @Autowired
    private AgentsRepository agentsRepository;
    @Autowired
    WalletTransactionRepository walletTransactionRepository;
    private ModelMapper modelMapper = new ModelMapper();
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Qualifier("gravityDailyCommissionServiceImpl")
    @Autowired
    private GravityDailyCommissionService commissionService;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TranChannelsRepository tranChannelsRepository;

    @Autowired
    private WalletService walletService;
    @Autowired
    private CommissionChargeService commissionCharge;
    @Autowired
    private GravityDailyCommssionRepo commssionRepo;
    @Autowired
    private SettingService settingService;
    @Autowired
    private BankDetailsService bankDetailsService;
    @Autowired
    private MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale();
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    InstitutionService institutionService;

    public PtspService() {
    }

    public String savePtspDetails(PtspDto ptspDto, String uploadFrom) throws GravityException {
        this.logger.info("###################################  BEGINING PTSP SAVE  ##################################");
        String message = "";
        Map res = new HashMap();
        PtspModel ptspModel = this.convertDtoToEntity(ptspDto);
        String terminalId = ptspDto.getTerminalId();

        if (!terminalId.equals(null) && !terminalId.isEmpty() && (ptspDto.getReversal().equals("false") || ptspDto.getReversal().equals("true"))) {
            this.logger.info("TERMINAL ID --- > {}", terminalId);
            Agents agent = this.agentsRepository.findByTerminalId(terminalId.trim());
            if (agent != null && terminalId != null && !terminalId.isEmpty()) {

                String tranTime = ptspModel.getTransactionTime().trim().replaceAll("-", "").substring(0, 8);
                ptspModel.setPtspTranDate(tranTime);
                ptspModel.setIsVerified(false);
                ptspModel.setUploadedBy(ptspDto.getPtsp());

                this.logger.info("ptsp transaction  checked against is :: {}, {}, {}, {}, {} ", new Object[]{ptspModel.getRrn(), ptspModel.getTerminalId(), ptspModel.getStan(), tranTime, ptspDto.getReversal()});
                //make rrn,terminalid, and reversal flag unique
                List<PtspModel> ptspModels = this.ptspRepository.findByRrnAndTerminalId(ptspModel.getRrn(), ptspModel.getTerminalId());

                if ( ptspModels.contains(ptspModel) || ptspModels.size() > 1 ) {
                    this.logger.info("rrn  and stan already exists -> {}", ptspDto);
                    res.put("respCode", "00");
                    res.put("respDesc", "RRN and STAN Already exists");

                    return (new Gson()).toJson(res);
                }

                for(int a=0;a<ptspModels.size();a++){
                    if(ptspModels.get(a).getReversal().equalsIgnoreCase(ptspModel.getReversal())){
                        this.logger.info("rrn  and stan already exists -> {}", ptspDto);
                        res.put("respCode", "00");
                        res.put("respDesc", "RRN and STAN Already exists");
                        return (new Gson()).toJson(res);
                    }
                }


                this.logger.info("ptsp model before saving--- > {}", ptspModel);
                ptspModel = this.ptspRepository.save(ptspModel);
                this.processPtsp(ptspModels,ptspModel, agent);
                message = this.messageSource.getMessage("ptsp.save.successfully", (Object[])null, this.locale);
                res.put("respCode", "00");
                res.put("respDesc", message);
            } else {
                message = this.messageSource.getMessage("ptsdterminalid.exist.error", (Object[])null, this.locale);
                res.put("respCode", "99");
                res.put("respDesc", message);
            }

            this.logger.info("DONE -> RESPONSE - > {}", res);
            return (new Gson()).toJson(res);
        } else {
            this.logger.error("Invalid Reversal Flag or Terminal ID Sent -> {}", ptspDto);
            throw new GravityException("Invalid Reversal Flag or Terminal ID Sent");
        }
    }

    public PtspDto convertEntityToDto(PtspModel ptspModel) {
        PtspDto ptspDto = (PtspDto)this.modelMapper.map(ptspModel, PtspDto.class);
        return ptspDto;
    }

    private PtspModel convertDtoToEntity(PtspDto ptspDto) {
        PtspModel ptspModel = (PtspModel)this.modelMapper.map(ptspDto, PtspModel.class);
        return ptspModel;
    }

    public void handleWithdrawalCredit(PtspModel ptspModel, Agents agent) {
        Date tranValDate = null;
        this.logger.debug("CONVERTING TRAN AMOUNT FROM KOBO {} TO NAIRA", ptspModel.getAmount());
        Double realtranAmount = ptspModel.getAmount() / 100.0D;
        this.logger.debug("NAIRA EQUIVALENT IS - > {} ", realtranAmount);
        Transactions transactions = new Transactions();
        transactions.setTranDate(new Date());
        transactions.setStatus((short)0);
        if (ptspModel.getStatusCode() == null) {
            transactions.setStatusdescription("PENDING");
            transactions.setStatus((short)0);
        } else if (ptspModel.getStatusCode().equals("00")) {
            transactions.setStatusdescription("SUCCESSFUL");
            transactions.setStatus((short)1);
        } else {
            transactions.setStatusdescription("FAILED");
            transactions.setStatus((short)2);
        }

        transactions.setInnitiatorId(agent.getId());
        transactions.setApproval(0L);
        transactions.setAmount(realtranAmount);
        transactions.setStan(ptspModel.getStan());
        transactions.setItexTranId(ptspModel.getRrn());
        transactions.setTerminalId(ptspModel.getTerminalId());
        transactions.setTransactionType("Withdrawal");
        transactions.setTransactionTypeDescription("Card withdrawal");
        transactions.setDescription("terminal withdrawal");
        transactions.setStan(ptspModel.getStan());
        transactions.setMaskedPan(ptspModel.getPan());

        try {
            tranValDate = this.dateFormat1.parse(ptspModel.getPtspTranDate());
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        transactions.setTranValueDate(tranValDate);
        transactions.setBankFrom(agent.getBankCode());
        transactions.setStatcode(ptspModel.getStatusCode());
        transactions.setAgentName(agent.getUsername());
        transactions.setMedia("POS TERMINAL");

        Double tranFee = getTranFee(ptspModel, agent, realtranAmount);

        transactions.setFee(tranFee!=null?tranFee.toString():null);


        List<Transactions> checkTran = this.transactionRepository.findByTerminalIdAndItexTranId(transactions.getTerminalId(), transactions.getItexTranId());
        if (checkTran != null && checkTran.size() == 0) {
            if (ptspModel.getStatusCode() != null && ptspModel.getStatusCode().equals("00")) {
                Transactions transaction = this.transactionRepository.save(transactions);
                this.logger.info("TRYING TO SPLIT COMMISSION");
                this.doCommission(agent,ptspModel,realtranAmount,tranFee,transaction.getTranId(),false);
            } else {
                this.transactionRepository.save(transactions);
                this.logger.info("Transaction from PTSP Failed with Code: " + ptspModel.getStatusCode());
            }
        } else {
            this.logger.error("Transaction already exist");
        }

    }

    public Double  getTranFee(PtspModel ptspModel, Agents agent, Double realtranAmount) {

        Double tranFee;
        String channelType = ptspModel.getProductId()==null?"NIBSS":ptspModel.getProductId();

        TranChannel tranChannel;

        if(channelType.equalsIgnoreCase("INTERSWITCH")){
            tranChannel = tranChannelsRepository.findByChannelName(channelType);
        }else{
            tranChannel = tranChannelsRepository.findByChannelName("NIBSS");
        }

        if(agent.getAgentType().equalsIgnoreCase("SOLE") && agent.getPosTerminalTranFee() != null ){
            tranFee = Double.valueOf(agent.getPosTerminalTranFee()) / 100 * realtranAmount;
        }else{
            tranFee = Double.valueOf(tranChannel.getPosTerminalPercentageFee()) / 100 * realtranAmount;
        }

//        tranFee = tranFee < tranChannel.getMinimumPosTerminalFee() ? tranChannel.getMinimumPosTerminalFee() : tranFee;


        if(tranFee < tranChannel.getMinimumPosTerminalFee()){
            tranFee = tranChannel.getMinimumPosTerminalFee();
        }else if(tranFee > tranChannel.getMaxPosTerminalFee()){
            tranFee = tranChannel.getMaxPosTerminalFee();
        }

        this.logger.info("before Fee to be deducted from amount is :: "+(tranFee));

        tranFee = new BigDecimal(Double.toString(tranFee)).setScale(2, RoundingMode.HALF_UP).doubleValue();


        return tranFee;
    }

    public void handleReversal(Boolean creditHasBeenDone, PtspModel ptspModel, Agents agents) {

        if (!creditHasBeenDone) {
            this.handleWithdrawalCredit(ptspModel, agents);
        }

        System.out.print("checking ptsp reversal transaction  with  " + ptspModel.getTerminalId() + " " + ptspModel.getRrn());
        List<Transactions> transactionList = this.transactionRepository.findByTerminalIdAndItexTranId(ptspModel.getTerminalId(), ptspModel.getRrn());
        if (Objects.nonNull(transactionList) && transactionList.size() > 1) {
            System.out.print("Multiple transactions found for " + ptspModel.getTerminalId() + " " + ptspModel.getRrn());
            throw new GravityException("Multiple tranactions with same tranID");
        } else {
            if (Objects.nonNull(transactionList) && transactionList.size() == 1) {
                Transactions transaction = transactionList.get(0);
                this.logger.info("transaction found for reversal , tran will be reversed ");

                GravityDailyCommission commission = this.commssionRepo.findByTransactionId(transaction.getTranId().toString());
                this.logger.info("commission split is {}", commission.toString());

                List<FreedomWalletTransaction> freedomWalletTransactions = this.walletTransactionRepository.findByTranID(String.valueOf(transaction.getTranId()));

                for(int a = 0; a < freedomWalletTransactions.size(); ++a) {

                    FreedomWalletTransaction walletTransaction = freedomWalletTransactions.get(a);

                    if(walletTransaction.getTranType().toString().equals("CREDIT")){
                        this.walletService.debitWallet(String.valueOf(transaction.getTranId()), walletTransaction.getWalletNumber(), walletTransaction.getAmount(), "SYSTEM", walletTransaction.getRemark()+"  REVERSAL");
                    }else{
                        this.walletService.creditWallet(String.valueOf(transaction.getTranId()), walletTransaction.getWalletNumber(), walletTransaction.getAmount(), "SYSTEM", walletTransaction.getRemark()+"  REVERSAL");
                    }
                    walletTransaction.setReversed(true);
                    this.walletTransactionRepository.save(walletTransaction);
                }

                commission.setReversed("TRUE");
                this.commssionRepo.save(commission);
                transaction.setStatus((short)0);
                transaction.setStatusdescription("REVERSED");
                this.transactionRepository.save(transaction);

            } else {
                this.logger.info("No reversal transaction  found for  " + ptspModel.getTerminalId() + " " + ptspModel.getRrn());
            }

        }
    }

    private void processPtsp(List<PtspModel> ptspTranHist, PtspModel ptspModel, Agents agent) {
        System.out.println("**** PROCESSING PTSP TRANSACTIONS *****");

        try {
            if (ptspModel.getReversal() != null && ptspModel.getReversal().toLowerCase().equals("true")) {
                Boolean creditHasBeenDone = false;
                for(int a=0;a<ptspTranHist.size();a++){
                    if(ptspTranHist.get(a).getReversal().equalsIgnoreCase("false")){
                        creditHasBeenDone = true;
                    }
                }
                this.handleReversal(creditHasBeenDone,ptspModel, agent);
                ptspModel.setProcessor_status("SUCCESS");
            } else {
                this.handleWithdrawalCredit(ptspModel, agent);
                ptspModel.setProcessor_status("SUCCESS");
            }

            ptspModel.setProcessed(true);
            this.ptspRepository.save(ptspModel);
        } catch (Exception var4) {
            var4.printStackTrace();
            ptspModel.setProcessed(true);
            ptspModel.setProcessor_status("FAILED: " + var4.getMessage());
            this.ptspRepository.save(ptspModel);

            throw new GravityException(ptspModel.getProcessor_status());
        }

    }


    public Double doCommission(Agents agent,PtspModel ptspModel,Double amount,Double tranFee,Long tranId,boolean isDispute) {


        String channelType = ptspModel.getProductId();

        TranChannel tranChannel;

        if(channelType.equalsIgnoreCase("INTERSWITCH")){
            tranChannel = tranChannelsRepository.findByChannelName(channelType);
        }else{
            channelType = "NIBSS";
            tranChannel = tranChannelsRepository.findByChannelName("NIBSS");
        }

        this.logger.info("Transaction Fee to be deducted from amount is :: "+tranFee);

        BankDetails acquirer = this.bankDetailsService.findByCBNCode(ptspModel.getTerminalId().substring(1,4));

        Double _3lineCommissionFromTransaction;
        Double agentCommission = 0.00;

        InstitutionDTO institution = institutionService.getInstitutionByAgentId(agent.getParentAgentId());

        if(institution == null){
            _3lineCommissionFromTransaction = tranFee ;
        }else{
            agentCommission = institution.getAgent_withdrawal_commission() * tranFee;

            _3lineCommissionFromTransaction = tranFee - agentCommission;
        }



        Double bankCommissionFromTransaction;

        if(channelType.equalsIgnoreCase("INTERSWITCH")){
            Double platformFeePerc = tranChannel.getMaxPerMiscAcquirerFee();
            bankCommissionFromTransaction = (Double.parseDouble(tranChannel.getAcquirerPercentageFee()) + platformFeePerc )
                    /100 * amount;

            Double cappedBankCommission = tranChannel.getMaxAcquirerFee() + (platformFeePerc/100)*amount;
            Double minCappedBankCommission = tranChannel.getMinimumAcquirerFee() + (platformFeePerc/100)*amount;

            if(bankCommissionFromTransaction < minCappedBankCommission){
                bankCommissionFromTransaction = minCappedBankCommission;
            }else if(bankCommissionFromTransaction > cappedBankCommission){
                bankCommissionFromTransaction = cappedBankCommission;
            }
        }else{
            bankCommissionFromTransaction = Double.parseDouble(acquirer.getAcquirePercentage()) / Double.parseDouble("100") * amount;
        }

        bankCommissionFromTransaction = new BigDecimal(Double.toString(bankCommissionFromTransaction)).
                setScale(2, RoundingMode.HALF_UP).doubleValue();



        Double _3lineActualCommission = _3lineCommissionFromTransaction - bankCommissionFromTransaction;

        Double amountAgentIsPaid = amount - tranFee;
        this.logger.debug(" after deducting {} from transaction amount {} agent trading wallet would be credited with {}", new Object[]{_3lineCommissionFromTransaction, amount, amountAgentIsPaid});

        try {
            Double miscellFee = this.commissionCharge.getCommissionForAmount(amount, TransactionType.STAMP_DUTY_WITHDRAWAL);
            if (miscellFee > 0.0D) {
                amountAgentIsPaid = amountAgentIsPaid - miscellFee;
                this.logger.info(" after deducting {} of {} from transaction amount {} agent trading wallet would be credited with {}", new Object[]{"STAMP_DUTY_WITHDRAWAL_CHARGE", miscellFee, amount - _3lineCommissionFromTransaction, amountAgentIsPaid});
                Transactions transaction = this.transactionRepository.findByTranId(tranId);
                Transactions stampDutyTran = new Transactions();
                stampDutyTran.setTransactionType("Withdrawal");
                stampDutyTran.setTransactionTypeDescription("CBN_STAMP_DUTY");
                stampDutyTran.setTranDate(new Date());
                stampDutyTran.setStatus((short)1);
                stampDutyTran.setStatcode("00");
                stampDutyTran.setStatusdescription("SUCCESSFUL");
                stampDutyTran.setAmount(miscellFee);
                stampDutyTran.setAgentName(transaction.getAgentName());
                stampDutyTran.setBankFrom(transaction.getBankFrom());
                stampDutyTran.setBankTo(transaction.getBankTo());
                stampDutyTran.setTerminalId(transaction.getTerminalId());
                stampDutyTran.setDescription(transaction.getDescription());
                stampDutyTran.setItexTranId(transaction.getItexTranId());
                stampDutyTran = (Transactions)this.transactionRepository.save(stampDutyTran);
                SettingDTO stampDutyWallet = this.settingService.getSettingByCode("CBN_STAMP_DUTY_WALLET");
                this.walletService.creditWallet(String.valueOf(tranId), stampDutyWallet.getValue(), miscellFee, "FREEDOM NETWORK", "STAMP_DUTY FOR RRN: " +
                        ptspModel.getRrn() + " and tran id: " + stampDutyTran.getTranId());
            }
        } catch (Exception var19) {
            this.logger.error(var19.getMessage());
        }

        String remark;
        if(isDispute){
                remark = "REVERSED WITHDRAWAL RRN{" +ptspModel.getRrn() + "}";
        }else{
            remark = "WITHDRAWAL RRN{" +ptspModel.getRrn() + "}";
        }

        this.logger.info("##### PASSING ENTRIES");
        this.logger.debug("CREDITING WALLET WITH PARTING CASH - > {}", amountAgentIsPaid);
        this.walletService.creditWallet(String.valueOf(tranId), agent.getWalletNumber(), amountAgentIsPaid, "PTSP", remark);

        if(institution !=null && agentCommission > 0.00){
            this.walletService.creditWallet(String.valueOf(tranId), agent.getIncomeWalletNumber(), agentCommission, "FREEDOM NETWORK", "AGENT COMMISSION");
        }


        //*************Commission Sharing Part****************//
        this.logger.debug("after deducting 3lines commission which is {} amount paid cutomer is {}", _3lineCommissionFromTransaction, amountAgentIsPaid);
        Double parentAgentCommission = 0.0D;
        if(institution!=null){
            parentAgentCommission = institution.getAggregator_withdrawal_commission() * _3lineActualCommission;
            Agents parentAgent = agentsRepository.findById(agent.getParentAgentId()).get();

            this.walletService.creditWallet(String.valueOf(tranId), parentAgent.getIncomeWalletNumber(), parentAgentCommission, "PTSP", "PARENT AGENT COMMISSION ");
        }else{

            if (_3lineActualCommission > 0 && Objects.nonNull(agent.getParentAgentId()) && agent.getParentAgentId() > 0L) {
                Agents parent = agentsRepository.findById(agent.getParentAgentId()).get();
                Double parentCommFeePerc = Double.valueOf(parent.getCommissionFeePercentage());

                this.logger.debug("agent has an agregator  {}  , percentage for aggregator is {}%", agent.getParentAgentId(), parent.getCommissionFeePercentage());
                parentAgentCommission = parentCommFeePerc / Double.parseDouble("100") * _3lineActualCommission;

                parentAgentCommission = new BigDecimal(Double.toString(parentAgentCommission)).
                        setScale(2, RoundingMode.HALF_UP).doubleValue();

                this.logger.debug("giving parent agent amount for parent agent ");
                this.walletService.creditWallet(String.valueOf(tranId), parent.getIncomeWalletNumber(), parentAgentCommission, "FREEDOM NETWORK", "PARENT AGENT COMMISION");
            }
        }

        _3lineActualCommission = _3lineActualCommission - parentAgentCommission;



        try {
            SettingDTO _3lineFinance = this.settingService.getSettingByCode("MAGTIPON_GL_WALLET");
            this.walletService.creditWallet(String.valueOf(tranId), _3lineFinance.getValue(), _3lineActualCommission, "FREEDOM NETWORK", "SUPER AGENT COMMISION");
        } catch (Exception var18) {
            var18.printStackTrace();
        }


        this.logger.info("commission was splitted as :: \n " +
                        "_3lineActualCommission {}," +
                        "_3lineCommissionFromTransaction {},amountAgentPayCostumer {}," +
                        "parentAgentCommission {}, agentCommission {}, " +
                        "bankCommissionFromTransaction {}",
                _3lineActualCommission,
                tranFee,
                amountAgentIsPaid,
                parentAgentCommission,
                agentCommission,
                bankCommissionFromTransaction) ;

        GravityDailyCommission dailyCommission = new GravityDailyCommission();
        dailyCommission.setTransactionChannel(channelType);
        dailyCommission.set_3lineCommission(_3lineActualCommission.toString());
        dailyCommission.setBankCommission(bankCommissionFromTransaction.toString());
        dailyCommission.setAgentBank(acquirer.getBankName());
        dailyCommission.setAgentBankCode(agent.getBankCode());
        dailyCommission.setAgentCommission(agentCommission.toString());
        dailyCommission.setAgentName(agent.getUsername());
        dailyCommission.setAgentAccount(agent.getAccountNo());
        dailyCommission.setParentAgentCommission(parentAgentCommission.toString());
        dailyCommission.setTransactionAmount(amount.toString());
        dailyCommission.setTransactionId(tranId.toString());
        dailyCommission.setTransactionType(TransactionType.WITHDRAWAL);
        this.commssionRepo.save(dailyCommission);
        return _3lineCommissionFromTransaction;
    }

}
