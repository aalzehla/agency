package com._3line.gravity.freedom.gravitymobile.service;


import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.agents.models.Agents;

import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.thirftmgt.dtos.ContributionDTO;
import com._3line.gravity.freedom.thirftmgt.dtos.ThriftDTO;
import com._3line.gravity.freedom.thirftmgt.services.ThriftService;
import com._3line.gravity.freedom.wallet.service.WalletService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileThriftService {

    @Autowired
    ThriftService thriftService;
    @Autowired
    WalletService walletService ;
    @Autowired
    AgentsRepository agentsRepository ;

    private Gson gson = new Gson() ;


    public Response registerForThrift(ThriftDTO thriftDTO){

        Response response = new Response() ;
        try {
            String r = thriftService.register(thriftDTO);
            response.setRespCode("00");
            response.setRespDescription(r);
        }catch (Exception e){
            e.printStackTrace();
            response.setRespCode("202");
            response.setRespDescription(e.getMessage());
        }

        return  response ;
    }

    public Response contribute(ContributionDTO contributionDTO){
        Response response = new Response() ;
        try {
            Agents agent = agentsRepository.findByUsername(contributionDTO.getAgentName());

            walletService.debitWallet(null,agent.getWalletNumber() , contributionDTO.getAmount() , "POS ", "THRIFT CONTRIBUTION {"+contributionDTO.getCardNumber()+"} DEBIT");
            String r = thriftService.contribute(contributionDTO);
            response.setRespCode("00");
            response.setRespDescription(r);
            }catch (Exception e){
            e.printStackTrace();
            response.setRespCode("202");
            response.setRespDescription(e.getMessage());
        }

        return  response ;
    }

    public Response getThriftDetails(String cardNumber){
        Response response = new Response() ;
        try {
            ThriftDTO r = thriftService.checkContribution(cardNumber);
            response.setRespCode("00");
            response.setRespDescription("success");
            response.setRespBody(r);
        }catch (Exception e){
            e.printStackTrace();
            response.setRespCode("202");
            response.setRespDescription(e.getMessage());
        }

        return  response ;
    }

    public Response preMatureLiquidation(String cardNumber){
        Response response = new Response() ;
        try {
//            String r = thriftService.prematureLiquidation(cardNumber);
            String r = thriftService.prematureLiquidation(null);
            response.setRespCode("00");
            response.setRespDescription("success");
            response.setRespBody(r);
        }catch (Exception e){
            e.printStackTrace();
            response.setRespCode("202");
            response.setRespDescription(e.getMessage());
        }

        return  response ;
    }


}
