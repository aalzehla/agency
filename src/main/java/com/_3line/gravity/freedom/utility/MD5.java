package com._3line.gravity.freedom.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {



public static String md5(String args)
{

    try {
        // Create MessageDigest instance for MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
        //Add password bytes to digest
        md.update(args.getBytes());
        //Get the hash's bytes
        byte[] bytes = md.digest();
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //Get complete hashed password in hex format
        return  sb.toString();
    }
    catch (NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }
   return "error";
}

public static void main(String [] ma){
System.out.println(md5("2340110003697bmeB50020161104152652"));}

public static  boolean compare(String email , String hash){
	if(md5(email).equals(hash)){return true;}
	return false;
												   }

}