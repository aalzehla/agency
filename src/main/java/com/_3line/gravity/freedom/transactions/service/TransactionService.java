package com._3line.gravity.freedom.transactions.service;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.billpayment.models.BillPayment;
import com._3line.gravity.freedom.financialInstitutions.accountopening.model.AccountOpening;
import com._3line.gravity.freedom.transactions.dtos.TransactionsDto;
import com._3line.gravity.freedom.transactions.models.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface TransactionService {

    Transactions getTransaction(Long id);

    Page<Transactions> getTransactions(String agentName, String status ,String from , String to , Pageable pageDetails) throws GravityException;

    Page<Transactions> getWithdrawals(String agentName , String terminalId , String maskedPan , String status ,String from , String to , Pageable pageDetails) throws GravityException;

    Page<Transactions> getDeposits(String agentName , String accountNumber ,String status , String from , String to , Pageable pageDetails) throws GravityException;

    Page<Transactions> getTransfers(String pattern, String status , String from , String to , Pageable pageDetails) throws GravityException;

    Page<AccountOpening> getAccountOpening(String agentName, String accountNumber , String phoneNumber , String email , String from , String to , Pageable pageable) throws  GravityException ;

    Page<BillPayment> getBillPayment(String agentName , String customer , String from , String to , Pageable pageable);


    List<String[]> getAllTimeStatsTransaction(String agentName);

    List<String[]> getCurrentStatsTransaction(String agentName);

    Page<BillPayment> getAirtime(String agentName , String phone , String from , String to , Pageable pageable);

    Page<Transactions> getTransactionsByDate(String from , String to , Pageable pageDetails) throws GravityException;

    Page<Transactions> getTransactionByAgentId(String agentName , Pageable pageDetails) throws GravityException;


    TransactionsDto addTransaction(TransactionsDto transactionsDto);

    TransactionsDto updateTransaction(TransactionsDto transactionsDto);

    TransactionsDto getTransactionBytranID(Long tranID);

    Transactions getTransactionBytranIdAndAgent(long tranID,long agentId);
}
