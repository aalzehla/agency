package com._3line.gravity.web.system_user.usermgt;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.rolemgt.dtos.RoleDTO;
import com._3line.gravity.core.rolemgt.models.Role;
import com._3line.gravity.core.rolemgt.repositories.RoleRepository;
import com._3line.gravity.core.rolemgt.service.RoleService;
import com._3line.gravity.core.usermgt.dto.ApplicationUserDTO;
import com._3line.gravity.core.usermgt.exception.AppUserServiceException;
import com._3line.gravity.core.usermgt.service.ApplicationUserService;
import com._3line.gravity.core.verification.exceptions.PendingVerificationException;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/core/system/users")
public class SystemUserController {


    private static final Logger logger = LoggerFactory.getLogger(SystemUserController.class);

    private final ApplicationUserService applicationUserService;
    @Autowired
    private RoleRepository roleRepository;
    private final RoleService roleService;
    private final MessageSource messageSource;

    @Autowired
    public SystemUserController(ApplicationUserService applicationUserService, RoleService roleService, MessageSource messageSource) {
        this.applicationUserService = applicationUserService;
        this.roleService = roleService;
        this.messageSource = messageSource;
    }

    @ModelAttribute
    public void addRolesToModel(Model model) {

        List<RoleDTO> roles = roleService.getSystemRoles();
        model.addAttribute("roles", roles);
    }

    @GetMapping
    public String viewUsers() {
        return "user/system/view";
    }

    @RequestMapping(value = "/data/all", method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<ApplicationUserDTO> getUsers(@Valid DataTablesInput input, @RequestParam(value = "csearch", required = false) String search) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<ApplicationUserDTO> users;
        if (StringUtils.isNotBlank(search)) {
            users = applicationUserService.findSystemUsers(search, pageable);
        } else {
            users = applicationUserService.getSystemUsers(pageable);
        }
        DataTablesOutput<ApplicationUserDTO> out = new DataTablesOutput<ApplicationUserDTO>();
        out.setDraw(input.getDraw());
        out.setData(users.getContent());
        out.setRecordsFiltered(users.getTotalElements());
        out.setRecordsTotal(users.getTotalElements());
        return out;
    }

    @GetMapping("/add")
    public String showAddPage(Model model) {

        model.addAttribute("user", new ApplicationUserDTO());
        return "user/system/add";
    }

    @GetMapping("/{id}/edit")
    public String showEditPage(@PathVariable Long id, Model model) {

        ApplicationUserDTO user = applicationUserService.getUser(id);
        model.addAttribute("user", user);
        return "user/system/edit";
    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute("user") ApplicationUserDTO applicationUserDTO, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {


        if (result.hasErrors()) {
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "user/system/add";
        }

        try {
            applicationUserService.validateNewUser(applicationUserDTO);
            Role role = roleRepository.findOne(applicationUserDTO.getRoleId());
            applicationUserDTO.setRoleName(role.getName());
            applicationUserDTO.setRoleType(role.getRoleType().name());
            String message = applicationUserService.createSystemUser(applicationUserDTO);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/system/users";
        } catch (VerificationException e) {
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/system/users";
        } catch (AppUserServiceException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "user/system/add";
        }

    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("user") ApplicationUserDTO applicationUserDTO, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {


        if (result.hasErrors()) {
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "user/system/add";
        }

        try {
            String message = applicationUserService.updateSystemUser(applicationUserDTO);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/system/users";
        } catch (PendingVerificationException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "user/system/edit";
        } catch (VerificationException e) {
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/system/users";
        } catch (AppUserServiceException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "user/system/edit";
        }

    }

    @GetMapping("{id}/enable")
    public String enableUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            String message = applicationUserService.enableSystemUser(id);
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (PendingVerificationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (VerificationException e) {
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
        } catch (AppUserServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        }
        return "redirect:/core/system/users";
    }

    @GetMapping("{id}/disable")
    public String disableUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            String message = applicationUserService.disableSystemUser(id);
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (PendingVerificationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (VerificationException e) {
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
        } catch (AppUserServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        }
        return "redirect:/core/system/users";
    }


    @GetMapping("{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            String message = applicationUserService.deleteSystemUser(id);
            redirectAttributes.addFlashAttribute("successMessage", message);
        } catch (PendingVerificationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (VerificationException e) {
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
        } catch (AppUserServiceException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        }
        return "redirect:/core/system/users";
    }

    @GetMapping("/{id}/password/reset")
    public String resetPassword(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try {
            String message = applicationUserService.resetPassword(id);
            redirectAttributes.addFlashAttribute("successMessage", message);
        }
        catch (AppUserServiceException ibe) {
            redirectAttributes.addFlashAttribute("errorMessage", ibe.getMessage());
            logger.error("Error resetting password for operation user", ibe);
        }
        return "redirect:/core/system/users";
    }
}
