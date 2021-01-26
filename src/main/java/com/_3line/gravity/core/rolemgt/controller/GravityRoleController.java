//package com._3line.gravity.core.rolemgt.controller;
//
//import com._3line.app.exceptions.GravityException;
//import com._3line.app.gravity.entities.Audit;
//import com._3line.app.gravity.model.GravityResponse;
//import com._3line.app.gravity.model.LoggedInUser;
//import com._3line.app.gravity.repositories.AuditRepository;
//import com._3line.app.gravityroles.dtos.RoleDTO;
//import com._3line.app.gravityroles.model.GravityRole;
//import com._3line.app.gravityroles.service.GravityRoleService;
//import com._3line.app.gravityroles.service.PermissionService;
//import com._3line.app.rolemanagement.service.RoleManagementService;
//import com._3line.app.service.PaginationService;
//import com._3line.app.utility.MessageSource;
//import com._3line.app.utility.PropertyResource;
//import com._3line.app.utility.Utility;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
//import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
//import org.springframework.data.jpa.datatables.repository.DataTablesUtils;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//import java.util.Date;
//
///**
// * @author Utosu Joy
// */
//
//@Controller
//@RequestMapping(value = "/gravityrole")
//public class GravityRoleController {
//
//    @Autowired
//    private GravityRoleService gravityRoleService;
//
//    @Autowired
//    private PermissionService permissionService;
//
//    @Autowired
//    private RoleManagementService service;
//
//    @Autowired
//    private AuditRepository auditRepository;
//
//    Logger logger = LoggerFactory.getLogger(this.getClass()) ;
//
//    private PaginationService paginationService;
//
//    private static PropertyResource pr = new PropertyResource();
//
//    @RequestMapping(value = "/allgravityroles")
//    @ResponseBody
//    public DataTablesOutput<RoleDTO> getClosedStatus(DataTablesInput input) {
//        System.out.println("This is the gravity role controller");
//        Pageable pageable = DataTablesUtils.getPageable(input);
//        Page<RoleDTO> gravityRoleDTOPage = gravityRoleService.getGravityRoleNotDefault(pageable);
//        DataTablesOutput<RoleDTO> out = new DataTablesOutput<RoleDTO>();
//        out.setDraw(input.getDraw());
//        out.setData(gravityRoleDTOPage.getContent());
//        out.setRecordsFiltered(gravityRoleDTOPage.getTotalElements());
//        out.setRecordsTotal(gravityRoleDTOPage.getTotalElements());
//        return out;
//    }
//
//    @RequestMapping(value = "/creategravityrole", method = RequestMethod.GET)
//    @ResponseBody
//    public GravityResponse createGravityRole(HttpServletRequest request) {
//        GravityResponse DTOmodel = new GravityResponse(true, pr.getV("path.creategravityrole", "angular.properties"), GravityResponse.SUCCESS_CODE, GravityResponse.SUCCESS_DESC);
//        DTOmodel.addToModel("csrf", Utility.getCsrfToken(request));
//        DTOmodel.addToModel("Permissions", permissionService.findAllPermissions());
//        DTOmodel.addToModel("roletype", service.getRoleTypes());
//        return DTOmodel;
//    }
//
//    @RequestMapping(value = "/creategravityrole", method = RequestMethod.POST)
//    @ResponseBody
//    public GravityResponse createGravityRole(@Valid @ModelAttribute("gravityrole") RoleDTO gravityRoleDTO, BindingResult errorStack, HttpServletRequest request) {
//        String status = "0";
//        GravityResponse DTOmodel = new GravityResponse(true, pr.getV("path.creategravityrole", "angular.properties"), GravityResponse.SUCCESS_CODE, GravityResponse.SUCCESS_DESC);
//        DTOmodel.addToModel("csrf", Utility.getCsrfToken(request));
//        if (!errorStack.hasErrors()) {
//            try {
//                logger.info("These are Permissions {} ", gravityRoleDTO.getRoleString());
//                logger.info("size is {}", gravityRoleDTO.toString());
//                gravityRoleService.createRole(gravityRoleDTO);
//                DTOmodel.setSuccessMessage(MessageSource.getMessage("rolecreationsuccess"));
//                status = "1";
//            } catch (GravityException ex) {
//                ex.printStackTrace();
//                DTOmodel.setErrorMessage(ex.getMessage());
//            }
//        } else {
//            errorStack.getAllErrors().forEach(err -> {
//                logger.info("##### Create ####" + err.getDefaultMessage() + "##########" + err.toString());
//            });
//            DTOmodel.setRespCode(GravityResponse.FAILURE_CODE);
//            DTOmodel.setRespDescription(GravityResponse.FAILURE_DESC);
//            DTOmodel.setErrorMessage(MessageSource.getMessage("bindingerror"));
//        }
//        LoggedInUser currentUser = Utility.currentUser();
//        service.auditLog(currentUser.getUsername(), "ROLE CREATION", "User " + currentUser.getUsername() + " attempted creating gravityroleDTO  " + gravityRoleDTO.getName(), status, currentUser.getUsername(), null, null);
//        return DTOmodel;
//    }
//
////    @RequestMapping(value = "/allgravityroles")
////    @ResponseBody
////    public DataTableResponse allGravityRoles(@RequestParam Map<String, Object> param) {
////        return paginationService.PageRoles(param);
////    }
//
//    @RequestMapping(value = "/editgravityrole", method = RequestMethod.GET)
//    @ResponseBody
//    public GravityResponse editGravityRole(HttpServletRequest request, @RequestParam(value = "roleId", required = true) Long roletoeditid) {
//        GravityResponse DTOmodel = new GravityResponse(false, null, GravityResponse.SUCCESS_CODE, GravityResponse.SUCCESS_DESC);
//        DTOmodel.addToModel("csrf", Utility.getCsrfToken(request));
//        DTOmodel.addToModel("roletype", service.getRoleTypes());
//        try {
//            RoleDTO roleDTO = gravityRoleService.getEditRole(roletoeditid);
//            DTOmodel.addToModel("roleDTO", roleDTO);
//            DTOmodel.addToModel("Permissions", roleDTO.getRolePermissions());
//        } catch (GravityException ex) {
//            DTOmodel.setRespCode(GravityResponse.FAILURE_CODE);
//            DTOmodel.setRespDescription(GravityResponse.FAILURE_DESC);
//            DTOmodel.setErrorMessage(ex.getMessage());
//        }
//        return DTOmodel;
//    }
//
//
//    @RequestMapping(value = "/editgravityrole", method = RequestMethod.POST)
//    @ResponseBody
//    public GravityResponse editGravityRole(@Valid @ModelAttribute("gravityrole") RoleDTO gravityRoleDTO, BindingResult errorStack, HttpServletRequest request) {
//        GravityResponse DTOmodel = new GravityResponse(true, pr.getV("path.editgravityrole", "angular.properties"), GravityResponse.SUCCESS_CODE, GravityResponse.SUCCESS_DESC);
//        DTOmodel.addToModel("csrf", Utility.getCsrfToken(request));
//        String status = "0";
//        GravityRole gravityRole = gravityRoleService.findOne(gravityRoleDTO.getRoleId());
//
//        if (!errorStack.hasErrors()) { // when editting doenot have binding error
//            try {
//                gravityRoleService.updateRole(gravityRoleDTO);
//                DTOmodel.setSuccessMessage(MessageSource.getMessage("roleeditsuccess"));
//                status = "1";
//            } catch (GravityException ex) {
//                DTOmodel.setErrorMessage(ex.getMessage());
//            }
//        } else {
//            errorStack.getAllErrors().forEach(err -> {
//                logger.info("##### Editt ####" + err.getDefaultMessage() + "###########################" + err.toString());
//            });
//            DTOmodel.setRespCode(GravityResponse.FAILURE_CODE);
//            DTOmodel.setRespDescription(GravityResponse.FAILURE_DESC);
//            DTOmodel.setErrorMessage(MessageSource.getMessage("bindingerror"));
//        }
//
//        LoggedInUser currentUser = Utility.currentUser();
//        Audit audit = new Audit();
//        audit.setSourceUsername(currentUser.getUsername());
//        audit.setOperation("ROLE MODIFICATION");
//        audit.setDescription("User " + currentUser.getUsername() + " attempted editting role with id " + gravityRoleDTO.getRoleId()+ " attempted editting role  " + gravityRoleDTO.getName());
//        audit.setStatus(Integer.parseInt(status));
//        audit.setRecepientUsername(currentUser.getUsername());
//        audit.setEventtime(new Date());
//        audit.setNewValues(gravityRoleDTO.toString());
//        audit.setOldValues(gravityRole.toString());
//        auditRepository.save(audit);
//        return DTOmodel;
//
//    }
//
//    @RequestMapping(value = "/deleterole", method = RequestMethod.POST)
//    @ResponseBody
//    public GravityResponse deleteRole(@RequestParam(value = "roleid", required = false) String roletodeleteid, HttpServletRequest request) {
//        String status = "0";
//        GravityResponse DTOmodel = new GravityResponse(false, null, GravityResponse.SUCCESS_CODE, GravityResponse.SUCCESS_DESC);
//        DTOmodel.addToModel("csrf", Utility.getCsrfToken(request));
//        try {
//            Long roleId = Long.parseLong(roletodeleteid);
//            gravityRoleService.deleteRole(roleId);
//            status = "1";
//            DTOmodel.setSuccessMessage(MessageSource.getMessage("roledeletesuccess"));
//        } catch (GravityException ex) {
//            DTOmodel.setErrorMessage(ex.getMessage());
//        }
//
//        LoggedInUser currentUser = Utility.currentUser();
//        service.auditLog(currentUser.getUsername(), "ROLE DELETION", "User " + currentUser.getUsername() + " attempted deleteing role  " + roletodeleteid.split("=")[1], status, currentUser.getUsername(), null, null);
//        return DTOmodel;
//
//    }
//
//
////    @RequestMapping(value = "/assignedRoles", method = RequestMethod.GET)
////    @ResponseBody
////    public ResponseEntity assignedRoles() {
////        List<GravityRole> assignedRoles = service.assignedRoles();
////        return ResponseEntity.ok(assignedRoles);
////    }
//
//
//}
