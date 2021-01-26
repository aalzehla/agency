package com._3line.gravity.freedom.institution.repository;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.freedom.institution.model.Institution;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstitutionRepository extends AppCommonRepository<Institution, Long> {

    Institution findByName(String name);

    Institution findByInstitutionAgent_Id(Long agentId);

    Institution findByInstitutionAgent_Username(String username);

}
