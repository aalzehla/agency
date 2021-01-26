package com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.service;


import com._3line.gravity.api.wallet.dto.FundWalletDTO;
import com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.dto.DebitCardDTO;

import java.math.BigDecimal;
import java.util.List;

public interface DebitCardService {


    DebitCardDTO addCard(DebitCardDTO debitCardDTO);

    void deleteCard(String token);

    DebitCardDTO getchCardByToken(String token);

    String debitCard(FundWalletDTO fundWalletDTO);

    List<DebitCardDTO> fetchCards();

}

