package com._3line.gravity.web.system_user.setting;


import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.setting.dto.SettingDTO;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.freedom.NIBBS.model.NIBBSReportSettings;
import com._3line.gravity.freedom.NIBBS.service.NIBBSReportSettingsService;
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

/**
 * Created by FortunatusE on 8/3/2018.
 */

@Controller
@RequestMapping("/core/settings")
public class SettingController {

    private static final Logger logger = LoggerFactory.getLogger(SettingController.class);
    private final SettingService settingService;
    private final MessageSource messageSource;

    @Autowired
    NIBBSReportSettingsService  nibbsReportSettingsService;


    @Autowired
    public SettingController(SettingService settingService, MessageSource messageSource) {
        this.settingService = settingService;
        this.messageSource = messageSource;
    }

    @GetMapping("/nibbs/edit")
    public String showNIBBSSetings(Model model) {
        model.addAttribute("nibbs_setting", nibbsReportSettingsService.fetchReportSettings());
        return "setting/nibbs/edit";
    }

    @RequestMapping(value = "/nibbs/update", method = RequestMethod.POST)
    public String updateNIBBSSetting(@Valid @ModelAttribute("nibbs_setting") NIBBSReportSettings settingDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes, Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "setting/edit";
        }

        try {
            nibbsReportSettingsService.updateReportSettings(settingDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Successfully Updated");
            return "redirect:/core/settings/nibbs/edit";

        }
        catch (VerificationException e){
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/settings/nibbs/edit";
        }
        catch (GravityException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/core/settings/nibbs/edit";
        }


    }


    @GetMapping("/add")
    public String showAddSettingPage(Model model) {

        model.addAttribute("setting", new SettingDTO());
        return "setting/add";
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addSetting(@Valid @ModelAttribute("setting") SettingDTO settingDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes, Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "setting/edit";
        }

        try {
            String message = settingService.addSetting(settingDTO);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/settings";
        } catch (GravityException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "setting/add";
        }


    }

    @GetMapping("/{id}/edit")
    public String showEditSettingPage(@PathVariable Long id, Model model) {

        SettingDTO setting = settingService.getSettingById(id);
        model.addAttribute("setting", setting);
        return "setting/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String updateSetting(@Valid @ModelAttribute("setting") SettingDTO settingDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes, Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "setting/edit";
        }

        try {
            String message = settingService.updateSetting(settingDTO);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/settings";

        }
        catch (VerificationException e){
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/settings";
        }
        catch (GravityException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "setting/edit";
        }


    }


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteSetting(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            String message = settingService.deleteSetting(id);
            redirectAttributes.addFlashAttribute("successMessage", message);
        }
        catch (VerificationException e){
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/settings";
        }
        catch (GravityException e) {
            logger.error(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/core/settings";

    }


    @GetMapping
    public String showSettingsPage() {
        return "setting/view";
    }


    @ResponseBody
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public DataTablesOutput<SettingDTO> getAllSettings(DataTablesInput input, @RequestParam(value = "csearch", required = false) String search) {

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<SettingDTO> settingDTOs;
        if (StringUtils.isNotBlank(search)) {
            settingDTOs = settingService.findSettings(search, pageable);
        } else {
            settingDTOs = settingService.getAllSettings(pageable);
        }
        DataTablesOutput<SettingDTO> out = new DataTablesOutput<SettingDTO>();
        out.setDraw(input.getDraw());
        out.setData(settingDTOs.getContent());
        out.setRecordsFiltered(settingDTOs.getTotalElements());
        out.setRecordsTotal(settingDTOs.getTotalElements());
        return out;
    }


    ////    @RequestMapping(value = "/enable/{id}", method = RequestMethod.GET)
////    public String enableSetting(@PathVariable Long id) {
////
////
////        try {
////            String message = settingService.enableSetting(id);
////            return ResponseEntity.ok(response);
////        } catch (GravityException e) {
////            response.setErrorMessage(e.getMessage());
////            logger.error(e.getMessage());
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
////        }
////
////    }
////
////    @RequestMapping(value = "/disable/{id}", method = RequestMethod.GET)
////    public ResponseEntity disableSetting(@PathVariable Long id) {
////
////        GravityResponse response = new GravityResponse();
////
////        try {
////            String message = settingService.disableSetting(id);
////            response.setSuccessMessage(message);
////            return ResponseEntity.ok(response);
////        } catch (GravityException e) {
////            logger.error(e.getMessage());
////            response.setErrorMessage(e.getMessage());
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
////        }
//
//    }

}
