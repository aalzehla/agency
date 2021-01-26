package com._3line.gravity.freedom.financialInstitutions._9PSB.dto;

import lombok.Data;

@Data
public class InitiateWithdrawalRespDTO extends InitiateWithdrawalReqDTO{

    private String withdrawalTrackingId;
    private String otpTrackingId;

}
