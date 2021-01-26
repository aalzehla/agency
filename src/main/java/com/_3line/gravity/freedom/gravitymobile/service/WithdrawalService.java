package com._3line.gravity.freedom.gravitymobile.service;



import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.financialInstitutions.dtos.GravityWithdrawalRequest;
import com._3line.gravity.freedom.financialInstitutions.opay.models.OTPRequest;

import java.util.Map;


public interface WithdrawalService {



     Response cardWithdrawal(Map<String, String> param, String username, GravityWithdrawalRequest jsonRequest) ;

     Response otp(OTPRequest jsonRequest) ;

}
