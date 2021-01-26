//package com._3line.gravity.core.rolemgt.controller;
//
//        import com._3line.app.exceptions.GravityException;
//        import com._3line.app.gravity.model.GravityResponse;
//        import com._3line.app.gravity.model.LoggedInUser;
//        import com._3line.app.gravityroles.dtos.PermissionDTO;
//        import com._3line.app.gravityroles.service.PermissionService;
//        import com._3line.app.rolemanagement.service.RoleManagementService;
//        import com._3line.app.service.PaginationService;
//        import com._3line.app.utility.MessageSource;
//        import com._3line.app.utility.PropertyResource;
//        import com._3line.app.utility.Utility;
//        import org.slf4j.Logger;
//        import org.slf4j.LoggerFactory;
//        import org.springframework.beans.factory.annotation.Autowired;
//        import org.springframework.stereotype.Controller;
//        import org.springframework.validation.BindingResult;
//        import org.springframework.web.bind.annotation.*;
//
//        import javax.servlet.http.HttpServletRequest;
//        import javax.validation.Valid;
//
///**
// * @author Utosu Joy
// */
//
//@Controller
//@RequestMapping(value = "/permission")
//public class PermissionController {
//
//    @Autowired
//    private PermissionService permissionService;
//
//    Logger logger = LoggerFactory.getLogger(this.getClass()) ;
//    private RoleManagementService service;
//    private PaginationService paginationService;
//
//    private static PropertyResource pr = new PropertyResource();
//
////    @RequestMapping(value = "/create", method = RequestMethod.GET)
////    @ResponseBody
////    public GravityResponse createPermission(HttpServletRequest request) {
////        GravityResponse DTOmodel = new GravityResponse(true, pr.getV("path.create", "angular.properties"), GravityResponse.SUCCESS_CODE, GravityResponse.SUCCESS_DESC);
////        DTOmodel.addToModel("csrf", Utility.getCsrfToken(request));
//////        DTOmodel.addToModel("System_Components", service.getSystemComponents());
//////        DTOmodel.addToModel("roletype", service.getRoleTypes());
////        return DTOmodel;
////    }
//
//
//    @RequestMapping(value = "/create", method = RequestMethod.POST)
//    @ResponseBody
//    public GravityResponse createPermission(@Valid @ModelAttribute("permission") PermissionDTO permissionDTO, BindingResult errorStack, HttpServletRequest request) {
//        String status = "0";
//        GravityResponse DTOmodel = new GravityResponse(true, pr.getV("path.create", "angular.properties"), GravityResponse.SUCCESS_CODE, GravityResponse.SUCCESS_DESC);
//        DTOmodel.addToModel("csrf", Utility.getCsrfToken(request));
//        if (!errorStack.hasErrors()) {
//            try {
//                permissionService.createPermission(permissionDTO);
//                logger.info("In Permission controller");
//                DTOmodel.setSuccessMessage(MessageSource.getMessage("permissioncreationsuccess"));
//                status = "1";
//            } catch (GravityException ex) {
//                DTOmodel.setErrorMessage(ex.getMessage());
//            }
//        } else {
//            errorStack.getAllErrors().forEach(err -> {
//                logger.info("#######" + err.getDefaultMessage() + "#########" + err.toString());
//            });
//            DTOmodel.setRespCode(GravityResponse.FAILURE_CODE);
//            DTOmodel.setRespDescription(GravityResponse.FAILURE_DESC);
//            DTOmodel.setErrorMessage(MessageSource.getMessage("bindingerror"));
//        }
//        LoggedInUser currentUser = Utility.currentUser();
//        service.auditLog(currentUser.getUsername(), "PERMISSION CREATION", "User " + currentUser.getUsername() + " attempted creating Gravity Permission  " + permissionDTO.getName(), status, currentUser.getUsername(), null, null);
//        return DTOmodel;
//    }
//
//
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ResponseBody
//    public GravityResponse deletePermission(@RequestParam(value = "sn", required = false) Long sn, HttpServletRequest request) {
//        String status = "0";
//        GravityResponse DTOmodel = new GravityResponse(false, null, GravityResponse.SUCCESS_CODE, GravityResponse.SUCCESS_DESC);
//        DTOmodel.addToModel("csrf", Utility.getCsrfToken(request));
//        try {
//            permissionService.deletePermission(sn);
//            status = "1";
//            DTOmodel.setSuccessMessage(MessageSource.getMessage("permissiondeletesuccess"));
//        } catch (GravityException ex) {
//            DTOmodel.setErrorMessage(ex.getMessage());
//        }
//
//        LoggedInUser currentUser = Utility.currentUser();
//        service.auditLog(currentUser.getUsername(), "PERMISSION DELETE", "User " + currentUser.getUsername() + " attempted deleting Gravity Permission  " , status, currentUser.getUsername(), null, null);
//        return DTOmodel;
//
//    }
//
//
//}
