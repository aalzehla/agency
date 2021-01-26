package com._3line.gravity.freedom.wallet.service.impl;

import com._3line.gravity.core.email.service.MailService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.setting.dto.SettingDTO;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.utils.AppUtility;
import com._3line.gravity.core.verification.annotations.RequireApproval;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.repository.BankDetailsRepository;
import com._3line.gravity.freedom.dispute.dtos.DisputeDto;
import com._3line.gravity.freedom.dispute.models.Dispute;
import com._3line.gravity.freedom.dispute.repository.DisputeRepository;
import com._3line.gravity.freedom.financialInstitutions.dtos.DepositRequest;
import com._3line.gravity.freedom.financialInstitutions.magtipon.MagtiponBankProcessor;
import com._3line.gravity.freedom.issuelog.models.IssueLog;
import com._3line.gravity.freedom.issuelog.models.IssueStatus;
import com._3line.gravity.freedom.issuelog.service.IssueLogService;
import com._3line.gravity.freedom.notifications.dtos.NotificationDTO;
import com._3line.gravity.freedom.notifications.service.MobileNotificationService;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.utility.DateUtil;
import com._3line.gravity.freedom.utility.Utility;
import com._3line.gravity.freedom.wallet.dto.*;
import com._3line.gravity.freedom.wallet.exceptions.WalletException;
import com._3line.gravity.freedom.wallet.models.*;
import com._3line.gravity.freedom.wallet.repository.*;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@Service
public class WalletServiceImplementation implements WalletService {


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ModelMapper modelMapper ;
    @Autowired
    FreedomWalletRepository freedomWalletRepository ;
    @Autowired
    WalletTransactionRepository walletTransactionRepository ;
    @Autowired
    BankDetailsRepository bankDetailsRepository ;
    @Autowired
    SettingService settingService ;
    @Autowired
    MagtiponBankProcessor magtiponProcessor;
    @Autowired
    CreditRequestRepository creditRequestRepository ;
    @Autowired
    MailService mailService;
    @Autowired
    DisputeRepository disputeRepository;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    IssueLogService issueLogService;

    @Autowired
    MobileNotificationService notificationService;


    @Autowired
    NIBSSReconcilationRepository nibssRecRepo;

    @Autowired
    AgentsWalletReconcilationRepository agentsWalletReconcilation;

    @Autowired
    AgentService agentService;

    @Autowired
    TransactionRepository  transactionRepository;

    @Autowired
    EntityManager entityManager;

    @Value("${threshold.amount}")
    private String amountThres;


    private final ExecutorService executors = Executors.newFixedThreadPool(10000);


    private final Locale locale = LocaleContextHolder.getLocale();

    @Override
    public String createWallet(String walletNumber, String purpose) throws WalletException {

        FreedomWallet tosave = new FreedomWallet();
        tosave.setPurpose(purpose);

        String walnum = walletNumber ;

        if(Objects.nonNull(walletNumber)) {
            FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(walletNumber);
            if (Objects.nonNull(wallet)) {
                throw new WalletException("Wallet with number already exist -->" + walletNumber);
            }

            tosave.setWalletNumber(walnum);

            freedomWalletRepository.save(tosave);

        }else {
            logger.info("wallet number not provided ");
            logger.info("wallet number will be generated");

            walnum = Long.valueOf(Utility.generateID()).toString();
            tosave.setWalletNumber(walnum);

            logger.info("generated wallet number {}", walnum);

            freedomWalletRepository.save(tosave);
        }
        return walnum ;
    }

//    @Transactional`
    @Override
    public  synchronized void  creditWallet(String tranId,String walletNumber, double amount, String channel, String remark) throws WalletException {

        if(tranId==null){
            tranId = Utility.generateID();
        }
        logger.debug("crediting -->{} , amount -->{} , reason -->{} , channel -->{}", walletNumber , amount,remark,channel);
        FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(walletNumber);

        if(Objects.isNull(wallet)){
            logger.debug("wallet -->{} not found", walletNumber);
            throw  new WalletException("Wallet not found");
        }

        logger.debug("wallet -->{} , current balances avai{} ,ledg{}",walletNumber , wallet.getAvailableBalance() , wallet.getLedgerBalance());
        logger.debug("wallet --> {} , lasttran {}", walletNumber , wallet.getLastTranDate());
        logger.debug("wallet --{} , to credit {}", walletNumber , amount);

        // preparing to credit wallet with amount

        // to credit wallet
        logger.debug("begining credit request -->{} on wallet -->{}", amount , walletNumber);
        Double oldBalance = wallet.getAvailableBalance();
        Double newBalance = wallet.getAvailableBalance() + amount;

        wallet.setAvailableBalance(newBalance);
        wallet.setLastTranDate(new Date());
        freedomWalletRepository.save(wallet);
        logger.debug("successs--->  {}", walletNumber);
        logger.info("wallet credited successfully--> {} , balance -->{}" , wallet.getWalletNumber() , wallet.getAvailableBalance());


        //logging transaction
        FreedomWalletTransaction freedomWalletTransaction = new FreedomWalletTransaction();
        freedomWalletTransaction.setAmount(amount);
        freedomWalletTransaction.setWallet(wallet);
        freedomWalletTransaction.setWalletNumber(walletNumber);
        freedomWalletTransaction.setChannel(channel);
        freedomWalletTransaction.setTranType(TranType.CREDIT);
        freedomWalletTransaction.setRemark(remark);
        freedomWalletTransaction.setTranID(tranId);
        freedomWalletTransaction.setBalanceBefore(oldBalance);
        freedomWalletTransaction.setBalanceAfter(newBalance);
        logger.debug("system transaction created -->{}",freedomWalletTransaction.toString());
        FreedomWalletTransaction tran = walletTransactionRepository.save(freedomWalletTransaction);
        logger.debug("wallet -->{} , transaction logged", walletNumber);




        // debit gl wallet , for every credit there is a debit
        try{
            debitWalletGlAccount(amount , channel , remark + "{"+tran.getTranID() + "}");
        }catch(WalletException w){
            //reverse credit
            this.logger.info("Reversing credit for failed GL debit Transaction");
            debitWallet(tranId,walletNumber,amount,"MOBILE", "(REVERSE CREDIT) "+remark);
            throw new GravityException("Error occurred crediting wallet.");
        }finally {

        }


    }

    private synchronized void debitWalletGlAccount(double amount , String channel , String remark) throws WalletException{

        final Future<?> processCallBack = executors.submit(() -> {

            String glWallet = settingService.getSettingByCode("MAGTIPON_GL_WALLET").getValue() ;
            FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(glWallet);
            //logging transaction
            Double oldBalance = wallet.getAvailableBalance();
            Double newBalance = wallet.getAvailableBalance() - amount;

            FreedomWalletTransaction freedomWalletTransaction = new FreedomWalletTransaction();
            freedomWalletTransaction.setAmount(amount);
            freedomWalletTransaction.setWallet(wallet);
            freedomWalletTransaction.setWalletNumber(glWallet);
            freedomWalletTransaction.setChannel(channel);
            freedomWalletTransaction.setTranType(TranType.DEBIT);
            freedomWalletTransaction.setRemark(remark);
            freedomWalletTransaction.setBalanceBefore(oldBalance);
            freedomWalletTransaction.setBalanceAfter(newBalance);
            logger.debug("system transaction created -->{}",freedomWalletTransaction.toString());
            walletTransactionRepository.save(freedomWalletTransaction);
            logger.debug("wallet -->{} , transaction logged", glWallet);

            // transaction saved

            // to debit wallet
            logger.debug("begining debit request -->{} on gl wallet -->{}", amount , glWallet);
            wallet.setAvailableBalance(newBalance);
            wallet.setLastTranDate(new Date());
            freedomWalletRepository.save(wallet);

        });

        try {
            processCallBack.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new WalletException("Error occurred debiting GL Wallet");
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new WalletException("Error occurred debiting GL Wallet");
        }


    }

    private synchronized void creditWalletGlAccount(double amount , String channel , String remark) throws WalletException{

        final Future<?> division = executors.submit(() -> {

            String glWallet = settingService.getSettingByCode("MAGTIPON_GL_WALLET").getValue() ;
            FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(glWallet);

            // to credit wallet
            logger.debug("begining credit request -->{} on wallet -->{}", amount , glWallet);

            Double oldBalance = wallet.getAvailableBalance();
            Double newBalance = wallet.getAvailableBalance() + amount;

            wallet.setAvailableBalance(newBalance);
            wallet.setLastTranDate(new Date());
            freedomWalletRepository.save(wallet);

            // transaction saved
            FreedomWalletTransaction freedomWalletTransaction = new FreedomWalletTransaction();
            freedomWalletTransaction.setAmount(amount);
            freedomWalletTransaction.setWallet(wallet);
            freedomWalletTransaction.setWalletNumber(glWallet);
            freedomWalletTransaction.setChannel(channel);
            freedomWalletTransaction.setTranType(TranType.CREDIT);
            freedomWalletTransaction.setRemark(remark);
            freedomWalletTransaction.setBalanceBefore(oldBalance);
            freedomWalletTransaction.setBalanceAfter(newBalance);
            logger.debug("system transaction created -->{}",freedomWalletTransaction.toString());
            walletTransactionRepository.save(freedomWalletTransaction);
            logger.debug("wallet -->{} , transaction logged", glWallet);

        });

        try {
            division.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new WalletException("Error occurred crediting GL Wallet");
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new WalletException("Error occurred crediting GL Wallet");
        }


    }




    @Override
    public synchronized void debitWallet(String tranId,String walletNumber, double amount, String channel, String remark) throws WalletException {
        logger.info("debiting -->{} , amount -->{} , reason -->{} , channel -->{}", walletNumber , amount,remark,channel);
        FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(walletNumber);

        if(tranId==null){
            tranId = Utility.generateID();
        }

        if(Objects.isNull(wallet)){
            logger.debug("wallet -->{} not found", walletNumber);
            throw  new WalletException("Wallet not found");
        }


        if(wallet.getIsGeneralLedger().equals("NO")) {

            if (wallet.getAvailableBalance() < amount) {
                throw new WalletException("Transaction amount greater than wallet balance");
            }

            if ((wallet.getAvailableBalance() - amount) < 0) {
                throw new WalletException("Wallet balance would be less than zero , not allowed");
            }

        }else {
            logger.info("perfoming transaction on sanef ledger ");
        }

        logger.debug("wallet -->{} , current balances avai{} ,ledg{}",walletNumber , wallet.getAvailableBalance() , wallet.getLedgerBalance());
        logger.debug("wallet --> {} , lasttran {}", walletNumber , wallet.getLastTranDate());
        logger.debug("wallet --{} , to debit {}", walletNumber , amount);

        // preparing to credit wallet with amount

        // to debit wallet
        logger.debug("begining debit request -->{} on wallet -->{}", amount , walletNumber);

        Double oldBalance = wallet.getAvailableBalance();
        Double newBalance = wallet.getAvailableBalance() - amount;

        wallet.setAvailableBalance(newBalance);
        wallet.setLastTranDate(new Date());
        freedomWalletRepository.save(wallet);
        logger.debug("successs--->  {}", walletNumber);
        logger.info("wallet debited successfully--> {} , balance -->{}" , wallet.getWalletNumber() , wallet.getAvailableBalance());


        //logging transaction
        FreedomWalletTransaction freedomWalletTransaction = new FreedomWalletTransaction();
        freedomWalletTransaction.setAmount(amount);
        freedomWalletTransaction.setWallet(wallet);
        freedomWalletTransaction.setWalletNumber(walletNumber);
        freedomWalletTransaction.setChannel(channel);
        freedomWalletTransaction.setTranType(TranType.DEBIT);
        freedomWalletTransaction.setRemark(remark);
        freedomWalletTransaction.setTranID(tranId);
        freedomWalletTransaction.setBalanceBefore(oldBalance);
        freedomWalletTransaction.setBalanceAfter(newBalance);
        logger.debug("system transaction created -->{}",freedomWalletTransaction.toString());
        FreedomWalletTransaction tran =  walletTransactionRepository.save(freedomWalletTransaction);
        logger.debug("wallet -->{} , transaction logged", walletNumber);

        // transaction saved


        try{
            creditWalletGlAccount(amount , channel , remark + "{"+tran.getTranID() + "}");
        }catch(WalletException w){
            this.logger.info("Reversing debit for failed GL credit Transaction");
            creditWallet(tranId,walletNumber, amount , "MOBILE", "(REVERSE DEBIT) "+remark);
            throw new GravityException("Error Occurred debiting wallet");
        }

    }

    @Transactional
    @Override
    public void transferWalletFunds(String tranId,String walletNumber, String destinationWalletNumber, double amount, String channel, String remark) throws WalletException {
        logger.info("transfer -->{} , destination -->{} , amount -->{} , reason -->{} , channel -->{}", walletNumber, destinationWalletNumber , amount,remark,channel);
        FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(walletNumber);
        FreedomWallet destinationWallet = freedomWalletRepository.findByWalletNumber(destinationWalletNumber);

        if(tranId==null){
            tranId = Utility.generateID();
        }

        if(Objects.isNull(wallet)){
            logger.debug("wallet -->{} not found", walletNumber);
            throw  new WalletException("Wallet not found");
        }

        if(Objects.isNull(destinationWallet)){
            logger.debug("destination wallet -->{} not found", walletNumber);
            throw  new WalletException("destination wallet not found");
        }

        if(wallet.getAvailableBalance() < amount){
            throw  new WalletException("Transaction amount greater than wallet balance");
        }

        if((wallet.getAvailableBalance() - amount) < 0 ){
            throw  new WalletException("Wallet balance would be less than zero , not allowed");
        }

        logger.debug("wallet -->{} , current balances avai{} ,ledg{}",walletNumber , wallet.getAvailableBalance() , wallet.getLedgerBalance());
        logger.debug("wallet --> {} , lasttran {}", walletNumber , wallet.getLastTranDate());
        logger.debug("wallet --{} , to debit {}", walletNumber , amount);
        logger.debug("wallet --{} , to credit {}", destinationWallet , amount);

        // preparing to credit wallet with amount

        //logging transaction
        FreedomWalletTransaction freedomWalletTransaction = new FreedomWalletTransaction();
        freedomWalletTransaction.setAmount(amount);
        freedomWalletTransaction.setWallet(wallet);
        freedomWalletTransaction.setWalletNumber(walletNumber);
        freedomWalletTransaction.setChannel(channel);
        freedomWalletTransaction.setTranID(tranId);
        freedomWalletTransaction.setTranType(TranType.DEBIT);
        String senderRemark = "TRANSFER TO { "+destinationWalletNumber +"}";
        freedomWalletTransaction.setRemark(senderRemark);
        freedomWalletTransaction.setBalanceBefore(wallet.getAvailableBalance());
        freedomWalletTransaction.setBalanceAfter((wallet.getAvailableBalance() - amount));
        logger.debug("system transaction created -->{}",freedomWalletTransaction.toString());
        FreedomWalletTransaction saved = walletTransactionRepository.save(freedomWalletTransaction);
        logger.debug("wallet -->{} , transaction logged", walletNumber);


        FreedomWalletTransaction transfer = new FreedomWalletTransaction();
        transfer.setAmount(amount);
        transfer.setWallet(destinationWallet);
        transfer.setWalletNumber(destinationWalletNumber);
        transfer.setChannel(channel);
        transfer.setTranID(tranId);
        transfer.setTranType(TranType.CREDIT);
        String receiverRemark = "TRANSFER FROM { "+walletNumber +"} ";
        transfer.setRemark(receiverRemark);
        transfer.setBalanceBefore(destinationWallet.getAvailableBalance());
        transfer.setBalanceAfter((destinationWallet.getAvailableBalance() + amount));
        logger.debug("system transaction created -->{}",transfer.toString());
        walletTransactionRepository.save(transfer);
        logger.debug("wallet -->{} , transaction logged", destinationWallet);

        // transaction saved

        // to debit wallet
        logger.debug("begining transfer request -->{} on wallet -->{}", amount , walletNumber);
        wallet.setAvailableBalance(wallet.getAvailableBalance() - amount);
        wallet.setLastTranDate(new Date());
        freedomWalletRepository.save(wallet);
        logger.debug("successs--->  {}", walletNumber);
        logger.info("wallet debited successfully--> {} , balance -->{}" , wallet.getWalletNumber() , wallet.getAvailableBalance());

        destinationWallet.setAvailableBalance(destinationWallet.getAvailableBalance() + amount);
        destinationWallet.setLastTranDate(new Date());
        freedomWalletRepository.save(destinationWallet);
        logger.debug("successs--->  {}", destinationWallet);
        logger.info("wallet credited. successfully--> {} , balance -->{}" , destinationWallet.getWalletNumber() , destinationWallet.getAvailableBalance());
    }

    @Transactional
    @Override
    public  void transferWalletFundToBank(String tranId,String walletNumber, String destinationAccount, String destinationBank, double amount, String channel, String remark) throws WalletException {
        logger.info("transfer -->{} , destination -->{} , amount -->{} , reason -->{} , channel -->{}", walletNumber, destinationAccount , amount,remark,channel);
        FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(walletNumber);

        if(tranId==null){
            tranId = Utility.generateID();
        }

        if(Objects.isNull(wallet)){
            logger.debug("wallet -->{} not found", walletNumber);
            throw  new WalletException("Wallet not found");
        }

        if(wallet.getAvailableBalance() < amount){
            throw  new WalletException("Transaction amount greater than wallet balance");
        }

        if((wallet.getAvailableBalance() - amount) < 0 ){
            throw  new WalletException("Wallet balance would be less than zero , not allowed");
        }

        logger.debug("wallet -->{} , current balances avai{} ,ledg{}",walletNumber , wallet.getAvailableBalance() , wallet.getLedgerBalance());
        logger.debug("wallet --> {} , lasttran {}", walletNumber , wallet.getLastTranDate());
        logger.debug("wallet --{} , to debit {}", walletNumber , amount);
        logger.debug("wallet --{} , to credit {}", destinationAccount , amount);

        // preparing to credit wallet with amount

        //logging transaction
        FreedomWalletTransaction freedomWalletTransaction = new FreedomWalletTransaction();
        freedomWalletTransaction.setAmount(amount);
        freedomWalletTransaction.setWallet(wallet);
        freedomWalletTransaction.setWalletNumber(walletNumber);
        freedomWalletTransaction.setChannel(channel);
        freedomWalletTransaction.setTranID(tranId);
        freedomWalletTransaction.setTranType(TranType.TRANSFER);
        freedomWalletTransaction.setRemark(remark);
        logger.debug("system transaction created -->{}",freedomWalletTransaction.toString());
        freedomWalletTransaction = walletTransactionRepository.save(freedomWalletTransaction);
        logger.debug("wallet -->{} , transaction logged", walletNumber);

        SettingDTO magtiponFee = settingService.getSettingByCode("WALLET_TRANSFER_MAGTIPON_FEE");


        BankDetails bankDetails = bankDetailsRepository.findByBankCode(destinationBank) ;

        DepositRequest depositRequest = new DepositRequest() ;
        depositRequest.setAccountNumber(destinationAccount);
        depositRequest.setBankCode(bankDetails.getCbnCode());
        Double amountocredit =  amount - Double.valueOf(magtiponFee.getValue()) ;
        logger.info("crediting agent with {}", amountocredit);
        depositRequest.setAmount(amountocredit.toString());
        depositRequest.setCustomerEmail("freedom network");
        depositRequest.setCustomerName("freedom network");
        depositRequest.setCustomerPhone("");
        logger.info("payload to magtipom ?{}", depositRequest.toString());
        Response response = magtiponProcessor.performDeposit(depositRequest, Long.valueOf(freedomWalletTransaction.getTranID()));

         if(!response.getRespCode().equals("00")){
             throw new GravityException(response.getRespDescription());
         }
    }

    @Override
    public WalletDTO getWalletByNumber(String walletNumber) {
        FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(walletNumber);

        if(Objects.nonNull(wallet)){
            WalletDTO walletDTO = modelMapper.map(wallet, WalletDTO.class);
            walletDTO.setOpeningDate(DateUtil.formatDateToreadable(wallet.getCreatedOn()));
            walletDTO.setAvailableBalance(round(wallet.getAvailableBalance() ,2));
            if(Objects.nonNull(wallet.getLastTranDate())){
                walletDTO.setLastTran(DateUtil.formatDateToreadable(wallet.getLastTranDate()));
            }

            return walletDTO ;
        }

        return null;
    }

    @Override
    public FreedomWallet getWalletObjByNumber(String walletNumber) {
        FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(walletNumber);
        return wallet;
    }


    public static synchronized double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public List<WalletDTO> getAllWallets() {
        List<WalletDTO> all = new ArrayList<>();
        List<FreedomWallet> freedomWallets = freedomWalletRepository.findAll();

        for (FreedomWallet w: freedomWallets) {
            WalletDTO walletDTO = modelMapper.map(w, WalletDTO.class);
            walletDTO.setOpeningDate(DateUtil.formatDateToreadable(w.getCreatedOn()));
            if(Objects.nonNull(w.getLastTranDate())){
                walletDTO.setLastTran(DateUtil.formatDateToreadable(w.getLastTranDate()));
            }
            all.add(walletDTO);
        }
        return all;
    }

    @Override
    public List<TransactionDTO> getWalletTransactions(String walletNumber) {
        List<TransactionDTO> transactionDTOS = new ArrayList<>( );
        List<FreedomWalletTransaction> walletTransactions = walletTransactionRepository.findByWalletNumber(walletNumber);
        for (FreedomWalletTransaction f: walletTransactions) {
            TransactionDTO transactionDTO = modelMapper.map(f, TransactionDTO.class);
            transactionDTO.setDate(DateUtil.formatDateToreadable(f.getTranDate()));
            transactionDTO.setWalletNumber(f.getWallet().getWalletNumber());
            transactionDTOS.add(transactionDTO) ;
        }
        return transactionDTOS;
    }

    @Override
    public List<FreedomWalletTransaction> getWalletTransactionsByTranId(String tranId) {
        List<TransactionDTO> transactionDTOS = new ArrayList<>( );
        List<FreedomWalletTransaction> walletTransactions = walletTransactionRepository.findByTranID(tranId);

        return walletTransactions;
    }



    @Override
    public Page<FreedomWalletTransaction> getWalletTransactionsWithPagination(String walletNumber, Pageable pageable) {


        Page<FreedomWalletTransaction> walletTransactions
                = walletTransactionRepository.findAllByWalletNumberOrderByTranDateDesc(walletNumber,pageable);

        return walletTransactions;
    }

    @Override
    public Page<FreedomWalletTransaction> getWalletTransactionsWithDatePagination(String walletNumber, Date from, Date to, Pageable pageable) {

        Page<FreedomWalletTransaction> walletTransactions
                = walletTransactionRepository.findAllByWalletNumberAndTranDateBetweenOrderByTranDateDesc(walletNumber,from,to,pageable);

        return walletTransactions;
    }


    @Override
    public List<FreedomWalletTransaction> getWalletTransactionsByDate(String walletNumber, Date from, Date to) {

        List<FreedomWalletTransaction> walletTransactions = walletTransactionRepository.findByWalletNumberAndTranDateBetweenOrderByIdDesc(walletNumber, from ,DateUtil.AddDays(to , 1) );
        return walletTransactions;
    }

    @Override
    public List<FreedomWalletTransaction> getWalletTransactionsByDateAndTranType(String walletNumber,TranType tranType , Date from, Date to) {

        List<FreedomWalletTransaction> walletTransactions = walletTransactionRepository.findByWalletNumberAndTranTypeAndTranDateBetweenOrderByIdDesc(walletNumber,tranType.toString(), from , DateUtil.AddDays(to , 1));

        return walletTransactions;
    }

    @Override
    public Page<FreedomWalletTransaction> findAll(String pattern,String from , String to , Pageable pageDetails) {
        Date startDate = new Date() ;
        Date endDate = new Date();
        Long tranId  = Long.parseLong("0") ;

        try {
            tranId = Long.parseLong(pattern);
        }catch (Exception we){

        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            startDate = dateFormat.parse(from);
            endDate = dateFormat.parse(to);

        }catch (Exception e){

        }


        return walletTransactionRepository.findByWalletNumberLikeOrTranIDEqualsOrRemarkContainingAndTranDateBetweenOrderByTranDateDesc(pattern, String.valueOf(tranId),pattern,startDate,endDate ,pageDetails);

    }

    @Override
    public void createCreditRequest(CreditRequestDTO creditRequestDTO) throws GravityException {
        logger.info("--------> BEGIGNING CREDIT REQUEST FOR WALLET {}",creditRequestDTO.getWalletNumber());

        if(Double.parseDouble(creditRequestDTO.getAmount()) <= 0){
            throw new GravityException("Invalid Amount specified");
        }

        FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(creditRequestDTO.getWalletNumber());
        if(Objects.isNull(wallet)){
            throw new GravityException("WALLET DOES NOT EXIST");
        }
//        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        CreditRequest creditRequest = modelMapper.map(creditRequestDTO, CreditRequest.class);
        creditRequest.setStatus(Status.PENDING);
        creditRequest.setRequestDate(new Date());
        if (creditRequest.getAmount() != null) {
            final boolean b = Double.parseDouble(creditRequest.getAmount()) >= Double.parseDouble(amountThres);
            if (b) {
                sendMailNotification(creditRequest);
            }
        }
        creditRequestRepository.save(creditRequest);

        logger.info("------> CREDIT REQUEST LOGGED SUCCESSFULLY");
        logger.info("-----> SENDING NOTIFICATION ");

        SettingDTO settingDTO = settingService.getSettingByCode("CREDIT_REQUEST_NOTIFICATION_EMAIL");

        if(settingDTO.isEnabled()){
            logger.info("EMAIL SENDING ENABLED ----");
            Map<String , Object> params = new HashMap<>();
            params.put("agent",creditRequest.getAgentName());
            params.put("bank",creditRequest.getBank());
            params.put("account",creditRequest.getAccountNumber());
            params.put("amount",creditRequest.getAmount());
            mailService.sendMail("New Credit Request" ,settingDTO.getValue() ,null ,params ,"credit-request");
//            new Notifier<>().sendEmail2(creditRequestDTO,"freedom@3lineng.com", "CREDIT WALLET REQUEST" , settingDTO.getValue() ,"mailtemplate/credit-request.html");
            logger.info("mail sent---..");
        }

    }

    private void sendMailNotification(CreditRequest creditRequest) {
        SettingDTO mailConfig = settingService.getSettingByCode("CREDIT_REQUEST_MAIL_THRESHOLD");
        boolean isEnable = mailConfig.isEnabled() && mailConfig.getValue() != null;
        if (isEnable) {
//            List<String> mailAdresss = Arrays.asList(mailConfig.getValue().split(","));
//            String[] addr = new String[mailAdresss.size()];
//            for (int i = 0; i < mailAdresss.size(); i++) {
//                addr[i] = mailAdresss.get(i);
//            }
//            logger.info("list of mails {}", addr);
            Map<String, Object> params = new HashMap<>();
            params.put("agent", creditRequest.getAgentName());
            params.put("bank", creditRequest.getBank());
            params.put("referenceNumber", creditRequest.getBankReference());
            params.put("amount", creditRequest.getAmount());
            if(creditRequest.getApprovedBy()==null){
                params.put("message","created");
            }
            else{
                params.put("message","approved");
            }
            mailService.sendMail("New Credit Request", mailConfig.getValue(), null, params, "credit-request");
            logger.info("mail sent---..");
        }
    }

    @Override
    public Page<CreditRequest> findAllPending(Pageable pageable){
        Page<CreditRequest> page = creditRequestRepository.findByStatusOrderByCreatedOnDesc(Status.PENDING,pageable);

        return page;

//        List<CreditRequestDTO> dtOs = new ArrayList<>();
//
//        for (CreditRequest p:page.getContent()) {
//            CreditRequestDTO cc = modelMapper.map(p , CreditRequestDTO.class);
//            cc.setRequestDate(p.getRequestDate().toString());
////            cc.setStat(p.getStatus().name());
//            dtOs.add(cc);
//        }
//        long t = page.getContent().size();
//        return new PageImpl<>(dtOs, pageable, t);
    }

    @Override
    public Page<CreditRequest> findAllRequestHistory(Pageable pageable){
        Page<CreditRequest> page = creditRequestRepository.findAllByOrderByApprovalDateDesc(pageable);

        return page;

//        List<CreditRequestDTO> dtOs = new ArrayList<>();
//
//        for (CreditRequest p:page.getContent()) {
//            CreditRequestDTO cc = modelMapper.map(p , CreditRequestDTO.class);
//            cc.setRequestDate(p.getCreatedOn().toString());
//            cc.setApprovalDate(p.getApprovalDate());
////            cc.setStat(p.getStatus().name());
//            dtOs.add(cc);
//        }
//        long t = page.getContent().size();
//        return new PageImpl<>(dtOs, pageable, t);
    }

    @Override
    public Page<FreedomWallet> gLAccounts(Pageable pageable) {
        return freedomWalletRepository.findByIsGeneralLedger("YES",pageable);
    }

    @Override
    public Page<Dispute> getAllDispute(String agentName,String fromDate,String toDate,String tranType,Pageable pageable) {

        Date startDate = new Date();
        Date endDate = new Date();


        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Dispute> q = cb.createQuery(Dispute.class);
        Root<Dispute> c = q.from(Dispute.class);
        List<Predicate> predicates = new ArrayList<>();

        try {

            if (startDate != null && endDate!=null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                endDate = dateFormat.parse(toDate);
                startDate = dateFormat.parse(fromDate);
                predicates.add(cb.between(c.get("createdOn"), startDate, endDate) );
            }

            if (agentName != null && !agentName.equals("")) {
                predicates.add((cb.equal(c.get("agentName"), agentName)) );
            }


            if (tranType != null && !tranType.equals("")) {
                predicates.add((cb.equal(c.get("type"), tranType)) );
            }


        } catch (Exception e) {
            e.printStackTrace();
            return new PageImpl<>(new ArrayList<>());
        }

        CriteriaQuery<Dispute> baseQuery = null;
        CriteriaQuery<Long> qc = cb.createQuery(Long.class).orderBy();
        CriteriaQuery<Long> countQuery = null;
        if (predicates.size() == 0) {
            baseQuery = q.select(c);
            countQuery = qc.select(cb.count(qc.from(Dispute.class)));
        } else {
            Predicate and = cb.and(predicates.toArray(new Predicate[0]));
            baseQuery = q.select(c).where(and).orderBy(cb.desc(c.get("id")));
            countQuery = qc.select(cb.count(qc.from(Dispute.class))).where(and);
        }

        TypedQuery<Dispute> query = entityManager.createQuery(baseQuery);
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Dispute> list = query.getResultList();
        return new PageImpl<>(list, pageable, count);

    }

    @Override
    public List<CreditRequest> getFilteredCreditRequest(String agentName, String from, String to, String status) {

        Date startDate = new Date();
        Date endDate = new Date();


        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CreditRequest> q = cb.createQuery(CreditRequest.class);
        Root<CreditRequest> c = q.from(CreditRequest.class);
        List<Predicate> predicates = new ArrayList<>();

        try {

            if (agentName != null && !agentName.equals("")) {
                predicates.add((cb.equal(c.get("agentName"), agentName)) );
            }

            if (startDate != null && endDate!=null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                startDate = dateFormat.parse(from);
                endDate = dateFormat.parse(to);
                predicates.add(cb.between(c.get("createdOn"), startDate, endDate) );
            }


            if (status != null && !status.equals("")) {
                predicates.add((cb.equal(c.get("status"), Status.valueOf(status)) ));
            }else{
                predicates.add((cb.notEqual(c.get("status"), Status.valueOf("PENDING")) ));
            }


        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        CriteriaQuery<CreditRequest> baseQuery = null;
        CriteriaQuery<Long> qc = cb.createQuery(Long.class).orderBy();
        if (predicates.size() == 0) {
            baseQuery = q.select(c).orderBy(cb.desc(c.get("id")));
        } else {
            Predicate and = cb.and(predicates.toArray(new Predicate[0]));
            baseQuery = q.select(c).where(and).orderBy(cb.desc(c.get("id")));
        }

        TypedQuery<CreditRequest> query = entityManager.createQuery(baseQuery);
        List<CreditRequest> list = query.getResultList();
        return list;

    }

    @Override
    public void approveCreditRequest(Long id,String bankRef) {
        SettingDTO magtiponWallet = settingService.getSettingByCode("MAGTIPON_GL_WALLET");


        if(bankRef==null || bankRef.equals("")){
            throw new GravityException("Invalid Bank Reference entered");
        }

        CreditRequest creditRequest = creditRequestRepository.findByBankReference(bankRef);
        if(creditRequest!=null){
            throw new GravityException("Bank Reference Already Raised");
        }
        creditRequest = creditRequestRepository.getOne(id);
        creditRequest.setStatus(Status.APPROVED);
        creditRequest.setBankReference(bankRef);
        creditRequest.setApprovedBy(AppUtility.getCurrentUserName());
        creditRequest.setApprovalDate(new Date());
        CreditRequest c=creditRequestRepository.save(creditRequest);
        boolean isValid=Double.parseDouble(c.getAmount())>=Double.parseDouble(amountThres);
        if(isValid){
            sendMailNotification(c);
            logger.info("Send notification successfully===>");
        }

        logger.info("--->  Approved , calling credit wallet service ");
        String tranID = Utility.generateID();
        this.debitWallet(tranID,magtiponWallet.getValue(), Double.valueOf(creditRequest.getAmount()), "CREDIT REQUEST", "Credit request approved {" + creditRequest.getAgentName() + "}");
        this.creditWallet(tranID,creditRequest.getWalletNumber(), Double.valueOf(creditRequest.getAmount()), "CREDIT REQUEST", "Credit request approved ");
    }

    @Override
    public CreditRequestDTO viewCreditRequest(Long id) {
        CreditRequest creditRequest = creditRequestRepository.getOne(id);
        System.out.println("i got :: " + creditRequest);
        CreditRequestDTO creditRequestDTO = modelMapper.map(creditRequest, CreditRequestDTO.class);
        return creditRequestDTO;
    }

    @Override
    public void declineCreditRequest(Long id,String remark) {
        CreditRequest creditRequest = creditRequestRepository.getOne(id);
        creditRequest.setStatus(Status.DECLINED);
        creditRequest.setRemark(remark);
        creditRequest.setApprovedBy(AppUtility.getCurrentUserName());
        creditRequestRepository.save(creditRequest) ;
        logger.info("----> Declined >");
    }

    @Override
    public List<CreditRequestDTO> getAllAgentRequest(String agentName){

        List<CreditRequest> pending = creditRequestRepository.findByAgentName(agentName) ;
        List<CreditRequestDTO> response = new ArrayList<>();

        pending.forEach(p -> {
            CreditRequestDTO cc = modelMapper.map(p , CreditRequestDTO.class);
            cc.setRequestDate(DateUtil.formatDateToreadable(p.getRequestDate()));
//            cc.setStat(p.getStatus().name());
        });

        return response ;

    }

    @RequireApproval(code = "WALLET_DISPUTE" , entityType = Dispute.class)
    @Override
    public String raiseDispute(DisputeDto disputeDto) {
        Dispute dispute = modelMapper.map(disputeDto, Dispute.class);

        String walletNumber="";
        String comment=dispute.getComment();



       if(dispute.getAction().equals("REVERSAL")){
           // dispute is reversal
           List<FreedomWalletTransaction> transactions = walletTransactionRepository.findByTranID(String.valueOf(dispute.getTranId()));
           for(int i=0;i<transactions.size();i++) {
               FreedomWalletTransaction transaction = transactions.get(i);

               walletNumber = transaction.getWalletNumber();
               if(!transaction.isReversed()) {

                   if(transaction.getTranType().equals(TranType.CREDIT)) {
                       debitWallet(transaction.getTranID(),transaction.getWalletNumber(), transaction.getAmount(), "DISPUTE", "REVERSAL{" + transaction.getTranID() + "} [" + dispute.getComment() + "]");
                   }

                   if(transaction.getTranType().equals(TranType.DEBIT)){
                       creditWallet(transaction.getTranID(),transaction.getWalletNumber(), transaction.getAmount(), "DISPUTE", "REVERSAL{" + transaction.getTranID() + "} [" + dispute.getComment() + "]");

                   }

                   if(transaction.getTranType().equals(TranType.TRANSFER)){
                       creditWallet(transaction.getTranID(),transaction.getWalletNumber(), transaction.getAmount(), "DISPUTE", "REVERSAL{" + transaction.getTranID() + "} [" + dispute.getComment() + "]");

                   }
                   transaction.setReversed(true);

                   walletTransactionRepository.save(transaction);
                   Transactions tranRecord = transactionRepository.findByTranId(dispute.getTranId());
                   if(tranRecord!=null){
                       tranRecord.setStatusdescription("REVERSED");
                       tranRecord.setStatus((short) 0);
                       transactionRepository.save(tranRecord);
                   }

//                   comment = "Dispute Reversal for: "+transaction.getTranType()+" Tran Id: "+disputeDto.getTranId()+", Amount : "+transaction.getAmount();

               }else {
                   logger.info("reversing transaction that has already been reversed");
                   throw new GravityException("Transaction already Reversed");
               }
           }

       }

       if(dispute.getAction().equals("CREDIT")){
           FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(disputeDto.getWalletNumber());
           walletNumber = wallet.getWalletNumber();
           creditWallet(null,wallet.getWalletNumber() , Double.parseDouble(dispute.getAmount()),"DISPUTE","CREDIT["+dispute.getComment()+"]");

//           comment = "Dispute Reversal for: "+dispute.getAction()+" Tran Id: "+disputeDto.getTranId()+", Amount : "+dispute.getAmount();
       }

       if(dispute.getAction().equals("DEBIT")){
           FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(disputeDto.getWalletNumber());
           walletNumber = wallet.getWalletNumber();
           debitWallet(null,wallet.getWalletNumber() , Double.parseDouble(dispute.getAmount()),"DISPUTE","CREDIT["+dispute.getComment()+"]");
           comment = "Dispute Reversal for: "+dispute.getAction()+" Tran Id: "+disputeDto.getTranId()+", Amount : "+dispute.getAmount();

       }
        dispute.setApprovedBy(AppUtility.getCurrentUserName());
        disputeRepository.save(dispute);



        try{
            //log and update as resolved
            Agents agent = agentService.getAgentByWalletNumber(walletNumber);
            NotificationDTO notification = new NotificationDTO();
            notification.setMessage(comment);
            notification.setAgentEmail(agent.getEmail());
            notification.setAgentName(agent.getUsername());
            notification.setAgentPhone(agent.getPhoneNumber());
            notification.setCategory("System Notification");
            notification.setMessageType("Notification");
            notificationService.logNotification(notification);
        }catch(Exception e){
            e.printStackTrace();
        }

        return messageSource.getMessage("dispute.add.success", null, locale);
    }


    public String manualRaiseDispute(DisputeDto disputeDto) {
        Dispute dispute = modelMapper.map(disputeDto, Dispute.class);


        if(dispute.getAction().equals("REVERSAL")){
            // dispute is reversal
//            List<FreedomWalletTransaction> transactions = walletTransactionRepository.
//                    findByTranIDAndReversedAndWalletNumber(String.valueOf(dispute.getTranId()),false,dispute.getWalletNumber());
             List<FreedomWalletTransaction> transactions = walletTransactionRepository.
                    findByTranID(String.valueOf(dispute.getTranId()));
            if(transactions.size() == 0){
                //dont flag this as successfull pending guys would be removed by system
                logger.info("Wallet Transaction not Found for :"+dispute.getTranId());
                throw new GravityException("Wallet Transaction not Found for :"+dispute.getTranId());
            }
            for(int a=0;a<transactions.size();a++){
                FreedomWalletTransaction transaction = transactions.get(a);

                if(!transaction.isReversed()) {

                    if(transaction.getTranType().equals(TranType.DEBIT)){
                        creditWallet(transaction.getTranID(),transaction.getWalletNumber(), transaction.getAmount(), "DISPUTE", "REVERSAL{" + transaction.getTranID() + "} [" + dispute.getComment() + "]");
                    }

                    if(transaction.getTranType().equals(TranType.CREDIT)) {
                        debitWallet(transaction.getTranID(),transaction.getWalletNumber(), transaction.getAmount(), "DISPUTE", "REVERSAL{" + transaction.getTranID() + "} [" + dispute.getComment() + "]");
                    }

                    if(transaction.getTranType().equals(TranType.TRANSFER)){
                        creditWallet(transaction.getTranID(),transaction.getWalletNumber(), transaction.getAmount(), "DISPUTE", "REVERSAL{" + transaction.getTranID() + "} [" + dispute.getComment() + "]");
                    }

                    transaction.setReversed(true);

                    walletTransactionRepository.save(transaction);
                } else {

                    logger.info("reversing transaction that has already been reversed");
//                    throw new GravityException("Transaction has already been reversed");
                }
            }

        }

        if(dispute.getAction().equals("CREDIT")){
            FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(disputeDto.getWalletNumber());
            creditWallet(null,wallet.getWalletNumber() , Double.parseDouble(dispute.getAmount()),"DISPUTE","CREDIT["+dispute.getComment()+"]");
        }


        dispute.setApprovedBy(AppUtility.getCurrentUserName());
        disputeRepository.save(dispute);
        return messageSource.getMessage("dispute.add.success", null, locale);
    }

    @Override
    public Boolean validateWalletTransactionAmount(String walletNumber, BigDecimal amount) {

        FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(walletNumber);

        boolean isSufficient = false;
        if(wallet==null){
            throw new WalletException("");
        }else{
            BigDecimal walletBal = BigDecimal.valueOf(wallet.getAvailableBalance());
            if(walletBal.compareTo(amount) >= 0){
                isSufficient = true;
            }
        }

        return isSufficient;
    }

    @Override
    public void normalizeWalletTransactionData() {
        //walletDisputeRepository should focus only on transactions for day 25th

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Date from = null;
        Date endDate = null;
        try {
            from = sdf.parse("28-12-2019");
            endDate =  sdf.parse("26-07-2019");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("Date is :: "+from+ " and "+endDate);
        List<FreedomWalletTransaction> transactionDisputes = new ArrayList<>();// walletTransactionRepository.findByAgentsAndTranDateBetween(from,endDate);
        System.out.println("size is :: "+transactionDisputes.size());

        for(int i=0;i<transactionDisputes.size();i++){

            FreedomWalletTransaction  dispute =  transactionDisputes.get(i);
            String rrn = dispute.getRemark().replaceAll("[^\\d.]", "");
            if( dispute.getRemark().equals("reversal") ){
                dispute.setDelFlag("Y");
                dispute.setDeletedOn(new Date());
                walletTransactionRepository.save(dispute);
                System.out.println("i deleted record");
            }else if(dispute.getRemark().contains("WITHDRAWAL RRN") ){
                List<NIBBSReconcilation> reconcilations = nibssRecRepo.findByRemark(rrn);
                if(reconcilations.size() == 0 ){
                    dispute.setDelFlag("Y");
                    dispute.setDeletedOn(new Date());
                    walletTransactionRepository.save(dispute);
                    System.out.println("i deleted record");
                }else{
                    System.out.println("am actually fine");
                }
            }else{
                System.out.println("Not a withdrawal transaction");
            }

        }
    }

    @Override
    public void generateBalances(String walletNums,String fromDate,String todate) {
        Date from = null;
        Date endDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            from = sdf.parse(fromDate);
            endDate =  sdf.parse(todate);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        List<AgentsWalletReconcilation>  walletList = agentsWalletReconcilation.findAll();


        for(int v=0;v<walletList.size();v++){

            String walletNum = walletList.get(v).getWalletNumber();

            //Transactions for that day
            List<FreedomWalletTransaction> transactionDisputes = walletTransactionRepository.findByTranDateBetweenAndWalletNumberOrderByTranDate(from, endDate,walletNum.trim());
            System.out.println("for each wallet :: "+transactionDisputes.size());

            //get previous balance
            List<FreedomWalletTransaction> transactions = walletTransactionRepository.
                    findByWalletNumberAndTranDateLessThanOrderByTranDateDesc(walletNum,from);
            Double prevBalance = 0.00;
            if(transactions.size() ==0 ){
                prevBalance = transactionDisputes.get(0).getBalanceBefore();
            }else{
                prevBalance = transactions.get(0).getBalanceAfter();
            }

            System.out.println("Balance before for :: "+walletNum+" is "+prevBalance+" record :: "+v);

            for(int i=0;i<transactionDisputes.size();i++){
                FreedomWalletTransaction freedomWalletTransaction = transactionDisputes.get(i);
                if(freedomWalletTransaction.getDelFlag() ==null || !freedomWalletTransaction.getDelFlag().equals("Y")){
                    if(freedomWalletTransaction.getTranType().equals(TranType.DEBIT)
                            || freedomWalletTransaction.getTranType().equals(TranType.TRANSFER)){
                        freedomWalletTransaction.setBalanceBefore(prevBalance);
                        freedomWalletTransaction.setBalanceAfter(prevBalance - freedomWalletTransaction.getAmount());
                        prevBalance = freedomWalletTransaction.getBalanceAfter();
                    }else{
                        freedomWalletTransaction.setBalanceBefore(prevBalance);
                        freedomWalletTransaction.setBalanceAfter(prevBalance + freedomWalletTransaction.getAmount());
                        prevBalance = freedomWalletTransaction.getBalanceAfter();
                    }
                }else{
                    System.out.println("updated to :: prevBalance "+prevBalance);
                    freedomWalletTransaction.setBalanceBefore(prevBalance);
                    freedomWalletTransaction.setBalanceAfter(prevBalance);
                }

//                FreedomWallet wallet = freedomWalletRepository.findByWalletNumber(freedomWalletTransaction.getWalletNumber());
//                wallet.setAvailableBalance(freedomWalletTransaction.getBalanceAfter());
//                freedomWalletRepository.save(wallet);

                walletTransactionRepository.save(freedomWalletTransaction);
            }

        }

    }






}
