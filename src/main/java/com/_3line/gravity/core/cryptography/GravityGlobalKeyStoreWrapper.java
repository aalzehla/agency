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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.io.InputStream;

@Service
public class GravityGlobalKeyStoreWrapper {


    private String path = "" ;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public  SecretKey getKey() throws Exception {

        logger.info("about getting secret key store");

        String fileName = "3LGVTAPI.KEYSTORE";
        String Keyalias = "SecretKeyAlias";
        String fileloadPassword = "com.mooasoft";
        String KeyloadPassword = "3LR@tio12to3Magnum";

        logger.info("loading file now ");

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path+fileName);


        final KeyStore keyStore = KeyStore.getInstance("JCEKS");
        if (inputStream!=null) {
            // .keystore file already exists => load it
            keyStore.load(inputStream, fileloadPassword.toCharArray());
           logger.info("Existing .keystore file loaded!");
        } else {
            // .keystore file not created yet => create it
            throw new FileNotFoundException("File could not be located");
        }

        SecretKey keyFound = (SecretKey) keyStore.getKey(Keyalias, KeyloadPassword.toCharArray());
        return keyFound;
    }
}
