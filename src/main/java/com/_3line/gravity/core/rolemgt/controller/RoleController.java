//package com._3line.gravity.core.rolemgt.controller;
//
//import com._3line.gravity.core.exceptions.GravityException;
//import com._3line.gravity.core.rolemgt.dtos.PermissionDTO;
//import com._3line.gravity.core.rolemgt.dtos.RoleDTO;
//import com._3line.gravity.core.rolemgt.service.RoleService;
//import org.springframework.data.domain.Page;
//import org.springframework.data.jpa.datatables.SpecificationBuilder;
//import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.math.NumberUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.HandlerMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//import java.util.*;
//
///**
// * @author FortunatusE
// * @date 11/6/2018
// */
//
//
//@Controller
//@RequestMapping("application/roles")
//public class RoleController {
//
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//    @Autowired
//    private RoleService roleService;
//
//    @Autowired
//    private MessageSource messageSource;
//
//    @GetMapping("/add")
//    public String addRole(Model model) {
//        RoleDTO roleDTO = new RoleDTO();
//        roleDTO.setPermissions(new ArrayList<>());
//        model.addAttribute("role", new RoleDTO());
//        return "role/add";
//    }
//
//    @ModelAttribute("permissions")
//    public Iterable<PermissionDTO> getPermissions(NativeWebRequest request) {
//        HttpServletRequest httpServletRequest = request.getNativeRequest(HttpServletRequest.class);
//        Map<String, String> map = (Map<String, String>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
//        Long reqId = null;
//        try {
//            reqId = NumberUtils.createLong(map.get("reqId"));
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//        Iterable<PermissionDTO> permissions;
//        if (reqId != null) {
//            RoleDTO role = roleService.getRole(reqId);
//            permissions = roleService.getPermissionsNotInRole(role);
//        } else {
//            permissions = roleService.getPermissions();
//        }
//        return permissions;
//    }
//
//
//    @GetMapping("/{roleId}")
//    public String getRole(@PathVariable Long roleId, Model model) {
//        RoleDTO role = roleService.getRole(roleId);
//        model.addAttribute("role", role);
//        return "role/edit";
//    }
//
//    @GetMapping
//    public String getRoles(Model model) {
//        return "role/view";
//    }
//
//    @GetMapping("/{reqId}/edit")
//    public String editRole(@PathVariable Long reqId, Model model) {
//        RoleDTO role = roleService.getRole(reqId);
//        Iterable<PermissionDTO> permissionDTOs = roleService.getRole(reqId).getPermissions();
//        model.addAttribute("role", role);
////        model.addAttribute("permissions",role);
//        return "role/edit";
//    }
//
//    @GetMapping("/{reqId}/view")
//    public String viewRole(@PathVariable Long reqId, Model model) {
//        RoleDTO role = roleService.getRole(reqId);
//        model.addAttribute("role", role);
//        return "role/details";
//    }
//
////    @GetMapping(path = "/{roleId}/users")
////    public
////    @ResponseBody
////    DataTablesOutput<User> getSystemUsers(@PathVariable Long roleId, DataTablesInput input) {
////        RoleDTO role = roleService.getRole(roleId);
////        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
////        Page<User> users = roleService.getSystemUsers(role, pageable);
////        DataTablesOutput<User> out = new DataTablesOutput<User>();
////        out.setDraw(input.getDraw());
////        out.setData(users.getContent());
////        out.setRecordsFiltered(users.getTotalElements());
////        out.setRecordsTotal(users.getTotalElements());
////        return out;
////    }
//
//    @GetMapping(path = "/all")
//    public
//    @ResponseBody
//    DataTablesOutput<RoleDTO> getRoles(DataTablesInput input, @RequestParam("csearch") String search) {
//
//        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
//        Page<RoleDTO> roles = null;
//        if (StringUtils.isNotBlank(search)) {
//            roles = roleService.findRoles(search, pageable);
//        } else {
//            roles = roleService.getRoles(pageable);
//        }
//        DataTablesOutput<RoleDTO> out = new DataTablesOutput<RoleDTO>();
//        out.setDraw(input.getDraw());
//        out.setData(roles.getContent());
//        out.setRecordsFiltered(roles.getTotalElements());
//        out.setRecordsTotal(roles.getTotalElements());
//        return out;
//    }
//
//    @PostMapping
//    public String createRole(@ModelAttribute("role") @Valid RoleDTO roleDTO, BindingResult result, WebRequest request, RedirectAttributes redirectAttributes, Locale locale) {
//        if (result.hasErrors()) {
//            result.addError(new ObjectError("invalid", messageSource.getMessage("form.fields.required", null, locale)));
//            return "role/add";
//        }
//        logger.info("Role {}", roleDTO.toString());
//        List<PermissionDTO> permissionList = new ArrayList<>();
//
//        String[] permissions = request.getParameterValues("permissionsList");
//        if (permissions != null) {
//            for (String perm : permissions) {
//                PermissionDTO pdto = new PermissionDTO();
//                pdto.setId(NumberUtils.toLong(perm));
//                permissionList.add(pdto);
//            }
//        }
//        roleDTO.setPermissions(permissionList);
//        try {
//            String message = roleService.addRole(roleDTO);
//            redirectAttributes.addFlashAttribute("message", message);
//            return "redirect:/application/roles";
//        }  catch (GravityException ibe) {
//            result.addError(new ObjectError("error", messageSource.getMessage("role.add.failure", null, locale)));
//            logger.error("Error creating role", ibe);
//            return "role/add";
//        }
//    }
//
//    @PostMapping("/update")
//    public String updateRole(@ModelAttribute("role") @Valid RoleDTO roleDTO, BindingResult result, WebRequest request, RedirectAttributes redirectAttributes, Locale locale) {
//        if (result.hasErrors()) {
//            result.addError(new ObjectError("invalid", messageSource.getMessage("form.fields.required", null, locale)));
//            return "role/edit";
//        }
//        logger.info("Role {}", roleDTO.toString());
//        List<PermissionDTO> permissionList = new ArrayList<>();
//
//        String[] permissions = request.getParameterValues("permissionsList");
//        if (permissions != null) {
//            for (String perm : permissions) {
//                PermissionDTO pdto = new PermissionDTO();
//                pdto.setId(NumberUtils.toLong(perm));
//                permissionList.add(pdto);
//            }
//        }
//        roleDTO.setPermissions(permissionList);
//        try {
//            String message = roleService.updateRole(roleDTO);
//            redirectAttributes.addFlashAttribute("message", message);
//            return "redirect:/application/roles";
//        }  catch (GravityException ge) {
//            result.addError(new ObjectError("error", ge.getMessage()));
//            logger.error("Error updating role", ge);
//            return "role/edit";
//        }
//    }
//
//    @GetMapping("/{roleId}/delete")
//    public String deleteRole(@PathVariable Long roleId, RedirectAttributes redirectAttributes, Locale locale) {
//        try {
//            String message = roleService.deleteRole(roleId);
//            redirectAttributes.addFlashAttribute("message", message);
//            return "redirect:/application/roles";
//        } catch (GravityException ibe) {
//            logger.error("Error deleting role", ibe);
//            redirectAttributes.addFlashAttribute("failure", ibe.getMessage());
//            return "redirect:/application/roles";
//
//        }
//    }
//
//}
