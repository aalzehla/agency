package com._3line.gravity.api.fcmbcardissuance;

import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.utils.ResponseUtils;
import com._3line.gravity.freedom.cardissuance.client.CardIssuanceRequest;
import com._3line.gravity.freedom.cardissuance.dto.CardIssuanceRequestDTO;
import com._3line.gravity.freedom.cardissuance.service.CardIssuanceService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author FortunatusE
 * @date 12/11/2018
 */

@RequestMapping("/api/v1/card")
@RestController
public class FcmbCardIssuanceController {

    private static final Logger logger = LoggerFactory.getLogger(FcmbCardIssuanceController.class);

    @Autowired
    CardIssuanceService cardIssuanceService;

    private Gson json = new Gson() ;



    @PostMapping("/issuance")
    public Response issueCard(@RequestBody CardIssuanceRequestDTO cardIssuanceRequest, HttpServletRequest request){

        try{

            Response response = cardIssuanceService.processCardIssuance(cardIssuanceRequest);
            logger.debug("Response to client: {}", response);
            return response;
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            Response response = ResponseUtils.createDefaultFailureResponse();
            logger.debug("Response to client: {}", response);
            return response;
        }


    }

    @PostMapping("/mobile/issuance")
    public ResponseEntity mobileIssueCard(@RequestBody CardIssuanceRequestDTO cardIssuanceRequest){

        try{
            Response response = cardIssuanceService.processCardIssuance(cardIssuanceRequest);
            logger.debug("Response to client: {}", response);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            Response response = ResponseUtils.createDefaultFailureResponse();
            logger.debug("Response to client: {}", response);
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    @PostMapping("/types")
    public Response getCardTypes(HttpServletRequest request){

        try{
            Response response = cardIssuanceService.getCardTypes();
            logger.debug("Response to client: {}", response);
            return response ;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            Response response = ResponseUtils.createDefaultFailureResponse();
            logger.debug("Response to client: {}", response);
            return response ;
        }
    }


    @GetMapping("/mobile/types")
    public ResponseEntity mobileGetCardTypes(HttpServletRequest request){

        try{
            Response response = cardIssuanceService.getCardTypes();
            logger.debug("Response to client: {}", response);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            Response response = ResponseUtils.createDefaultFailureResponse();
            logger.debug("Response to client: {}", response);
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }
}
