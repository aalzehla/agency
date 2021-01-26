/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.utility;

/**
 *
 * @author OlalekanW
 */


import com._3line.gravity.core.exceptions.GravityException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.Formatter;

public class FileUtility {
    

    
    public static void main(String[] args)
    {	

        
       
       
    	try{
                /*
    		//create a temp file
    		File temp = File.createTempFile("temp-file-name", ".tmp"); 
 
    		System.out.println("Temp file : " + temp.getAbsolutePath());
 
		//Get tempropary file path
    		String absolutePath = temp.getAbsolutePath();
    		String tempFilePath = absolutePath.
    		    substring(0,absolutePath.lastIndexOf(File.separator));
 
    		System.out.println("Temp file path : " + tempFilePath);
                */
            System.out.println(getClasspathFilesAbsolutePath(".")); //NOTE using . (as current directory) would not work if the core is running on server
 
    	}catch(Exception e){
    		e.printStackTrace();
    	                   }
 
    }
    public static String temdir(){
        return System.getProperty("java.io.tmpdir");
                                 }
    public static String workdir(){ //current working directory
        return System.getProperty("user.dir");
                                  }
    public static String ActualFileName(String absoluteName){
    return absoluteName.substring(absoluteName.lastIndexOf(File.separator)+1);
    }
   /* 
    public static String getClasspathFilesAbsolutePath(String name){
        URL url  = ClassLoader.getSystemResource(name); //uses system classload could return null on server because webserver uses different classloader
        if(url==null)
            return null;
        try{return URLDecoder.decode(url.getFile(),"UTF-8");}catch(UnsupportedEncodingException e){e.printStackTrace();}
        return null;
                                                                   } 
    public static File getClasspathFile(String name){
        URL url  = ClassLoader.getSystemResource(name);//uses system classload could return null on server because webserver uses different classloader
        if(url==null)
            return null;
        try{return new File(url.toURI());}catch(URISyntaxException e){e.printStackTrace();}
        return null;
    } 
    */
    public static String getClasspathFilesAbsolutePath(String name){
        URL url  = FileUtility.class.getClassLoader().getResource(name); //uses current classload Thus works whether on server or on system
        //NOTE if name supplied is  . instead of / (i.e current directory) it would not work if the core is running on server but will work if running on destop java se
                                                                        // . is used on desktop / is used on server
        if(url==null)
            return null;
        try{return URLDecoder.decode(url.getFile(),"UTF-8");}catch(UnsupportedEncodingException e){e.printStackTrace();}
        return null;
                                                                   }
    
    public static File getClasspathFile(String name){
        URL url  = FileUtility.class.getClassLoader().getResource(name);//uses current classload Thus works whether on server or on system
        //NOTE if name supplied is  . instead of /  (i.e current directory) it would not work if the core is running on server but will work if running on destop java se
                                                                       // . is used on desktop / is used on server
        if(url==null)
            return null;
        try{return new File(url.toURI());}catch(URISyntaxException e){e.printStackTrace();}
        return null;
    }


    public static File createEmtyFile(String folder, String name)throws GravityException {
        	Formatter outputfile = null;	
		try{
			outputfile = new Formatter(folder+name);
		   }
		catch(SecurityException se){
                        se.printStackTrace();
			throw new GravityException("You don't have permission to creeate file");

					   }
		catch(FileNotFoundException fn){
                        fn.printStackTrace();
			throw new GravityException("File was not found");
			}

		finally{
			if( outputfile != null){
                                outputfile.close();
                                                }
		       }
		

		if( outputfile != null){
			  outputfile.close();
					}
                return new File(folder+name);
                                                                   }
    
    public static String SHA1checkSum(String absolutefilename) throws Exception {
   
    

    MessageDigest md = MessageDigest.getInstance("SHA1");
    FileInputStream fis = new FileInputStream(absolutefilename);
    byte[] dataBytes = new byte[1024];
    
    int nread = 0; 
    
    while ((nread = fis.read(dataBytes)) != -1) {
      md.update(dataBytes, 0, nread);
    }

        byte[] mdbytes = md.digest();
   
    //convert the byte to hex format
        StringBuffer sb = new StringBuffer();
    for (int i = 0; i < mdbytes.length; i++) {
    	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }
    
    System.out.println("Digest(in hex format):: " + sb.toString());
    return sb.toString();
    
                                                                                }
    public static String SHA512checkSum(String absolutefilename) throws Exception {
   
    

    MessageDigest md = MessageDigest.getInstance("SHA-512");
    FileInputStream fis = new FileInputStream(absolutefilename);
    byte[] dataBytes = new byte[1024];
    
    int nread = 0; 
    
    while ((nread = fis.read(dataBytes)) != -1) {
      md.update(dataBytes, 0, nread);
    }

        byte[] mdbytes = md.digest();
   
    //convert the byte to hex format
        StringBuffer sb = new StringBuffer();
    for (int i = 0; i < mdbytes.length; i++) {
    	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }

    
    return sb.toString();
    
                                                                                }
    

    public static String createClassPathFolder(String name){
      try{
      String parent =  new File(FileUtility.class.getClassLoader().getResource(".").toString()).getParent().replace("file:/","").replace("file:\\","");
      //for linux hack prepend pathseparator befor the path to make absolute path
      parent = File.separator + parent;
      String path = parent+File.separator+name;
      File dir = new File(path);
      dir.mkdirs();
      return path+File.separator;
      }catch(Exception e){e.printStackTrace();}
      return "";
                                                           }    

}



	

