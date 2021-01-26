package com._3line.gravity.freedom.agents.repository;

import com._3line.gravity.freedom.agents.models.Devicesetup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceSetupRepository extends JpaRepository<Devicesetup, Long> {

    Devicesetup findByAgentId(Long agentId);

    Devicesetup findByClientid(String clienId);

    Devicesetup findByDeviceid(String deviceId);

    Devicesetup findByDeviceidAndAgentId(String deviceId,long agentId);
}
