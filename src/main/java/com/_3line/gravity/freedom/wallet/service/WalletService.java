package com._3line.gravity.freedom.wallet.service;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.dispute.dtos.DisputeDto;
import com._3line.gravity.freedom.dispute.models.Dispute;
import com._3line.gravity.freedom.wallet.dto.*;
import com._3line.gravity.freedom.wallet.exceptions.WalletException;
import com._3line.gravity.freedom.wallet.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface WalletService {


    /**
     * Create wallet with @wallerNumber for @purpose
     *
     * @param walletNumber
     * @param purpose
     * @return
     */
    String createWallet(String walletNumber, String purpose) throws WalletException;

    /**
     * Methods credits walletNumber with stated @amount , @channel specifies the channel the transaction is been
     * consummated from , @remark could be a narration is specified
     *
     * @param walletNumber
     * @param amount
     * @param channel
     * @param remark
     * @throws WalletException when @wallerNumber not found
     */
    void creditWallet(String tranId,String walletNumber, double amount, String channel, String remark) throws WalletException;

    /**
     * Method debits  walletNumber with stated @amount , @channel specifies the channel the transaction is been
     * consummated from , @remark could be a narration is specified
     *
     * @param walletNumber
     * @param amount
     * @param channel
     * @param remark
     * @throws WalletException when @wallerNumber not found and @amount is greater than wallet available balance
     */

    void debitWallet(String tranId,String walletNumber, double amount, String channel, String remark) throws  WalletException;

    /**
     * Method transfers @amount from @wallerNumber to @destinationWalletNumber , @channel specifies the channel the transaction is been
     * consummated from , @remark could be a narration is specified
     *
     * @param walletNumber
     * @param destinationWalletNumber
     * @param amount
     * @param channel
     * @param remark
     * @throws WalletException when @wallerNumber not found , @amount is greater than wallet available balance and when @destinationWalletNumber not found
     */
    void transferWalletFunds(String tranId,String walletNumber, String destinationWalletNumber, double amount, String channel, String remark) throws  WalletException;


    /**
     * Method transfers @amount from @wallerNumber to @destinationAccount , @channel specifies the channel the transaction is been
     * consummated from , @remark could be a narration is specified
     *
     * @param walletNumber
     * @param destinationAccount
     * @param amount
     * @param channel
     * @param remark
     * @throws WalletException when @wallerNumber not found , @amount is greater than wallet available balance and when @destinationWalletNumber not found
     */
    void transferWalletFundToBank(String tranId,String walletNumber, String destinationAccount, String destinationBank, double amount, String channel, String remark) throws  WalletException;



    /**
     * Get wallet information for specified @wallerNumber
     * @param walletNumber
     * @return empty walletdto if @wallerNumber is not found
     */
    WalletDTO getWalletByNumber(String walletNumber);

    FreedomWallet getWalletObjByNumber(String walletNumber);

    /**
     * Fetch all wallet on the system
     * @return
     */
    List<WalletDTO> getAllWallets();


    /**
     * Fetch all transactions doneon a wallet
     * @param walletNumber
     * @return
     */
    List<TransactionDTO> getWalletTransactions(String walletNumber);


    List<FreedomWalletTransaction> getWalletTransactionsByTranId(String tranId);

    /**
     * @param walletNumber
     * @param pageable
     * @return List<FreedomWalletTransaction>
     */
    Page<FreedomWalletTransaction> getWalletTransactionsWithPagination(String walletNumber,Pageable pageable);


    Page<FreedomWalletTransaction> getWalletTransactionsWithDatePagination(String walletNumber,Date from,Date to,Pageable pageable);




    /**
     * Fetch a;; transaction done on a wallet between @from and @to
     * @param walletNumber
     * @param from
     * @param to
     * @return
     */
    List<FreedomWalletTransaction> getWalletTransactionsByDate(String walletNumber, Date from, Date to);


    List<FreedomWalletTransaction> getWalletTransactionsByDateAndTranType(String walletNumber, TranType tranType , Date from, Date to);



    Page<FreedomWalletTransaction> findAll(String pattern,String from , String to , Pageable pageDetails);


    /**
     * Create a credit request to a wallet
     * @param creditRequestDTO
     */
    void createCreditRequest(CreditRequestDTO creditRequestDTO) throws GravityException;

    Page<CreditRequest> findAllPending(Pageable pageable);

    Page<CreditRequest> findAllRequestHistory(Pageable pageable);

    Page<FreedomWallet> gLAccounts(Pageable pageable);

    Page<Dispute> getAllDispute(String agentName,String from,String to,String tranType,Pageable pageable);


    List<CreditRequest> getFilteredCreditRequest(String agentName,String from,String to,String status);

    /**
     * Approve credit wallet request , and fund wallet with specified amount
     * @param id
     */
    @PreAuthorize("hasAuthority('MANAGE_WALLETS_CREDIT_REQUEST')")
    void approveCreditRequest(Long id,String bankRef) ;

    @PreAuthorize("hasAuthority('MANAGE_WALLETS_CREDIT_REQUEST')")
    CreditRequestDTO viewCreditRequest(Long id) ;

    /**
     * Decline credit wallet request
     * @param id
     */
    void declineCreditRequest(Long id,String remark);

    /**
     * Get all credit request created by an agent
     * @param agentName
     * @return
     */
    List<CreditRequestDTO> getAllAgentRequest(String agentName) ;

    /**
     *  Raise dispute for agent against transaction or credit wallet amount or debit wallet
     * @param disputeDto
     * @return
     */

    String raiseDispute(DisputeDto disputeDto);


    String manualRaiseDispute(DisputeDto disputeDto);

    Boolean validateWalletTransactionAmount(String walletNumber, BigDecimal amount);

    void normalizeWalletTransactionData();

    void generateBalances(String walletNum,String fromDate,String toDate);

}
