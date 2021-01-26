package com._3line.gravity.freedom.gravitymobile.service;

import com._3line.gravity.api.transaction.dto.TransactionHistoryDTO;
import com._3line.gravity.api.utility.GeneralUtil;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.transactions.dtos.TransactionHistoryRespDTO;
import com._3line.gravity.freedom.transactions.dtos.TransactionListDto;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.utility.DateUtil;
import com._3line.gravity.freedom.wallet.service.WalletService;
//import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {


    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    WalletService walletService;

    @Autowired
    GeneralUtil generalUtil;

    public TransactionListDto getAgentsTransactionHistory(TransactionHistoryDTO transactionHistoryReqDTO){

        TransactionListDto transactionListDto= new TransactionListDto();

        List<TransactionHistoryRespDTO> historyRespDTOS;
        Page<Transactions> pagedTrans = null;

        Pageable paged = null;
        if(transactionHistoryReqDTO.getSize()>0 &&  transactionHistoryReqDTO.getPage() >= 0){
            paged = PageRequest.of(transactionHistoryReqDTO.getPage(), transactionHistoryReqDTO.getSize());
        }



        if(transactionHistoryReqDTO.getFromDate()!=null && !transactionHistoryReqDTO.getFromDate().equals("")
                && transactionHistoryReqDTO.getToDate()!=null && !transactionHistoryReqDTO.getToDate().equals("")){

            Date fromDate = DateUtil.dateFullFormat(transactionHistoryReqDTO.getFromDate());
            Date toDate = DateUtil.dateFullFormat(transactionHistoryReqDTO.getToDate());
            toDate = DateUtils.addDays(toDate,1);

            if(paged==null){
                List<Transactions> transactions = transactionRepository.findByAgentNameIgnoreCaseAndTranDateBetweenOrderByTranDateDesc(transactionHistoryReqDTO.getAgentName(),fromDate,toDate);
                historyRespDTOS = generalUtil.converttodto(transactions);
            }else{
                pagedTrans = transactionRepository.findByAgentNameIgnoreCaseAndTranDateBetweenOrderByTranDateDesc(transactionHistoryReqDTO.getAgentName(),fromDate,toDate,paged);
                historyRespDTOS = generalUtil.converttodto(pagedTrans.getContent());
            }
        }else {
            if(paged==null){
                List<Transactions>  transactions = transactionRepository.findByAgentNameIgnoreCaseOrderByTranDateDesc(transactionHistoryReqDTO.getAgentName());
                historyRespDTOS = generalUtil.converttodto(transactions);
            }else{
                pagedTrans = transactionRepository.findByAgentNameIgnoreCaseOrderByTranDateDesc(transactionHistoryReqDTO.getAgentName(),paged);
                historyRespDTOS = generalUtil.converttodto(pagedTrans.getContent());
            }

        }



        if(pagedTrans!=null && historyRespDTOS.size()>0){
            transactionListDto.setHasNextRecord(pagedTrans.hasNext());
            transactionListDto.setTotalCount((int) pagedTrans.getTotalElements());
        }else{
            transactionListDto.setHasNextRecord(false);
            transactionListDto.setTotalCount(historyRespDTOS.size());
        }

        transactionListDto.setTransactions(historyRespDTOS);



        return transactionListDto;
    }

    public Transactions getAgentsTransaction(String tranId){

        Transactions transaction = transactionRepository.findByTranId(Long.valueOf(tranId));
        if(transaction==null){
            throw new GravityException("Record Not Found");
        }

        return transaction;
    }

    public Transactions updateTransaction(Transactions transactions){

        Transactions transaction = transactionRepository.save(transactions);

        return transaction;
    }



    public Response getSendEmail(TransactionHistoryDTO transactionHistoryReqDTO){
        return  null ;
    }
}
