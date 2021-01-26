package com._3line.gravity.api.commission;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.bankdetails.dtos.FreedomCommisionDTO;
import com._3line.gravity.freedom.bankdetails.model.TransactionType;
import com._3line.gravity.freedom.commisioncharge.service.CommissionChargeService;
import com._3line.gravity.freedom.gravitymobile.service.MobileService;
import com._3line.gravity.freedom.institution.dto.InstitutionDTO;
import com._3line.gravity.freedom.institution.service.InstitutionService;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/gravity/api/commission/")
public class CommissionAPI {

    @Autowired
    WalletService walletService;

    @Autowired
    AgentService agentService;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    InstitutionService institutionService;


    @Autowired
    CommissionChargeService commissionChargeService;

    private Logger logger = LoggerFactory.getLogger(CommissionAPI.class);



    @RequestMapping(value = "getCommissionFee", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> getCommissionFee(@RequestBody FreedomCommisionDTO request) {

        Response response = new Response();
        try {

            Double amount = Double.valueOf(request.getAmount());
            TransactionType transactionType = TransactionType.valueOf(request.getTransactionType()) ;
            logger.info("about to get fee for amount {}", amount);

            Agents agent = jwtUtility.getCurrentAgent();
            Double fee;

            InstitutionDTO institutionDTO = institutionService.getInstitutionByAgentId(agent.getParentAgentId());
            if(institutionDTO!=null){
                fee = commissionChargeService.getInstCommissionForAmount(amount , transactionType,institutionDTO.getName()) ;
            }else{
                fee = commissionChargeService.getCommissionForAmount(amount , transactionType) ;
            }


            response.setRespDescription("Tran Fee");
            response.setRespCode("00");
            Map res = new HashMap();
            res.put("fee", fee);
            response.setRespBody(res);

        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespDescription("error occured");
            response.setRespBody(e.getMessage());

        }

        logger.info("response going out");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }





}
