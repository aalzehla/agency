package com._3line.gravity.web.system_user.agents;


import com._3line.gravity.core.code.service.CodeService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.freedom.agents.dtos.AgentDto;
import com._3line.gravity.freedom.agents.dtos.DeviceRefreshDto;
import com._3line.gravity.freedom.agents.dtos.PassWordResetDto;
import com._3line.gravity.freedom.agents.dtos.PinResetDto;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.repository.BankDetailsRepository;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.terminalmanager.dto.TerminalManagerDTO;
import com._3line.gravity.freedom.terminalmanager.model.TerminalManager;
import com._3line.gravity.freedom.terminalmanager.repository.TerminalManagerRepository;
import com._3line.gravity.freedom.terminalmanager.service.TerminalService;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/core/agents")
public class AgentController {

    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);

    private final MessageSource messageSource;
    private final AgentService agentService;
    private final CodeService codeService  ;
    private final BankDetailsService bankDetailsService ;
    private final WalletService walletService ;
    private final BankDetailsRepository bankDetailsRepository ;
    private final TerminalService terminalService ;

    @Autowired
    TerminalManagerRepository  managerRepository;


    @Autowired
    public AgentController(MessageSource messageSource, AgentService agentService , CodeService codeService , BankDetailsService bankDetailsService , TerminalService terminalService,WalletService walletService,BankDetailsRepository bankDetailsRepository) {
        this.messageSource = messageSource;
        this.agentService = agentService;
        this.codeService = codeService ;
        this.bankDetailsService = bankDetailsService ;
        this.walletService = walletService ;
        this.bankDetailsRepository = bankDetailsRepository ;
        this.terminalService = terminalService ;
    }
    @ModelAttribute
    public void agentModels(Model model) {


//        model.addAttribute("availableTerminals", terminalService.getAllTerminal());

    }


    @GetMapping("/")
    @PreAuthorize("hasAuthority('VIEW_AGENTS')")
    public String index(){

        return "agents/view";
    }

     @GetMapping("/aggregator")
     @PreAuthorize("hasAuthority('VIEW_AGENTS')")
    public String aggregators(){ return "agents/aggregators"; }

    @GetMapping(value = "/{id}/")
    public  String agent(@PathVariable("id") Long id , Model model){
        viewAgent(model , id);
        return "agents/view-agent";
    }


    public void viewAgent(Model model , Long id){
        Agents agents = agentService.getAgent(id) ;
        if(Objects.nonNull(agents.getParentAgentId()) && agents.getParentAgentId() > 0){
            model.addAttribute("parent", agentService.getAgent(agents.getParentAgentId()).getUsername());
        }else {
            model.addAttribute("parent","NONE");
        }
        model.addAttribute("agent",agents );
        model.addAttribute("trading",walletService.getWalletByNumber(agents.getWalletNumber()) );
        model.addAttribute("income",walletService.getWalletByNumber(agents.getIncomeWalletNumber()) );
        model.addAttribute("device",agentService.getDevice(id) );
        model.addAttribute("mandate",agentService.getAgentMandate(agents) );
        model.addAttribute("subs",agentService.getAggregatorsAgent(agents) );
    }

    @PreAuthorize("hasAuthority('MANAGE_AGENTS')")
    @GetMapping(value = "/{id}/resetPin")
    public  String resetPin(@PathVariable("id") Long id , Model model,RedirectAttributes redirectAttributes){
        try {
            PinResetDto pinResetDto = new PinResetDto();
            Agents agents = agentService.getAgent(id);

            pinResetDto.setAgentId(id);
            pinResetDto.setAgentName(agents.getUsername());

            String message = agentService.resetAgentPin(pinResetDto);
            model.addAttribute("successMessage", message);
            viewAgent(model , id);
        }catch (VerificationException e){
            e.printStackTrace();
            logger.info(e.getMessage());
            model.addAttribute("successMessage", e.getMessage());
            viewAgent(model , id);
        }catch (GravityException e){
            logger.error(e.getMessage());
            viewAgent(model , id);
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "agents/view-agent";
    }

    @PreAuthorize("hasAuthority('MANAGE_AGENTS')")
    @GetMapping(value = "/{id}/resetPassword")
    public  String resetPassword(@PathVariable("id") Long id , Model model,RedirectAttributes redirectAttributes){
        try {
            PassWordResetDto passWordResetDto = new PassWordResetDto() ;
            Agents agents = agentService.getAgent(id);

            passWordResetDto.setAgentId(id);
            passWordResetDto.setAgentName(agents.getUsername());

            String message = agentService.resetAgentPassword(passWordResetDto);
            model.addAttribute("successMessage", message);
            viewAgent(model , id);
        }catch (VerificationException e){
            e.printStackTrace();
            logger.info(e.getMessage());
            model.addAttribute("successMessage", e.getMessage());
            viewAgent(model , id);
        }catch (Exception e){
            logger.error(e.getMessage());
            viewAgent(model , id);
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "agents/view-agent";
    }

    @GetMapping(value = "/{agent}/device/{id}/update")
    public  String refreshDevice(@PathVariable("agent") Long agent ,@PathVariable("id") Long id, Model model,RedirectAttributes redirectAttributes){
        try {
            DeviceRefreshDto deviceRefreshDto = new DeviceRefreshDto() ;

            Agents agents = agentService.getAgent(agent);

            deviceRefreshDto.setDeviceId(id);
            deviceRefreshDto.setAgentName(agents.getUsername());

            String message = agentService.refreshDevice(deviceRefreshDto);
            model.addAttribute("successMessage", message);
            viewAgent(model , agent);
        }catch (VerificationException e){
            e.printStackTrace();
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            viewAgent(model , id);
        }catch (GravityException e){
            logger.error(e.getMessage());
            viewAgent(model , agent);
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "agents/view-agent";
    }


    @GetMapping(value = "/{id}/enable")
    public  String enable(@PathVariable("id") Long id , Model model,RedirectAttributes redirectAttributes){
        try {
            String message = agentService.enableAgent(id);
            model.addAttribute("successMessage", message);
            viewAgent(model , id);
        }catch (GravityException e){
            logger.error(e.getMessage());
            viewAgent(model , id);
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "agents/view-agent";
    }

    @GetMapping(value = "/{id}/disable")
    public  String disable(@PathVariable("id") Long id , Model model,RedirectAttributes redirectAttributes){
        try {
            String message = agentService.disableAgent(id);
            model.addAttribute("successMessage", message);
            viewAgent(model , id);
        }catch (GravityException e){
            logger.error(e.getMessage());
            viewAgent(model , id);
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "agents/view-agent";
    }

    @GetMapping(value = "/create")
    public String createAgents(Model model){

        model.addAttribute("agent", new AgentDto());
        model.addAttribute("idtype", codeService.getCodesByType("ID TYPE"));
        model.addAttribute("idtype", codeService.getCodesByType("ID TYPE"));
        model.addAttribute("bank", bankDetailsService.findAllBankDetails());
        model.addAttribute("aggregators", agentService.aggregators());
        model.addAttribute("subAggregators", agentService.subAggregators());

        return "agents/create-agent";
    }

    @GetMapping(value = "/{id}/edit")
    public  String editAgents(@PathVariable("id") Long id , Model model){
        AgentDto agentDto =  agentService.convertEntityToDTO(agentService.getAgent(id));
        List<TerminalManagerDTO> terminalManagerDTOS = terminalService.getAllTerminal();
        if(agentDto.getTerminalId()!=null && !agentDto.getTerminalId().equals("")){
            TerminalManagerDTO managerDTO = new TerminalManagerDTO();
            managerDTO.setTerminalId(agentDto.getTerminalId());
            managerDTO.setIsAssigned(true);
            managerDTO.setAgentDetails(agentDto.getUsername());
            if(!terminalManagerDTOS.contains(managerDTO)){
                terminalManagerDTOS.add(managerDTO);
            }

        }

        model.addAttribute("idtype", codeService.getCodesByType("ID TYPE"));
        model.addAttribute("idtype", codeService.getCodesByType("ID TYPE"));
        model.addAttribute("bank", bankDetailsService.findAllBankDetails());
        model.addAttribute("aggregators", agentService.aggregators());
        model.addAttribute("subAggregators", agentService.subAggregators());
        model.addAttribute("agent",agentDto);
//        model.addAttribute("availableTerminals",terminalManagerDTOS);
        return "agents/edit-agent";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String addAgent(@Valid @ModelAttribute("agent") AgentDto agentDTO , BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {

        logger.info("Agent details: {}", agentDTO);
        if (result.hasErrors()) {
            List<String> errors = new ArrayList<>();
            result.getAllErrors().forEach(objectError -> {
                if(objectError instanceof FieldError) {
                    FieldError fieldError = (FieldError) objectError;
                    errors.add(fieldError.getDefaultMessage());
                }
            });

            model.addAttribute("errorMessage",errors.get(0));
            return "agents/create-agent";
        }

        try {
            agentDTO.setUsername(agentDTO.getFirstName().trim()+"."+agentDTO.getLastName().trim());
            agentService.validateNewAgent(agentDTO);



            String message = agentService.createNewAgent(agentDTO);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/agents/";
        } catch (VerificationException e){
            e.printStackTrace();
            logger.error("Error {}",e);
            model.addAttribute("successMessage",e.getMessage());
            return "agents/create-agent";
        } catch (GravityException e) {
            e.printStackTrace();
            logger.error("Error {}",e);
            model.addAttribute("errorMessage",e.getMessage());
            return "agents/create-agent";
        }

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateAgent(@Valid @ModelAttribute("agent") AgentDto agentDTO , BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {

        if (result.hasErrors()) {

            List<String> errors = new ArrayList<>();
            result.getAllErrors().forEach(objectError -> {
                if(objectError instanceof FieldError) {
                    FieldError fieldError = (FieldError) objectError;
                    errors.add(fieldError.getDefaultMessage());
                }
            });

            model.addAttribute("errorMessage",errors.get(0));
            return "agents/edit-agent";
        }

        try {
            agentService.validateUpdate(agentDTO);
            logger.info("validated successfully");



            if(agentDTO !=null && agentDTO.getTerminalId() != null && !agentDTO.getTerminalId().trim().equals("")){
                agentDTO.setTerminalId(agentDTO.getTerminalId().trim());
                BankDetails bankDetails = bankDetailsRepository.findByCbnCode(agentDTO.getTerminalId().substring(1,4));

                if(bankDetails !=null){
                    agentDTO.setBankCode(bankDetails.getBankCode());
                }
            }
            String message = agentService.updateAgent(agentDTO);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/agents/"+agentDTO.getId()+"/";
        } catch (VerificationException e){
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return"redirect:/core/agents/";
        }catch (GravityException e) {
            logger.error(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/core/agents/"+agentDTO.getId()+"/edit";
        }
    }

    @GetMapping(value = "/all")
    @ResponseBody
    public DataTablesOutput<Agents> getAllAgents(DataTablesInput input,@RequestParam("csearch") String search) {

        DataTablesOutput<Agents> out = new DataTablesOutput<>();

        try {

            Pageable pageable = new SpecificationBuilder<>(input).createPageable();
            Page<Agents> agentsPage = null;
            if(search==null || search.trim().equals("")){
                agentsPage = agentService.fetchAgentViewDTO(pageable);
            }else{
                agentsPage = agentService.fetchAgentViewDTO(search,pageable);
            }
            out.setDraw(input.getDraw());
            out.setData(agentsPage.getContent());
            out.setRecordsFiltered(agentsPage.getTotalElements());
            out.setRecordsTotal(agentsPage.getTotalElements());
        }catch (Exception e){
            e.printStackTrace();
        }

        return out;
    }

    @GetMapping(value = "/aggregators")
    public @ResponseBody
    DataTablesOutput<AgentDto> getAllAggregators(DataTablesInput input) {
        List<AgentDto> codes = agentService.aggregators();
        DataTablesOutput<AgentDto> out = new DataTablesOutput<AgentDto>();
        out.setDraw(input.getDraw());
        out.setData(codes);
        out.setRecordsFiltered(codes.size());
        out.setRecordsTotal(codes.size());
        return out;
    }

    @PreAuthorize("hasAuthority('UPLOAD_AGENTS_FILE')")
    @GetMapping("/upload")
    public String uploadAgents(){
        return "agents/upload-agents";
    }

    @PostMapping("/upload")
    public String saveUploadedAgents(@RequestParam("file") MultipartFile file , Model model, RedirectAttributes redirectAttributes){

        if (file.isEmpty()) {
            logger.info("Empty file");
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/core/agents/";
        }

        try {
            Long fileId  = agentService.uploadAgents(file);
            model.addAttribute("fileId", fileId);
            return "agents/agents-upload-view";
        }catch (VerificationException v){
            logger.info(v.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", v.getMessage());
        }catch (GravityException e){

            logger.error(e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/core/agents/";

    }


    @GetMapping("/upload/{fileId}/create")
    public String createUploadedAgents(@PathVariable Long fileId, Model model, HttpSession session,  RedirectAttributes redirectAttributes){



        try {
            List<AgentDto> failedAgents = agentService.createUploadedAgents(fileId);
            session.setAttribute("failedAgents", failedAgents);
            return "agents/agents-upload-result";
        }catch (VerificationException v){
            logger.info(v.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", v.getMessage());
        }catch (GravityException e){

            logger.error(e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/core/agents/";
    }


    @GetMapping("/upload/view")
    public String viewUploadedAgents(){
        return "agents/agents-upload-view";
    }


    @GetMapping(value = "/upload/{fileId}/all")
    @ResponseBody
    public DataTablesOutput<AgentDto> getAllAgents(DataTablesInput input, @PathVariable Long fileId) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        List<AgentDto> codes = agentService.getUploadedAgents(fileId);
        DataTablesOutput<AgentDto> out = new DataTablesOutput<AgentDto>();
        out.setDraw(input.getDraw());
        out.setData(codes);
        out.setRecordsFiltered(codes.size());
        out.setRecordsTotal(codes.size());
        return out;
    }

    @GetMapping(value = "/upload/result")
    @ResponseBody
    public DataTablesOutput<AgentDto> agentsUploadResult(DataTablesInput input, HttpSession session) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();

        List<AgentDto> agentDtos = new ArrayList<>();
        Object failedAgents = session.getAttribute("failedAgents");
        if(failedAgents!=null){
            try {
                logger.info("Showing failed agents: {}", failedAgents);
                agentDtos = (ArrayList)failedAgents;

            }
            catch (Exception e){
                logger.error(e.getMessage(),e);
            }

        }

        DataTablesOutput<AgentDto> out = new DataTablesOutput<AgentDto>();
        out.setDraw(input.getDraw());
        out.setData(agentDtos);
        out.setRecordsFiltered(agentDtos.size());
        out.setRecordsTotal(agentDtos.size());
        return out;
    }
}
