package com._3line.gravity.core.security.utils;

import com._3line.gravity.core.security.service.implementation.CustomUserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SecurityUtility {

    private static Logger logger = LoggerFactory.getLogger(SecurityUtility.class) ;

    public static Map<String, String> getHeaders(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            logger.debug("############# HEADERS ###########");
            logger.debug(key + "  : " + value);
            map.put(key, value);
        }
        return map;

    }

    public static String sha512(String args) {

        try {
            // Create MessageDigest instance for MD5
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-512");
            //Add password bytes to digest
            md.update(args.getBytes(StandardCharsets.UTF_8));
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format

            return byteArrayToHexString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "error";
    }

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //Get complete hashed password in hex format
        return sb.toString();
    }

    public static CustomUserPrincipal currentUser() {
        return ((CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
