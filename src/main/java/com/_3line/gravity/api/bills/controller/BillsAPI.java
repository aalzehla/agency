package com._3line.gravity.api.bills.controller;

import com._3line.gravity.api.bills.dto.PayBills;
import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.code.dto.CodeDTO;
import com._3line.gravity.core.code.service.CodeService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.billpayment.dtos.ValidateCustomerRequest;
import com._3line.gravity.freedom.gravitymobile.service.BillerService;
import com._3line.gravity.freedom.gravitymobile.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/gravity/api/bills")
public class BillsAPI {

    @Autowired
    MobileService service;

    @Autowired
    BillerService billerService;

    @Autowired
    AgentService agentService;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    CodeService codeService;



    @RequestMapping(value = "/category", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> billerCategory(HttpServletRequest request) {
        Response response;
        response =  billerService.getCategories() ;
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value = "/updateBillService", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> updateBillService(HttpServletRequest request) {
        Response response;
        response =  billerService.updateBillServices() ;
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    @RequestMapping(value = "/category/{categoryId}/service", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> billerService(HttpServletRequest request,@PathVariable long categoryId) {
        Response response;
        response = billerService.getServiceForCategory(categoryId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/category/service/{serviceId}/options", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> billerOption(HttpServletRequest request,@PathVariable long serviceId) {
        Response response;
        response = billerService.getOptionsForService(serviceId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> billerPayBills(HttpServletRequest request,@RequestBody PayBills data) {

        Response response;
        Agents agent = jwtUtility.getCurrentAgent();

        List<CodeDTO> codeDTOS = codeService.getCodesByType("EXEMPTED_USERS");

        for (CodeDTO s : codeDTOS) {
            if(s.getCode().equalsIgnoreCase(agent.getUsername()) ){
                throw new GravityException("Error occurred Processing Transaction");
            }
        }

        if(jwtUtility.isValidAgentPin(data.getPin(),agent)){
            data.setAgentName(agent.getUsername());
            response = billerService.payBills(data);
        }else{
            response = new Response("119","Invalid Pin entered",null);
        }

        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> validateBillerCustomer(HttpServletRequest request,@RequestBody ValidateCustomerRequest data) {

        Response response;
        Agents agent = jwtUtility.getCurrentAgent();
        response = billerService.validate(data,agent);
        return new ResponseEntity<>(response,HttpStatus.OK);

    }



    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }



}
