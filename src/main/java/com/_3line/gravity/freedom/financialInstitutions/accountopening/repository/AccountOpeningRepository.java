package com._3line.gravity.freedom.financialInstitutions.accountopening.repository;

import com._3line.gravity.freedom.financialInstitutions.accountopening.model.AccountOpening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AccountOpeningRepository extends JpaRepository<AccountOpening, Long> {

    List<AccountOpening> findByAgentNameAndCommissionPaid(String agentName, String commissionPaid);

    Page<AccountOpening> findByAgentNameContainsAndAccountNumberContainsAndPhoneNumberContainsAndEmailContainsAndCreatedOnBetweenOrderByIdDesc(String agentName, String accountNumber , String phoneNumber , String email , Date from , Date to , Pageable pageable);

    Page<AccountOpening> findByCreatedOnBetweenOrderByIdDesc(Date from , Date to , Pageable pageable);
}
