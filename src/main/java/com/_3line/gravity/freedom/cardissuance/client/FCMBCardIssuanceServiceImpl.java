package com._3line.gravity.freedom.cardissuance.client;

import com._3line.gravity.freedom.cardissuance.dto.CardIssuanceRequestDTO;
import com._3line.gravity.freedom.cardissuance.dto.CardIssuanceResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author FortunatusE
 * @date 12/11/2018
 */

@Service
public class FCMBCardIssuanceServiceImpl implements FCMBCardIssuanceService {

    private static final Logger logger = LoggerFactory.getLogger(FCMBCardIssuanceServiceImpl.class);

    private final TemplateEngine templateEngine;
    private FCMBCustomHttpClient httpClient;

    @Value("${fcmb.card.issuance.url}")
    private String url;

    @Value("${fcmb.card.issuance.soap.action}")
    private String soapAction;

    @Autowired
    public FCMBCardIssuanceServiceImpl(TemplateEngine templateEngine, FCMBCustomHttpClient httpClient) {
        this.templateEngine = templateEngine;
        this.httpClient = httpClient;
    }

    @Override
    public CardIssuanceResponseDTO sendCardIssuanceRequest(CardIssuanceRequestDTO cardIssuanceRequest) {

        logger.debug("Sending card issuance request: {}", cardIssuanceRequest);

        String payLoad = preparePayLoad(cardIssuanceRequest);

        CardIssuanceResponseDTO cardIssuanceResponse;

        try {
            Response response = httpClient.sendHttpRequest(payLoad, url, soapAction);

            if (response == null) {
                throw new CardIssuanceException("No response returned");
            }
            logger.info("Response code: {} and Response message: {}", response.getCode(), response.getMessage());
            cardIssuanceResponse = ResponseParser.parse(response.getMessage());
            logger.info("Card issuance response: {}", cardIssuanceResponse);

        } catch (Exception e) {
            logger.error("Error sending card issuance request", e);
            throw new CardIssuanceException(e.getMessage(), e);
        }
        return cardIssuanceResponse;

    }



    private String preparePayLoad(CardIssuanceRequestDTO cardIssuanceRequest) {

        Context context = new Context();
        context.setVariable("cardSerial", cardIssuanceRequest.getCardSerial());
        context.setVariable("accountNumber", cardIssuanceRequest.getAccountNumber());
        context.setVariable("cardType", cardIssuanceRequest.getCardType());
        context.setVariable("createdBy", cardIssuanceRequest.getCreatedBy());
        context.setVariable("header", cardIssuanceRequest.getHeader());

        String payLoad = templateEngine.process("cardissuance/soap.xml", context);

        payLoad = payLoad.replace("<arg_0_0>", "");
        payLoad = payLoad.replace("</arg_0_0>", "");
        payLoad = payLoad.replace("&lt;", "<");
        payLoad = payLoad.replace("&gt;", ">");
        return payLoad;

    }


}
