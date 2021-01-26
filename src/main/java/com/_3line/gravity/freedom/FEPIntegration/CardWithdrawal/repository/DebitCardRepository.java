package com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.repository;

import com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.model.DebitCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DebitCardRepository extends JpaRepository<DebitCard,Long> {

//    DebitCard findByAgentIdentifier(String agentIdentifier);
//
//    DebitCard findByAgentIdentifierAndDeviceId(String agentId, String deviceId);

    List<DebitCard> findByAgent_Id(Long agentId);

    DebitCard findByToken(String agentId);

    DebitCard findByPan(String pan);


}
