package com._3line.gravity.freedom.financialInstitutions._9PSB.service;

import com._3line.gravity.freedom.financialInstitutions._9PSB.dto.*;

public interface _9psbService {

    ValidateCodeResponseDTO validateCode(ValidateCodeRequestDTO requestDTO);

    DepositResponseDTO depositWithCode(DepositRequestDTO requestDTO);

    DepositWithoutCodeRespDTO depositWithoutCode(DepositWithoutCodeReqDTO requestDTO);

    InitiateWithdrawalRespDTO initiateWithdrawal(InitiateWithdrawalReqDTO requestDTO);

    WithdrawalResponseDTO withdraw(WithdrawalRequestDTO requestDTO);

}
