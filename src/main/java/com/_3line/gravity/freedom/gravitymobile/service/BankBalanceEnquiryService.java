package com._3line.gravity.freedom.gravitymobile.service;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.financialInstitutions.fidelity.models.AccountBalanceResponse;
import com._3line.gravity.freedom.financialInstitutions.fidelity.service.FidelityWebServices;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.models.AccBalRetrievalAPI;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.service.GTBankService;
import com._3line.gravity.freedom.utility.PropertyResource;
import com._3line.gravity.freedom.wallet.dto.WalletDTO;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankBalanceEnquiryService {

    @Autowired
    private AgentsRepository applicationUsersRepository;
    @Autowired
    private BankDetailsService bankDetailsService;
    @Autowired
    private GTBankService gtBankService;
    @Autowired
    private FidelityWebServices fidelityWebServices;
    @Autowired
    private PropertyResource pr;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private WalletService walletService ;


    public Response getAgentBalance(String userName) {
        Agents applicationUsers = applicationUsersRepository.findByUsername(userName);

        BankDetails bankDetails = bankDetailsService.findByCode(applicationUsers.getBankCode());

        WalletDTO agentWalet = walletService.getWalletByNumber(applicationUsers.getWalletNumber());


            if (!bankDetails.getIsIntegrated()) {
                throw new GravityException("Bank Not Setup");

            }
            Response response = new Response();

            JSONObject jsono = new JSONObject();
            jsono.put("Currency", "NGN");
            jsono.put("AvailableBalance", agentWalet.getAvailableBalance());
            jsono.put("LedgerBalance", agentWalet.getLedgerBalance());

            response.setRespBody(jsono);
            response.setRespCode("00");

            return response;

//            switch (bankDetails.getBankCode()){
//                case "GTB":
//                    return GTBBalance(applicationUsers.getAccountNo());
//                case "FBP":
//                    return FidelityBalance(applicationUsers.getAccountNo());
//                 default:
//                     throw new GravityException("Bank "+bankDetails.getBankCode()+" not setup");
//            }

    }


    private Response  GTBBalance(String account){
        Response response = new Response();


        AccBalRetrievalAPI abrapiRequest = new AccBalRetrievalAPI();
        abrapiRequest.setAccountNumber(account);
        AccBalRetrievalAPI abrapiResponse = gtBankService.retrieveAccountBalance(abrapiRequest);

        String responseCode = abrapiResponse.getResponseCode();
        String responseDesc = abrapiResponse.getResponseDesc();

        if (responseCode.equalsIgnoreCase("1000")) {
            String currency = abrapiResponse.getCurrency();
            JSONObject jsono = new JSONObject();
            jsono.put("Currency", currency);
            jsono.put("AvailableBalance", abrapiResponse.getAvaliableBalance());
            jsono.put("LedgerBalance", abrapiResponse.getLedgerBalance());

            response.setRespBody(jsono);
            response.setRespCode("00");
            response.setRespDescription(responseDesc);

            return response;

        } else {
            response.setRespCode(abrapiResponse.getResponseCode());
            response.setRespDescription(pr.getV(abrapiResponse.getResponseCode(), "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }
    }

    private Response FidelityBalance(String account){
        Response response = new Response();
        BankDetails bankDetails = bankDetailsService.findByCode("FBP");

        AccountBalanceResponse accountBalanceResponse = fidelityWebServices.balanceEnquiry(account , bankDetails.get_3lineAccount());

        if (accountBalanceResponse.getResponseCode().equals("00")) {

            JSONObject jsono = new JSONObject();
            jsono.put("Currency", accountBalanceResponse.getAccountBalance().getAcct_crncy());
            jsono.put("AvailableBalance", accountBalanceResponse.getAccountBalance().getAvailable_balance());
            jsono.put("LedgerBalance", accountBalanceResponse.getAccountBalance().getUnclr_balance());

            response.setRespBody(jsono);
            response.setRespCode("00");
            response.setRespDescription("balance");

            return response;

        } else {
            response.setRespCode(accountBalanceResponse.getResponseCode());
            response.setRespDescription(pr.getV(accountBalanceResponse.getResponseCode(), "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

    }



}
