package com._3line.gravity.web.system_user.issuelog;


import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.utils.DateUtil;
import com._3line.gravity.freedom.issuelog.dtos.CommentDTO;
import com._3line.gravity.freedom.issuelog.dtos.IssueLogDTO;
import com._3line.gravity.freedom.issuelog.models.IssueLog;
import com._3line.gravity.freedom.issuelog.service.IssueLogService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;


@Controller
@RequestMapping(value = "core/issuelog")
public class IssueLogController {


    private IssueLogService issueLogService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private MessageSource messageSource;

    @Autowired
    public IssueLogController(IssueLogService issueLogService, MessageSource messageSource) {
        this.issueLogService = issueLogService;
        this.messageSource = messageSource;
    }

    @PreAuthorize("hasAuthority('VIEW_ISSUE_LOG')")
    @GetMapping(value = "/")
    public String viewIssuelog(IssueLogDTO issueLogDTO, Model model) {
        model.addAttribute("issueLogDTO", issueLogDTO);
        return "issuelog/view";
    }

    @ModelAttribute
    public void models(Model model) {

        model.addAttribute("yesterday", DateUtil.formatDateToreadable_(DateUtil.yesterday()));
        model.addAttribute("today", DateUtil.formatDateToreadable_(DateUtil.tomorrow()));

    }

    @GetMapping(value = "/all")
    @ResponseBody
    public DataTablesOutput<IssueLog> getDatatablesTransactions(DataTablesInput input, @RequestParam("agentName") String agentName, @RequestParam("status") String status, @RequestParam("from") String from,
                                                                @RequestParam("category") String category,@RequestParam("to") String to,
                                                                @RequestParam("ticket_id") String ticket_id) {


        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<IssueLog> gravityRoleDTOPage;
        gravityRoleDTOPage = issueLogService.getTransactionsCustom(agentName, status, from, to, category,ticket_id,pageable);
        DataTablesOutput<IssueLog> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(gravityRoleDTOPage.getContent());
        out.setRecordsFiltered(gravityRoleDTOPage.getTotalElements());
        out.setRecordsTotal(gravityRoleDTOPage.getTotalElements());
        return out;
    }


    @PreAuthorize("hasAuthority('MANAGE_ISSUE_LOG')")
    @GetMapping(value = "/{id}/update")
    public String updateLog(@PathVariable("id") Long id, Model model) {

        IssueLog issueLog = issueLogService.findIssueLog(id);
        IssueLogDTO issueLogDTO = issueLogService.convertEntityToDTO(issueLog);
        List<CommentDTO> issueLogComments = issueLogService.getComments(id);
        model.addAttribute("issueLogComments", issueLogComments);
        model.addAttribute("updateIssue", issueLogDTO);
        model.addAttribute("agent", issueLog.getAgent());
        model.addAttribute("dispute", issueLog.getDisputeLog());
        return "issuelog/update";
    }

    @PreAuthorize("hasAuthority('MANAGE_ISSUE_LOG')")
    @PostMapping(value = "/resolved")
    public String getUpdateCommentAndStatus(@Valid @ModelAttribute("updateIssue") IssueLogDTO issueLogDTO, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale, WebRequest webRequest) {


        if (result.hasErrors()) {
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "issuelog/view";
        }

        try {
            //get response
            String buttonAction = webRequest.getParameter("buttonAction");
            logger.info("Button Action {}", buttonAction);
            issueLogDTO.setButtonAction(buttonAction);
            String response = issueLogService.updateCommentAndStatus(issueLogDTO);
            logger.info("Response {}", response);
            redirectAttributes.addFlashAttribute("successMessage",response);
            return "redirect:/core/issuelog/";
        }
        catch (GravityException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/core/issuelog/";
        }

    }

    @GetMapping("/{id}/presave/compliant")
    public
    @ResponseBody
    String preSaveCompliant(@PathVariable Long id) {
        IssueLog issueLog = issueLogService.findIssueLog(id);
        IssueLogDTO issueLogDTO = issueLogService.convertEntityToDTO(issueLog);
        issueLogDTO.setButtonAction("V");
//        logger.info("issue log: {}", issueLogDTO);
        issueLogService.addComments(issueLogDTO);
//        if (!issueLog.getStatus().toString().equals("RESOLVED")) {
//            issueLogService.updateCommentAndStatus(issueLogDTO);
//        }
        return "success";
    }

}
