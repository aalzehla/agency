package com._3line.gravity.freedom.dispute.service.impl;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.email.service.MailService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.usermgt.service.ApplicationUserService;
import com._3line.gravity.core.utils.AppUtility;
import com._3line.gravity.core.verification.annotations.RequireApproval;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.model.TransactionType;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.commisioncharge.service.CommissionChargeService;
import com._3line.gravity.freedom.dispute.dtos.DisputeDto;
import com._3line.gravity.freedom.dispute.dtos.DisputeLogDTO;
import com._3line.gravity.freedom.dispute.models.Dispute;
import com._3line.gravity.freedom.dispute.models.DisputeLog;
import com._3line.gravity.freedom.dispute.models.DisputeType;
import com._3line.gravity.freedom.dispute.repository.DisputeLogRepository;
import com._3line.gravity.freedom.dispute.repository.DisputeRepository;
import com._3line.gravity.freedom.dispute.service.DisputeLogService;
import com._3line.gravity.freedom.institution.dto.InstitutionDTO;
import com._3line.gravity.freedom.institution.service.InstitutionService;
import com._3line.gravity.freedom.issuelog.models.IssueLog;
import com._3line.gravity.freedom.issuelog.models.IssueStatus;
import com._3line.gravity.freedom.issuelog.service.IssueLogService;
import com._3line.gravity.freedom.itexintegration.model.PtspDto;
import com._3line.gravity.freedom.itexintegration.model.PtspModel;
import com._3line.gravity.freedom.itexintegration.repository.PtspRepository;
import com._3line.gravity.freedom.itexintegration.service.PtspService;
import com._3line.gravity.freedom.notifications.dtos.NotificationDTO;
import com._3line.gravity.freedom.notifications.service.MobileNotificationService;
import com._3line.gravity.freedom.transactions.dtos.TransactionsDto;
import com._3line.gravity.freedom.transactions.models.TranChannel;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TranChannelsRepository;
import com._3line.gravity.freedom.transactions.service.TranChannelService;
import com._3line.gravity.freedom.transactions.service.TransactionService;
import com._3line.gravity.freedom.wallet.models.FreedomWallet;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class DisputeLogServiceImpl implements DisputeLogService {

    private final Locale locale = LocaleContextHolder.getLocale();
    @Autowired
    DisputeLogRepository disputeLogRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    MobileNotificationService notificationService;

    @Autowired
    JwtUtility jwtUtility;

    @Autowired
    EntityManager entityManager;

    @Autowired
    IssueLogService  issueLogService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    DisputeRepository disputeRepository;

    @Autowired
    WalletService walletService;

    @Autowired
    PtspService ptspService;

    @Autowired
    AgentService agentService;

    @Autowired
    BankDetailsService bankDetailsService;

    @Autowired
    TranChannelsRepository tranChannelsRepository;

    @Autowired
    PtspRepository ptspRepository;

    @Autowired
    InstitutionService institutionService;

    @Autowired
    private CommissionChargeService commissionCharge;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void logDisputeComplaint(DisputeLog o,Long issueLogId) {
        if(o !=null){
            Agents agent = jwtUtility.getCurrentAgent();


            Dispute dispute = new Dispute();
            dispute.setLoggedBy(agent.getUsername());
            dispute.setType(DisputeType.WITHDRAWAL.toString());
            dispute.setAmount(String.valueOf(o.getTranAmount()));
            dispute.setAction("CREDIT");
            dispute.setAgentName(agent.getUsername());
            dispute.setWalletNumber(agent.getWalletNumber());

            dispute = disputeRepository.save(dispute);

            TransactionsDto transactionsDto = transactionService.getTransactionBytranID(o.getTranId());
            if(transactionsDto!=null){
                o.setRrn(transactionsDto.getItexTranId());
                o.setTerminalId(transactionsDto.getTerminalId());
                o.setRrn(transactionsDto.getItexTranId());
                o.setPan(transactionsDto.getMaskedPan());
                o.setTranStatus(transactionsDto.getStatusdescription());
            }
            o.setDispute(dispute);
            o = disputeLogRepository.save(o);
            if(issueLogId!=null && issueLogId > 0){
                issueLogService.linkToDisputeLog(issueLogId,o);
            }


        }else{
            throw new GravityException("Null object canot be saved");
        }
    }

    @RequireApproval(code = "WALLET_DISPUTE" , entityType = DisputeLog.class)
    @Override
    public String raiseWalletDispute(DisputeLogDTO disputeLogDTO) {

//        System.out.println("inputss :: "+disputeLogDTO);

        Agents agent = agentService.getAgentByTerminalId(disputeLogDTO.getTerminalId());
        String comment = disputeLogDTO.getRemark();
        String rrn;
        Date tranDate;


        if(disputeLogDTO.getLoggedBy()==null || disputeLogDTO.getLoggedBy().equalsIgnoreCase("SYSTEM")){
            Dispute dispute = new Dispute();
            dispute.setLoggedBy("SYSTEM");
            dispute.setType(DisputeType.WITHDRAWAL.toString());
            dispute.setAmount(String.valueOf(disputeLogDTO.getTranAmount()));
            dispute.setAction("CREDIT");
            dispute.setAgentName(agent.getUsername());
            dispute.setWalletNumber(agent.getWalletNumber());

            PtspDto  dto = new PtspDto();
            dto.setReversal("false");
            dto.setStatusCode("00");
            dto.setAmount(disputeLogDTO.getTranAmount()*100);
            dto.setBank(disputeLogDTO.getTerminalId().substring(1,4));
            dto.setPan(disputeLogDTO.getPan());
            dto.setRrn(disputeLogDTO.getRrn());
            rrn = dto.getRrn();
            tranDate = disputeLogDTO.getTranDate();
            dto.setStan("");
            dto.setTerminalId(disputeLogDTO.getTerminalId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
            dto.setTransactionTime(dateFormat.format(disputeLogDTO.getTranDate()));
            dto.setProductId(disputeLogDTO.getChannel());
            try{
                List<PtspModel> ptspModels = ptspRepository.findByRrnAndTerminalId(dto.getRrn(),dto.getTerminalId());
                if(ptspModels.size()>0){
                    throw new GravityException("Withdrawal Transaction already exist");
                }
                ptspService.savePtspDetails(dto,null);

            }catch(Exception e){
                e.printStackTrace();
                throw new GravityException("Error: "+e.getMessage());
            }

            dispute.setComment(comment);
            dispute.setRaisedBy(disputeLogDTO.getRaisedBy());
            dispute.setApprovedBy(AppUtility.getCurrentUserName());
            disputeRepository.save(dispute);

        }else{
            //do commission settling and agent crediting
            List<PtspModel> ptspModels = ptspRepository.findByRrnAndTerminalId(disputeLogDTO.getRrn(), disputeLogDTO.getTerminalId());

            PtspModel ptspModel = ptspModels.get(0);

            Double tranFee = getTranFee(ptspModel, agent, disputeLogDTO.getTranAmount());

            ptspService.doCommission(agent,ptspModel,disputeLogDTO.getTranAmount(),tranFee,disputeLogDTO.getTranId(),true);

            //Update dispute log
            DisputeLog disputeLog = disputeLogRepository.findOne(disputeLogDTO.getId());

            Dispute dispute = disputeLog.getDispute();

//            comment = "Dispute for: "+dispute.getAction()+" Tran Id: "+disputeLogDTO.getTranId()+", Amount : "+dispute.getAmount();

            dispute.setApprovedBy(AppUtility.getCurrentUserName());
            disputeRepository.save(dispute);

            //Update issue log
            IssueLog issueLog = issueLogService.findByDisputeLogId(disputeLogDTO.getId());
            issueLog.setStatus(IssueStatus.RESOLVED);
            issueLog.setTreatedBy(disputeLogDTO.getRaisedBy());
            issueLog.setTreatedDate(new Date());
            issueLogService.updateIssueLog(issueLog);

            ///transaction update to show succesful
            Transactions transaction = transactionService.getTransaction(disputeLogDTO.getTranId());
            transaction.setStatus((short) 1);
            transaction.setStatusdescription("SUCCESSFUL");
            transaction.setStatcode("00");

            rrn = transaction.getItexTranId();
            tranDate = transaction.getTranDate();

            TransactionsDto transactionsDto = modelMapper.map(transaction,TransactionsDto.class);
            transactionService.updateTransaction(transactionsDto);


        }

        try{
            NotificationDTO notification = new NotificationDTO();

            comment = String.format("The failed withdrawal you made on the %s with RRN: %s " +
                    "has been credited to your trading wallet.",formatDate(tranDate),rrn);


            notification.setMessage(comment);
            notification.setAgentName(agent.getUsername());
            notification.setAgentPhone(agent.getPhoneNumber());
            notification.setAgentEmail(agent.getEmail());
            notification.setCategory("System Notification");
            notification.setMessageType("Notification");
            notificationService.logNotification(notification);
        }catch(Exception e){
            e.printStackTrace();
        }

        return "dispute.add.success";

    }

    @Override
    public void validateDisputeLog(long tranId) {
        List<DisputeLog> check = disputeLogRepository.findByTranId(tranId);
        if( check.size() >0 ){
            throw new GravityException("Dispute already raised");
        }
    }


    private Double  getTranFee(PtspModel ptspModel, Agents agent, Double realtranAmount) {

        Double tranFee;
        InstitutionDTO institutionDTO = isAnInstitutionDownlIne(agent);

        if(institutionDTO!=null) {
            tranFee = commissionCharge.getInstCommissionForAmount(realtranAmount, TransactionType.WITHDRAWAL, institutionDTO.getName());
        }else{
            String channelType = ptspModel.getProductId();
            TranChannel tranChannel;
            if(channelType.equalsIgnoreCase("INTERSWITCH")){
                tranChannel = tranChannelsRepository.findByChannelName(channelType);
            }else{
                tranChannel = tranChannelsRepository.findByChannelName("NIBSS");
            }

            if(agent.getAgentType().equalsIgnoreCase("SOLE") && agent.getPosTerminalTranFee() != null ){
                tranFee = Double.valueOf(agent.getPosTerminalTranFee()) / Double.parseDouble("100") * realtranAmount;
            }else{
                tranFee = Double.valueOf(tranChannel.getPosTerminalPercentageFee()) / Double.parseDouble("100") * realtranAmount;
            }

            if(tranFee < tranChannel.getMinimumPosTerminalFee()){
                tranFee = tranChannel.getMinimumPosTerminalFee();
            }else if(tranFee > tranChannel.getMaxPosTerminalFee()){
                tranFee = tranChannel.getMaxPosTerminalFee();
            }
        }

        this.logger.info("before Fee to be deducted from amount is :: "+(tranFee));

        tranFee = new BigDecimal(Double.toString(tranFee)).setScale(2, RoundingMode.HALF_UP).doubleValue();


        return tranFee;
    }

    InstitutionDTO isAnInstitutionDownlIne(Agents agent){

        InstitutionDTO  institutionDTO  = null;
        if(agent.getParentAgentId()!=null && agent.getParentAgentId() > 0){
            institutionDTO = institutionService.getInstitutionByAgentId(agent.getParentAgentId());
            if(institutionDTO!=null){
                return institutionDTO;
            }
        }
        System.out.println("is not an institution");
        return institutionDTO;
    }


    private String formatDate(Date date){

        String[] suffixes =
                //    0     1     2     3     4     5     6     7     8     9
                { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                        //    10    11    12    13    14    15    16    17    18    19
                        "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                        //    20    21    22    23    24    25    26    27    28    29
                        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                        //    30    31
                        "th", "st" };

        SimpleDateFormat formatDayOfMonth  = new SimpleDateFormat("d");
        SimpleDateFormat formatMonthOfYear  = new SimpleDateFormat("MMMMM");
        int day = Integer.parseInt(formatDayOfMonth.format(date));

        String finalDate = day + suffixes[day]+" of "+formatMonthOfYear.format(date);

        return finalDate;
    }
}
