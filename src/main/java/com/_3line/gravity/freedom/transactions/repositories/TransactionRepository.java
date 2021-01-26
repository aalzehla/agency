/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.transactions.repositories;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.freedom.billpayment.models.BillPayment;
import com._3line.gravity.freedom.transactions.models.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 *
 * @author NiyiO
 */
public interface TransactionRepository extends JpaRepository<Transactions, Long> {


    Transactions findByTranId(Long tran_id);

    Transactions findByTranIdAndInnitiatorId(Long tranId,
                                             long innitiatorId);

    List<Transactions> findByBankFromAndTransactionTypeAndTranDateBetween(String bankFrom,
                                                                          String transactionType,
                                                                          Date from ,
                                                                          Date to);

    Transactions findByWebpayTranReference(String requestId);

    Page<Transactions>findByAgentNameIgnoreCaseAndTranDateBetweenOrderByTranDateDesc(String agentName, Date from , Date to,Pageable pageable);

    List<Transactions>findByAgentNameIgnoreCaseAndTranDateBetweenOrderByTranDateDesc(String agentName,
                                                                                     Date from ,
                                                                                     Date to);

    Page<Transactions>findByAgentNameIgnoreCaseOrderByTranDateDesc(String agentName,Pageable pageable);

    List<Transactions>findByAgentNameIgnoreCaseOrderByTranDateDesc(String agentName);



    @Query(value = " select a.transaction_type,sum(a.amount) as amount,count(0) as count from transactions a\n" +
            "group by a.transaction_type ",nativeQuery = true)
    List<Object[]> getDashBoardTransactionGroupByType();

    @Query(value = " select a.transaction_type,sum(a.amount) as amount,count(0) as count from transactions a\n" +
            "where tran_date BETWEEN :startDate AND :endDate group by a.transaction_type ",nativeQuery = true)
    List<Object[]> getDashBoardTransactionByDateGroupByType(@Param("startDate") String startDate,
                                                            @Param("endDate")  String endDate);

    @Query(value = "select a.agent_id,a.username,a.terminal_id,\n" +
            "b.s_dep_val,b.s_dep_vol,\n" +
            "c.f_dep_val,c.f_dep_vol,\n" +
            "d.s_with_val,d.s_with_vol,\n" +
            "e.f_with_val,e.f_with_vol,\n" +
            "f.s_bill_val,f.s_bill_vol,\n" +

            "g.f_bill_val,g.f_bill_vol,\n" +
            "h.s_acct_vol,\n" +
            "i.s_rechg_val,i.s_rechg_vol,\n" +
            "j.f_rechg_val,j.f_rechg_vol\n" +
            "from agents a \n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as s_dep_val ,count(0) as s_dep_vol from transactions \n" +
            "where transaction_type = 'Deposit' and \n" +
            "(statusdescription = 'SUCCESSFUL' or statusdescription = 'PENDING')\n" +
            "and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "group by agent_name,transaction_type\n" +
            ") as b\n" +
            "ON a.username = b.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as f_dep_val ,count(0) as f_dep_vol from transactions \n" +
            "where transaction_type = 'Deposit' and \n" +
            "(statusdescription = 'FAILED' or statusdescription = 'REVERSED')\n" +
            "and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "group by agent_name,transaction_type\n" +
            ") as c\n" +
            "ON a.username = c.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as s_with_val,count(0) as s_with_vol from transactions \n" +
            "where transaction_type = 'Withdrawal' and \n" +
            "(statusdescription = 'SUCCESSFUL' or statusdescription = 'PENDING')\n" +
            "and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "group by agent_name,transaction_type\n" +
            ") as d\n" +
            "ON a.username = d.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as f_with_val,count(0) as f_with_vol from transactions \n" +
            "where transaction_type = 'Withdrawal' and \n" +
            "(statusdescription = 'FAILED' or statusdescription = 'REVERSED')\n" +
            "and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "group by agent_name,transaction_type\n" +
            ") as e\n" +
            "ON a.username = e.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as s_bill_val,count(0) as s_bill_vol from transactions \n" +
            "where transaction_type = 'Bill Payment' and \n" +
            "(statusdescription = 'SUCCESSFUL' or statusdescription = 'PENDING')\n" +
            "and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "group by agent_name,transaction_type \n" +
            ") as f\n" +
            "ON a.username = f.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as f_bill_val,count(0) as f_bill_vol from transactions \n" +
            "where transaction_type = 'Bill Payment' and \n" +
            "(statusdescription = 'FAILED' or statusdescription = 'REVERSED')\n" +
            "and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "group by agent_name,transaction_type\n" +
            ") as g\n" +
            "ON a.username = g.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,count(0) as s_acct_vol from account_opening \n" +
            "where (created_on BETWEEN :fromDate AND :endDate  )\n" +
            "group by agent_name\n" +
            ") as h \n" +
            "ON a.username = h.agent_name \n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as s_rechg_val,count(0) as s_rechg_vol from transactions \n" +
            "where transaction_type = 'Recharge' and \n" +
            "(statusdescription = 'SUCCESSFUL' or statusdescription = 'PENDING')\n" +
            "and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "group by agent_name,transaction_type \n" +
            ") as i \n" +
            "ON a.username = i.agent_name \n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as f_rechg_val,count(0) as f_rechg_vol from transactions \n" +
            "where transaction_type = 'Recharge' and \n" +
            "(statusdescription = 'FAILED' or statusdescription = 'REVERSED')\n" +
            "and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "group by agent_name,transaction_type\n" +
            ") as j \n" +
            "ON a.username = j.agent_name \n" +
            "where a.username in (select a.username from agents a where a.id =:agentId)",nativeQuery = true)
    List<Object[]> getMiniStatement(@Param("fromDate") String fromDate,
                                    @Param("endDate") String endDate,
                                    @Param("agentId") long agentId);


    @Query(value = "select a.username,b.bill_val,b.bill_vol,d.dep_val,d.dep_vol,w.with_val,w.with_vol,r.rech_val,r.rech_vol \n" +
            "from agents a \n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as dep_val ,count(0) as dep_vol from transactions \n" +
            "where transaction_type = 'Deposit' \n" +
            "and status = 1 "+
            "group by agent_name,transaction_type\n" +
            ") as d\n" +
            "ON a.username = d.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as with_val,count(0) as with_vol from transactions\n" +
            "where transaction_type = 'Withdrawal'\n" +
            "and status = 1 "+
            "group by agent_name,transaction_type\n" +
            ") as w\n" +
            "ON a.username = w.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as bill_val,count(0) as bill_vol from transactions \n" +
            "where transaction_type = 'Bill Payment'\n" +
            "and status = 1 "+
            "group by agent_name,transaction_type\n" +
            ") as b\n" +
            "ON a.username = b.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as rech_val,count(0) as rech_vol from transactions \n" +
            "where transaction_type = 'Recharge'\n" +
            "and status = 1 "+
            "group by agent_name,transaction_type\n" +
            ") as r\n" +
            "ON a.username = r.agent_name \n" +
            "where a.username =:agentName ", nativeQuery = true)
    List<Object[]> getAllTimeStats(@Param("agentName") String agentName);


    @Query(value = "SELECT transaction_type , amount, tran_date FROM transactions, "
            + "(SELECT id FROM agents WHERE username =:username)as Agents WHERE "
            + "transaction_type IS NOT NULL AND Agents.id = innitiator_id", nativeQuery = true)
    List<String[]> summary(@Param("username") String username);

    @Query(value = "select count(*) , sum(amount) from transactions where agent_name = :agent and transaction_type = :tranType and status = 1 and tran_date between :from and  :to" , nativeQuery = true)
    List<Object []> agentTransactionReportByTypeAndDate(@Param("agent") String agent ,@Param("tranType") String tranType ,@Param("from") String from ,@Param("to") String to);


    @Query(value = "select a.agent_id,b.bill_val,b.bill_vol,d.dep_val,d.dep_vol,w.with_val,w.with_vol,r.rech_val,r.rech_vol,a.agent_code\n" +
            "from agents a \n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as dep_val ,count(0) as dep_vol from transactions \n" +
            "where transaction_type = 'Deposit'\n" +
            "and (tran_date BETWEEN :fromDate AND :endDate )\n" +
            "and status = 1 "+
            "group by agent_name,transaction_type\n" +
            ") as d\n" +
            "ON a.username = d.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as with_val,count(0) as with_vol from transactions \n" +
            "where transaction_type = 'Withdrawal'\n" +
            "and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "and status = 1 "+
            "group by agent_name,transaction_type\n" +
            ") as w\n" +
            "ON a.username = w.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as bill_val,count(0) as bill_vol from transactions \n" +
            "where transaction_type = 'Bill Payment'\n" +
            "and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "and status = 1 "+
            "group by agent_name,transaction_type\n" +
            ") as b\n" +
            "ON a.username = b.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as rech_val,count(0) as rech_vol from transactions \n" +
            "where transaction_type = 'Recharge'\n" +
            "and (tran_date BETWEEN :fromDate AND :endDate )\n" +
            "and status = 1 "+
            "group by agent_name,transaction_type \n" +
            ") as r\n" +
            "ON a.username = r.agent_name", nativeQuery = true)
    List<Object[]> getAgentTransactionList(@Param("fromDate") String fromDate,
                                           @Param("endDate") String endDate);


    @Query(value = "select a.username,b.bill_val,b.bill_vol,d.dep_val,d.dep_vol,w.with_val,w.with_vol,r.rech_val,r.rech_vol \n" +
            "from agents a \n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as dep_val ,count(0) as dep_vol from transactions \n" +
            "where transaction_type = 'Deposit' and (tran_date BETWEEN :fromDate AND :endDate  ) \n" +
            "and status = 1 "+
            "group by agent_name,transaction_type\n" +
            ") as d\n" +
            "ON a.username = d.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as with_val,count(0) as with_vol from transactions\n" +
            "where transaction_type = 'Withdrawal' and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "and status = 1 "+
            "group by agent_name,transaction_type\n" +
            ") as w\n" +
            "ON a.username = w.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as bill_val,count(0) as bill_vol from transactions \n" +
            "where transaction_type = 'Bill Payment' and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "and status = 1 "+
            "group by agent_name,transaction_type\n" +
            ") as b\n" +
            "ON a.username = b.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as rech_val,count(0) as rech_vol from transactions \n" +
            "where transaction_type = 'Recharge' and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "and status = 1 "+
            "group by agent_name,transaction_type\n" +
            ") as r\n" +
            "ON a.username = r.agent_name \n" +
            "where a.username =:agentName ", nativeQuery = true)
    List<Object[]> getCurrentStats(@Param("agentName") String agentName,
                                   @Param("fromDate") String fromDate,
                                   @Param("endDate") String endDate);


    @Query(value = "select \n" +
            "concat(a.first_name,' ',a.last_name),\n" +
            "a.terminal_id,\n" +
            "(select username from agents where id = a.parent_agent_id) as aggregator,\n" +
            "d.dep_val,\n" +
            "d.dep_vol,\n" +
            "w.with_val,\n" +
            "w.with_vol\n" +
            "from agents a \n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as dep_val ,count(0) as dep_vol from transactions \n" +
            "where transaction_type = 'Deposit'\n" +
            "and (tran_date BETWEEN :fromDate AND :endDate)\n" +
            "and status = 1 "+
            "group by agent_name,transaction_type\n" +
            ") as d\n" +
            "ON a.username = d.agent_name\n" +
            "left JOIN  (\n" +
            "select agent_name,sum(amount) as with_val,count(0) as with_vol from transactions \n" +
            "where transaction_type = 'Withdrawal'\n" +
            "and (tran_date BETWEEN :fromDate AND :endDate)\n" +
            "and status = 1 "+
            "group by agent_name,transaction_type\n" +
            ") as w\n" +
            "ON a.username = w.agent_name", nativeQuery = true)
    List<Object[]> getAgentPerformance(@Param("fromDate") String fromDate,
                                       @Param("endDate") String endDate);


    @Query(value = "select a.agent_id,a.username,a.terminal_id,b.bill_val,b.bill_vol,d.dep_val,d.dep_vol,w.with_val,w.with_vol,r.rech_val,r.rech_vol\n" +
            "            from agents a \n" +
            "            left JOIN  (\n" +
            "            select agent_name,sum(amount) as dep_val ,count(0) as dep_vol from transactions \n" +
            "            where transaction_type = 'Deposit'\n" +
            "            and (tran_date BETWEEN :fromDate AND :endDate )\n" +
            "            and status = 1 "+
            "            group by agent_name,transaction_type\n" +
            "            ) as d\n" +
            "            ON a.username = d.agent_name\n" +
            "            left JOIN  (\n" +
            "            select agent_name,sum(amount) as with_val,count(0) as with_vol from transactions \n" +
            "            where transaction_type = 'Withdrawal'\n" +
            "            and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "            and status = 1 "+
            "            group by agent_name,transaction_type\n" +
            "            ) as w\n" +
            "            ON a.username = w.agent_name\n" +
            "            left JOIN  (\n" +
            "            select agent_name,sum(amount) as bill_val,count(0) as bill_vol from transactions \n" +
            "            where transaction_type = 'Bill Payment'\n" +
            "            and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "            and status = 1 "+
            "            group by agent_name,transaction_type\n" +
            "            ) as b\n" +
            "            ON a.username = b.agent_name\n" +
            "            left JOIN  (\n" +
            "            select agent_name,sum(amount) as rech_val,count(0) as rech_vol from transactions \n" +
            "            where transaction_type = 'Recharge'\n" +
            "            and (tran_date BETWEEN :fromDate AND :endDate )\n" +
            "            and status = 1 "+
            "            group by agent_name,transaction_type \n" +
            "            ) as r\n" +
            "            ON a.username = r.agent_name" +
            "            where a.username \n" +
            " in (select a.username from agents a where a.parent_agent_id =:aggregatorId)", nativeQuery = true)
    List<Object[]> getAggregatorAgentsPerformance(@Param("fromDate") String fromDate,
                                                  @Param("endDate") String endDate,
                                                  @Param("aggregatorId") long aggregatorId);


    @Query(value = "select a.agent_id,a.username,a.terminal_id,b.bill_val,b.bill_vol,d.dep_val,d.dep_vol,w.with_val,w.with_vol,r.rech_val,r.rech_vol\n" +
            "            from agents a \n" +
            "            left JOIN  (\n" +
            "            select agent_name,sum(amount) as dep_val ,count(0) as dep_vol from transactions \n" +
            "            where transaction_type = 'Deposit'\n" +
            "            and (tran_date BETWEEN :fromDate AND :endDate )\n" +
            "            and status = 1 "+
            "            group by agent_name,transaction_type\n" +
            "            ) as d\n" +
            "            ON a.username = d.agent_name\n" +
            "            left JOIN  (\n" +
            "            select agent_name,sum(amount) as with_val,count(0) as with_vol from transactions \n" +
            "            where transaction_type = 'Withdrawal'\n" +
            "            and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "            and status = 1 "+
            "            group by agent_name,transaction_type\n" +
            "            ) as w\n" +
            "            ON a.username = w.agent_name\n" +
            "            left JOIN  (\n" +
            "            select agent_name,sum(amount) as bill_val,count(0) as bill_vol from transactions \n" +
            "            where transaction_type = 'Bill Payment'\n" +
            "            and (tran_date BETWEEN :fromDate AND :endDate  )\n" +
            "            and status = 1 "+
            "            group by agent_name,transaction_type\n" +
            "            ) as b\n" +
            "            ON a.username = b.agent_name\n" +
            "            left JOIN  (\n" +
            "            select agent_name,sum(amount) as rech_val,count(0) as rech_vol from transactions \n" +
            "            where transaction_type = 'Recharge'\n" +
            "            and (tran_date BETWEEN :fromDate AND :endDate )\n" +
            "            and status = 1 "+
            "            group by agent_name,transaction_type \n" +
            "            ) as r\n" +
            "            ON a.username = r.agent_name" +
            "            where a.username \n" +
            " in (select a.username from agents a where a.sub_parent_agent_id =:subAggregatorId)", nativeQuery = true)
    List<Object[]> getSubAggregatorAgentsPerformance(@Param("fromDate") String fromDate,
                                                  @Param("endDate") String endDate,
                                                  @Param("subAggregatorId") long subAggregatorId);

    @Query(value = "select top 10 agent_name , sum(amount) as cnt from  transactions  where status = 1 group by agent_name order by cnt desc", nativeQuery = true)
    List<Object []> topAgents();

    @Query(value="select top 2 t.* from transactions t\n" +
            "where t.itex_tran_id =:itexTranId \n" +
            "and t.terminal_id =:terminalId and t.status = 1 ",nativeQuery = true)
    List<Transactions> findByTerminalIdAndItexTranId( @Param("terminalId") String terminalId,
                                                      @Param("itexTranId") String itexTranId);










//
//




}
