/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.gravitymobile.service;

import com._3line.gravity.core.cryptography.SecretKeyCrypt;
import com._3line.gravity.core.cryptography.exceptions.CryptographyException;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.agents.service.SecurityDao;
import com._3line.gravity.freedom.gravitymobile.oauth.model.AgentsDetails;
import com._3line.gravity.freedom.gravitymobile.oauth.model.OAuth2Response;
import com._3line.gravity.freedom.gravitymobile.oauth.model.OAuthCache;
import com._3line.gravity.freedom.utility.PropertyResource;
import com._3line.gravity.freedom.utility.Utility;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author OlalekanW
 */
@Service
public class OAuth2Service {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PropertyResource pr ;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder ;

    @Autowired
    SecretKeyCrypt secretKeyCrypt ;

    @Autowired
    private SecurityDao dao;

    @Autowired
    private OAuthCache oauthCache;

    public AgentsDetails validateOauthAccessRequestHeaders(HttpServletRequest request) throws GravityException {
        Map<String, String> headers = Utility.getHeaders(request);
        return validateAuthorisation(headers.get("authorization"));

    }

    public String generateOauthToken(AgentsDetails usernameuserid) {
        String oauthtoken = Utility.sha512(Utility.randomString(30));
        oauthCache.getOauthCache().put(oauthtoken, usernameuserid);
        return oauthtoken;
    }

    public Map<String, String> validateRequestHeaders(HttpServletRequest request, boolean skipAuthorization) throws GravityException {
        Map<String, String> headers = Utility.getHeaders(request);

        Response response = new Response();

//        if (headers.get("timestamp") == null) {
//            response.setRespCode("113");
//            response.setRespDescription(pr.getV("113", "response.properties"));
//            logger.warn(response.getRespDescription());
//            throw new GravityException("", response);
//        }
//        if (headers.get("timestamp").length() != 10) {
//            response.setRespCode("114");
//            response.setRespDescription(pr.getV("114", "response.properties"));
//            logger.warn(response.getRespDescription());
//            throw new GravityException("", response);
//        }
//        if (headers.get("nonce") == null) {
//            response.setRespCode("115");
//            response.setRespDescription(pr.getV("115", "response.properties"));
//            logger.warn(response.getRespDescription());
//            throw new GravityException("", response);
//        }
//        if (headers.get("signature") == null) {
//            response.setRespCode("116");
//            response.setRespDescription(pr.getV("116", "response.properties"));
//            logger.warn(response.getRespDescription());
//            throw new GravityException("", response);
//        }

        if (!skipAuthorization) {
            String Authorisation = headers.get("authorization");
            if (Authorisation == null) {
                response.setRespCode("100");
                response.setRespDescription(pr.getV("100", "response.properties"));
                logger.warn(response.getRespDescription());
                throw new GravityException("", response);
            }

            String[] value = Authorisation.split("\\s+");
            if (value.length != 2) {
                response.setRespCode("111");
                response.setRespDescription(pr.getV("111", "response.properties"));
                logger.warn(response.getRespDescription());
                throw new GravityException("", response);
            }
            if (!value[0].equalsIgnoreCase("Bearer")) {
                response.setRespCode("112");
                response.setRespDescription(pr.getV("112", "response.properties"));
                logger.warn(response.getRespDescription());
                throw new GravityException("", response);
            }

            if (oauthCache.getOauthCache() != null) {
                AgentsDetails agentsdetails = oauthCache.getOauthCache().getIfPresent(value[1]);
                if (agentsdetails != null) {
                    headers.put("username", agentsdetails.getUsername());
                    headers.put("clientid", agentsdetails.getClientId());
                    headers.put("userid", agentsdetails.getUserId());
                    headers.put("belongsto", agentsdetails.getBelongsTo());
                } else {
                    response.setRespCode("109");
                    response.setRespDescription(pr.getV("109", "response.properties"));
                    logger.warn(response.getRespDescription());
                    throw new GravityException("", response);
                }
            } else {
                response.setRespCode("110");
                response.setRespDescription(pr.getV("110", "response.properties"));
                logger.warn(response.getRespDescription());
                throw new GravityException("", response);
            }

        }

        return headers;

    }

    public Map<String, String> validateRequestHeaders2(Map<String, String> headers, boolean skipAuthorization) throws GravityException {
//        Map<String, String> headers = Utility.getHeaders(request);

        Response response = new Response();

        if (headers.get("timestamp") == null) {
            response.setRespCode("113");
            response.setRespDescription(pr.getV("113", "response.properties"));
            logger.warn(response.getRespDescription());
            throw new GravityException("", response);
        }
        if (headers.get("timestamp").length() != 10) {
            response.setRespCode("114");
            response.setRespDescription(pr.getV("114", "response.properties"));
            logger.warn(response.getRespDescription());
            throw new GravityException("", response);
        }
        if (headers.get("nonce") == null) {
            response.setRespCode("115");
            response.setRespDescription(pr.getV("115", "response.properties"));
            logger.warn(response.getRespDescription());
            throw new GravityException("", response);
        }
        if (headers.get("signature") == null) {
            response.setRespCode("116");
            response.setRespDescription(pr.getV("116", "response.properties"));
            logger.warn(response.getRespDescription());
            throw new GravityException("", response);
        }

        if (!skipAuthorization) {
            String Authorisation = headers.get("authorization");
            if (Authorisation == null) {
                response.setRespCode("100");
                response.setRespDescription(pr.getV("100", "response.properties"));
                logger.warn(response.getRespDescription());
                throw new GravityException("", response);
            }

            String[] value = Authorisation.split("\\s+");
            if (value.length != 2) {
                response.setRespCode("111");
                response.setRespDescription(pr.getV("111", "response.properties"));
                logger.warn(response.getRespDescription());
                throw new GravityException("", response);
            }
            if (!value[0].equalsIgnoreCase("Bearer")) {
                response.setRespCode("112");
                response.setRespDescription(pr.getV("112", "response.properties"));
                logger.warn(response.getRespDescription());
                throw new GravityException("", response);
            }

            if (oauthCache.getOauthCache() != null) {
                AgentsDetails agentsdetails = oauthCache.getOauthCache().getIfPresent(value[1]);
                if (agentsdetails != null) {
                    headers.put("username", agentsdetails.getUsername());
                    headers.put("clientid", agentsdetails.getClientId());
                    headers.put("userid", agentsdetails.getUserId());
                    headers.put("belongsto", agentsdetails.getBelongsTo());
                } else {
                    response.setRespCode("109");
                    response.setRespDescription(pr.getV("109", "response.properties"));
                    logger.warn(response.getRespDescription());
                    throw new GravityException("", response);
                }
            } else {
                response.setRespCode("110");
                response.setRespDescription(pr.getV("110", "response.properties"));
                logger.warn(response.getRespDescription());
                throw new GravityException("", response);
            }

        }

        return headers;

    }

    private AgentsDetails validateAuthorisation(String Authorisation) throws GravityException {

        OAuth2Response response = new OAuth2Response();

        if (Authorisation == null) {
            response.setRespCode("100");
            response.setRespDescription(pr.getV("100", "response.properties"));
            logger.warn(response.getRespDescription());
            throw new GravityException(response.getRespDescription(), response);
        }
        String[] value = Authorisation.split("\\s+");
        if (value.length != 2) {
            response.setRespCode("101");
            response.setRespDescription(pr.getV("101", "response.properties"));
            logger.warn(response.getRespDescription());
            throw new GravityException(response.getRespDescription(), response);
        }
        if (!value[0].equalsIgnoreCase("Basic")) {
            response.setRespCode("102");
            response.setRespDescription(pr.getV("102", "response.properties"));
            logger.warn(response.getRespDescription());
            throw new GravityException(response.getRespDescription(), response);
        }
        try {
            value[1] = new String(Base64.decode(value[1]), StandardCharsets.UTF_8);
        } catch (Base64DecodingException ex) {
            response.setRespCode("103");
            response.setRespDescription(pr.getV("103", "response.properties"));
            logger.warn(response.getRespDescription());
            throw new GravityException(response.getRespDescription(), response);
        }
        value = value[1].split(":");
        if (value.length != 2) {
            response.setRespCode("104");
            response.setRespDescription(pr.getV("104", "response.properties"));
            logger.warn(response.getRespDescription());
            throw new GravityException(response.getRespDescription(), response);
        }
        try {
            Map secretkeyusername = dao.getSecretkeyByClientId(value[0]);
            if (!value[1].equals(secretkeyusername.get("secretkey"))) {
                response.setRespCode("105");
                response.setRespDescription(pr.getV("105", "response.properties"));
                logger.warn(response.getRespDescription());
                throw new GravityException(response.getRespDescription(), response);
            }
            if ((Integer) secretkeyusername.get("status") == 0) {
                response.setRespCode("141");
                response.setRespDescription(pr.getV("141", "response.properties"));
                logger.warn(response.getRespDescription());
                throw new GravityException(response.getRespDescription(), response);
            }
            return new AgentsDetails((String) secretkeyusername.get("username"), secretkeyusername.get("agentId") + "",
                    value[0], secretkeyusername.get("BelongsTo") + "");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setRespCode("105");
            response.setRespDescription(pr.getV("105", "response.properties"));
            logger.warn(response.getRespDescription());
            throw new GravityException(response.getRespDescription(), response);
        }

    }

    public void validateBody(HttpServletRequest request) throws GravityException {
        String requestbody = Utility.getRequestBody(request);
        String jsonRequest;
        OAuth2Response response = new OAuth2Response();
        if (requestbody == null) {
            response.setRespCode("106");
            response.setRespDescription(pr.getV("106", "response.properties"));
            logger.warn(response.getRespDescription());
            throw new GravityException("", response);
        }
        //request decrypted
        try {
            System.out.println(String.format(" \r\n############\r\n request raw : %s", requestbody));
            jsonRequest = secretKeyCrypt.decrypt(requestbody);
            System.out.println(String.format(" \r\n############\r\n request decrypted: %s", jsonRequest));
        } catch (CryptographyException e) {
            response.setRespCode("107");
            response.setRespDescription(pr.getV("107", "response.properties"));
            logger.warn(response.getRespDescription());
            throw new GravityException("", response);
        }

        //grant_type=client_credentials
        if (!jsonRequest.replaceAll("\\s+", "").equalsIgnoreCase("grant_type=client_credentials")) {
            response.setRespCode("108");
            response.setRespDescription(pr.getV("108", "response.properties"));
            logger.warn(response.getRespDescription());
            throw new GravityException("", response);
        }
    }

}
