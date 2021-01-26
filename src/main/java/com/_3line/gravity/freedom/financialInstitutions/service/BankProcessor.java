package com._3line.gravity.freedom.financialInstitutions.service;


import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.financialInstitutions.dtos.*;

public interface BankProcessor {

    Response performDeposit(DepositRequest depositRequest,Long tranId) ;

    Response performWithdrawal(GravityWithdrawalRequest gravityWithdrawalRequest) ;

    Response performTransfer(TransferRequest transferRequest) ;

    Response enterPin(EnterPinDTO enterPinDTO);

    Response enterOtp(EnterOtpDTO enterOtpDTO);

    Response nameEnquiry(NameEnquiryDTO nameEnquiryDTO);

}
