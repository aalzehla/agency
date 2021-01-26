package com._3line.gravity.web.system_user.dailycommission;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.utils.DateUtil;
import com._3line.gravity.freedom.cardrequest.models.CardRequest;
import com._3line.gravity.freedom.cardrequest.service.CardRequestService;
import com._3line.gravity.freedom.commisions.models.GravityDailyCommission;
import com._3line.gravity.freedom.commisions.repositories.GravityDailyCommssionRepo;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Controller
@RequestMapping(value = "core/daily_commission")
public class DailyCommissionController {

    @Autowired
    GravityDailyCommssionRepo dailyCommssionRepo;

    Logger logger = LoggerFactory.getLogger(this.getClass());



    @ModelAttribute
    public void models(Model model) {

        model.addAttribute("yesterday", DateUtil.formatDateToreadable_(DateUtil.yesterday())) ;
        model.addAttribute("today",DateUtil.formatDateToreadable_(DateUtil.tomorrow())) ;

    }


    @GetMapping("/")
    public String viewIssuelog(Model model) {
        return "daily_commission/view";
    }



    @RequestMapping(value = "/all",method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<GravityDailyCommission> getDatatablesTransactions(DataTablesInput input, @RequestParam("bankName") String bankName, @RequestParam("channel") String channel , @RequestParam("from") String from , @RequestParam("to") String to) {
        logger.info("This is the daily commission controller using {} and {} date {} and {}", channel,bankName,from,to );

        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        Page<GravityDailyCommission> gravityDailyCommissions = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date fromDateObj = null;
        Date toDateObj = null;

        try{
            fromDateObj = dateFormat.parse(from);
            toDateObj = dateFormat.parse(to);
        }catch(Exception e){
            e.printStackTrace();
        }

        if (bankName != null && !bankName.isEmpty()  && channel!=null && !channel.isEmpty() ) {
            gravityDailyCommissions = dailyCommssionRepo.
                    findByTransactionChannelAndAgentBankAndTranDateBetweenOrderByCreatedOnDesc(channel,bankName,fromDateObj,toDateObj,pageable);
        }else if(bankName != null && !bankName.isEmpty()){
            gravityDailyCommissions = dailyCommssionRepo.findByAgentBankAndTranDateBetweenOrderByCreatedOnDesc(bankName,fromDateObj,toDateObj,pageable);
        }else if(channel!=null && !channel.isEmpty()){
            gravityDailyCommissions = dailyCommssionRepo.findByTransactionChannelAndTranDateBetweenOrderByCreatedOnDesc(channel,fromDateObj,toDateObj,pageable);
        } else{
            gravityDailyCommissions = dailyCommssionRepo.findByTranDateBetweenOrderByCreatedOnDesc(fromDateObj,toDateObj,pageable);
        }
        DataTablesOutput<GravityDailyCommission> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(gravityDailyCommissions.getContent());
        out.setRecordsFiltered(gravityDailyCommissions.getTotalElements());
        out.setRecordsTotal(gravityDailyCommissions.getTotalElements());
        return out;
    }





}
