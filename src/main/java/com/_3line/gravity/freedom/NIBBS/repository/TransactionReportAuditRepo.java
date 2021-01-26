package com._3line.gravity.freedom.NIBBS.repository;

import com._3line.gravity.freedom.NIBBS.model.TransationReportAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionReportAuditRepo extends JpaRepository<TransationReportAudit,Long> {

}
