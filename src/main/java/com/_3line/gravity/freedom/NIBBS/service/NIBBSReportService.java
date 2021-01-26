package com._3line.gravity.freedom.NIBBS.service;

import com._3line.gravity.freedom.NIBBS.dto.AgentDataReportDTO;
import com._3line.gravity.freedom.NIBBS.dto.TransactionReportDTO;
import com._3line.gravity.freedom.NIBBS.dto.ResetReqDTO;
import com._3line.gravity.freedom.reports.dtos.AgentNibbsActivity;

import java.util.List;

public interface NIBBSReportService {

    void ping();

    void reset(ResetReqDTO resetReqDTO);

    AgentDataReportDTO createAgent(AgentDataReportDTO agentDataReportDTO);

    AgentDataReportDTO updateAgent(AgentDataReportDTO agentDataReportDTO);

    void createReport(TransactionReportDTO performanceDTO);

    void createReportInBulk(List<TransactionReportDTO> transactionReportDTOS,String uploadProcessId);


}
