package com._3line.gravity.web.system_user.verifiableactions;


import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.verification.dtos.VerifiableActionDto;
import com._3line.gravity.core.verification.service.VerifiableActionService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/core/approvals")
public class VerifiableActionsController {
    private static final Logger logger = LoggerFactory.getLogger(VerifiableActionsController.class);
    private final VerifiableActionService verifiableActionService ;
    private final MessageSource messageSource;

    @Autowired
    public VerifiableActionsController( VerifiableActionService verifiableActionService , MessageSource messageSource){
        this.messageSource = messageSource ;
        this.verifiableActionService = verifiableActionService ;
    }


    @RequestMapping("/")
    public String index(){
        return "approvals/view";
    }


    @GetMapping("/{id}/edit")
    public String showEditApprovalPage(@PathVariable Long id, Model model) {

        VerifiableActionDto actionDto = verifiableActionService.getOne(id);
        model.addAttribute("action", actionDto);
        return "approvals/edit";
    }


    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String updateApproval(@Valid @ModelAttribute("action") VerifiableActionDto actionDto, BindingResult result, Model model, RedirectAttributes redirectAttributes, Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "approvals/edit";
        }

        try {
            String message = verifiableActionService.updateVerifiableAction(actionDto);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/approvals/";

        } catch (GravityException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "approvals/edit";
        }


    }


    @ResponseBody
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public DataTablesOutput<VerifiableActionDto> getAllSettings(DataTablesInput input, @RequestParam(value = "csearch", required = false) String search) {

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<VerifiableActionDto> actionDtos;
        if (StringUtils.isNotBlank(search)) {
            actionDtos = verifiableActionService.findUsingPattern(search, pageable);
        } else {
            actionDtos = verifiableActionService.getAll(pageable);
        }
        DataTablesOutput<VerifiableActionDto> out = new DataTablesOutput<VerifiableActionDto>();
        out.setDraw(input.getDraw());
        out.setData(actionDtos.getContent());
        out.setRecordsFiltered(actionDtos.getTotalElements());
        out.setRecordsTotal(actionDtos.getTotalElements());
        return out;
    }

}
