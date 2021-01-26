package com._3line.gravity.web.system_user.charge;


import com._3line.gravity.core.code.dto.CodeDTO;
import com._3line.gravity.core.code.service.CodeService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.utils.DateUtil;
import com._3line.gravity.freedom.commisioncharge.dtos.CommissionChargeDTO;
import com._3line.gravity.freedom.commisioncharge.service.CommissionChargeService;
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
@RequestMapping("/core/charge")
public class ChargeController {


    private static final Logger logger = LoggerFactory.getLogger(ChargeController.class);
    private CommissionChargeService chargeService ;

    private MessageSource messageSource;

    @Autowired
    CodeService  codeService;


    @Autowired
    public ChargeController(CommissionChargeService chargeService , MessageSource messageSource) {
        this.chargeService = chargeService;
        this.messageSource = messageSource ;
    }

    @ModelAttribute
    public void models(Model model) {

        List<CodeDTO> codeDTOList = codeService.getCodesByType("INSTITUTION_CHARGES");
        model.addAttribute("institutionOptions", codeDTOList);

    }

    @GetMapping("/add")
    public String addChargePage(Model model) {
        model.addAttribute("charge", new CommissionChargeDTO());
        model.addAttribute("tranType", chargeService.getChargeTransactionTypes());
        return "charge/add";
    }


    @GetMapping("/{reqId}/edit")
    public String editRole(@PathVariable Long reqId, Model model) {
        CommissionChargeDTO commissionChargeDTO  =  chargeService.convertEntityToDTO(chargeService.findCommissionCharge(reqId));
        model.addAttribute("charge", commissionChargeDTO);
        model.addAttribute("tranType", chargeService.getChargeTransactionTypes());
        return "charge/edit";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addCharge(@Valid @ModelAttribute("charge") CommissionChargeDTO commissionChargeDTO, Model model, RedirectAttributes redirectAttributes) {

        try {
            String message = chargeService.createCommissionCharge(commissionChargeDTO);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/charge";
        } catch (GravityException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "charge/add";
        }
    }


    @PostMapping("/update")
    public String updateCharge(@ModelAttribute("charge") @Valid CommissionChargeDTO commissionChargeDTO , Model model, BindingResult result, WebRequest request, RedirectAttributes redirectAttributes, Locale locale) {
        if (result.hasErrors()) {
            result.addError(new ObjectError("invalid", messageSource.getMessage("form.fields.required", null, locale)));
            return "charge/edit";
        }

        try {
            String message = chargeService.updateCommissionCharge(commissionChargeDTO);
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/core/charge";
        } catch (GravityException ibe) {
            model.addAttribute("error", ibe.getMessage());
            result.addError(new ObjectError("error", ibe.getMessage()));
            logger.error("Error updating charge", ibe);
            return "charge/edit";
        }

    }

    @GetMapping("/{chargeId}/delete")
    public String deleteCharge(@PathVariable Long chargeId, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            String message = chargeService.deleteCommissionCharge(chargeId);
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/core/charge";
        } catch (GravityException ibe) {
            logger.error("Error deleting charge", ibe);
            redirectAttributes.addFlashAttribute("failure", ibe.getMessage());
            return "redirect:/core/charge";
        }
    }


    @GetMapping
    public String showChargePage(Model model){

        return "charge/view";
    }

    @ResponseBody
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public DataTablesOutput<CommissionChargeDTO> getCharges(DataTablesInput input,@RequestParam("institution") String institution) {
        logger.info("looking for charges {} ",institution);
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<CommissionChargeDTO> settingDTOs;
        if(!institution.equals("")){
            settingDTOs = chargeService.getCommissionChargeByInstitution(pageable,institution);
        }else{
            settingDTOs = chargeService.getCommissionCharge(pageable);
        }

        DataTablesOutput<CommissionChargeDTO> out = new DataTablesOutput<CommissionChargeDTO>();
        out.setDraw(input.getDraw());
        out.setData(settingDTOs.getContent());
        out.setRecordsFiltered(settingDTOs.getTotalElements());
        out.setRecordsTotal(settingDTOs.getTotalElements());
        return out;
    }


}
