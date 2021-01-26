package com._3line.gravity.freedom.financialInstitutions.wemaapi.service;



import com._3line.gravity.freedom.financialInstitutions.wemaapi.model.*;

import java.util.List;

/**
 * @author JoyU
 * @date 9/17/2018
 */

public interface WemaApiService {

    /**
     * This methhod is used to encrypt messages
     * @param message
     * @return string
     */
    String encryptMessage(String message);

    /**
     *This methhod is used to decrypt messages
     * @param message
     * @return string
     */
    String decryptMessage(String message);

    /**
     *This method is used for creating a new Wema Bank Account
     * @param accOpeningWema
     * @return AccOpeningWema
     */
    AccOpeningWema openAccount(AccOpeningWema accOpeningWema);

    /**
     *This methhod is used to get the list os branches
     * @return list of branches
     */
    List<String> getBranchList();

    List<BasicDetailsAPI> branchList(BasicDetailsAPI basicDetailsAPI);

    /**
     *The Account number is used to get the account name
     * @param accNameEnquiryAPI
     * @return AccNameEnquiryAPI
     */
    AccNameEnquiryAPI getAccountName(AccNameEnquiryAPI accNameEnquiryAPI);

    /**
     *This method transfer money from one account to another through NIP
     * @param nipFundsTransferAPI
     * @return NipFundsTransferAPI
     */
    NipFundsTransferAPI transferFund(NipFundsTransferAPI nipFundsTransferAPI);

    /**
     *This method help to check the status of any transaction
     * @param basicDetailsAPI
     * @param tranRefStatus
     * @return BasicDetailsAPI
     */
    BasicDetailsAPI getTransactionStatus(BasicDetailsAPI basicDetailsAPI, String tranRefStatus);

    /**
     *This method is used to deposit money into Wema Bank
     * @param depositAndWithdrawal
     * @return DepositAndWithdrawal
     */
    DepositAndWithdrawal makeDeposit(DepositAndWithdrawal depositAndWithdrawal);

    /**
     *This method helps Wema Customers Withdraw their money
     * @param depositAndWithdrawal
     * @param password
     * @param token
     * @return DepositAndWithdrawal
     */
    DepositAndWithdrawal makeWithdrawal(DepositAndWithdrawal depositAndWithdrawal, String password, String token);

    /**
     * This method helps to get token for withdrawal
     * @param depositAndWithdrawal
     * @return DepositAndWithdrawal
     */
    DepositAndWithdrawal getWithdrawalToken(DepositAndWithdrawal depositAndWithdrawal);

}
