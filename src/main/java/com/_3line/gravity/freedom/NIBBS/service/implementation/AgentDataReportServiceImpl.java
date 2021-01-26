package com._3line.gravity.freedom.NIBBS.service.implementation;

import com._3line.gravity.api.utility.GeneralUtil;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.files.service.FileService;
import com._3line.gravity.freedom.NIBBS.dto.AgentDataReportDTO;
import com._3line.gravity.freedom.NIBBS.dto.TransactionReportDTO;
import com._3line.gravity.freedom.NIBBS.model.AgentDataReport;
import com._3line.gravity.freedom.NIBBS.model.AgentPerformanceReport;
import com._3line.gravity.freedom.NIBBS.model.NIBBSReportSettings;
import com._3line.gravity.freedom.NIBBS.repository.AgentDataReportRepo;
import com._3line.gravity.freedom.NIBBS.repository.AgentPerformanceReportRepo;
import com._3line.gravity.freedom.NIBBS.repository.ReportSettingsRepo;
import com._3line.gravity.freedom.NIBBS.service.AgentDataReportService;
import com._3line.gravity.freedom.NIBBS.service.NIBBSReportService;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.reports.dtos.AgentNibbsActivity;
import com._3line.gravity.freedom.reports.service.ReportService;
import io.github.mapper.excel.ExcelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AgentDataReportServiceImpl implements AgentDataReportService {

    @Autowired
    AgentDataReportRepo agentDataReportRepo;

    @Autowired
    NIBBSReportService nibbsReportService;

    @Autowired
    GeneralUtil  generalUtil;

    @Autowired
    ReportSettingsRepo  reportSettingsRepo;

    @Autowired
    AgentPerformanceReportRepo performanceReportRepo;

    @Autowired
    FileService  fileService;

    @Autowired
    AgentService  agentService;

    @Autowired
    ReportService reportService;

    private Logger logger = LoggerFactory.getLogger(AgentDataReportServiceImpl.class);



    @Override
    public AgentDataReport fetchAgentDataReport(String agentId) {
        System.out.println("Searching with :: "+agentId);
        return agentDataReportRepo.findByAgent_AgentId(agentId);
    }

    @Override
    public AgentDataReport addAgentDataReport(Agents agent) throws GravityException{

        AgentDataReportDTO dataReportDTO = generalUtil.converttoagentdatareportdto(agent);

        Boolean doPush = false;

        AgentDataReport dataReport = agentDataReportRepo.findByNibbsAgentEmail(agent.getEmail());
        if(dataReport!=null){
            if(!dataReport.getStatus().equalsIgnoreCase("success")){
                doPush = true;
            }
        }else{
            dataReport = new AgentDataReport();
            String finalEmail = agent.getEmail();
            //check if email already exist
            List<Agents> agentsList =  agentService.fetchAgentByEmail(dataReportDTO.getEmailAddress());
            if(agentsList.size() > 1 ){
                finalEmail = finalEmail.replace("@",new Date().getTime()+"@");
            }
            doPush = true;
            dataReportDTO.setEmailAddress(finalEmail);
            dataReport.setNibbsAgentEmail(finalEmail);
            dataReport.setAgent(agent);
        }

        if(doPush){
            dataReportDTO = nibbsReportService.createAgent(dataReportDTO);

            if(dataReportDTO!=null){
                System.out.println("incoming is :: "+dataReportDTO);
                dataReport.setStatus(dataReportDTO.getResponseMsg());

                if(dataReportDTO.getResponseCode().equals("00")){
                    dataReport.setNibbsAgentCode(dataReportDTO.getAgentCode());
                    dataReport.setStatus("success");
                    agentService.setNIBSSAgentCode(agent.getAgentId(),dataReport.getNibbsAgentCode());
                }
            }else{
                dataReport.setStatus("UNDEFINED");
            }

            dataReport = agentDataReportRepo.save(dataReport);
        }




        return dataReport;
    }

    @Override
    public AgentDataReport updateAgentDataReport(Agents agent) throws GravityException{

        AgentDataReportDTO updatedDTO = generalUtil.converttoagentdatareportdto(agent);

        AgentDataReport model = fetchAgentDataReport(agent.getAgentId());

        if(agent.getAgentCode()!=null && !agent.getAgentCode().equals("") ){
            model = fetchAgentDataReport(agent.getAgentId());
            if(model ==null){
                model = new AgentDataReport();
                model.setAgent(agent);
                model.setNibbsAgentEmail(agent.getEmail());
            }
            model.setNibbsAgentCode(agent.getAgentCode());
            model = agentDataReportRepo.save(model);
        }else if(model!=null && model.getNibbsAgentCode()!=null){

            updatedDTO.setAgentCode(model.getNibbsAgentCode());
            updatedDTO.setEmailAddress(model.getNibbsAgentEmail());

            updatedDTO = nibbsReportService.updateAgent(updatedDTO);

            if(updatedDTO!=null){
                model.setStatus(updatedDTO.getResponseMsg());
                if(updatedDTO.getResponseCode().equals("00")){
                    model.setStatus("success");
                }
            }else{
                model.setStatus("UNDEFINED");
            }
            model.setUpdatedOn(new Date());
            model = agentDataReportRepo.save(model);
        }else{
            addAgentDataReport(agent);
        }

        return model;
    }


//    @Scheduled(fixedDelay = 60000)
    @Override
    public void sendBulkTransactionReport() {
        List<AgentPerformanceReport> reportas = performanceReportRepo.findByStatusOrderByCreatedOnDesc("initiated");
        if(reportas!=null && reportas.size()>0){
            AgentPerformanceReport performanceReport = performanceReportRepo.findById(reportas.get(0).getId()).orElse(null) ;
            File file;
            try {

                file = fileService.loadFileAsResource(reportas.get(0).getFileUploaded()).getFile();
                List<AgentNibbsActivity> dtos = mapTransactionDataFromFile(file);
                List<TransactionReportDTO> transactionReportDTOS = generalUtil.converttotransactionreportdto(dtos);

                performanceReport.setStatus("processing");
                performanceReportRepo.save(performanceReport);
                String uploadProcessId = String.valueOf(performanceReport.getId());

                nibbsReportService.createReportInBulk(transactionReportDTOS,uploadProcessId);

                performanceReport = performanceReportRepo.findById(reportas.get(0).getId()).orElse(null) ;

                performanceReport.setStatus("Completed");

                performanceReportRepo.save(performanceReport);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        else{
            List<NIBBSReportSettings> NIBBSReportSettings = reportSettingsRepo.findAll();
            if(NIBBSReportSettings !=null && NIBBSReportSettings.size()>0){
                NIBBSReportSettings reportSetting = NIBBSReportSettings.get(0);
                if(reportSetting.getAutomateReportSending()!=null && reportSetting.getAutomateReportSending()){
                    Date now = new Date();
                    Date nextRUnDate = reportSetting.getNextRunDate();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(nextRUnDate);
                    calendar.add(Calendar.MINUTE,reportSetting.getFetchInterval());

                    if(now.getTime() >= nextRUnDate.getTime() &&
                            performanceReportRepo.findByUpdatedOn(reportSetting.getNextRunDate())==null){

                        System.out.println("am sending transaction report now at "+now);


                        List<AgentNibbsActivity> activities = reportService.getAgentActivity(nextRUnDate,calendar.getTime());

                        List<TransactionReportDTO> transactionReportDTOS = generalUtil.converttotransactionreportdto(activities);
                        //todo create file from list and save path in audit
                        nibbsReportService.createReportInBulk(transactionReportDTOS,"M"+new Date().getTime());


                        //add performance report audit along with file path or id
                        AgentPerformanceReport performanceReport = new AgentPerformanceReport();
                        performanceReport.setUpdatedOn(reportSetting.getNextRunDate());
                        performanceReport.setUpdateProcess("AUTOMATIC");
                        performanceReport.setNumOfRecordsPushed(transactionReportDTOS.size());
                        performanceReport.setFileUploaded("C://users/sample.txt");
                        performanceReport.setStatus("Completed");
                        performanceReportRepo.save(performanceReport);

                        //update report setting
                        reportSetting.setLastRunDate(reportSetting.getNextRunDate());
                        reportSetting.setNextRunDate(calendar.getTime());
                        reportSettingsRepo.save(reportSetting);

                    }else{
                        //report has been pushed
                        System.out.println("report has been pushed "+new Date());
                    }
                }else{
                    System.out.println("automated report setting is disabled "+new Date());
                }
            }else{
                //no settings found.. leave process for manual initiation
                System.out.println("no settings found.. leave process for manual initiation "+new Date());
            }
        }
    }

    @Override
    public String uploadBulkTransactionReport(MultipartFile multipartFile) {

        List<AgentPerformanceReport> performanceReports = performanceReportRepo.findByStatusOrderByCreatedOnDesc("processing");
        List<AgentPerformanceReport> performanceReports2 = performanceReportRepo.findByStatusOrderByCreatedOnDesc("initiated");
        if((performanceReports !=null && performanceReports.size()> 0)
                || (performanceReports2!=null && performanceReports2.size()>0)){
            logger.info("process still running");
            return null;
        }else{
            logger.info("Uploading Transactions file: {}", multipartFile.getOriginalFilename());

            Long fileId = fileService.storeFile(multipartFile, "NIBBS_TRANSACTION_REPORT_FILE");

            File file;
            try {
                file = fileService.loadFileAsResource(fileId).getFile();

                List<AgentNibbsActivity> dtos = mapTransactionDataFromFile(file);

                List<TransactionReportDTO> transactionReportDTOS = generalUtil.converttotransactionreportdto(dtos);

                AgentPerformanceReport performanceReport = new AgentPerformanceReport();
                performanceReport.setUpdatedOn(new Date());
                performanceReport.setUpdateProcess("MANUAL");
                performanceReport.setNumOfRecordsPushed(transactionReportDTOS.size());
                performanceReport.setFileUploaded(file.getAbsolutePath());
                performanceReport.setStatus("initiated");
                performanceReportRepo.save(performanceReport);

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new GravityException("Error uploading file");

            } catch (Throwable throwable) {
                logger.error(throwable.getMessage(), throwable);
                throw new GravityException("Error uploading file");
            }

            return "";
        }


    }

    @Override
    public List<AgentPerformanceReport> getNIBBSUploadReport() {
        List<AgentPerformanceReport> performanceReports = performanceReportRepo.findAll();
        return performanceReports;
    }

    private List<AgentNibbsActivity> mapTransactionDataFromFile(File file) throws Throwable{

        List<AgentNibbsActivity> dtos = ExcelMapper.mapFromExcel(file)
                .toObjectOf(AgentNibbsActivity.class)
                .fromSheet(0) // if this method not used , called all sheets
                .map();
        return dtos;
    }
}
