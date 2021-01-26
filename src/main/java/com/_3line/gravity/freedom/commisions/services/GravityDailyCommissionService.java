package com._3line.gravity.freedom.commisions.services;

import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.bankdetails.model.TransactionType;
import com._3line.gravity.freedom.billpayment.models.BillOption;
import com._3line.gravity.freedom.commisions.dto.GravityDailyCommissionDTO;
import com._3line.gravity.freedom.commisions.models.AgentTotalCommission;
import com._3line.gravity.freedom.commisions.models.BankTotalCommission;
import com._3line.gravity.freedom.commisions.models.GravityDailyTotalCommission;
import com._3line.gravity.freedom.commisions.models._3lineTotalCommission;
import com._3line.gravity.freedom.institution.dto.InstitutionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface GravityDailyCommissionService {

    String generateDepositCommission(String bankCode, String amount,Double fee, Long tranId, Agents agent, TransactionType transactionType);

    String generateInstDepositCommission(String bankCode, String amount,Double fee, Long tranId, Agents agent, TransactionType transactionType,InstitutionDTO institutionDTO);


    String generateItexCommission(String bankCode, String amount, Long tranId, Agents agent, TransactionType transactionType);



     void generateThriftCommission(Double amount, Agents agent, TransactionType transactionType);

     void generateBillPaymentCommission(Double amount, Double fee, Agents agent, boolean isRecharge, String remark);

     void generateInstBillPaymentCommission(Double amount, Double fee, Agents agent, boolean isRecharge, String remark,InstitutionDTO institutionDTO);

    Page<GravityDailyCommissionDTO> getDailyCommissions(Pageable pageable);

    GravityDailyTotalCommission getTodaysTotalUnpaidCommission();

    GravityDailyTotalCommission computeTodaysTotalCommission();

    List<AgentTotalCommission> extractAgentsTodaysTotalCommissions();

    List<BankTotalCommission> extractBanksTodaysTotalCommissions();

    _3lineTotalCommission extract3lineTodaysTotalCommission();


    Map<String,String> getBillAmountWithCharge(Double amount, BillOption billOption, InstitutionDTO institutionDTO);

    Map<String, String> getInstBillAmountWithCharge(Double amount, BillOption billOption, InstitutionDTO institutionDTO);



    //    @Scheduled(cron = "${cron.commission.report}")
    void generateDailyReport();


}
