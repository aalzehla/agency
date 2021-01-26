package com._3line.gravity.freedom.terminalmanager.service.implementation;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.files.service.FileService;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.verification.annotations.RequireApproval;
import com._3line.gravity.freedom.agents.dtos.AgentDto;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.financialInstitutions.wemaapi.utils.DateFormatter;
import com._3line.gravity.freedom.itexintegration.model.PtspModel;
import com._3line.gravity.freedom.itexintegration.repository.PtspRepository;
import com._3line.gravity.freedom.terminalmanager.dto.TerminalAuditDTO;
import com._3line.gravity.freedom.terminalmanager.dto.TerminalManagerDTO;
import com._3line.gravity.freedom.terminalmanager.dto.TerminalRequest;
import com._3line.gravity.freedom.terminalmanager.model.TerminalAudit;
import com._3line.gravity.freedom.terminalmanager.model.TerminalManager;
import com._3line.gravity.freedom.terminalmanager.repository.TerminalAuditRepository;
import com._3line.gravity.freedom.terminalmanager.repository.TerminalManagerRepository;

import com._3line.gravity.freedom.terminalmanager.service.TerminalService;
import io.github.mapper.excel.ExcelMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class TerminalServiceImpl implements TerminalService {
    private static Logger logger = LoggerFactory.getLogger(Object.class);


    private AgentsRepository agentsRepository;
    private TerminalManagerRepository managerRepository;
    private TerminalAuditRepository auditRepository;
    private PtspRepository ptspRepository;
    private ModelMapper modelMapper;
    private FileService fileService;

    @Autowired
    public TerminalServiceImpl(AgentsRepository agentsRepository, TerminalManagerRepository managerRepository, TerminalAuditRepository auditRepository, PtspRepository ptspRepository, ModelMapper modelMapper, FileService fileService) {
        this.agentsRepository = agentsRepository;
        this.managerRepository = managerRepository;
        this.auditRepository = auditRepository;
        this.ptspRepository = ptspRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }

    @RequireApproval(code = "DETACH_TERMINAL", entityType = TerminalManager.class)
    @Override
    public Response detachTerminal(TerminalRequest request) {
        Response response = new Response();
        Agents agents = request.getAgents();
        Agents newAgent;
        if (agents == null) {
            try {
                throw new Exception("AgentId not found");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.info("got here");
            /* criteria for retrieving a terminal */
            logger.info("Terminal Id: {}", request.getTerminalId());
            boolean isEmpty = request.getTerminalId() == null || request.getTerminalId().trim().isEmpty();
            if (isEmpty) {
                // handle the validation
                response.setRespCode("99");
                response.setRespDescription("TerminalId not found for agent");
                return response;
            }

            if (checkIfTerminalExists(request.getTerminalId())) {
                //update Agent information

                Agents agent = getAgentByUsername(request.getUsername());
                logger.info("agent information with agent details {} and {} and terminal {}", agent.getId(), agent.getAgentId(), agent.getTerminalId());

                if (request.getAction().equalsIgnoreCase("A")) {
                    agent.setTerminalId(request.getTerminalId());
                }
                if (request.getAction().equalsIgnoreCase("D")) {
                    agent.setTerminalId("");
                }

                Agents checker = agentsRepository.findByTerminalId(request.getTerminalId().trim());
                if (Objects.nonNull(checker)) {
                    throw new GravityException("Terminal ID [" + request.getTerminalId() + "] already exists");
                }


                logger.info("new terminal Id: {}", agent.getTerminalId());

                newAgent = agentsRepository.save(agent);
                logger.info("Update agent info {} and {} and {}", newAgent.getId(), newAgent.getAgentId(), newAgent.getTerminalId());
                //update ends here

                //update Terminal Management table, update status only
                saveTerminalAvailable(newAgent, request);
                // update ends here


                /* Audit section*/
                //save audit record
                saveAuditManager(agents, request);
                //updates ends here

                response.setRespCode("00");
                response.setRespDescription("success");
                response.setRespBody(newAgent);
            } else {
                response.setRespCode("99");
                response.setRespDescription("Agent still has a pending transaction");
            }
        }
        return response;
    }

    @Override
    public Agents getAgentByUsername(String username) {
        logger.info("username of the user: {}", username);
        return agentsRepository.findByUsername(username);
    }

    private boolean checkIfTerminalExists(String terminalID) {
        boolean response = false;
        logger.info("checking....");
        PtspModel ptspModels = ptspRepository.findFirstByTerminalIdAndIsVerified(false,terminalID);
        logger.info("checking complete....{}", ptspModels);
        if (ptspModels == null) {
            response = true;
        }

        return response;
    }

    private void saveAuditManager(Agents agents, TerminalRequest request) {
        TerminalAudit audit = new TerminalAudit();
        Agents aggregator = agentsRepository.getOne(agents.getParentAgentId());
        audit.setDateAssigned(new Date());
        audit.setTerminalId(request.getTerminalId());
        audit.setAggregator(aggregator);
        audit.setAssignToAgent(agents);
        if (request.getAction().equalsIgnoreCase("D")) {
            audit.setDescription("Detach terminal from " + agents.getAgentId());
        }
        if (request.getAction().equalsIgnoreCase("A")) {
            audit.setDescription("Assigned terminal to " + agents.getAgentId());
        }
        auditRepository.save(audit);
    }

    private void saveTerminalAvailable(Agents newAgent, TerminalRequest request) {
        TerminalManager terminalManager = new TerminalManager();
        if (request.getAction() != null) {
            if (request.getAction().equalsIgnoreCase("A")) {
                terminalManager.setIsAssigned(true);
                terminalManager.setStatus("ASSIGNED");
            }
            if (request.getAction().equalsIgnoreCase("D")) {
                terminalManager.setIsAssigned(false);
                terminalManager.setStatus("DETACHED");
            }
            terminalManager.setTerminalId(request.getTerminalId());
            terminalManager.setAgents(newAgent);
            logger.info("terminal info: {}", terminalManager);
            managerRepository.save(terminalManager);
        }
    }

    @Override
    public Response fetchAllTerminal() {
        Response response = new Response();
        try {
            List<TerminalManager> result = new ArrayList<>();
            for (TerminalManager i : managerRepository.findAll()) {
                if (!"ASSIGNED".equalsIgnoreCase(i.getStatus())) {
                    result.add(i);
                }
            }
            List<TerminalManagerDTO> list = convertEntitiesToDTOs(
                    result);

            response.setRespCode("00");
            response.setRespDescription("success");
            response.setRespBody(list);
        }
        catch (GravityException e) {
            response.setRespCode("96");
            response.setRespDescription(e.getMessage());
        }
        return response;
    }

    private List<TerminalManagerDTO> convertEntitiesToDTOs(List<TerminalManager> terminalManagers) {
        return terminalManagers.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    private TerminalManagerDTO convertEntityToDTO(TerminalManager terminalManager) {
        TerminalManagerDTO managerDTO = modelMapper.map(terminalManager, TerminalManagerDTO.class);
        if (terminalManager.getAgents() != null && terminalManager.getAgents().getAgentId() != null) {
            managerDTO.setAgentDetails(terminalManager.getAgents().getAgentId());
        } else {
            managerDTO.setAgentDetails("");
        }
        return managerDTO;
    }

    private List<TerminalAuditDTO> convertEntitiesToDTOsAudit(List<TerminalAudit> terminalAudits) {
        return terminalAudits.stream().map(this::convertEntityToDTOAudit).collect(Collectors.toList());
    }

    private TerminalAuditDTO convertEntityToDTOAudit(TerminalAudit terminalAudit) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        TerminalAuditDTO auditDTO = modelMapper.map(terminalAudit, TerminalAuditDTO.class);
        auditDTO.setAssignToAgent(terminalAudit.getAssignToAgent().getAgentId());
        auditDTO.setAggregator(terminalAudit.getAggregator().getAgentId());
        auditDTO.setDateAssigned(DateFormatter.format(terminalAudit.getDateAssigned()));
        return auditDTO;

    }

    @Override
    public Page<TerminalManagerDTO> getAllTerminalPagination(Pageable pageDetails) {
        Page<TerminalManager> page = managerRepository.findAll(pageDetails);
        List<TerminalManagerDTO> dtOs = convertEntitiesToDTOs(page.getContent());
        long t = page.getTotalElements();
        return new PageImpl<>(dtOs, pageDetails, t);
    }

    @Override
    public List<TerminalManagerDTO> getAllTerminal() {
        List<TerminalManager> terminalManagers = managerRepository.findByStatus("DETACHED");
        List<TerminalManagerDTO> terminalManagerDTOS = convertEntitiesToDTOs(terminalManagers);
        return terminalManagerDTOS;
    }

    @Override
    public Page<TerminalAuditDTO> getAllAuditPagination(Pageable pageDetails) {
        Page<TerminalAudit> page = auditRepository.findAll(pageDetails);
        List<TerminalAuditDTO> dtOs = convertEntitiesToDTOsAudit(page.getContent());
        long t = page.getTotalElements();
        return new PageImpl<>(dtOs, pageDetails, t);
    }

    @Override
    public List<Agents> getAllAgentUsername() {
        List<Agents> agentsList = new ArrayList<>();
        for (Agents i : agentsRepository.findAll()) {
            if (i.getUsername() != null) {
                agentsList.add(i);
            }
        }
        return agentsList;
    }

    @Override
    public TerminalManagerDTO getTerminalById(Long id) {
        TerminalManager terminalManager = managerRepository.findOne(id);
        return convertEntityToDTO(terminalManager);
    }

    @Override
    public Long uploadTerminal(MultipartFile multipartFile) {

        logger.info("Uploading agent terminal file: {}", multipartFile.getOriginalFilename());

        Long fileId = fileService.storeFile(multipartFile, "CREATE_TERMINAL_ID_FILE");

        File file;
        try {
            file = fileService.loadFileAsResource(fileId).getFile();

            List<TerminalManagerDTO> dtos = mapTerminalIdDataFromFile(file);

            logger.info("Agents terminaLd file: {}", dtos);
            return fileId;
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new GravityException("Error uploading file");

        }
        catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            throw new GravityException("Error uploading file");
        }


    }

    @Override
    public List<TerminalManagerDTO> createUploadedAgents(Long fileId) {
        logger.info("Creating agents in file with Id [{}]", fileId);
        List<TerminalManagerDTO> uploadedAgents = getUploadedTerminal(fileId);
        List<TerminalManagerDTO> failedAgents = new ArrayList<>();

        for (TerminalManagerDTO managerDTO : uploadedAgents) {

            try {
                TerminalManager terminalManager = new TerminalManager();
                terminalManager.setAgents(null);
                terminalManager.setIsAssigned(false);
                terminalManager.setStatus("DETACHED");
                terminalManager.setTerminalId(managerDTO.getTerminalId());
                List<TerminalManager> terminalManagers = managerRepository.findByTerminalId(managerDTO.getTerminalId());
                if(terminalManagers == null || terminalManagers.size()==0 ){
                    managerRepository.save(terminalManager);
                }else{
                    logger.error("Terminal ID already exists: "+managerDTO.getTerminalId());
                }

            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                failedAgents.add(managerDTO);
            }
        }

        logger.info("Failed agents: {}", failedAgents);
        return failedAgents;
    }

    @Override
    public List<TerminalManagerDTO> getUploadedTerminal(Long fileId) {

        File file;
        try {
            file = fileService.loadFileAsResource(fileId).getFile();
            List<TerminalManagerDTO> terminalManagerDTOS = mapTerminalIdDataFromFile(file);

            logger.info("Agents from file: {}", terminalManagerDTOS);
            return terminalManagerDTOS;
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new GravityException("Error fetching agents file");

        }
        catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            throw new GravityException("Error fetching agents file");
        }
    }

    @Override
    public Page<TerminalManagerDTO> findUsingPattern(String search, Pageable pageable) {
        Page<TerminalManager> page = managerRepository.findUsingPattern(search, pageable);
        List<TerminalManagerDTO> list = new ArrayList<>();
        page.getContent().forEach( p -> {
            list.add(convertEntityToDTO(p));
        });
        return new PageImpl<>(list,pageable, page.getTotalElements());
    }

    @Override
    public Page<TerminalAuditDTO> findUsingPatternAudit(String search, Pageable pageable) {
        Page<TerminalAudit> page=auditRepository.findUsingPattern(search,pageable);
        List<TerminalAuditDTO> list = new ArrayList<>();
        page.getContent().forEach( p -> {
            list.add(convertEntityToDTOAudit(p));
        });
        return new PageImpl<>(list,pageable, page.getTotalElements());
    }

    private List<TerminalManagerDTO> mapTerminalIdDataFromFile(File file) throws Throwable {

        List<TerminalManagerDTO> map = ExcelMapper.mapFromExcel(file)
                .toObjectOf(TerminalManagerDTO.class)

                .fromSheet(0) // if this method not used , called all sheets
                .map();
        return map;
    }
}
