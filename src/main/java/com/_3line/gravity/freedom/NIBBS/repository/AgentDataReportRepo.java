package com._3line.gravity.freedom.NIBBS.repository;


import com._3line.gravity.freedom.NIBBS.model.AgentDataReport;
import com._3line.gravity.freedom.agents.models.Agents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentDataReportRepo extends JpaRepository<AgentDataReport,Long>{

    AgentDataReport findByAgent_AgentId(String agentId);

    AgentDataReport findByNibbsAgentEmail(String Agentemail);

}
