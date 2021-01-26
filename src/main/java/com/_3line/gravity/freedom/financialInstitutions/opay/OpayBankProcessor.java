package com._3line.gravity.freedom.financialInstitutions.opay;


import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.financialInstitutions.dtos.*;
import com._3line.gravity.freedom.financialInstitutions.service.BankProcessor;

public class OpayBankProcessor implements BankProcessor {
    @Override
    public Response performDeposit(DepositRequest depositRequest,Long tranId) {
        return null;
    }

    @Override
    public Response performWithdrawal(GravityWithdrawalRequest gravityWithdrawalRequest) {
        return null;
    }

    @Override
    public Response performTransfer(TransferRequest transferRequest) {
        return null;
    }

    @Override
    public Response enterPin(EnterPinDTO enterPinDTO) {
        return null;
    }

    @Override
    public Response enterOtp(EnterOtpDTO enterOtpDTO) {
        return null;
    }

    @Override
    public Response nameEnquiry(NameEnquiryDTO nameEnquiryDTO) {
        return null;
    }
}
