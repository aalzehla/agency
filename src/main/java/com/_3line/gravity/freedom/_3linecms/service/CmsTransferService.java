package com._3line.gravity.freedom._3linecms.service;

import com._3line.gravity.core.exceptions.GravityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * A transfer service. This service communicates with the CMS API for credit request.
 * Created by FortunatusE on 8/27/2018.
 */

@Service
public class CmsTransferService {


//    @Value("${quickteller.cms.url}")
    private String CMS_WSDL_URL;

//    @Value("${cems.cooperative.rgnum}")
    private String regNum;

    @Autowired
    private CustomHttpClient httpClient;

    @Autowired
    private TemplateEngine templateEngine;


    private static final Logger logger = LoggerFactory.getLogger(CmsTransferService.class);


    /**
     * Sends a credit request to the CMS API to make fund transfer.
     *
     * @param creditRequest an object with details of the request
     * @return response from the API
     * @throws GravityException if no response is recieved from the service
     */
    public CreditResponse sendCreditRequest(CreditRequest creditRequest) throws GravityException {

        logger.info("Sending credit request: {}", creditRequest);

        String payLoad = preparePayLoad(creditRequest);

        final String soapAction = "executePayment";
        CreditResponse creditResponse;

        try {
            Response response = httpClient.sendHttpRequest(payLoad, CMS_WSDL_URL, soapAction);

            if (response == null) {
                throw new GravityException("No response returned");
            }
            logger.info("Response code: {} and Response message: {}", response.getCode(), response.getMessage());
            creditResponse = ResponseParser.parse(response.getMessage());
            logger.info("Credit response: {}", creditResponse);

        } catch (Exception e) {
            logger.error("Error sending credit request", e);
            throw new GravityException(e.getMessage(), e);
        }
        return creditResponse;

    }


    private String preparePayLoad(CreditRequest creditRequest) {

        Context context = new Context();
        context.setVariable("refNum", creditRequest.getRefNumber());
        context.setVariable("pan", creditRequest.getPan());
        context.setVariable("amount", creditRequest.getAmount());
        context.setVariable("regNum", regNum);
        context.setVariable("senderInfo", creditRequest.getSenderInfo());

        String payLoad = templateEngine.process("transfer", context);

        payLoad = payLoad.replace("<arg_0_0>", "");
        payLoad = payLoad.replace("</arg_0_0>", "");
        payLoad = payLoad.replace("&lt;", "<");
        payLoad = payLoad.replace("&gt;", ">");
        return payLoad;

    }



}

