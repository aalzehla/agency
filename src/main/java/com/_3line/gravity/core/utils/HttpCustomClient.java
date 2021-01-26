package com._3line.gravity.core.utils;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


@Data
public class HttpCustomClient {

    private  final Logger logger = LoggerFactory.getLogger(getClass());

    private String reuestURL;
    private String requestBody ;
    private String requestType="GET";
    private boolean hasHeaderToken ;
    private HashMap<String,String> headerToken ;

    public HttpCustomClient() {

    }

    public HttpCustomClient(String reuestURL, String requestBody, String requestType, boolean hasHeaderToken, HashMap<String,String> headerContents) {
        this.reuestURL = reuestURL;
        this.requestBody = requestBody;
        this.requestType = requestType;
        this.hasHeaderToken = hasHeaderToken;
        this.headerToken = headerContents;
    }

    public String sendRequest() {
        String finalResponse="";
        String url = this.reuestURL;

        URL obj;
        try {
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(this.requestType);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            if(hasHeaderToken && headerToken !=null){
                headerToken.forEach((s, s2) -> {
                    con.setRequestProperty(s,s2);
                });
            }

            if(this.requestType=="POST" && this.requestBody!=null){
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = this.requestBody.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }



            int responseCode = con.getResponseCode();

            System.out.println("\nSending '"+this.requestType+"' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = null;
            if(con.getErrorStream() !=null){
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }else if(con.getInputStream()!=null){
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            finalResponse = response.toString();
            System.out.println("Response is :: "+finalResponse);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            finalResponse = e.getMessage();
        }  catch (IOException e) {
            e.printStackTrace();
            finalResponse = e.getMessage();
        }

        return finalResponse;


    }


}
