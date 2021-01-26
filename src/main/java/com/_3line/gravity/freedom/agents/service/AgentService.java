package com._3line.gravity.freedom.agents.service;

import com._3line.gravity.api.users.agents.dto.AgentDashBoardDTO;
import com._3line.gravity.api.users.agents.dto.AgentSetupDto;
import com._3line.gravity.api.users.agents.dto.AgentSetupResponse;
import com._3line.gravity.api.auth.dto.LoginDto;
import com._3line.gravity.api.users.aggregator.dto.AggregatorAgentDTO;
import com._3line.gravity.api.users.aggregator.dto.AggregatorDashBoardDTO;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.agents.dtos.*;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.models.Devicesetup;
import com._3line.gravity.freedom.agents.models.Mandates;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface AgentService {


    @PreAuthorize("hasAuthority('MANAGE_AGENTS')")
    String createNewAgent(AgentDto agentDto)throws GravityException ;

    Map<String,Object> createAggregatorNewAgent(AgentDto agentDto)throws GravityException ;

    void validateNewAgent(AgentDto agentDto) throws GravityException;

    void manualCreateAgent(Agents agent ) throws GravityException;


    Agents validateAgentCreds(LoginDto loginDto) throws GravityException;

    @Async
    void sendSetupNotification(Agents sn, Map<String, Object> params);

    @PreAuthorize("hasAuthority('MANAGE_AGENTS')")
    String updateAgent(AgentDto agentDto);

    String updateAgent(Agents agents);

    String setNIBSSAgentCode(String agentId,String agentCode);

    void validateUpdate(AgentDto agentDto);

//    @PreAuthorize("hasAuthority('MANAGE_AGENTS')")
    String resetAgentPin(PinResetDto pinResetDto);

    String resetPin(PinResetDto pinResetDto);

//    @PreAuthorize("hasAuthority('MANAGE_AGENTS')")
    String resetAgentPassword(PassWordResetDto passWordResetDto);

    String resetPassword(PassWordResetDto passWordResetDto);

    String updatePassword(PasswordUpdateDto passwordUpdateDto);

    String updatePin(PinUpdateDTO pinUpdateDTO);

    @PreAuthorize("hasAuthority('MANAGE_AGENTS')")
    String disableAgent(Long id);

    @PreAuthorize("hasAuthority('MANAGE_AGENTS')")
    String enableAgent(Long id);

    Agents getAgent(Long id);


    Agents getAgentByAgentId(String agentId);

    Agents getAgentByWalletNumber(String walletNumber);

    Agents getAgentByTerminalId(String terminalId);

    Mandates getAgentMandate(Agents agents);

    List<Agents> getAggregatorsAgent(Agents agents);

    Page<AggregatorAgentDTO> getPagedAggregatorsAgent(Agents agents, Pageable pageable,boolean findByParentAgent);

    Page<AggregatorAgentDTO> getPagedSubAggregatorsAgent(Agents agents, Pageable pageable);

    Devicesetup getDevice(Long agentId);

    @PreAuthorize("hasAuthority('MANAGE_AGENTS')")
    String refreshDevice(DeviceRefreshDto deviceRefreshDto);

    Page<AgentDto> getAgents(Pageable pageDetails);

    Page<Agents> getUnSyncedSanefAgents(Pageable pageDetails);


    List<AgentDto> aggregators();

    List<AgentDto> subAggregators();

    @Cacheable(cacheNames = "findAllAgents")
    Page<Agents> fetchAgentViewDTO(Pageable pageable);

    Page<Agents> fetchAgentViewDTO(String username,Pageable pageable);

    AgentDto convertEntityToDTO(Agents agents);

    String generateAgentId(AgentDto agent);


    Long uploadAgents(MultipartFile file);

    List<AgentDto> getUploadedAgents(Long fileId);

    List<AgentDto> createUploadedAgents(Long fileId);

    AgentSetupResponse agentSetup(AgentSetupDto request) throws GravityException;

    Response logon(LoginDto request) throws GravityException;

    Agents fetchAgentByAgentName(String agentName);

    /**
     * We currently having some aggregator agents sharing email with their agents
     * and so it is most likely we get multiple records for one email
     * @param  @email
     * @return @List<Agents>
     */
    List<Agents> fetchAgentByEmail(String email);

    Agents fetchAgentById(String agentId);

    AggregatorDashBoardDTO fetchAggregatorDashborad(Agents agent);

    AgentDashBoardDTO fetchAgentDashBoard(String agentId);

    Agents fetchAgentByParentAgentAndAgentId(Agents parentAgentId,String agentId);


}
