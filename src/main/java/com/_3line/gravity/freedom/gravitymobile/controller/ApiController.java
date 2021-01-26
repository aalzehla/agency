///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com._3line.gravity.freedom.gravitymobile.controller;
//
//import com._3line.gravity.api.auth.dto.LoginDto;
//import com._3line.gravity.api.users.agents.dto.AgentSetupDto;
//import com._3line.gravity.core.cryptography.SecretKeyCrypt;
//import com._3line.gravity.core.exceptions.GravityException;
//import com._3line.gravity.core.models.Response;
//import com._3line.gravity.core.security.jwt.AuthenticationRequest;
//import com._3line.gravity.core.security.jwt.JwtTokenUtil;
//import com._3line.gravity.freedom.agents.dtos.AgentDto;
//import com._3line.gravity.freedom.agents.models.Agents;
//import com._3line.gravity.freedom.agents.repository.AgentsRepository;
//import com._3line.gravity.freedom.gravitymobile.oauth.model.AgentsDetails;
//import com._3line.gravity.freedom.gravitymobile.oauth.model.OAuth2Response;
//import com._3line.gravity.freedom.gravitymobile.service.MobileService;
//import com._3line.gravity.freedom.utility.ApiCallLogs;
//import com._3line.gravity.freedom.utility.PropertyResource;
//import com._3line.gravity.freedom.utility.Utility;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.csrf.CsrfToken;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Map;
//
///**
// * @author OlalekanW
// */
//@Controller
//@RequestMapping(value = "/gravity/api")
//public class ApiController {
//
//    @Autowired
//    private MobileService service;
//    @Autowired
//    SecretKeyCrypt secretKeyCrypt;
//
//    @Autowired
//    AgentsRepository agentsRepository;
//
//    @Qualifier("jwtservice")
//    @Autowired
//    UserDetailsService userDetailsService;
//
//    @Autowired
//    JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    AuthenticationManager authenticationManager;
//
//
//    @Autowired
//    PropertyResource pr;
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private String returnEncryptedResponse(String securedResponse) {
////        System.out.println(String.format(" \r\n############\r\n preparing unpacked body to return to client: %s", securedResponse));
////        try {
////            securedResponse = secretKeyCrypt.encrypt(securedResponse);
////            System.out.println(String.format(" \r\n############\r\n returning packed response to client : %s", securedResponse));
//            return securedResponse;
////        } catch (Exception ex) {
////            ex.printStackTrace();
////        }
////        return securedResponse;
//    }
//
//    @RequestMapping(value = "/csrf-token")
//    @ResponseBody
//    String getCsrfToken(HttpServletRequest request) {
//        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//        return token.getToken();
//    }
//
//    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST, consumes = {"application/x-www-form-urlencoded", "application/json"}, produces = "application/json")
//    @ResponseBody
//    ResponseEntity<?> getBearerToken(HttpServletRequest request, @RequestBody AuthenticationRequest authenticationRequest) {
//
////        String securedResponse = service.getBearerToken(request);
////        return new ResponseEntity<>(returnEncryptedResponse(securedResponse), HttpStatus.OK);
//        OAuth2Response response = new OAuth2Response();
//        try {
//
//            System.out.println("checking here with :: "+authenticationRequest.getUsername());
//            Agents ag = agentsRepository.findByUsername(authenticationRequest.getUsername());
//            if(ag ==null){
//                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//            }else{
//                System.out.println("agent is :: "+ag);
//            }
//
//
//            final UserDetails userDetails = userDetailsService.loadUserByUsername(ag.getAgentId());
//            final String token = jwtTokenUtil.generateToken(userDetails, null);
//
//            response.setAccess_token(token);
//            response.setRespCode("00");
//            response.setRespDescription(pr.getV("00", "response.properties"));
//            logger.warn(response.getRespDescription());
//        } catch (GravityException e) {
//            response = (OAuth2Response) e.getObj();
//        }
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//
//
//    }
//
//    @RequestMapping(value = "/agentSetup", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<?> agentSetup(@RequestBody AgentSetupDto agentSetupDto) {
//
//        ResponseEntity<?> responseEntity;
//        String securedResponse = service.agentSetup(agentSetupDto);
//        responseEntity = new ResponseEntity<>(securedResponse,HttpStatus.OK);
//        return responseEntity;
//
//    }
//
//    @RequestMapping(value = "/changePassword", method = RequestMethod.POST, consumes = {"text/plain"}, produces = "text/plain")
//    @ResponseBody
//    String changePassword(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.changePassword(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//        return returnEncryptedResponse(securedResponse);
//
//    }
//
//    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST, consumes = {"text/plain"}, produces = "text/plain")
//    @ResponseBody
//    String forgotPassword(HttpServletRequest request) {
//
//        String securedResponse = service.forgotPassword(request);
//        return returnEncryptedResponse(securedResponse);
//
//    }
//
//    @RequestMapping(value = "/pinChange", method = RequestMethod.POST, consumes = {"text/plain"}, produces = "text/plain")
//    @ResponseBody
//    String pinChange(HttpServletRequest request) {
//
//        String securedResponse = service.pinChange(request);
//        return returnEncryptedResponse(securedResponse);
//
//    }
//
//    @RequestMapping(value = "/logon", method = RequestMethod.POST)
//    @ResponseBody
//    ResponseEntity<?> logon(@RequestBody LoginDto loginDto) {
//        ResponseEntity<?>  responseEntity;
//        logger.info("incoming request is :: {} ",loginDto);
//        Response response = service.logon(loginDto);
//        responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
//        return responseEntity;
//
//    }
//
//
//    @RequestMapping(value = "/savings", method = RequestMethod.POST)
//    @ResponseBody
//    String savings(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.savings(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//
//    }
//
//
//    @RequestMapping(value = "/cardwithdrawals", method = RequestMethod.POST)
//    @ResponseBody
//    String cardWithdrawals(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.cardWithdrawals(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//
//    }
//
//    @RequestMapping(value = "/cardwithdrawals/otp", method = RequestMethod.POST)
//    @ResponseBody
//    String cardWithdrawalsOtp(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.cardWithdrawalsOTP(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//
//    }
//
//
//    @RequestMapping(value = "/agentBalance", method = RequestMethod.POST)
//    @ResponseBody
//    String agentBalance(HttpServletRequest request) {
//
//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.agentBalance(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//
//    }
//
//    @RequestMapping(value = "/nameEnquiry", method = RequestMethod.POST)
//    @ResponseBody
//    String nameEnquiry(HttpServletRequest request) {
//
//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.nameEnquiry(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//
//    }
//
//
//    @RequestMapping(value = "/maketransfer", method = RequestMethod.POST)
//    @ResponseBody
//    String interBankTransfer(HttpServletRequest request) {
//
//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.interBankTransfer(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "/walletBalance", method = RequestMethod.POST)
//    @ResponseBody
//    String wallBalance(HttpServletRequest request) {
//
//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.walletBalance(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//
//    @RequestMapping(value = "/walletHistory/trading", method = RequestMethod.POST)
//    @ResponseBody
//    String walletHistory(HttpServletRequest request) {
//
//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.walletHistory(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "/walletHistory/income", method = RequestMethod.POST)
//    @ResponseBody
//    String incomeWalletHistory(HttpServletRequest request) {
//
//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.incomeWalletHistory(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//
//    @RequestMapping(value = "/disburseWallet/trading", method = RequestMethod.POST)
//    @ResponseBody
//    String disburseWallet(HttpServletRequest request) {
//
//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.disburseWallet(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//
//    @RequestMapping(value = "/disburseWallet/income", method = RequestMethod.POST)
//    @ResponseBody
//    String disburseIncomeWallet(HttpServletRequest request) {
//
//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.disburseIncomeWallet(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "/wallet/transfer", method = RequestMethod.POST)
//    @ResponseBody
//    String transferBetweenWallet(HttpServletRequest request) {
//
//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.transferBetweenWallet(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//
//    @RequestMapping(value = "/creditRequest", method = RequestMethod.POST)
//    @ResponseBody
//    String creditRequest(HttpServletRequest request) {
//
//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.creditRequest(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "/creditRequestHistory", method = RequestMethod.POST)
//    @ResponseBody
//    String creditRequestHistory(HttpServletRequest request) {
//
//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.creditRequestHistory(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "getAllBanks", method = RequestMethod.POST)
//    @ResponseBody
//    String getAllBanks(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.getAllBanks(requestBody, request, callLog);
//        logger.info("response will be sent ");
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "getBankDetails", method = RequestMethod.POST)
//    @ResponseBody
//    String getBankDetails(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.getBankDetails(requestBody, request, callLog);
//        logger.info("response will be sent ");
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "logIssues", method = RequestMethod.POST)
//    @ResponseBody
//    String logIssues(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.logIssues(requestBody, request, callLog);
//        logger.info("response will be sent ");
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "getCommissionFee", method = RequestMethod.POST)
//    @ResponseBody
//    String getCommissionFee(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.getCommissionFee(requestBody, request, callLog);
//        logger.info("response will be sent ");
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "registerForThrift", method = RequestMethod.POST)
//    @ResponseBody
//    String registerForThrift(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.registerForThrift(requestBody, request, callLog);
//        logger.info("response will be sent ");
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "contributeThrift", method = RequestMethod.POST)
//    @ResponseBody
//    String contributeThrift(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.contributeThrift(requestBody, request, callLog);
//        logger.info("response will be sent ");
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "thriftDetails", method = RequestMethod.POST)
//    @ResponseBody
//    String thriftDetails(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.thriftDetails(requestBody, request, callLog);
//        logger.info("response will be sent ");
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "prematureLiquidation", method = RequestMethod.POST)
//    @ResponseBody
//    String prematureLiquidation(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.prematureLiquidation(requestBody, request, callLog);
//        logger.info("response will be sent ");
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "openAccount", method = RequestMethod.POST)
//    @ResponseBody
//    String openAccount(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.openAccount(requestBody, request, callLog);
//        logger.info("response will be sent ");
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "bills/category", method = RequestMethod.POST)
//    @ResponseBody
//    String billerCategory(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.getBillerCategoryies(requestBody, request, callLog);
//        logger.info("response will be sent ");
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "bills/category/service", method = RequestMethod.POST)
//    @ResponseBody
//    String billerService(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.getBillerCategoryServices(requestBody, request, callLog);
//        logger.info("response will be sent ");
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "bills/category/service/options", method = RequestMethod.POST)
//    @ResponseBody
//    String billerOption(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.getBillerServiceOptions(requestBody, request, callLog);
//        logger.info("response will be sent ");
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "bills/pay", method = RequestMethod.POST)
//    @ResponseBody
//    String billerPayBills(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.paybills(requestBody, request, callLog);
//        logger.info("response will be sent ");
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "bills/validate", method = RequestMethod.POST)
//    @ResponseBody
//    String validateBillerCustomer(HttpServletRequest request) {
//
//        String headers2 = Utility.getHeaders2(request).get("HeadersAsString");
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.validateBillerCustomer(requestBody, request, callLog);
//        logger.info("response will be sent ");
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
//    @ResponseBody String transactionHistory(HttpServletRequest request) {
//
//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.transactionHistory(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//
//    @RequestMapping(value = "/transactions/mail", method = RequestMethod.POST)
//    @ResponseBody String transactionHistorySendMail(HttpServletRequest request) {
//
//        Map<String, String> headers2 = Utility.getHeaders2(request);
//        String requestBody = Utility.getRequestBody(request);
//
//        ApiCallLogs callLog = new ApiCallLogs(2, headers2 + requestBody);
//
//        String securedResponse = service.getTranHistorySendEmail(requestBody, request, callLog);
//
//        callLog.setResponse(securedResponse);
//
//        //Encrypting and returning response
//        return returnEncryptedResponse(securedResponse);
//    }
//}
