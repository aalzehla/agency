package com._3line.gravity.freedom.utility;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Utility<T> {

    private static Logger logger = LoggerFactory.getLogger(Utility.class);
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static final int NAIRA = 10;
    public static final int KOBO = 20;

    public static String nextday(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e1) {
            e1.printStackTrace();
            return date;
        }
        c.add(Calendar.DATE, 1); // Adding 1 days

        return sdf.format(c.getTime());

    }

    public static long dateDiff(String earlierdate, Date laterdate) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        earlierdate = sdf.format(sdf.parse(earlierdate));

        Calendar c = Calendar.getInstance();
        c.setTime(laterdate);

        LocalDate startDate = LocalDate.parse(earlierdate);
        LocalDate endtDate = LocalDate.parse(sdf.format(c.getTime()));
        return ChronoUnit.DAYS.between(startDate, endtDate);
    }

    public static Map<String, String> getHeaders(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
//            logger.info("############# HEADERS ###########");
//            logger.info(key + "  : " + value);
            map.put(key, value);

        }

        return map;

    }
    protected static SecureRandom random = new SecureRandom();

    public static synchronized String generateToken( ) {
        long longToken = Math.abs( random.nextLong() );
        String random = Long.toString( longToken, 20 );
        return (  random );
    }

    public static String generateID() {
        Random rnd = new Random();
        char [] digits = new char[11];
        digits[0] = (char) (rnd.nextInt(9) + '1');
        for(int i=1; i<digits.length; i++) {
            digits[i] = (char) (rnd.nextInt(10) + '0');
        }
        return new String(digits);
    }



    public static Map<String, String> getHeaders2(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();
        String headersAsString = "";
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
//            logger.info("############# HEADERS ###########");
//            logger.info(key + "  : " + value);
            map.put(key, value);
            headersAsString = String.format(headersAsString + key + " : " + value + "\n");
            //System.out.println(key+" "+value);
        }
        map.put("HeadersAsString", headersAsString + "\n");

        return map;

    }

    public static String getRequestBody(HttpServletRequest request) {

        StringBuilder body = new StringBuilder();
        String line = "";
        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
                body.append(line);
            }

            return body.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRequestAsString(HttpServletRequest request) {
        return getHeaders2(request).get("HeadersAsString") + getRequestBody(request);
    }

    public static String getId(HttpServletRequest request) throws IllegalAccessException {

        Object id = request.getSession().getAttribute("userid");

        if (id == null) { // if id could not be obtained from session , then user no longer has access
            throw new IllegalAccessException("");
        }
        return id.toString();
    }

    public static String contentOf(String sessVar, HttpServletRequest request) {

        Object id = request.getSession().getAttribute(sessVar);

        if (id == null) { // if id could not be obtained from session , then variable is not set yet
            return "null";
        }
        return id.toString();
    }

    public static String randomString(int len) {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public static String randomNumber(int len) {
        final String AB = "0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public static String maskPan(String val) {

        if (val == null || val.length() < 16) {
            return "";
        }

        char[] chararray = val.toCharArray();
        for (int i = 0; i < chararray.length; i++) {

            if (i >= chararray.length / 4 && i <= chararray.length - 6) {
                chararray[i] = '*';
            }

        }

        val = new String(chararray);

        return val;
    }

    public static String getDayName(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
        return simpleDateformat.format(d);
    }

    public static String getDayName(Date d) {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
        return simpleDateformat.format(d);
    }

    public static String getMonthName(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMM");
        return simpleDateformat.format(d);
    }

    public static String getMonthName(Date d) {

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMM");
        return simpleDateformat.format(d);
    }

    public static String getDay(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("dd");
        return simpleDateformat.format(d);
    }

    public static String getDay(Date d) {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("dd");
        return simpleDateformat.format(d);
    }

    public static String getYear(String date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
        return simpleDateformat.format(d);
    }

    public static String getYear(Date d) {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
        return simpleDateformat.format(d);
    }

    public static String getCurrentDate() {
        return new Date().toString();
    }

    public static String formatDate(String date, String initDateFormat, String endDateFormat) throws ParseException {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }

    public static String getDateTimePlusMinute(int minute) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, minute);
        String date = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss a").format(cal.getTime());
        return date;
    }

    public static String getFormatedCurrentDateTime() {
        String date = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss a").format(Calendar.getInstance().getTime());
        return date;
    }

    public static String sendSMS(String Title, String msg, String... numberlist) throws Exception {
        Title = Title.length() > 11 ? Title.substring(0, 11) : Title;

        Title = URLEncoder.encode(Title, "UTF-8");
        msg = URLEncoder.encode(msg, "UTF-8");

        String numbers = "";
        for (String n : numberlist) {
            numbers += numbers.equals("") ? preparePhoneNumber(n) : "," + preparePhoneNumber(n);
        }
        PropertyResource r = new PropertyResource();
        String Url = r.getV("sms.api.url", "response.properties");
        URL sms = new URL(String.format(Url, numbers, Title, msg));
        URLConnection yc = sms.openConnection();
        //yc.getInputStream(); this actually sends requests the page
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(sms.toString());
        return response.toString();
    }

    /*

    public static String sendSMS(String Title,String msg,String... numberlist) throws InvalidPhoneNumberException,Exception {
            Title = Title.length() > 11 ? Title.substring(0,11):Title;

                /* infobip already does this : avoid double url encoding
                Title = URLEncoder.encode(Title, "UTF-8");
                msg = URLEncoder.encode(msg, "UTF-8");
                        //

                String numbers="";
                for(String n:numberlist){
                numbers += numbers.equals("")?preparePhoneNumber(n):","+preparePhoneNumber(n);
                                         }
                return numbers.indexOf(":")==-1  ?  InfobipSms.sendSms(Title,numbers,msg)   : InfobipSms.sendSmsMultiple(Title,numbers,msg);
	                                                                                                                    }
     */
    public static String preparePhoneNumber(String phonenumber) throws InvalidPhoneNumberException {
        if (phonenumber == null) {
            throw new InvalidPhoneNumberException("Invalid Phone Number");
        }
        phonenumber = phonenumber.trim();
        phonenumber = phonenumber.replace("+", "");
        if (phonenumber.length() == 11) {
            return "234" + phonenumber.substring(1);
        } else if (phonenumber.length() == 13 && phonenumber.startsWith("234")) {
            return phonenumber;
        } else {
            throw new InvalidPhoneNumberException("Invalid Phone Number :" + phonenumber);
        }
    }

    public static boolean isValidPhoneNumber(String phonenumber) {
        if (phonenumber == null) {
            return false;
        }

        phonenumber = phonenumber.trim();
        phonenumber = phonenumber.replace("+", "");

        if (!phonenumber.matches("\\d+")) {
            return false;
        }

        if (phonenumber.length() == 11) {
            return true;
        } else return phonenumber.length() == 13 && phonenumber.startsWith("234");

    }

    public static ArrayList<String[]> orderWith(ArrayList<String[]> System_Components, Collection<Object> moduleComponents) {
        if (moduleComponents == null || moduleComponents.size() == 0) {
            return System_Components;
        }
        ArrayList<String[]> unorderedSystem_Components = new ArrayList<String[]>(System_Components);
        ArrayList<String[]> orderedSystem_Components = new ArrayList<String[]>();

        for (Object urlstring : moduleComponents) {
            int index = firstIndexOf(unorderedSystem_Components, (String) urlstring);
            if (index != -1) {
                orderedSystem_Components.add(0, unorderedSystem_Components.get(index));
                unorderedSystem_Components.remove(index);
            }
        }
        orderedSystem_Components.addAll(unorderedSystem_Components);

        return orderedSystem_Components;
    }

    public static ArrayList<String[]> orderWithM(ArrayList<String[]> System_Components, Collection<String> existingComponent) {
        if (existingComponent == null || existingComponent.size() == 0) {
            return System_Components;
        }
        ArrayList<String[]> unorderedSystem_Components = new ArrayList<String[]>(System_Components);
        ArrayList<String[]> orderedSystem_Components = new ArrayList<String[]>();

        for (String component : existingComponent) {
            int index = firstIndexOf(unorderedSystem_Components, component);
            if (index != -1) {
                orderedSystem_Components.add(0, unorderedSystem_Components.get(index));
                unorderedSystem_Components.remove(index);
            }
        }
        orderedSystem_Components.addAll(unorderedSystem_Components);

        return orderedSystem_Components;
    }

    private static int firstIndexOf(ArrayList<String[]> st, String valtosearch) {
        int index = 0;
        for (String[] vv : st) {
            if (vv[1].equals(valtosearch)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public static List<Map> generatePins(List<String> ids) {
        ArrayList<Map> list = new ArrayList<Map>();
        for (String id : ids) {
            Map idPin = new HashMap();
            idPin.put("id", id);
            idPin.put("pin", randomNumber(4));
            list.add(idPin);
        }

        return list;
    }

    public static List<Map> encryptPins(List<Map> idPinlist) {
        ArrayList<Map> list = new ArrayList<Map>();
        for (Map idpinunencrypted : idPinlist) {
            Map idPin = new HashMap();
            idPin.put("id", idpinunencrypted.get("id"));
            idPin.put("pin", bcrypt((String) idpinunencrypted.get("pin")));
            list.add(idPin);

        }

        return list;
    }

    /*
     public static void sendEmail(final CosmosUser user, final String additionalInfo, final String fromEmailAddress,
     final String subject) {

     Map dtos = new HashMap();
     dtos.put("user", user);
     dtos.put("pass", additionalInfo);



     VelocityEngine velocityEngine = new  VelocityEngine();
     velocityEngine.setProperty("resource.loader", "class");
     velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

     String body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mailtemplate/my-password.html", dtos);

     SendMail mail = new SendMail();
     mail.setAuthentications();
     mail.sendMail(user.getEmail(),subject,body);



     }*/
    public static String getSavingCycle(String saving_cycle, String cycle_metric, String cycle_type) {
        String check = saving_cycle + cycle_metric + cycle_type;
        try {
            if (Integer.parseInt(cycle_type) == 1) {
                switch (check) {
                    case "111":
                        return "ONE_DAY_ANNIVERSARY";
                    case "131":
                        return "ONE_MONTH_ANNIVERSARY";
                    case "231":
                        return "TWO_MONTH_ANNIVERSARY";
                    case "631":
                        return "SIX_MONTH_ANNIVERSARY";
                    case "141":
                        return "ONE_YEAR_ANNIVERSARY";
                    default:
                        return "UNDEFINED";
                }
            }
            if (Integer.parseInt(cycle_type) == 2) {
                switch (check) {
                    case "132":
                        return "END_OF_MONTH_NONANIVERSARY";
                    case "232":
                        return "END_OF_SECONDMONTH_NONANIVERSARY";
                    case "632":
                        return "END_OF_SIXTHMONTH_NONANIVERSARY";
                    case "142":
                        return "END_OF_YEAR_NONANIVERSARY";
                    default:
                        return "UNDEFINED";
                }
            }
        } catch (NumberFormatException e) {
            return "UNDEFINED";
        }
        return "UNDEFINED";
    }

    public static String getCsrfToken(HttpServletRequest request) {
        return ((CsrfToken) request.getAttribute(CsrfToken.class.getName())).getToken();
    }

    public static String removeDot(String value) {
        return (value == null || !value.contains(".")) ? (value == null ? "" : value) : value.substring(0, value.indexOf("."));
    }

    public static String encrypt(String data, String key) { //input data must only be numerical , character in key must exist only once and keylength must > 10
        if (data != null) {
            data = data.trim();
        }

        String coded = "";
        for (int i = 0; i < data.toCharArray().length; i++) {
            coded += key.charAt(Integer.parseInt(data.charAt(i) + ""));
        }
        return coded;
    }

    public static String decrypt(String data, String key) {
        String decoded = "";

        if (data == null) {
            return decoded;
        }

        for (int i = 0; i < data.toCharArray().length; i++) {
            decoded += key.indexOf(data.charAt(i)) + "";
        }
        return decoded;
    }

    public static String strip(String toRemove, String from) {
        if (from == null) {
            return "";
        }
        if (toRemove == null || toRemove.equals("")) {
            return from;
        }
        int endindex = from.indexOf(toRemove);
        return from.substring(0, (endindex == -1 ? from.length() : endindex));
    }

    public static String hexToAscii(String hex) {
        if (hex == null || hex.equals("")) {
            return "";
        }
        if (hex.length() % 2 != 0) {
            System.err.println("requires EVEN number of chars");
            return "";
        }
        //String hex = "75546f7272656e745c436f6d706c657465645c6e667375635f6f73745f62795f6d757374616e675c50656e64756c756d2d392c303030204d696c65732e6d7033006d7033006d7033004472756d202620426173730050656e64756c756d00496e2053696c69636f00496e2053696c69636f2a3b2a0050656e64756c756d0050656e64756c756d496e2053696c69636f303038004472756d2026204261737350656e64756c756d496e2053696c69636f30303800392c303030204d696c6573203c4d757374616e673e50656e64756c756d496e2053696c69636f3030380050656e64756c756d50656e64756c756d496e2053696c69636f303038004d50330000";
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public static String bytesToHex_2(byte[] bytes) { //this fails to produce same hex as php
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String bytesToHex(byte[] raw) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < raw.length; i++) {
            sb.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static String asciiToHex(String ascii) {
        StringBuilder hex = new StringBuilder();

        for (int i = 0; i < ascii.length(); i++) {
            hex.append(Integer.toHexString(ascii.charAt(i)));
        }
        return hex.toString();
    }

    public static String generateDESkey() {
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("DESede");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        keyGen.init(168); // key length 112 for two keys, 168 for three keys
        SecretKey secretKey = keyGen.generateKey();
        return bytesToHex(secretKey.getEncoded());
    }

    public static String bcrypt(String pwd) {
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        String hashed = passwordEncoder.encode(pwd);
        return hashed;
        // just like PBKDF2WithHmacSHA1 the hashes for a unique string are not exactly the to test for equality use bellow
        // passwordEncoder.matches("hashedfromdb", hashed); 
    }

    public static String sha512(String args) {

        try {
            // Create MessageDigest instance for MD5
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-512");
            //Add password bytes to digest
            md.update(args.getBytes(StandardCharsets.UTF_8));
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format

            return byteArrayToHexString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "error";
    }

    public static byte[] sha512Byte(String args) {

        try {
            // Create MessageDigest instance for MD5
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-512");
            //Add password bytes to digest
            md.update(args.getBytes(StandardCharsets.UTF_8));
            //Get the hash's bytes
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //Get complete hashed password in hex format
        return sb.toString();
    }

    public static synchronized double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String formatexpo(double v) //Got here 6.743240136E7 or something..
    {
        DecimalFormat formatter;

        double value = round(v , 2);

        if(value - (int)value > 0.0)
            formatter = new DecimalFormat("0.00"); //Here you can also deal with rounding if you wish..
        else
            formatter = new DecimalFormat("0");

        return formatter.format(value);
    }

    public static String parseAmount(Double amount) {
        String formattedString = "";
        try {
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,##0.00");
            formattedString = formatter.format(amount);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return formattedString;
    }

    public static String parseAmount(String amount, int type) {
        String formattedString = "";
        try {
            Double value = Double.parseDouble(amount);
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,##0.00");
            if (type == KOBO) {
                value /= 100;
            }
            formattedString = formatter.format(value);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return formattedString;
    }

    public static String formatForView(double value){

        if(Objects.isNull(value)) throw new IllegalArgumentException();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        return decimalFormat.format(round(value , 2));
    }
    public static List<String []> getStringsFromObjectList(List<Object []> info){
        List<String[]> data = new ArrayList<>();
        for (int i = 0; i < info.size(); i++) {
            Object[] obj = info.get(i);
            if(obj==null){
                continue;
            }
            String[] stringArray = new String[obj.length];
            for (int j = 0; j < obj.length; j++) {
                String value = String.valueOf(obj[j]);
                stringArray[j] = value;
            }
            data.add(stringArray);

        }

        return  data ;
    }

}
