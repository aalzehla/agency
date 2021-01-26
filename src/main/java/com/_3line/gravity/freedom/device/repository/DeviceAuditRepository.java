package com._3line.gravity.freedom.device.repository;

import com._3line.gravity.freedom.device.model.DeviceAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceAuditRepository extends JpaRepository<DeviceAudit,Long> {

    DeviceAudit findByAgentIdentifier(String agentIdentifier);

    DeviceAudit findByAgentIdentifierAndDeviceId(String agentId,String deviceId);
}
