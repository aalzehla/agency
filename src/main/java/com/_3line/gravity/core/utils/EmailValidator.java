package com._3line.gravity.core.utils;


import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Created by Fortune on 9/22/2018.
 */


public class EmailValidator {

    public static boolean isValid(String email) {

        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }

    public static boolean isEmail(String text) {
       return isValid(text);
    }


    public static boolean isValid(String email, String domainName) {

        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            String domainPart = StringUtils.substringAfter(email, "@");
            if (domainPart.equalsIgnoreCase(domainName)) {
                return true;
            }
        } catch (AddressException e) {
            return false;
        }
        return false;
    }




}

