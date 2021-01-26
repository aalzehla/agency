package com._3line.gravity.freedom.financialInstitutions.accountopening.service.impl;

import com._3line.gravity.core.setting.dto.SettingDTO;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.repository.BankDetailsRepository;
import com._3line.gravity.freedom.financialInstitutions.accountopening.dtos.AgentToCommission;
import com._3line.gravity.freedom.financialInstitutions.accountopening.model.AccountOpening;
import com._3line.gravity.freedom.financialInstitutions.accountopening.repository.AccountOpeningRepository;
import com._3line.gravity.freedom.financialInstitutions.accountopening.service.AccountService;
import com._3line.gravity.freedom.financialInstitutions.dtos.AccOpeningGeneral;
import com._3line.gravity.freedom.financialInstitutions.fidelity.requests.AcctCreationResponse;
import com._3line.gravity.freedom.financialInstitutions.fidelity.service.FidelityWebServices;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.models.AccOpeningGTB;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.service.GTBankService;
import com._3line.gravity.freedom.financialInstitutions.gtbankapi.utils.DateFormatterGt;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountOpeningResponse;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountRequest;
import com._3line.gravity.freedom.financialInstitutions.sanef.service.SanefService;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.request.StanbicAccountOpeningRequest;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response.StanbicAccountOpeningResponse;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.response.StanbicResponseItem;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.services.StanbicIBTCServices;
import com._3line.gravity.freedom.financialInstitutions.wemaapi.model.AccOpeningWema;
import com._3line.gravity.freedom.financialInstitutions.wemaapi.service.WemaApiService;
import com._3line.gravity.freedom.financialInstitutions.wemaapi.utils.DateFormatter;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    WemaApiService wemaApiService;
    @Autowired
    GTBankService gtBankService;
    @Autowired
    BankDetailsRepository bankDetailsRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountOpeningRepository openingRepository;

    @Autowired
    private SettingService settingService ;
    @Autowired
    private WalletService walletService ;
    @Autowired
    private AgentsRepository agentsRepository;

    @Autowired
    FidelityWebServices fidelityWebServices;

    @Autowired
    private StanbicIBTCServices stanbicIBTCServices;

    @Autowired
    private SanefService sanefService;


    private String getSessionId() {
        Long systemtime = System.currentTimeMillis();
        String time = systemtime.toString().substring(0, 6);
        return time;
    }

//    private AccOpeningWema getWemaAccOpenDetails(AccOpeningGeneral accOpeningGeneral) {
//        AccOpeningWema accOpeningWema = new AccOpeningWema();
//        String wemaDate = DateFormatter.wemaPayloadDate(accOpeningGeneral.getDateOfBirth());
//        modelMapper.map(accOpeningGeneral, accOpeningWema);
//        accOpeningWema.setDateOfBirth(wemaDate);
//        return accOpeningWema;
//    }

//    private AccOpeningGTB getGTBAccOpenDetails(AccOpeningGeneral accOpeningGeneral) {
//        AccOpeningGTB accOpeningGTB = new AccOpeningGTB();
//        String sessionId = getSessionId();
//        String gtDate = DateFormatterGt.gtPayloadDate(accOpeningGeneral.getDateOfBirth());
//        modelMapper.map(accOpeningGeneral, accOpeningGTB);
//        String gender = accOpeningGeneral.getGender();
//        if (gender.equalsIgnoreCase("Male")) {
//            accOpeningGTB.setGender("1");
//        } else {
//            accOpeningGTB.setGender("2");
//        }
//        accOpeningGTB.setDoB(gtDate);
//        accOpeningGTB.setMobileNo(accOpeningGeneral.getPhoneNo());
//        accOpeningGTB.setSessionId(sessionId);
//        return accOpeningGTB;
//    }



    private AccOpeningWema getWemaAccOpenDetails(AccOpeningGeneral accOpeningGeneral) {
        AccOpeningWema accOpeningWema = coverttoAccOpeningGTB(accOpeningGeneral);
        accOpeningWema.setDateOfBirth(accOpeningGeneral.getDateOfBirth());

        return accOpeningWema;
    }

    private AccOpeningGTB getGTBAccOpenDetails(AccOpeningGeneral accOpeningGeneral) {
        AccOpeningGTB accOpeningGTB = new AccOpeningGTB();
        String sessionId = getSessionId();
        String gtDate = DateFormatterGt.gtPayloadDate(accOpeningGeneral.getDateOfBirth());
        modelMapper.map(accOpeningGeneral, accOpeningGTB);
        String gender = accOpeningGeneral.getGender();
        if (gender.equalsIgnoreCase("Male")) {
            accOpeningGTB.setGender("1");
        } else {
            accOpeningGTB.setGender("2");
        }
        accOpeningGTB.setDoB(gtDate);
        accOpeningGTB.setMobileNo(accOpeningGeneral.getPhoneNo());
        accOpeningGTB.setSessionId(sessionId);
        return accOpeningGTB;
    }


    private StanbicAccountOpeningRequest getStanbicAccountOpeningRequest(AccOpeningGeneral accOpeningGeneral){
        StanbicAccountOpeningRequest stanbicAccountOpeningRequest = new StanbicAccountOpeningRequest();
        modelMapper.map(accOpeningGeneral, stanbicAccountOpeningRequest);

        return stanbicAccountOpeningRequest;
    }


    private AccOpeningWema coverttoAccOpeningGTB(AccOpeningGeneral  accOpeningGeneral){
        AccOpeningWema accOpeningWema = new AccOpeningWema();
        accOpeningWema.setDateOfBirth(DateFormatterGt.gtPayloadDate(accOpeningGeneral.getDateOfBirth()));
        accOpeningWema.setGender(accOpeningGeneral.getGender());
        accOpeningWema.setAmount(accOpeningGeneral.getAmount());
        accOpeningWema.setTranId(accOpeningGeneral.getTranId());
        accOpeningWema.setTranDate(accOpeningGeneral.getTranDate());
        accOpeningWema.setTranReference(accOpeningGeneral.getTranReference());
        accOpeningWema.setAccountNumber(accOpeningGeneral.getAccountNumber());
        accOpeningWema.setAddress(accOpeningGeneral.getAddress());
        accOpeningWema.setBranchCode(accOpeningGeneral.getBranchCode());
        accOpeningWema.setTranType(accOpeningGeneral.getTranType());
        accOpeningWema.setFirstName(accOpeningGeneral.getFirstName());
        accOpeningWema.setGender(accOpeningGeneral.getGender());
        accOpeningWema.setLastName(accOpeningGeneral.getLastName());
        accOpeningWema.setMiddleName(accOpeningGeneral.getMiddleName());
        accOpeningWema.setPhoneNo(accOpeningGeneral.getPhoneNo());
        accOpeningWema.setPhoto(accOpeningGeneral.getPhoto());
        accOpeningWema.setSignature(accOpeningGeneral.getSignature());
//        accOpeningWema.setSecuritySessionKey(accOpeningGeneral.getSecuritySessionKey());
        accOpeningWema.setSubAgentId(accOpeningGeneral.getSubAgentId());
        accOpeningWema.setSuperAgentId(accOpeningGeneral.getSuperAgentId());

        return accOpeningWema;
    }

    public AccOpeningGeneral openAccountGeneral(AccOpeningGeneral accOpeningGeneral) {
        AccOpeningGeneral accOpeningGenResponse = new AccOpeningGeneral();
        try {
            String cbnCode = accOpeningGeneral.getBankCode();
            System.out.println("issue : "+cbnCode);
            if(cbnCode.trim().equals("221")){
                cbnCode = "039";
            }

            System.out.println("now : "+cbnCode);
            BankDetails bankDetails = bankDetailsRepository.findByCbnCode(cbnCode);
            String bank = bankDetails.getBankCode();
            switch (bank) {
                case "WEMA":
                    //This case maps the General Account Opening to that of Wema and Opens a Wema bank Account
                    AccOpeningWema accOpeningWema = getWemaAccOpenDetails(accOpeningGeneral);
                    AccOpeningWema accOpeningWemaResponse = wemaApiService.openAccount(accOpeningWema);
                    logger.info("WEMA Account Open Response {} ", accOpeningWemaResponse);

                    accOpeningGenResponse.setResponseCode(accOpeningWemaResponse.getResponseCode());
                    accOpeningGenResponse.setResponseDesc(accOpeningWemaResponse.getResponseDesc());
                    accOpeningGenResponse.setResponseDescription(accOpeningWemaResponse.getResponseDesc());
                    accOpeningGenResponse.setTranReference(accOpeningWemaResponse.getTranReference());
                    accOpeningGenResponse.setAccountNumber(accOpeningWemaResponse.getAccountNumber());

//                    modelMapper.map(accOpeningWemaResponse, accOpeningGenResponse);
                    break;
                case "GTB":
                    //This case maps the General Account Opening to that of GTB and Opens a GTB bank Account
                    AccOpeningGTB accOpeningGTB = getGTBAccOpenDetails(accOpeningGeneral);
                    accOpeningGTB.setDoB(accOpeningGeneral.getDateOfBirth());
                    AccOpeningGTB accOpeningGTBResponse = gtBankService.openGTBAccount(accOpeningGTB);
                    logger.info("GTB Account Open Response {} ", accOpeningGTBResponse);
                    accOpeningGenResponse.setAccountNumber(accOpeningGTBResponse.getNuban());
                    modelMapper.map(accOpeningGTBResponse, accOpeningGenResponse);
                    break;
                case "FBP":
                    AcctCreationResponse acctCreationResponse = fidelityWebServices.openAccount(accOpeningGeneral);
                    logger.info("Fidelity Account Open Response {} ", acctCreationResponse);

                    if(acctCreationResponse!=null){
                        if(acctCreationResponse.getRetVal().equals("0")){
                            accOpeningGenResponse.setResponseCode("00");
                            accOpeningGenResponse.setAccountNumber(acctCreationResponse.getAccountNumber());
                        }else{
                            accOpeningGenResponse.setResponseCode("96");
                            if(acctCreationResponse.getErrorMessages().size() > 0){
                                accOpeningGenResponse.setResponseDescription
                                        (acctCreationResponse.getErrorMessages().get(0).getErrorMessage());
                            }

                        }
                    }else{
                        accOpeningGenResponse.setResponseCode("96");
                        accOpeningGenResponse.setResponseDescription("Error occured opening account");
                    }
                    break;

                case "STANBIC":
                    //This case maps the General Account Opening to that of Stanbic and Opens a Stanbic bank Account
                    StanbicAccountOpeningRequest stanbicAccountOpeningRequest = getStanbicAccountOpeningRequest(accOpeningGeneral);
                    logger.info("Stanbic Account Open Request {} ", stanbicAccountOpeningRequest);
                    StanbicAccountOpeningResponse stanbicAccountOpeningResponse = stanbicIBTCServices.openAccount(stanbicAccountOpeningRequest);
                    logger.info("Stanbic Account Open Response {} ", stanbicAccountOpeningResponse);
                    modelMapper.map(stanbicAccountOpeningResponse, accOpeningGenResponse);
                    accOpeningGenResponse.setResponseCode(stanbicAccountOpeningResponse.getApiResponse().getResponseCode());
                    accOpeningGenResponse.setPhoneNumber(stanbicAccountOpeningRequest.getPhoneNumber());
                    accOpeningGenResponse.setPhoneNo(stanbicAccountOpeningRequest.getPhoneNumber());
                    if (stanbicAccountOpeningResponse.getResponseItem() != null) {
                        StanbicResponseItem stanbicResponseItem = modelMapper.map(stanbicAccountOpeningResponse.getResponseItem(), StanbicResponseItem.class);
                        accOpeningGenResponse.setAccountNumber(stanbicResponseItem.getAccountNumber());
                    }
                    break;

                default:
                    accOpeningGenResponse.setResponseDesc("Please Enter A Valid Bank Code");
                    break;
            }
            // log successfull account opening here
            if(accOpeningGenResponse.getResponseCode() != null && !accOpeningGenResponse.getResponseCode().isEmpty()) {
                if (accOpeningGenResponse.getResponseCode().equals("00")) {
                    AccountOpening aco = new AccountOpening();
                    aco.setAgentName(accOpeningGeneral.getAgentName());
                    aco.setFullName(accOpeningGeneral.getFirstName() + " " + accOpeningGeneral.getLastName());
                    aco.setAccountNumber(accOpeningGenResponse.getAccountNumber());
                    aco.setOpeningBank(bankDetails.getBankName());
                    aco.setEmail(accOpeningGeneral.getEmail());
                    aco.setMotherMaidenName(accOpeningGeneral.getMotherMaiden());
                    aco.setPhoneNumber(accOpeningGeneral.getPhoneNo());
                    if(accOpeningGeneral.getAmount()!=null && !accOpeningGeneral.getAmount().equals("")){
                        aco.setDepositAmount(Double.valueOf(accOpeningGeneral.getAmount()));
                    }else{
                        aco.setDepositAmount(0.00);
                    }
                    aco.setAddress(accOpeningGeneral.getAddress());
                    aco.setGender(accOpeningGeneral.getGender());
                    aco.setDateOfBirth(accOpeningGeneral.getGender());
                    openingRepository.save(aco);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("General Account Opening Error {} ", e.getCause().getMessage());
        }


        return accOpeningGenResponse;
    }

    @Override
    public SanefAccountOpeningResponse openAccountSanef(SanefAccountRequest accountRequest){
       return sanefService.sanefAccountOpening(accountRequest);
    }


    @Override
    public List<AgentToCommission> getCountPendingCommissionPerAgent() {
//        List<Agents> allUsers = applicationUsersRepository.findByGravityRole(roleRepository.findByName("AGENT"));
//        List<AgentToCommission> result = new ArrayList<>();
//
//        SettingDTO settingDTO = settingService.getSettingByCode("ACCOUNT_OPENING_COMMISSION") ;
//
//        allUsers.forEach( u -> {
//            List<AccountOpening> pending = openingRepository.findByAgentNameAndCommissionPaid(u.getUsername() ,"N");
//            if(pending.size() > 0){
//                logger.info("agent {} , has {} [ending commissions");
//                AgentToCommission ag = new AgentToCommission() ;
//                ag.setAgentName(u.getUsername());
//                ag.setNoOfAccounts(String.valueOf(pending.size()));
//                ag.setId(u.getId());
//                ag.setCommission(String.valueOf(Double.parseDouble(settingDTO.getValue()) * Double.parseDouble(String.valueOf(pending.size()))));
//                result.add(ag) ;
//            }
//        });
//
//        logger.info("total pending account opening commissions are {}" , result.size());
//
//        return result;

        return null ;
    }

    @Override
    public void processCommissionForAccountOpening(Long  agentId ) {
        Agents agent = agentsRepository.getOne(agentId) ;
        SettingDTO settingDTO = settingService.getSettingByCode("ACCOUNT_OPENING_COMMISSION") ;
        List<AccountOpening> pending = openingRepository.findByAgentNameAndCommissionPaid(agent.getUsername() ,"N");
        double com = Double.parseDouble(settingDTO.getValue()) * Double.parseDouble(String.valueOf(pending.size()));
        logger.info("agent {} is getting {}", agent.getUsername()  , com);

        walletService.creditWallet(null,agent.getIncomeWalletNumber() , com  , "CHANNEL WEB PORTAL" ,"ACCOUNT OPENING COMMISSION");

    }

    @Override
    public List<AccountOpening> getAllCustomers(){

        //loop through using forr each loop
        List<AccountOpening> all = new ArrayList<>();
        openingRepository.findAll().forEach(e -> all.add(e));

        return all;
    }
}
