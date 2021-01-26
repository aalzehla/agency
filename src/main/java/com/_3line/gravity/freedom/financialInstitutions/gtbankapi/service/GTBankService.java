package com._3line.gravity.freedom.financialInstitutions.gtbankapi.service;


import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.models.*;

public interface GTBankService {

    /**
     * This is method retrieves Account Balance, with AccBalRetrievalAPI as the request and response
     * @param accBalRetrievalAPI
     * @return accBalRetrieveAPI
     */
    AccBalRetrievalAPI retrieveAccountBalance(AccBalRetrievalAPI accBalRetrievalAPI);

    /**
     * This method performs single account transfer, with SingleTransferAPI as the request and response
     * @param singleTransferAPI
     * @return SingleTransferAPI
     */
    SingleTransferAPI performSingleTransfer(SingleTransferAPI singleTransferAPI);

    /**
     * This method performs Freedom account transfer, with FreedomGTB as the request and response
     * @param freedomGTB
     * @return FreedomGTB
     */
    FreedomGTB performFreedomTransfer(FreedomGTB freedomGTB);

    /**
     * This method is used to generate the Single transfer SHA Code
     * @param singleTransferAPI
     * @return SHACode
     */
    String getSingleTransferSHACode(SingleTransferAPI singleTransferAPI);

    /**
     * This method does Transaction Requery, with TransactionRequery as the request and response
     * @param transactionRequery
     * @return TransactionRequery
     */
    TransactionRequery getTransactionRequery(TransactionRequery transactionRequery);

    /**
     * This method checks if the BVN in the GetBVNDetailsRequest exists
     * @param getBVNDetailsRequest
     * @return GetBVNDetailsResponse
     */
    GetBVNDetailsResponse checkBVN(GetBVNDetailsRequest getBVNDetailsRequest);

    /**
     * This method returns the Check BVN response
     * @param accOpeningGTB
     * @return Response Code
     */
    String checkBVNResponse(AccOpeningGTB accOpeningGTB);

    /**
     * This method does Account Opening, with AccOpeningGTB as the request and response
     * @param accOpeningGTB
     * @return AccOpeningGTB
     */
    AccOpeningGTB openGTBAccount(AccOpeningGTB accOpeningGTB);


    FACValidateResponse validateFacCode(FACValidate  facValidate) throws GravityException;

    FACTranResponse cashOut(FACTranRequest  facTranRequest) throws GravityException;

}
