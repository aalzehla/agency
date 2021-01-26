package com._3line.gravity.freedom.utility.isopackager;///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com._3line.limitmanager.tms.iso8583.controller;
//
//import com.mooasoft.smartinsure.utility.PropertyResource;
//import com.mooasoft.tms.iso8583.model.Constants;
//import com.mooasoft.smartinsure.tms.iso8583.dao.TmsDao;
//import com.mooasoft.smartinsure.tms.packager.PackagerForAriusOnline;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import org.jpos.iso.ISOMsg;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.context.ApplicationContext;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
///**
// *
// * @author OlalekanW
// */
//@Controller
//@RequestMapping(value = "/api")
//public class IsoController {
//
//    public static ApplicationContext context;
//    private static final Logger logger = LoggerFactory.getLogger("com.mooasoft");
//    private static final PropertyResource r = new PropertyResource();
//    private static final ExecutorService executor = Executors.newCachedThreadPool();
//
//    @Autowired
//    TmsDao tmsDao;
//
//    
//    @RequestMapping({"TransactionDispatcherSingleISO"})
//    @ResponseBody
//    public String tms(@RequestParam(defaultValue = "") String isomsg, @RequestParam(defaultValue = "") String lat, @RequestParam(defaultValue = "") String lon, @RequestParam(defaultValue = "") String initiator, @RequestParam(defaultValue = "") String beneficiary) {
//        if (beneficiary.equals("")) {
//            beneficiary = initiator;
//        }
//
//        if (lat.equals("") || lon.equals("")) {
//            lat = "6.427292";
//            lon = "3.412225";
//        }
//
//        if (this.tmsDao.isInValidAgent(initiator)) {
//            System.out.println("############### RETURNING ERROR CODE 58");
//            return "081020380100028000189600000000631725120216012335385031353338333246043546303130333133413031303031354B4F4E46414D2D45535553552D303041303230303036323232323232413033303032314B4F4E46414D204553555355204147454E542D3033413034303031353741204944454A4F20535452205649413035303030354C41474F53413036303032345448414E4B20594F5520464F5220594F5552205452555354413037303030382A39392A2A2A3123413038303030382A39392A2A2A31234130393030303330363041304130303033313230413043303031343136303232303137313732353132413044303030363033353230314130453030313631303130413330303039303043383038413046303030334E474E4131303030303430353636413131303030343035363641313230303038303030303030303046434C30303234484144303030312A484356303030313048464C3030303130463032303130384230313030303636333935383742303230303036363339353837423033303031304C334C494E45202020204230343030303133423035303030313142303630303031304230373030303130423038303030313242303930303031304230413030303134423042303030323136000130";
//        } else {
//            return this.logToGravity(isomsg, lat, lon, initiator, beneficiary);
//        }
//    }
//
//    public String logToGravity(@RequestParam(defaultValue = "") String isomsg, String lat, String lon, String initiator, String beneficiary) {
//        ISOMsg isomsgObject = null;
//        String respCode = "";
//        String[] pans = new String[3];
//        boolean var26 = false;
//
//        String var18;
//        label64:
//        {
//            try {
//                var26 = true;
//                isomsgObject = PackagerForAriusOnline.unpack(isomsg);
//                String iso = PackagerForAriusOnline.toString(isomsgObject);
//                pans[0] = isomsgObject.getString(2);
//                pans[1] = isomsgObject.getString(35);
//                pans[1] = pans[1] != null ? pans[1].split("=")[0] : pans[1];
//                pans[2] = isomsgObject.getString(63);
//                URL site = new URL("http://10.4.4.25:9080/servlet/TransactionDispatcherSingleISO?isomsg=" + isomsg);
//                final String executorIso = iso;
//                this.executor.submit(() -> {
//                    logger.info("request :" + isomsg);
//                    logger.info("============================REQUEST:" + beneficiary + "#===========================================");
//                    logger.info(executorIso);
//                    logger.info("============================REQUEST FINISHED===========================================");
//                    System.out.println("FORWARDING TO SITE " + site.toString());
//                });
//                URLConnection yc = site.openConnection();
//                BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
//                StringBuilder response = new StringBuilder();
//
//                String inputLine;
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//
//                in.close();
//                iso = response.toString();
//                ISOMsg msg = PackagerForAriusOnline.unpack(iso);
//                respCode = msg.getString(39);
//                final String executorIso2 = iso;
//                this.executor.submit(() -> {
//                    try {
//                        logger.info("============================RESPONSE===========================================");
//                        logger.info("RESPONSE FROM TMS " + executorIso2);
//                        logger.info(PackagerForAriusOnline.toString(PackagerForAriusOnline.unpack(executorIso2)));
//                        logger.info("============================RESPONSE FINISHED===========================================");
//                    } catch (Exception var2) {
//                        var2.printStackTrace();
//                    }
//
//                });
//                var18 = iso;
//                var26 = false;
//                break label64;
//            } catch (Exception var27) {
//                var27.printStackTrace();
//                logger.error("Exception is ", var27);
//                var26 = false;
//            } finally {
//                if (var26) {
//                    final ISOMsg exeIsomsgObject = isomsgObject;
//                    final String exeRespCode = respCode;
//                    this.executor.submit(() -> {
//                        try {
//                            this.tmsDao.logMessage(exeIsomsgObject.getMTI(), exeIsomsgObject.getString(3), exeRespCode, exeIsomsgObject.getString(4), pans, exeIsomsgObject.getString(11), exeIsomsgObject.getString(41), exeIsomsgObject.getString(18), lat, lon, initiator, beneficiary);
//                        } catch (Exception var9) {
//                            var9.printStackTrace();
//                        }
//
//                    });
//                }
//            }
//
//            final ISOMsg exeIsomsgObject = isomsgObject;
//            final String exeRespCode = respCode;
//            this.executor.submit(() -> {
//                try {
//                    this.tmsDao.logMessage(exeIsomsgObject.getMTI(), exeIsomsgObject.getString(3), exeRespCode, exeIsomsgObject.getString(4), pans, exeIsomsgObject.getString(11), exeIsomsgObject.getString(41), exeIsomsgObject.getString(18), lat, lon, initiator, beneficiary);
//                } catch (Exception var9) {
//                    var9.printStackTrace();
//                }
//
//            });
//            return "exception occured";
//        }
//
//        final ISOMsg exeIsomsgObject = isomsgObject;
//        final String exeRespCode = respCode;
//        this.executor.submit(() -> {
//            try {
//                this.tmsDao.logMessage(exeIsomsgObject.getMTI(), exeIsomsgObject.getString(3), exeRespCode, exeIsomsgObject.getString(4), pans, exeIsomsgObject.getString(11), exeIsomsgObject.getString(41), exeIsomsgObject.getString(18), lat, lon, initiator, beneficiary);
//            } catch (Exception var9) {
//                var9.printStackTrace();
//            }
//
//        });
//        return var18;
//    }
//
//}