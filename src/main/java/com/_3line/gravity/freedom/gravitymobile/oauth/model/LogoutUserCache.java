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
public class LogoutUserCache {

    private Cache<String, String> userCache;

    public LogoutUserCache() {
        if (userCache == null) {
            userCache = CacheBuilder.newBuilder().maximumSize(Constants.MaximumParallelUser).expireAfterWrite(Constants.CacheReset, TimeUnit.MINUTES).build();
                              }
    }

    public Cache<String, String> getUserCache() {
        return userCache;
    }

}
