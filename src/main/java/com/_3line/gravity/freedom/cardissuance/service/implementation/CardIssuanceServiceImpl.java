package com._3line.gravity.freedom.cardissuance.service.implementation;


import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.code.dto.CodeDTO;
import com._3line.gravity.core.code.service.CodeService;
import com._3line.gravity.core.integration.service.IntegrationService;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.utils.DateUtil;
import com._3line.gravity.core.utils.ResponseUtils;
import com._3line.gravity.core.utils.ValidationUtils;
import com._3line.gravity.freedom.accountmgt.model.BankAccountOpening;
import com._3line.gravity.freedom.accountmgt.repository.BankAccountOpeningRepository;
import com._3line.gravity.freedom.accountmgt.repository.WalletAccountOpeningRepository;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.cardissuance.client.FCMBCardIssuanceService;
import com._3line.gravity.freedom.cardissuance.dto.CardIssuanceDTO;
import com._3line.gravity.freedom.cardissuance.dto.CardIssuanceRequestDTO;
import com._3line.gravity.freedom.cardissuance.dto.CardIssuanceResponseDTO;
import com._3line.gravity.freedom.cardissuance.exception.CardIssuanceException;
import com._3line.gravity.freedom.cardissuance.model.CardIssuance;
import com._3line.gravity.freedom.cardissuance.repository.CardIssuanceRepository;
import com._3line.gravity.freedom.cardissuance.service.CardIssuanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author FortunatusE
 * @date 12/10/2018
 */

@Service
public class CardIssuanceServiceImpl implements CardIssuanceService {

    private static final Logger logger = LoggerFactory.getLogger(CardIssuanceServiceImpl.class);

    private final CardIssuanceRepository cardIssuanceRepository;
    private final IntegrationService integrationService;
    private final BankAccountOpeningRepository accountOpeningRepository;
    private final WalletAccountOpeningRepository walletAccountOpeningRepository;

    private final AgentService agentService;
    private final CodeService codeService;
    private final JwtUtility loggedInAgent;
    private final FCMBCardIssuanceService fcmbCardIssuanceService;

    private EntityManager entityManager;




    @Autowired
    public CardIssuanceServiceImpl(CardIssuanceRepository cardIssuanceRepository, IntegrationService integrationService,JwtUtility loggedInAgent, BankAccountOpeningRepository accountOpeningRepository, AgentService agentService, CodeService codeService , WalletAccountOpeningRepository walletAccountOpeningRepository, @Qualifier("newCardIssuanceService") FCMBCardIssuanceService fcmbCardIssuanceService,
                                   EntityManager entityManager) {
        this.cardIssuanceRepository = cardIssuanceRepository;
        this.integrationService = integrationService;
        this.accountOpeningRepository = accountOpeningRepository;
        this.agentService = agentService;
        this.codeService = codeService;
        this.loggedInAgent = loggedInAgent;
        this.walletAccountOpeningRepository = walletAccountOpeningRepository;
        this.fcmbCardIssuanceService = fcmbCardIssuanceService;
        this.entityManager = entityManager;
    }

    @Override
    public Response processCardIssuance(CardIssuanceRequestDTO issuanceRequest) {

        logger.debug("Processing card issuance request: {}", issuanceRequest);

        try {
            validateCardIssuanceRequest(issuanceRequest);
        } catch (CardIssuanceException e) {
            logger.error(e.getMessage());
            return ResponseUtils.createFailureResponse(e.getMessage());
        }

        CardIssuance cardIssuance = saveCardIssuance(issuanceRequest);
        try {
            CardIssuanceResponseDTO cardIssuanceResponse = fcmbCardIssuanceService.sendCardIssuanceRequest(issuanceRequest);
            logger.debug("Card issuance response: {}", cardIssuanceResponse);
            updateCardIssuance(cardIssuance, cardIssuanceResponse);
            if (cardIssuanceResponse != null) {
//                auditService.addAgentAudit("CARD_ISSUANCE", String.valueOf(cardIssuanceResponse.isSuccessful()), cardIssuanceResponse.getStatusMessage());

                if(cardIssuanceResponse.isSuccessful()) {
                    return ResponseUtils.createSuccessResponse(cardIssuanceResponse.getStatusMessage());
                }
                else {
                    return ResponseUtils.createFailureResponse(cardIssuanceResponse.getStatusMessage());
                }
            }
//            auditService.addAgentAudit("CARD_ISSUANCE", "FAILED", "No response from service");
            return ResponseUtils.createDefaultFailureResponse();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
//            auditService.addAgentAudit("CARD_ISSUANCE", "FAILED", e.getMessage());
            return ResponseUtils.createFailureResponse(e.getMessage());

        }
    }

    @Override
    public Response getCardTypes() {

        logger.debug("Retrieving card types");
        List<CodeDTO> cardTypes = codeService.getCodesByType("CARD_TYPE");
        List<String> cards = cardTypes.stream().map(codeDTO -> codeDTO.getDescription()).collect(Collectors.toList());
        return ResponseUtils.createResponse(cards);
    }

    private void validateCardIssuanceRequest(CardIssuanceRequestDTO cardIssuanceRequest) throws CardIssuanceException {


        String errorMessage = ValidationUtils.validateObject(cardIssuanceRequest);
        if (!errorMessage.isEmpty()) {
            throw new CardIssuanceException(errorMessage);
        }

        String accountNumber = cardIssuanceRequest.getAccountNumber();

        BankAccountOpening accountOpening = accountOpeningRepository.findByAccountNumber(cardIssuanceRequest.getAccountNumber());

        String accountNumberPrefix = accountNumber.substring(0,3);
        logger.info("Account Number Prefix {}", accountNumberPrefix);

        if (accountOpening == null && !accountNumberPrefix.equalsIgnoreCase("234")) {
            throw new CardIssuanceException("Account number not found");
        }

        CardIssuance cardIssuance = cardIssuanceRepository.findByAccountNumberAndCardSerial(cardIssuanceRequest.getAccountNumber(), cardIssuanceRequest.getCardSerial());
        if (cardIssuance != null && cardIssuance.isIssued()) {
            throw new CardIssuanceException("Card already issued to customer");
        }
    }


    private CardIssuance saveCardIssuance(CardIssuanceRequestDTO cardIssuanceRequest) {

        logger.debug("Saving card issuance request: {}", cardIssuanceRequest);

        CardIssuance cardIssuance = cardIssuanceRepository.findByAccountNumberAndCardSerial(cardIssuanceRequest.getAccountNumber(), cardIssuanceRequest.getCardSerial());
        if (cardIssuance != null) {
            logger.info("Card issuance request already existing and would be returned instead");
            return cardIssuance;
        }
        cardIssuance = convertDtoToModel(cardIssuanceRequest);

        Agents agent = loggedInAgent.getCurrentAgent();
        logger.debug("Logged in agent: {}", agent.getFirstName() + " " + agent.getLastName());
        cardIssuance.setAgent(agent);

        CardIssuance savedCardIssuance = cardIssuanceRepository.save(cardIssuance);
        return savedCardIssuance;
    }

    @Transactional
    private void updateCardIssuance(CardIssuance newCardIssuance, CardIssuanceResponseDTO cardIssuanceResponse) {

        logger.debug("Updating card issuance with response: {}", newCardIssuance);

        if (cardIssuanceResponse != null) {

            try {
                CardIssuance cardIssuance = cardIssuanceRepository.findOne(newCardIssuance.getId());

                cardIssuance.setStatusMessage(cardIssuanceResponse.getStatusMessage());

                if(cardIssuanceResponse.isSuccessful()) {
                    cardIssuance.setLinked(true);
                    cardIssuance.setIssued(true);
                }
                cardIssuanceRepository.save(cardIssuance);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }



    @Override
    public Page<CardIssuanceDTO> getCardIssuances(String fromDate, String toDate, String status, String agentId, Pageable pageable) {

        logger.debug("Status: {},  From: {} To {}, agentId {}", status, fromDate, toDate, agentId);


        ArrayList<Predicate> predicates = new ArrayList<>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<CardIssuance> baseQuery = cb.createQuery(CardIssuance.class);
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class).orderBy();

        Root<CardIssuance> transactionRoot = baseQuery.from(CardIssuance.class);

        Predicate jointPredicate;

        if(agentId!=null && !agentId.equals("")){
            Predicate agentPredicate = cb.equal(transactionRoot.get("createdBy"), agentId);
            predicates.add(agentPredicate);
        }


        if(status!=null && !status.equals("")){

            boolean linked = "1".equals(status)?true:false;

            Predicate linkedPredicate = cb.equal(transactionRoot.get("linked"), linked);
            predicates.add(linkedPredicate);
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
            countQuery = countQuery.select(cb.count(countQuery.from(CardIssuance.class)));
        }else{
            Predicate[] predicateArray = new Predicate[predicates.size()];
            for(int a=0;a<predicates.size();a++){
                predicateArray[a] = predicates.get(a);
            }
            jointPredicate = cb.and(predicateArray);
            baseQuery = baseQuery.select(transactionRoot).where(jointPredicate).orderBy(cb.desc(transactionRoot.get("dateCreated")));
            countQuery = countQuery.select(cb.count(countQuery.from(CardIssuance.class))).where(jointPredicate);
        }

        TypedQuery<CardIssuance> query = entityManager.createQuery(baseQuery);
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        System.out.println("i gat :: "+query.getResultList().size());

        return new PageImpl<>(convertEntitiesToDtos(query.getResultList()),pageable,count);

    }


    private List<CardIssuanceDTO> convertEntitiesToDtos(List<CardIssuance> cardIssuances) {
        return cardIssuances.stream().map(cardIssuance -> convertEntityToDto(cardIssuance)).collect(Collectors.toList());
    }

    private CardIssuance convertDtoToModel(CardIssuanceRequestDTO issuanceRequest) {

        CardIssuance cardIssuance = new CardIssuance();
        cardIssuance.setAccountNumber(issuanceRequest.getAccountNumber());
        cardIssuance.setCardSerial(issuanceRequest.getCardSerial());
        cardIssuance.setCardType(issuanceRequest.getCardType());
        cardIssuance.setCreatedBy(issuanceRequest.getCreatedBy());
        cardIssuance.setHeader(issuanceRequest.getHeader());
        return cardIssuance;
    }

    private CardIssuanceDTO convertEntityToDto(CardIssuance cardIssuance) {

        CardIssuanceDTO cardIssuanceDTO = new CardIssuanceDTO();
        String issuerId = cardIssuance.getCreatedBy();
        Agents agent = agentService.getAgentByAgentId(issuerId);
        if (agent != null) {
            cardIssuanceDTO.setAgentId(issuerId);
        }else {
            cardIssuanceDTO.setOperatorId(issuerId);
        }
        cardIssuanceDTO.setAccountNumber(cardIssuance.getAccountNumber());
        cardIssuanceDTO.setCardSerial(cardIssuance.getCardSerial());
        cardIssuanceDTO.setCardType(cardIssuance.getCardType());
        cardIssuanceDTO.setCreatedBy(cardIssuance.getCreatedBy());
        cardIssuanceDTO.setStatusMessage(cardIssuance.getStatusMessage());
        cardIssuanceDTO.setIssued(cardIssuance.isIssued());
        cardIssuanceDTO.setLinked(cardIssuance.isLinked());
        cardIssuanceDTO.setDateIssued(cardIssuance.getCreatedOn());
        return cardIssuanceDTO;
    }
}
