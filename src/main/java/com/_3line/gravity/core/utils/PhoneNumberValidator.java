package com._3line.gravity.core.utils;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fortune on 9/22/2018.
 */

public class PhoneNumberValidator {

    public static boolean  isValid(String phoneNumber) {

        String regex = "^\\+?[0-9 ]{11,15}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();

    }

}

