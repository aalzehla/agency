/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.core.cryptography.rsa;

/**
 *
 * @author OlalekanW
 */

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAKeyPairTest {
 
    private final String privateKeyPathName = "C://temp//private.key";
    private final String publicKeyPathName = "C://temp//public.key";
     

    public void testToFileSystem()
            throws Exception {
 
        try {
             
            RSAKeyPair rsaKeyPair = new RSAKeyPair(2048);
            rsaKeyPair.toFileSystem(privateKeyPathName, publicKeyPathName);
 
            KeyFactory rsaKeyFactory = KeyFactory.getInstance("RSA");
 
            System.out.println(rsaKeyPair.getPrivateKey() ==  null);
            System.out.println(rsaKeyPair.getPublicKey()  ==  null);
            System.out.println(rsaKeyPair.getPrivateKey().equals( rsaKeyFactory.generatePrivate(new PKCS8EncodedKeySpec(IOUtils.toByteArray(new FileInputStream(privateKeyPathName))))));
            System.out.println(rsaKeyPair.getPublicKey().equals( rsaKeyFactory.generatePublic(new X509EncodedKeySpec(IOUtils.toByteArray(new FileInputStream(publicKeyPathName))))));
             
        } catch(Exception exception) {
            System.out.println("The testToFileSystem() test failed because: " + exception.getMessage());
        }
    }
}
