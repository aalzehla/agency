package com._3line.gravity.freedom.accountmgt.repository;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.freedom.accountmgt.model.BankAccountOpening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BankAccountOpeningRepository extends AppCommonRepository<BankAccountOpening, Long> {


    BankAccountOpening findByAccountNumber(String accountNumber);

//    Page<BankAccountOpening> findAllByAccountNumber(String accountNumber, Pageable pageable);
//
//    Page<BankAccountOpening> findByDateCreatedBetweenOrderByDateCreatedDesc(Date startDate, Date endDate, Pageable pageable);
//
//    Page<BankAccountOpening> findByAccountNumberAndDateCreatedBetweenOrderByDateCreatedDesc(String accountNumber, Date startDate, Date endDate, Pageable pageable);
//
//    Page<BankAccountOpening> findAllByOrderByDateCreatedDesc(Pageable pageable);

}
