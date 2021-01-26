package com._3line.gravity.freedom.financialInstitutions.magtipon;


import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.financialInstitutions.dtos.*;
import com._3line.gravity.freedom.financialInstitutions.magtipon.model.*;
import com._3line.gravity.freedom.financialInstitutions.magtipon.service.MagtiponService;
import com._3line.gravity.freedom.financialInstitutions.service.BankProcessor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Qualifier("magtipon")
public class MagtiponBankProcessor implements BankProcessor {

    @Autowired
    MagtiponService magtiponService ;
    @Autowired
    ModelMapper modelMapper ;

    Logger logger = LoggerFactory.getLogger(this.getClass()) ;

    @Override
    public Response performDeposit(DepositRequest depositRequest,Long tranId) {
        try {
            MagtiponDepositRequest request = new MagtiponDepositRequest();
            request.setAmount(depositRequest.getAmount());
            MagtiponCustomerDetails customerDetails = new MagtiponCustomerDetails();
            customerDetails.setEmail(depositRequest.getCustomerEmail());
            customerDetails.setFullname(depositRequest.getCustomerName());
            customerDetails.setMobilePhone(depositRequest.getCustomerPhone());
            request.setCustomerDetails(customerDetails);
            MagtiponBeneficiaryDetails beneficiaryDetails = new MagtiponBeneficiaryDetails();
            beneficiaryDetails.setEmail(depositRequest.getCustomerEmail());
            beneficiaryDetails.setFullname(depositRequest.getCustomerName());
            beneficiaryDetails.setMobilePhone(depositRequest.getCustomerPhone());
            request.setBeneficiaryDetails(beneficiaryDetails);
            MagtiponBankDetails magtiponBankDetails = new MagtiponBankDetails();
            magtiponBankDetails.setBankCode(depositRequest.getBankCode());
            magtiponBankDetails.setBankType("comm");
            magtiponBankDetails.setAccountNumber(depositRequest.getAccountNumber());
            magtiponBankDetails.setAccountType("10");
            request.setBankDetails(magtiponBankDetails);
            request.setRequestRef(String.valueOf(tranId));
            MagtiponResponse r = magtiponService.depositMoney(request) ;

            if (r == null) {
                Response response = new Response();
                response.setRespCode("999");
                response.setRespDescription("Issuer or switch Inoperative");
                return response ;
            }else if(r.getResponseCode().equals("00")){
                Response response = new Response();
                response.setRespCode("00");
                response.setRespDescription(r.getTransactionRef());
                return response ;
            }else{
                Response response = new Response();
                response.setRespCode(r.getResponseCode());
                response.setRespDescription(r.getResponseDescription());
                return response ;
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.getLocalizedMessage());
            logger.info("error using magtipon {}" , e.getMessage());
            Response response = new Response();
            response.setRespCode("999");
            response.setRespDescription(e.getMessage());
            return response ;
        }

    }

    @Override
    public Response performWithdrawal(GravityWithdrawalRequest gravityWithdrawalRequest) {
        return null;
    }

    @Override
    public Response performTransfer(TransferRequest transferRequest) {
        return null;
    }

    @Override
    public Response enterPin(EnterPinDTO enterPinDTO) {
        return null;
    }

    @Override
    public Response enterOtp(EnterOtpDTO enterOtpDTO) {
        return null;
    }

    @Override
    public Response nameEnquiry(NameEnquiryDTO nameEnquiryDTO) {
        String res  = magtiponService.nameEnquiry(nameEnquiryDTO);

        Response response = new Response();

        if(res.equals("999")){
            response.setRespCode("999");
            response.setRespDescription("Not Found");
        }else {
            response.setRespCode("00");
            Map name = new HashMap<>();
            name.put("name", res);
            name.put("accountNumber", nameEnquiryDTO.getAccountNumber());
            response.setRespBody(name);
            response.setRespDescription("found");
        }
        return response;
    }
}
