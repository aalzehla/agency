/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.utility;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author OlalekanW
 */
public class Hash {

    public Hash() {

    }

    public static String generateMD5(String message) throws Exception {
        return hashString(message, "MD5");
    }

    public static String generateSHA1(String message) throws Exception {
        return hashString(message, "SHA-1");
    }
    public static byte[] generateSHA1byte(String message) throws Exception {
        return hashByte(message, "SHA-1");
    }
    public static String generateSHA256(String message) throws Exception {
        return hashString(message, "SHA-256");
    }
    public static byte[] generateSHA256byte(String message) throws Exception {
        return hashByte(message, "SHA-256");
    }
    public static String generateSHA512(String message) throws Exception {
        return hashString(message, "SHA-512");
    }
    public static byte[] generateSHA512byte(String message) throws Exception {
        return hashByte(message, "SHA-512");
    }
    
    public static String hashString(String message, String algorithm)
            throws Exception {

        try {
            //just four step
            MessageDigest digest = MessageDigest.getInstance(algorithm); // 1 .get digest instance with specified algorithm 
            byte[] byteTobeHashed = message.getBytes("UTF-8");           // 2 .to get the byte you want to hash
            byte[] hashedBytes = digest.digest(byteTobeHashed);          // 3 .call the digest function on the MessageDigest instance to return the Hashed byte
            return bytesToHex(hashedBytes);                              // 4 .covert the byte to hex string
            // Note : for file or large byte add the  bytes to hash to the MessageDigest instance with the update method then call the digest() method with no argument 

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }
    
    public static byte [] hashByte(String message, String algorithm)
            throws Exception {

        try {
            //just four step
            MessageDigest digest = MessageDigest.getInstance(algorithm); // 1 .get digest instance with specified algorithm 
            byte[] byteTobeHashed = message.getBytes("UTF-8");           // 2 .to get the byte you want to hash
            return digest.digest(byteTobeHashed);                        // 3 .call the digest function on the MessageDigest instance to return the Hashed byte
                                                                          
            // Note : for file or large byte add the  bytes to hash to the MessageDigest instance with the update method then call the digest() method with no argument 

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }    

    private static String bytesToHex2(byte[] bytes) { //this produces differrent hex
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static String bytesToHex(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}
