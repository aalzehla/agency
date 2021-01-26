package com._3line.gravity.freedom.dashboard;



import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.models.Mandates;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.agents.repository.MandateRepository;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.billpayment.repository.BillPaymentRepo;
import com._3line.gravity.freedom.billpayment.service.Billservice;
import com._3line.gravity.freedom.commisions.repositories.GravityDailyCommssionRepo;
import com._3line.gravity.freedom.dashboard.models.Dashboard;
import com._3line.gravity.freedom.dashboard.models.TopAgent;
import com._3line.gravity.freedom.dashboard.models.TopBiller;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.transactions.service.TransactionService;
import com._3line.gravity.freedom.utility.DateUtil;
import com._3line.gravity.freedom.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class DashboardService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    private AgentService agentService;
    private TransactionService transactionService;
    private Billservice billservice;
    private BillPaymentRepo billPaymentRepo;
    private TransactionRepository transactionRepository;
    private AgentsRepository agentsRepository ;
    private GravityDailyCommssionRepo commssionRepo;
    private MandateRepository mandateRepository;

    @Autowired
    public DashboardService(AgentService agentService, TransactionService transactionService, Billservice billservice, BillPaymentRepo billPaymentRepo, TransactionRepository transactionRepository, AgentsRepository agentsRepository, GravityDailyCommssionRepo commssionRepo, MandateRepository mandateRepository) {
        this.agentService = agentService;
        this.transactionService = transactionService;
        this.billservice = billservice;
        this.billPaymentRepo = billPaymentRepo;
        this.transactionRepository = transactionRepository;
        this.agentsRepository = agentsRepository;
        this.commssionRepo = commssionRepo;
        this.mandateRepository = mandateRepository;
    }

    List<TopAgent> topPerforming(){
        List<TopAgent> result = new ArrayList<>();
        List<Object []> data = transactionRepository.topAgents() ;

        for (Object[] o: data) {
            TopAgent topAgent = new TopAgent();
            logger.info( "username {}",String.valueOf(o[0]) );
            Agents agents = agentsRepository.findByUsername(String.valueOf(o[0]));
            topAgent.setName( agents.getFirstName() + " " + agents.getLastName());
            topAgent.setAgentId(agents.getId());
            topAgent.setType(agents.getAgentType());
            topAgent.setTotalTran( formatexpo( Double.valueOf(String.valueOf(o[1]))));
            Mandates mandates = mandateRepository.findByApplicationUsers(agents);

            if(Objects.nonNull(mandates)){
                topAgent.setImage(mandates.getPicture());
            }
            result.add(topAgent);

        }

        return result ;
    }

    List<TopBiller> topBillers(){
        List<TopBiller> result = new ArrayList<>();
        List<Object[]> data =  billPaymentRepo.topBillers();
        for (Object [] s: data) {
            TopBiller topBiller = new TopBiller();
            topBiller.setCode(String.valueOf(s[0]));
            topBiller.setName(String.valueOf(s[1]));
            topBiller.setCount(String.valueOf(s[2]));
            result.add(topBiller);
        }
        return result ;
    }

    public Dashboard getRecords(){

//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Dashboard dashboard = new Dashboard();

        dashboard.setTopAgents(topPerforming());



        List<Object []> dashBoardGroupedTransactionCount = transactionRepository.getDashBoardTransactionGroupByType();

        Long totalTran = (long) 0;
        for(int a=0;a<dashBoardGroupedTransactionCount.size();a++){

            Object[] obj = dashBoardGroupedTransactionCount.get(a);

            String tranType = String.valueOf(obj[0]);
            int count = Integer.parseInt(String.valueOf(obj[2]));
            double amount = Double.parseDouble(String.valueOf(obj[1]));

            totalTran = totalTran + count;

            if(tranType.equalsIgnoreCase("Bill Payment")){
                dashboard.setTotalBillPayments(Long.valueOf(count));
            }else if(tranType.equalsIgnoreCase("Deposit")){
                dashboard.setTotalDeposits(Long.valueOf(count));
            }else if(tranType.equalsIgnoreCase("Withdrawal")){
                dashboard.setTotalWithdrawals(Long.valueOf(count));
            }else if(tranType.equalsIgnoreCase("Recharge")){
                dashboard.setTotalTransfers(Long.valueOf(count));
            }

        }

        dashboard.setTotalTransactions(totalTran);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDate = dateFormat.format(DateUtil.getStartOfDay(new Date()));
        String endDate = dateFormat.format(DateUtil.getEndOfDay(new Date()));

        System.out.println("using date:: "+startDate+" and "+endDate);

        List<Object []> dashBoardGroupedRecentTransactionCount = transactionRepository.getDashBoardTransactionByDateGroupByType(startDate,endDate);

        totalTran = (long) 0;

        for(int a=0;a<dashBoardGroupedRecentTransactionCount.size();a++){

            Object[] obj2 = dashBoardGroupedRecentTransactionCount.get(a);

            String tranType = String.valueOf(obj2[0]);
            int count = Integer.parseInt(String.valueOf(obj2[2]));
            double amount = Double.parseDouble(String.valueOf(obj2[1]));

            totalTran = totalTran + count;

            if(tranType.equalsIgnoreCase("Bill Payment")){
                dashboard.setDailyBillPayments(Long.valueOf(count));
            }else if(tranType.equalsIgnoreCase("Deposit")){
                dashboard.setDailyDeposit(Long.valueOf(count));
            }else if(tranType.equalsIgnoreCase("Withdrawal")){
                dashboard.setDailyWithdrawals(Long.valueOf(count));
            }else if(tranType.equalsIgnoreCase("Recharge")){
                dashboard.setDailyRecharge(Long.valueOf(count));
            }

        }

        dashboard.setDailyTransactions(totalTran);

        dashboard.setTopBillers(topBillers());

        List<String []>  today = Utility.getStringsFromObjectList(commssionRepo.getTotalCommissionFordate(DateUtil.formatForQuery(new Date()) , DateUtil.formatForQuery(DateUtil.AddDays(new Date(), 1))));
        List<String []>  yesterday = Utility.getStringsFromObjectList(commssionRepo.getTotalCommissionFordate(DateUtil.formatForQuery(DateUtil.yesterday()) , DateUtil.formatForQuery(DateUtil.AddDays(DateUtil.yesterday(), 1))));

        dashboard.setTotalAgentEarnings(roundValueForData("0.0"));//data.get(0)[0]));
        dashboard.setTotal3lineEarnings(roundValueForData("0.0"));//data.get(0)[1]));

        dashboard.setTotalAgentMonthEarnings(roundValueForData("0.0"));//thisMonth.get(0)[0]));
        dashboard.setTotal3lineEarningMonth(roundValueForData("0.0"));//thisMonth.get(0)[1]));
        dashboard.setTotalAgentpreviousMonthEarnings(roundValueForData("0.0"));//lastMonth.get(0)[0]));
        dashboard.setTotal3lineEarningPrevMonth(roundValueForData("0.0"));//lastMonth.get(0)[1]));

        dashboard.setIncomeToday(roundValueForData(today.size()==0?"0.00":today.get(0)[1]));
        dashboard.setIncomeYesterday(roundValueForData(yesterday.size()==0?"0.00":yesterday.get(0)[1]));

        return  dashboard ;
    }


    double roundValueForData (String data ){

        if(Objects.isNull(data) || data.equals("null")){
            return 0.0;
        }

        return  Utility.round(Double.valueOf(data),2);
    }

    public String formatexpo(double value) //Got here 6.743240136E7 or something..
    {
        DecimalFormat formatter;

        if(value - (int)value > 0.0)
            formatter = new DecimalFormat("0.00"); //Here you can also deal with rounding if you wish..
        else
            formatter = new DecimalFormat("0");

        return formatter.format(value);
    }


}
