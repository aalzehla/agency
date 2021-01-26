package com._3line.gravity.web.system_user.services;


import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.freedom.service.tranupdate.dto.TransactionUpdateDto;
import com._3line.gravity.freedom.service.tranupdate.model.TransactionUpdate;
import com._3line.gravity.freedom.service.tranupdate.service.TransactionUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/core/service/tran")
public class WalletTransactionController {

    private static Logger logger = LoggerFactory.getLogger(WalletTransactionController.class);

    @Autowired
    TransactionUpdateService transactionUpdateService;



    @GetMapping("/update")
    public String view() {
        return "service/transaction_update";
    }

    @PreAuthorize("hasAuthority('UPDATE_ITEX_POS_TRANSACTIONS')")
    @GetMapping("/upload")
    public String create(){
        return "service/upload_transaction";
    }

    @PostMapping("/upload")
    public String save(@RequestParam("file") MultipartFile file ,  RedirectAttributes redirectAttributes){

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/core/service/tran/upload";
        }

        try {
            TransactionUpdateDto dto = transactionUpdateService.updateTransaction(file);
            String message = transactionUpdateService.pushTransaction(dto);
            redirectAttributes.addFlashAttribute("successMessage",message);
        }catch (VerificationException v){
            logger.info(v.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", v.getMessage());
        }catch (GravityException e){
            e.printStackTrace();
            logger.error(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/core/service/tran/update";
    }

    @RequestMapping(value = "/update/all" , method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<TransactionUpdate> getPosNotifications(DataTablesInput input) {
        try {
            Pageable pageable = new SpecificationBuilder<>(input).createPageable();
            Page<TransactionUpdate> ptspDtos =transactionUpdateService.getUpdates(pageable);
            DataTablesOutput<TransactionUpdate> out = new DataTablesOutput<TransactionUpdate>();
            out.setDraw(input.getDraw());
            out.setData(ptspDtos.getContent());
            out.setRecordsFiltered(ptspDtos.getTotalElements());
            out.setRecordsTotal(ptspDtos.getTotalElements());
            return out;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null ;
    }

}
