package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.utility;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kwerenachi Utosu
 * @date 10/15/2019
 */
public class Base64Converter {

    private static Logger logger = LoggerFactory.getLogger(Base64Converter.class);

    public static String encodeString(String string){
        String encodeString = "";
        Base64 base64 = new Base64();
        encodeString = base64.encodeAsString(string.getBytes());
        logger.info("The String converted to Base64 {}", encodeString);
        return encodeString;
    }
}
