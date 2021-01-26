package com._3line.gravity.freedom.gravitymobile.service;


import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.financialInstitutions.accountopening.service.AccountService;
import com._3line.gravity.freedom.financialInstitutions.dtos.AccOpeningGeneral;
import com._3line.gravity.freedom.financialInstitutions.fidelity.service.FidelityWebServices;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountOpeningResponse;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountOpeningService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FidelityWebServices fidelityWebServices ;

    @Autowired
    BankDetailsService bankDetailsService ;

    @Autowired
    AccountService bankServices;

    public Response openAccount(AccOpeningGeneral accOpeningGeneral){
        Response response = new Response() ;
        logger.info("############ OPENING ACCOUNT ##########");
        AccOpeningGeneral res = bankServices.openAccountGeneral(accOpeningGeneral) ;
        logger.info("The Account Response {}", res);
        if(res!=null && res.getResponseCode().equals("00")){
            response.setRespCode("00");
            Map data = new HashMap();
            data.put("accountNumber",res.getAccountNumber());
            response.setRespBody(res);
            if (res.getResponseDescription() == null && res.getResponseDescription().isEmpty()){
            response.setRespDescription("successful");}
            else { response.setRespDescription(res.getResponseDescription());}

        }else {
            response.setRespCode(res.getResponseCode());
            response.setRespDescription(res.getResponseDescription());
            response.setRespBody(res);
        }

        return response ;
    }

    public SanefAccountOpeningResponse openSanefAccount(SanefAccountRequest accOpeningGeneral){
        logger.info("############ OPENING ACCOUNT ##########");

        SanefAccountOpeningResponse res = bankServices.openAccountSanef(accOpeningGeneral) ;
        logger.info("The Account Response {}", res);
        return res ;
    }
}
