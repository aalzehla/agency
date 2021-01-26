package com._3line.gravity.freedom.utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

public class Post {

    //private static final org.slf4j.Logger logger = LoggerFactory.getLogger("com._3line");
    HttpClient httpclient = HttpClientBuilder.create().build();

    public String sendRawPost(String Url, String data) {

        StringBuilder result = new StringBuilder();

        try {
            HttpPost httppost = new HttpPost(Url);
            StringEntity entity = new StringEntity(data);
            entity.setContentType("text/plain");
            httppost.setEntity(entity);

            HttpResponse response = httpclient.execute(httppost);
            System.out.println("GravityResponse Code : " + response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            //HttpEntity resultEntity = result.getEntity();
        } catch (ClientProtocolException e) {
            //logger.error("Exception is ", e);
            e.printStackTrace();
            return null;
            /**/
        } catch (IOException e) {
            /**/
            //logger.error("Exception is ", e);
            e.printStackTrace();
            return null;
        }

        return result.toString();
    }

    public static String get(String url, Map<String, String> headers) throws IOException, URISyntaxException {
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(new URI(url));
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpget.setHeader(header.getKey(), header.getValue());
            }
        }

        HttpResponse response = httpclient.execute(httpget);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response
                .getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(url);
        return result.toString();
    }

    public static String httpGet(String url, Map<String, String> headers) throws IOException {

        URL site = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) site.openConnection();
        conn.setRequestMethod("GET");
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                conn.setRequestProperty(header.getKey(), header.getValue());
            }
        }
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();
        System.out.println(site.toString());
        return response.toString();
    }

    public String sendRawPost(String Url, String contenttype, String data,
            Map<String, String> headers) {

        StringBuilder result = new StringBuilder();

        try {

            HttpPost httppost = new HttpPost(Url);

            System.out.println("===========================================================\nContent-Type: " + contenttype);
            if (headers != null) {
                for (Map.Entry<String, String> header : headers
                        .entrySet()) {
                    httppost.setHeader(header.getKey(), header.getValue());
                    System.out.println(header.getKey() + ": " + header.getValue());
                }
            }
            StringEntity entity = new StringEntity(data);
            entity.setContentType(contenttype);
            httppost.setEntity(entity);

            HttpResponse response = httpclient.execute(httppost);
            System.out.println("GravityResponse Code : " + response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            System.out.println("\n" + data + "\n===========================================================\n");
            System.out.println("GravityResponse :" + result.toString());
            // HttpEntity resultEntity = result.getEntity();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
            /**/
        } catch (IOException e) {
            /**/
            e.printStackTrace();
            return null;
        }

        return result.toString();
    }

    public String PostMultipart(String Url, File picture, File signature, String param, Map<String, String> headers) {

        StringBuilder result = new StringBuilder();

        try {

            HttpPost httppost = new HttpPost(Url);
            if (headers != null) {
                for (Map.Entry<String, String> header : headers
                        .entrySet()) {
                    httppost.setHeader(header.getKey(), header.getValue());
                }
            }
            HttpEntity reqEntity = MultipartEntityBuilder
                    .create()
                    .addTextBody("authorisation", param)
                    .addBinaryBody("picture", picture, ContentType.create("core/octet-stream"), picture.getName())
                    .addBinaryBody("signature", signature, ContentType.create("core/octet-stream"), signature.getName())
                    .build();

            httppost.setEntity(reqEntity);

            //StringEntity entity = new StringEntity(data);
            //entity.setContentType("text/plain");
            //httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            System.out.println("GravityResponse Code : " + response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            //HttpEntity resultEntity = result.getEntity();
        } catch (ClientProtocolException e) {
            //logger.error("Exception is ", e);
            e.printStackTrace();
            return null;
            /**/
        } catch (IOException e) {
            /**/
            //logger.error("Exception is ", e);
            e.printStackTrace();
            return null;
        }

        return result.toString();
    }

    public String PostWithInBuiltAuthorisationParam(String Url, String param, Map<String, String> headers) {

        StringBuilder result = new StringBuilder();

        try {

            HttpPost httppost = new HttpPost(Url);
            if (headers != null) {
                for (Map.Entry<String, String> header : headers
                        .entrySet()) {
                    httppost.setHeader(header.getKey(), header.getValue());
                }
            }
            HttpEntity reqEntity = MultipartEntityBuilder
                    .create()
                    .addTextBody("authorisation", param)
                    .setContentType(ContentType.TEXT_PLAIN)
                    .build();

            httppost.setEntity(reqEntity);

            //StringEntity entity = new StringEntity(data);
            //entity.setContentType("text/plain");
            //httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            System.out.println("GravityResponse Code : " + response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            //HttpEntity resultEntity = result.getEntity();
        } catch (ClientProtocolException e) {
            //logger.error("Exception is ", e);
            e.printStackTrace();
            return null;
            /**/
        } catch (IOException e) {
            /**/
            //logger.error("Exception is ", e);
            e.printStackTrace();
            return null;
        }

        return result.toString();
    }

}
