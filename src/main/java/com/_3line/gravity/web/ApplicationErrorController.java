//package com._3line.gravity.web;
//
//
//import com._3line.gravity.core.email.service.MailService;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.boot.web.servlet.error.ErrorAttributes;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.context.request.WebRequest;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//
//
//@Controller
//public class ApplicationErrorController implements ErrorController {
//
//    private static final String PATH = "/error";
//
//    @Autowired
//    private MailService mailService;
//
//    @Autowired
//    private ErrorAttributes errorAttributes;
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//
//    @Value("${dev.members.mail}")
//    private String devMembersMails;
//
//
//    @RequestMapping(value = PATH)
//    public String handleError(WebRequest request) {
//
//        Map<String, Object> errorDetails = errorAttributes.getErrorAttributes(request, true);
//
//        String errorPath = (String) errorDetails.get("path");
//        String statusCode = errorDetails.get("status").toString();
//        Object exception = errorDetails.get("exception");
//
//        logger.error("Error Details: {}",errorDetails.toString());
//        if (exception != null) {
//            logger.info("About sending mail");
//            sendNotification(errorDetails);
//            logger.info("Mail sending should be completed now!!");
//        }
//
//        if("403".equals(statusCode)){
//            return "/error403";
//        }
//
////        if("404".equals(statusCode)){
////            return "/error404";
////        }
//
//        String subPath = StringUtils.substringAfter(errorPath, "/");
//
//        if (subPath != null) {
//            if (subPath.startsWith("core")) {
//                return "error";
//            }
//        }
//
//        return "redirect:/core/login/";
//    }
//
//
//    @Override
//    public String getErrorPath() {
//        return PATH;
//    }
//
//
//    private void sendNotification(Map errorDetails) {
//        String time = errorDetails.get("timestamp").toString();
//        String statusCode = errorDetails.get("status").toString();
//        String error = errorDetails.get("error").toString();
//        String exception = (String) errorDetails.get("exception");
//        String message = errorDetails.get("message").toString();
//        String trace = (String) errorDetails.get("trace");
//        String path = (String) errorDetails.get("path");
//
//        if (!"405".equals(statusCode)) {
//            StringBuilder messageBuilder = new StringBuilder();
//            messageBuilder.append("Time: " + time + "\n")
//                    .append("Path: " + path + "\n")
//                    .append("Status Code: " + statusCode + "\n")
//                    .append("Error: " + error + "\n")
//                    .append("Exception: " + exception + "\n")
//                    .append("Message: " + message + "\n")
//                    .append("Trace: " + trace + "\n");
//            Map<String, Object> params = new HashMap<>();
//            params.put("Time", time);
//            params.put("Path", path);
//            params.put("StatusCode", statusCode);
//            params.put("Error", error);
//            params.put("Exception:",exception);
//            params.put("Message:",message);
//            params.put("Trace:",trace);
//
//            if(devMembersMails!=null) {
//                String[] mailAddresses = StringUtils.split(devMembersMails, ",");
//                mailService.sendMail(error,"softwaredev@3lineng.com",mailAddresses,params,"error_mail");
//
//            }
//        }
//    }
//}
