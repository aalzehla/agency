package com._3line.gravity.freedom.device.service;

import com._3line.gravity.freedom.agents.models.Devicesetup;
import org.springframework.mobile.device.Device;

public interface DeviceService {
    void auditLoginDevice(Devicesetup device);

    Devicesetup fetchAuditByDeviceIdAndAgentId(String deviceId, String agentId);

    Boolean hasLoggedInBefore(String deviceId, long agentId);

    Devicesetup attacheDeviceToUser(String deviceId,long agentId);

    Devicesetup fetchByDeviceIdAndAgentId(String deviceId,long agentId);
}

