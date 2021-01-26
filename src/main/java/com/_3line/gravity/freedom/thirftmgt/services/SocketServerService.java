package com._3line.gravity.freedom.thirftmgt.services;///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com._3line.app.thirftmgt.services;
//
//
//import com._3line.app.utility.MainConverter;
//import com._3line.app.utility.PackagerForAriusOnline;
//import com._3line.app.utility.isopackager.AriusPackager;
//import com._3line.app.utility.isopackager.Packager;
//import com._3line.app.wallet.service.WalletService;
//import org.jpos.iso.ISOException;
//import org.jpos.iso.ISOMsg;
//import org.jpos.iso.packager.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.net.*;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author NiyiO
// */
//@Configuration
//public class SocketServerService implements CommandLineRunner {
//
//
//    @Value("${aesoftOnline.websocketPort}")
//    private int socketPort;
//
//    @Autowired
//    WalletService walletService ;
//
//    private static final Logger LOGGER = Logger.getLogger(SocketServerService.class.getName());
//
//
//    public String unpackAndMakeDecision() {
//
//        LOGGER.info("starting socket server .......");
//        //create server socket on configured port
//        ServerSocket serverSocket = null;
//        try {
//            serverSocket = createSocket();
//        } catch (IOException ex) {
//            LOGGER.log(Level.SEVERE, null, ex);
//        }
//
//        //keep listening on port for messages
//        while (true) {
//
//            //try to read message
//            try {
//
//                //read message
//                Map<Integer,Object> res =readFromPort(serverSocket);
//                String hexMessage = (String) res.get(2);
//                ISOMsg iSOMsg;
//
//                Socket socket = (Socket) res.get(1);
//
//                //unpack message
//                iSOMsg = unpackIsoMsg(hexMessage);
//
//
//                //print ISO msg fields
//                LOGGER.log(Level.INFO, Packager.toString(iSOMsg));
//
//                //perform switch processing on a different thread
////                asyncProcessing(iSOMsg, hexMessage);
//
//
//                Packager packager = new Packager(new AriusPackager());
//
//
//
//                iSOMsg.set(0,"0210");
//                iSOMsg.set(39,"00");
//                iSOMsg.set(54,"0001566C0000003710000002566C0000003710000003566C000000000000");
//                packager.setIsoMsg(iSOMsg);
//                packager.printISOMessage();
//                String messageHexString =packager.buildISOMessage();
//
//                LOGGER.log(Level.INFO, "ISO Message {0}", messageHexString);
//
////                PrintWriter writer =  new PrintWriter(socket.getOutputStream(), true);
////
////                writer.println(messageHexString);
////
////                writer.close();
////                socket.close();
//
//                OutputStream os = socket.getOutputStream();
//                OutputStreamWriter osw = new OutputStreamWriter(os);
//                BufferedWriter bw = new BufferedWriter(osw);
//
//                bw.write(messageHexString);
//                System.out.println("Message sent to the client is "+messageHexString);
//                ISOMsg t = unpackIsoMsg(messageHexString);
////                bw.flush();
//
//                LOGGER.info("unpacking");
//                LOGGER.log(Level.INFO, Packager.toString(t));
//
//
//
//            } catch (IOException ex) {
//                LOGGER.log(Level.SEVERE, "Reading from Socket Exception", ex);
//            } catch (Exception ex) {
//                LOGGER.log(Level.SEVERE, "Exception in Unpacking ISO msg", ex);
//            }
//        }
//
//    }
//
//    private ISOMsg unpackIsoMsg(String hexMessage) throws ISOException {
//        Packager packager;
//
//        ISO87APackagerBBitmap iSO87APackagerBBitmap = new ISO87APackagerBBitmap();
//        packager = new Packager(iSO87APackagerBBitmap);
//        LOGGER.info("Trying to unpack iso hex string");
//        try {
//            ISOMsg iSOMsg = packager.unpack(hexMessage);
//            iSOMsg.setPackager(iSO87APackagerBBitmap);
//            return iSOMsg;
//        } catch (ISOException ex) {
//            LOGGER.log(Level.SEVERE, "ISO87APackagerBBitmap Failed", ex);
//            ISO87APackager iSO87APackager = new ISO87APackager();
//            packager = new Packager(iSO87APackager);
//            try {
//                ISOMsg iSOMsg = packager.unpack(hexMessage);
//                iSOMsg.setPackager(iSO87APackager);
//                return iSOMsg;
//            } catch (ISOException ex1) {
//                LOGGER.log(Level.SEVERE, "ISO87APackager Failed", ex1);
//                ISO87BPackager iSO87BPackager = new ISO87BPackager();
//                packager = new Packager(iSO87BPackager);
//                try {
//                    ISOMsg iSOMsg = packager.unpack(hexMessage);
//                    iSOMsg.setPackager(iSO87BPackager);
//                    return iSOMsg;
//                } catch (ISOException ex2) {
//                    LOGGER.log(Level.SEVERE, "ISO87BPackager Failed", ex2);
//                    ISO93APackager iSO93APackager = new ISO93APackager();
//                    packager = new Packager(iSO93APackager);
//                    try {
//                        ISOMsg iSOMsg = packager.unpack(hexMessage);
//                        iSOMsg.setPackager(iSO93APackager);
//                        return iSOMsg;
//                    } catch (ISOException ex3) {
//                        LOGGER.log(Level.SEVERE, "ISO93APackager Failed", ex3);
//                        ISO93BPackager iSO93BPackager = new ISO93BPackager();
//                        packager = new Packager(iSO93BPackager);
//                        try {
//                            ISOMsg iSOMsg = packager.unpack(hexMessage);
//                            iSOMsg.setPackager(iSO93BPackager);
//                            return iSOMsg;
//                        } catch (ISOException ex4) {
//                            LOGGER.log(Level.SEVERE, "ISO93BPackager Failed", ex4);
//                            throw new ISOException();
//                        }
//
//                    }
//                }
//            }
//        }
//
//    }
//
////    @Async
////    private void asyncProcessing(ISOMsg iSOMsg, String messageHex) throws ISOException, UnknownHostException, IOException, NoBankException, CreditRequestException {
////
////        //Get important fields to determine operation
////        String mti = iSOMsg.getMTI();
////        String processingCode = iSOMsg.getString("3");
////        String pan = iSOMsg.getString("2");
////        String leadZeroPaddingAmount = iSOMsg.getString("4");
////        String feeStr = iSOMsg.getString("28");
////
////        //Get first 10 digits bank code from Pan
////        String bankCode = pan.substring(0, 10);
////
////        String amountStr = Packager.removeLeadingZeros(leadZeroPaddingAmount);
////
////        double amount = Double.parseDouble(amountStr);
////
////        double fee = Double.parseDouble(feeStr);
////
////        System.out.println(mti);
////        System.out.println(pan);
////        System.out.println(bankCode);
////        System.out.println(processingCode);
////        System.out.println(leadZeroPaddingAmount);
////        System.out.println(Objects.isNull(feeStr) ? "No fee" : feeStr);
////
////        ApiLog apiLog = new ApiLog();
////
////        apiLog = apiLogRepo.save(apiLog);
////
////        //find bank with pan
////        Optional<Bank> optionalBank = bankRepository.findByBankCode(bankCode);
////        Bank bank = optionalBank.orElseThrow(() -> {
////            return new NoBankException("No such Bank with BankCode " + bankCode); //To change body of generated lambdas, choose Tools | Templates.
////        });
////
////        if (mti.equalsIgnoreCase("0200")) {//transaction request
////
////            CreditRequest creditRequest = new CreditRequest();
////            creditRequest.setPan(pan);
////            creditRequest.setNarration("Enquiry");
////            creditRequest.setAmount("0");
////
////            creditRequest.setSenderInfo("3Line");
////            creditRequest.setRefNumber(Packager.zeroPad(apiLog.getId().toString(), 6, "0"));
////            creditRequest.setRegNum(bank.getRegNum().toString());
////
////            if (processingCode.equalsIgnoreCase("010000")) {//withdrawal
////
////                CreditResponse creditResponse = cmsApiService.sendCreditRequest(creditRequest);
////
////                if (creditResponse.getStatusCode().contentEquals("0")) {
////
////                    double balance = Double.parseDouble(creditResponse.getBalance());
////
////                    if (balance >= (amount + fee)) {//Yes enough balance to honour request
////
////                        forwardMessageToBank(messageHex, bank.getIp(), bank.getPort());
////
////                    } else {
////                        //decline request
////
////                        iSOMsg.set("44", "Insufficient Balance");
////                        iSOMsg.set("39", "99");
////
////                        byte[] isoByteArray = iSOMsg.pack();
////
////                        //try this
////                        String bytesToHex = Packager.bytesToHex(isoByteArray);
////
////                        //else use this
//////                        String bytesToHex = MainConverter.hexify(isoByteArray);
////                        forwardMessageToBank(bytesToHex, bank.getIp(), bank.getPort());
////
////                    }
////
////                }
////
////            } else if (processingCode.equalsIgnoreCase("000000")) {//purchase
////
////                CreditResponse creditResponse = cmsApiService.sendCreditRequest(creditRequest);
////
////                if (creditResponse.getStatusCode().contentEquals("0")) {
////
////                    double balance = Double.parseDouble(creditResponse.getBalance());
////
////                    if (balance >= (amount + fee)) {//Yes enough balance to honour request
////
////                        forwardMessageToBank(messageHex, bank.getIp(), bank.getPort());
////
////                    } else {
////                        //decline request
////
////                        iSOMsg.set("44", "Insufficient Balance");
////                        iSOMsg.set("39", "99");
////
////                        byte[] isoByteArray = iSOMsg.pack();
////
////                        //try this
////                        String bytesToHex = Packager.bytesToHex(isoByteArray);
////
////                        //else use this
//////                        String bytesToHex = MainConverter.hexify(isoByteArray);
////                        forwardMessageToBank(bytesToHex, bank.getIp(), bank.getPort());
////
////                    }
////
////                }
////
////            }
////
////        } else if (mti.equalsIgnoreCase("0420")) {//reversal request
////
////            //some logic to done before forwardingToBank. Yet to be done
////            forwardMessageToBank(messageHex, bank.getIp(), bank.getPort());
////        } else {
////
////            forwardMessageToBank(messageHex, bank.getIp(), bank.getPort());
////        }
////
////    }
//
//    @Async
//    void forwardMessageToBank(String messageHex, String ip, int port) throws UnknownHostException, IOException {
//        //convert messageHex to byteArray
//        byte[] messageByteArray = MainConverter.parseHexString(messageHex);
//
//        //create socket address with bank ip and port
//        InetAddress inetAddress = InetAddress.getByName(ip);
//        SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
//
//        //instantiate socket; set keepalive and timeout
//        Socket socket = new Socket();
//        socket.connect(socketAddress);
//        socket.setKeepAlive(true);
//        socket.setSoTimeout(60000);
//
//        //get socket output stream
//        OutputStream outputStream = socket.getOutputStream();
//        outputStream.write(messageByteArray);
//        outputStream.flush();
//    }
//
//    private ServerSocket createSocket() throws IOException {
//        ServerSocket serverSocket = null;
//        serverSocket = new ServerSocket(socketPort);
//        LOGGER.log(Level.INFO, "Server Started and listening to the port {0}", socketPort);
//
//        return serverSocket;
//    }
//
//    private Map<Integer ,Object> readFromPort(ServerSocket serverSocket) throws IOException {
//
//        Socket socket = serverSocket.accept();
//        InputStream inputStream = socket.getInputStream();
//
//        byte[] messageLengthByteArray = new byte[2];
//        int messageLengthByte = inputStream.read(messageLengthByteArray);
//        StringBuffer sb = null;
//        String messageLengthHex = null;
//        String messageDataHex = "";
//        int messageDataByte = 0;
//
//        messageLengthHex = MainConverter.hexify(messageLengthByteArray);
//        messageDataHex = Packager.removeLeadingZeros(messageLengthHex);
//        messageDataByte = Integer.parseInt(messageDataHex, 16);
//
//        try {
//            sb = new StringBuffer(50);
//            sb.append("messageLengthByte:").append(messageLengthByte);
//            messageLengthHex = MainConverter.hexify(messageLengthByteArray);
//            sb.append(", messageLengthHex:").append(messageLengthHex);
//            messageDataHex = Packager.removeLeadingZeros(messageLengthHex);
//            sb.append(", messageDataHex:").append(messageDataHex);
//            messageDataByte = Integer.parseInt(messageDataHex, 16);
//            sb.append(", messageDataByte:").append(messageDataByte);
//        } finally {
//            LOGGER.log(Level.INFO, sb.toString());
//        }
//
//        // Reading the message itself; MessageDataProcitedDateByte is the number of received bytes
//        byte[] messageByteArray = new byte[messageDataByte];
//        int messageDataProcitedDataByte = inputStream.read(messageByteArray); // IOException - if an I/O error occurs
//        String messageHexString = MainConverter.hexify(messageByteArray);
//
//        LOGGER.log(Level.INFO, "ISO Message {0}", messageHexString);
//
//        Map<Integer , Object> response = new HashMap<>();
//        response.put(2,messageHexString) ;
//        response.put(1,socket);
//        return response ;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        unpackAndMakeDecision();
//    }
//}
