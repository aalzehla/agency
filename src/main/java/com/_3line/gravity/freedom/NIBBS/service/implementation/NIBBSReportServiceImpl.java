package com._3line.gravity.freedom.NIBBS.service.implementation;

import com._3line.gravity.api.utility.GeneralUtil;
import com._3line.gravity.core.utils.HttpCustomClient;
import com._3line.gravity.freedom.NIBBS.dto.AgentDataReportDTO;
import com._3line.gravity.freedom.NIBBS.dto.TransactionReportDTO;
import com._3line.gravity.freedom.NIBBS.dto.ResetReqDTO;
import com._3line.gravity.freedom.NIBBS.model.TransationReportAudit;
import com._3line.gravity.freedom.NIBBS.repository.TransactionReportAuditRepo;
import com._3line.gravity.freedom.NIBBS.service.NIBBSReportService;
import com._3line.gravity.freedom.NIBBS.util.AESHash;
import com._3line.gravity.freedom.reports.dtos.AgentNibbsActivity;
import com._3line.gravity.freedom.reports.service.ReportService;
import com._3line.gravity.freedom.utility.PropertyResource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class NIBBSReportServiceImpl implements NIBBSReportService {

//    @Value("${nibbs.service.url}")
    private String baseURL = "http://196.6.103.58:8080/agencybankingservice";

//    @Value("${nibbs.bulk.transaction.maximumrow}")
    private String maximumPushableRowRequest="10";

    @Autowired
    private AESHash aesHash;

    @Autowired
    ReportService reportService;

    @Autowired
    GeneralUtil generalUtil;

    @Autowired
    PropertyResource propertyResource;

    @Autowired
    TransactionReportAuditRepo transactionReportAuditRepo;




    private HashMap<String,String> headerConents;

    private Logger logger = LoggerFactory.getLogger(NIBBSReportServiceImpl.class);

    public NIBBSReportServiceImpl() {
        headerConents = new HashMap<>();
        headerConents.put("AUTHORIZATION",aesHash.generateAuthHeader());
        headerConents.put("SIGNATURE",aesHash.generateSignature());
        headerConents.put("SIGNATURE_METH","SHA256");
        headerConents.put("Content-Type","application/json");
    }

    @Override
    public void ping() {
        String url = baseURL+"/ping";
        this.logger.info("Pinging service at :: "+url);
        HttpCustomClient httpCustomClient = new HttpCustomClient(url,null,"GET",true,headerConents);

        httpCustomClient.sendRequest();
    }

    @Override
    public void reset(ResetReqDTO resetReqDTO) {

        String url = baseURL+"/reset";
        Gson g = new Gson();
        String requestBody = g.toJson(resetReqDTO);

        HttpCustomClient httpCustomClient = new HttpCustomClient(url,requestBody,"POST",true,headerConents);

        httpCustomClient.sendRequest();
        logger.info("***IMPORTANT** Please remember to update new keys in AESH class or config " +
                "files after successful reset");
    }

    @Override
    public AgentDataReportDTO createAgent(AgentDataReportDTO agentDataReportDTO) {
        String url = baseURL+"/api/agents/create";
        return processAgentOperation(agentDataReportDTO,url);
    }

    @Override
    public AgentDataReportDTO updateAgent(AgentDataReportDTO agentDataReportDTO) {
        String url = baseURL+"/api/agents/update";
        return processAgentOperation(agentDataReportDTO,url);

    }


    @Override
    public void createReport(TransactionReportDTO performanceDTO) {
        String url = baseURL+"/api/transactions/single";
        Gson g = new Gson();
        String requestBody = aesHash.encrypt(g.toJson(performanceDTO));

        HttpCustomClient httpCustomClient = new HttpCustomClient(url,requestBody,"POST",true,headerConents);

        httpCustomClient.sendRequest();

    }

    @Override
    public void createReportInBulk(List<TransactionReportDTO> transactionReportDTOS,String uploadProcessId) {
        int totalRequestBatch=0;
        int maxRecordPerPush = Integer.parseInt(maximumPushableRowRequest);
        List<TransactionReportDTO> reportDTORequest = new ArrayList<>();

            for(int i=0;i<transactionReportDTOS.size();i++){
                reportDTORequest.add(transactionReportDTOS.get(i));
                if(transactionReportDTOS.size()-i == 1 && reportDTORequest.size() != maxRecordPerPush){
                    int batchNum = i /10 + 1;
                    processBulkTanRequest(reportDTORequest,uploadProcessId,batchNum);
                }

                if(maxRecordPerPush == reportDTORequest.size()){
                    int batchNum = i /10 + 1;
                    processBulkTanRequest(reportDTORequest,uploadProcessId,batchNum);

                    totalRequestBatch++;
                    reportDTORequest = new ArrayList<>();
                }
            }
    }

    private void processBulkTanRequest(List<TransactionReportDTO> reportDTORequest,String uploadProcesId,int batchNo ){
        TransationReportAudit reportAudit = new TransationReportAudit();
        reportAudit.setBatch(batchNo);
        reportAudit.setUploadProcessId(uploadProcesId);
        reportAudit.setStatus("processing");
        reportAudit = transactionReportAuditRepo.save(reportAudit);
        String url = baseURL+"/api/transactions/bulk";
        Gson g = new Gson();
        System.out.println("Sending request in plain form :: "+g.toJson(reportDTORequest));

        String requestBody = aesHash.encrypt(g.toJson(reportDTORequest));
        HttpCustomClient httpCustomClient = new HttpCustomClient(url,requestBody,"POST",true,headerConents);

        String resp = httpCustomClient.sendRequest();
        System.out.println("Service response :: "+resp);
        reportAudit.setStatus(resp);
        reportAudit.setUpdatedOn(new Date());
        transactionReportAuditRepo.save(reportAudit);


    }


    private AgentDataReportDTO processAgentOperation(AgentDataReportDTO agentDataReportDTO,String url){
        Gson g = new Gson();
        String requestBody = aesHash.encrypt(g.toJson(agentDataReportDTO));

        System.out.println("Plain request sent is :: "+g.toJson(agentDataReportDTO));

        HttpCustomClient httpCustomClient = new HttpCustomClient(url,requestBody,"POST",true,headerConents);

        String resp = httpCustomClient.sendRequest();
        AgentDataReportDTO nibbsResponse = g.fromJson(resp,AgentDataReportDTO.class);

        agentDataReportDTO.setResponseCode(nibbsResponse.getResponseCode());
        if(nibbsResponse.getResponseCode().equals("00")){
            agentDataReportDTO.setAgentCode(nibbsResponse.getAgentCode());
        }else{
            if(nibbsResponse.getErrors()!=null && nibbsResponse.getErrors().length > 0){
                agentDataReportDTO.setResponseMsg(nibbsResponse.getErrors()[0]);
            }else{
                agentDataReportDTO.setResponseMsg(propertyResource.getV(nibbsResponse.getResponseCode(),"nibbs_mesage.properties"));
            }
        }

        return agentDataReportDTO;
    }

}
