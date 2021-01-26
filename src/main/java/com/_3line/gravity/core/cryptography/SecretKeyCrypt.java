/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.core.cryptography;

/**
 *
 * @author OlalekanW
 */

import com._3line.gravity.core.cryptography.exceptions.CryptographyException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giulio
 */

@Service
public class SecretKeyCrypt implements InitializingBean {

    @Autowired
    private GravityGlobalKeyStoreWrapper keyStoreWrapper ;

    private  Key key ;
     public  String encrypt(String message) throws CryptographyException {
        // Get a cipher object.
        String base64;
        try {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            // Gets the raw bytes to encrypt, UTF8 is needed for
            // having a standard character set
            byte[] stringBytes = message.getBytes("UTF8");

            // encrypt using the cypher
            byte[] raw = cipher.doFinal(stringBytes);

            base64 = Base64.encodeBase64String(raw);
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException | UnsupportedEncodingException | NoSuchPaddingException e) {
            throw new CryptographyException(e);
        }
        // converts to base64 for easier display.

        return base64;
    }

    public  String decrypt(String encrypted) throws CryptographyException {
        String clear;
        // Get a cipher object.
        try {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);

            // decode the BASE64 coded string message
            byte[] raw = Base64.decodeBase64(encrypted);

            //decode the message
            byte[] stringBytes = cipher.doFinal(raw);

            //converts the decoded message to a String
            clear = new String(stringBytes, "UTF8");
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException | UnsupportedEncodingException | NoSuchPaddingException e) {
            throw new CryptographyException(e);
        }
        return clear;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws CryptographyException {
        //new SecretKeyCrypt("this is a test crypt");
        System.out.println( new SecretKeyCrypt().encrypt("kUuvqVXvU8lVDBG4fKtSjxg87bpzOFCo3v2qIMoYYyQ="));
//        System.out.println("now" + new SecretKeyCrypt().decrypt("cO/UgceIXM9v0gKCTE1dQRUHR5V0iIGPfFXifdPcs5V1mi/rcjqKYPsYBngGVr1XpHhDsc1LxztlL5k9o6luAx8RzrHWBCgYjTKZxLN784gy7hyW1in2NfneFN4I7fKkBIdo3x9VlZkIs5ELD76OlA=="));
    }

    private  Key fromKeyStore() {
        try {
            return keyStoreWrapper.getKey();
        } catch (Exception ex) {
            Logger.getLogger(SecretKeyCrypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        key = fromKeyStore() ;
    }
}
