package com._3line.gravity.freedom.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

    static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd");
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    static SimpleDateFormat dateField7 = new SimpleDateFormat("MMddHHmmss");
    static SimpleDateFormat dateField12 = new SimpleDateFormat("HHmmss");
    static SimpleDateFormat transRef = new SimpleDateFormat("yyyyMMddHHmmss");
    static SimpleDateFormat timeStamp = new SimpleDateFormat("yyyyMMddHHmmss");

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public static String date(Date date){

        return dateFormat.format(date);
    }

    public static String requestID(Date date){

        return dateField7.format(date);
    }

    public static String valueDate(Date date){

        return dateFormat.format(date);
    }

    public static String timeStamp(Date date){

        return timeStamp.format(date);
    }

    public static String tranRef(Date date){

        return transRef.format(date);
    }

    public static String responseField7(String date) {
        Date dateResponse = new Date();
        try {
             dateResponse = dateFormat.parse(date);
            System.out.println("DATE IS @@@@@@ {}" + dateResponse);
        }catch (ParseException e){

        }
        return format.format(dateResponse);
    }

    public static String accopening(String date) {
        String newDate = "";
        try {
            Date dateResponse = dateFormat.parse(date);
            System.out.println("DATE IS @@@@@@ {}" + dateResponse);
            newDate = format.format(dateResponse);
        }catch (ParseException e){
        }
        return newDate;
    }
}
