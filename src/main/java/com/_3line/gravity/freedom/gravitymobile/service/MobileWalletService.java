package com._3line.gravity.freedom.gravitymobile.service;


import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.wallet.dto.CreditRequestDTO;

public interface MobileWalletService {

    Response getWalletBalance(String agentName);

    Response getWalletTransactionHistory(String agentName, String from, String to);

    Response getIncomeWalletTransactionHistory(String agentName, String from, String to);

    Response disburseFromWallet(String agentName, String amount);

    Response disburseFromInWallet(String agentName, String amount) ;

    Response transferBetweenWallets(String agentName, String amount, String fromWallet, String toWallet,
                                    String remark,String requestId) ;

    Response creditRequest(CreditRequestDTO creditRequestDTO);

    Response getCreditRequesthistory(String agentName);
}
