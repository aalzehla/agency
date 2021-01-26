package com._3line.gravity.web.system_user.institution;


import com._3line.gravity.core.code.dto.CodeDTO;
import com._3line.gravity.core.code.service.CodeService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.commisioncharge.dtos.CommissionChargeDTO;
import com._3line.gravity.freedom.commisioncharge.service.CommissionChargeService;
import com._3line.gravity.freedom.institution.dto.InstitutionDTO;
import com._3line.gravity.freedom.institution.service.InstitutionService;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/core/institution")
public class InstitutionController {


    private static final Logger logger = LoggerFactory.getLogger(InstitutionController.class);


    @Autowired
    MessageSource messageSource;

    @Autowired
    CodeService  codeService;

    @Autowired
    InstitutionService institutionService;



    @ModelAttribute
    public void models(Model model) {

//        List<CodeDTO> codeDTOList = codeService.getCodesByType("INSTITUTION_CHARGES");
//        model.addAttribute("institutionOptions", codeDTOList);

    }

    @GetMapping("/add")
    public String addInstitutionPage(Model model) {
        model.addAttribute("institutionDTO", new InstitutionDTO());
        return "institution/add";
    }


    @GetMapping("/{reqId}/edit")
    public String editInstitution(@PathVariable Long reqId, Model model) {
        InstitutionDTO institutionDTO  =  institutionService.getInstitutionById(reqId);
        model.addAttribute("institutionDTO", institutionDTO);
        return "institution/edit";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addInstitution(@Valid @ModelAttribute("institutionDTO") InstitutionDTO institutionDTO, Model model,BindingResult result, RedirectAttributes redirectAttributes,Locale locale) {

        if (result.hasErrors()) {
            result.addError(new ObjectError("invalid", messageSource.getMessage("form.fields.required", null, locale)));
            return "institution/add";
        }

        try {
            institutionService.addInstitution(institutionDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Institution Added");
            return "redirect:/core/institution";
        } catch (GravityException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "institution/add";
        }
    }


    @PostMapping("/update")
    public String updateInstitution(@ModelAttribute("institutionDTO") @Valid InstitutionDTO institutionDTO , Model model, BindingResult result, WebRequest request, RedirectAttributes redirectAttributes, Locale locale) {
        if (result.hasErrors()) {
            result.addError(new ObjectError("invalid", messageSource.getMessage("form.fields.required", null, locale)));
            return "institution/edit";
        }

        try {

            institutionDTO.setDel_flag("N");
            institutionService.updateInstitution(institutionDTO);
            redirectAttributes.addFlashAttribute("message", "Institution Updated");
            return "redirect:/core/institution";
        } catch (GravityException ibe) {
            model.addAttribute("error", ibe.getMessage());
            result.addError(new ObjectError("error", ibe.getMessage()));
            logger.error("Error updating Institution", ibe);
            return "institution/edit";
        }

    }

    @GetMapping("/{chargeId}/delete")
    public String deleteInstitution(@PathVariable Long chargeId, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            institutionService.deleteInstitutionById(chargeId);
            redirectAttributes.addFlashAttribute("message", "Institution deleted");
            return "redirect:/core/institution";
        } catch (GravityException ibe) {
            logger.error("Error deleting Institution", ibe);
            redirectAttributes.addFlashAttribute("failure", ibe.getMessage());
            return "redirect:/core/institution";
        }
    }


    @GetMapping
    public String showInstitutionPage(Model model){

        return "institution/view";
    }

    @ResponseBody
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public DataTablesOutput<InstitutionDTO> getInstitution(DataTablesInput input) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();

        List<InstitutionDTO> institutionDTOS = institutionService.getAllInstitutions();

        System.out.println(institutionDTOS);

        DataTablesOutput<InstitutionDTO> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(institutionDTOS);
        out.setRecordsFiltered(institutionDTOS.size());
        out.setRecordsTotal(institutionDTOS.size());
        return out;
    }


}
