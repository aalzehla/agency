//package com._3line.gravity.freedom.financialInstitutions.sanef.service;
//
//import com._3line.gravity.core.exceptions.GravityException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedInputStream;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//
//@Component
//public class CreateClient {
//    private static final int PORT = 12000;
//    @Value("${sanef.ip}")
//    private String ipv6;
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//    private Socket socket;
//    private ApplicationContext applicationContext;
//
//    @Autowired
//    public CreateClient(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }
//
//    void createSocket() {
//        InetAddress ip;
//        String hostname;
//
//        try {
//            this.socket = new Socket();
//            this.socket.setTcpNoDelay(true);
//            this.socket.setKeepAlive(true);
//            this.socket.setSoLinger(true, 0);
//
//            ip = InetAddress.getLocalHost();
//            hostname = ip.getHostName();
//            logger.info("Your current IP address : {}", ip.getHostAddress());
//            logger.info("Your current IP address : {} ", ip);
//            logger.info("Your current Hostname : {}", hostname);
//            // Create a TCP/IP socket.
//
//            //Make IP Address configurable
//
//            //declare in the properties file
//            socket.connect(new InetSocketAddress(ipv6, PORT));
//            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//
//            Thread clientHandler = new SanefClientHandler(socket, dataInputStream, dataOutputStream);
//            applicationContext.getAutowireCapableBeanFactory().autowireBean(clientHandler);
//            clientHandler.start();
//
//
//        }
//        catch (IOException e) {
//            throw new GravityException("Could not connect to remote SANEF_SSM Service");
//        }
//        catch (Exception e) {
//            logger.error("There was an error during connection", e);
//        }
//    }
//}
