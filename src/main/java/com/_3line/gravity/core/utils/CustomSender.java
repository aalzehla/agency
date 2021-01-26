package com._3line.gravity.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class CustomSender {
    private  final Logger logger = LoggerFactory.getLogger(getClass());
    // Username that is to be used for submission
    String username;
    // password that is to be used along with username
    String password;
    // Message content that is to be transmitted
    String message;

    /**
     * Destinations to which message is to be sent For submitting more than one
     * destination at once destinations should be comma separated Like
     * <p>
     * 2341111111111,2342222222
     * 222
     */
    String destination;
    // Sender Id to be used for submitting the message
    String source;
// To what server you need to connect to for submission

    String server;
    // Port that is to be used like 8080 or 8000


    public CustomSender(String server, String username, String password,
                        String message, String destination,
                        String source) {
        this.username = username;
        this.password = password;
        this.message = message;
        this.destination = destination;
        this.source = source;
        this.server = server;

    }

    public void submitMessage() {
        try {
// Url that will be called to submit the message
            URL sendUrl = new URL("http://" + this.server
                  );
            HttpURLConnection httpConnection = (HttpURLConnection) sendUrl
                    .openConnection();
// This method sets the method type to POST so that
// will be send as a POST request
            httpConnection.setRequestMethod("POST");
// This method is set as true wince we intend to send
// input to the server
            httpConnection.setDoInput(true);
// This method implies that we intend to receive data from server.
            httpConnection.setDoOutput(true);
// Implies do not use cached data
            httpConnection.setUseCaches(false);
// Data that will be sent over the stream to the server.
            DataOutputStream dataStreamToServer = new DataOutputStream(
                    httpConnection.getOutputStream());
            dataStreamToServer.writeBytes("username="
                    + URLEncoder.encode(this.username, "UTF-8") + "&password="
                    + URLEncoder.encode(this.password, "UTF-8") + "&message="
                    + URLEncoder.encode(this.message, "UTF-8") + "&sender="
                    + URLEncoder.encode(this.source, "UTF-8") + "&mobiles="
                    + URLEncoder.encode(this.destination, "UTF-8") );


            dataStreamToServer.flush();
            dataStreamToServer.close();
// Here take the output value of the server.
            BufferedReader dataStreamFromUrl = new BufferedReader(
                    new InputStreamReader(httpConnection.getInputStream()));
            String dataFromUrl = "", dataBuffer = "";
// Writing information from the stream to the buffer
            while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
                dataFromUrl += dataBuffer;
            }
/**
 * Now dataFromUrl variable contains the GravityResponse received from the
 * server so we can parse the response and process it accordingly.
 */
            dataStreamFromUrl.close();
            System.out.println("GravityResponse: " + dataFromUrl);
            logger.info("GravityResponse: " + dataFromUrl);
        } catch (Exception ex) {
            logger.error("error occurred: ",ex );
            ex.printStackTrace();
        }
    }


}
