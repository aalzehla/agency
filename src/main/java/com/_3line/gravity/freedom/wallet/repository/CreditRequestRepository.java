package com._3line.gravity.freedom.wallet.repository;

import com._3line.gravity.freedom.wallet.models.CreditRequest;
import com._3line.gravity.freedom.wallet.models.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CreditRequestRepository extends JpaRepository<CreditRequest, Long> {

    Page<CreditRequest> findByStatus(Pageable pageable, Status status);
    List<CreditRequest> findByOrderByIdDesc();



    @Query("select s from CreditRequest s order by s.approvalDate desc ")
    Page<CreditRequest> findAllByOrderByApprovalDateDesc(Pageable pageable);

    Page<CreditRequest> findByStatusOrderByCreatedOnDesc(Status status,Pageable pageable);

    List<CreditRequest> findByAgentName(String agentName);

    CreditRequest findByBankReference(String bankReference);
}
