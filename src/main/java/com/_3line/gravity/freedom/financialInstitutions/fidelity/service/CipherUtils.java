package com._3line.gravity.freedom.financialInstitutions.fidelity.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class CipherUtils {

    @Value("${fidelityapi.encryptionKey}")
    private String encryptionKey;

    @Value("${fidelityapi.secret}")
    private String apiSecret;

    public String encrypt(String strToEncrypt) {
        strToEncrypt = strToEncrypt.trim();
        try {
            final SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey.getBytes(), "DESede");
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            final String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
            return encryptedString;

        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(CipherUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public String decrypt(String strToDecrypt) {
        strToDecrypt = strToDecrypt.trim();
        try {
            final SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey.getBytes(), "DESede");
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
            return decryptedString;
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            Logger.getLogger(CipherUtils.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public String encode(String strToEncode) {
        strToEncode = strToEncode.trim();
        try {
            SecretKeySpec signingKey = new SecretKeySpec(Base64.decodeBase64(strToEncode), "HmacSHA256");
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            sha256_HMAC.init(signingKey);
            byte[] rawHmac = sha256_HMAC.doFinal(apiSecret.getBytes(StandardCharsets.UTF_8));
            strToEncode = Base64.encodeBase64String(rawHmac);
            return strToEncode;
        } catch (IllegalStateException | InvalidKeyException | NoSuchAlgorithmException e) {
            Logger.getLogger(CipherUtils.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public String padRight(String original, int padToLength, char padWith) {

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
