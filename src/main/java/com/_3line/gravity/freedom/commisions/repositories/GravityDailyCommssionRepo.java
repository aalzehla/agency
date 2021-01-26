package com._3line.gravity.freedom.commisions.repositories;

import com._3line.gravity.freedom.bankdetails.model.TransactionType;
import com._3line.gravity.freedom.commisions.models.GravityDailyCommission;
import com._3line.gravity.freedom.commisions.models.GravityDailyTotalCommission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface GravityDailyCommssionRepo extends JpaRepository<GravityDailyCommission, Long> {

    List<GravityDailyCommission> findByTranDateGreaterThanEqualAndTranDateLessThanAndPaid(Date startDate, Date endDate, boolean paid);
    List<GravityDailyCommission> findByTranDateGreaterThanEqualAndTranDateLessThan(Date startDate, Date endDate);
    List<GravityDailyCommission> findByAgentAccountAndTranDateGreaterThanEqualAndTranDateLessThan(String agentAccount, Date startDate, Date endDate);
    GravityDailyCommission findByTransactionId(String transactionId);

    List <GravityDailyCommission> findByTransactionTypeAndAgentBankAndReversedAndTranDateBetween(TransactionType transactionType , String agentBank , String reversed  , Date from , Date to);

    List<GravityDailyCommission> findByAgentNameEqualsAndTranDateBetween(String agentName, Date from, Date to);

    Page<GravityDailyCommission> findByTransactionChannelAndAgentBankAndTranDateBetweenOrderByCreatedOnDesc(String channel,String bankName, Date from, Date to, Pageable pageable);

    Page<GravityDailyCommission> findByTranDateBetweenOrderByCreatedOnDesc(Date from, Date to, Pageable pageable);

    Page<GravityDailyCommission> findByAgentBankAndTranDateBetweenOrderByCreatedOnDesc(String bankName,Date from, Date to, Pageable pageable);

    Page<GravityDailyCommission> findByTransactionChannelAndTranDateBetweenOrderByCreatedOnDesc(String channel,Date from, Date to, Pageable pageable);

    //@MSSQL query todo
        @Query(value = "select sum(cast(agent_commission as float)) as ag_1 , sum(cast(_3line_commission as float)) as ag_2 from gravity_daily_commission", nativeQuery = true)
        List<Object []> totalCommissions();

        @Query(value = "select sum(cast(agent_commission as float)) as ag_1 , sum(cast(_3line_commission as float)) as ag_2 from gravity_daily_commission where tran_date BETWEEN :fromDate AND :toDate", nativeQuery = true)
        List<Object []> getTotalCommissionFordate(@Param("fromDate") String fromDate , @Param("toDate") String toDate);

    //@Mysql query todo
//        @Query(value = "select sum(agent_commission) as ag_1 , sum(_3line_commission) as ag_2 from gravity_daily_commission", nativeQuery = true)
//        List<Object []> totalCommissions();
//
//        @Query(value = "select sum(agent_commission) as ag_1 , sum(_3line_commission) as ag_2 from gravity_daily_commission where tran_date BETWEEN :fromDate AND :toDate", nativeQuery = true)
//        List<Object []> getTotalCommissionFordate(@Param("fromDate") String fromDate , @Param("toDate") String toDate);


}
