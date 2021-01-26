package com._3line.gravity.freedom.accountmgt.service.implementation;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.code.dto.CodeDTO;
import com._3line.gravity.core.code.service.CodeService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.integration.service.IntegrationService;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.utils.DateUtil;
import com._3line.gravity.core.utils.ResponseUtils;
import com._3line.gravity.core.utils.ValidationUtils;
import com._3line.gravity.freedom.accountmgt.dtos.accountopening.BankAccountOpeningDTO;
import com._3line.gravity.freedom.accountmgt.dtos.accountopening.BankAccountOpeningRequest;
import com._3line.gravity.freedom.accountmgt.dtos.accountopening.BankAccountOpeningResponse;
import com._3line.gravity.freedom.accountmgt.dtos.accountopening.NameEnquiryDTO;
import com._3line.gravity.freedom.accountmgt.dtos.walletopening.WalletAccountOpeningDTO;
import com._3line.gravity.freedom.accountmgt.dtos.walletopening.WalletAccountOpeningRequest;
import com._3line.gravity.freedom.accountmgt.dtos.walletopening.WalletAccountOpeningResponse;
import com._3line.gravity.freedom.accountmgt.model.BankAccountOpening;
import com._3line.gravity.freedom.accountmgt.model.WalletAccountOpening;
import com._3line.gravity.freedom.accountmgt.repository.BankAccountOpeningRepository;
import com._3line.gravity.freedom.accountmgt.repository.WalletAccountOpeningRepository;
import com._3line.gravity.freedom.accountmgt.service.AccountService;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AccountServiceImplementation implements AccountService {


    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImplementation.class);

    private final IntegrationService integrationService;
    private final BankAccountOpeningRepository bankAccountOpeningRepository;
    private final WalletAccountOpeningRepository walletAccountOpeningRepository;
    private final AgentService agentService;
    private final JwtUtility loggedInAgent;
    private final CodeService codeService;
    private final ModelMapper modelMapper;

    @Value("${base.url}")
    private String BASE_URL;

    @Value("${endpoint.nameenquiry.path}")
    private String nameEnquiryUrl;

    @Value("${endpoint.account.opening}")
    private String ACCOUNT_OPENING_ENDPOINT;

    @Value("${endpoint.wallet.opening}")
    private String WALLET_OPENING_ENDPOINT;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    EntityManager entityManager;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    public AccountServiceImplementation(IntegrationService integrationService, BankAccountOpeningRepository bankAccountOpeningRepository, WalletAccountOpeningRepository walletAccountOpeningRepository, AgentService agentService, JwtUtility loggedInAgent, CodeService codeService, ModelMapper modelMapper) {
        this.integrationService = integrationService;
        this.bankAccountOpeningRepository = bankAccountOpeningRepository;
        this.walletAccountOpeningRepository = walletAccountOpeningRepository;
        this.agentService = agentService;
        this.loggedInAgent = loggedInAgent;
        this.codeService = codeService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Response openBankAccount(BankAccountOpeningRequest bankAccountOpeningRequest) {
        logger.debug("Opening bank account: {}", bankAccountOpeningRequest);


        try {
            validateRequestFields(bankAccountOpeningRequest);
        } catch (Exception e) {
            return ResponseUtils.createFailureResponse(e.getMessage());
        }
        BankAccountOpeningResponse bankAccountOpeningResponse;

        //Save the request details which will be updated when we get a response

        bankAccountOpeningRequest.setPhoneNumber(Arrays.asList(bankAccountOpeningRequest.getMobile()));
        Response response;
        String url = BASE_URL + ACCOUNT_OPENING_ENDPOINT;
        try {
            Agents agent = jwtUtility.getCurrentAgent();

            if(bankAccountOpeningRequest.getBrokerCode() == null) {
//                bankAccountOpeningRequest.setBrokerCode(agent.getBrokerCode());
            }
            if(bankAccountOpeningRequest.getAccountOfficerCode() == null) {
//                bankAccountOpeningRequest.setAccountOfficerCode(agent.getAoCode());
            }
            BankAccountOpening bankAccountOpening = saveBankAccountOpening(bankAccountOpeningRequest);
            response = integrationService.doPost(url, bankAccountOpeningRequest);
            logger.debug("Response: {}", response);

            bankAccountOpeningResponse = (BankAccountOpeningResponse) ResponseUtils.convertResponseBodyToClassInstance(response, BankAccountOpeningResponse.class);
            logger.debug("Bank account response: {}", bankAccountOpeningResponse);

            //update the account opening details with the response
            updateBankAccountOpening(bankAccountOpening, bankAccountOpeningResponse);
//            auditService.addAgentAudit("BANK_ACCOUNT_OPENING", bankAccountOpeningResponse != null ? bankAccountOpeningResponse.getResponseCode() : "", bankAccountOpeningResponse != null ? bankAccountOpeningResponse.getResponseMessage() : "");
            if (bankAccountOpeningResponse != null) {
                if ("00".equals(bankAccountOpeningResponse.getResponseCode())) {
                    return ResponseUtils.createResponse(bankAccountOpeningResponse.getResponseCode(), bankAccountOpeningResponse.getResponseMessage(), bankAccountOpeningResponse);
                } else {
                    return ResponseUtils.createResponse(bankAccountOpeningResponse.getResponseCode(), bankAccountOpeningResponse.getErrorMessage(), bankAccountOpeningResponse);

                }
            } else {
                return ResponseUtils.createFailureResponse("No response");

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
//            auditService.addAgentAudit("BANK_ACCOUNT_OPENING", "FAILED", e.getMessage());
            return ResponseUtils.createFailureResponse(e.getMessage());

        }

    }


    @Override
    public Response openWalletAccount(WalletAccountOpeningRequest walletAccountOpeningRequest) {
        logger.debug("Opening wallet account: {}", walletAccountOpeningRequest);

        try {
            validateRequestFields(walletAccountOpeningRequest);
        } catch (Exception e) {
            return ResponseUtils.createFailureResponse(e.getMessage());
        }

        //Save the request details which will be updated when we get a response

        Response response;
        String url = BASE_URL + WALLET_OPENING_ENDPOINT;

        try {
            WalletAccountOpening walletAccountOpening = saveWalletAccountOpening(walletAccountOpeningRequest);

            response = integrationService.doPost(url, walletAccountOpeningRequest);
            logger.debug("Response: {}", response);

            WalletAccountOpeningResponse walletAccountOpeningResponse = (WalletAccountOpeningResponse) ResponseUtils.convertResponseBodyToClassInstance(response, WalletAccountOpeningResponse.class);
            logger.debug("Wallet account response: {}", walletAccountOpeningResponse);
            //update the account opening details with the response
            updateWalletAccountOpening(walletAccountOpening, response, walletAccountOpeningResponse);
//            auditService.addAgentAudit("WALLET_ACCOUNT_OPENING", response != null ? response.getRespCode() : "", response != null ? response.getRespDescription() : "");

            if (response != null ) {
                return ResponseUtils.createResponse(response.getRespCode(), response.getRespDescription(), walletAccountOpeningResponse);
            } else {
                return ResponseUtils.createFailureResponse("No response");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
//            auditService.addAgentAudit("WALLET_ACCOUNT_OPENING", "FAILED", e.getMessage());
            return ResponseUtils.createFailureResponse(e.getMessage());

        }
    }


    private BankAccountOpening saveBankAccountOpening(BankAccountOpeningRequest bankAccountOpeningRequest) {

        logger.debug("Saving account opening request: {}", bankAccountOpeningRequest);
        BankAccountOpening bankAccountOpening = convertBankAccountOpeningRequestToModel(bankAccountOpeningRequest);

        Agents agent = jwtUtility.getCurrentAgent();
        logger.debug("Logged in agent: {}", agent.getFirstName() + " " + agent.getLastName());
        bankAccountOpening.setAgent(agent);
        BankAccountOpening savedBankAccountOpening = bankAccountOpeningRepository.save(bankAccountOpening);
        return savedBankAccountOpening;

    }

    @Transactional
    void updateBankAccountOpening(BankAccountOpening bankAccountOpening, BankAccountOpeningResponse bankAccountOpeningResponse) {

        logger.debug("Updating bank account opening with response: {}", bankAccountOpeningResponse);

        if (bankAccountOpeningResponse != null) {

            try {
                BankAccountOpening accountOpening = bankAccountOpeningRepository.findOne(bankAccountOpening.getId());
                accountOpening.setAccountNumber(bankAccountOpeningResponse.getAccountNumber());
                accountOpening.setCifId(bankAccountOpeningResponse.getCifId());
                accountOpening.setReferenceNo(bankAccountOpeningResponse.getReferenceNo());
                accountOpening.setResponseCode(bankAccountOpeningResponse.getResponseCode());
                accountOpening.setResponseMessage(bankAccountOpeningResponse.getResponseMessage());
                accountOpening.setErrorMessage(bankAccountOpeningResponse.getErrorMessage());
                bankAccountOpeningRepository.save(accountOpening);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private WalletAccountOpening saveWalletAccountOpening(WalletAccountOpeningRequest walletAccountOpeningRequest) {

        logger.debug("Saving wallet opening request: {}", walletAccountOpeningRequest);
        WalletAccountOpening walletAccountOpening = convertWalletAccountOpeningRequestToModel(walletAccountOpeningRequest);

        Agents agent = jwtUtility.getCurrentAgent();
        logger.debug("Logged in agent: {}", agent.getFirstName() + " " + agent.getLastName());
        walletAccountOpening.setAgent(agent);
        WalletAccountOpening savedWalletAccountOpening = walletAccountOpeningRepository.save(walletAccountOpening);
        return savedWalletAccountOpening;
    }


    @Transactional
    void updateWalletAccountOpening(WalletAccountOpening walletAccountOpening, Response response, WalletAccountOpeningResponse walletAccountOpeningResponse) {

        logger.debug("Updating wallet opening with response: {}", walletAccountOpeningResponse);

        if (walletAccountOpeningResponse != null) {
            try {
                WalletAccountOpening accountOpening = walletAccountOpeningRepository.findOne(walletAccountOpening.getId());
                accountOpening.setResponseCode(response.getRespCode());
                accountOpening.setResponseMessage(response.getRespDescription());
                accountOpening.setRemark(walletAccountOpeningResponse.getRemark());
                walletAccountOpeningRepository.save(accountOpening);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private BankAccountOpening convertBankAccountOpeningRequestToModel(BankAccountOpeningRequest openingRequest) {

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(openingRequest, BankAccountOpening.class);
    }

    private WalletAccountOpening convertWalletAccountOpeningRequestToModel(WalletAccountOpeningRequest openingRequest) {
        return modelMapper.map(openingRequest, WalletAccountOpening.class);
    }

    private void validateRequestFields(Object object) {
        logger.debug("Validating request fields: {}", object);
        String errorMessage = ValidationUtils.validateObject(object);
        if (!errorMessage.isEmpty()) {
            logger.error(errorMessage);
            throw new GravityException(errorMessage);
        }
    }

    @Override
    public List<String> getAccountTypes() {

        logger.debug("Retrieving bank account types");
        List<CodeDTO> accountCodes = codeService.getCodesByType("ACCOUNT_TYPE");
        List<String> accountTypes = accountCodes.stream().map(accountCode -> accountCode.getCode()).collect(Collectors.toList());
        return accountTypes;
    }

    @Override
    public List<String> getBranches() {

        logger.debug("Retrieving branches");
        List<CodeDTO> accountCodes = codeService.getCodesByType("BRANCH");
        List<String> accountTypes = accountCodes.stream().map(accountCode -> accountCode.getCode()).collect(Collectors.toList());
        return accountTypes;
    }

    @Override
    public NameEnquiryDTO doNameEnquiry(String acctNumber) {
        NameEnquiryDTO nameEnquiryDTO = null;
        logger.debug("Fetching farm inputs");
        String url = BASE_URL + nameEnquiryUrl;
        logger.info("Making API call to {}", url);


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type","application/json");
        HttpEntity httpEntity = new HttpEntity(acctNumber,httpHeaders);

        try {

            ResponseEntity<Response> entity =
                    restTemplate.exchange(url, HttpMethod.POST, httpEntity, Response.class);

            logger.info("Name Enquiry Response status: {}", entity.getStatusCode());
            logger.info("Name Enquiry Response body: {}", entity.getBody());
            ObjectMapper mapper = new ObjectMapper();
            nameEnquiryDTO = mapper.convertValue(entity.getBody().getRespBody(),NameEnquiryDTO.class);

        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return nameEnquiryDTO;
    }


    @Override
    public Page<BankAccountOpeningDTO> findBankAccountOpenings(String pattern, Pageable pageable) {

        logger.debug("Searching bank account openings for {}", pattern);
        Page<BankAccountOpening> bankAccountOpenings = bankAccountOpeningRepository.findUsingPattern(pattern, pageable);
        List<BankAccountOpeningDTO> bankAccountOpeningDtos = bankAccountOpenings.getContent().stream().map(bankAccountOpening -> convertBankAccountOpeningEntityToDto(bankAccountOpening)).collect(Collectors.toList());
        return new PageImpl<BankAccountOpeningDTO>(bankAccountOpeningDtos, pageable, bankAccountOpenings.getTotalElements());
    }

    @Override
    public Page<BankAccountOpeningDTO> findBankAccountOpeningsByAccountNumberFromAndToDate(String accountNumber, String agentId, String fromDate, String toDate, Pageable pageable) {

        logger.info("accountNumber : {}, agentId: {}, fromDate: {}, toDate {}",accountNumber, agentId, fromDate, toDate);

        ArrayList<Predicate> predicates = new ArrayList<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<BankAccountOpening> baseQuery = cb.createQuery(BankAccountOpening.class);
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class).orderBy();

        Root<BankAccountOpening> transactionRoot = baseQuery.from(BankAccountOpening.class);

        Predicate jointPredicate;

        if(accountNumber!=null && !accountNumber.equals("")){
            Predicate accountPredicate = cb.equal(transactionRoot.get("accountNumber"), accountNumber);
            predicates.add(accountPredicate);
        }


        if(agentId!=null && !agentId.equals("")){

            //subquery
            Subquery<Agents> agentsSubquery = baseQuery.subquery(Agents.class);
            Root<Agents> agentRoot = agentsSubquery.from(Agents.class);
            agentsSubquery.select(agentRoot)//subquery selection
                    .where(cb.equal(agentRoot.get("agentId"),agentId));
            Predicate agentIdPredicate = cb.exists(agentsSubquery);
            predicates.add(agentIdPredicate);
        }


        if(fromDate!=null && !fromDate.equals("") && toDate!=null && !toDate.equals("")){
            Date startDate;
            Date endDate;
            try {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
                startDate = dateFormatter.parse(fromDate);
                endDate = dateFormatter.parse(toDate);
            } catch (Exception e) {
                startDate = new Date();
                endDate = DateUtil.AddDays(startDate, 1);
                logger.error(e.getMessage());
            }

            Predicate datePredicate = cb.and(cb.between(transactionRoot.get("dateCreated"), startDate, endDate));

            predicates.add(datePredicate);
        }

        if(predicates.size() == 0){
            baseQuery = baseQuery.select(transactionRoot).orderBy(cb.desc(transactionRoot.get("dateCreated")));
            countQuery = countQuery.select(cb.count(countQuery.from(BankAccountOpening.class)));
        }else{
            Predicate[] predicateArray = new Predicate[predicates.size()];
            for(int a=0;a<predicates.size();a++){
                predicateArray[a] = predicates.get(a);
            }
            jointPredicate = cb.and(predicateArray);
            baseQuery = baseQuery.select(transactionRoot).where(jointPredicate).orderBy(cb.desc(transactionRoot.get("dateCreated")));
            countQuery = countQuery.select(cb.count(countQuery.from(BankAccountOpening.class))).where(jointPredicate);
        }

        TypedQuery<BankAccountOpening> query = entityManager.createQuery(baseQuery);
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        System.out.println("i gat :: "+query.getResultList().size());

        return new PageImpl<>(convertEntitiesToDtos(query.getResultList()),pageable,count);

   }

    @Override
    public Page<BankAccountOpeningDTO> findBankAccountOpeningsByAccNumber(String accountNumber, Pageable pageable) {
        return null;
    }


    @Override
    public Page<WalletAccountOpeningDTO> findWalletAccountOpenings(String pattern, Pageable pageable) {

        logger.debug("Searching wallet account openings for {}", pattern);
        Page<WalletAccountOpening> walletAccountOpenings = walletAccountOpeningRepository.findUsingPattern(pattern, pageable);
        List<WalletAccountOpeningDTO> walletAccountOpeningDtos = walletAccountOpenings.getContent().stream().map(walletAccountOpening -> convertWalletAccountOpeningEntityToDto(walletAccountOpening)).collect(Collectors.toList());
        return new PageImpl<WalletAccountOpeningDTO>(walletAccountOpeningDtos, pageable, walletAccountOpenings.getTotalElements());
    }

    @Override
    public Page<BankAccountOpeningDTO> getBankAccountOpenings(Pageable pageable) {

        logger.debug("Retrieving bank account openings");
        Page<BankAccountOpening> bankAccountOpenings = bankAccountOpeningRepository.findAll(pageable);
        List<BankAccountOpeningDTO> bankAccountOpeningDtos = bankAccountOpenings.getContent().stream().map(bankAccountOpening -> convertBankAccountOpeningEntityToDto(bankAccountOpening)).collect(Collectors.toList());
        return new PageImpl<BankAccountOpeningDTO>(bankAccountOpeningDtos, pageable, bankAccountOpenings.getTotalElements());
    }

    @Override
    public Page<WalletAccountOpeningDTO> getWalletAccountOpenings(Pageable pageable) {

        logger.debug("Retrieving wallet account openings");
        Page<WalletAccountOpening> walletAccountOpenings = walletAccountOpeningRepository.findAll(pageable);
        List<WalletAccountOpeningDTO> walletAccountOpeningDtos = walletAccountOpenings.getContent().stream().map(walletAccountOpening -> convertWalletAccountOpeningEntityToDto(walletAccountOpening)).collect(Collectors.toList());
        return new PageImpl<WalletAccountOpeningDTO>(walletAccountOpeningDtos, pageable, walletAccountOpenings.getTotalElements());
    }

    @Override
    public Page<WalletAccountOpeningDTO> findWalletAccountOpeningsByPhoneNumberFromAndToDate(String phoneNumber, String fromDate, String toDate, Pageable pageable) {

        logger.debug("Phone number: {}, From: {} To {}", phoneNumber, fromDate, toDate);

        Date startDate = new Date();
        Date endDate = new Date();

        Page<WalletAccountOpening> walletAccountOpenings = new PageImpl<WalletAccountOpening>(new ArrayList<>(), pageable, 0);

        if ((!fromDate.isEmpty() && !toDate.isEmpty()) || ( phoneNumber != null && !phoneNumber.isEmpty())) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                startDate = dateFormat.parse(fromDate);
                endDate = dateFormat.parse(toDate);

            } catch (Exception e) {
                logger.error(e.getMessage());
            }if (!fromDate.isEmpty() && !toDate.isEmpty()){

                logger.info("Only From Date and To Date Passed");
                walletAccountOpenings = walletAccountOpeningRepository.findByCreatedOnBetweenOrderByCreatedOnDesc(startDate, endDate, pageable);

            }else if (!phoneNumber.isEmpty() && phoneNumber != null){

                logger.info("Only wallet number Passed");
                walletAccountOpenings = walletAccountOpeningRepository.findAllByMobilePhone(phoneNumber, pageable);


            }else if (!fromDate.isEmpty() && !toDate.isEmpty() && phoneNumber != null && !phoneNumber.isEmpty() ) {

                logger.info("From Date, To Date and Account Number Passed");
                walletAccountOpenings = walletAccountOpeningRepository.findByMobilePhoneAndCreatedOnBetweenOrderByCreatedOnDesc(phoneNumber, startDate, endDate, pageable);
            }
        } else {
            walletAccountOpenings = walletAccountOpeningRepository.findAllByOrderByCreatedOnDesc(pageable);
        }

        return new PageImpl<WalletAccountOpeningDTO>(convertWalletAccountEntitiesToDtos(walletAccountOpenings.getContent()), pageable, walletAccountOpenings.getTotalElements());

    }


    private BankAccountOpeningDTO convertBankAccountOpeningEntityToDto(BankAccountOpening bankAccountOpening) {

        logger.debug("Converting entity to dto");

        ModelMapper modelMapper = new ModelMapper();
        BankAccountOpeningDTO accountOpeningDTO = modelMapper.map(bankAccountOpening, BankAccountOpeningDTO.class);
        Agents agentDTO = bankAccountOpening.getAgent();
        logger.info("The Agent {}", agentDTO);
        accountOpeningDTO.setAgentIdentifier(bankAccountOpening.getAgent().getAgentId());
        logger.info("The Agent Id {}", accountOpeningDTO.getAgentIdentifier());
        return accountOpeningDTO;

    }

    private List<WalletAccountOpeningDTO> convertWalletAccountEntitiesToDtos(List<WalletAccountOpening> walletAccountOpenings) {
        return walletAccountOpenings.stream().map(transaction -> convertWalletAccountOpeningEntityToDto(transaction)).collect(Collectors.toList());
    }

    private List<BankAccountOpeningDTO> convertEntitiesToDtos(List<BankAccountOpening> bankAccountOpenings) {
        return bankAccountOpenings.stream().map(transaction -> convertBankAccountOpeningEntityToDto(transaction)).collect(Collectors.toList());
    }

    private WalletAccountOpeningDTO convertWalletAccountOpeningEntityToDto(WalletAccountOpening walletAccountOpening) {

        logger.debug("Converting entity to dto");

        ModelMapper modelMapper = new ModelMapper();
        WalletAccountOpeningDTO accountOpeningDTO = modelMapper.map(walletAccountOpening, WalletAccountOpeningDTO.class);
        accountOpeningDTO.setAgentIdentifier(walletAccountOpening.getAgent().getAgentId());
        return accountOpeningDTO;

    }
}
