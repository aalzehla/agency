package com._3line.gravity.web.system_user.billpayment;

import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.freedom.billpayment.dto.BillPaymentViewDto;
import com._3line.gravity.freedom.billpayment.dto.BillServicesViewDto;
import com._3line.gravity.freedom.billpayment.service.Billservice;
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
import java.util.Locale;

@Controller
@RequestMapping(value = "/core/bill/service")
public class BillController {

    @Autowired
    private Billservice billservice;
    @Autowired
    private MessageSource messageSource;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @PreAuthorize("hasAuthority('MANAGE_BILLERS')")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String view() {
        return "bill/view";
    }

    @RequestMapping(value = "/all" , method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<BillServicesViewDto> getBillServices(DataTablesInput input) {
        try {
            Pageable pageable = new SpecificationBuilder<>(input).createPageable();
            Page<BillServicesViewDto> billServicesDtos = billservice.findAllBillServiceDTOPageable(pageable);
            DataTablesOutput<BillServicesViewDto> out = new DataTablesOutput<BillServicesViewDto>();
            out.setDraw(input.getDraw());
            out.setData(billServicesDtos.getContent());
            out.setRecordsFiltered(billServicesDtos.getTotalElements());
            out.setRecordsTotal(billServicesDtos.getTotalElements());
            logger.info("content {}", out.getData().toString());
            return out;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null ;
    }

    @GetMapping("/{id}/modify")
    public String showModifyPage(@PathVariable Long id, Model model) {

        BillServicesViewDto bill =  billservice.getBillById(id);
        model.addAttribute("bill", bill);
        return "bill/modify";

    }

    @RequestMapping(value = "/modified", method = RequestMethod.POST)
    public String modifyBillService(@Valid @ModelAttribute("bill") BillServicesViewDto billServicesViewDto, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {

        if(result.hasErrors()){
            model.addAttribute("errorMzessage", messageSource.getMessage("form.fields.required", null, locale));
            return "bill/view";
        }
        try{
            String message = billservice.updateService(billServicesViewDto);
            redirectAttributes.addFlashAttribute("successMessage", message);
            return "redirect:/core/bill/service/";
        } catch (VerificationException e) {
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/bill/service";
        }
    }

    @GetMapping("/{id}/option")
    public String showAll(@PathVariable Long id, Model model) {
        model.addAttribute("options", billservice.getOptionsByServiceId(id));
        return "bill/options";
    }

    @PreAuthorize("hasAuthority('VIEW_BILL_PAYMENT_LOGS')")
    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public String viewLog() {
        return "bill/logs";
    }

    @RequestMapping(value = "/log/all" , method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<BillPaymentViewDto> getBillPayment(DataTablesInput input) {
        try {
            Pageable pageable = new SpecificationBuilder<>(input).createPageable();
            Page<BillPaymentViewDto> billPaymentViewDtos = billservice.findAllBillPaymentDTOPageable(pageable);
            DataTablesOutput<BillPaymentViewDto> out = new DataTablesOutput<BillPaymentViewDto>();
            out.setDraw(input.getDraw());
            out.setData(billPaymentViewDtos.getContent());
            out.setRecordsFiltered(billPaymentViewDtos.getTotalElements());
            out.setRecordsTotal(billPaymentViewDtos.getTotalElements());
            logger.info("content {}", out.getData().toString());
            return out;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null ;
    }

}
