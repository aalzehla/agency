package com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.service.implementation;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.api.wallet.dto.FundWalletDTO;
import com._3line.gravity.core.cryptography.SecretKeyCrypt;
import com._3line.gravity.core.cryptography.rsa.RSACipher;
import com._3line.gravity.core.cryptography.rsa.RSACrypt;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.setting.dto.SettingDTO;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.utils.HttpCustomClient;
import com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.dto.DebitCardDTO;
import com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.dto.sva.SVAResponse;
import com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.dto.sva.SendTransactionReqDTO;
import com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.dto.sva.SendTransactionRespDTO;
import com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.model.DebitCard;
import com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.service.DebitCardService;
import com._3line.gravity.freedom.NIBBS.dto.AgentDataReportDTO;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.FEPIntegration.CardWithdrawal.repository.DebitCardRepository;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.repository.BankDetailsRepository;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.utility.Utility;
import com._3line.gravity.freedom.wallet.service.WalletService;
import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class DebitCardServiceImpl implements DebitCardService {

    @Autowired
    ModelMapper  modelMapper;

    @Autowired
    DebitCardRepository debitCardRepository;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    RSACipher rsaCipher;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    SettingService settingService;

    @Autowired
    WalletService walletService;

    @Autowired
    BankDetailsRepository bankDetailsRepository;

    @Value("${sva.service.bas_url}")
    private String svaBaseUrl;

    @Value("${sva.service.sendtransaction}")
    private String sendTransactionPath;

    @Value("${sva.service.mode}")
    private String mode;

    Logger logger = LoggerFactory.getLogger(this.getClass());



    @Override
    public DebitCardDTO addCard(DebitCardDTO debitCardDTO) {

        Agents agent = jwtUtility.getCurrentAgent();

        String token = Utility.generateToken();

        DebitCard debitCard = modelMapper.map(debitCardDTO,DebitCard.class);
        debitCard.setMeta(debitCardDTO.getPan().substring((debitCardDTO.getPan().length()-4)));

        debitCard.setPan(debitCard.getPan().trim());
        debitCard.setBank(debitCardDTO.getBank());
        debitCard.setCardType(debitCard.getCardType().trim());

        DebitCard debitCard1 = debitCardRepository.findByPan(debitCard.getPan());
        System.out.println(debitCard1);
        System.out.println(agent.getUsername());
        if(debitCard1!=null && debitCard1.getAgent().getUsername().equals(agent.getUsername())){
            throw new GravityException("Card Already Added");
        }
        debitCard.setToken(token);
        debitCard.setAgent(agent);

        debitCardRepository.save(debitCard);

        debitCardDTO.setToken(token);
        debitCardDTO.setMeta(debitCard.getMeta());

        return debitCardDTO;

    }

    @Override
    public void deleteCard(String token) {
        DebitCard debitCard = debitCardRepository.findByToken(token);
        if(debitCard==null){
            throw new GravityException("Invalid Token Provided");
        }
        debitCard.setDelFlag("Y");
        debitCard.setDeletedOn(new Date());
        debitCardRepository.save(debitCard);

    }


    @Override
    public DebitCardDTO getchCardByToken(String token) {

        DebitCardDTO debitCardDTO = null;
        DebitCard debitCard = debitCardRepository.findByToken(token);
        if(debitCard!=null){
            debitCardDTO =  converttoDTO(debitCard);
        }

        return debitCardDTO;
    }

    @Override
    public String debitCard(FundWalletDTO fundWalletDTO) {

        Agents agent = jwtUtility.getCurrentAgent();

        DebitCardDTO debitCard = this.getchCardByToken(fundWalletDTO.getToken());
        if(debitCard!=null){

            Transactions transactions = new Transactions();
            transactions.setTranDate(new Date());
            transactions.setStatus((short) 0);
            transactions.setStatusdescription("PENDING");
            transactions.setInnitiatorId(agent.getId());
            transactions.setApproval(0);
            transactions.setAmount(fundWalletDTO.getAmount());
            transactions.setStan(null);
            transactions.setMaskedPan(debitCard.getPan());
            transactions.setTerminalId(agent.getTerminalId());
            transactions.setTransactionType("Wallet Funding");
            transactions.setTransactionTypeDescription("Wallet Funding (Card)");
            transactions.setDescription("Wallet Funding (Card)");

            transactions.setBankTo(agent.getBankCode());
            transactions.setAgentName(agent.getUsername());
            transactions.setMedia("POS TERMINAL");
            transactions = transactionRepository.save(transactions);


            //call sva service
            SendTransactionReqDTO sendTransactionReqDTO = new SendTransactionReqDTO();

            SettingDTO  svaAccountNumberSetting = settingService.getSettingByCode("sva_account_number");
            SettingDTO  svaAccountTypeSetting = settingService.getSettingByCode("sva_account_type");

            BankDetails bankDetails = bankDetailsRepository.findByCbnCode(debitCard.getBank());

            if(bankDetails==null){
                transactions.setStatusdescription("FAILED");
                transactions.setStatus((short) 2);
                transactionRepository.save(transactions);
                throw new GravityException("Incomplete Card Details: Kindly contact Support");
            }


                String cardPan = debitCard.getPan();

                sendTransactionReqDTO.setAmount(fundWalletDTO.getAmount());

                sendTransactionReqDTO.setBankId(bankDetails.getCardBinCode());
                if(cardPan.length() > 6){
//                    System.out.println("setting :: "+cardPan.substring(0,6));
                    sendTransactionReqDTO.setCardBin(cardPan.substring(0,6));
                }
                sendTransactionReqDTO.setCustomerEmail(agent.getEmail());
                sendTransactionReqDTO.setCustomerId(agent.getEmail());
                sendTransactionReqDTO.setCustomerMobile(agent.getPhoneNumber());
                sendTransactionReqDTO.setCvv(fundWalletDTO.getCvv());
                sendTransactionReqDTO.setDestinationAccountNumber(svaAccountNumberSetting.getValue());
                sendTransactionReqDTO.setDestinationAccountType(svaAccountTypeSetting.getValue());
                sendTransactionReqDTO.setExpDate(debitCard.getExpDate()!=null?debitCard.getExpDate().replaceAll("/",""):"0000");
                sendTransactionReqDTO.setMsisdn(agent.getPhoneNumber());
                sendTransactionReqDTO.setPan(cardPan);
                sendTransactionReqDTO.setPaymentCode("70101");
                sendTransactionReqDTO.setPin(fundWalletDTO.getPin());
                sendTransactionReqDTO.setReciepientName(agent.getFirstName() + " "+agent.getLastName());
                sendTransactionReqDTO.setReciepientPhone(agent.getPhoneNumber());
                sendTransactionReqDTO.setTransactionRef("OFI|T|WEB|CA|DTN|AC|251119164229|NARGXAHA4Y");


            try{
                sendTransactionReqDTO.setPan(debitCard.getPan());
                logger.info("Set PAM as :: "+sendTransactionReqDTO.getPan());
                this.svaCall(sendTransactionReqDTO, String.valueOf(transactions.getTranId()));
            }catch(Exception e){
                e.printStackTrace();
                transactions.setStatusdescription("FAILED");
                transactions.setStatus((short) 2);
                transactionRepository.save(transactions);
                throw new GravityException(e.getMessage());
            }



            try{
                SettingDTO  sva_card_tran_charge = settingService.getSettingByCode("SVA_CARD_TRAN_CHARGE");
                Double fee = Double.valueOf(sva_card_tran_charge.getValue());

                walletService.creditWallet(String.valueOf(transactions.getTranId()),agent.getWalletNumber(),fundWalletDTO.getAmount()-fee,"Debit Card","Fund Wallet :"+agent.getWalletNumber());
                transactions.setStatusdescription("SUCCESSFUL");
                transactions.setStatus((short) 1);
                transactionRepository.save(transactions);
                return String.valueOf(transactions.getTranId());

            }catch(Exception e){
                e.printStackTrace();
                transactions.setStatusdescription("FAILED");
                transactions.setStatus((short) 2);
                transactionRepository.save(transactions);
            }

        }else{
            throw new GravityException("Invalid Wallet Token");
        }

        return null;
    }

    @Override
    public List<DebitCardDTO> fetchCards() {
        List<DebitCardDTO> debitCardDTOS = new ArrayList<>();

        Agents agent = jwtUtility.getCurrentAgent();

        List<DebitCard>  debitCards = debitCardRepository.findByAgent_Id(agent.getId());

        debitCardDTOS = debitCards.stream().map(d-> converttoDTO(d)).collect(Collectors.toList());

        return debitCardDTOS;
    }

    DebitCardDTO converttoDTO(DebitCard debitCard){

        DebitCardDTO  debitCardDTO = new DebitCardDTO();
        debitCardDTO.setToken(debitCard.getToken());
        debitCardDTO.setCardType(debitCard.getCardType());
        debitCardDTO.setExpDate(debitCard.getExpDate());
        debitCardDTO.setMeta(debitCard.getMeta());
        debitCardDTO.setBank(debitCard.getBank());
        debitCardDTO.setPan(debitCard.getPan());

        return debitCardDTO;
    }


    private void svaCall(SendTransactionReqDTO sendTransactionReqDTO,String tranId) throws GravityException{

        //log sva call as pending

        Gson g = new Gson();

        String requestBody = g.toJson(sendTransactionReqDTO);

        System.out.println("Plain request sent is :: "+requestBody);

        HashMap headerConents = new HashMap<>();
        headerConents.put("Content-Type","application/json");

        HttpCustomClient httpCustomClient = new HttpCustomClient(svaBaseUrl+"/"+sendTransactionPath,requestBody,"POST",true,headerConents);

        String resp = httpCustomClient.sendRequest();


        SVAResponse svaResponse = null;
        try{
            svaResponse =  g.fromJson(resp,SVAResponse.class);
        }catch (Exception e){}

        logger.info("Sva Response is :: "+tranId+" "+svaResponse);

        if(svaResponse!=null ){
            if(!svaResponse.getRespCode().equals("90000")){
                //log sva response as received
                if(svaResponse.getRespBody()!=null && svaResponse.getRespBody().getError()!=null && svaResponse.getRespBody().getError().getError()!=null){
                    throw new GravityException(svaResponse.getRespBody().getError().getError().getMessage());
                }else{
                    throw new GravityException(svaResponse.toString());
                }

            }
        }else{


            //log sva response as received
            throw new GravityException("Could not get response from SVA Service");
        }
        //log sva response as received
    }


}
