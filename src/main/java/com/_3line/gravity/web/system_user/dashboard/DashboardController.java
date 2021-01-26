package com._3line.gravity.web.system_user.dashboard;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.usermgt.dto.ApplicationUserDTO;
import com._3line.gravity.core.usermgt.dto.UpdatePasswordDTO;
import com._3line.gravity.core.usermgt.service.ApplicationUserService;
import com._3line.gravity.core.verification.dtos.VerificationDto;
import com._3line.gravity.freedom.NIBBS.model.AgentPerformanceReport;
import com._3line.gravity.freedom.dashboard.DashboardService;
import com._3line.gravity.freedom.dashboard.models.Dashboard;
import com._3line.gravity.web.system_user.setting.SettingController;
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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequestMapping
@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(SettingController.class);
    private final SettingService settingService;
    private final MessageSource messageSource;
    private final ApplicationUserService userService;
    private final DashboardService dashboardService ;


    @Autowired
    public DashboardController(SettingService settingService, MessageSource messageSource, ApplicationUserService userService , DashboardService dashboardService) {
        this.settingService = settingService;
        this.messageSource = messageSource;
        this.userService = userService;
        this.dashboardService = dashboardService;
    }


    @GetMapping(path = "/core/dashboard/data")
    public
    @ResponseBody
    DataTablesOutput<Dashboard> getAllUserVerifications(DataTablesInput input) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Dashboard dashboard = new Dashboard();//dashboardService.getRecords();
        List<Dashboard>  dashboards = new ArrayList<>();
        dashboards.add(dashboard);
        DataTablesOutput<Dashboard> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(dashboards);
        out.setRecordsFiltered(dashboards.size());
        out.setRecordsTotal(dashboards.size());
        return out;
    }


    @RequestMapping("/core/dashboard")
    public String dashBoard(Principal principal , Model model){
        try {
            logger.info("principal {}",principal);
            ApplicationUserDTO user = userService.findByUsername(principal.getName());
            if (user.isChangePassword()) {
                return "redirect:/core/password";
            }
            model.addAttribute("dashboard",dashboardService.getRecords());
//            model.addAttribute("dashboard", new LazyContextVariable<Dashboard>() {
//                @Override
//                protected Dashboard loadValue() {
//                    return dashboardService.getRecords();
//                }
//            });

        }catch (Exception e){
            e.printStackTrace();
        }
        return "dashboard/dashboard";
    }


    @RequestMapping("/")
    public String index(){

        return "redirect:/core/dashboard/";
    }


    @GetMapping("/core/password")
    public String updatePassword(Principal principal, Model model ){
        ApplicationUserDTO applicationUserDTO = userService.findByUsername(principal.getName());
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setId(applicationUserDTO.getId());
        model.addAttribute("update", updatePasswordDTO);

        return "security/resetPassword";
    }


    @RequestMapping(value = "/core/password/update", method = RequestMethod.POST)
    public String updateSetting(@Valid @ModelAttribute("update") UpdatePasswordDTO updatePasswordDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes, Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("failed", messageSource.getMessage("form.fields.required", null, locale));
            return "setting/edit";
        }

        try {
            String message = userService.updatePassword(updatePasswordDTO);
            redirectAttributes.addFlashAttribute("failed", message);
            return "redirect:/core/logout";

        }
        catch (GravityException e) {
            logger.error(e.getMessage());
            redirectAttributes.addFlashAttribute("failed", e.getMessage());
            return "redirect:/core/password";
        }


    }




}
