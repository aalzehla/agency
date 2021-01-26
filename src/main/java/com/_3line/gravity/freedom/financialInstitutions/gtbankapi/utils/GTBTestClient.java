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
public class GTBTestClient {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${gtbank.url}")
    private String GTBankServiceURL;


    public GTBTestClient() {

    }

    /**
     * This calls the GT Bank Services
     * to execute the requests
     *
     * @return boolean
     */


    public String sendHttpRequest(String soapActionValue) {
        String payload = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:fil=\"http://tempuri.org/GAPS_Uploader/FileUploader\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <fil:SingleTransfers>\n" +
                "         <!--Optional:-->\n" +
                "         <fil:xmlRequest><![CDATA[<SingleTransfers><transdetails>&lt;transactions&gt;&lt;transaction&gt;&lt;amount&gt;11000.00&lt;/amount&gt;&lt;paymentdate&gt;2018/08/02&lt;/paymentdate&gt;&lt;reference&gt;5b615970d2d9b&lt;/reference&gt;&lt;remarks&gt;Sola-5b615970d2d9b&lt;/remarks&gt;&lt;vendorcode&gt;Sola&lt;/vendorcode&gt;&lt;vendorname&gt;Sola&lt;/vendorname&gt;&lt;vendoracctnumber&gt;0148209090&lt;/vendoracctnumber&gt;&lt;vendorbankcode&gt;058152052&lt;/vendorbankcode&gt;&lt;ltcustomeracctnumber&gt;0004527849&lt;/ltcustomeracctnumber&gt;&lt;/transaction&gt;&lt;/transactions&gt;</transdetails><accesscode>20514001902</accesscode><username>adewotol2</username><password>abcd1234*</password><hash>C81223752F5B0505700224D64393E746E6C8EC7123BC68640760FFAB47D762C4FCEBC785A65588228BAF4535AC8C640773936DDCD9CCCFBFB1916F655EA52B90</hash></SingleTransfers>]]></fil:xmlRequest>\n" +
                "      </fil:SingleTransfers>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        String action = "";
        logger.info("Calling GTBankServiceURL with payload -- {} ", payload);

//        payload = payload.replace("&lt;","<");
//        payload = payload.replace("&gt;",">");
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
