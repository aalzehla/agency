package com._3line.gravity.web.system_user.services;


import com._3line.gravity.freedom.service.settlementreport.model.SettlementReportDto;
import com._3line.gravity.freedom.service.settlementreport.service.SettlementReportService;
import com._3line.gravity.freedom.utility.DateUtil;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/core/service/settlement")
public class SettlementReportController {

    private static Logger logger = LoggerFactory.getLogger(SettlementReportController.class);

    private SettlementReportService settlementReportService;

    @ModelAttribute
    public void models(Model model) {

        model.addAttribute("yesterday", com._3line.gravity.core.utils.DateUtil.formatDateToreadable_(com._3line.gravity.core.utils.DateUtil.yesterday())) ;
        model.addAttribute("today", com._3line.gravity.core.utils.DateUtil.formatDateToreadable_(com._3line.gravity.core.utils.DateUtil.tomorrow())) ;

    }

    @GetMapping("/")
    public String view() {
        return "service/settlement_report";
    }

    @Autowired
    public SettlementReportController(SettlementReportService settlementReportService) {
        this.settlementReportService = settlementReportService;
    }


    @RequestMapping(value = "/report/all" , method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<SettlementReportDto> getPosNotifications(DataTablesInput input  , @RequestParam("from") String from , @RequestParam("to") String to) {
        try {
//           Pageable pageable = new SpecificationBuilder<>(input).createPageable();
            List <SettlementReportDto> reportDtos = settlementReportService.getSettlementReport(DateUtil.dateFullFormat(from), DateUtil.AddDays(DateUtil.dateFullFormat(to),1));
            DataTablesOutput<SettlementReportDto> out = new DataTablesOutput<>();
            out.setDraw(input.getDraw());
            out.setData(reportDtos);
            out.setRecordsFiltered(reportDtos.size());
            out.setRecordsTotal(reportDtos.size());
            return out;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null ;
    }
}
