package com._3line.gravity.freedom.cardissuance.client;


import com._3line.gravity.freedom.cardissuance.dto.CardIssuanceResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by FortunatusE on 8/27/2018.
 */


public class ResponseParser {


    private static final Logger logger = LoggerFactory.getLogger(ResponseParser.class);


    public static CardIssuanceResponseDTO parse(String xmlResponse){

        logger.debug("Parsing xml response: {}", xmlResponse);

        CardIssuanceResponseDTO response = null;

        String responseTag = "CardIssuanceResponse";

        if(xmlResponse.contains(responseTag)){

            response = new CardIssuanceResponseDTO();

            String result =  StringUtils.substringBetween(xmlResponse, "<CardIssuanceResult>", "</CardIssuanceResult>");
            String[] strings = StringUtils.split(result, "|");
            if(strings.length == 4){
                response.setStatusCode(strings[1]);
                response.setStatusMessage(strings[3]);
            }
            else {
                response.setStatusMessage(result);
            }
        }
        return response;
    }

    public static CardIssuanceResponseDTO process(String xmlResponse){

        logger.debug("Parsing xml response: {}", xmlResponse);

        CardIssuanceResponseDTO response = null;

        String responseTag = "LinkCardResponse";

        if(xmlResponse.contains(responseTag)){

            response = new CardIssuanceResponseDTO();

            String successful =  StringUtils.substringBetween(xmlResponse, "<IsSuccessful>", "</IsSuccessful>");
            String message =  StringUtils.substringBetween(xmlResponse, "<FailureReason>", "</FailureReason>");
            String successMessage = StringUtils.substringBetween(xmlResponse, "<SuccessMessage>", "</SuccessMessage>");

            if(successful != null && "true".equalsIgnoreCase(successful)){
                response.setSuccessful(true);
            }
            if (successMessage != null && !successMessage.isEmpty()) {
                response.setStatusMessage(successMessage);
            }else {
                response.setStatusMessage(message);
            }
        }
        return response;
    }
}
