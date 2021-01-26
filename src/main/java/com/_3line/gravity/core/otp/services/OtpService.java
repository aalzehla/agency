package com._3line.gravity.core.otp.services;


import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.otp.model.OtpRequest;

/**
 * @author FortunatusE
 * @date 3/21/2019
 */
public interface OtpService {

//    void generateAndSendOtp(String username);todo revert back to void
    String generateAndSendOtp(String username);

    boolean validateOTP(OtpRequest otpRequest, boolean checkExpirationTime) throws GravityException;
}
