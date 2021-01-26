package com._3line.gravity.freedom.NIBBS.util;

import com._3line.gravity.freedom.NIBBS.dto.AgentDataReportDTO;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class AESHash {

    private static String agentManagerCode = "00038";
    private static String apiKey = "J+1wXN/IekxwxhkR";
    private static String ivKey = "h0qQRklqe0BTV/F1";
    private static String password = "gZCyRYOnc2";



    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(ivKey.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(apiKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string is :: "+ Hex.encodeHexString( encrypted ));
            return Hex.encodeHexString( encrypted );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(ivKey.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(apiKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Hex.decodeHex(encrypted.toCharArray()));
            System.out.println("decrypted string is :: "+new String(original));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String generateAuthHeader(){

        String concString = agentManagerCode+":"+password;
        String encodedString = String.valueOf(Base64.encodeBase64String(concString.getBytes()));
        System.out.println("base64 encoded string :: "+encodedString);
        return "Basic "+encodedString;
    }

    public static String generateSignature(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMdd");
        String concString = agentManagerCode+simpleDateFormat.format(new Date())+password;
        String hashtext;
        System.out.println("SHA256 being generated for "+concString);

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] messageDigest = md.digest(concString.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        }

        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown"
                    + " for incorrect algorithm: " + e);
            return null;
        }


        System.out.println("SHA256  generated :: "+hashtext);
        return hashtext;
    }

}
