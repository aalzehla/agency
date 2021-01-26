package com._3line.gravity.freedom.utility.isopackager;//package com._3line.app.utility.isopackager;
//
//import org.jpos.iso.ISOException;
//import org.jpos.iso.ISOMsg;
//import org.jpos.iso.ISOPackager;
//import org.jpos.iso.ISOUtil;
//
//public class Packager {
//
//    //private static PostPackager packager = new PostPackager();
////    private static ISO87APackagerBBitmap packager = new ISO87APackagerBBitmap();//ARIUSONLINE AND TMS USE ISO87BPackager
//    private ISOPackager iSOPackager;
//    private ISOMsg isoMsg;
//
//    public Packager(ISOPackager iSOPackager) {
//        this.isoMsg = new ISOMsg();
//        this.isoMsg.setPackager(iSOPackager);
//        this.iSOPackager = iSOPackager;
//    }
//
//    public ISOPackager getiSOPackager() {
//        return iSOPackager;
//    }
//
//    public void setiSOPackager(ISOPackager iSOPackager) {
//        this.iSOPackager = iSOPackager;
//    }
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
//
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
//    public ISOMsg unpack(String hex) throws ISOException {
//        byte[] b = ISOUtil.hex2byte(hex.getBytes(), 0, hex.length() / 2);
//        ISOMsg isoMsg = new ISOMsg();
//        isoMsg.setPackager(iSOPackager);
//        isoMsg.unpack(b);
//        return isoMsg;
//    }
//
//    public void printISOMessage() {
//        try {
//            System.out.printf("MTI = %s%n", getIsoMsg().getMTI());
//            for (int i = 1; i <= getIsoMsg().getMaxField(); i++) {
//                if (getIsoMsg().hasField(i)) {
//                    System.out.printf("Field (%s) = %s%n", i, getIsoMsg().getString(i));
//                }
//            }
//        } catch (ISOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void printISOMessage(ISOMsg isoMsg) {
//        try {
//            System.out.printf("MTI = %s%n", isoMsg.getMTI());
//            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
//                if (isoMsg.hasField(i)) {
//                    System.out.printf("Field (%s) = %s%n", i, isoMsg.getString(i));
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
//    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
//
//    public static String bytesToHex(byte[] bytes) {
//        char[] hexChars = new char[bytes.length * 2];
//        for (int j = 0; j < bytes.length; j++) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = hexArray[v >>> 4];
//            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//        }
//        return new String(hexChars);
//    }
//
//    public static String int2HexZeroPad(int i) {
//        String hex = Integer.toHexString(i);
//        if (hex.length() % 2 != 0) {
//            hex = "0" + hex;
//        }
//        return hex;
//    }
//
//    public static String zeroPad(String valToPad, int fixedlength, String padChar) {
//        String pad = "";
//        for (int i = 0; i < fixedlength - valToPad.length(); i++) {
//            pad += padChar;
//        }
//        return pad + valToPad;
//    }
//
//    public static String removeLeadingZeros(String str) {
//        if (str == null) {
//            return null;
//        }
//        char[] chars = str.toCharArray();
//        int index = 0;
//        for (; index < str.length(); index++) {
//            if (chars[index] != '0') {
//                break;
//            }
//        }
//        return (index == 0) ? str : str.substring(index);
//    }
//
//    private static String setMessageLength(String messageHex) {
//        // odredjivanje duzine poruke
//        int duzinaPoruke = messageHex.length() / 2;
//        // Odredjivanje header-a u duzini od dva bajta prema ARKSYS-u v2.2
//        String duzinaPorukeHex = Integer.toString(duzinaPoruke, 16);
//        while (duzinaPorukeHex.length() < 4) {
//            duzinaPorukeHex = "0" + duzinaPorukeHex;
//        }
//
//        return duzinaPorukeHex.toUpperCase() + messageHex;
//
//    }
//}
