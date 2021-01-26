package com._3line.gravity.freedom._3linecms.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

/**
 * Created by FortunatusE on 8/27/2018.
 */


public class ResponseParser {


    private static final Logger logger = LoggerFactory.getLogger(ResponseParser.class);

    public static CreditResponse process(String xmlResponse) {

        CreditResponse response = null;

        try {

            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();

            Document doc = dBuilder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));

            doc.getDocumentElement().normalize();

            Element responseElement = (Element) doc.getElementsByTagName("resp").item(0);

            System.out.println(responseElement);

            String code = responseElement.getElementsByTagName("errCode").item(0).getTextContent();
            String message = responseElement.getElementsByTagName("errDesc").item(0).getTextContent();

            String newBalance = null;
            NodeList newBalanceObject = responseElement.getElementsByTagName("newBalance");
            if(newBalanceObject.getLength() != 0) {
                    newBalance = newBalanceObject.item(0).getTextContent();
            }
            response = new CreditResponse(code, message, newBalance);
        }

        catch (Exception e){
            logger.error("Error processing response", e);
        }

        return response;
    }

    public static CreditResponse parse(String xmlResponse){

        CreditResponse response = null;

        String responseTag = "<resp>";

        if(xmlResponse.contains(responseTag)){

            response = new CreditResponse();

            String statusCode =  StringUtils.substringBetween(xmlResponse, "<errCode>", "</errCode>");
            String  description =  StringUtils.substringBetween(xmlResponse, "<errDesc>", "</errDesc>");
            String newBalance = StringUtils.substringBetween(xmlResponse, "<newbalance>", "</newbalance>");

            response.setStatusCode(statusCode);
            response.setStatusMessage(description);
            response.setBalance(newBalance);
        }
        return response;
    }
}
