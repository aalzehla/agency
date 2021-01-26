package com._3line.gravity.freedom.terminalmanager.repository;


import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.freedom.terminalmanager.model.TerminalAudit;
import com._3line.gravity.freedom.terminalmanager.model.TerminalManager;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminalAuditRepository extends AppCommonRepository<TerminalAudit,Long> {

//Page<TerminalAudit> findBy
}
