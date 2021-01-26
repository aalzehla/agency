package com._3line.gravity.freedom.financialInstitutions.wemaapi.utils;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DateFormatter {

    static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
    public static SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
    static SimpleDateFormat wemaDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    public static SimpleDateFormat wemaPayloadDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat customFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat customFormatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat agentOnbardDateFormat = new SimpleDateFormat("dd-MM-yy");


    public static String format(Date date){

        return dateFormatter.format(date);
    }

    public static String formatDate(String date) throws ParseException {

        Date date1= newDateFormat.parse(date);
        System.out.println("DATE IS @@@@@@ {}"+ date1);

        return dateFormat.format(date1);
    }

    public static String wemaFormat(Date date){

        return wemaDateFormat.format(date);
    }

    public static String wemaPayloadDate(String date) {
        Date date1 = new Date();
        try {
            date1 = wemaPayloadDateFormat.parse(date);
            System.out.println("DATE IS @@@@@@ {}" + date1);
        }catch (ParseException e){

        }
        return wemaPayloadDateFormat.format(date1);

    }

}
