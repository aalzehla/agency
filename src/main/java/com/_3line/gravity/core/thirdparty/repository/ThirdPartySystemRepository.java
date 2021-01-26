package com._3line.gravity.core.thirdparty.repository;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.core.thirdparty.model.ThirdPartySystem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThirdPartySystemRepository extends JpaRepository<ThirdPartySystem , Long> {

    ThirdPartySystem findByAppId(String appId);

    Boolean existsByClientName(String clientName);


}
