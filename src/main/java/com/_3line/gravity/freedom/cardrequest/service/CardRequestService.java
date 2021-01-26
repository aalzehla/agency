package com._3line.gravity.freedom.cardrequest.service;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.api.transaction.dto.CardRequestDTO;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.usermgt.service.ApplicationUserService;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.cardrequest.models.CardRequest;
import com._3line.gravity.freedom.cardrequest.repository.CardRequestRepository;
import com._3line.gravity.freedom.utility.DateUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class CardRequestService {

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ApplicationUserService userService;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    CardRequestRepository requestRepository;




    public void addCardRequest(CardRequestDTO cardRequestDTO){

        Agents agent = jwtUtility.getCurrentAgent();
        CardRequest cardRequest = modelMapper.map(cardRequestDTO,CardRequest.class);
        cardRequest.setAgentName(agent.getUsername());
        cardRequest.setStatus("PENDING");
        requestRepository.save(cardRequest);
    }


    public Page<CardRequest> getCards(String agentName, String status, String startDate, String endDate, Pageable pageable){
        Date startDateObj = new Date();
        Date endDateObj = new Date();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            startDateObj = dateFormat.parse(startDate);
            endDateObj = dateFormat.parse(endDate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return requestRepository.findByAgentNameContainsAndStatusAndCreatedOnBetweenOrderByCreatedOnDesc
                (agentName, status, startDateObj, DateUtil.AddDays(endDateObj, 1), pageable);
    }

    public Page<CardRequest> getCards(Pageable pageable){

        return requestRepository.findAllByOrderByCreatedOnDesc(pageable);
    }

    public CardRequest getCardById(Long id){

        return requestRepository.findById(id).orElse(null);
    }

    public CardRequest setCardAsDelivered(CardRequest cardRequest){

        if(!cardRequest.getStatus().equals("DELIVERED")){
            throw new GravityException("Invalid Status Passed");
        }
        CardRequest request =  requestRepository.findById(cardRequest.getId()).orElse(null);
        request.setStatus(cardRequest.getStatus());
        request.setDeliveredOn(new Date());
        request = requestRepository.save(request);
        return request;
    }

}
