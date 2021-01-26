package com._3line.gravity.web.system_user.reports;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.utils.DateUtil;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.freedom.NIBBS.model.AgentPerformanceReport;
import com._3line.gravity.freedom.NIBBS.service.AgentDataReportService;
import com._3line.gravity.freedom.agents.dtos.AgentDto;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.reports.dtos.AgentNibbsActivity;
import com._3line.gravity.freedom.reports.dtos.AgentPerformance;
import com._3line.gravity.freedom.reports.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/core/reports")
public class ReportsController {

    @Autowired
    private ReportService reportService ;

    @Autowired
    AgentDataReportService  dataReportService;

    private Logger logger = LoggerFactory.getLogger(ReportsController.class);


    @ModelAttribute
    public void models(Model model) {

        model.addAttribute("yesterday", DateUtil.formatDateToreadable_(DateUtil.yesterday())) ;
        model.addAttribute("today",DateUtil.formatDateToreadable_(DateUtil.tomorrow())) ;

    }

    @GetMapping("/performance/")
    public String agentPerformance(){
        return "reports/agent_performance";
    }


    @GetMapping("/nibbs/agents/")
    public String nibbsAgentReport(){
        return "reports/agents";
    }

    @GetMapping("/nibbs/agents/upload/")
    public String nibbsAgentReportUpload(){
        return "reports/upload-agents-report";
    }

    @GetMapping("/nibbs/transactions/")
    public String nibbsTransactionReport(){
        return "reports/transactions";
    }

    @GetMapping("/nibbs/transactions/upload/all/")
    public @ResponseBody
    DataTablesOutput<AgentPerformanceReport> getNIBBSUploadReport(DataTablesInput input){
        List<AgentPerformanceReport> codes = dataReportService.getNIBBSUploadReport();
        System.out.println("I got :: "+codes.size());
        DataTablesOutput<AgentPerformanceReport> out = new DataTablesOutput<AgentPerformanceReport>();
        out.setData(codes);
        out.setDraw(input.getDraw());
        out.setRecordsFiltered(codes.size());
        out.setRecordsTotal(codes.size());
        return out;
    }

    @GetMapping("/nibbs/transactions/upload/")
    public String nibbsTransactionReportUpload(){
        return "reports/upload-transactions-report";
    }

    @PostMapping("/nibbs/transactions/upload/")
    public String saveUploadedAgents(@RequestParam("file") MultipartFile file , Model model, RedirectAttributes redirectAttributes){

        if (file.isEmpty()) {
            logger.info("Empty file");
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/core/reports/nibbs/transactions/upload/";
        }else{
            try {

                String ret = dataReportService.uploadBulkTransactionReport(file);
                if(ret==null){
                    model.addAttribute("successMessage", "Initial Processing Still running");
                }else{
                    model.addAttribute("successMessage", "Upload Initiated Successfully");
                }
                return "reports/upload-transactions-report";
            }catch (GravityException e){
                logger.error(e.getMessage(), e);
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            }
//            return "redirect:/core/reports/nibbs/transactions/upload/";
            return "reports/upload-transactions-report";
        }
    }


    @GetMapping(path = "/performance/data")
    public @ResponseBody
    DataTablesOutput<AgentPerformance> getAllBillPayments(DataTablesInput dataTablesInput , @RequestParam("from") String from , @RequestParam("to") String to) {

        Pageable pageable = new SpecificationBuilder<>(dataTablesInput).createPageable();
        List<AgentPerformance> codes = reportService.agentPerfomances(DateUtil.dateFullFormat(from), DateUtil.AddDays(DateUtil.dateFullFormat(to),1));
        DataTablesOutput<AgentPerformance> out = new DataTablesOutput<>();
        out.setDraw(dataTablesInput.getDraw());
        out.setData(codes);
        out.setRecordsFiltered(codes.size());
        out.setRecordsTotal(codes.size());
        return out;
    }

    @GetMapping(path = "/agents/data")
    public @ResponseBody
    DataTablesOutput<AgentDto> getNIBBSAgentReport(DataTablesInput dataTablesInput , @RequestParam("from") String from , @RequestParam("to") String to) {

        List<AgentDto> agents = new ArrayList<>();
        if(!from.equals("") || from!=null){
            agents = reportService.getAgents(DateUtil.dateFullFormat(from), DateUtil.AddDays(DateUtil.dateFullFormat(to),1));
        }
        DataTablesOutput<AgentDto> out = new DataTablesOutput<>();
        out.setDraw(dataTablesInput.getDraw());
        out.setData(agents);
        out.setRecordsFiltered(agents.size());
        out.setRecordsTotal(agents.size());
        return out;
    }

    @GetMapping(path = "/transactions/data")
    public @ResponseBody
    DataTablesOutput<AgentNibbsActivity> getNIBBSTranReport(DataTablesInput dataTablesInput , @RequestParam("from") String from , @RequestParam("to") String to) {

        List<AgentNibbsActivity> agentNibbsActivities = reportService.getAgentActivity(DateUtil.dateFullFormat(from), DateUtil.AddDays(DateUtil.dateFullFormat(to),1));
        DataTablesOutput<AgentNibbsActivity> out = new DataTablesOutput<>();
        out.setDraw(dataTablesInput.getDraw());
        out.setData(agentNibbsActivities);
        out.setRecordsFiltered(agentNibbsActivities.size());
        out.setRecordsTotal(agentNibbsActivities.size());
        return out;
    }


}
