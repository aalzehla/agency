/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.core.cryptography.rsa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author OlalekanW
 */

@Service
public class RSACrypt {


    @Value("${path.to.secret}")
    private String path = "" ;
    private String privateKeyPathName = "private.key";
    private String publicKeyPathName = "public.key";
    private String transformation = "RSA/ECB/PKCS1Padding";
    private String encoding = "UTF-8";


    public  String decrypt(String cipher) throws Exception {
        RSACipher rsaCipher = new RSACipher();
        return rsaCipher.decrypt(cipher, path+privateKeyPathName, transformation, encoding);
    }

    public  String encrypt(String clear) throws Exception {
        RSACipher rsaCipher = new RSACipher();
        return rsaCipher.encrypt(clear, path+publicKeyPathName, transformation, encoding);
    }
}
