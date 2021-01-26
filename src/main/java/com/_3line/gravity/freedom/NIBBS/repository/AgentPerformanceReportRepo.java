package com._3line.gravity.freedom.NIBBS.repository;


import com._3line.gravity.freedom.NIBBS.model.AgentDataReport;
import com._3line.gravity.freedom.NIBBS.model.AgentPerformanceReport;
import com._3line.gravity.freedom.agents.models.Agents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AgentPerformanceReportRepo extends JpaRepository<AgentPerformanceReport,Long>{

    AgentPerformanceReport findByUpdatedOn(Date lastUpdated);

    List<AgentPerformanceReport> findByStatusOrderByCreatedOnDesc(String status);
}
