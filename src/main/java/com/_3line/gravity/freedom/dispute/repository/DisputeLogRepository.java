package com._3line.gravity.freedom.dispute.repository;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.freedom.dispute.models.DisputeLog;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DisputeLogRepository extends AppCommonRepository<DisputeLog, Long> {

    List<DisputeLog> findByTranId(long tranId);

}
