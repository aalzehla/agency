package com._3line.gravity.freedom.financialInstitutions.accountopening.service;


import com._3line.gravity.freedom.financialInstitutions.accountopening.dtos.AgentToCommission;
import com._3line.gravity.freedom.financialInstitutions.accountopening.model.AccountOpening;
import com._3line.gravity.freedom.financialInstitutions.dtos.AccOpeningGeneral;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountOpeningResponse;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountRequest;

import java.util.List;

public interface AccountService {

    AccOpeningGeneral openAccountGeneral(AccOpeningGeneral accOpeningGeneral);


    SanefAccountOpeningResponse openAccountSanef(SanefAccountRequest accountRequest);

    List <AgentToCommission> getCountPendingCommissionPerAgent();

    void processCommissionForAccountOpening(Long agentId);


    //get all customers
    List<AccountOpening> getAllCustomers();


//    List<AccountOpening> getAgentsPendingCommission(String agentName);
    // get all pending commission
    // sort pendning commissions by agent
    // pay commission to agent
    // pay commission for particular opening

}
