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

@Service("newCardIssuanceService")
public class FCMBNewCardIssuanceServiceImpl implements FCMBCardIssuanceService {

    private static final Logger logger = LoggerFactory.getLogger(FCMBNewCardIssuanceServiceImpl.class);

    private final TemplateEngine templateEngine;
    private FCMBCustomHttpClient httpClient;

    @Value("${fcmb.card.issuance.url2}")
    private String url;

    @Value("${fcmb.card.issuance.soap.action2}")
    private String soapAction;

    @Value("${fcmb.card.issuance.channelid}")
    private String channelId;

    @Value("${fcmb.card.issuance.channelpass}")
    private String channelPass;

    @Value("${fcmb.card.issuance.cardFeeType}")
    private String cardFeeType;

    @Autowired
    public FCMBNewCardIssuanceServiceImpl(TemplateEngine templateEngine, FCMBCustomHttpClient httpClient) {
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
            cardIssuanceResponse = ResponseParser.process(response.getMessage());
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
        context.setVariable("cardFeeType", cardFeeType);
        context.setVariable("channelId", channelId);
        context.setVariable("channelPass", channelPass);

        String payLoad = templateEngine.process("cardissuance/payload.xml", context);

        payLoad = payLoad.replace("<arg_0_0>", "");
        payLoad = payLoad.replace("</arg_0_0>", "");
        payLoad = payLoad.replace("&lt;", "<");
        payLoad = payLoad.replace("&gt;", ">");
        return payLoad;

    }
}
