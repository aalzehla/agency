package com._3line.gravity.core.utils;



import java.util.Random;

public class CharacterUtil {

    public  static String generateId(int length){
        String AB = "ABCDEFGHJKLMNPQRSTUVWXYZ123456789123456789" ;
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public  static String generateOtp(int length){
        String AB = "123456789" ;
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}
