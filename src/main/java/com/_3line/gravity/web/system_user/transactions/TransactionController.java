package com._3line.gravity.web.system_user.transactions;


import com._3line.gravity.core.utils.DateUtil;
import com._3line.gravity.freedom.billpayment.models.BillPayment;
import com._3line.gravity.freedom.financialInstitutions.accountopening.model.AccountOpening;
import com._3line.gravity.freedom.itexintegration.model.PtspModel;
import com._3line.gravity.freedom.itexintegration.repository.PtspRepository;
import com._3line.gravity.freedom.transactions.dtos.TransactionsDto;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.service.TransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Controller
@RequestMapping("/core/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;
    @Autowired
    ObjectMapper mapper ;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PtspRepository ptspRepository;


    @ModelAttribute
    public void models(Model model) {

        model.addAttribute("yesterday", DateUtil.formatDateToreadable_(DateUtil.yesterday())) ;
        model.addAttribute("today",DateUtil.formatDateToreadable_(DateUtil.tomorrow())) ;

    }

    @PreAuthorize("hasAuthority('VIEW_TRANSACTIONS')")
    @GetMapping("/")
    public String index(){
        return "transaction/view";
    }

    @PreAuthorize("hasAuthority('VIEW_TRANSACTIONS')")
    @GetMapping("/withdrawal/")
    public String withdrawal(){
        return "transaction/withdrawal";
    }

    @PreAuthorize("hasAuthority('VIEW_TRANSACTIONS')")
    @GetMapping("/deposit/")
    public String deposit(){
        return "transaction/deposit";
    }

    @PreAuthorize("hasAuthority('VIEW_TRANSACTIONS')")
    @GetMapping("/transfer/")
    public String transfer(){
        return "transaction/transfer";
    }

    @PreAuthorize("hasAuthority('VIEW_TRANSACTIONS')")
    @GetMapping("/accounts/")
    public String accountOpening(){
        return "transaction/account_opening";
    }

    @PreAuthorize("hasAuthority('VIEW_TRANSACTIONS')")
    @GetMapping("/billpayment/")
    public String billPayment(){
        return "transaction/billpayment";
    }

    @PreAuthorize("hasAuthority('VIEW_TRANSACTIONS')")
    @GetMapping("/recharge/")
    public String airtime(){
        return "transaction/recharge";
    }

    @PreAuthorize("hasAuthority('VIEW_TRANSACTIONS')")
    @GetMapping("/{tranId}")
    public String getDetails(@PathVariable Long tranId , Model model) {
      Transactions transactionLog = transactionService.getTransaction(tranId);
        TransactionsDto dto = null;
        Map<String , String> stringMap = new HashMap<>() ;
        if(transactionLog != null){
            dto = modelMapper.map(transactionLog, TransactionsDto.class);
            JsonNode node = mapper.valueToTree(dto) ;
            Iterator itr = node.fieldNames();

            while (itr.hasNext()){
                String field =  itr.next().toString() ;
                String formate = StringUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(field), ' '));
                stringMap.put(formate , node.findValue(field).asText().replace("null","") ) ;
            }

        }

        stringMap.remove("id") ;
        stringMap.remove("version") ;
        stringMap.remove("delFlag") ;
        stringMap.remove("deletedOn") ;
        stringMap.remove("handler") ;
        stringMap.remove("hibernateLazyInitializer") ;

        model.addAttribute("sets" , stringMap) ;
        if(dto.getTransactionReference()==null || dto.getTransactionReference().equals("") ){
            dto.setTransactionReference(dto.getItexTranId());
        }
        model.addAttribute("transactionDetail" , dto) ;

        return "transaction/details" ;
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    DataTablesOutput<Transactions> getAllCodes(DataTablesInput dataTablesInput, @RequestParam("csearch") String agentName,@RequestParam("status") String status , @RequestParam("from") String from , @RequestParam("to") String to) {

        Pageable pageable = new SpecificationBuilder<>(dataTablesInput).createPageable();
        Page<Transactions> codes = transactionService.getTransactions( agentName, status , from , to ,pageable);
        DataTablesOutput<Transactions> out = new DataTablesOutput<>();
        out.setDraw(dataTablesInput.getDraw());
        out.setData(codes.getContent());
        out.setRecordsFiltered(codes.getTotalElements());
        out.setRecordsTotal(codes.getTotalElements());
        return out;
    }

    @GetMapping(path = "/accounts/all")
    public @ResponseBody
    DataTablesOutput<AccountOpening> getAllBillPayments(DataTablesInput dataTablesInput, @RequestParam("agentName") String agentName, @RequestParam("accountNumber") String accountNumber, @RequestParam("phone") String phone, @RequestParam("email") String email , @RequestParam("from") String from , @RequestParam("to") String to) {

        Pageable pageable = new SpecificationBuilder<>(dataTablesInput).createPageable();

        Page<AccountOpening> codes = transactionService.getAccountOpening( agentName ,accountNumber,phone,email , from , to ,pageable);

        DataTablesOutput<AccountOpening> out = new DataTablesOutput<>();
        out.setDraw(dataTablesInput.getDraw());
        out.setData(codes.getContent());
        out.setRecordsFiltered(codes.getTotalElements());
        out.setRecordsTotal(codes.getTotalElements());
        return out;
    }

    @GetMapping(path = "/withdrawal/data")
    public @ResponseBody
    DataTablesOutput<Transactions> getAllWithdrawals(DataTablesInput input, @RequestParam("agentName") String agentName,@RequestParam("terminalId") String terminalId ,@RequestParam("pan") String pan,@RequestParam("status") String status, @RequestParam("from") String from , @RequestParam("to") String to) {

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<Transactions> codes = transactionService.getWithdrawals(agentName,terminalId, pan , status, from , to ,pageable);
        DataTablesOutput<Transactions> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(codes.getContent());
        out.setRecordsFiltered(codes.getTotalElements());
        out.setRecordsTotal(codes.getTotalElements());
        return out;
    }

    @GetMapping(path = "/deposit/data")
    public @ResponseBody
    DataTablesOutput<Transactions> getAllDeposits(DataTablesInput input, @RequestParam("agentName") String agentName ,@RequestParam("accountNumber") String accountNumber,@RequestParam("status") String status , @RequestParam("from") String from , @RequestParam("to") String to) {

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<Transactions> codes = transactionService.getDeposits(agentName, accountNumber ,status, from , to , pageable);
        DataTablesOutput<Transactions> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(codes.getContent());
        out.setRecordsFiltered(codes.getTotalElements());
        out.setRecordsTotal(codes.getTotalElements());
        return out;
    }

    @GetMapping(path = "/transfer/data")
    public @ResponseBody
    DataTablesOutput<Transactions> getAllTransfers(DataTablesInput input, @RequestParam("csearch") String search,@RequestParam("status") String status , @RequestParam("from") String from , @RequestParam("to") String to) {

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<Transactions> codes = transactionService.getTransfers(search ,status, from , to , pageable);
        DataTablesOutput<Transactions> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(codes.getContent());
        out.setRecordsFiltered(codes.getTotalElements());
        out.setRecordsTotal(codes.getTotalElements());
        return out;
    }


    @GetMapping(path = "/billpayment/data")
    public @ResponseBody
    DataTablesOutput<BillPayment> getAllBillPayment(DataTablesInput input, @RequestParam("customerId") String customerId , @RequestParam("agentName") String agentName ,@RequestParam("from") String from , @RequestParam("to") String to) {

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<BillPayment> codes = transactionService.getBillPayment(agentName , customerId , from , to , pageable);
        DataTablesOutput<BillPayment> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(codes.getContent());
        out.setRecordsFiltered(codes.getTotalElements());
        out.setRecordsTotal(codes.getTotalElements());
        return out;
    }

    @GetMapping(path = "/airtime/data")
    public @ResponseBody
    DataTablesOutput<BillPayment> getAllAirtime(DataTablesInput input, @RequestParam("customerId") String customerId , @RequestParam("agentName") String agentName ,@RequestParam("from") String from , @RequestParam("to") String to) {

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<BillPayment> codes = transactionService.getAirtime(agentName , customerId , from , to , pageable);
        DataTablesOutput<BillPayment> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(codes.getContent());
        out.setRecordsFiltered(codes.getTotalElements());
        out.setRecordsTotal(codes.getTotalElements());
        return out;
    }




}
