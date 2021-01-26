package com._3line.gravity.api.wallet.controller;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.api.wallet.dto.CardDTO;
import com._3line.gravity.api.wallet.dto.CustomWalletDTO;
import com._3line.gravity.api.wallet.dto.FundWalletDTO;
import com._3line.gravity.core.code.dto.CodeDTO;
import com._3line.gravity.core.code.service.CodeService;
import com._3line.gravity.core.cryptography.SecretKeyCrypt;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.dto.DebitCardDTO;
import com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.service.DebitCardService;
import com._3line.gravity.freedom.dispute.dtos.DisputeDto;
import com._3line.gravity.freedom.financialInstitutions.magtipon.service.MagtiponClientService;
import com._3line.gravity.freedom.itexintegration.model.PtspModel;
import com._3line.gravity.freedom.itexintegration.repository.PtspRepository;
import com._3line.gravity.freedom.itexintegration.service.PtspService;
import com._3line.gravity.freedom.wallet.dto.MagtiponCreditWalletRequest;
import com._3line.gravity.freedom.gravitymobile.service.MobileService;
import com._3line.gravity.freedom.gravitymobile.service.MobileWalletService;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.utility.DateUtil;
import com._3line.gravity.freedom.utility.PropertyResource;
import com._3line.gravity.freedom.utility.Utility;
import com._3line.gravity.freedom.wallet.dto.*;
import com._3line.gravity.freedom.wallet.models.FreedomWalletTransaction;
import com._3line.gravity.freedom.wallet.models.WalletCreditReversal;
import com._3line.gravity.freedom.wallet.models.WalletEnquiry;
import com._3line.gravity.freedom.wallet.repository.WalletCreditReversalRepository;
import com._3line.gravity.freedom.wallet.repository.WalletEnquiryRepository;
import com._3line.gravity.freedom.wallet.service.WalletService;
import com.google.gson.Gson;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/gravity/api/wallet")
public class WalletAPI {

    @Autowired
    WalletService walletService;

    @Autowired
    AgentService agentService;

    @Autowired
    MobileService service;

    @Autowired
    MobileWalletService mobileWalletService;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PropertyResource pr ;

    @Autowired
    DebitCardService  debitCardService;


    @Autowired
    WalletCreditReversalRepository creditReversalRepository;

    @Autowired
    SecretKeyCrypt secretKeyCrypt;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    WalletEnquiryRepository walletEnquiryRepository;

    @Autowired
    MagtiponClientService magtiponClientService;

    @Autowired
    CodeService codeService;

    @Autowired
    PtspService ptspService;

    @Autowired
    PtspRepository ptspRepository;

    @Autowired
    AgentsRepository agentsRepository;




    private Gson json = new Gson();

    private Logger logger = LoggerFactory.getLogger(WalletAPI.class);

    @Autowired
    WalletCreditReversalRepository walletCreditReversalRepository;




    @RequestMapping(value = "/reversewallettrann", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> reverseWalletTransaction(HttpServletRequest request) {

        Response response = new Response();
        List<WalletCreditReversal> walletCreditReversals = walletCreditReversalRepository.findAll();
        for(int a=0;a<walletCreditReversals.size();a++){
            WalletCreditReversal creditReversal = walletCreditReversals.get(a);
            DisputeDto disputeDto = new DisputeDto();
            disputeDto.setComment(creditReversal.getRemark());
            disputeDto.setTranId(Long.valueOf(creditReversal.getPtspId()));
            disputeDto.setAction("REVERSAL");
            disputeDto.setWalletNumber(creditReversal.getWalletNumber());
            try{
                String resp = walletService.manualRaiseDispute(disputeDto);
                System.out.println("Response here is :: "+resp);
                creditReversal.setStatus(resp);
                creditReversal.setDelFlag("Y");
                walletCreditReversalRepository.save(creditReversal);
            }catch(Exception e){
                e.printStackTrace();
                creditReversal.setStatus("failed: "+e.getMessage());
                walletCreditReversalRepository.save(creditReversal);
            }
        }

        response.setRespCode("00");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/reversewithdrawal", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> reversewithdrawal (HttpServletRequest request) {

        Response response = new Response();
        List<WalletCreditReversal> walletCreditReversals = walletCreditReversalRepository.findAll();
        for(int a=0;a<walletCreditReversals.size();a++){
            WalletCreditReversal creditReversal = walletCreditReversals.get(a);

            try{
                PtspModel ptspModel = ptspRepository.getOne(Long.valueOf(creditReversal.getChannel()));

                Agents agent = agentsRepository.findByWalletNumber(creditReversal.getWalletNumber());

                Double realTranAmount = creditReversal.getAmount();

                Double tranFee = ptspService.getTranFee(ptspModel, agent, realTranAmount);

                ptspService.doCommission(agent,ptspModel,realTranAmount,tranFee, Long.valueOf(creditReversal.getPtspId()),true);

                System.out.println("Response here is :: "+"SUCCESS");
                creditReversal.setStatus("SUCCESS");
                creditReversal.setDelFlag("Y");
                walletCreditReversalRepository.save(creditReversal);
            }catch(Exception e){
                e.printStackTrace();
                creditReversal.setStatus("failed: "+e.getMessage());
                walletCreditReversalRepository.save(creditReversal);
            }
        }

        response.setRespCode("00");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @RequestMapping(value = "/walletBalance", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> wallBalance(HttpServletRequest request) {

        Response response = new Response();
        Agents agent = jwtUtility.getCurrentAgent();
        if(Objects.isNull(agent)){
            response.setRespCode("999");
            response.setRespDescription("Agent with user name not found");
        }
        CustomWalletDTO trading = convertwalletdtotocustom(walletService.getWalletByNumber(agent.getWalletNumber()));

        CustomWalletDTO income = convertwalletdtotocustom(walletService.getWalletByNumber(agent.getIncomeWalletNumber()));

        Map<String ,Object> res = new HashMap<>();

        res.put("trading" , trading) ;
        res.put("income" , income) ;

        response.setRespCode("00");
        response.setRespBody(res);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @RequestMapping(value = "/walletHistory/trading", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> getWalletradingHist(@RequestBody WalletHistoryDto request) {
        Agents agents = jwtUtility.getCurrentAgent();
        Response response = processTradingHistory(request,agents);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @RequestMapping(value = "/walletHistory/trading/{agentid}", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> getWalletTradingHistByAgentId(@RequestBody WalletHistoryDto request, @PathVariable String agentid) {

        Agents agent = agentService.getAgentByAgentId(agentid);
        if(agent==null || !agent.getParentAgentId().equals(jwtUtility.getCurrentAgent().getId())){
            throw new GravityException("Agent not found for Aggregator");
        }
        Response response = processTradingHistory(request,agent);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/walletHistory/income", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> getWalletIncomeHist(@RequestBody  WalletHistoryDto request) {

        Agents agent = jwtUtility.getCurrentAgent();
        Response response = processIncomeWalletHistory(request,agent);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/walletHistory/income/{agentid}", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> getWalletIncomeHistByAgentId(@RequestBody  WalletHistoryDto request, @PathVariable String agentid) {

        Agents agent = agentService.fetchAgentById(agentid);
        if(agent==null || !agent.getParentAgentId().equals(jwtUtility.getCurrentAgent().getId())){
            throw new GravityException("Agent not found for Aggregator");
        }
        Response response = processIncomeWalletHistory(request,agent);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }







    @RequestMapping(value = "/disburseWallet/trading", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> disburseWallet(@RequestBody WalletTransferDTO request) {


        Response response = new Response();

        Agents agent = jwtUtility.getCurrentAgent();//applicationUsersRepository.findByUsername((String)data.get("agentName"));




        List<CodeDTO> codeDTOS = codeService.getCodesByType("EXEMPTED_USERS");

        for (CodeDTO s : codeDTOS) {
            if(s.getCode().equalsIgnoreCase(agent.getUsername()) ){
                throw new GravityException("Error occurred Processing Transaction");
            }
        }

        try {
            if (!passwordEncoder.matches(request.getPin(), agent.getUserPin())) {
                throw new GravityException("Invalid Agent Pin");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespDescription(pr.getV("154", "response.properties"));
//            logger.warn(response.getRespDescription());
            response.setRespCode("154");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }

        try {
            response =  mobileWalletService.disburseFromWallet(agent.getUsername(), String.valueOf(request.getAmount()));
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }


        return new ResponseEntity<>(response,HttpStatus.OK);
    }

@RequestMapping(value = "/fund", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> fundWalletViaCard(HttpServletRequest request ) {

        String requestbody = Utility.getRequestBody(request);

        logger.info("Card funding Incoming Request is :: "+requestbody);

        try {
            requestbody = secretKeyCrypt.decrypt(requestbody);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GravityException("Invalid  or Empty encrypted string sent");
        }

        FundWalletDTO fundWalletDTO = json.fromJson(requestbody, FundWalletDTO.class);

        logger.info("Card encrypted funding Incoming Request is :: "+fundWalletDTO);


        Response response = new Response();

        Agents agent = jwtUtility.getCurrentAgent();

        try{
            String tranId = debitCardService.debitCard(fundWalletDTO );
            response.setRespBody(tranId);
            response.setRespCode("00");
            response.setRespDescription("success");
        }catch(Exception e){
            e.printStackTrace();
            response.setRespCode("96");
            response.setRespDescription(e.getMessage());
        }





        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/fund/direct", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> fundWallet(HttpServletRequest request, @RequestBody MagtiponCreditWalletRequest magtiponCreditWalletRequest) {


        Response response = new Response();

        System.out.println("I have :: "+magtiponCreditWalletRequest);

        WalletEnquiry walletEnquiry = walletEnquiryRepository.findByEnquiryRequestIdAndHasTransactionIsNot(magtiponCreditWalletRequest.getRequestId(),true);

        if(walletEnquiry == null ){
            throw new GravityException("Invalid Request ID");
        }else{
            walletEnquiry.setHasTransaction(true);
            walletEnquiryRepository.save(walletEnquiry);
        }

        Agents agent = agentService.getAgentByWalletNumber(walletEnquiry.getWalletNumber());



        String tranId = magtiponClientService.creditWallet(magtiponCreditWalletRequest,agent);

        walletEnquiry.setTransactionId(tranId);
        walletEnquiry.setStatus("SUCCESSFUL");
        walletEnquiryRepository.save(walletEnquiry);


        Map<String,String> respObj = new HashMap<>();
        respObj.put("tranId",tranId);
        respObj.put("tranDate","23/01/2020");
        respObj.put("requestId",magtiponCreditWalletRequest.getRequestId());

        response.setRespBody(respObj);
        response.setRespCode("00");
        response.setRespDescription("success");


        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/transaction/query{requestId}", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> queryTransaction(HttpServletRequest request, @PathVariable String requestId) {


        Response response = new Response();


        WalletEnquiry walletEnquiry = walletEnquiryRepository.findByEnquiryRequestId(requestId);

        if(walletEnquiry == null ){
            response.setRespBody(null);
            response.setRespCode("96");
            response.setRespDescription("No Record Found");
        }else{
            Map<String,String> respObj = new HashMap<>();
            respObj.put("tranId",walletEnquiry.getTransactionId());
            respObj.put("tranDate","23/01/2020");
            respObj.put("requestId",walletEnquiry.getEnquiryRequestId());
            response.setRespBody(respObj);
            if(!walletEnquiry.getStatus().equals("SUCCESSFUL")){
                response.setRespCode("96");
                response.setRespDescription("failed");
            }else{
                response.setRespCode("00");
                response.setRespDescription("success");
            }
        }

        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @RequestMapping(value = "/cards/add", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> addWalletCard(HttpServletRequest  request) {



        String requestbody = Utility.getRequestBody(request);

        logger.info("Card Incoming Request is :: "+requestbody);


        try {
            requestbody = secretKeyCrypt.decrypt(requestbody);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GravityException("Invalid or Empty encrypted string sent");
        }

        CardDTO cardDTO = json.fromJson(requestbody, CardDTO.class);

        logger.info("Card encrypted Incoming Request is :: "+cardDTO);


        Response response = new Response();

        if(cardDTO.getBank()==null || cardDTO.getBank().trim().equals("")){
            response.setRespCode("96");
            response.setRespDescription("Invalid bank specified");
        }


        DebitCardDTO debitCardDTO = new DebitCardDTO(cardDTO);
        debitCardDTO = debitCardService.addCard(debitCardDTO);

        if(debitCardDTO!=null){
            response.setRespCode("00");
            response.setRespDescription("success");
            response.setRespBody(debitCardDTO);
        }else{
            response.setRespCode("96");
            response.setRespDescription("failed");
        }

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/cards/remove", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> removeWalletCard(HttpServletRequest request) {


        String token = Utility.getRequestBody(request);


        try {
            token = secretKeyCrypt.decrypt(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GravityException("Invalid or Empty encrypted string sent");
        }


        Response response = new Response();

        debitCardService.deleteCard(token);
        response.setRespCode("00");
        response.setRespDescription("success");

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/cards", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> fetchAgentsCard() {


        Response response = new Response();
        List<DebitCardDTO>  debitCardDTOS = debitCardService.fetchCards();
        response.setRespCode("00");
        response.setRespDescription("success");
        response.setRespBody(debitCardDTOS);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }




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

    @RequestMapping(value = "/disburseWallet/income", method = RequestMethod.POST)
    @ResponseBody
    String normalizeBalances(HttpServletRequest request) {

        String fromDate = request.getHeader("fromDate");//01-01-2019
        String toDate = request.getHeader("toDate");//05-03-2020
        walletService.generateBalances("",fromDate,toDate);
        return "OKAY";

    }


//

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> transferBetweenWallet(@RequestBody WalletTransferDTO request) {

        Response response = new Response();

        Agents agent = jwtUtility.getCurrentAgent();//applicationUsersRepository.findByUsername((String)data.get("agentName"));

        if (!agent.getWalletNumber().equals(request.getFromWallet())
                && !agent.getIncomeWalletNumber().equals(request.getFromWallet())) {
            throw new GravityException("Invalid source wallet number");
        }

        if (request.getFromWallet().trim().equals(request.getToWallet().trim())) {
            throw new GravityException("Source and Destination wallet cannot be the same");
        }

        if (request.getFromWallet()==null || request.getToWallet()==null
        ||request.getFromWallet().equals("") || request.getToWallet().equals("")) {
            throw new GravityException("Invalid Source or Destination wallet");
        }

        if (!passwordEncoder.matches(request.getPin(), agent.getUserPin())) {
            throw new GravityException("Invalid Agent Pin");
        }



        try {
            response =  mobileWalletService.transferBetweenWallets(agent.getUsername(),String.valueOf(request.getAmount()),request.getFromWallet(),
                    request.getToWallet(),request.getRemark(),request.getRequestId());

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("96");
            response.setRespDescription(e.getMessage());
            response.setRespBody(null);
        }


        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @RequestMapping(value = "/creditRequest", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> creditRequest(@RequestBody CreditRequestDTO creditRequestDTO) {
        Response response = new Response();
        try{
            Agents agent  = jwtUtility.getCurrentAgent();
            creditRequestDTO.setWalletNumber(agent.getWalletNumber());
            creditRequestDTO.setAgentEmail(agent.getEmail());
            creditRequestDTO.setAgentName(agent.getUsername());
            walletService.createCreditRequest(creditRequestDTO);
            response.setRespCode("00");
        }catch (Exception e){
            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription(e.getMessage());
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/creditRequestHistory/{agentName}", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> creditRequestHistory(HttpServletRequest request,@PathVariable String agentName) {

        Response response;
        response = mobileWalletService.getCreditRequesthistory(agentName);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @RequestMapping(value = "/enquiry/{walletNumber}", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> getWalletInfoViaWalletNumber(@PathVariable String walletNumber) {
        

        
        
        Agents agent = agentService.getAgentByWalletNumber(walletNumber);

        WalletDTO walletDTO = walletService.getWalletByNumber(walletNumber);

        if(agent==null || walletDTO == null){
            throw new GravityException("No Record Found for wallet: "+walletNumber);
        }



        Response response = new Response();
        Map<String,String> respObj = new HashMap<>();
        respObj.put("name",agent.getFirstName()+" "+agent.getLastName());
        respObj.put("bvn",agent.getBvn());
        respObj.put("phoneNumber",agent.getPhoneNumber());
        respObj.put("walletNumber",agent.getWalletNumber());

        respObj.put("balance",String.format ("%.2f", walletDTO.getAvailableBalance()));

        WalletEnquiry walletEnquiry = new WalletEnquiry();
        walletEnquiry.setAccountNumber(agent.getAccountNo());
        walletEnquiry.setAgentName( agent.getFirstName()+" "+agent.getLastName() );
        walletEnquiry.setWalletNumber(agent.getWalletNumber());
        walletEnquiry.setEnquiryRequestId(agent.getWalletNumber().substring(0,3)+String.valueOf(new Date().getTime()));

        walletEnquiryRepository.save(walletEnquiry);


        respObj.put("enquiryRequestId", walletEnquiry.getEnquiryRequestId());


        response.setRespBody(respObj);
        response.setRespCode("00");
        response.setRespDescription("success");
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    private Response processIncomeWalletHistory(WalletHistoryDto request,Agents agents){

        Pageable pageable;
        Page<FreedomWalletTransaction> transactionList;
        if(request.getSize() > 0 && request.getPage() >= 0){
            pageable = PageRequest.of(request.getPage(), request.getSize());
        }else{
            pageable = PageRequest.of(0, 1000000);
        }

        if(request.getFrom()!=null && !request.getFrom().equals("") && request.getTo()!=null && !request.getTo().equals("")){
            Date fromDate = DateUtil.dateFullFormat(request.getFrom());
            Date toDate = DateUtil.dateFullFormat(request.getTo());
            toDate = DateUtils.addDays(toDate,1);

            transactionList = walletService.getWalletTransactionsWithDatePagination(agents.getIncomeWalletNumber(),fromDate,toDate,pageable);
        }else{

            transactionList = walletService.getWalletTransactionsWithPagination(agents.getIncomeWalletNumber(),pageable);
        }
        Response response = new Response();
        response.setRespBody(transactionList.getContent());
        response.setRespCode("00");

        return response;
    }

    private Response processTradingHistory(WalletHistoryDto request,Agents agents){

        Page<FreedomWalletTransaction> transactionList;
        Pageable firstPageWithFiveElements;
        if(request.getSize() > 0 && request.getPage() >= 0){
            firstPageWithFiveElements = PageRequest.of(request.getPage(), request.getSize());
        }else{
            firstPageWithFiveElements = PageRequest.of(0, 1000000);
        }

        if(request.getFrom()!=null && !request.getFrom().equals("") && request.getTo()!=null && !request.getTo().equals("")){
            Date fromDate = DateUtil.dateFullFormat(request.getFrom());
            Date toDate = DateUtil.dateFullFormat(request.getTo());
            toDate = DateUtils.addDays(toDate,1);

            transactionList =  walletService.getWalletTransactionsWithDatePagination(agents.getWalletNumber(),fromDate,toDate,firstPageWithFiveElements);

        }else{
            transactionList = walletService.getWalletTransactionsWithPagination(agents.getWalletNumber(),firstPageWithFiveElements);


        }
        Response response = new Response();
        response.setRespCode("00");
        response.setRespBody(transactionList.getContent());
        response.setRespDescription("success");

        return response;
    }


    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }


    private CustomWalletDTO convertwalletdtotocustom(WalletDTO wallet){

        CustomWalletDTO customWalletDTO = new CustomWalletDTO();
        customWalletDTO.setAvailableBalance(String.format("%.2f",wallet.getAvailableBalance()));
        customWalletDTO.setLedgerBalance(String.format("%.2f",wallet.getLedgerBalance()));
        customWalletDTO.setOpeningDate(wallet.getOpeningDate());
        customWalletDTO.setPurpose(wallet.getPurpose());
        customWalletDTO.setWalletNumber(wallet.getWalletNumber());
        customWalletDTO.setId(String.valueOf(wallet.getId()));

        return customWalletDTO;
    }

}
