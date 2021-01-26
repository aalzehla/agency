package com._3line.gravity.web.system_user.verification;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.utils.DateUtil;
import com._3line.gravity.core.verification.dtos.VerificationDto;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.core.verification.repository.VerificationRepository;
import com._3line.gravity.core.verification.service.VerificationService;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Locale;


@Controller
@RequestMapping("/core/verification")
public class VerificationController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private VerificationService verificationService;

    @Autowired
    private MessageSource messageSource;

    @ModelAttribute
    public void models(Model model) {

        model.addAttribute("yesterday", DateUtil.formatDateToreadable_(DateUtil.yesterday())) ;
        model.addAttribute("today",DateUtil.formatDateToreadable_(DateUtil.tomorrow())) ;

    }

    @PreAuthorize("hasAuthority('MANAGE_VERIFICATIONS')")
    @GetMapping("/")
    public String getVerifications(Model model) {

        return "verification/view";
    }
    @PreAuthorize("hasAuthority('MANAGE_VERIFICATIONS')")
    @GetMapping("/user")
    public String getUserVerifications(Model model) {

        return "verification/view_user";
    }

    @PreAuthorize("hasAuthority('MANAGE_VERIFICATIONS')")
    @GetMapping("/audit")
    public String getVerificationAudit(Model model) {

        return "verification/view_audits";
    }



    @PreAuthorize("hasAuthority('MANAGE_VERIFICATIONS')")
    @GetMapping("/{id}/view")
    public String getObjectsForVerification(@PathVariable Long id, Model model) {

        VerificationDto verification = verificationService.getVerification(id);
        model.addAttribute("verification", new VerificationDto());
        model.addAttribute("verify", verification);
        return "verification/details";
    }

    @PreAuthorize("hasAuthority('MANAGE_VERIFICATIONS')")
    @GetMapping("/user/{id}/view")
    public String getVerificationDetails(@PathVariable Long id, Model model) {

        VerificationDto verification = verificationService.getVerification(id);
        model.addAttribute("verification", new VerificationDto());
        model.addAttribute("verify", verification);
        return "verification/details_user";
    }

    @PreAuthorize("hasAuthority('MANAGE_VERIFICATIONS')")
    @GetMapping("/user/{id}/cancel")
    public String cancelVerification(@PathVariable Long id, Model model,RedirectAttributes redirectAttributes) {
      try {
          String msg = verificationService.cancel(id);
          redirectAttributes.addFlashAttribute("successMessage", msg);

      }catch (GravityException e){
          logger.error(e.getMessage());
          redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
      }

        return "redirect:/core/verification/user";

    }

    @PreAuthorize("hasAuthority('MANAGE_VERIFICATIONS')")
    @PostMapping("/verify")
    public String verify(@ModelAttribute("verification") @Valid VerificationDto verification, BindingResult result, WebRequest request, Model model, RedirectAttributes redirectAttributes, Locale locale) {

        String approval = request.getParameter("approve");

        try {
            if ("true".equals(approval)) {
                verificationService.verify(verification);
                model.addAttribute("successMessage" , " Approved successfully" ) ;
                redirectAttributes.addFlashAttribute("successMessage", " Approved successfully");

            } else if ("false".equals(approval)) {
                if (result.hasErrors()) {
                    VerificationDto verification2 = verificationService.getVerification(verification.getId());
                    model.addAttribute("verify", verification2);
                    model.addAttribute("errorMessage" ,"Please enter a comment") ;
                    result.addError(new ObjectError("invalid", messageSource.getMessage("reason.required", null, locale)));
                    return "verification/details";
                }
                logger.info("verification declined !!!!!");
                verificationService.decline(verification);

                model.addAttribute("successMessage" , " Declined successfully" ) ;
                redirectAttributes.addFlashAttribute("successMessage", " Declined successfully");
            }
        } catch (VerificationException ve) {
            logger.info("Error verifying the operation", ve);
            model.addAttribute("errorMessage" , ve.getMessage() ) ;
            result.addError(new ObjectError("error", ve.getMessage()));
            redirectAttributes.addFlashAttribute("errorMessage", ve.getMessage());
            return "verification/view";
        } catch (GravityException ibe) {
            logger.info("Error verifying operation", ibe);
            model.addAttribute("errorMessage" , ibe.getMessage() ) ;
            result.addError(new ObjectError("error", ibe.getMessage()));
            redirectAttributes.addFlashAttribute("errorMessage", ibe.getMessage());
            return "verification/view";

        }catch (Exception ibe) {
            logger.info("Error operation", ibe);
            model.addAttribute("errorMessage" , ibe.getMessage() ) ;
            result.addError(new ObjectError("error", ibe.getMessage()));
            redirectAttributes.addFlashAttribute("errorMessage", ibe.getMessage());
            return "verification/view";

        }
        return "redirect:/core/verification/";
    }


    @GetMapping(path = "/all")
    public
    @ResponseBody
    DataTablesOutput<VerificationDto> getAllPending(DataTablesInput input) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<VerificationDto> page = verificationService.getPendingForUser(pageable);
        DataTablesOutput<VerificationDto> out = new DataTablesOutput<VerificationDto>();
        out.setDraw(input.getDraw());
        out.setData(page.getContent());
        out.setRecordsFiltered(page.getTotalElements());
        out.setRecordsTotal(page.getTotalElements());
        return out;
    }


    @GetMapping(path = "/user/all")
    public
    @ResponseBody
    DataTablesOutput<VerificationDto> getAllUserVerifications(DataTablesInput input) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<VerificationDto> page = verificationService.getInitiatedByUser(pageable);
        DataTablesOutput<VerificationDto> out = new DataTablesOutput<VerificationDto>();
        out.setDraw(input.getDraw());
        out.setData(page.getContent());
        out.setRecordsFiltered(page.getTotalElements());
        out.setRecordsTotal(page.getTotalElements());
        return out;
    }

    @GetMapping(path = "/audits/all")
    public
    @ResponseBody
    DataTablesOutput<VerificationDto> getAllVerifiableAUdit(DataTablesInput input,
                                                            @RequestParam("opCode") String opCode,
                                                            @RequestParam("initiator") String initiator ,
                                                            @RequestParam("from") String from ,
                                                            @RequestParam("to") String to) {
        System.out.println(opCode+" dfdf");
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<VerificationDto> page = verificationService.getApprovableActions(pageable,opCode,initiator,from,to);
        DataTablesOutput<VerificationDto> out = new DataTablesOutput<VerificationDto>();
        out.setDraw(input.getDraw());
        out.setData(page.getContent());
        out.setRecordsFiltered(page.getTotalElements());
        out.setRecordsTotal(page.getTotalElements());
        return out;
    }


}
