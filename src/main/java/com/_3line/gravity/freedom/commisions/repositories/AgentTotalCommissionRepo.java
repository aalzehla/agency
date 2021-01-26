package com._3line.gravity.freedom.commisions.repositories;

import com._3line.gravity.freedom.commisions.models.AgentTotalCommission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AgentTotalCommissionRepo extends JpaRepository<AgentTotalCommission, Long> {

}
