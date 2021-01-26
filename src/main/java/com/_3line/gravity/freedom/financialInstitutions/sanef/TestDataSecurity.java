package com._3line.gravity.freedom.financialInstitutions.sanef;//package com._3line.gravity.freedom.financialInstitutions.sanef;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountOpeningResponse;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountRequest;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TestDataSecurity /*implements SanefService*/ {
    private static final String KEY_GENERATION_RESPONSE = "Key Generation Response : {}";
    // The PORT number for the remote device.
    private static final int PORT = 12000;
    private static final String EXCEPTION_CAUGHT = "Exception caught: {}";
    private static final String[] hexChars = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
    // The response from the remote device.
    private static String response;
    private static Logger logger = LoggerFactory.getLogger(TestDataSecurity.class);
    private static Socket socket = new Socket();
    private static byte[] resp = new byte[10 * 1024];

    public static void main(String[] args) throws IOException {
        logger.info("1. Generate Keys");
        logger.info("2. Encrypt");
        logger.info("3. Decrypt");

        String input = "";
        while(!input.equals("0")){
         input = new Scanner(System.in).nextLine();

            if ("1".equals(input)) {
                logger.info("Enter username");
                String username = new Scanner(System.in).nextLine();
                logger.info("Enter password");
                String password = new Scanner(System.in).nextLine();
                generateKeys(username, password);
            } else if ("2".equals(input)) {
                logger.info("Enter data to encrypt");
                String plainText = new Scanner(System.in).nextLine();
                encrypt(plainText);

            } else if ("3".equals(input)) {
                logger.info("Enter data to decrypt");
                String encryptedData = new Scanner(System.in).nextLine();
                logger.info("Enter password");
                String password = new Scanner(System.in).nextLine();
                decrypt(encryptedData, password);
            }
        }
    }

    private static boolean getConnection() {
        // Connect to a remote device.
        // Establish the remote endpoint for the socket.
        // getting localhost ip
        InetAddress ip;
        String hostname;

        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            logger.info("Your current IP address : {}", ip.getHostAddress());
            logger.info("Your current IP address : {} ", ip);
            logger.info("Your current Hostname : {}", hostname);
            // Create a TCP/IP socket.

            //Make IP Address configurable
            socket.connect(new InetSocketAddress("fe80::98f2:7059:7b43:6527%12", PORT));

            return true;
        }
        catch (IOException e) {
            logger.info(EXCEPTION_CAUGHT, e.getMessage());
        }
        return false;
    }

    //    @Override
    public static void generateKeys(String username, String password) {

        try {
            if (getConnection()) {
                logger.info("got here-----to generate keys");
                String message = String.format("GENERATE%1$s#%2$s", username, password);
                socket.getOutputStream().write(message.getBytes());
                socket.getOutputStream().flush();
                socket.getInputStream().read(resp);
                response = hexToAscii(hexify(resp));
            }
            // Write the response to the console.
            logger.info(KEY_GENERATION_RESPONSE, response);

            socket.close();
        }
        catch (IOException e) {
            logger.info(EXCEPTION_CAUGHT, e.getMessage());
        }
    }

    //    @Override
    public static String encrypt(String plainText) throws IOException {

        try {
            if (getConnection()) {
                logger.info("got here--->");
                String message = String.format("ENCRYPT%1$s", plainText);
                socket.getOutputStream().write(message.getBytes());
                socket.getInputStream().read(resp);
                response = hexToAscii(hexify(resp));

            }
            // Write the response to the console.
            logger.info(KEY_GENERATION_RESPONSE, response);


        }
        catch (IOException e) {
            logger.info("Exception: {}", e.getMessage());
            socket.close();
        }
        return response;
    }

    //    @Override
    public static String decrypt(String encryptedMessage, String password) {
        byte[] resp = new byte[100 * 1024];
        Socket socketconn = new Socket();
        try {
            socketconn.connect(new InetSocketAddress("fe80::98f2:7059:7b43:6527%12", PORT));
        }
        catch (IOException e) {
            logger.info("Exception: {}", e.getMessage());
        }

        String responseRecieve = null;
        try {
            if (socketconn.isConnected()) {
                logger.info("still connected!!");
                String message = String.format("DECRYPT%1$s#%2$s", password, encryptedMessage);
                socketconn.getOutputStream().write(message.getBytes());

                socketconn.getInputStream().read(resp);

                responseRecieve = hexToAscii(hexify(resp));
            }
            // Write the response to the console.
            logger.info(KEY_GENERATION_RESPONSE, responseRecieve);

            socketconn.close();
        }
        catch (IOException e) {
            logger.info(EXCEPTION_CAUGHT, e.getMessage());
        }
        return responseRecieve;
    }

    private static SanefAccountOpeningResponse getEncryptedPayloadAndDecryptResponse(SanefAccountRequest payload) throws IOException {
        setRequestPayload(payload);
        JSONObject jsonObject = new JSONObject(payload);
        logger.info("The request payload: {}", payload);
        String toJson = jsonObject.toString();
        logger.info("TO JSON STRING: {}", toJson);
        String data = encrypt(toJson).trim();
        logger.info("After encryption:=====> Decrypt response");
        String encryptedResponse = "";
        if (sendHttpRequest(data) == null) encryptedResponse = "";
        else {
            SanefResponse sanefResponse = sendHttpRequest(data);
            if (sanefResponse != null && sanefResponse.getData() != null) {
                encryptedResponse = sanefResponse.getData();
            }
        }

        logger.info("After encryption data:=====> {}", encryptedResponse);
        String plainText= decrypt(encryptedResponse, "cruza");
        SanefAccountOpeningResponse openingResponse = null;
        try {

            openingResponse = new ObjectMapper().readValue(plainText, SanefAccountOpeningResponse.class);

            logger.info("accountNumber: {}",openingResponse.getAccountNumber());
            logger.info("responseCode: {}",openingResponse.getResponseCode());
            logger.info("responseDescription: {}",openingResponse.getResponseDescription());
        }
        catch (IOException e) {
            logger.info(EXCEPTION_CAUGHT,e.getMessage());
        }
        return openingResponse;
    }

    private static void setRequestPayload(SanefAccountRequest payload) {
        payload.setSuperagentCode("008");
        payload.setAccountOpeningBalance("1000");
        payload.setAgentCode("99989098");
        payload.setBankCode("000015");
        payload.setRequestId("000001201910240846150999883774");
        payload.setBankVerificationNumber("22123456789");
        payload.setFirstName("Samuel");
        payload.setMiddleName("John");
        payload.setLastName("John");
        payload.setGender("Male");
        payload.setDateOfBirth("1978-Oct-20");
        payload.setHouseNumber("10B");
        payload.setStreetName("Almond street");
        payload.setCity("Igando");
        payload.setLgaCode("502");
        payload.setEmailAddress("aa@gmail.com");
        payload.setPhoneNumber("08012345678");
        payload.setCustomerImage("base64 image");
        payload.setCustomerSignature("base64 image");
    }


    private static SanefResponse sendHttpRequest(String excryptedData) {
        String url = "http://35.231.60.190/sanef_api_thirdparty/api/v1/accounts/createAccount";
        JsonObject map = new JsonObject();
        map.addProperty("data", excryptedData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("ClientID", "008");
        headers.set("cache-control", "no-cache");
        HttpEntity entity = new HttpEntity<>(map.toString(), headers);

        Gson gson = new Gson();
        SanefResponse object = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SanefResponse> responseEntity = restTemplate.exchange(
                    url, HttpMethod.POST, entity,
                    SanefResponse.class);


            object = responseEntity.getBody();
            String serverResponse = gson.toJson(object);
            logger.info("Response: {}", serverResponse);

        }
        catch (JSONException e) {
            throw new GravityException("JSONException occurred");
        }
        catch (final HttpClientErrorException httpClientErrorException) {
            String responseBodyAsString = httpClientErrorException.getResponseBodyAsString();
            HttpStatus statusCode = httpClientErrorException.getStatusCode();
            java.util.logging.Logger.getLogger(TestDataSecurity.class.getName()).log(Level.SEVERE, "Status code:{0}, Response body: {1}", new Object[]{statusCode, responseBodyAsString});

        }
        catch (HttpServerErrorException httpServerErrorException) {
            throw new GravityException(httpServerErrorException);
        }
        catch (GravityException exception) {
            throw new GravityException();
        }
        return object;
    }

    private static String hexify(byte[] data) {
        if (data == null) {
            return "null";
        }
        String out;
        int x = data.length;

        out = IntStream.range(0, x).mapToObj(i -> hexChars[(data[i] >> 4) & 0x0f] + hexChars[data[i] & 0x0f]).collect(Collectors.joining());
        return out;
    }

    private static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

}
