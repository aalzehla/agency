/**
 * Copyright 2014 Markus Geiss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com._3line.gravity.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class SMSClientProvider {

    private static final Logger logger = LoggerFactory.getLogger(SMSClientProvider.class);
    private static final int PHONE_NO_LENGTH = 14;
    @Value("${kudi.server}")
    private String server;

    @Value("${kudi.username}")
    private String username;
    @Value("${kudi.password}")
    private String password;
    @Value("${kudi.source}")
    private String source;

    @Value("${kudi.sms.alert}")
    private String allowSms;


    SMSClientProvider() {
        super();
    }

    /**
     * This validates a phone number against some specified rules
     * returns the correct number or null if the number failed a rule
     */
    public String validateNumber(String phone) throws NullPointerException {
        String returned = null;
        String phoneLocal = phone.replaceAll("[()]", "").trim();
        //check if it is in international format
        if (phoneLocal.contains("+")) {
            //Only send to mobile numbers
            //check if this is a nigerian number
            if (phoneLocal.contains("+234")) {
                if (phoneLocal.length() == PHONE_NO_LENGTH) {
                    returned = phoneLocal;
                } else {
                    //this a landline or invalid number hence no alert will be sent
                    logger.error("This is an invalid number or a landline hence no alert will be sent: %s",
                            phoneLocal.replaceAll("[()]", "").trim());
                    logger.info("Accepted formats; +234********** and 0**********");
                    throw new NullPointerException();
                }
            } else {
                //a non nigerian number
                returned = phoneLocal;
            }

            //check for more than one + sign
            if (phoneLocal.lastIndexOf('+') > 0) {
                //the phone number contains more than one + therefore it is invalid
                logger.error(String.format("The phone number %s has more than one plus", phoneLocal));
                logger.info("Accepted formats; +234********** and 0**********");
                throw new NullPointerException("Invalid phone number");
            }
        } else {
            //check if it is a landline
            if (phoneLocal.length() < 10) {
                logger.error("This is a landline hence no alert will be sent");
//                throw new NullPointerException("");
            } else if (phoneLocal.length() > 11) {
                logger.error("Invalid number: " + phoneLocal);
                logger.info("Accepted formats; +234********** and 0**********");
                throw new NullPointerException();
            } else {
                //It is a mobile number
                //convert it to international format
                returned = "+234" + phoneLocal.substring(1).replaceAll("[()]", "").trim();
            }
        }
        //check if the number is invalid
        if (returned.length() < PHONE_NO_LENGTH) {
            logger.error(String.format("Phone Number %s: is invalid", phoneLocal));
            logger.info("Accepted formats; +234********** and 0**********");
            throw new NullPointerException("");
        } else if (returned.equals("+2348000000000")) {
            logger.error("Invalid number: 08000000000");
            throw new NullPointerException("");
        }
        return returned.substring(1);
    }

    public void sendMessage(final String mobileNo, final String message) {

        try {

            if(allowSms!=null && !allowSms.isEmpty() && "Y".equalsIgnoreCase(allowSms)) {
                logger.info("Sending SMS to " + mobileNo + " ...");
                String destNo = validateNumber(mobileNo);
                logger.info("converted sms is " + destNo + " ...");
                CustomSender sender = new CustomSender(server,  username,
                        password, message,  destNo,
                        source);

                sender.submitMessage();
            }
        } catch (Exception trex) {
            logger.error("Could not send message, reason: ", trex);

        }
    }
}
