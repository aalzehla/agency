package com._3line.gravity.freedom.service.settlementreport.service;


import com._3line.gravity.core.utils.DateUtil;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.service.settlementreport.model.SettlementReportDto;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service for generating settlement report
 */
@Service
public class SettlementReportService {

    private static Logger logger = LoggerFactory.getLogger(SettlementReportService.class);

    private TransactionRepository transactionRepository;
    private BankDetailsService bankDetailsService;


    @Autowired
    public SettlementReportService(TransactionRepository transactionRepository, BankDetailsService bankDetailsService) {
        this.transactionRepository = transactionRepository;
        this.bankDetailsService = bankDetailsService;
    }

    /**
     * Method gets Settlement report foe each interated bank using days between params
     * @param fromDate
     * @param toDate
     * @return
     */
    public List<SettlementReportDto> getSettlementReport(Date fromDate , Date toDate){
        List<SettlementReportDto> result = new ArrayList<>();
        List<BankDetails> banks = bankDetailsService.findIntegratedBanks(true);

        // get days object between the given dates , thes would form the list on the report
        List<Date>  daysBetween = DateUtil.getDatesBetween(fromDate ,toDate);
        SettlementReportDto total = new SettlementReportDto() ;
        total.setSettlementDate("Total :");
        total.setTotalAcquirerFee("0.0");
        total.setTransactionCount("0");
        total.setTotalFailed("0");
        total.setTotalSuccessful("0");
        total.setTotalTransactionAmount("0.0");
        total.setExpectedSettlementAmount("0.0");

        for (BankDetails b: banks ) {

            for (Date day: daysBetween) {

                SettlementReportDto dto = getForDate(b , day);
                result.add(dto);
            }
        }


       // total all results

        for (SettlementReportDto d: result) {

            total.setTotalAcquirerFee(Utility.parseAmount(Double.valueOf(total.getTotalAcquirerFee().replace(",", ""))+ Double.valueOf(d.getTotalAcquirerFee().replace(",", ""))));
            total.setTransactionCount(Integer.toString(Integer.valueOf(total.getTransactionCount())+ Integer.valueOf(d.getTransactionCount())));
            total.setTotalFailed(Integer.toString(Integer.valueOf(total.getTotalFailed())+ Integer.valueOf(d.getTotalFailed())));
            total.setTotalSuccessful(Integer.toString(Integer.valueOf(total.getTotalSuccessful())+ Integer.valueOf(d.getTotalSuccessful())));
            total.setTotalTransactionAmount(Utility.parseAmount(Double.valueOf(total.getTotalTransactionAmount().replace(",", ""))+ Double.valueOf(d.getTotalTransactionAmount().replace(",", ""))));
            total.setExpectedSettlementAmount(Utility.parseAmount(Double.valueOf(total.getExpectedSettlementAmount().replace(",", ""))+ Double.valueOf(d.getExpectedSettlementAmount().replace(",", ""))));

        }



        result.add(total);
        return  result ;
    }




    private SettlementReportDto getForDate(BankDetails bank, Date date ){
        // get first time and last time for said date
        Date from  = DateUtil.getStart(date) ;
        Date nextDate = DateUtil.AddDays(date,1);
        Date to  = DateUtil.getStart(nextDate);
        Integer successCount = 0 ;
        Integer failedCount = 0 ;
        Double sussessFullValue = 0.0;

        // get banks transaction for said date
        List<Transactions> recorsdForDate = transactionRepository.findByBankFromAndTransactionTypeAndTranDateBetween(bank.getBankCode(), "Withdrawal" , from , to);


        // create response entity
        SettlementReportDto response = new SettlementReportDto();
        // formate date for excel record
        response.setSettlementDate(DateUtil.formatDateToreadable_(date));
        // set total number of transaction
        response.setTransactionCount(Integer.valueOf(recorsdForDate.size()).toString());
        // set aquirer fee / bank
        response.setAcquirerFee(bank.getAcquirePercentage());

        response.setAcquiringBank(bank.getBankCode());

        for (Transactions t : recorsdForDate) {

            if(t.getStatus() == 1){
                // transaction was successful
                successCount ++ ;
                // add transaction amount
                sussessFullValue = sussessFullValue + t.getAmount() ;
            }else {
                // transaction failed or was reversed
                failedCount ++ ;
            }

        }


        response.setTotalSuccessful(successCount.toString());
        response.setTotalFailed(failedCount.toString());

        // get acquirer fee
        Double totalValuDueBank = (Double.valueOf(bank.getAcquirePercentage()) / 100 ) * sussessFullValue ;
        // set tptal successful value
        response.setTotalTransactionAmount( Utility.parseAmount(sussessFullValue));
        // set total value bank is expecting
        response.setTotalAcquirerFee( Utility.parseAmount(totalValuDueBank));
        // set expected settlement amount
        Double settlementAmount  = sussessFullValue - totalValuDueBank;
        response.setExpectedSettlementAmount(Utility.parseAmount(settlementAmount));

        return response ;
    }
}
