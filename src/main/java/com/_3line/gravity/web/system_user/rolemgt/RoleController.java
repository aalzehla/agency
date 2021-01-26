package com._3line.gravity.web.system_user.rolemgt;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.rolemgt.dtos.PermissionDTO;
import com._3line.gravity.core.rolemgt.dtos.RoleDTO;
import com._3line.gravity.core.rolemgt.models.PermissionType;
import com._3line.gravity.core.rolemgt.models.RoleType;
import com._3line.gravity.core.rolemgt.service.RoleService;
import com._3line.gravity.core.usermgt.model.SystemUser;
import com._3line.gravity.core.verification.dtos.VerifiableActionDto;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.core.verification.models.VerifiableAction;
import com._3line.gravity.core.verification.service.VerifiableActionService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/core/roles")
public class RoleController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RoleService roleService;
    private final VerifiableActionService verifiableActionService ;
    private final MessageSource messageSource;

    @Autowired
    public RoleController(RoleService roleService, VerifiableActionService verifiableActionService, MessageSource messageSource) {
        this.roleService = roleService;
        this.messageSource = messageSource;
        this.verifiableActionService = verifiableActionService ;
    }

    @GetMapping("/new")
    public String addRole(Model model) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setType("_3LINE");
        roleDTO.setPermissions(new ArrayList<>());
        model.addAttribute("actions", verifiableActionService.findAll());
        model.addAttribute("role", roleDTO);
        return "rolemgt/add";
    }


    @GetMapping("/permissions")
    public String gerAllPermissions() {
        return "permission/view";
    }

    @ModelAttribute
    public void roleTypes(Model model){
        model.addAttribute("roleTypes", RoleType.values());

    }

    @ModelAttribute("permissions")
    public Iterable<PermissionDTO> getPermissions(NativeWebRequest request) {
        HttpServletRequest httpServletRequest = request.getNativeRequest(HttpServletRequest.class);
        Map<String, String> map = (Map<String, String>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long reqId = null;
        try {
            reqId = NumberUtils.createLong(map.get("reqId"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        Iterable<PermissionDTO> permissions;
        if (reqId != null) {
            RoleDTO role = roleService.getRole(reqId);
            permissions = roleService.getPermissionsNotInRole(role);
        } else {
            permissions = roleService.getPermissions();
        }

        return permissions;
    }

    @GetMapping("/permissions/{id}/edit")
    public String editPermission(@PathVariable Long id, Model model){

        PermissionDTO permission = roleService.getPermission(id);
        model.addAttribute("permissionTypes", PermissionType.values());
        model.addAttribute("permission", permission);

        return "permission/edit";
    }

    @PostMapping("/permissions/update")
    public String processPermission(@Valid @ModelAttribute("permission") PermissionDTO permission, RedirectAttributes redirectAttributes, Model model) {

        try {
            String message = roleService.updatePermission(permission);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/roles/permissions";
        }
        catch (VerificationException e){
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/roles/permissions";
        }
        catch (Exception e) {
            model.addAttribute("permissionTypes", PermissionType.values());
            model.addAttribute("errorMessage", e.getMessage());
            return "permission/edit";
        }
    }


    @GetMapping("/{roleId}")
    public String getRole(@PathVariable Long roleId, Model model) {
        RoleDTO role = roleService.getRole(roleId);
        model.addAttribute("role", role);
        return "rolemgt/edit";
    }

    @GetMapping
    public String getRoles(Model model) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setType("_3LINE");
        roleDTO.setPermissions(new ArrayList<>());
        model.addAttribute("role", roleDTO);
        return "rolemgt/view";
    }

    @GetMapping("/{reqId}/edit")
    public String editRole(@PathVariable Long reqId, Model model) {
        RoleDTO role = roleService.getRole(reqId);
        Iterable<PermissionDTO> permissionDTOs = roleService.getRole(reqId).getPermissions();
        List<VerifiableActionDto> actions = verifiableActionService.findAll() ;
        List<VerifiableActionDto> toView =  verifiableActionService.findAll();
        role.getApprovables().forEach( ap->{
            logger.info("-- {}", ap);
            for (VerifiableActionDto a: actions) {
                if(a.getCode().equals(ap)){
                    logger.info("removing {}", ap);
                    toView.remove(a);
                }
            }
        });
        model.addAttribute("actions", toView);
        model.addAttribute("role", role);
        return "rolemgt/edit";
    }

    @GetMapping("/{reqId}/view")
    public String viewRole(@PathVariable Long reqId, Model model) {
        RoleDTO role = roleService.getRole(reqId);
        model.addAttribute("role", role);
        return "rolemgt/details";
    }

    @GetMapping(path = "/{roleId}/users")
    public
    @ResponseBody
    DataTablesOutput<SystemUser> getUsers(@PathVariable Long roleId, DataTablesInput input) {
        RoleDTO role = roleService.getRole(roleId);
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<SystemUser> users = roleService.getPersons(role, pageable);
        DataTablesOutput<SystemUser> out = new DataTablesOutput<SystemUser>();
        out.setDraw(input.getDraw());
        out.setData(users.getContent());
        out.setRecordsFiltered(users.getTotalElements());
        out.setRecordsTotal(users.getTotalElements());
        return out;
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<RoleDTO> getRoles(@Valid DataTablesInput input, @RequestParam(value = "csearch", required = false) String search) {

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<RoleDTO> roles;
        if (StringUtils.isNotBlank(search)) {
            roles = roleService.findRoles(search, pageable);
        } else {
            roles = roleService.getRoles(pageable);
        }
        DataTablesOutput<RoleDTO> out = new DataTablesOutput<RoleDTO>();
        out.setDraw(input.getDraw());
        out.setData(roles.getContent());
        out.setRecordsFiltered(roles.getTotalElements());
        out.setRecordsTotal(roles.getTotalElements());
        return out;
    }


    @RequestMapping(value = "/permissions/all", method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<PermissionDTO> getAllPermissions(@Valid DataTablesInput input, @RequestParam(value = "csearch", required = false) String search) {

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<PermissionDTO> permissions = null;
        if (StringUtils.isNotBlank(search)) {
        	permissions = roleService.findPermissions(search,pageable);
		}else{
        permissions = roleService.getPermissions(pageable);
		}
        DataTablesOutput<PermissionDTO> out = new DataTablesOutput<PermissionDTO>();
        out.setDraw(input.getDraw());
        out.setData(permissions.getContent());
        out.setRecordsFiltered(permissions.getTotalElements());
        out.setRecordsTotal(permissions.getTotalElements());
        return out;
    }

    @PostMapping
    public String createRole(@ModelAttribute("role") @Valid RoleDTO roleDTO, BindingResult result, WebRequest request, Model model, RedirectAttributes redirectAttributes, Locale locale) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "rolemgt/add";
        }
        logger.info("Role {}", roleDTO.toString());
        List<PermissionDTO> permissionList = new ArrayList<>();
        Set<String> approvals = new HashSet<>();

        String[] permissions = request.getParameterValues("permissionsList");
        String[] actions = request.getParameterValues("approval_actions");

        if(actions != null){
            for (String a:actions) {
                approvals.add(a);
            }
        }
        if (permissions != null) {
            for (String perm : permissions) {
                PermissionDTO pdto = new PermissionDTO();
                pdto.setId(NumberUtils.toLong(perm));
                permissionList.add(pdto);
            }
        }
        roleDTO.setPermissions(permissionList);
       roleDTO.setApprovables(approvals);
        try {
            String message = roleService.addRole(roleDTO);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/roles";
        }
        catch (VerificationException e){
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/roles";
        }
        catch (GravityException ge) {
            model.addAttribute("errorMessage", ge.getMessage());
            logger.error(ge.getMessage());
            return "rolemgt/add";
        }
    }

    @PostMapping("/update")
    public String updateRole(@ModelAttribute("role") @Valid RoleDTO roleDTO, Model model, BindingResult result, WebRequest request, RedirectAttributes redirectAttributes, Locale locale) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "rolemgt/view";
        }
        logger.info("Role {}", roleDTO.toString());
        List<PermissionDTO> permissionList = new ArrayList<>();
        Set<String> approvals = new HashSet<>();

        String[] permissions = request.getParameterValues("permissionsList");
        String[] actions = request.getParameterValues("approval_actions");

        if(actions != null){
            for (String a:actions) {
                approvals.add(a);
            }
        }
        if (permissions != null) {
            for (String perm : permissions) {
                PermissionDTO pdto = new PermissionDTO();
                pdto.setId(NumberUtils.toLong(perm));
                permissionList.add(pdto);
            }
        }
        roleDTO.setPermissions(permissionList);
        roleDTO.setApprovables(approvals);
        try {
            String message = roleService.updateRole(roleDTO);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/roles";
        }
        catch (VerificationException e){
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/roles";
        }
        catch (GravityException ge) {
            model.addAttribute("errorMessage", ge.getMessage());
            logger.error(ge.getMessage());
            return "rolemgt/view";
        }
    }

    @GetMapping("/{roleId}/delete")
    public String deleteRole(@PathVariable Long roleId, RedirectAttributes redirectAttributes, Locale locale) {
        try {
            String message = roleService.deleteRole(roleId);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/roles";
        }
        catch (VerificationException e){
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/roles";
        }
        catch (GravityException ge) {
            logger.error(ge.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", ge.getMessage());
            return "redirect:/application/roles";

        }
    }
}
