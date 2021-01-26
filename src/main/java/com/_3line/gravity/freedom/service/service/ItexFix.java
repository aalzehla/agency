//package com._3line.gravity.freedom.service.service;
//
//import com._3line.gravity.freedom.service.tranupdate.dto.TranFromExcel;
//import com._3line.gravity.freedom.itexintegration.model.PtspDto;
//import com._3line.gravity.freedom.itexintegration.service.PtspService;
//
//import com._3line.gravity.freedom.utility.FileUtility;
//import com.google.gson.Gson;
//import io.github.mapper.excel.ExcelMapper;
//import org.apache.poi.ss.usermodel.DateUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.util.*;
//
//@Service
//public class ItexFix {
//
//    @Autowired
//    private PtspService ptspService ;
//
//    private Gson gson = new Gson();
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    public String fixItex(){
//        logger.info("loading file to update transactions ");
//        List<TranFromExcel> dtos = new ArrayList<>();
//        Map<String ,String> success = new HashMap<>();
//        File file = new File(FileUtility.getClasspathFilesAbsolutePath("25jantrans.xlsx"));
//
//        try{
//            dtos = ExcelMapper.mapFromExcel(file)
//                    .toObjectOf(TranFromExcel.class)
//                    .fromSheet(0) // if this method not used , called all sheets
//                    .map();
//
//            for (TranFromExcel d: dtos) {
//                logger.info("transaction is {}",d.toString());
//                try {
//                    PtspDto ptspDto = new PtspDto() ;
//                    ptspDto.setTerminalId(d.getTerminal());
//                    ptspDto.setRrn(d.getRrn());
//                    ptspDto.setPan(d.getPan());
//                    //added 00 at the back to signify kobo
//                    ptspDto.setAmount(Double.parseDouble(d.getAmount()+"00"));
//                    ptspDto.setReversal("false");
//                    ptspDto.setStan(d.getStan());
//                    ptspDto.setBank("GTBank");
//                    Date javaDate= DateUtil.getJavaDate((Double.parseDouble(d.getTransactionTime())));
//                    ptspDto.setTransactionTime(com._3line.gravity.freedom.utility.DateUtil.formatDateToItexFormat(javaDate));
//                    if(d.getStatus().equals("Approved")){
//                        ptspDto.setStatusCode("00");
//                    }else {
//                        ptspDto.setStatusCode(d.getResponse().substring(0,2));
//                    }
//
//                    if(d.getType().equals("Reversal")){
//                        ptspDto.setReversal("true");
//                    }else {
//                        ptspDto.setReversal("false");
//                    }
//                    logger.info("ptsp dto now looks like {}",ptspDto.toString());
//                    String response  = ptspService.savePtspDetails(ptspDto);
//                    Map<String , String> res = gson.fromJson(response , HashMap.class);
//
//                    if(res.get("respCode").contains("00")){
//                        success.put(d.getRrn() , d.getAmount());
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            logger.info("success {}", success);
//        }catch (Throwable e){
//            e.printStackTrace();
//        }
//        return "done";
//    }
//}
