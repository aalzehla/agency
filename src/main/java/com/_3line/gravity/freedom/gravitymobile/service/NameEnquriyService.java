package com._3line.gravity.freedom.gravitymobile.service;

import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.financialInstitutions.dtos.NameEnquiryDTO;
import com._3line.gravity.freedom.financialInstitutions.service.BankProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NameEnquriyService {

    @Autowired
    private BankDetailsService bankDetailsService;
    @Autowired
    @Qualifier("magtipon")
    BankProcessor bankProcessor ;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Response getName(NameEnquiryDTO nameEnquiryDTO){
        BankDetails bankDetails =  bankDetailsService.findByCode(nameEnquiryDTO.getBankCode());
        logger.info("about changing to cbn code {} ,{}" , nameEnquiryDTO.getBankCode() , bankDetails.getCbnCode());
        nameEnquiryDTO.setBankCode(bankDetails.getCbnCode());
        return bankProcessor.nameEnquiry(nameEnquiryDTO);

    }
}