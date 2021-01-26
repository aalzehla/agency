/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author OlalekanW
 */

@Service
public class RestTemplateHttp {

    @Autowired
    RestTemplate restTemplate;
    
    Logger logger = LoggerFactory.getLogger(this.getClass()) ;

    public String post(String Url, String contenttype, String body, Map<String, String> headers/*String methodPath, Object bodyObject, HttpMethod httpMethod,Map<String, String> headers*/) {
        logger.info("url to call is {}" , Url);
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            for (Map.Entry<String, String> header : ((Map<String, String>) headers).entrySet()) {
                httpHeaders.set(header.getKey(), header.getValue());
            }
        }
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        org.springframework.http.HttpEntity<Object> httpEntity = new org.springframework.http.HttpEntity<>(body, httpHeaders);

            ResponseEntity response ;
            logger.info("calling service now !!");
            response = restTemplate.exchange(Url, HttpMethod.POST, httpEntity, String.class);
            logger.info(response.toString());
            return response.getBody().toString();


    }

    public String get(String Url, Map<String, String> headers/*String methodPath, Object bodyObject, HttpMethod httpMethod,Map<String, String> headers*/) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            for (Map.Entry<String, String> header : ((Map<String, String>) headers).entrySet()) {
                httpHeaders.set(header.getKey(), header.getValue());
            }
        }
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        org.springframework.http.HttpEntity<Object> httpEntity = new org.springframework.http.HttpEntity<>(httpHeaders);

            ResponseEntity response;
            response = restTemplate.exchange(Url, HttpMethod.GET, httpEntity, String.class);
            logger.info(response.toString());
            return response.getBody().toString();

    }
}
