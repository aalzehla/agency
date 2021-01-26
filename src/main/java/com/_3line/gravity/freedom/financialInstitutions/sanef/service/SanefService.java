package com._3line.gravity.freedom.financialInstitutions.sanef.service;

import com._3line.gravity.freedom.agents.dtos.AgentDto;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountOpeningResponse;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountRequest;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAgentCreation;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAgentCreationResponse;

public interface SanefService  {



    String generateKeys();

    SanefAccountOpeningResponse sanefAccountOpening(SanefAccountRequest payload);

    SanefAgentCreationResponse createSanefAgent(SanefAgentCreation agentDto);
}
