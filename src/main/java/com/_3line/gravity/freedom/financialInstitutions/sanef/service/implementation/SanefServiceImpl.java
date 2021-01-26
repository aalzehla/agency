package com._3line.gravity.freedom.financialInstitutions.sanef.service.implementation;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.*;
import com._3line.gravity.freedom.financialInstitutions.sanef.service.SanefService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


@Service
public class SanefServiceImpl implements SanefService {
    private static final String EXCEPTION_CAUGHT = "Exception caught: {}";
    private static final String REGEX = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$";
    private static Logger logger = LoggerFactory.getLogger(SanefServiceImpl.class);
    // The response from the remote device.
    private String response;

    @Value("${sanef.third_party.agent.creation}")
    private String agentCreationUrl;

    @Value("${sanef.third_party.account.opening}")
    private String accountOpeningUrl;

    @Value("${sanef.superagent.code}")
    private String superAgentCode;

    private JwtUtility jwtUtility;
    private BankDetailsService bankDetailsService;
    private AgentService  agentService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Autowired
    public SanefServiceImpl(JwtUtility jwtUtility, BankDetailsService bankDetailsService,AgentService  agentService) {
        this.jwtUtility = jwtUtility;
        this.bankDetailsService = bankDetailsService;
        this.agentService = agentService;
    }


    @Override
    public String generateKeys() {
        return response;
    }



    private boolean isRegex(String base64String) {
        return Pattern.compile(REGEX).matcher(base64String).matches();
    }





    @Override
    public SanefAccountOpeningResponse sanefAccountOpening(SanefAccountRequest payload) {
        SanefAccountRequest request = getRequestPayload(payload);
        SanefAccountOpeningResponse openingResponse;


        try {
            openingResponse = sendAccountOpeningHttpRequest(request, accountOpeningUrl);
            logger.info("accountNumber: {}", openingResponse.getAccountNumber());
            logger.info("responseCode: {}", openingResponse.getResponseCode());
            logger.info("responseDescription: {}", openingResponse.getResponseDescription());

        }
        catch (Exception ex) {
            throw new GravityException(ex.getMessage());
        }
        return openingResponse;
    }

    private SanefAccountRequest getRequestPayload(SanefAccountRequest payload) {
        ModelMapper modelMapper = new ModelMapper();
        SanefAccountRequest accountRequest = modelMapper.map(payload, SanefAccountRequest.class);
        //this should be declared in properties
        accountRequest.setSuperagentCode(superAgentCode);
        accountRequest.setHouseNumber("1");
        Agents agent = jwtUtility.getCurrentAgent();
        if(agent==null || agent.getSanefAgentCode()==null){
            throw new GravityException("User not authorized");
        }else{
            accountRequest.setSuperagentCode(agent.getSanefAgentCode());
        }

        BankDetails bankDetails = getBankDetails(payload.getBankCode());
        if (bankDetails != null) {
            accountRequest.setBankCode(bankDetails.getSanefBankCode());
        } else accountRequest.setBankCode("");
        logger.info("Sanef code: {}", accountRequest.getBankCode());

        if (!isRegex(accountRequest.getCustomerImage())) {
            throw new GravityException("Invalid image format");
        }
        if (!isRegex(accountRequest.getCustomerSignature())) {
            throw new GravityException("Invalid customer signature");
        }

        return accountRequest;
    }

    private BankDetails getBankDetails(String bankCode) {
        logger.info("BankCode: {}", bankCode);
        return bankCode != null ?
                bankDetailsService.findByCode(bankCode) : null;
    }

    private SanefAccountOpeningResponse sendAccountOpeningHttpRequest(SanefAccountRequest request, String uri) {
        SanefAccountOpeningResponse accountOpeningResponse = new SanefAccountOpeningResponse();
        try {

            Gson gson = new Gson();
            String requestPayload = gson.toJson(request);
            logger.info("Make API call to {}, With Payload {}", uri, requestPayload);

            String agentResp = sendRequest(uri, requestPayload);
            accountOpeningResponse = new ObjectMapper().readValue(agentResp, SanefAccountOpeningResponse.class);

        }
        catch (IOException e) {
            logger.info(EXCEPTION_CAUGHT, e.getMessage());
        }
        return accountOpeningResponse;
    }

    @Override
    public SanefAgentCreationResponse createSanefAgent(SanefAgentCreation agentCreation) {

        SanefAgentCreation sanefAgentCreation = setAgentDetails(agentCreation);

        SanefAgentCreationResponse openingResponse = new SanefAgentCreationResponse();
        try {
            openingResponse = sendAgentCreationHttpRequest(sanefAgentCreation, agentCreationUrl);
            logger.info("response: {}", openingResponse);
        }
        catch (Exception e) {
            logger.info(EXCEPTION_CAUGHT, e.getMessage());
        }
        return openingResponse;
    }
    @Scheduled(cron ="${sanef.agent_creation.scheduler}")
    private void syncAgentsToSanef(){
        int cnt = 0;
        PageRequest pageRequest = PageRequest.of(cnt,20);
        Page<Agents> pendingAgents = agentService.getUnSyncedSanefAgents(pageRequest);
        boolean hasRecord = pendingAgents.getSize()>0?true:false;
        while(hasRecord){
            ++cnt;
            List<Agents> ag = pendingAgents.getContent();
            //process
            try{
                for(int i=0;i<ag.size();i++){
                    Agents agent = ag.get(i);
                    SanefAgentCreation  sanefAgentCreation = new SanefAgentCreation();
                    sanefAgentCreation.setRequestId(""+new Date().getTime());
                    sanefAgentCreation.setFirstName(agent.getFirstName());
                    sanefAgentCreation.setLastName(agent.getLastName());
                    sanefAgentCreation.setMiddleName(agent.getMiddleName());
                    sanefAgentCreation.setGender(agent.getGender()==null
                            ||agent.getGender().equalsIgnoreCase("Male")?1:2);
                    try{
                        sanefAgentCreation.setDateOfBirth(dateFormat.format(agent.getDob())+"T18:14:28.5654073+01:00");
                    }catch(Exception e){
                        sanefAgentCreation.setDateOfBirth("2000-11-22T18:14:28.5654073+01:00");
                    }
                    sanefAgentCreation.setPhoneNumber1(agent.getPhoneNumber());
                    sanefAgentCreation.setPhoneNumber2(agent.getPhoneNumber());
                    sanefAgentCreation.setBusinessName(agent.getBusinessName()==null||agent.getBusinessName().equals("")?
                            "1":agent.getBusinessName());
                    sanefAgentCreation.setAgentBusiness(0);
                    sanefAgentCreation.setEmailAddress(agent.getEmail());
                    sanefAgentCreation.setAgentCode(agent.getAgentId());
                    sanefAgentCreation.setAgentType(1);
                    sanefAgentCreation.setBankVerificationNumber(agent.getBvn());
                    sanefAgentCreation.setLatitude(0.00);
                    sanefAgentCreation.setLongitude(0.00);
                    sanefAgentCreation.setLocalGovernmentCode("15");
                    sanefAgentCreation.setClosestLandmark(agent.getLga());
                    sanefAgentCreation.setTaxIdentificationNumber(agent.getIdNumber());
                    sanefAgentCreation.setAgentAddress(agent.getAddress());
                    sanefAgentCreation.setUserName(agent.getUsername());
                    sanefAgentCreation.setPassword("password");
                    sanefAgentCreation.setTransactionPin("1111");
                    sanefAgentCreation.setBusinessAddress(agent.getBuisnessLocation()==null||agent.getBuisnessLocation().equals("")
                            ?"1":agent.getBuisnessLocation());


                    SanefAgentCreationResponse agentCreationResponse = this.createSanefAgent(sanefAgentCreation);
                    if(agentCreationResponse.getResponseCode().equals("00")){
                        agent.setSanefAgentCode(agentCreationResponse.getAgentCode());
                        agentService.updateAgent(agent);
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }

            pageRequest = PageRequest.of(cnt,20);
            pendingAgents = agentService.getUnSyncedSanefAgents(pageRequest);
            hasRecord = pendingAgents.hasNext();
        }
    }

    private SanefAgentCreation setAgentDetails(SanefAgentCreation agentDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(agentDto, SanefAgentCreation.class);
    }


    private SanefAgentCreationResponse sendAgentCreationHttpRequest(SanefAgentCreation agentCreation, String requestUrl) {
        SanefAgentCreationResponse sanefAgentCreationResponse = new SanefAgentCreationResponse();
        try {

            Gson gson = new Gson();
            String agentCreationPayload = gson.toJson(agentCreation);
            logger.info("About to Make API call to {}, With Payload {}", requestUrl, agentCreationPayload);

            String agentResp = sendRequest(requestUrl, agentCreationPayload);
            sanefAgentCreationResponse = new ObjectMapper().readValue(agentResp, SanefAgentCreationResponse.class);

        }
        catch (IOException e) {
            logger.info(EXCEPTION_CAUGHT, e.getMessage());
        }
        return sanefAgentCreationResponse;

    }



    private String sendRequest(String url, String payload) {
        String responseMessage = "";
        try {
            HttpClient client = HttpClientBuilder.create().build();
            org.apache.http.HttpEntity httpEntity = new ByteArrayEntity(payload.getBytes(StandardCharsets.UTF_8));
            logger.info("Received http Entity is {}", httpEntity);
            logger.info("The Url {}", url);
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(httpEntity);
            HttpResponse httpResponse = client.execute(httpPost);
            logger.info("Http Response Message {} ", httpResponse);
            responseMessage = EntityUtils.toString(httpResponse.getEntity());
            logger.info("Received response Message is {} ", responseMessage);
        }
        catch (Exception e) {
            logger.info("exception caught: {}", e.getMessage());
        }
        return responseMessage;
    }



}
