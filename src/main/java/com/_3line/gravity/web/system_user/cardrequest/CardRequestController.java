package com._3line.gravity.web.system_user.cardrequest;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.utils.DateUtil;
import com._3line.gravity.freedom.cardrequest.models.CardRequest;
import com._3line.gravity.freedom.cardrequest.service.CardRequestService;
import com._3line.gravity.freedom.utility.PropertyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping(value = "core/cards")
public class CardRequestController {

    private static PropertyResource pr = new PropertyResource();
    private final Locale locale = LocaleContextHolder.getLocale();
    @Autowired
    CardRequestService cardRequestService;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MessageSource messageSource;

    @PreAuthorize("hasAuthority('VIEW_ISSUE_LOG')")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String viewIssuelog(Model model) {

        return "cardrequests/view";
    }

    @ModelAttribute
    public void models(Model model) {

        model.addAttribute("yesterday", DateUtil.formatDateToreadable_(DateUtil.yesterday())) ;
        model.addAttribute("today",DateUtil.formatDateToreadable_(DateUtil.tomorrow())) ;

    }

    @RequestMapping(value = "/all")
    @ResponseBody
    public DataTablesOutput<CardRequest> getDatatablesTransactions(DataTablesInput input,@RequestParam("csearch") String search,@RequestParam("status") String status , @RequestParam("from") String from , @RequestParam("to") String to) {
        logger.info("This is the core card request controller using {} and {} date {} and {}", status,search,from,to );

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<CardRequest> gravityRoleDTOPage = null;
        if (status != null && !status.isEmpty()) {
            gravityRoleDTOPage = cardRequestService.getCards(search, status, from, to, pageable);
        }
       else {
            gravityRoleDTOPage = cardRequestService.getCards(pageable);
        }
        DataTablesOutput<CardRequest> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(gravityRoleDTOPage.getContent());
        out.setRecordsFiltered(gravityRoleDTOPage.getTotalElements());
        out.setRecordsTotal(gravityRoleDTOPage.getTotalElements());
        return out;
    }


    @PreAuthorize("hasAuthority('MANAGE_ISSUE_LOG')")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    public String updateLog(@PathVariable("id") Long id, Model model) {

        CardRequest cardRequest = cardRequestService.getCardById(id);
        model.addAttribute("cardRequest", cardRequest);
        return "cardrequests/update";
    }

    @PreAuthorize("hasAuthority('MANAGE_ISSUE_LOG')")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    public String getUpdateCommentAndStatus(@Valid @ModelAttribute("updateIssue") CardRequest cardRequest, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {


        if (result.hasErrors()) {
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return "issuelog/view";
        }

        try {
            cardRequestService.setCardAsDelivered(cardRequest);
            logger.info("Response {}", "SUCCESS");
            model.addAttribute("cardRequest", cardRequest);
            model.addAttribute("successMessage", "Updated Successfully");
            return "cardrequests/update";
        }
        catch (GravityException e) {
            logger.error(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/core/cards/";
        }

    }


}
