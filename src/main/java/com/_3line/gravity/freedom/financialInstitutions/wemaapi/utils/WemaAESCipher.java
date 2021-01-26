package com._3line.gravity.freedom.financialInstitutions.wemaapi.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author JoyU
 * @date 9/19/2018
 */

@Service
public class WemaAESCipher {

    private static final Logger logger = LoggerFactory.getLogger(WemaAESCipher.class);

    /** A 128-bit key used for encryption */
    @Value("${wema.encrpytion.key}")
    private String KEY; //TODO store a secured file location

    /** A 16-byte initialization vector. */
    @Value("${wema.encrpytion.iv}")
    private String INIT_VECTOR;


    /**
     * Returns an encrypted text of the provided plain text
     *
     * @param plainText the text to be encrypted
     * @return an encrypted text
     */
    public String encrypt(String plainText){
        logger.debug("Starting encryption");

        String encryptedText = null;

        try {
            if(plainText != null || !plainText.isEmpty()) {

                IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
                SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

                byte[] encrypted = cipher.doFinal(plainText.getBytes());
                encryptedText = Base64.encodeBase64String(encrypted);
            }
        }
        catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return encryptedText;
    }


    /**
     * Returns a plain text of the provided encrypted text
     *
     * @param encryptedText the encrypted text
     * @return a plain text
     */
    public String decrypt(String encryptedText){

        logger.debug("Starting decryption");

        String plainText = null;

        try {

                IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
                SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

                byte[] original = cipher.doFinal(Base64.decodeBase64(encryptedText.getBytes()));
                plainText = new String(original);

        }
        catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return plainText;
    }

}
