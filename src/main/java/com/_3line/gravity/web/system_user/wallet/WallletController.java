package com._3line.gravity.web.system_user.wallet;


import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.utils.AppUtility;
import com._3line.gravity.core.utils.DateUtil;
import com._3line.gravity.core.verification.exceptions.VerificationException;

import com._3line.gravity.freedom.dispute.dtos.DisputeDto;
import com._3line.gravity.freedom.dispute.dtos.DisputeLogDTO;
import com._3line.gravity.freedom.dispute.models.Dispute;
import com._3line.gravity.freedom.dispute.models.DisputeLog;
import com._3line.gravity.freedom.dispute.service.DisputeLogService;
import com._3line.gravity.freedom.transactions.service.TransactionService;
import com._3line.gravity.freedom.wallet.dto.*;
import com._3line.gravity.freedom.wallet.models.*;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/core/wallet")
public class WallletController {


    @Autowired
    private WalletService walletService ;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TransactionService transactionService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ModelMapper  modelMapper;

    @Autowired
    DisputeLogService disputeLogService;

    @GetMapping("/")
    @PreAuthorize("hasAuthority('VIEW_WALLETS')")
    public String search(){
        return "wallet/search";
    }


    @GetMapping("/transactions")
    public String transactions(){
        return "wallet/transactions";
    }

    @GetMapping("/gl")
    public String glaccount(){
        return "wallet/glaccounts";
    }

    @GetMapping("/creditrequest")
    @PreAuthorize("hasAuthority('MANAGE_WALLETS_CREDIT_REQUEST')")
    public String creditRequest(){
        return "wallet/creditrequest";
    }

    @GetMapping("/creditrequesthistory")
    @PreAuthorize("hasAuthority('MANAGE_WALLETS_CREDIT_REQUEST')")
    public String creditRequestHistory(){
        return "wallet/credit_request_history";
    }

    @GetMapping("/dispute")
    @PreAuthorize("hasAuthority('MANAGE_WALLET_DISPUTE')")
    public String raisedispute(){
        return "wallet/dispute";
    }

    @GetMapping("/dispute/create")
    @PreAuthorize("hasAuthority('MANAGE_WALLET_DISPUTE')")
    public String createDispute(Model model){
        model.addAttribute("dispute",new DisputeDto());
        return "wallet/create_dispute";
    }

    @GetMapping("/withdrawal_dispute/create")
    @PreAuthorize("hasAuthority('MANAGE_WALLET_DISPUTE')")
    public String createWalletDispute(Model model){
        model.addAttribute("dispute",new DisputeLogDTO());
        return "dispute/create_wallet_dispute";
    }

    @ModelAttribute
    public void agentModels(Model model) {
        model.addAttribute("done" ,false);
        model.addAttribute("walletNum" ,"");
        model.addAttribute("yesterday", DateUtil.formatDateToreadable_(DateUtil.yesterday())) ;
        model.addAttribute("today",DateUtil.formatDateToreadable_(DateUtil.tomorrow())) ;
    }

    @GetMapping("/search/{num}")
    public String checkWalletNumber(@PathVariable("num") String  num  , Model model){
        WalletDTO walletDTO = walletService.getWalletByNumber(num);
        model.addAttribute("done" ,true);
        model.addAttribute("walletNum" ,num);

        if(Objects.isNull(walletDTO)) {
            model.addAttribute("notfound" ,true);

        }else {
            model.addAttribute("notfound" ,false);
        }
        model.addAttribute("wallet", walletDTO);
        return "wallet/wallet_info";
    }

    @PostMapping("/dispute/create/")
    public String createDispute(@ModelAttribute("code") @Valid DisputeDto disputeDto, BindingResult result, Model model, RedirectAttributes redirectAttributes, Locale locale) {
        if (result.hasErrors()) {
            logger.error("Invalid form inputs {}", disputeDto);
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "code/add";
        }
        if (disputeDto.getType().equals("BALANCE_DISPUTE")){
            if ((disputeDto.getAmount().isEmpty()) || (disputeDto.getWalletNumber().isEmpty())){
                redirectAttributes.addFlashAttribute("Fields cannot be Null");
                //return "redirect:/core/wallet/dispute";
            }
        }
        else if (disputeDto.getType().equals("TRAN_REVERSAL")){
            disputeDto.setAction("REVERSAL");
        }


        try {
            disputeDto.setRaisedBy(AppUtility.getCurrentUserName());
            String message = walletService.raiseDispute(disputeDto);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/wallet/dispute";
        }
        catch (VerificationException e){
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/wallet/dispute";
        }
        catch (GravityException e) {
            model.addAttribute("dispute",disputeDto);
            model.addAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
            return "wallet/create_dispute";
        }
    }

    @PostMapping("/withdrawal_dispute/create/")
    public String createWithdrawalDispute(@ModelAttribute("dispute") DisputeLogDTO dispute, BindingResult result, Model model, RedirectAttributes redirectAttributes, Locale locale) {

        if (result.hasErrors()) {
            logger.error("Invalid form inputs {}", dispute);
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "dispute/create_wallet_dispute";
        }



        try {

            dispute.setLoggedBy("SYSTEM");
            dispute.setRaisedBy(AppUtility.getCurrentUserName());
            String message = disputeLogService.raiseWalletDispute(dispute);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/wallet/withdrawal_dispute/create";
        }
        catch (VerificationException e){
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/wallet/withdrawal_dispute/create";
        }
        catch (GravityException e) {
            model.addAttribute("dispute",dispute);
            model.addAttribute("errorMessage", e.getMessage());
            logger.error(e.getMessage());
            return "dispute/create_wallet_dispute";
        }
    }



    @PostMapping(value = "/trans")
    public String checkWalletTransactions(@RequestParam Map<String, String> body, Model model){


        List<FreedomWalletTransaction> walletTrans ;
        if(body.get("tranType").equals("")){
            walletTrans = walletService.getWalletTransactionsByDate(body.get("walletNumber") , DateUtil.dateFullFormat(body.get("from")), DateUtil.dateFullFormat(body.get("to")));
        }else {
            TranType tranType = TranType.valueOf(body.get("tranType"));
            walletTrans = walletService.getWalletTransactionsByDateAndTranType(body.get("walletNumber"),tranType , DateUtil.dateFullFormat(body.get("from")), DateUtil.dateFullFormat(body.get("to")));
        }

        model.addAttribute("trans", walletTrans);
        return "wallet/wallet_transaction";
    }

    @RequestMapping(value = "/creditrequest/all" , method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<CreditRequest> getDatatablesCreditRequest(@RequestParam("from") String from,
                                                                      @RequestParam("to") String to,
                                                                      @RequestParam("agentName") String agentName,
                                                                      DataTablesInput input) {

        List<CreditRequest> gravityRoleDTOPage = walletService.getFilteredCreditRequest(agentName,from,to,"PENDING");
        DataTablesOutput<CreditRequest> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(gravityRoleDTOPage);
        out.setRecordsFiltered(gravityRoleDTOPage.size());
        out.setRecordsTotal(gravityRoleDTOPage.size());
        return out;
    }

    @RequestMapping(value = "/creditrequest/history" , method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<CreditRequest> getDatatablesCreditRequestHistory(@RequestParam("from") String from,
                                                                             @RequestParam("to") String to,
                                                                             @RequestParam("agentName") String agentName,
                                                                             @RequestParam("status") String status,
                                                                             DataTablesInput input) {

        List<CreditRequest> creditRequests = walletService.
                getFilteredCreditRequest(agentName,from,to,status);
        DataTablesOutput<CreditRequest> out = new DataTablesOutput<>();
        out.setData(creditRequests);
        out.setDraw(input.getDraw());
        out.setRecordsFiltered(creditRequests.size());
        out.setRecordsTotal(creditRequests.size());
        return out;
    }

    @PostMapping("/credit/update")
    public String approveCreditRequest(@RequestParam("id") Long id,
                                       @RequestParam("updateOption") String updateOption,
                                       @RequestParam("remark") String remark,
                                       @RequestParam("bankReference") String bankreference,
                                       Model model,RedirectAttributes redirectAttributes){

        if(updateOption.equalsIgnoreCase("approve") && bankreference.equals("")){
            model.addAttribute("errorMessage" , "Kindly Enter Bank reference Number" ) ;
            redirectAttributes.addFlashAttribute("errorMessage",  "Kindly Enter Bank reference Number");
            CreditRequestDTO  creditRequestDTO = walletService.viewCreditRequest(id);
            model.addAttribute("creditRequest" , creditRequestDTO ) ;
            return "wallet/creditrequestdetail";
        }

        try {
            if(updateOption.equalsIgnoreCase("approve")){
                walletService.approveCreditRequest(id,bankreference);
                model.addAttribute("successMessage" , " Approved successfully" ) ;
                redirectAttributes.addFlashAttribute("successMessage", " Approved successfully");
                return "redirect:/core/wallet/creditrequest";
            }else{
                walletService.declineCreditRequest(id,remark);
                model.addAttribute("successMessage" , " Declined successfully" ) ;
                redirectAttributes.addFlashAttribute("successMessage", " Declined successfully");
                return "redirect:/core/wallet/creditrequest";
            }
        }catch (Exception e){
            model.addAttribute("errorMessage" , e.getMessage() ) ;
            redirectAttributes.addFlashAttribute("errorMessage",  e.getMessage());
            return "redirect:/core/wallet/creditrequest";
        }
    }

    @GetMapping("/credit/view/{id}")
    public String viewCreditRequest(@PathVariable("id") Long id,Model model,RedirectAttributes redirectAttributes){

        try {
            CreditRequestDTO  creditRequestDTO = walletService.viewCreditRequest(id);
            model.addAttribute("creditRequest" , creditRequestDTO ) ;
            return "wallet/creditrequestdetail";
        }catch (Exception e){
            model.addAttribute("errorMessage" , e.getMessage() ) ;
            redirectAttributes.addFlashAttribute("errorMessage",  e.getMessage());
            return "redirect:/core/wallet/creditrequest";
        }
    }

    @GetMapping("/credit/decline/{id}/{remark}")
    public String declineCreditRequest(@PathVariable("id") Long id,
                                       @PathVariable("id") String remark,
                                       Model model,RedirectAttributes redirectAttributes){
        try {
            walletService.declineCreditRequest(id,remark);
            model.addAttribute("successMessage" , " Declined successfully" ) ;
            redirectAttributes.addFlashAttribute("successMessage", " Declined successfully");
            return "redirect:/core/wallet/creditrequest";
        }catch (Exception e){
            model.addAttribute("errorMessage" , e.getMessage() ) ;
            redirectAttributes.addFlashAttribute("errorMessage",  e.getMessage());
            return "redirect:/core/wallet/creditrequest";
        }
    }





    @GetMapping(path = "/all")
    public @ResponseBody
    DataTablesOutput<FreedomWalletTransaction> getAllTransactions(DataTablesInput dataTablesInput, @RequestParam("csearch") String search , @RequestParam("from") String from , @RequestParam("to") String to) {

        Pageable pageable = new SpecificationBuilder<>(dataTablesInput).createPageable();
        Page<FreedomWalletTransaction> codes = walletService.findAll( search , from , to ,pageable);
        DataTablesOutput<FreedomWalletTransaction> out = new DataTablesOutput<>();
        out.setDraw(dataTablesInput.getDraw());
        out.setData(codes.getContent());
        out.setRecordsFiltered(codes.getTotalElements());
        out.setRecordsTotal(codes.getTotalElements());
        return out;
    }

    @GetMapping(path = "/dispute/all")
    public @ResponseBody
    DataTablesOutput<Dispute> getAllDisputes(DataTablesInput dataTablesInput,@RequestParam("agentName") String agentName,
                                             @RequestParam("from") String from,
                                             @RequestParam("tranType") String tranType,@RequestParam("to") String to) {

        Pageable pageable = new SpecificationBuilder<>(dataTablesInput).createPageable();
        Page<Dispute> codes = walletService.getAllDispute(agentName,from,to,tranType,pageable);
        DataTablesOutput<Dispute> out = new DataTablesOutput<>();
        out.setDraw(dataTablesInput.getDraw());
        out.setData(codes.getContent());
        out.setRecordsFiltered(codes.getTotalElements());
        out.setRecordsTotal(codes.getTotalElements());
        return out;
    }

    @GetMapping(path = "/gls/data")
    public @ResponseBody
    DataTablesOutput<FreedomWallet> getAllGlAccounts(DataTablesInput dataTablesInput) {

        Pageable pageable = new SpecificationBuilder<>(dataTablesInput).createPageable();
        Page<FreedomWallet> codes = walletService.gLAccounts(pageable);
        DataTablesOutput<FreedomWallet> out = new DataTablesOutput<>();
        out.setDraw(dataTablesInput.getDraw());
        out.setData(codes.getContent());
        out.setRecordsFiltered(codes.getTotalElements());
        out.setRecordsTotal(codes.getTotalElements());

        return out;
    }
}
