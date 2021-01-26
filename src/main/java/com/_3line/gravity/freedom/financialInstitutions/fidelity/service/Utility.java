package com._3line.gravity.freedom.financialInstitutions.fidelity.service;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class Utility implements Constant {

    private static final Logger logger = LoggerFactory.getLogger(Utility.class);

    private static String ENCRYPTION_KEY = "78c33bdd75b45cc9";
    private static byte[] key = ENCRYPTION_KEY.getBytes();

    private static String encrypt(String strToEncrypt) {
        try {
            final SecretKeySpec secretKey = new SecretKeySpec(key, "DESede");
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            final String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private static String decrypt(String strToDecrypt) {
        try {
            final SecretKeySpec secretKey = new SecretKeySpec(key, "DESede");
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
            return decryptedString;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public static String encode(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), ENCRYPTION_KEY);
        sha256_HMAC.init(secret_key);

        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    public static String padRight(String original, int padToLength, char padWith) {
        if (original.length() >= padToLength) {
            return original;
        }
        StringBuilder sb = new StringBuilder(padToLength);
        sb.append(original);
        for (int i = original.length(); i < padToLength; ++i) {
            sb.append(padWith);
        }
        return sb.toString();
    }

}
