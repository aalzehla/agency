package com._3line.gravity.web.system_user.code;

import com._3line.gravity.core.code.dto.CodeDTO;
import com._3line.gravity.core.code.model.CodeType;
import com._3line.gravity.core.code.service.CodeService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.verification.exceptions.VerificationException;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Locale;


/**
 * @author FortunatusE
 * @date 11/6/2018
 */

@Controller
@RequestMapping("/core/codes")
public class CodeController {

    private static final Logger logger = LoggerFactory.getLogger(CodeController.class);

    private final MessageSource messageSource;
    private final CodeService codeService;


    @Autowired
    public CodeController(MessageSource messageSource, CodeService codeService) {
        this.messageSource = messageSource;
        this.codeService = codeService;
    }

    @GetMapping("/add")
    public String addCode(Model model) {

        model.addAttribute("code", new CodeDTO());
        return "code/add";
    }

    @PostMapping
    public String createCode(@ModelAttribute("code") @Valid CodeDTO codeDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes, Locale locale) {
        if (result.hasErrors()) {
            logger.error("Invalid form inputs {}", codeDTO);
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "code/add";
        }

        try {
            String message = codeService.addCode(codeDTO);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/codes/alltypes";
        }
        catch (VerificationException e){
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/codes/alltypes";
        }
        catch (GravityException e) {
            model.addAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
            return "code/add";
        }
    }


    @GetMapping("/{codeId}")
    public CodeDTO getCode(@PathVariable Long codeId, Model model) {
        CodeDTO code = codeService.getCode(codeId);
        model.addAttribute("code", code);
        return code;
    }

    @GetMapping
    public String getCodes() {
        return "code/view";
    }

    @GetMapping("/alltypes")
    public String getCodeTypes() {
        return "code/type-view";
    }

    @GetMapping("/type/{type}/view")
    public String getCodeType(@PathVariable String type, Model model) {
        model.addAttribute("codeType", type);
        return "code/type-code-view";
    }


    @GetMapping("/type/{type}/new")
    public String addCode(@PathVariable String type, Model model) {
        CodeDTO code = new CodeDTO();
        code.setType(type);
        model.addAttribute("codeType", type);
        model.addAttribute("codeDTO", code);
        return addCode(model);
    }


    @GetMapping(path = "/type")
    public @ResponseBody
    DataTablesOutput<CodeType> getAllCodeTypes(DataTablesInput input, @RequestParam(value = "csearch", required = false) String search) {

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<CodeType> codeTypes;
        if (StringUtils.isNotBlank(search)) {
            codeTypes = codeService.findByCodeType(search, pageable);
        } else {
            codeTypes = codeService.getCodeTypes(pageable);
        }
        DataTablesOutput<CodeType> out = new DataTablesOutput<CodeType>();
        out.setDraw(input.getDraw());
        out.setData(codeTypes.getContent());
        out.setRecordsFiltered(codeTypes.getTotalElements());
        out.setRecordsTotal(codeTypes.getTotalElements());
        return out;
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    DataTablesOutput<CodeDTO> getAllCodes(DataTablesInput input) {

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<CodeDTO> codes = codeService.getCodes(pageable);
        DataTablesOutput<CodeDTO> out = new DataTablesOutput<CodeDTO>();
        out.setDraw(input.getDraw());
        out.setData(codes.getContent());
        out.setRecordsFiltered(codes.getTotalElements());
        out.setRecordsTotal(codes.getTotalElements());
        return out;
    }

    @GetMapping(path = "/alltype")
    public @ResponseBody
    DataTablesOutput<CodeDTO> getAllCodesOfType(@RequestParam(name = "codeType") String codeType, @RequestParam(name = "csearch", required = false) String search, DataTablesInput input) {

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<CodeDTO> codes;
        if (StringUtils.isNotBlank(search)) {
            codes = codeService.findByTypeAndValue(codeType, search, pageable);
        } else {
            codes = codeService.getCodesByType(codeType, pageable);
        }
        DataTablesOutput<CodeDTO> out = new DataTablesOutput<CodeDTO>();
        out.setDraw(input.getDraw());
        out.setData(codes.getContent());
        out.setRecordsFiltered(codes.getTotalElements());
        out.setRecordsTotal(codes.getTotalElements());
        return out;
    }

    @GetMapping("/{type}")
    public Iterable<CodeDTO> getCodesByType(@PathVariable String type, Model model) {
        Iterable<CodeDTO> codeList = codeService.getCodesByType(type);
        model.addAttribute("codeList", codeList);
        return codeList;
    }

    @GetMapping("/{id}/edit")
    public String editCode(@PathVariable Long id, Model model) {
        CodeDTO code = codeService.getCode(id);
        model.addAttribute("code", code);
        return "code/edit";
    }

    @PostMapping("/update")
    public String updateCode(@ModelAttribute("code") @Valid CodeDTO codeDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes, Locale locale) {
        if (result.hasErrors()) {
            logger.error("Invalid form inputs: {}", codeDTO);
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "code/edit";

        }
        try {
            String message = codeService.updateCode(codeDTO);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/codes/alltypes";
        }
        catch (VerificationException e){
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/codes/alltypes";
        }
        catch (GravityException ge) {
            model.addAttribute("errorMessage", ge.getMessage());
            logger.error(ge.getMessage());
            return "code/edit";

        }
    }

    @GetMapping("/{codeId}/delete")
    public String deleteCode(@PathVariable Long codeId, RedirectAttributes redirectAttributes) {
        try {
            String message = codeService.deleteCode(codeId);
            redirectAttributes.addFlashAttribute("successMessage", message);
        }
        catch (VerificationException e){
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/codes/alltypes";
        }
        catch (GravityException ge) {
            logger.error(ge.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", ge.getMessage());

        }
        return "redirect:/core/codes/alltypes";
    }

}
