package com._3line.gravity.freedom.commisions.controller;

import com._3line.gravity.freedom.commisions.dto.AgentTotalCommissionDTO;
import com._3line.gravity.freedom.commisions.dto.GravityDailyCommissionDTO;
import com._3line.gravity.freedom.commisions.models.AgentTotalCommission;
import com._3line.gravity.freedom.commisions.models.BankTotalCommission;
import com._3line.gravity.freedom.commisions.models.GravityDailyCommission;
import com._3line.gravity.freedom.commisions.models._3lineTotalCommission;
import com._3line.gravity.freedom.commisions.repositories.GravityDailyCommssionRepo;
import com._3line.gravity.freedom.commisions.services.GravityDailyCommissionService;
import com._3line.gravity.freedom.utility.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by FortunatusE on 8/6/2018.
 */

@RestController
@RequestMapping(value = "/application/commission")
public class CommissionController {


    private final GravityDailyCommissionService gravityDailyCommissionService;

    private GravityDailyCommssionRepo dailyCommssionRepo ;
    private static final Logger logger = LoggerFactory.getLogger(CommissionController.class);


    @Autowired
    public CommissionController(@Qualifier("gravityDailyCommissionServiceImpl") GravityDailyCommissionService gravityDailyCommissionService , GravityDailyCommssionRepo dailyCommssionRepo) {
        this.gravityDailyCommissionService = gravityDailyCommissionService;
        this.dailyCommssionRepo = dailyCommssionRepo ;
    }


    @RequestMapping(value = "/all" , method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<GravityDailyCommission> getDatatablesTransactions(@RequestParam MultiValueMap<String, Object> param) {
        System.out.println(param);
        Object fromdate = param.get("fromdate"); //this is returned as list in MultiValueMap
        Object todate = param.get("todate"); //this is returned as list in MultiValueMap
        Object agentName = param.get("agentName"); //this is returned as list in MultiValueMap
        Date from = DateUtil.yesterday() ;
        Date to  = DateUtil.tomorrow();
        String agent = "";
        try {
            from = DateUtil.dateViewFormat(((List) fromdate).get(0).toString()) ;
            to = DateUtil.dateViewFormat(((List) todate).get(0).toString());
        }catch (Exception e){

        }

        try {
            agent =((List) agentName).get(0).toString();
        }catch (Exception e){

        }

        List<GravityDailyCommission> dailyCommissions = dailyCommssionRepo.findByAgentNameEqualsAndTranDateBetween(agent , from ,to) ;

        DataTablesOutput<GravityDailyCommission> out = new DataTablesOutput<GravityDailyCommission>();
        out.setDraw(0);
        out.setData(dailyCommissions);
        out.setRecordsFiltered(dailyCommissions.size());
        out.setRecordsTotal(dailyCommissions.size());
        return out;
    }


    @RequestMapping(value = "/commissions/agent", method = RequestMethod.GET)
    public ResponseEntity retrieveAgentsTotalCommission (){

        logger.debug("Retrieving agents total commission");

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        try {
            List<AgentTotalCommission> agentsTodaysTotalCommissions = gravityDailyCommissionService.extractAgentsTodaysTotalCommissions();

            List<AgentTotalCommissionDTO> commissionDTOs = new ArrayList<>();

            agentsTodaysTotalCommissions.stream().forEach(agentTotalCommission -> {
                AgentTotalCommissionDTO commissionDTO = new AgentTotalCommissionDTO();
                commissionDTO.setId(agentTotalCommission.getId());
                commissionDTO.setAccountNumber(agentTotalCommission.getAccountNumber());
                commissionDTO.setAgentName(agentTotalCommission.getAgentName());
                commissionDTO.setBank(agentTotalCommission.getBank());
                commissionDTO.setTotalAmount(agentTotalCommission.getTotalAmount());
                commissionDTO.setTranDate(dateFormat.format(agentTotalCommission.getTranDate()));
                commissionDTOs.add(commissionDTO);
            });

            logger.debug("Retrieved commissions: {}", commissionDTOs);
            return ResponseEntity.ok(commissionDTOs);
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(value = "/commissions/bank", method = RequestMethod.GET)
    public ResponseEntity getBanksTotalCommission (){

        List<BankTotalCommission> banksTodaysTotalCommissions = gravityDailyCommissionService.extractBanksTodaysTotalCommissions();
        return  ResponseEntity.ok(banksTodaysTotalCommissions);
    }

    @RequestMapping(value = "/commissions/3line", method = RequestMethod.GET)
    public ResponseEntity get3lineTotalCommission (){

        _3lineTotalCommission _3lineTodaysTotalCommission = gravityDailyCommissionService.extract3lineTodaysTotalCommission();
        return  ResponseEntity.ok(_3lineTodaysTotalCommission);
    }


    @RequestMapping(value = "/commissions", method = RequestMethod.GET)
    public DataTablesOutput<GravityDailyCommissionDTO> getAllCommissions(DataTablesInput input) {

        logger.debug("Getting all daily commissions");

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<GravityDailyCommissionDTO> commissionDTOs = gravityDailyCommissionService.getDailyCommissions(pageable);
        DataTablesOutput<GravityDailyCommissionDTO> out = new DataTablesOutput<GravityDailyCommissionDTO>();
        out.setDraw(input.getDraw());
        out.setData(commissionDTOs.getContent());
        out.setRecordsFiltered(commissionDTOs.getTotalElements());
        out.setRecordsTotal(commissionDTOs.getTotalElements());
        return out;
    }
}
