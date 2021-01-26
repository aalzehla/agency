/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com._3line.gravity.freedom.utility;

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
public class OtpCache {

    private Cache<String, String> keyCache;

    public OtpCache() {
        if (keyCache == null) {
            keyCache = CacheBuilder.newBuilder().maximumSize(Constants.MaximumParallelUser).expireAfterWrite(Constants.OtpExpiryMin, TimeUnit.MINUTES).build();
                              }
                      }
 
    public Cache<String, String> getOtpCache() {
        return keyCache;
                                               }

}
