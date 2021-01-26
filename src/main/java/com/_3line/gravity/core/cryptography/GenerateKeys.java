package com._3line.gravity.core.cryptography;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;

public class GenerateKeys {

    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private SecretKey key ;
    private KeyGenerator generator = KeyGenerator.getInstance("AES");
    public GenerateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
//        this.keyGen = KeyPairGenerator.getInstance("RSA");
//        this.keyGen.initialize(keylength);
        generator.init(128);
    }

    public static void main(String[] args) {
        GenerateKeys gk;
        try {
            gk = new GenerateKeys(1024);
            gk.createKeys();

            SecretKey test = gk.key ;

            String tobeencrypted =  "Test information goinf in ";
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, test);

            byte[] encryted = cipher.doFinal(tobeencrypted.getBytes());

            System.out.println("see encrypted "+ encryted);


            System.out.println("decrypting .....");
            SecretKey decrypt = new SecretKeySpec(test.getEncoded(), 0, test.getEncoded().length, "AES");

            Cipher cipher2 = Cipher.getInstance("AES");
            cipher2.init(Cipher.DECRYPT_MODE, decrypt);


            String d = new String(cipher2.doFinal(encryted), "UTF-8");

            System.out.println("decrypted  .."+d);

//            gk.writeToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
//            gk.writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public void createKeys() {
//        this.pair = this.keyGen.generateKeyPair();
//        this.privateKey = pair.getPrivate();
//        this.publicKey = pair.getPublic();
         this.key = this.generator.generateKey() ;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public SecretKey getKey(){
        return this.key ;
    }

    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }
}
