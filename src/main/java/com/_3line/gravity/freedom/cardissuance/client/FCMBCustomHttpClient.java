package com._3line.gravity.freedom.cardissuance.client;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * Created by FortunatusE on 8/27/2018.
 */

@Service
public class FCMBCustomHttpClient {


    @Value("${fcmb.card.service.timeout}")
    private String connectionTimeoutStr;

    private final Logger LOGGER = LoggerFactory.getLogger(FCMBCustomHttpClient.class);

    private int CONNECTION_TIMEOUT;


    public boolean stringIsNullOrEmpty(String arg) {
        return (arg == null) || ("".equals(arg)) || (arg.length() == 0);
    }


    public Response sendHttpRequest(String xmlFormattedMessage, String url, String soapAction) throws IOException {

        String result;
        Response response;
        CloseableHttpResponse httpResponse;
        CloseableHttpClient httpClient;

        CONNECTION_TIMEOUT = Integer.parseInt(connectionTimeoutStr);

        LOGGER.info("Sending http request with EndPoint URL : " + url);
        LOGGER.info("sending formatted payload  : \n\n" + xmlFormattedMessage);
        LOGGER.debug("Timeout for plugin-adapter : " + CONNECTION_TIMEOUT / 1000 + "s");

        System.setProperty("javax.net.debug", "false");

        RequestConfig.Builder requestBuilder = RequestConfig.custom();
        requestBuilder = requestBuilder.setConnectTimeout(CONNECTION_TIMEOUT);
        requestBuilder = requestBuilder.setConnectionRequestTimeout(CONNECTION_TIMEOUT);

        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setDefaultRequestConfig(requestBuilder.build());

        httpClient = builder.build();

        HttpPost httpPost = new HttpPost(url);
        ByteArrayEntity postDataEntity = new ByteArrayEntity(xmlFormattedMessage.getBytes());
        httpPost.setEntity(postDataEntity);


        httpPost.setHeader("Content-Type", "text/xml; charset=utf-8");
        httpPost.setHeader("SOAPAction", soapAction);
        httpResponse = httpClient.execute(httpPost);
        HttpEntity entity = httpResponse.getEntity();


        response = new Response();
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        response.setCode(statusCode);

        LOGGER.debug("The HTTP response code received is {}", statusCode);


        if (entity != null) {
            result = EntityUtils.toString(entity);
            result = StringEscapeUtils.unescapeXml(result);

            response.setMessage(result);
            EntityUtils.consume(entity);
        }

        if (httpResponse != null) {
            httpResponse.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }

        return response;
    }
}


