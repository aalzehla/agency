//package com._3line.gravity.freedom.financialInstitutions.sanef.service;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
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
//
//
//@Component
//public class NetworkConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(NetworkConfig.class);
//    private static final int port = 12000;
//    private static final int RECONNECT_WAIT_TIME = 1000;
//    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
//    private final ExecutorService executors = Executors.newFixedThreadPool(50);
//    private final Object disconnectLock = new Object();
//    private final Object retryConnectLock = new Object();
//    private final Object connectLock = new Object();
//    @Value("${sanef.ip}")
//    private String host;
//    private Socket socket;
//    private Consumer<byte[]> consumer;
//
//    private static int decodeSignedLenBytes(byte firstByte, byte secondByte) {
//        return ((firstByte & 255) << 8) + (secondByte & 255);
//    }
//
//    private static String bytesToHex(byte[] bytes) {
//        char[] hexChars = new char[bytes.length * 2];
//        for (int j = 0; j < bytes.length; j++) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
//            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
//        }
//        return new String(hexChars);
//    }
//
//    @PostConstruct
//    public void init() {
//
//        logger.info("The Sanef Connection");
//        executors.submit(() -> connect());
//        executors.submit(() -> read());
//    }
//
//    public void setResponseConsumer(Consumer<byte[]> consumer) {
//        this.consumer = consumer;
//    }
//
//    public void reset() {
//        closeSocket();
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
//                    logger.info("Attempting connection to server {}:{}", host, port);
//                    this.socket.connect(new InetSocketAddress(host, port));
//                    logger.info("Connected to server {}:{}", host, port);
//                }
//                catch (IOException e) {
//                    closeSocket();
//                    logger.error(String.format("Could not connect to remote %s:%d", host, port), e);
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
//    public void read() {
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
//                    byte[] readData = readToByteArray(socket.getInputStream(), len);
////                    String response=bytesToHex(readData);
////                    System.out.println("My respoonses: "+response);
////                    System.out.println("data read:"+readData);
//                    executors.submit(() -> consumer.accept(readData));
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
//        dataInputStream.read(data);
//
//        return data;
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
//    private void notifyConnect() {
//        synchronized (connectLock) {
//            connectLock.notifyAll();
//        }
//
//        System.out.println("the system is connected");
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
//    private synchronized void closeSocket() {
//        if (socket == null || !socket.isConnected()) {
//            return;
//        }
//
//        try {
//            socket.close();
//        }
//        catch (IOException e) {
//            logger.error(String.format("There was an exception closing the client socket %s:%d", host, port), e);
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
//
//        System.out.println("The system is disconnected");
//    }
//
//    public void write(byte[] data) throws IOException {
//        if (socket == null || !socket.isConnected()) {
//            throw new IOException("Socket is not connected");
//        }
//
//        String log = String.format("writing to %s:%d %d bytes%n%s", host, port, data.length, new String(data));
//        logger.info(log);
//        socket.getOutputStream().write(data);
//    }
//
//}
//
//
