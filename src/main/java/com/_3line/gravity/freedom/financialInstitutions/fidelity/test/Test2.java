package com._3line.gravity.freedom.financialInstitutions.fidelity.test;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Test2 {




        public static void main (String[] args) throws Exception {
            String API_SECRETE = "Fi4g9@we!gh567";
            String combo = "78c33bdd75b45cc95041b51bff6fedfe505006490800000000000000";
            byte [] dataByte = Base64.decodeBase64(combo);
//            byte[] secretKey = API_SECRETE.getBytes("UTF-8");


            SecretKeySpec secretKey = new SecretKeySpec(dataByte, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            byte[] rawHmac = mac.doFinal(API_SECRETE.getBytes());
            System.out.println(Base64.encodeBase64String(rawHmac));
        }

    }
