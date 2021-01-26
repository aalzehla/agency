package com._3line.gravity.freedom.utility.isopackager;//$$$Note to Developer$$$
//**Before You Delete this useless hashed out controller class**
//**Please note that this is the sole surviving copy of the class which we were able to generate thanks to IntelliJ decompiler**//
//**Yes the class is not in use but it holds the original code of the servlet.war project which forwards POS terminal requests to TMS.**//
//If you choose to delete consider reconstructing the project from the war file or have a backup//










//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//package com.mooasoft.smartinsure.tms.iso8583.controller;
//
//import com.mooasoft.smartinsure.dao.Dao;
//import com.mooasoft.smartinsure.tms.iso8583.dao.TmsDao;
//import com.mooasoft.smartinsure.tms.packager.PackagerForAriusOnline;
//import com.mooasoft.smartinsure.utility.PropertyResource;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import org.jpos.iso.ISOMsg;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.http.HttpEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@Controller
//public class IsoController2 implements ApplicationContextAware {
//
//    public static ApplicationContext context;
//    private static final Logger logger = LoggerFactory.getLogger("com.mooasoft");
//    TmsDao dao;
//    PropertyResource r = new PropertyResource();
//    ExecutorService executor = Executors.newCachedThreadPool();
//
//    public IsoController2() {
//    }
//
//    public void setApplicationContext(ApplicationContext context) {
//        context = context;
//        this.dao = (Dao) context.getBean("Dao");
//    }
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
//        if (this.dao.isInValidAgent(initiator)) {
//            System.out.println("############### RETURNING ERROR CODE 58");
//            return "081020380100028000189600000000631725120216012335385031353338333246043546303130333133413031303031354B4F4E46414D2D45535553552D303041303230303036323232323232413033303032314B4F4E46414D204553555355204147454E542D3033413034303031353741204944454A4F20535452205649413035303030354C41474F53413036303032345448414E4B20594F5520464F5220594F5552205452555354413037303030382A39392A2A2A3123413038303030382A39392A2A2A31234130393030303330363041304130303033313230413043303031343136303232303137313732353132413044303030363033353230314130453030313631303130413330303039303043383038413046303030334E474E4131303030303430353636413131303030343035363641313230303038303030303030303046434C30303234484144303030312A484356303030313048464C3030303130463032303130384230313030303636333935383742303230303036363339353837423033303031304C334C494E45202020204230343030303133423035303030313142303630303031304230373030303130423038303030313242303930303031304230413030303134423042303030323136000130";
//        } else {
//            return this.logToGravity(isomsg, lat, lon, initiator, beneficiary);
//        }
//    }
//
//    @RequestMapping({"bpp"})
//    @ResponseBody
//    public String bpp(@RequestBody String xml, @RequestParam(defaultValue = "") String lat, @RequestParam(defaultValue = "") String lon, @RequestParam(defaultValue = "") String initiator, @RequestParam(defaultValue = "") String beneficiary) {
//        if (beneficiary.equals("")) {
//            beneficiary = initiator;
//        }
//
//        if (lat.equals("") || lon.equals("")) {
//            lat = "6.427292";
//            lon = "3.412225";
//        }
//
//        if (this.dao.isInValidAgent(initiator)) {
//            System.out.println("############### RETURNING ERROR CODE 58");
//            return "081020380100028000189600000000631725120216012335385031353338333246043546303130333133413031303031354B4F4E46414D2D45535553552D303041303230303036323232323232413033303032314B4F4E46414D204553555355204147454E542D3033413034303031353741204944454A4F20535452205649413035303030354C41474F53413036303032345448414E4B20594F5520464F5220594F5552205452555354413037303030382A39392A2A2A3123413038303030382A39392A2A2A31234130393030303330363041304130303033313230413043303031343136303232303137313732353132413044303030363033353230314130453030313631303130413330303039303043383038413046303030334E474E4131303030303430353636413131303030343035363641313230303038303030303030303046434C30303234484144303030312A484356303030313048464C3030303130463032303130384230313030303636333935383742303230303036363339353837423033303031304C334C494E45202020204230343030303133423035303030313142303630303031304230373030303130423038303030313242303930303031304230413030303134423042303030323136000130";
//        } else {
//            return this.logToGravityBpp(xml, lat, lon, initiator, beneficiary);
//        }
//    }
//
//    @RequestMapping({"rch"})
//    @ResponseBody
//    public String rch(@RequestBody String xml, @RequestParam(defaultValue = "") String lat, @RequestParam(defaultValue = "") String lon, @RequestParam(defaultValue = "") String initiator, @RequestParam(defaultValue = "") String beneficiary) {
//        if (beneficiary.equals("")) {
//            beneficiary = initiator;
//        }
//
//        if (lat.equals("") || lon.equals("")) {
//            lat = "6.427292";
//            lon = "3.412225";
//        }
//
//        if (this.dao.isInValidAgent(initiator)) {
//            System.out.println("############### RETURNING ERROR CODE 58");
//            return "081020380100028000189600000000631725120216012335385031353338333246043546303130333133413031303031354B4F4E46414D2D45535553552D303041303230303036323232323232413033303032314B4F4E46414D204553555355204147454E542D3033413034303031353741204944454A4F20535452205649413035303030354C41474F53413036303032345448414E4B20594F5520464F5220594F5552205452555354413037303030382A39392A2A2A3123413038303030382A39392A2A2A31234130393030303330363041304130303033313230413043303031343136303232303137313732353132413044303030363033353230314130453030313631303130413330303039303043383038413046303030334E474E4131303030303430353636413131303030343035363641313230303038303030303030303046434C30303234484144303030312A484356303030313048464C3030303130463032303130384230313030303636333935383742303230303036363339353837423033303031304C334C494E45202020204230343030303133423035303030313142303630303031304230373030303130423038303030313242303930303031304230413030303134423042303030323136000130";
//        } else {
//            return this.logToGravityRch(xml, lat, lon, initiator, beneficiary);
//        }
//    }
//
//    @RequestMapping({"/iso"})
//    @ResponseBody
//    public String iso(@RequestParam(defaultValue = "") String isomsg, String lat, String lon) {
//        logger.info("request :" + isomsg);
//
//        try {
//            String iso = PackagerForAriusOnline.toString(PackagerForAriusOnline.unpack(isomsg));
//            logger.info("============================REQUEST===========================================");
//            logger.info(iso);
//            logger.info("============================REQUEST FINISHED===========================================");
//            URL site = new URL("http://10.4.4.26:9080/servlet/TransactionDispatcherSingleISO?isomsg=" + isomsg);
//            System.out.println("FORWARDING TO SITE " + site.toString());
//            URLConnection yc = site.openConnection();
//            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
//            StringBuilder response = new StringBuilder();
//
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//
//            in.close();
//            iso = response.toString();
//            logger.info("============================RESPONSE===========================================");
//            logger.info("RESPONSE FROM TMS " + iso);
//            logger.info(PackagerForAriusOnline.toString(PackagerForAriusOnline.unpack(iso)));
//            logger.info("============================RESPONSE FINISHED===========================================");
//            return iso;
//        } catch (Exception var10) {
//            var10.printStackTrace();
//            logger.error("Exception is ", var10);
//            return "exception occured";
//        }
//    }
//
//    @RequestMapping({"/log"})
//    @ResponseBody
//    public String log(HttpEntity<String> httpEntity) {
//        logger.info("request :" + (String) httpEntity.getBody());
//        return (String) httpEntity.getBody();
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
//                this.executor.submit(() -> {
//                    logger.info("request :" + isomsg);
//                    logger.info("============================REQUEST:" + beneficiary + "#===========================================");
//                    logger.info(iso);
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
//                this.executor.submit(() -> {
//                    try {
//                        logger.info("============================RESPONSE===========================================");
//                        logger.info("RESPONSE FROM TMS " + iso);
//                        logger.info(PackagerForAriusOnline.toString(PackagerForAriusOnline.unpack(iso)));
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
//                    this.executor.submit(() -> {
//                        try {
//                            this.dao.logMessage(isomsgObject.getMTI(), isomsgObject.getString(3), respCode, isomsgObject.getString(4), pans, isomsgObject.getString(11), isomsgObject.getString(41), isomsgObject.getString(18), lat, lon, initiator, beneficiary);
//                        } catch (Exception var9) {
//                            var9.printStackTrace();
//                        }
//
//                    });
//                }
//            }
//
//            this.executor.submit(() -> {
//                try {
//                    this.dao.logMessage(isomsgObject.getMTI(), isomsgObject.getString(3), respCode, isomsgObject.getString(4), pans, isomsgObject.getString(11), isomsgObject.getString(41), isomsgObject.getString(18), lat, lon, initiator, beneficiary);
//                } catch (Exception var9) {
//                    var9.printStackTrace();
//                }
//
//            });
//            return "exception occured";
//        }
//
//        this.executor.submit(() -> {
//            try {
//                this.dao.logMessage(isomsgObject.getMTI(), isomsgObject.getString(3), respCode, isomsgObject.getString(4), pans, isomsgObject.getString(11), isomsgObject.getString(41), isomsgObject.getString(18), lat, lon, initiator, beneficiary);
//            } catch (Exception var9) {
//                var9.printStackTrace();
//            }
//
//        });
//        return var18;
//    }
//
//    public String logToGravityBpp(String xml, String lat, String lon, String initiator, String beneficiary) {
//        String respCode = "";
//        boolean var15 = false;
//
//        String var9;
//        label37:
//        {
//            try {
//                var15 = true;
//                this.executor.submit(() -> {
//                    logger.info("============================REQUEST===========================================");
//                    logger.info(xml);
//                    logger.info("============================REQUEST FINISHED===========================================");
//                    System.out.println("FORWARDING TO SITE http://10.2.2.36:4040/api/fcmb/billpayment");
//                });
//                Map<String, String> headers = new HashMap();
//                headers.put("Accept", "application/xml");
//                String responsexml = (new Post()).sendRawPost("http://10.2.2.36:4040/api/fcmb/billpayment", "application/xml", xml, headers);
//                this.executor.submit(() -> {
//                    logger.info("============================RESPONSE===========================================");
//                    logger.info("RESPONSE FROM TMS " + responsexml);
//                    logger.info("============================RESPONSE FINISHED===========================================");
//                });
//                respCode = XMLTags.get(responsexml, "<ResponseCode>");
//                var9 = responsexml;
//                var15 = false;
//                break label37;
//            } catch (Exception var16) {
//                var16.printStackTrace();
//                logger.error("Exception is ", var16);
//                var15 = false;
//            } finally {
//                if (var15) {
//                    this.executor.submit(() -> {
//                        try {
//                            System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t", "0150", "", respCode, XMLTags.get(xml, "<Amount>"), XMLTags.get(xml, "<BillInfo>"), XMLTags.get(xml, "<Stan>"), XMLTags.get(xml, "<TerminalID>"), "", "6.427292", "3.412225");
//                            this.dao.logMessage("0150", "", respCode, XMLTags.get(xml, "<Amount>"), new String[]{XMLTags.get(xml, "<BillInfo>"), null, null}, XMLTags.get(xml, "<Stan>"), XMLTags.get(xml, "<TerminalID>"), "", lat, lon, initiator, beneficiary);
//                        } catch (Exception var8) {
//                            var8.printStackTrace();
//                        }
//
//                    });
//                }
//            }
//
//            this.executor.submit(() -> {
//                try {
//                    System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t", "0150", "", respCode, XMLTags.get(xml, "<Amount>"), XMLTags.get(xml, "<BillInfo>"), XMLTags.get(xml, "<Stan>"), XMLTags.get(xml, "<TerminalID>"), "", "6.427292", "3.412225");
//                    this.dao.logMessage("0150", "", respCode, XMLTags.get(xml, "<Amount>"), new String[]{XMLTags.get(xml, "<BillInfo>"), null, null}, XMLTags.get(xml, "<Stan>"), XMLTags.get(xml, "<TerminalID>"), "", lat, lon, initiator, beneficiary);
//                } catch (Exception var8) {
//                    var8.printStackTrace();
//                }
//
//            });
//            return "exception occured";
//        }
//
//        this.executor.submit(() -> {
//            try {
//                System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t", "0150", "", respCode, XMLTags.get(xml, "<Amount>"), XMLTags.get(xml, "<BillInfo>"), XMLTags.get(xml, "<Stan>"), XMLTags.get(xml, "<TerminalID>"), "", "6.427292", "3.412225");
//                this.dao.logMessage("0150", "", respCode, XMLTags.get(xml, "<Amount>"), new String[]{XMLTags.get(xml, "<BillInfo>"), null, null}, XMLTags.get(xml, "<Stan>"), XMLTags.get(xml, "<TerminalID>"), "", lat, lon, initiator, beneficiary);
//            } catch (Exception var8) {
//                var8.printStackTrace();
//            }
//
//        });
//        return var9;
//    }
//
//    public String logToGravityRch(@RequestBody String xml, String lat, String lon, String initiator, String beneficiary) {
//        String respCode = "";
//        boolean var15 = false;
//
//        String var9;
//        label37:
//        {
//            try {
//                var15 = true;
//                this.executor.submit(() -> {
//                    logger.info("============================REQUEST===========================================");
//                    logger.info(xml);
//                    logger.info("============================REQUEST FINISHED===========================================");
//                    System.out.println("FORWARDING TO SITE http://10.2.2.36:4040/api/fcmb/recharge");
//                });
//                Map<String, String> headers = new HashMap();
//                headers.put("Accept", "application/xml");
//                String responsexml = (new Post()).sendRawPost("http://10.2.2.36:4040/api/fcmb/recharge", "application/xml", xml, headers);
//                this.executor.submit(() -> {
//                    logger.info("============================RESPONSE===========================================");
//                    logger.info("RESPONSE FROM TMS " + responsexml);
//                    logger.info("============================RESPONSE FINISHED===========================================");
//                });
//                respCode = XMLTags.get(responsexml, "<ResponseCode>");
//                var9 = responsexml;
//                var15 = false;
//                break label37;
//            } catch (Exception var16) {
//                var16.printStackTrace();
//                logger.error("Exception is ", var16);
//                var15 = false;
//            } finally {
//                if (var15) {
//                    this.executor.submit(() -> {
//                        try {
//                            this.dao.logMessage("0160", "", respCode, XMLTags.get(xml, "<Amount>"), new String[]{XMLTags.get(xml, "<Pan>"), null, null}, XMLTags.get(xml, "<Stan>"), XMLTags.get(xml, "<TerminalID>"), "", lat, lon, initiator, beneficiary);
//                        } catch (Exception var8) {
//                            var8.printStackTrace();
//                        }
//
//                    });
//                }
//            }
//
//            this.executor.submit(() -> {
//                try {
//                    this.dao.logMessage("0160", "", respCode, XMLTags.get(xml, "<Amount>"), new String[]{XMLTags.get(xml, "<Pan>"), null, null}, XMLTags.get(xml, "<Stan>"), XMLTags.get(xml, "<TerminalID>"), "", lat, lon, initiator, beneficiary);
//                } catch (Exception var8) {
//                    var8.printStackTrace();
//                }
//
//            });
//            return "exception occured";
//        }
//
//        this.executor.submit(() -> {
//            try {
//                this.dao.logMessage("0160", "", respCode, XMLTags.get(xml, "<Amount>"), new String[]{XMLTags.get(xml, "<Pan>"), null, null}, XMLTags.get(xml, "<Stan>"), XMLTags.get(xml, "<TerminalID>"), "", lat, lon, initiator, beneficiary);
//            } catch (Exception var8) {
//                var8.printStackTrace();
//            }
//
//        });
//        return var9;
//    }
//}
