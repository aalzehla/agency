package com._3line.gravity.freedom.financialInstitutions.fidelity.controller;//package com._3line.app.financialInstitutions.fidelity.controller;
//
//import com._3line.app.financialInstitutions.fidelity.requests.ValidateAccountByNibssBvnRequest;
//import com._3line.app.financialInstitutions.fidelity.models.AccountSummary;
//import com._3line.app.financialInstitutions.fidelity.models.GetAccountResponse;
//import com._3line.app.financialInstitutions.fidelity.models.UserDetailsResponse;
//import com._3line.app.financialInstitutions.fidelity.models.ValidateAccountByNibssBvnResponse;
//import com._3line.app.financialInstitutions.fidelity.service.FidelityWebServices;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import org.springframework.beans.factory.annotation.Value;
//
//@Controller
//@RestController
//@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//public class AccountController {
//
//    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
//
//    @Value("${fidelityapi.agentid}")
//    private String agentid;
//
//    @Autowired
//    private FidelityWebServices fidelityWebServices;
//
//    @RequestMapping(value = "/customerbvn", method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseEntity<AccountSummary[]> getAccountByBVN(@RequestBody String bvn) {
//
//        GetAccountResponse getAccountResponse = fidelityWebServices.getAccountByBVN(bvn, agentid);
//
//        AccountSummary[] accountSummarys = new AccountSummary[]{};
//        accountSummarys = getAccountResponse.getAccountSummaryProfile();
//
//        return ResponseEntity.ok(accountSummarys);
//    }
//
//    @RequestMapping(value = "/customerNibssBvn", method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseEntity<ValidateAccountByNibssBvnResponse> getAccountByBVN(@RequestBody ValidateAccountByNibssBvnRequest nibssBvnRequest) {
//
//        ValidateAccountByNibssBvnResponse nibssBvnResponse = fidelityWebServices.validateAccountByNibssBvn(nibssBvnRequest, agentid);
//
//        return ResponseEntity.ok(nibssBvnResponse);
//    }
//
//    @RequestMapping(value = "/getAccountDetails", method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseEntity getAccountDetails(@RequestBody String accountNo) {
//
//        UserDetailsResponse userDetails = fidelityWebServices.getUserDetails(accountNo, agentid);
//
//        return ResponseEntity.ok(userDetails);
//    }
//
//}
