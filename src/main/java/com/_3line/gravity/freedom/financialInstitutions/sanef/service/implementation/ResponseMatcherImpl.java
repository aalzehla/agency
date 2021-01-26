package com._3line.gravity.freedom.financialInstitutions.sanef.service.implementation;

import com._3line.gravity.freedom.financialInstitutions.sanef.service.ResponseMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseMatcherImpl implements ResponseMatcher {
    private static final Logger logger = LoggerFactory.getLogger(ResponseMatcherImpl.class);
    private String request;
    private String matchKey;
    private String response;

    public ResponseMatcherImpl(String message) {
        this.request = message;
        this.matchKey = getKey(message);
    }

    @Override
    public String getResponse() {
        return this.response;
    }

    @Override
    public String getKey() {
        return matchKey;
    }


    @Override
    public void setResponse(String message) {
        this.response = message;
    }

    public static String getKey(String message) {
        return message;
    }
}
