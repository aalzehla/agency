package com._3line.gravity.api.transaction.controller;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.financialInstitutions._9PSB.dto.*;
import com._3line.gravity.freedom.financialInstitutions._9PSB.service._9psbService;
import com._3line.gravity.freedom.gravitymobile.service.*;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.utility.PropertyResource;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/gravity/api/transactions/_9psb")
public class _9PsbTransactionAPI {

    @Autowired
    TransactionService transactionService;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    _9psbService _9psbService;



    private Logger logger = LoggerFactory.getLogger(_9PsbTransactionAPI.class);


    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> validateCode(@RequestBody ValidateCodeRequestDTO requestDTO) {

        Response response = new Response();

        ValidateCodeResponseDTO codeResponseDTO = _9psbService.validateCode(requestDTO);

        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(codeResponseDTO);


        return new ResponseEntity<>(response,HttpStatus.OK);

    }


    @RequestMapping(value = "/inquiry", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> acctInquiry(@RequestBody AcctInquiryRequestDTO requestDTO) {

        Response response = new Response();
        AcctInquiryResponseDTO inquiryResponseDTO = new AcctInquiryResponseDTO();
        inquiryResponseDTO.setFirstName("Timothy");
        inquiryResponseDTO.setLastName("Owolabi");
        inquiryResponseDTO.setBankCode("9PSB");
        inquiryResponseDTO.setAccountNumber("909909988");
        inquiryResponseDTO.setMobileNumber("08169141091");
        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(inquiryResponseDTO);


        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "/code/deposit", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> doDepositWithCode(@RequestBody DepositRequestDTO requestDTO) {

        Response response = new Response();
        DepositResponseDTO depositResponseDTO = _9psbService.depositWithCode(requestDTO);

        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(depositResponseDTO);


        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> doDepositWithoutCode(@RequestBody DepositWithoutCodeReqDTO requestDTO) {

        Response response = new Response();
        DepositWithoutCodeRespDTO depositResponseDTO = _9psbService.depositWithoutCode(requestDTO);

        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(depositResponseDTO);


        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "/initiate/withdrawal", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> initiateWithdrawal(@RequestBody InitiateWithdrawalReqDTO requestDTO) {

        Response response = new Response();
        InitiateWithdrawalRespDTO initiateWithdrawalResp = null;//modelMapper.map(requestDTO,InitiateWithdrawalRespDTO.class);
        initiateWithdrawalResp.setWithdrawalTrackingId("1ac9b389-8ea9-4e34-8177-2d7c9ed90852");
        initiateWithdrawalResp.setOtpTrackingId("fa2f3c62-727c-4a86-809d-4950392c4756");
        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(initiateWithdrawalResp);


        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @RequestMapping(value = "/withdrawal", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> withdraw(@RequestBody WithdrawalRequestDTO requestDTO) {

        Response response = new Response();
        WithdrawalResponseDTO withdrawalResp = null;//modelMapper.map(requestDTO,WithdrawalResponseDTO.class);
        withdrawalResp.setPin(null);
        withdrawalResp.setTransactionRef("1ac9b389-8ea9-4e34-8177-2d7c9ed90852");
        withdrawalResp.setTranDate(new Date());
        withdrawalResp.setAmount(5000.00);
        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(withdrawalResp);


        return new ResponseEntity<>(response,HttpStatus.OK);

    }




}
