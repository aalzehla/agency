/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.utility;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

/**
 *
 * @author OlalekanW
 */
@Service
public class MessageSource {

    public static ResourceBundleMessageSource messageSource;

    /**
     * @param messageSource the messageSource to set
     */
    public static String getMessage(String Key) {
        if (messageSource == null) {
            System.out.println("||||||||||||||||||||||||||||||\nMessageSource Could not be Loaded\n||||||||||||||||||||||||||||||");
            return "";
        }
        String val = messageSource.getMessage(Key, null, null);
        if (val == null) {
            System.out.println("||||||||||||||||||||||||||||||\nMessageSource Could not Get The Value of Key \"" + Key + "\" Specified\n||||||||||||||||||||||||||||||");
            return "";
        }
        return val;
    }

    public void setMessageSource(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
                                                                            }

}
