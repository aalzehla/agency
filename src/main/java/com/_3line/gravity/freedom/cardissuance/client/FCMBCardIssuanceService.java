package com._3line.gravity.freedom.cardissuance.client;

import com._3line.gravity.freedom.cardissuance.dto.CardIssuanceRequestDTO;
import com._3line.gravity.freedom.cardissuance.dto.CardIssuanceResponseDTO;

/**
 * @author FortunatusE
 * @date 12/11/2018
 */
public interface FCMBCardIssuanceService {


    CardIssuanceResponseDTO sendCardIssuanceRequest(CardIssuanceRequestDTO cardIssuanceRequest);
}
