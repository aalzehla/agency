package com._3line.gravity.core.sms.service;

import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public interface SmsService {

    @Async
    void sendSms(String recepient , Map<String , Object> params , String templateName);

    @Async
    void sendPlainSms(String recepient , String message);
}
