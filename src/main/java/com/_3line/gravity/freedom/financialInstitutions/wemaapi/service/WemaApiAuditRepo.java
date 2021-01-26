package com._3line.gravity.freedom.financialInstitutions.wemaapi.service;


import com._3line.gravity.freedom.financialInstitutions.wemaapi.model.WemaApiAudit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author JoyU
 * @date 9/17/2018
 */
public interface WemaApiAuditRepo extends JpaRepository<WemaApiAudit, Long> {

}
