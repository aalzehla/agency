package com._3line.gravity.core.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Random;

public class AppUtility {

    public static String randomNumber(int len) {
        final String AB = "0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
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

    public static String getCurrentUserName() {
        try {
            String principal =  SecurityContextHolder.getContext().getAuthentication()
                    .getName();
            return principal;
        }catch (Exception d){
            return "SYSTEM" ;
        }
    }
}
