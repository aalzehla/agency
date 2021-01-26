package com._3line.gravity.freedom.reports.service;

import com._3line.gravity.api.users.agents.dto.AgentMiniStatementDTO;
import com._3line.gravity.freedom.agents.dtos.AgentDto;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.billpayment.repository.BillPaymentRepo;
import com._3line.gravity.freedom.financialInstitutions.wemaapi.utils.DateFormatter;
import com._3line.gravity.freedom.reports.dtos.AgentNibbsActivity;
import com._3line.gravity.freedom.reports.dtos.AgentPerformance;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.utility.DateUtil;
import com._3line.gravity.freedom.utility.Utility;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ReportService {

    private static Logger logger = LoggerFactory.getLogger(ReportService.class);

    private TransactionRepository transactionRepository;
    private AgentsRepository agentsRepository;
    private BillPaymentRepo billPaymentRepo ;
    private WalletService walletService;

    private DateFormatter simpleDateFormatter = new DateFormatter();


    @Autowired
    public ReportService(TransactionRepository transactionRepository, AgentsRepository agentsRepository, BillPaymentRepo billPaymentRepo, WalletService walletService) {
        this.transactionRepository = transactionRepository;
        this.agentsRepository = agentsRepository;
        this.billPaymentRepo = billPaymentRepo;
        this.walletService = walletService;
    }


    public List<AgentPerformance> agentPerfomances(Date from , Date to){

        List<AgentPerformance> response = new ArrayList<>();
        String starDate = simpleDateFormatter.customFormatter.format(from);
        String endDate = simpleDateFormatter.customFormatter.format(to);
        List<String[]> agentsPerformance = Utility.getStringsFromObjectList(transactionRepository.getAgentPerformance(starDate,endDate));

        for (String[] a: agentsPerformance) {
            AgentPerformance performance = new AgentPerformance();
            performance.setAgentName(a[0]);
            performance.setTerminal(escapeNulls(a[1]));
            performance.setAgreegator(escapeNulls(a[2]));
            performance.setTotalDeposits(Utility.formatForView(roundValueForData(a[4])));
            performance.setTotalDepositsValue(Utility.formatForView(roundValueForData(a[3])));
            performance.setTotalWithdrawals(Utility.formatForView(roundValueForData(a[6])));
            performance.setTotalWithdrawalsValue(Utility.formatForView(roundValueForData(a[5])));
            response.add(performance);
        }

        return response ;
    }

    public List<AgentPerformance> aggregatorsAgentPerformance(boolean isSubAggregator,Agents agent,Date from , Date to){

        logger.info("Data used :: {}, {}, {} ",agent.getUsername(),from,to);

        List<AgentPerformance> response = new ArrayList<>();
        String starDate = simpleDateFormatter.customFormatter.format(from);
        String endDate = simpleDateFormatter.customFormatter.format(to);
        List<String[]> agentsPerformance;
        if(!isSubAggregator){
            agentsPerformance = Utility.getStringsFromObjectList(transactionRepository
                    .getAggregatorAgentsPerformance(starDate,endDate,agent.getId()));
        }else{
            agentsPerformance = Utility.getStringsFromObjectList(transactionRepository
                    .getSubAggregatorAgentsPerformance(starDate,endDate,agent.getId()));
        }


        for (String[] a: agentsPerformance) {
            AgentPerformance performance = new AgentPerformance();

            performance.setAgentId(a[0]);
            performance.setAgentName(a[1]);
            performance.setTerminal(escapeNulls(a[2]));
            performance.setAgreegator(agent.getUsername());
//            bill_val,b.bill_vol,d.dep_val,d.dep_vol,w.with_val,w.with_vol,r.rech_val,r.rech_vol
            performance.setBillsPaymentsValue(Utility.formatForView(roundValueForData(a[3])));
            performance.setBillsPayments(Utility.formatForView(roundValueForData(a[4])));
            performance.setTotalDepositsValue(Utility.formatForView(roundValueForData(a[5])));
            performance.setTotalDeposits(Utility.formatForView(roundValueForData(a[6])));
            performance.setTotalWithdrawalsValue(Utility.formatForView(roundValueForData(a[7])));
            performance.setTotalWithdrawals(Utility.formatForView(roundValueForData(a[8])));
            response.add(performance);
        }

        return response ;
    }

    public AgentMiniStatementDTO agentsMiniStatement(Agents agent, Date from , Date to){

        AgentMiniStatementDTO agentMiniStatementDTO = new AgentMiniStatementDTO();

        String starDate = simpleDateFormatter.newDateFormat.format(from)+" 00:00:00";
        String endDate = simpleDateFormatter.newDateFormat.format(to)+" 00:00:00";

        logger.info("Data used :: {}, {}, {} ",agent.getUsername(),starDate,endDate);
        List<Object[]> miniStatement = transactionRepository.getMiniStatement(starDate,endDate,agent.getId());

        for (Object[] a: miniStatement) {

            agentMiniStatementDTO.setSuccessfulRechargeVal(Utility.formatForView(roundValueForData(String.valueOf(a[16]))));
            agentMiniStatementDTO.setSuccessfulRechargeVol(Utility.formatForView(roundValueForData(String.valueOf(a[17]))));
            agentMiniStatementDTO.setFailedRechargeVal(Utility.formatForView(roundValueForData(String.valueOf(a[18]))));
            agentMiniStatementDTO.setFailedRechargeVol(Utility.formatForView(roundValueForData(String.valueOf(a[19]))));

            agentMiniStatementDTO.setSuccessfulAcctOpening(Utility.formatForView(roundValueForData(String.valueOf(a[15]))));

            agentMiniStatementDTO.setSuccessfulBillPaymntVol(Utility.formatForView(roundValueForData(String.valueOf(a[12]))));
            agentMiniStatementDTO.setSuccessfulBillPaymntVal(Utility.formatForView(roundValueForData(String.valueOf(a[11]))));
            agentMiniStatementDTO.setSuccessfulDepositVol(Utility.formatForView(roundValueForData(String.valueOf(a[4]))));
            agentMiniStatementDTO.setSuccessfulDepositVal(Utility.formatForView(roundValueForData(String.valueOf(a[3]))));
            agentMiniStatementDTO.setSuccessfulWithVol(Utility.formatForView(roundValueForData(String.valueOf(a[8]))));
            agentMiniStatementDTO.setSuccessfulWithVal(Utility.formatForView(roundValueForData(String.valueOf(a[7]))));

            agentMiniStatementDTO.setFailedBillPaymntVol(Utility.formatForView(roundValueForData(String.valueOf(a[14]))));
            agentMiniStatementDTO.setFailedBillPaymntVal(Utility.formatForView(roundValueForData(String.valueOf(a[13]))));
            agentMiniStatementDTO.setFailedDepositVol(Utility.formatForView(roundValueForData(String.valueOf(a[6]))));
            agentMiniStatementDTO.setFailedDepositVal(Utility.formatForView(roundValueForData(String.valueOf(a[5]))));
            agentMiniStatementDTO.setFailedWithVol(Utility.formatForView(roundValueForData(String.valueOf(a[10]))));
            agentMiniStatementDTO.setFailedWithVal(Utility.formatForView(roundValueForData(String.valueOf(a[9]))));

        }

        return agentMiniStatementDTO ;
    }

    public List<AgentDto> getAgents(Date starDate , Date endDate){

        ModelMapper modelMapper = new ModelMapper();
        List<Agents> agents = agentsRepository.findByDatecreatedBetween(starDate,endDate) ;
        List<AgentDto> agentDtos = new ArrayList<>();

        for (Agents a: agents) {
            AgentDto agentDto = modelMapper.map(a,AgentDto.class);
            String finalName = String.format("%s %s",agentDto.getFirstName() ,agentDto.getLastName());
            agentDto.setAgentName(finalName);
            if(agentDto.getDatecreated()!=null){
                Date onBoardDate;
                try {
                    onBoardDate = DateFormatter.customFormatter.parse(agentDto.getDatecreated());
                    agentDto.setDatecreated(DateFormatter.agentOnbardDateFormat.format(onBoardDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else{
                agentDto.setDatecreated("");
            }
            agentDtos.add(agentDto);
        }

        return agentDtos ;
    }

    public List<AgentNibbsActivity> getAgentActivity(Date from , Date to){

        String starDate = simpleDateFormatter.customFormatter.format(from);
        String endDate = simpleDateFormatter.customFormatter.format(to);

        List<AgentNibbsActivity> agentNibbsActivities = new ArrayList<>() ;
        List<String[]> transactions = Utility.getStringsFromObjectList(transactionRepository.getAgentTransactionList(starDate,endDate));
        for(String[] t:transactions){
            AgentNibbsActivity agentNibbsActivity = new AgentNibbsActivity();
            agentNibbsActivity.setAgentId(t[0]);
            agentNibbsActivity.setBillsPaymentValue(Utility.formatForView(roundValueForData(t[1])));
            agentNibbsActivity.setBillsPaymentVolume(t[2]=="null"?"0":t[2]);
            agentNibbsActivity.setCashInValue(Utility.formatForView(roundValueForData(t[3])));
            agentNibbsActivity.setCashInVolume(t[4]=="null"?"0":t[4]);
            agentNibbsActivity.setCashOutValue(Utility.formatForView(roundValueForData(t[5])));
            agentNibbsActivity.setCashOutVolume(t[6]=="null"?"0":t[6]);
            agentNibbsActivity.setAirtimeValue(Utility.formatForView(roundValueForData(t[7])));
            agentNibbsActivity.setAirtimeVolume(t[8]=="null"?"0":t[8]);
            agentNibbsActivity.setDate(from.toString());
            agentNibbsActivity.setAgentCode(t[9]=="null"?"":t[9]);

            agentNibbsActivity.setAcctVolume("0");
            agentNibbsActivity.setFtValue("0");
            agentNibbsActivity.setFtVolume("0");

            agentNibbsActivities.add(agentNibbsActivity);
        }

        return agentNibbsActivities ;
    }


    double roundValueForData (String data ){

        if(Objects.isNull(data) || data.equals("null")){
            return 0.0;
        }

        return  Utility.round(Double.valueOf(data),2);
    }

    String escapeNulls(String data){
        if(Objects.isNull(data) || data.equals("null")){
            return "";
        }else{
            return data;
        }
    }


}
