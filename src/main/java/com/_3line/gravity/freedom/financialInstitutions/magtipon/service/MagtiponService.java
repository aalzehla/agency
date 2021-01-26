package com._3line.gravity.freedom.financialInstitutions.magtipon.service;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.financialInstitutions.dtos.NameEnquiryDTO;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.utils.MySSLSocketFactory;
import com._3line.gravity.freedom.financialInstitutions.magtipon.model.MagtiponDepositRequest;
import com._3line.gravity.freedom.financialInstitutions.magtipon.model.MagtiponLogs;
import com._3line.gravity.freedom.financialInstitutions.magtipon.model.MagtiponNameResponse;
import com._3line.gravity.freedom.financialInstitutions.magtipon.model.MagtiponResponse;
import com._3line.gravity.freedom.financialInstitutions.magtipon.repository.MagtiponLogsRepository;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.utility.Utility;
import com._3line.gravity.freedom.wallet.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class MagtiponService {

    @Value("${magtiponservices.username}")
    private String username;

    @Value("${magtiponservices.primarykey}")
    private String primaryKey;

    @Value("${magtiponservices.fundtransfer}")
    private String fundsurl;

    @Value("${magtiponservices.nameEnquiry}")
    private String nameurl;

    @Value("${magtiponservices.tran_query}")
    private String tranQueryUrl;

    @Autowired
    WalletService walletService;

    @Autowired
    TransactionRepository transactionRepository;

    Gson gson = new Gson();

    @Autowired
    private MagtiponLogsRepository logsRepository;


    org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass()) ;


    public MagtiponResponse depositMoney(MagtiponDepositRequest depositRequest){
        MagtiponResponse magtiponResponse = null;
        Long systemtime = System.currentTimeMillis();

        String time = systemtime.toString().substring(0, 10);
        String encodedString = "";
        byte [] a  = Utility.sha512Byte(depositRequest.getRequestRef() + primaryKey) ;
        String sign = Base64.getEncoder().encodeToString(a) ;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            String string = time + primaryKey;

            md.update(string.getBytes());

            byte[] digest = md.digest();

            encodedString = Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MagtiponService.class.getName()).log(Level.SEVERE, null, ex);
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set("Authorization", "magtipon " + username + ":" + encodedString);
        httpHeaders.set("timestamp", time);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        logger.info("signatue is {}", depositRequest.getSignature());
        logger.info("encoded {}", sign);
        logger.info("nullifiying");
        depositRequest.setSignature(null);
        depositRequest.setSignature(sign);


        try {

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            DefaultHttpClient httpClient = new DefaultHttpClient(ccm,params);

            ObjectMapper mapper  = new ObjectMapper() ;
            logger.info("body before json {}",depositRequest.toString());
            String body = mapper.writeValueAsString(depositRequest) ;
            System.out.println("The URL *** " +fundsurl);
            org.apache.http.HttpEntity httpEntity = new ByteArrayEntity(body.getBytes(StandardCharsets.UTF_8));
            HttpPost httpPost = new HttpPost(fundsurl);
            httpPost.addHeader("Authorization","magtipon " + username + ":" + encodedString);
            httpPost.addHeader("timestamp",time);
            httpPost.addHeader("Content-Type","application/json; charset=utf-8");
            httpPost.setEntity(httpEntity);
            logger.info("sending request .......");


            MagtiponLogs logs = new MagtiponLogs() ;
            logs.setService("FUNDS_TRANSFER");
            logs.setHeaders(httpPost.getAllHeaders().toString());
            logs.setRequest(body);
            MagtiponLogs saved = logsRepository.save(logs);


            HttpResponse response = httpClient.execute(httpPost);
            String res = EntityUtils.toString(response.getEntity()) ;

            /**
             * Noticed jpa is unable to save response containing the sign ₦,
             * so had to temporarily replace for any occurrences
             * res = res.replaceAll("₦","");
             */
            res = res.replaceAll("₦","");

            logger.info("Received response is "+res);

            saved.setResponse(res);
            logsRepository.save(saved);

            magtiponResponse = new MagtiponResponse();

            magtiponResponse = gson.fromJson(res, MagtiponResponse.class) ;

            logger.info("Response after conversion to JSON "+magtiponResponse);

            if(magtiponResponse.getResponseCode().equals("90000")){
                magtiponResponse.setResponseCode("00");
                return  magtiponResponse ;
            }else {
                throw new GravityException(magtiponResponse.getResponseDescription());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return magtiponResponse;

    }

    public String nameEnquiry(NameEnquiryDTO nameEnquiryDTO){

        Long systemtime = System.currentTimeMillis();
        String time = systemtime.toString().substring(0, 10);
        String encodedString = "";
        byte [] a  = Utility.sha512Byte(systemtime + primaryKey) ;
        String sign = Base64.getEncoder().encodeToString(a) ;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            String string = time + primaryKey;
            md.update(string.getBytes());
            byte[] digest = md.digest();
            encodedString = Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MagtiponService.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nameUrl = nameurl + "/"+nameEnquiryDTO.getBankCode()+"/account/"+nameEnquiryDTO.getAccountNumber();

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            DefaultHttpClient httpClient = new DefaultHttpClient(ccm,params);

            System.out.println("The URL *** " +nameUrl);
            HttpGet httpPost = new HttpGet(nameUrl);
            httpPost.addHeader("Authorization","magtipon " + username + ":" + encodedString);
            httpPost.addHeader("timestamp",time);
            httpPost.addHeader("Content-Type","application/json; charset=utf-8");

            logger.info("sending request .......");
            HttpResponse response = httpClient.execute(httpPost);
            String res = EntityUtils.toString(response.getEntity()) ;

            logger.info("Received response is "+res);


            MagtiponNameResponse magtiponNameResponse = new MagtiponNameResponse();

            magtiponNameResponse = gson.fromJson(res, MagtiponNameResponse.class) ;

            logger.info("convert to {}", magtiponNameResponse.toString());
            if(magtiponNameResponse.getResponseCode().equals("90000")){
                return magtiponNameResponse.getAccountName();
            }


        }catch (Exception e){
            e.printStackTrace();
        }


        return "999";

    }

//    @Scheduled(fixedRate = 60000)
//    public void transactionQuery(){
//
//        Date toDate =  DateUtils.addMinutes(new Date(),-10);
//        Date toDate2 =  DateUtils.addDays(new Date(),-1);//used -3
//        logger.info("doing query for pending transactions older than : "+toDate +" and "+toDate2);
//
//        String[]tranTypes = {"Deposit"};
////        String[]tranTypes = {"Bill Payment","Deposit","Recharge","Wallet Disbursement"};
//        List<Transactions> transactionsList = transactionRepository.
//                findByTransactionTypeInAndStatusdescriptionAndTranDateLessThanAndTranDateGreaterThan(tranTypes,"PENDING",toDate,toDate2);
//
//        logger.info("count for query is :: "+transactionsList.size());
//        for(int an=0;an<transactionsList.size();an++){
//            Transactions transaction = transactionsList.get(an);
//
//            Long systemtime = System.currentTimeMillis();
//            String time = systemtime.toString().substring(0, 10);
//            String encodedString = "";
//            byte [] a  = Utility.sha512Byte(systemtime + primaryKey) ;
//            String sign = Base64.getEncoder().encodeToString(a) ;
//            try {
//                MessageDigest md = MessageDigest.getInstance("SHA-512");
//                String string = time + primaryKey;
//                md.update(string.getBytes());
//                byte[] digest = md.digest();
//                encodedString = Base64.getEncoder().encodeToString(digest);
//            } catch (NoSuchAlgorithmException ex) {
//                Logger.getLogger(MagtiponService.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            String nameUrl = tranQueryUrl + "/"+transaction.getTranId();
//
//            try {
//                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//                trustStore.load(null, null);
//
//                SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
//                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//                HttpParams params = new BasicHttpParams();
//                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//
//                SchemeRegistry registry = new SchemeRegistry();
//                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//                registry.register(new Scheme("https", sf, 443));
//
//                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
//
//                DefaultHttpClient httpClient = new DefaultHttpClient(ccm,params);
//
//                System.out.println("The URL *** " +nameUrl);
//                HttpGet httpPost = new HttpGet(nameUrl);
//                httpPost.addHeader("Authorization","magtipon " + username + ":" + encodedString);
//                httpPost.addHeader("timestamp",time);
//                httpPost.addHeader("Content-Type","application/json; charset=utf-8");
//
//                logger.info("sending request .......");
//                HttpResponse response = httpClient.execute(httpPost);
//                String res = EntityUtils.toString(response.getEntity()) ;
//
//                logger.info("Received response is "+res);
//
//
//                MagtiponNameResponse magtiponNameResponse = gson.fromJson(res, MagtiponNameResponse.class) ;
//
//                if(magtiponNameResponse.getResponseCode().equals("50003") || magtiponNameResponse.getResponseCode().equals("50001")){
//
//                    logger.info("transaction failed");
//                    DisputeDto depositDTO = new DisputeDto();
//                    depositDTO.setTranId(Long.valueOf(transaction.getTranId()));
//                    depositDTO.setAction("REVERSAL");
//                    depositDTO.setRaisedBy("SYSTEM");
//                    depositDTO.setApprovedBy("SYSTEM");
//                    depositDTO.setComment("Reversal for: "+transaction.getTranId());
//                    try{
//                        walletService.manualRaiseDispute(depositDTO);
//                        transaction.setStatusdescription("FAILED");
//                        transaction.setStatus((short) 2);
//                        transactionRepository.save(transaction);
//                    }catch(GravityException e){
//                        if(e.getMessage().contains("Transaction not Found")){
//                            transaction.setStatusdescription("FAILED");
//                            transaction.setStatus((short) 2);
//                            transactionRepository.save(transaction);
//                        }
//                        e.printStackTrace();
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }
//
//                }else if( magtiponNameResponse.getResponseCode().equals("90000") ){
//                    transaction.setStatusdescription("SUCCESSFUL");
//                    transaction.setStatus((short) 1);
//                    transactionRepository.save(transaction);
//                    logger.info("transaction was successful");
//                }else{
//                    //do nothing
//                }
//
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }
//    }
}
