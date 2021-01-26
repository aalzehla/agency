package com._3line.gravity.web.system_user.thirdparty;


import com._3line.gravity.core.thirdparty.dto.ThirdPartyDto;
import com._3line.gravity.core.thirdparty.exceptions.ThirdPartyException;
import com._3line.gravity.core.thirdparty.service.ThirdPartyService;
import com._3line.gravity.core.verification.exceptions.PendingVerificationException;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/core/thirdparty")
public class ThirdPartyController {

    @Autowired
    private ThirdPartyService thirdPartyService ;
    @Autowired
    private MessageSource messageSource;
    private static Logger logger = LoggerFactory.getLogger(ThirdPartyController.class);

    @GetMapping("/")
    public String index(){
        return "thirdparty/index";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("party", new ThirdPartyDto());
        return "thirdparty/create";
    }

    @GetMapping("/{id}/")
    public String view(@PathVariable("id") Long id , Model model){
        model.addAttribute("party", thirdPartyService.getThridParty(id));
        return "thirdparty/view";
    }

    @GetMapping(value = "/all")
    @ResponseBody
    public DataTablesOutput<ThirdPartyDto> getAll(DataTablesInput input) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        List<ThirdPartyDto> codes = thirdPartyService.getAll() ;
        DataTablesOutput<ThirdPartyDto> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(codes);
        out.setRecordsFiltered(codes.size());
        out.setRecordsTotal(codes.size());
        return out;
    }

    @PostMapping("/create")
    public String addUser(@Valid @ModelAttribute("party") ThirdPartyDto thirdPartyDto, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {


        if (result.hasErrors()) {
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return  "thirdparty/create";
        }

        try {

            String message = thirdPartyService.createThirdParty(thirdPartyDto);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/thirdparty/";
        } catch (VerificationException e) {
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/thirdparty/";
        } catch (ThirdPartyException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "thirdparty/create";
        }

    }

    @GetMapping("{id}/enable")
    public String enableParty(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            String message = thirdPartyService.enableThirdParty(id);
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (PendingVerificationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (VerificationException e) {
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
        } catch (ThirdPartyException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        }
        return "redirect:/core/thirdparty/";
    }

    @GetMapping("{id}/disable")
    public String disableUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            String message = thirdPartyService.disableThirdParty(id);
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (PendingVerificationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (VerificationException e) {
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
        } catch (ThirdPartyException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        }
        return "redirect:/core/thirdparty/";
    }



}
