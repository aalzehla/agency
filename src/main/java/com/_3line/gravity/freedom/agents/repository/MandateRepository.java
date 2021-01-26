package com._3line.gravity.freedom.agents.repository;

import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.models.Mandates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MandateRepository extends JpaRepository<Mandates, Long> {

    Mandates findByApplicationUsers(Agents applicationUsers);
}
