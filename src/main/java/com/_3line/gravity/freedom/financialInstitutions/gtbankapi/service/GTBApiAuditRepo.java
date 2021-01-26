package com._3line.gravity.freedom.financialInstitutions.gtbankapi.service;

import com._3line.gravity.freedom.financialInstitutions.gtbankapi.models.GTBApiAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Utosu Joy
 */
@Repository
public interface GTBApiAuditRepo extends JpaRepository<GTBApiAudit, Long> {

}
