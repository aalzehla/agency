package com._3line.gravity.freedom.utility;//package com._3line.app.utility;
//
//
//import com._3line.app.utility.isopackager.AriusPackager;
//import org.jpos.iso.ISOBasePackager;
//import org.jpos.iso.ISOException;
//import org.jpos.iso.ISOMsg;
//import org.jpos.iso.ISOUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class PackagerForAriusOnline {
//
//    private static ISOBasePackager packager = new AriusPackager();
//    private static Logger logger = LoggerFactory.getLogger(PackagerForAriusOnline.class) ;
//
//
//    private ISOMsg isoMsg;
//
//    public PackagerForAriusOnline() {
//        isoMsg = new ISOMsg();
//        isoMsg.setPackager(packager);
//    }
//    public PackagerForAriusOnline(ISOMsg isoMsg) {
//        this.isoMsg = isoMsg;
//        isoMsg.setPackager(packager);
//                                               }
//    public void setField(int i, String data) throws ISOException {
//        getIsoMsg().set(i, data);
//    }
//
//    public String buildISOMessage() throws Exception {
//        try {
//            byte[] result = getIsoMsg().pack();
//            //return bytesToHex(result);
//            return ISOUtil.hexString(result);
//        } catch (ISOException e) {
//            throw new Exception(e);
//        }
//    }
//
//    public static ISOMsg unpack(String hex) throws ISOException {
//        byte[] b = ISOUtil.hex2byte(hex.getBytes(), 0, hex.length() / 2);
//        ISOMsg isoMsg = new ISOMsg();
//        isoMsg.setPackager(packager);
//        isoMsg.unpack(b);
//        return isoMsg;
//    }
//
//    public void printISOMessage() {
//        try {
//            logger.info("MTI = %s%n", getIsoMsg().getMTI());
//            for (int i = 1; i <= getIsoMsg().getMaxField(); i++) {
//                if (getIsoMsg().hasField(i)) {
//                    logger.info("Field (%s) = %s%n", i, getIsoMsg().getString(i));
//                }
//            }
//        } catch (ISOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void printISOMessage(ISOMsg isoMsg) {
//        try {
//            logger.info("MTI = %s%n", isoMsg.getMTI());
//            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
//                if (isoMsg.hasField(i)) {
//                    logger.info("Field (%s) = %s%n", i, isoMsg.getString(i));
//                }
//            }
//        } catch (ISOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static String toString(ISOMsg isoMsg) {
//        try {
//            StringBuilder b = new StringBuilder("");
//            b.append(String.format("MTI = %s%n", isoMsg.getMTI()));
//            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
//                if (isoMsg.hasField(i)) {
//                    b.append(String.format("Field (%s) = %s%n", i, isoMsg.getString(i)));
//                }
//            }
//
//            return b.toString();
//        } catch (ISOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//
//
//
//final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
//public static String bytesToHex(byte[] bytes) {
//    char[] hexChars = new char[bytes.length * 2];
//    for ( int j = 0; j < bytes.length; j++ ) {
//        int v = bytes[j] & 0xFF;
//        hexChars[j * 2] = hexArray[v >>> 4];
//        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//    }
//    return new String(hexChars);
//}
//
//    public static String int2HexZeroPad(int i) {
//        String hex = Integer.toHexString(i);
//        if (hex.length() % 2 != 0) {
//            hex = "0" + hex;
//        }
//        return hex;
//    }
//    private static String zeroPad(String valToPad, int fixedlength, String padChar) {
//        String pad = "";
//        for (int i= 0 ; i< fixedlength - valToPad.length() ; i++)
//             pad += padChar;
//        return pad + valToPad;
//                                                                               }
//
//    /**
//     * @return the isoMsg
//     */
//    public ISOMsg getIsoMsg() {
//        return isoMsg;
//    }
//
//    /**
//     * @param isoMsg the isoMsg to set
//     */
//    public void setIsoMsg(ISOMsg isoMsg) {
//        this.isoMsg = isoMsg;
//    }
//}
