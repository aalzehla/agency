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

 



 
public class RSACipherTest {
 
    private static final String privateKeyPathName = "C://temp//private.key";
    private static final String publicKeyPathName = "C://temp//public.key";
    private static final String transformation = "RSA/ECB/PKCS1Padding";
    private static final String encoding = "UTF-8";
     

    public static void main(String[]ar)
            throws Exception {
     
        try {
            //generate the keys
            RSAKeyPair rsaKeyPair = new RSAKeyPair(2048);
            rsaKeyPair.toFileSystem(privateKeyPathName, publicKeyPathName);
            
            //use the key to encrypt or decrypt
            RSACipher rsaCipher = new RSACipher();
            String encrypted = rsaCipher.encrypt("John has a long mustache.", publicKeyPathName, transformation, encoding);
            String decrypted = rsaCipher.decrypt(encrypted, privateKeyPathName, transformation, encoding);
            System.out.println("Encrypted :"+encrypted+" decrypted :"+decrypted+"decrypted is eqal to orignal ="+decrypted.equals("John has a long mustache."));   
             
        } catch(Exception exception) {
            System.out.println("The testEncryptDecryptWithKeyPairFiles() test failed because: " + exception.getMessage());
        }
    }
}