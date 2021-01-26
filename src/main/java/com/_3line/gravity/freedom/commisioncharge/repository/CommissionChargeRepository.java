package com._3line.gravity.freedom.commisioncharge.repository;

import com._3line.gravity.freedom.commisioncharge.models.CommissionCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommissionChargeRepository extends JpaRepository<CommissionCharge, Long> {

    List<CommissionCharge> findByTransactionTypeOrderByIdDesc(String transactionType) ;

    List<CommissionCharge> findByInstitution(String institutionName) ;

    List<CommissionCharge> findByTransactionTypeAndDelFlagOrderByIdDesc(String transactionType,String delFlag) ;

    @Query(value = "select * from commission_charge c " +
            "where c.transaction_type =:transactionType " +
            "and c.lower_bound < :amount " +
            "and c.upper_bound >= :amount " +
            "and c.del_flag = 'N' and c.institution =:institution",nativeQuery = true)
    CommissionCharge findByTransactionTypeAndAmountAndInst(@Param("transactionType") String transactionType,
                                                           @Param("amount") Double amount,
                                                           @Param("institution") String institution) ;
}
