package com._3line.gravity.freedom.terminalmanager.repository;


import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.freedom.terminalmanager.model.TerminalManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TerminalManagerRepository extends AppCommonRepository<TerminalManager,Long> {

    List<TerminalManager> findByTerminalId(String terminalId);

    List<TerminalManager> findByStatus(String status);

}
