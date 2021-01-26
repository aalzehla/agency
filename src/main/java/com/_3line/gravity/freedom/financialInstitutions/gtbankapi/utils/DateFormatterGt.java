package com._3line.gravity.freedom.financialInstitutions.gtbankapi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatterGt {

    static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
    static SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
    static SimpleDateFormat gtDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
    static SimpleDateFormat gtPayloadDateFormat =new SimpleDateFormat("MM/dd/yyyy");

    public static String format(Date date){

        return dateFormatter.format(date);
    }

    public static String formatDate(String date) throws ParseException {

        Date date1= newDateFormat.parse(date);
        System.out.println("DATE IS @@@@@@ {}"+ date1);

        return dateFormat.format(date1);
    }

    public static String gtFormat(Date date){

        return gtDateFormat.format(date);
    }

    public static String gtPayloadDate(String date){
        Date date1 = new Date();
        try {
            date1 = gtPayloadDateFormat.parse(date);
            System.out.println("DATE IS @@@@@@ {}" + date1);
        }catch (ParseException e){

        }

        return gtPayloadDateFormat.format(date1);
    }

}
