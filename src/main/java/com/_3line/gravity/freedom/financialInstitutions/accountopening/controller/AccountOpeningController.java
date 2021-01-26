package com._3line.gravity.freedom.financialInstitutions.accountopening.controller;


import com._3line.gravity.freedom.financialInstitutions.accountopening.dtos.AgentToCommission;
import com._3line.gravity.freedom.financialInstitutions.accountopening.model.AccountOpening;
import com._3line.gravity.freedom.financialInstitutions.accountopening.repository.AccountOpeningRepository;
import com._3line.gravity.freedom.financialInstitutions.accountopening.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accountopening/")
public class AccountOpeningController {


    @Autowired
    AccountService accountService ;

    private AccountOpeningRepository accountOpeningRepository;


    @RequestMapping(method = RequestMethod.GET , value = "/data")
    public DataTablesOutput<AgentToCommission> pendingCommission (DataTablesInput input){
        try {

            Pageable pageable = new SpecificationBuilder<>(input).createPageable();
            List<AgentToCommission> data = accountService.getCountPendingCommissionPerAgent() ;
            DataTablesOutput<AgentToCommission> out = new DataTablesOutput<AgentToCommission>();
            out.setDraw(input.getDraw());
            out.setData(data);
            out.setRecordsFiltered(data.size());
            out.setRecordsTotal(data.size());
            return out;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null ;
    }

    @RequestMapping(method = RequestMethod.GET , value = "/pay/{agentId}")
    public ResponseEntity payCommission(@PathVariable Long agentId){
        try {
            accountService.processCommissionForAccountOpening(agentId);
            return ResponseEntity.ok("AgentDto credited successfully");
        }catch (Exception e){
            e.printStackTrace();
        }

        return null ;
    }


    //get the list of all account opening
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public ResponseEntity<List<AccountOpening>> getCustomers(){

        List<AccountOpening> all = accountService.getAllCustomers();

        return new ResponseEntity<>(all, HttpStatus.OK);
    }

}
