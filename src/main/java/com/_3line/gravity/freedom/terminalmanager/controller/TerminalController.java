package com._3line.gravity.freedom.terminalmanager.controller;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.freedom.terminalmanager.dto.TerminalAuditDTO;
import com._3line.gravity.freedom.terminalmanager.dto.TerminalManagerDTO;
import com._3line.gravity.freedom.terminalmanager.dto.TerminalRequest;
import com._3line.gravity.freedom.terminalmanager.service.TerminalService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Controller
@RequestMapping("/core/agents")
public class TerminalController {
    private static final Logger logger = LoggerFactory.getLogger(Object.class);
    private  MessageSource messageSource;

    private TerminalService terminalService;

    @Autowired
    public TerminalController(MessageSource messageSource, TerminalService terminalService) {
        this.messageSource = messageSource;
        this.terminalService = terminalService;
    }

    @ModelAttribute
    public void agentModels(Model model) {
        model.addAttribute("terminalIds", terminalService.fetchAllTerminal());
        model.addAttribute("agentList",terminalService.getAllAgentUsername());
    }

    @GetMapping("/terminal")
    public String getList() {
        return "terminal/terminalmanagement";
    }

    @GetMapping("/add")
    public String Add() {
        return "terminal/terminaladd";
    }

    @GetMapping("/audit")
    public String getAudit() {
        return "terminal/terminalaudit";
    }

    @GetMapping(value = "/all/terminal")
    public @ResponseBody
    DataTablesOutput<TerminalManagerDTO> getAllAggregators(DataTablesInput input, @RequestParam(value = "csearch", required = false) String search) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<TerminalManagerDTO> codes;
        if (Objects.nonNull(search)) {
            logger.info("search value: {}",search);
            codes = terminalService.findUsingPattern(search, pageable);
        } else {
            codes = terminalService.getAllTerminalPagination(pageable);
        }

        DataTablesOutput<TerminalManagerDTO> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(codes.getContent());
        out.setRecordsFiltered(codes.getTotalElements());
        out.setRecordsTotal(codes.getTotalElements());
        return out;
    }


    @GetMapping(value = "/all/audit")
    public @ResponseBody
    DataTablesOutput<TerminalAuditDTO> getAllHistory(DataTablesInput input,@RequestParam(value = "csearch", required = false) String search) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<TerminalAuditDTO> codes;
        if (Objects.nonNull(search)) {
            logger.info("search cricteria: {}",search);
            codes = terminalService.findUsingPatternAudit(search, pageable);
        }
         else{
            codes = terminalService.getAllAuditPagination(pageable);
        }
        DataTablesOutput<TerminalAuditDTO> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(codes.getContent());
        out.setRecordsFiltered(codes.getTotalElements());
        out.setRecordsTotal(codes.getTotalElements());
        return out;
    }

    @GetMapping("/upload/terminal")
    public String uploadAgents() {
        return "terminal/terminalview";
    }

    @GetMapping("/terminal/{id}/web/attach")
    public String atachWeb(@PathVariable Long id,Model model){
        TerminalManagerDTO terminalManagerDTO=terminalService.getTerminalById(id);
        model.addAttribute("terminalManagerDTO",terminalManagerDTO);
        return "terminal/terminalattach";
    }

    @GetMapping("/terminal/{id}/web/detach")
    public String detachWeb(@PathVariable Long id,Model model){
        TerminalManagerDTO terminalManagerDTO=terminalService.getTerminalById(id);
        model.addAttribute("terminalManagerDTO",terminalManagerDTO);
        return "terminal/terminaldetach";
    }

    @PostMapping("/terminal/web/attach")
    public String attach(@ModelAttribute("terminalManagerDTO") @Valid TerminalManagerDTO terminalManagerDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes, Locale locale){
        TerminalRequest terminalRequest=new TerminalRequest();
        if (result.hasErrors()) {
            logger.error("Invalid form inputs: {}", terminalManagerDTO);
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "terminal/terminalattach";

        }
        try {
            terminalRequest.setAction("A");
            setTerminalDetails(terminalManagerDTO, redirectAttributes, terminalRequest);
            return "redirect:/core/agents/terminal";
        }
         catch (GravityException ge) {
            model.addAttribute("errorMessage", ge.getMessage());
            logger.error(ge.getMessage());
             return "terminal/terminalattach";

        }
    }

    private void setTerminalDetails(@Valid @ModelAttribute("terminalManagerDTO") TerminalManagerDTO terminalManagerDTO, RedirectAttributes redirectAttributes, TerminalRequest terminalRequest) {
        terminalRequest.setUsername(terminalManagerDTO.getAgentDetails());
        terminalRequest.setTerminalId(terminalManagerDTO.getTerminalId());
        terminalRequest.setAgents(terminalService.getAgentByUsername(terminalManagerDTO.getAgentDetails()));
        Response response = terminalService.detachTerminal(terminalRequest);
        redirectAttributes.addFlashAttribute("successMessage", response.getRespDescription());
    }

    @PostMapping("/terminal/web/detach")
    public String detach(@ModelAttribute("terminalManagerDTO") @Valid TerminalManagerDTO terminalManagerDTO, BindingResult result, Model model,RedirectAttributes redirectAttributes,Locale locale){
        TerminalRequest terminalRequest=new TerminalRequest();
        if (result.hasErrors()) {
            logger.error("Invalid form inputs: {}", terminalManagerDTO);
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "terminal/terminaldetach";

        }
        try {
            terminalRequest.setAction("D");
            setTerminalDetails(terminalManagerDTO, redirectAttributes, terminalRequest);
            return "redirect:/core/agents/terminal";
        }
        catch (GravityException ge) {
            model.addAttribute("errorMessage", ge.getMessage());
            logger.error(ge.getMessage());
            return "terminal/terminaldetach";

        }
    }

    @PostMapping("/upload/terminal")
    public String saveUploadedAgents(@RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            logger.info("Empty file");
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/core/agents/";
        }

        try {
            Long fileId = terminalService.uploadTerminal(file);
            model.addAttribute("fileId", fileId);
            return "terminal/terminaladd";
        }
        catch (VerificationException v) {
            logger.info(v.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", v.getMessage());
        }
        catch (GravityException e) {

            logger.error(e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/core/agents/";

    }

    @GetMapping("/upload/terminal/{fileId}/create")
    public String createUploadedAgents(@PathVariable Long fileId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            List<TerminalManagerDTO> failedAgents = terminalService.createUploadedAgents(fileId);
            session.setAttribute("failedAgents", failedAgents);
            return "terminal/terminalresult";
        }
        catch (VerificationException v) {
            logger.info(v.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", v.getMessage());
        }
        catch (GravityException e) {

            logger.error(e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/core/agents/";
    }

    @GetMapping(value = "/upload/terminal/{fileId}/all")
    @ResponseBody
    public DataTablesOutput<TerminalManagerDTO> getAllAgents(DataTablesInput input, @PathVariable Long fileId) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        List<TerminalManagerDTO> codes = terminalService.getUploadedTerminal(fileId);
        DataTablesOutput<TerminalManagerDTO> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(codes);
        out.setRecordsFiltered(codes.size());
        out.setRecordsTotal(codes.size());
        return out;
    }

    @GetMapping(value = "/upload/terminal/result")
    @ResponseBody
    public DataTablesOutput<TerminalManagerDTO> agentsUploadResult(DataTablesInput input, HttpSession session) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();

        List<TerminalManagerDTO> agentDtos = new ArrayList<>();
        Object failedAgents = session.getAttribute("failedAgents");
        if (failedAgents != null) {
            try {
                logger.info("Showing failed agents: {}", failedAgents);
                agentDtos = (ArrayList) failedAgents;

            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }

        DataTablesOutput<TerminalManagerDTO> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(agentDtos);
        out.setRecordsFiltered(agentDtos.size());
        out.setRecordsTotal(agentDtos.size());
        return out;
    }
}
