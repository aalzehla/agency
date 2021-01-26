package com._3line.gravity.core.utils;


import com._3line.gravity.core.models.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author FortunatusE
 * @date 12/6/2018
 */
public class ResponseUtils {


    public static Response createDefaultSuccessResponse() {

        Response response = new Response();
        response.setRespCode("00");
        response.setRespDescription("success");
        return response;
    }


    public static Response createResponse(Object data) {

        Response response = new Response();
        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(data);
        return response;
    }

    public static Response createResponse(String code, String message) {

        Response response = new Response();
        response.setRespCode(code);
        response.setRespDescription(message);
        return response;
    }

    public static Response createResponse(String code, String message, Object body) {

        Response response = new Response();
        response.setRespCode(code);
        response.setRespDescription(message);
        response.setRespBody(body);
        return response;
    }

    public static Response createDefaultFailureResponse() {

        Response response = new Response();
        response.setRespCode("999");
        response.setRespDescription("Error performing operation");
        return response;
    }

   public static Response createFailureResponse(String errorMessage) {

        Response response = new Response();
        response.setRespCode("999");
        response.setRespDescription(errorMessage);
        return response;
    }


    public static Response createSuccessResponse(String message) {

        Response response = new Response();
        response.setRespCode("00");
        response.setRespDescription(message);
        return response;
    }


    public static Response createFailureResponse(String code, String errorMessage) {

        Response response = new Response();
        response.setRespCode(code);
        response.setRespDescription(errorMessage);
        return response;
    }

    public static Object convertResponseBodyToClassInstance(Response response, Class to) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(response.getRespBody());
        return objectMapper.readValue(body, to);
    }


}
