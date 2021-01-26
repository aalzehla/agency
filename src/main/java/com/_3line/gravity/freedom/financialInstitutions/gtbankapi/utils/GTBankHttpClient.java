package com._3line.gravity.freedom.financialInstitutions.gtbankapi.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.List;

/**
 * @author Kwere
 */
@Service
public class GTBankHttpClient {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${gtbank.url}")
    private String GTBankServiceURL;


    public GTBankHttpClient() {

    }

    /**
     * This calls the GT Bank Services
     * to execute the requests
     *
     * @return boolean
     */

     public static void main(String ...args)
     {
         String toconvert = "<DoTransaction><Login><username>AriusAdmin</username><password>admin</password></Login><Transact><transid>5122826</transid><transtype>203</transtype><terminalid>W145150F</terminalid><stan>0000016128</stan><pan>6395875597000122189</pan><accountno_from>8888888145105</accountno_from><accountno_to>9995289167016</accountno_to><amount>000000420000</amount><fee>000000000000</fee><ExtendedTranCode></ExtendedTranCode><date>02/08/2018</date></Transact></DoTransaction>";
         System.out.printf("converted %s" , toconvert.replace("<","&lt;").replace(">","&gt;"));
     }

        public String sendHttpRequest(String payload, String soapActionValue,boolean replaceTag) {
        String action = "";
        logger.info("Calling GTBankServiceURL with payload -- {} ", payload);

        if(replaceTag) {
            payload = payload.replace("&lt;", "<");
            payload = payload.replace("&gt;", ">");
        }else {
            payload = payload.replace("&amp;","&");
        }
        payload = payload.replace("<arg_0_0>","");
        payload = payload.replace("</arg_0_0>","");
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
            logger.info("Payload now {}", payload);
            HttpEntity httpEntity = new ByteArrayEntity(payload.getBytes(StandardCharsets.UTF_8));
            logger.info("Received http Entity is "+httpEntity);

            System.out.println("The URL *** " +GTBankServiceURL);
            HttpPost httpPost = new HttpPost(GTBankServiceURL);
            httpPost.addHeader("Content-Type","text/xml");
            httpPost.addHeader("SOAPAction",soapActionValue);


            httpPost.setEntity(httpEntity);

            HttpResponse response = httpClient.execute(httpPost);
            logger.info("Received response111 is "+response);

            String responseMessage = EntityUtils.toString(response.getEntity());
            logger.info("Received response Message is "+responseMessage);

            List<String> output = null;

            logger.info("Received response is "+responseMessage);
            if(responseMessage!=null && !responseMessage.isEmpty()){
                CharSequence lessThanEscaped ="&lt;";
                CharSequence lessThan ="<";
                CharSequence greaterThanEscaped ="&gt;";
                CharSequence greaterThan =">";

                return responseMessage.replace(lessThanEscaped,lessThan).replace(greaterThanEscaped, greaterThan).replace("&amp;gt;",">").replace("&amp;lt;","<");
            }

            return responseMessage;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: "+e.getMessage());

        }

        return "NO RESPONSE";

    }

}
