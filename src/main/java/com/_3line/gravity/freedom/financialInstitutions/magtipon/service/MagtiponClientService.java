package com._3line.gravity.freedom.financialInstitutions.magtipon.service;


import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.wallet.dto.MagtiponCreditWalletRequest;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MagtiponClientService {




    Logger logger = LoggerFactory.getLogger(this.getClass()) ;

    @Autowired
    WalletService walletService;

    @Autowired
    TransactionRepository transactionRepository;


    public String creditWallet(MagtiponCreditWalletRequest magtiponCreditWalletRequest, Agents agent){

        Transactions transactions = new Transactions();
        transactions.setTranDate(new Date());
        transactions.setStatus((short) 0);
        transactions.setStatusdescription("PENDING");
        transactions.setInnitiatorId(agent.getId());
        transactions.setApproval(0);
        transactions.setAmount(magtiponCreditWalletRequest.getAmount());
        transactions.setStan(null);
        transactions.setMaskedPan(null);
        transactions.setTerminalId(agent.getTerminalId());
        transactions.setTransactionType("Wallet Funding");
        transactions.setTransactionTypeDescription("Wallet Funding (Magtipon)");
        transactions.setDescription("Wallet Funding (Card)");

        transactions.setBankTo(agent.getBankCode());
        transactions.setAgentName(agent.getUsername());
        transactions.setMedia("");
        transactions = transactionRepository.save(transactions);



        try{
            walletService.creditWallet(String.valueOf(transactions.getTranId()),agent.getWalletNumber(),magtiponCreditWalletRequest.getAmount(),"Magtipon","Fund Wallet :"+agent.getWalletNumber());
            transactions.setStatus((short) 1);
            transactions.setStatusdescription("SUCCESSFUL");
            transactionRepository.save(transactions);
            return String.valueOf(transactions.getTranId());
        }catch(Exception e){
            transactions.setStatusdescription("FAILED");
            transactions.setStatus((short) 2);
            transactionRepository.save(transactions);
            throw new GravityException(e.getMessage());
        }
    }


}
