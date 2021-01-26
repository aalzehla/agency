//package com._3line.gravity.freedom.financialInstitutions.sanef.service;
//
//import com._3line.gravity.api.shared.utility.JwtUtility;
//import com._3line.gravity.core.exceptions.GravityException;
//import com._3line.gravity.core.exceptions.UnAuthorizedUserException;
//import com._3line.gravity.freedom.agents.models.Agents;
//import com._3line.gravity.freedom.agents.repository.AgentsRepository;
//import com._3line.gravity.freedom.bankdetails.model.BankDetails;
//import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
//import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountOpeningResponse;
//import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountRequest;
//import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.modelmapper.ModelMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import javax.annotation.PostConstruct;
//import java.io.DataInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.function.Consumer;
//import java.util.logging.Level;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//@Component
//public class SanefClientHandler {
//    private static final int PORT = 12000;
//    private static final String[] hexChars = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
//    private static final int RECONNECT_WAIT_TIME = 1000;
//    private final ExecutorService executors = Executors.newFixedThreadPool(50);
//    private final Object disconnectLock = new Object();
//    private final Object retryConnectLock = new Object();
//    private final Object connectLock = new Object();
//    private Logger logger = LoggerFactory.getLogger(SanefClientHandler.class);
//    @Value("${sanef.url}")
//    private String uri;
//    @Value("${sanef.username}")
//    private String username;
//    @Value("${sanef.password}")
//    private String password;
//    @Value("${sanef.superagent.code}")
//    private String superAgentCode;
//    @Value("${sanef.ip}")
//    private String ipv6;
//    private JwtUtility jwtUtility;
//    private BankDetailsService bankDetailsService;
//    private Socket socket;
//    private Consumer<byte[]> consumer;
//    @Autowired
//    AgentsRepository agentsRepository;
//
//    private static int decodeSignedLenBytes(byte firstByte, byte secondByte) {
//        return ((firstByte & 255) << 8) + (secondByte & 255);
//    }
//
//    private static String hexify(byte[] data) {
//        if (data == null) {
//            return "null";
//        }
//        String out;
//        int x = data.length;
//
//        out = IntStream.range(0, x).mapToObj(i -> hexChars[(data[i] >> 4) & 0x0f] + hexChars[data[i] & 0x0f]).collect(Collectors.joining());
//        return out;
//    }
//
//    private static String hexToAscii(String hexStr) {
//        StringBuilder output = new StringBuilder("");
//        for (int i = 0; i < hexStr.length(); i += 2) {
//            String str = hexStr.substring(i, i + 2);
//            output.append((char) Integer.parseInt(str, 16));
//        }
//        return output.toString();
//    }
//
//    public void setResponseConsumer(Consumer<byte[]> consumer) {
//        this.consumer = consumer;
//    }
//
//    @PostConstruct
//    public void init() {
//        logger.info("Started my sanef");
//
//        setResponseConsumer(consumer);
//    }
//
//    public void reset() {
//        closeSocket();
//    }
//
//    private synchronized void closeSocket() {
//        if (socket == null || !socket.isConnected()) {
//            return;
//        }
//
//        try {
//            socket.close();
//        }
//        catch (IOException e) {
//            logger.error(String.format("There was an exception closing the client socket %s:%d", ipv6, PORT), e);
//        } finally {
//            socket = null;
//            notifyDisconnect();
//        }
//    }
//
//    private void notifyDisconnect() {
//        synchronized (disconnectLock) {
//            disconnectLock.notifyAll();
//        }
//    }
//
//    private void connect() {
//        while (true) {
//
//            while (isConnected()) {
//                waitForDisconnection();
//            }
//
//            try {
//
//                this.socket = new Socket();
//                this.socket.setTcpNoDelay(true);
//                this.socket.setKeepAlive(true);
//                this.socket.setSoLinger(true, 0);
//
//                try {
//                    logger.info("Attempting connection to server {}:{}", ipv6, PORT);
//                    this.socket.connect(new InetSocketAddress(ipv6, PORT));
//                    logger.info("Connected to server {}:{}", ipv6, PORT);
//
//                    if (socket == null) break;
//                }
//                catch (IOException e) {
//                    closeSocket();
//                    logger.error(String.format("Could not connect to remote %s:%d", ipv6, PORT), e);
//                    waitForRetryConnection();
//                }
//
//                if (isConnected()) {
//                    notifyConnect();
//                }
//
//            }
//            catch (Exception e) {
//                logger.error("There was an error during connection", e);
//            }
//        }
//    }
//
//    private boolean isConnected() {
//        if (this.socket == null) {
//            return false;
//        }
//
//        return this.socket.isConnected();
//    }
//
//    private void waitForDisconnection() {
//        synchronized (disconnectLock) {
//            try {
//                disconnectLock.wait();
//            }
//            catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//    }
//
//    private void waitForConnection() {
//        synchronized (connectLock) {
//            try {
//                connectLock.wait();
//            }
//            catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//    }
//
//    private void waitForRetryConnection() {
//        synchronized (retryConnectLock) {
//            try {
//                retryConnectLock.wait(RECONNECT_WAIT_TIME);
//            }
//            catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//    }
//
//    private void notifyConnect() {
//        synchronized (connectLock) {
//            connectLock.notifyAll();
//        }
//    }
//
//    public void write(byte[] data) throws IOException {
//        if (socket == null || !socket.isConnected()) {
//            throw new IOException("Socket is not connected");
//        }
//
//        String log = String.format("writing to %s:%d %d bytes%n%s", ipv6, PORT, data.length, new String(data));
//        logger.info(log);
//        socket.getOutputStream().write(data);
//    }
//
//    private byte[] read() {
//        while (true) {
//            try {
//
//                if (!isConnected()) {
//                    waitForConnection();
//                }
//
//                try {
//
//                    byte[] lenBytes = readToByteArray(socket.getInputStream(), 2);
//
//                    int len = decodeSignedLenBytes(lenBytes[0], lenBytes[1]);
//
//                    return readToByteArray(socket.getInputStream(), len);
//
//
//                }
//                catch (IOException e) {
//                    closeSocket();
//                    logger.error("There was an error reading data from socket", e);
//                }
//            }
//            catch (Exception e) {
//                logger.error("There as an error in read process", e);
//            }
//        }
//    }
//
//    private byte[] readToByteArray(InputStream is, int len) throws IOException {
//        DataInputStream dataInputStream = new DataInputStream(is);
//        byte[] data = new byte[len];
//        dataInputStream.readFully(data);
//
//        return data;
//    }
//
//    private String encrypt(String plainText) {
//        executors.submit(() -> connect());
//        String response = "";
//        try {
//            logger.info("got here--->");
//            String message = String.format("ENCRYPT%1$s", plainText);
//            write(message.getBytes());
//            byte[] resp = read();
//            response = hexToAscii(hexify(resp));
//
//
//            // Write the response to the console.
//            logger.info("response: {}", response);
//
//            socket.close();
//        }
//        catch (IOException e) {
//            logger.info("Exception: {} now", e.getMessage());
//            e.printStackTrace();
//
//        }
//        return response;
//    }
//
//    private String decrypt(String encryptedMessage) {
//        executors.submit(() -> connect());
//        String responseRecieve = "";
//        try {
//            logger.info("still connected!!");
//            String message = String.format("DECRYPT%1$s#%2$s", password, encryptedMessage);
//            write(message.getBytes());
//            byte[] bytes = read();
//            responseRecieve = hexToAscii(hexify(bytes));
//
//            // Write the response to the console.
//            logger.info("resp: {}", responseRecieve);
//
//
//        }
//        catch (IOException e) {
//            logger.info(e.getMessage());
//        }
//        catch (Exception ex) {
//            throw new GravityException(ex.getMessage());
//        }
//        return responseRecieve;
//    }
//
//    private String extractData(JSONObject jsonObject, String url) {
//        try {
//            String toJson = jsonObject.toString();
//            logger.info("TO JSON STRING: {}", toJson);
//            String data = encrypt(toJson).trim();
//            String encryptedResponse = "";
//            if (sendHttpRequest(data, url) == null) encryptedResponse = "";
//            else {
//                SanefResponse sanefResponse = sendHttpRequest(data, url);
//                if (sanefResponse != null && sanefResponse.getData() != null) {
//                    encryptedResponse = sanefResponse.getData();
//                }
//            }
//
//            logger.info("After encryption data:=====> {}", encryptedResponse);
//            //declare this in the properties file
//            return decrypt(encryptedResponse);
//        }
//        catch (Exception e) {
//            throw new GravityException(e.getMessage());
//        }
//    }
//
//    public SanefAccountOpeningResponse sanefAccountOpening(SanefAccountRequest payload) {
//        SanefAccountRequest request = getRequestPayload(payload);
//        JSONObject jsonObject = new JSONObject(request);
//        logger.info("The request payload: {}", request);
//        String plainText = extractData(jsonObject, uri);
//
//        SanefAccountOpeningResponse openingResponse = null;
//
//        try {
//
//            openingResponse = new ObjectMapper().readValue(plainText, SanefAccountOpeningResponse.class);
//
//            logger.info("accountNumber: {}", openingResponse.getAccountNumber());
//            logger.info("responseCode: {}", openingResponse.getResponseCode());
//            logger.info("responseDescription: {}", openingResponse.getResponseDescription());
//
//        }
//        catch (IOException e) {
//            logger.info("exception: {}", e.getMessage());
//        }
//        catch (Exception ex) {
//            throw new GravityException(ex.getMessage());
//        }
//        return openingResponse;
//    }
//
//    private SanefAccountRequest getRequestPayload(SanefAccountRequest payload) {
//        ModelMapper modelMapper = new ModelMapper();
//        SanefAccountRequest accountRequest = modelMapper.map(payload, SanefAccountRequest.class);
//        //this should be declared in properties
//        accountRequest.setSuperagentCode(superAgentCode);
////        Agents agent = agentsRepository.findByUsername("funmilayo.olakunle");
//////        if (agent == null) {
//////            throw new UnAuthorizedUserException("User not authorized");
//////        }
////        accountRequest.setAgentCode(agent.getAgentCode());
////        logger.info("Agent Code: {}", agent.getAgentCode());
////        BankDetails bankDetails = getBankDetails(payload.getBankCode());
////        if (bankDetails != null) {
////            accountRequest.setBankCode(bankDetails.getSanefBankCode());
////        } else accountRequest.setBankCode("");
////        logger.info("Sanef code: {}", accountRequest.getBankCode());
//        return accountRequest;
//    }
//
//    private BankDetails getBankDetails(String bankCode) {
//        logger.info("BankCode: {}", bankCode);
//        return bankCode != null ?
//                bankDetailsService.findByCode(bankCode) : null;
//    }
//
//    private SanefResponse sendHttpRequest(String excryptedData, String requestUrl) {
//
//        //declare this in the properties
//
//        JsonObject map = new JsonObject();
//        map.addProperty("data", excryptedData);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("ClientID", superAgentCode);
//        headers.set("cache-control", "no-cache");
//        HttpEntity entity = new HttpEntity<>(map.toString(), headers);
//        logger.info("URL:{}", requestUrl);
//        Gson gson = new Gson();
//        SanefResponse object;
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//            ResponseEntity<SanefResponse> responseEntity = restTemplate.exchange(
//                    requestUrl, HttpMethod.POST, entity,
//                    SanefResponse.class);
//
//
//            object = responseEntity.getBody();
//            String serverResponse = gson.toJson(object);
//            logger.info("Response: {}", serverResponse);
//
//        }
//        catch (JSONException e) {
//            throw new GravityException("JSONException occurred");
//        }
//        catch (final HttpClientErrorException httpClientErrorException) {
//            String responseBodyAsString = httpClientErrorException.getResponseBodyAsString();
//            HttpStatus statusCode = httpClientErrorException.getStatusCode();
//            java.util.logging.Logger.getLogger(SanefClientHandler.class.getName()).log(Level.SEVERE, "Status code:{0}, Response body: {1}", new Object[]{statusCode, responseBodyAsString});
//            JSONObject jsonObject = new JSONObject(responseBodyAsString);
//            JSONArray ja = (JSONArray) jsonObject.get("Errors");
//            throw new GravityException(ja.getString(0));
//        }
//        catch (HttpServerErrorException httpServerErrorException) {
//            throw new GravityException(httpServerErrorException);
//        }
//        catch (GravityException exception) {
//            throw new GravityException();
//        }
//        catch (Exception e) {
//            throw new GravityException(e.getMessage());
//        }
//        return object;
//    }
//
//}
