package com._3line.gravity.freedom.cardissuance.service;

import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.cardissuance.dto.CardIssuanceDTO;
import com._3line.gravity.freedom.cardissuance.dto.CardIssuanceRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * @author FortunatusE
 * @date 12/10/2018
 */


public interface CardIssuanceService {

    Response processCardIssuance(CardIssuanceRequestDTO issuanceRequest);

    Response getCardTypes();


    Page<CardIssuanceDTO> getCardIssuances(String from, String to, String status, String agentId, Pageable pageable);
}
