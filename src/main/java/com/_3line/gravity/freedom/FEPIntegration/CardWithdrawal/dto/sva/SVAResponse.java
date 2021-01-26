package com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.dto.sva;

import lombok.Data;

@Data
public class SVAResponse {

    private String respCode;
    private String respDescription;
    private SendTransactionRespDTO respBody;

}
