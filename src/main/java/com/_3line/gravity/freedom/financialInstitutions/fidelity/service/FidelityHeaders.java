package com._3line.gravity.freedom.financialInstitutions.fidelity.service;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Security;

@Component
public class FidelityHeaders {

    @Value("${fidelityapi.username}")
    private String username;

    @Value("${fidelityapi.password}")
    private String password;

    @Value("${fidelityapi.key}")
    private String apiKey;

    @Autowired
    private CipherUtils cipherUtils;

    public HttpHeaders create(String agentAccount) {

        Security.addProvider(new BouncyCastleProvider());
        String paddedUserId = cipherUtils.padRight(agentAccount, 24, '0');
        System.out.println("Padded UserId: " + paddedUserId);
        String data = apiKey + paddedUserId;
        System.out.println("Data: " + data);
        String authorization = cipherUtils.encode(data);
        System.out.println("Authorization: " + authorization);

        String user_pass = username + ":" + password;
        System.out.println("UsernameAndPassword: " + user_pass);
        String signature = cipherUtils.encrypt(user_pass);
        System.out.println("Signature: " + signature);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "bearer " + authorization);
        headers.add("Signature", signature);
        headers.add("agentid", agentAccount);
        headers.add("Accept", "application/json");
        return headers;
    }
}
