package com._3line.gravity.freedom.NIBBS.service;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.NIBBS.model.AgentDataReport;
import com._3line.gravity.freedom.NIBBS.model.AgentPerformanceReport;
import com._3line.gravity.freedom.agents.models.Agents;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AgentDataReportService {

    AgentDataReport fetchAgentDataReport ( String agentId);

    AgentDataReport addAgentDataReport ( Agents agent) throws GravityException;

    AgentDataReport updateAgentDataReport ( Agents agent) throws GravityException;

    void sendBulkTransactionReport();

    String uploadBulkTransactionReport(MultipartFile  multipartFile);

    List<AgentPerformanceReport> getNIBBSUploadReport();
}
