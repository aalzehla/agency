package com._3line.gravity.freedom.financialInstitutions.stanbicibtc.utility;

import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.services.StanbicSSLFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

/**
 * @author Kwerenachi Utosu
 * @date 10/15/2019
 */
@Service
public class StanbicHttpClient {

    private Logger logger = LoggerFactory.getLogger(StanbicHttpClient.class);

    @Value("${stanbic.authorizationid}")
    private String authorizationId;

    @Value("${stanbic.moduleIdacc}")
    private String moduleId;

    @Value("${stanbic.channel}")
    private String channel;

    @Value("${stanbic.contenttype}")
    private String contentType;

    public String sendTokenRequest(String payload, String url, String agentId) {

        String responseMessage = "";
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new StanbicSSLFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            DefaultHttpClient httpClient = new DefaultHttpClient(ccm,params);

            HttpEntity httpEntity = new ByteArrayEntity(payload.getBytes(StandardCharsets.UTF_8));

            logger.info("Received http Entity is " + httpEntity);
            logger.info("The Bank Url {}", url);
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", contentType);
            httpPost.addHeader("ModuleId", moduleId);
            httpPost.addHeader("channel", channel);
            httpPost.addHeader("AuthorizationId", authorizationId);
            httpPost.addHeader("AgentId", agentId);

            httpPost.setEntity(httpEntity);
            HttpResponse response = httpClient.execute(httpPost);
            logger.info("Http Response Message {} ", response);
            responseMessage = EntityUtils.toString(response.getEntity());
            logger.info("Received response Message is {} ", responseMessage);
            return responseMessage;

        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
            return responseMessage+e.getMessage();
        }

    }


    public String sendRequest(String payload, String url, String authorization, String agentId) {

        String responseMessage = "";
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new StanbicSSLFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            DefaultHttpClient httpClient = new DefaultHttpClient(ccm,params);

            HttpEntity httpEntity = new ByteArrayEntity(payload.getBytes(StandardCharsets.UTF_8));

            logger.info("Received http Entity is " + httpEntity);
            logger.info("The Bank Url {}", url);
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", contentType);
            httpPost.addHeader("ModuleId", moduleId);
            httpPost.addHeader("channel", channel);
            httpPost.addHeader("AuthorizationId", authorizationId);
            httpPost.addHeader("AgentId", agentId);
            httpPost.addHeader("Authorization", "Bearer "+authorization);

            logger.info("Logging the headers {}", httpPost.getHeaders("ModuleId"));
            logger.info("Logging the headers {}", httpPost.getHeaders("channel"));
            logger.info("Logging the headers {}", httpPost.getHeaders("AuthorizationId"));
            logger.info("Logging the headers {}", httpPost.getHeaders("AgentId"));

            httpPost.setEntity(httpEntity);
            HttpResponse response = httpClient.execute(httpPost);
            logger.info("Stanbic Http Response Message {} ", response);
            responseMessage = EntityUtils.toString(response.getEntity());
            logger.info("Received Stanbic response Message is {} ", responseMessage);
            return responseMessage;

        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
            return responseMessage+e.getMessage();
        }

    }

}
