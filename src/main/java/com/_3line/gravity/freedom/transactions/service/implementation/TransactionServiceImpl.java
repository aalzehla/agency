package com._3line.gravity.freedom.transactions.service.implementation;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.billpayment.models.BillPayment;
import com._3line.gravity.freedom.billpayment.repository.BillPaymentRepo;
import com._3line.gravity.freedom.financialInstitutions.accountopening.model.AccountOpening;
import com._3line.gravity.freedom.financialInstitutions.accountopening.repository.AccountOpeningRepository;
import com._3line.gravity.freedom.financialInstitutions.wemaapi.utils.DateFormatter;
import com._3line.gravity.freedom.issuelog.models.IssueLog;
import com._3line.gravity.freedom.issuelog.models.IssueStatus;
import com._3line.gravity.freedom.transactions.dtos.TransactionsDto;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.transactions.service.TransactionService;
import com._3line.gravity.freedom.utility.DateUtil;
import com._3line.gravity.freedom.utility.Utility;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.*;


@Repository
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountOpeningRepository accountOpeningRepository;
    @Autowired
    private BillPaymentRepo billPaymentRepo;

    private EntityManager em;

    private DateFormatter simpleDateFormatter = new DateFormatter();

    @Autowired
    EntityManager entityManager;

    @Autowired
    ModelMapper  modelMapper;


    @Autowired
    public TransactionServiceImpl(EntityManager em) {
     this.em = em ;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Transactions getTransaction(Long id) {
        Transactions transactions = transactionRepository.findByTranId((id));
        return  transactions;
    }

    @Override
    public Page<Transactions> getTransactions(String agentName,String status , String from , String to , Pageable pageable) throws GravityException {
        Date startDate = new Date() ;
        Date endDate = new Date();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transactions> q = cb.createQuery(Transactions.class);
        Root<Transactions> c = q.from(Transactions.class);
        List<Predicate> predicates = new ArrayList<>();

        try {
            if (agentName != null && !agentName.equals("")) {
                predicates.add((cb.equal(c.get("agentName"), agentName)) );
            }

            if (status != null && !status.equals("")) {
                predicates.add((cb.equal(c.get("statusdescription"), status)) );
            }

            if (endDate!=null && startDate != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                startDate = dateFormat.parse(from);
                endDate = dateFormat.parse(to);
                predicates.add(cb.between(c.get("tranDate"), startDate, endDate) );
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new PageImpl<>(new ArrayList<>());
        }

        CriteriaQuery<Transactions> baseQuery = null;
        CriteriaQuery<Long> qc = cb.createQuery(Long.class).orderBy();
        CriteriaQuery<Long> countQuery = null;
        if (predicates.size() > 0) {
            Predicate and = cb.and(predicates.toArray(new Predicate[0]));
            baseQuery = q.select(c).where(and).orderBy(cb.desc(c.get("tranId")));
            countQuery = qc.select(cb.count(qc.from(Transactions.class))).where(and);
        } else {
            baseQuery = q.select(c);
            countQuery = qc.select(cb.count(qc.from(Transactions.class)));
        }

        TypedQuery<Transactions> query = entityManager.createQuery(baseQuery);
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Transactions> list = query.getResultList();
        return new PageImpl<>(list, pageable, count);

    }

    @Override
    public Page<Transactions> getWithdrawals(String agentName , String terminalId , String maskedPan ,String status , String from , String to , Pageable pageable) throws GravityException {

        Date startDate = new Date() ;
        Date endDate = new Date();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transactions> q = cb.createQuery(Transactions.class);
        Root<Transactions> c = q.from(Transactions.class);
        List<Predicate> predicates = new ArrayList<>();

        try {

            predicates.add((cb.equal(c.get("transactionType"), "Withdrawal")) );

            if (agentName != null && !agentName.equals("")) {
                predicates.add((cb.equal(c.get("agentName"), agentName)) );
            }

            if (terminalId != null && !terminalId.equals("")) {
                predicates.add((cb.equal(c.get("terminalId"), terminalId)) );
            }

            if (status != null && !status.equals("")) {
                predicates.add((cb.equal(c.get("statusdescription"), status)) );
            }

            if (endDate!=null && startDate != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                endDate = dateFormat.parse(to);
                startDate = dateFormat.parse(from);
                predicates.add(cb.between(c.get("tranDate"), startDate, endDate) );
            }

            if (maskedPan != null && !maskedPan.equals("")) {
                predicates.add((cb.equal(c.get("maskedPan"), maskedPan)) );
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new PageImpl<>(new ArrayList<>());
        }

        CriteriaQuery<Transactions> baseQuery = null;
        CriteriaQuery<Long> qc = cb.createQuery(Long.class).orderBy();
        CriteriaQuery<Long> countQuery = null;
        if (predicates.size() > 0) {
            Predicate and = cb.and(predicates.toArray(new Predicate[0]));
            baseQuery = q.select(c).where(and).orderBy(cb.desc(c.get("tranId")));
            countQuery = qc.select(cb.count(qc.from(Transactions.class))).where(and);
        } else {
            baseQuery = q.select(c);
            countQuery = qc.select(cb.count(qc.from(Transactions.class)));
        }

        TypedQuery<Transactions> query = entityManager.createQuery(baseQuery);
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Transactions> list = query.getResultList();
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<Transactions> getDeposits(String agentName , String accountNumber, String status ,String from , String to , Pageable pageable) throws GravityException {

        Date startDate = new Date() ;
        Date endDate = new Date();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transactions> q = cb.createQuery(Transactions.class);
        Root<Transactions> c = q.from(Transactions.class);
        List<Predicate> predicates = new ArrayList<>();

        try {

            predicates.add((cb.equal(c.get("transactionType"), "Deposit")) );

            if (agentName != null && !agentName.equals("")) {
                predicates.add((cb.equal(c.get("agentName"), agentName)) );
            }

            if (status != null && !status.equals("")) {
                predicates.add((cb.equal(c.get("statusdescription"), status)) );
            }

            if (endDate!=null && startDate != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                startDate = dateFormat.parse(from);
                endDate = dateFormat.parse(to);
                predicates.add(cb.between(c.get("tranDate"), startDate, endDate) );
            }


            if (accountNumber != null && !accountNumber.equals("")) {
                predicates.add((cb.equal(c.get("accountNumber"), accountNumber)) );
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new PageImpl<>(new ArrayList<>());
        }

        CriteriaQuery<Transactions> baseQuery = null;
        CriteriaQuery<Long> qc = cb.createQuery(Long.class).orderBy();
        CriteriaQuery<Long> countQuery = null;
        if (predicates.size() > 0) {
            Predicate and = cb.and(predicates.toArray(new Predicate[0]));
            baseQuery = q.select(c).where(and).orderBy(cb.desc(c.get("tranId")));
            countQuery = qc.select(cb.count(qc.from(Transactions.class))).where(and);
        } else {
            baseQuery = q.select(c);
            countQuery = qc.select(cb.count(qc.from(Transactions.class)));
        }

        TypedQuery<Transactions> query = entityManager.createQuery(baseQuery);
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Transactions> list = query.getResultList();
        return new PageImpl<>(list, pageable, count);

    }

    @Override
    public Page<Transactions> getTransfers(String tranId, String status , String from , String to , Pageable pageable) throws GravityException {
        Date startDate = new Date() ;
        Date endDate = new Date();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Transactions> q = cb.createQuery(Transactions.class);
        Root<Transactions> c = q.from(Transactions.class);
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> orPredicates = new ArrayList<>();

        try {
            orPredicates.add((cb.equal(c.get("transactionType"), "Wallet Transfer")) );
            orPredicates.add((cb.equal(c.get("transactionType"), "Wallet Disbursement")) );

            if (status != null && !status.equals("")) {
                predicates.add((cb.equal(c.get("statusdescription"), status)) );
            }

            if (tranId != null && !tranId.equals("")) {
                predicates.add((cb.equal(c.get("tranId"), tranId)) );
            }

            if (endDate!=null && startDate != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                startDate = dateFormat.parse(from);
                endDate = dateFormat.parse(to);
                predicates.add(cb.between(c.get("tranDate"), startDate, endDate) );
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new PageImpl<>(new ArrayList<>());
        }

        CriteriaQuery<Transactions> baseQuery = null;
        CriteriaQuery<Long> qc = cb.createQuery(Long.class).orderBy();
        CriteriaQuery<Long> countQuery = null;
        Predicate or = cb.or(orPredicates.toArray(new Predicate[0]));
        if (predicates.size() > 0) {
            Predicate and = cb.and(predicates.toArray(new Predicate[0]));

            baseQuery = q.select(c).where(and).where(or).orderBy(cb.desc(c.get("tranId")));
            countQuery = qc.select(cb.count(qc.from(Transactions.class))).where(and).where(or);
        } else {
            baseQuery = q.select(c).where(or).orderBy(cb.desc(c.get("tranId")));
            countQuery = qc.select(cb.count(qc.from(Transactions.class))).where(or);
        }

        TypedQuery<Transactions> query = entityManager.createQuery(baseQuery);
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Transactions> list = query.getResultList();
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<AccountOpening> getAccountOpening(String agentName, String accountNumber, String phoneNumber, String email, String from, String to, Pageable pageable) throws GravityException {
        Date startDate = new Date() ;
        Date endDate = new Date();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            startDate = dateFormat.parse(from);
            endDate = dateFormat.parse(to);

        }catch (Exception e){

        }
        Page<AccountOpening> accountOpenings = null;
        if( agentName.equals("") && accountNumber.equals("") && phoneNumber.equals("")){
            accountOpenings = accountOpeningRepository.findByCreatedOnBetweenOrderByIdDesc(startDate,DateUtil.AddDays(endDate, 1),pageable);
        }else{
            accountOpenings = accountOpeningRepository.findByAgentNameContainsAndAccountNumberContainsAndPhoneNumberContainsAndEmailContainsAndCreatedOnBetweenOrderByIdDesc(agentName,accountNumber,phoneNumber,email,startDate,DateUtil.AddDays(endDate, 1),pageable);
        }
        return accountOpenings;
    }

    @Override
    public Page<BillPayment> getBillPayment(String agentName, String customer, String from, String to, Pageable pageable) {
        Date startDate = new Date() ;
        Date endDate = new Date();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            startDate = dateFormat.parse(from);
            endDate = dateFormat.parse(to);

        }catch (Exception e){

        }

        return billPaymentRepo.findByAgentNameContainsAndCustomerIdContainsAndRechargeAndCreatedOnBetweenOrderByIdDesc(agentName , customer , BillPayment.Recharge.NO , startDate , DateUtil.AddDays(endDate , 1) , pageable);
    }

    @Override
    public List<String[]> getAllTimeStatsTransaction(String agentName) {
        List<String[]> dailyTransactions = Utility.getStringsFromObjectList(transactionRepository.getAllTimeStats(agentName));
        return dailyTransactions;
    }

    @Override
    public List<String[]> getCurrentStatsTransaction(String agentName) {
        Calendar calendar = Calendar.getInstance();
        Calendar endCal = calendar;
        endCal.add(Calendar.HOUR,24);

        String starDate = simpleDateFormatter.customFormatter.format(calendar.getTime());
        String endDate = simpleDateFormatter.customFormatter.format(endCal.getTime());
        System.out.println("using date :: "+starDate+" and "+endDate);

        List<String[]> currentTransactions = Utility.getStringsFromObjectList(transactionRepository.getCurrentStats(agentName,starDate,endDate));
        return currentTransactions;
    }


    @Override
    public Page<BillPayment> getAirtime(String agentName, String phone, String from, String to, Pageable pageable) {
        Date startDate = new Date() ;
        Date endDate = new Date();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            startDate = dateFormat.parse(from);
            endDate = dateFormat.parse(to);

        }catch (Exception e){

        }

        return billPaymentRepo.findByAgentNameContainsAndCustomerIdContainsAndRechargeAndCreatedOnBetweenOrderByIdDesc(agentName , phone , BillPayment.Recharge.YES , startDate , DateUtil.AddDays(endDate , 1) , pageable);

    }

    @Override
    public Page<Transactions> getTransactionsByDate(String from, String to, Pageable pageDetails) throws GravityException {
        return null;
    }

    @Override
    public Page<Transactions> getTransactionByAgentId(String agentId, Pageable pageDetails) throws GravityException {
        return transactionRepository.findByAgentNameIgnoreCaseOrderByTranDateDesc(agentId,pageDetails);
    }

    @Override
    public TransactionsDto addTransaction(TransactionsDto transactionsDto) {
        Transactions transactions  = modelMapper.map(transactionsDto,Transactions.class);
        transactions = transactionRepository.save(transactions);
        transactionsDto.setTranId(transactions.getTranId());
        return transactionsDto;
    }

    @Override
    public TransactionsDto updateTransaction(TransactionsDto transactionsDto) {
        Transactions transactions = modelMapper.map(transactionsDto,Transactions.class);
        Transactions transactions1 = transactionRepository.findByTranId((transactionsDto.getTranId()));
        transactions.setTranId(transactions1.getTranId());
        transactionRepository.save(transactions);
        return transactionsDto;
    }

    @Override
    public TransactionsDto getTransactionBytranID(Long tranID) {
        TransactionsDto transactionsDto = new TransactionsDto();
        Transactions transactions = transactionRepository.findByTranId(tranID);
        modelMapper.map(transactions,transactionsDto);
        return transactionsDto;
    }

    @Override
    public Transactions getTransactionBytranIdAndAgent(long tranID,long agentId) {
        Transactions transactions = transactionRepository.findByTranIdAndInnitiatorId(tranID,agentId);
        return transactions;
    }




    @Transactional
    Page<Transactions> findUsingPattern(String pattern, String from , String to , Pageable details) {

        Transactions t = new Transactions() ;
        String lpattern = pattern.toLowerCase();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Transactions> q = cb.createQuery(Transactions.class);
        Root<Transactions> c = q.from(Transactions.class);
        Predicate[] predicates = null;
        try {
            predicates = new Predicate[t.getClass().getDeclaredFields().length];
            int cnt = 0;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date startDate = dateFormat.parse(from);
            java.util.Date endDate = dateFormat.parse(to);
            for (String field : t.getDefaultSearchFields()) {
                Predicate predicate = cb.and(cb.like(cb.lower(c.get(field)), "%" + lpattern + "%"), cb.between(c.get("tranDate"), startDate, endDate));
                predicates[cnt] = predicate;
                cnt++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new PageImpl<>(new ArrayList<>());
        }

        CriteriaQuery<Transactions> baseQuery = null;
        CriteriaQuery<Long> qc = cb.createQuery(Long.class);
        CriteriaQuery<Long> countQuery = null;
        if (predicates.length > 0) {
            Predicate or = cb.or(predicates);
            baseQuery = q.select(c).where(or).orderBy(cb.desc(c.get("tranId")));
            countQuery = qc.select(cb.count(qc.from(Transactions.class))).where(or);
        } else {
            baseQuery = q.select(c);
            countQuery = qc.select(cb.count(qc.from(Transactions.class)));
        }

        if(Objects.isNull(baseQuery)){
            logger.info("it is null");
        }

        TypedQuery<Transactions> query = em.createQuery(baseQuery);
        Long count = em.createQuery(countQuery).getSingleResult();
        query.setFirstResult(((int) details.getOffset()));
        query.setMaxResults(details.getPageSize());
        List<Transactions> list = query.getResultList();
        logger.info("returning response");
        return new PageImpl<Transactions>(list, details, count);

    }


    @Transactional
     Page<Transactions> findUsingPatternPerTranType(String pattern, String from , String to, String tranType , Pageable details) {

        Transactions t = new Transactions() ;
        String lpattern = pattern.toLowerCase();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Transactions> q = cb.createQuery(Transactions.class);
        Root<Transactions> c = q.from(Transactions.class);
        Predicate[] predicates = null;
        try {
            predicates = new Predicate[t.getClass().getDeclaredFields().length];
            int cnt = 0;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            java.util.Date startDate = dateFormat.parse(from);
            java.util.Date endDate = dateFormat.parse(to);
            for (String field : t.getDefaultSearchFields()) {
                Predicate predicate = cb.and(cb.like(cb.lower(c.get(field)), "%" + lpattern + "%"),cb.and( cb.equal(cb.lower(c.get("transactionType")),tranType) , cb.between(c.get("tranDate"), startDate, endDate)));
                predicates[cnt] = predicate;
                cnt++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new PageImpl<>(new ArrayList<>());
        }

        CriteriaQuery<Transactions> baseQuery = null;
        CriteriaQuery<Long> qc = cb.createQuery(Long.class).orderBy();
        CriteriaQuery<Long> countQuery = null;
        if (predicates.length > 0) {
            Predicate or = cb.or(predicates);
            baseQuery = q.select(c).where(or).orderBy(cb.desc(c.get("tranId")));
            countQuery = qc.select(cb.count(qc.from(Transactions.class))).where(or);
        } else {
            baseQuery = q.select(c);
            countQuery = qc.select(cb.count(qc.from(Transactions.class)));
        }

        TypedQuery<Transactions> query = em.createQuery(baseQuery);
        Long count = em.createQuery(countQuery).getSingleResult();
        query.setFirstResult(((int) details.getOffset()));
        query.setMaxResults(details.getPageSize());
        List<Transactions> list = query.getResultList();
        logger.info("returning response");
        return new PageImpl<Transactions>(list, details, count);

    }



}
