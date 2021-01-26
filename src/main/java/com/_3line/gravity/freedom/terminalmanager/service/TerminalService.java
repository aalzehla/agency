package com._3line.gravity.freedom.terminalmanager.service;

import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.models.Devicesetup;
import com._3line.gravity.freedom.terminalmanager.dto.TerminalAuditDTO;
import com._3line.gravity.freedom.terminalmanager.dto.TerminalManagerDTO;
import com._3line.gravity.freedom.terminalmanager.dto.TerminalRequest;
import com._3line.gravity.freedom.terminalmanager.model.TerminalManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TerminalService {
    /** create method to attach and detach terminal*/
    Response detachTerminal(TerminalRequest request);

    Agents getAgentByUsername(String username);

    /** List of all terminal id and status */
    Response fetchAllTerminal();

    Page<TerminalManagerDTO> getAllTerminalPagination(Pageable pageDetails);

    List<TerminalManagerDTO> getAllTerminal();

    Page<TerminalAuditDTO> getAllAuditPagination(Pageable pageDetails);

    List<Agents> getAllAgentUsername();

    TerminalManagerDTO getTerminalById(Long id);

    Long uploadTerminal(MultipartFile multipartFile);

    List<TerminalManagerDTO> createUploadedAgents(Long fileId);

    List<TerminalManagerDTO> getUploadedTerminal(Long fileId);

    Page<TerminalManagerDTO> findUsingPattern(String search, Pageable pageable);

    Page<TerminalAuditDTO> findUsingPatternAudit(String search, Pageable pageable);
}

