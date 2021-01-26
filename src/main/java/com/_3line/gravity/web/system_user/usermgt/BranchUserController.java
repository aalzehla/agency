package com._3line.gravity.web.system_user.usermgt;


import com._3line.gravity.core.code.dto.CodeDTO;
import com._3line.gravity.core.code.service.CodeService;
import com._3line.gravity.core.rolemgt.dtos.RoleDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/core/branch/users")
public class BranchUserController {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ApplicationUserService applicationUserService;
    private final RoleService roleService;
    private final CodeService codeService;

    @Autowired
    public BranchUserController(ApplicationUserService applicationUserService, RoleService roleService, CodeService codeService) {
        this.applicationUserService = applicationUserService;
        this.roleService = roleService;
        this.codeService = codeService;
    }

    @ModelAttribute
    public void addRolesToModel(Model model) {

        List<RoleDTO> roles = roleService.getBranchRoles();
        List<CodeDTO> branches = codeService.getCodesByType("BRANCH");
        model.addAttribute("roles", roles);
        model.addAttribute("branches", branches);

    }

    @GetMapping
    public String viewUsers() {

        return "user/branch/view";
    }

    @RequestMapping(value = "/data/all", method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<ApplicationUserDTO> getUsers(@Valid DataTablesInput input, @RequestParam(value = "csearch", required = false) String search) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<ApplicationUserDTO> users;

        if (StringUtils.isNotBlank(search)) {
            users = applicationUserService.findBranchUsers(search, pageable);
        } else {
            users = applicationUserService.getBranchUsers(pageable);
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
        return "user/branch/add";
    }

    @GetMapping("/{id}/edit")
    public String showEditPage(@PathVariable Long id, Model model) {

        ApplicationUserDTO user = applicationUserService.getUser(id);
        model.addAttribute("user", user);
        return "user/branch/edit";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") ApplicationUserDTO applicationUserDTO, RedirectAttributes redirectAttributes, Model model) {


        if(applicationUserDTO.getBranchId() == null){
            logger.error("New branch user has no attached branch");
            model.addAttribute("errorMessage", "Branch is required");
            return "user/branch/add";
        }
        try {
            String message = applicationUserService.createBranchUser(applicationUserDTO);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/branch/users";
        } catch (VerificationException e) {
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/branch/users";
        } catch (AppUserServiceException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "user/branch/add";
        }

    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") ApplicationUserDTO applicationUserDTO, RedirectAttributes redirectAttributes, Model model) {


        if(applicationUserDTO.getBranchId() == null){
            logger.error("Branch user has no attached branch");
            model.addAttribute("errorMessage", "Branch is required");
            return "user/branch/edit";
        }
        try {
            String message = applicationUserService.updateBranchUser(applicationUserDTO);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/branch/users";
        }
        catch (PendingVerificationException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "user/branch/edit";
        }
        catch (VerificationException e) {
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/branch/users";
        }
        catch (AppUserServiceException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "user/branch/edit";
        }

    }

    @GetMapping("{id}/enable")
    public String enableUser(@PathVariable Long id, RedirectAttributes redirectAttributes){

        try {
            String message = applicationUserService.enableBranchUser(id);
            redirectAttributes.addFlashAttribute("successMessage", message);
        }
        catch (PendingVerificationException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        catch (VerificationException e){
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
        }
        catch (AppUserServiceException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        }
        return "redirect:/core/branch/users";
    }

    @GetMapping("{id}/disable")
    public String disableUser(@PathVariable Long id, RedirectAttributes redirectAttributes){

        try {
            String message = applicationUserService.disableBranchUser(id);
            redirectAttributes.addFlashAttribute("successMessage", message);
        }
        catch (PendingVerificationException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        catch (VerificationException e){
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
        }
        catch (AppUserServiceException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        }
        return "redirect:/core/branch/users";
    }


    @GetMapping("{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes){

        try {
            String message = applicationUserService.deleteBranchUser(id);
            redirectAttributes.addFlashAttribute("successMessage", message);
        }
        catch (PendingVerificationException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        catch (VerificationException e){
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
        }
        catch (AppUserServiceException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        }
        return "redirect:/core/branch/users";
    }
}
