package com._3line.gravity.freedom.cardissuance.repository;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.freedom.cardissuance.model.CardIssuance;

public interface CardIssuanceRepository extends AppCommonRepository<CardIssuance, Long> {


    CardIssuance findByAccountNumber(String accountNumber);
    CardIssuance findByAccountNumberAndCardSerial(String accountNumber, String cardSerial);

//    Page<CardIssuance> findAllByOrderByDateCreatedDesc(Pageable pageable);
//
//    Page<CardIssuance> findByLinkedAndDateCreatedBetweenOrderByDateCreatedDesc(boolean linked, Date startDate, Date date, Pageable pageable);
//
//    Page<CardIssuance> findByDateCreatedBetweenOrderByDateCreatedDesc(Date startDate, Date date, Pageable pageable);
//
//    Page<CardIssuance> findByLinkedOrderByDateCreatedDesc(boolean linked, Pageable pageable);
}
