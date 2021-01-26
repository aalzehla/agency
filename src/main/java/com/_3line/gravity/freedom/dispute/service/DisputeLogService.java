package com._3line.gravity.freedom.dispute.service;

import com._3line.gravity.freedom.dispute.dtos.DisputeLogDTO;
import com._3line.gravity.freedom.dispute.models.Dispute;
import com._3line.gravity.freedom.dispute.models.DisputeLog;

public interface DisputeLogService {

    void logDisputeComplaint(DisputeLog o,Long issuLogId);

    String raiseWalletDispute(DisputeLogDTO dispute);

    void validateDisputeLog(long tranId);
}
