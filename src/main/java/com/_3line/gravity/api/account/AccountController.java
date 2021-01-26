package com._3line.gravity.api.account;

import com._3line.gravity.api.account.dto.*;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.agents.dtos.AgentDto;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.financialInstitutions.dtos.AccOpeningGeneral;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountOpeningResponse;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountRequest;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAgentCreation;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAgentCreationResponse;
import com._3line.gravity.freedom.financialInstitutions.sanef.service.SanefService;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.dto.StanbicOTPDto;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.request.StanbicAccountOpeningRequest;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response.StanbicAccountOpeningResponse;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.services.StanbicIBTCServices;
import com._3line.gravity.freedom.gravitymobile.service.AccountOpeningService;
import com._3line.gravity.freedom.utility.ApiCallLogs;
import com._3line.gravity.freedom.utility.Utility;
import com._3line.gravity.freedom.wallet.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/gravity/api/account")
public class AccountController {



    @Autowired
    private AccountOpeningService accountOpeningService ;

    @Autowired
    private StanbicIBTCServices stanbicIBTCServices;

    @Autowired
    SanefService sanefService;

    @Autowired
    ModelMapper modelMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/gtb/open", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> gtbOpenAccount(@RequestBody GTBAccountOpeningDTO request) {
        Response resp = null;
        try {
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            AccOpeningGeneral accOpeningGeneral = modelMapper.map(request,AccOpeningGeneral.class);
            accOpeningGeneral.setBankCode("058");
            resp =  accountOpeningService.openAccount(accOpeningGeneral);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @RequestMapping(value = "/wema/open", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> wemaOpenAccount(@RequestBody WemaAccountOpeningDTO request) {
        Response resp = null;
        try {
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            AccOpeningGeneral accOpeningGeneral = modelMapper.map(request,AccOpeningGeneral.class);
            accOpeningGeneral.setBankCode("035");
            resp =  accountOpeningService.openAccount(accOpeningGeneral);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @RequestMapping(value = "/sanef/open", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity sanefOpenAccount(@RequestBody SanefAccountRequest request) {
        SanefAccountOpeningResponse resp = new SanefAccountOpeningResponse();
        try {
            resp =accountOpeningService.openSanefAccount(request);
        }catch (GravityException e){
           resp.setResponseCode("99");
           resp.setResponseDescription(e.getMessage());
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @RequestMapping(value = "/sanef/agent/creation", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity sanefAgentCreation(@RequestBody SanefAgentCreation request) {
        SanefAgentCreationResponse resp = new SanefAgentCreationResponse();
        try {
            resp =sanefService.createSanefAgent(request);
        }catch (GravityException e){
            logger.debug("could not open account: {}",e.getMessage());
            resp.setResponseCode("99");
            resp.setResponseDescription(e.getMessage());

        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @RequestMapping(value = "/sanef/generate/keys", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity sanefGenerateKeys() {
        Response resp=new Response();
        try {
            resp.setRespBody(sanefService.generateKeys());
            resp.setRespDescription("success");
            resp.setRespCode("00");

        }catch (GravityException e){
            logger.debug("could not open account: {}",e.getMessage());
            resp.setRespCode("99");
            resp.setRespDescription(e.getMessage());
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }



    @RequestMapping(value = "/fidelity/open", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> fidelityOpenAccount(@RequestBody FidelityAccountOpeningDTO request) {
        Response resp = null;
        try {
            AccOpeningGeneral accOpeningGeneral = modelMapper.map(request,AccOpeningGeneral.class);
            accOpeningGeneral.setBankCode("070");
            accOpeningGeneral.setBvn(request.getBvn());
            accOpeningGeneral.setEmail(request.getEmail());
            accOpeningGeneral.setSignature(request.getCustomerMandate());
            System.out.println("here :: "+accOpeningGeneral);

            resp =  accountOpeningService.openAccount(accOpeningGeneral);

        } catch (Exception e) {
            e.printStackTrace();
            resp = new Response();
            resp.setRespCode("99");
            resp.setRespBody(e.getMessage());
        }

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


    @RequestMapping(value = "/bvn/validate", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> validatebvnRequest(@RequestBody BVNRequest bvnRequest) {
        Response resp = new Response();
        if(bvnRequest.equals("") || bvnRequest==null || !bvnRequest.getBvn().equals("22294626861")){
            resp.setRespCode("96");
            resp.setRespDescription("failed");
        }else if(bvnRequest.getBvn().equals("22294626861")){
            resp.setRespCode("00");
            resp.setRespDescription("success");
            BVNResponse bvnResponse = new BVNResponse();
            bvnResponse.setBvn(bvnRequest.getBvn());
            bvnResponse.setFirstName("Labi");
            bvnResponse.setLastName("Timothy");
            bvnResponse.setEmail("owolabi.sunday08@gmail.com");
            bvnResponse.setDateOfBirth("23-08-1990");
            bvnResponse.setGender("Male");
            bvnResponse.setPhoneNumber("08169141091");
            bvnResponse.setStateOfOrigin("Lagos");
            bvnResponse.setResidentialAddress("Male");
            resp.setRespBody(bvnResponse);
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


    @RequestMapping(value = "/stanbic/otp", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> stanbicGetOTP(@RequestBody StanbicOTPDto request) {
        Response resp = null;
        try {

            resp =  stanbicIBTCServices.getOTP(request);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @RequestMapping(value = "/stanbic/open", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> stanbicOpenAccount(@RequestBody StanbicAccountOpeningRequest request) {
        logger.info("Stanbic Request Body {}", request);
        Response resp = null;
        try {
            Gson gson = new Gson();
            AccOpeningGeneral accOpeningGeneral = objectMapper.readValue(gson.toJson(request),AccOpeningGeneral.class);
            logger.info("Account Opening General Request Body {}", accOpeningGeneral);
            accOpeningGeneral.setBankCode("221");
            resp =  accountOpeningService.openAccount(accOpeningGeneral);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }


    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
