package com._3line.gravity.freedom.billpayment.repository;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.freedom.billpayment.models.BillPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BillPaymentRepo extends AppCommonRepository<BillPayment, Long> {

    Page<BillPayment> findAllByOrderByIdDesc(Pageable pageable);

    BillPayment findByTransactionId(String transactionId);


    //mssql version
    @Query(value = "select top 5 b.payment_code , o.option_name , count(*) as cnt from bill_payment b , bill_option o  where b.payment_code = o.code  " +
            " group by b.payment_code, o.option_name  order by cnt desc " , nativeQuery = true)
    List<Object [] > topBillers();

    //Mysql Version
//    @Query(value = "select b.payment_code , o.option_name , count(*) as cnt from bill_payment b , bill_option o  where b.payment_code = o.code  " +
//            " group by b.payment_code order by cnt desc limit 5" , nativeQuery = true)
//    List<Object [] > topBillers();

    
    Page<BillPayment> findByAgentNameContainsAndCustomerIdContainsAndRechargeAndCreatedOnBetweenOrderByIdDesc
            (String agentName , String customerId , BillPayment.Recharge recharge , Date from , Date to , Pageable pageable );
}
