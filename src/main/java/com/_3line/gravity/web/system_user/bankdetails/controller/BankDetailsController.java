package com._3line.gravity.web.system_user.bankdetails.controller;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.usermgt.exception.AppUserServiceException;
import com._3line.gravity.core.verification.exceptions.PendingVerificationException;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.freedom.bankdetails.dtos.BankDetailsDTO;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.repository.BankDetailsRepository;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.utility.PropertyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
import java.util.Locale;

/**
 * @author Utosu Joy
 */

@Controller
@RequestMapping(value = "/core/bankdetails")
public class BankDetailsController {


    Logger logger = LoggerFactory.getLogger(this.getClass()) ;
    private static PropertyResource pr = new PropertyResource();


    private final MessageSource messageSource;
    BankDetailsService bankDetailsService;

    private BankDetailsRepository bankDetailsRepository;

    public BankDetailsController(BankDetailsService bankDetailsService,MessageSource messageSource){
        this.bankDetailsService = bankDetailsService;
        this.messageSource = messageSource;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('VIEW_FINANCIAL_INSTITUTIONS')")
    public String viewBanks() {

        return "bank/view";
    }



    //create a new Bank
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String createBank(Model model) {
        //map data to UI
        model.addAttribute("bank", new BankDetailsDTO());

        return "bank/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String createBank(@Valid @ModelAttribute("bankDetailsDTO") BankDetailsDTO bankDetailsDTO, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {


        if(result.hasErrors()){
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "bank/add";
        }

        try {
            String dbBankDetailsDTO = bankDetailsService.createBank(bankDetailsDTO);
            //if bank data is not empty give a successful message
            redirectAttributes.addFlashAttribute("successMessage",dbBankDetailsDTO);
            return "redirect:/core/bankdetails/";
        }catch (VerificationException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("successMessage",e.getMessage());
            return "redirect:/core/bankdetails/";
        }catch (GravityException e){
            model.addAttribute("errorMessage","Bank is not Added Successfully, Please Try Again!");
            model.addAttribute("bankDetailsDTO",bankDetailsDTO);
            return "bank/add";
        }

    }


    @Cacheable("bankdetails")
    @RequestMapping(value = "/allbankdetails" , method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<BankDetails> getBankDetails(DataTablesInput input, @RequestParam("csearch") String search) {
        try {
            Pageable pageable = new SpecificationBuilder<>(input).createPageable();
            Page<BankDetails> bankDetailsDTOPage = null;
            if(search==null || search.trim().equals("")){
                bankDetailsDTOPage = bankDetailsService.findAllBankDetailsDTOPageable(pageable);
            }else{
                bankDetailsDTOPage = bankDetailsService.findAllBankDetailsDTOPageable(search,pageable);
            }

            DataTablesOutput<BankDetails> out = new DataTablesOutput<BankDetails>();
            out.setDraw(input.getDraw());
            out.setData(bankDetailsDTOPage.getContent());
            out.setRecordsFiltered(bankDetailsDTOPage.getTotalElements());
            out.setRecordsTotal(bankDetailsDTOPage.getTotalElements());
            return out;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null ;
    }

    //Update an existing user

    @RequestMapping(value= "/{id}/edit", method = RequestMethod.GET)
    public String updateBank(@PathVariable("id") Long id, Model model ) {

        BankDetailsDTO bankDetailsDTO = bankDetailsService.getBank(id);
        model.addAttribute("updateBank", bankDetailsService.findOne(id));
        return  "bank/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateBankDetails(@Valid @ModelAttribute("bank") BankDetailsDTO bankDetailsDTO, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {


        if (result.hasErrors()) {
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "bank/edit";
        }

        try {
            String message = bankDetailsService.updateBankDetails(bankDetailsDTO);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/bankdetails/";

        } catch (PendingVerificationException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/core/bankDetails/";
        } catch (VerificationException e) {
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/bankDetails/";
        }catch (GravityException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "bank/edit";
        }


    }




}
