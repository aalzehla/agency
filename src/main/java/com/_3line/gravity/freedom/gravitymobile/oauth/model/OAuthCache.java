/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.gravitymobile.oauth.model;

import com._3line.gravity.freedom.utility.Constants;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author OlalekanW
 */
@Component
@Scope("singleton")
public class OAuthCache {

    private Cache<String, AgentsDetails> oauthCache;

    public OAuthCache() {
        oauthCache = CacheBuilder.newBuilder().maximumSize(5000).expireAfterWrite(60, TimeUnit.MINUTES).build();
    }

    public Cache<String, AgentsDetails> getOauthCache() {
        return oauthCache;
    }

}
