//package com._3line.gravity.freedom.itexintegration.controller;
//
//
//import com._3line.gravity.core.exceptions.GravityException;
//import com._3line.gravity.freedom.itexintegration.model.PtspDto;
//import com._3line.gravity.freedom.itexintegration.service.PtspService;
//import com.google.gson.Gson;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping(value = "/gravity/api/itex")
//public class PtspController {
//
//
//    @Autowired
//    private PtspService ptspService ;
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @PostMapping(value = "/save")
//    public String savePtspDetails(@RequestBody PtspDto ptspDto){
//        String message = "";
//        logger.info("This is The PTSP Controller");
//        try{
//            logger.info("request {}",ptspDto.toString());
//            ptspDto.setPtsp("Itex");
//            if(ptspDto.getAmount()==null || ptspDto.getAmount()<=0 || ptspDto.getTerminalId()==null || ptspDto.getTerminalId().equals("")
//            || ptspDto.getRrn()==null || ptspDto.getRrn().equals("") || ptspDto.getReversal()==null || ptspDto.getReversal().equals("")
//            || ptspDto.getTransactionTime()==null || ptspDto.getTransactionTime().equals("")){
//                message = "Bad Request Invalid Data Sent ";
//                Map res = new HashMap();
//                res.put("respDesc",message);
//                res.put("respCode","96");
//                return new Gson().toJson(res);
//            }else{
//
//                try{
//                    message = ptspService.savePtspDetails(ptspDto,"PTSP");
//                    return message;
//                }catch(Exception e){
//                    e.printStackTrace();
//                    throw new GravityException("Error occurred processing notification "+e.getMessage());
//                }
//
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//            logger.error("Error In Controller {} ", e.getMessage());
//            Map res = new HashMap();
//            res.put("respDesc",e.getMessage());
//            res.put("respCode","96");
//            message =  new Gson().toJson(res);
//        }
//        return message;
//    }
//
//}
