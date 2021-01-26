package com._3line.gravity.freedom.cardrequest.repository;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.freedom.cardrequest.models.CardRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CardRequestRepository extends AppCommonRepository<CardRequest, Long> {

    Page<CardRequest> findByAgentNameContainsAndStatusAndCreatedOnBetweenOrderByCreatedOnDesc
            (String agentName, String status, Date from, Date to, Pageable pageable);

    Page<CardRequest> findAllByOrderByCreatedOnDesc(Pageable pageable);

}
