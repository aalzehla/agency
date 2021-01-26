package com._3line.gravity.web.system_user.channels;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.verification.exceptions.PendingVerificationException;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.freedom.bankdetails.dtos.BankDetailsDTO;
import com._3line.gravity.freedom.bankdetails.repository.BankDetailsRepository;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.transactions.models.TranChannel;
import com._3line.gravity.freedom.transactions.service.TranChannelService;
import com._3line.gravity.freedom.utility.PropertyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping(value = "/core/channels")
public class TranChannelsController {


    Logger logger = LoggerFactory.getLogger(this.getClass()) ;
    private static PropertyResource pr = new PropertyResource();


    private final MessageSource messageSource;
    TranChannelService tranChannelService;

    public TranChannelsController(TranChannelService tranChannelService, MessageSource messageSource){
        this.tranChannelService = tranChannelService;
        this.messageSource = messageSource;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('VIEW_FINANCIAL_INSTITUTIONS')")
    public String viewChannels() {

        return "tran_channel/view";
    }



    //create a new Bank
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String createChannel(Model model) {
        //map data to UI
        model.addAttribute("tranChannel", new TranChannel());

        return "tran_channel/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String createChannel(@Valid @ModelAttribute("tranChannel") TranChannel tranChannel, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {


        if(result.hasErrors()){
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "tran_channel/add";
        }

        try {
            String dbBankDetailsDTO = tranChannelService.createBank(tranChannel);
            //if bank data is not empty give a successful message
            redirectAttributes.addFlashAttribute("successMessage",dbBankDetailsDTO);
            return "redirect:/core/channels/";
        }catch (VerificationException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("successMessage",e.getMessage());
            return "redirect:/core/channels/";
        }catch (GravityException e){
            e.printStackTrace();
            model.addAttribute("errorMessage",e.getMessage());
            model.addAttribute("tranChannel",tranChannel);
            return "tran_channel/add";
        }

    }


    @RequestMapping(value = "/allChannels" , method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<TranChannel> getChannels(DataTablesInput input) {
        try {
            logger.info("datatable input is {}", input.toString());
            logger.info("order size is  {}", input.getOrder().size());
            Pageable pageable = new SpecificationBuilder<>(input).createPageable();
            logger.info("sort is {}", pageable.getSort());
            Page<TranChannel> channelsPageable = tranChannelService.getChannelsPageable(pageable);
            DataTablesOutput<TranChannel> out = new DataTablesOutput<>();
            out.setDraw(input.getDraw());
            out.setData(channelsPageable.getContent());
            out.setRecordsFiltered(channelsPageable.getTotalElements());
            out.setRecordsTotal(channelsPageable.getTotalElements());
            return out;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null ;
    }

    //Update an existing user

    @RequestMapping(value= "/{id}/edit", method = RequestMethod.GET)
    public String getChannelDetails(@PathVariable("id") Long id, Model model ) {

        TranChannel tranChannel = tranChannelService.findOne(id);
        model.addAttribute("channelDetails", tranChannel);
        return  "tran_channel/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateChannels(@Valid @ModelAttribute("tranChannel") TranChannel tranChannel, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {


        if (result.hasErrors()) {
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "tran_channel/edit";
        }

        try {
            String message =  tranChannelService.updateBankDetails(tranChannel);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/channels/";

        } catch (PendingVerificationException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/core/channels/";
        } catch (VerificationException e) {
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/channels/";
        }catch (GravityException e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "tran_channel/edit";
        }


    }




}
