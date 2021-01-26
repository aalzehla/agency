package com._3line.gravity.api.utility;

import com._3line.gravity.api.auth.dto.LoginRespDto;
import com._3line.gravity.freedom.NIBBS.dto.AgentDataReportDTO;
import com._3line.gravity.freedom.NIBBS.dto.TransactionReportDTO;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.billpayment.models.BillPayment;
import com._3line.gravity.freedom.billpayment.repository.BillPaymentRepo;
import com._3line.gravity.freedom.reports.dtos.AgentNibbsActivity;
import com._3line.gravity.freedom.transactions.dtos.DepositDTO;
import com._3line.gravity.freedom.transactions.dtos.TransactionHistoryRespDTO;
import com._3line.gravity.freedom.transactions.dtos.UtiltyDTO;
import com._3line.gravity.freedom.transactions.dtos.WithdrawalDTO;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.wallet.dto.WalletDTO;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@Service
public class GeneralUtil {

    @Autowired
    WalletService walletService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BillPaymentRepo billPaymentRepo;

    @Autowired
    BankDetailsService bankDetailsService;

    public LoginRespDto converttodto(Agents agents) {

        LoginRespDto respDto = new LoginRespDto();
        respDto.setAddress(agents.getAddress());
        respDto.setCity(agents.getCity());
        respDto.setLga(agents.getLga());
        respDto.setAgentName(agents.getUsername());
        respDto.setAgentId(agents.getAgentId());
        respDto.setFullName(agents.getFirstName() + " " + agents.getLastName());

        respDto.setTerminalId(agents.getTerminalId());
        respDto.setUsername(agents.getUsername());
        respDto.setPhoneNumber(agents.getPhoneNumber());
        respDto.setIncomeNumber(agents.getIncomeWalletNumber());
        respDto.setAccountNo(agents.getAccountNo());
        respDto.setTerminalId(agents.getTerminalId());
        respDto.setState(agents.getState());
        respDto.setActivated(agents.getActivated() == 1 ? "1" : "0");
        respDto.setWalletNumber(agents.getWalletNumber());
        respDto.setUserType(agents.getAgentType());

        WalletDTO walletDTO = walletService.getWalletByNumber(agents.getWalletNumber());
        WalletDTO income = walletService.getWalletByNumber(agents.getIncomeWalletNumber());
        if (walletDTO != null) {
            respDto.setWalletBalance(String.format("%.2f",walletDTO.getAvailableBalance()) );
        }

        if (income != null) {
            respDto.setIncomeBalance(String.format("%.2f",income.getAvailableBalance()));
        }

        return respDto;
    }

    public AgentDataReportDTO converttoagentdatareportdto(Agents agents){

        AgentDataReportDTO dataReportDTO = new AgentDataReportDTO();

        DecimalFormat decimalFormat = new DecimalFormat("#.####");

        dataReportDTO.setLastName(agents.getLastName());
        dataReportDTO.setFirstName(agents.getFirstName());
        dataReportDTO.setMiddleName(agents.getMiddleName());
        dataReportDTO.setEmailAddress(agents.getEmail());
        dataReportDTO.setBvn(agents.getBvn());
        dataReportDTO.setAgentCode(agents.getAgentCode());
        dataReportDTO.setCity(agents.getState());
        dataReportDTO.setAdditionalInfo1("");
        dataReportDTO.setAdditionalInfo2("");
        dataReportDTO.setLatitude(Double.parseDouble(decimalFormat.format(6.1234)));
        dataReportDTO.setLongitude(Double.parseDouble(decimalFormat.format(3.0012)));
        dataReportDTO.setLga(agents.getLga());
        dataReportDTO.setState(agents.getState());
        dataReportDTO.setPassword("Pass@123foon");
        dataReportDTO.setTitle("Mr");
        dataReportDTO.setUsername(agents.getUsername());
        dataReportDTO.setWard(agents.getWards());
        String[] phoneArray = new String[1];
        phoneArray[0] = agents.getPhoneNumber();
        dataReportDTO.setPhoneList(phoneArray);

        String[] services = new String[1];
        services[0] = "CASH_IN";
        dataReportDTO.setServicesProvided(services);
        dataReportDTO.setStreetDescription(agents.getAddress());
        dataReportDTO.setStreetName(agents.getAddress());
        dataReportDTO.setStreetNumber(agents.getAddress());

        return dataReportDTO;
    }

    public List<TransactionReportDTO> converttotransactionreportdto(List<AgentNibbsActivity> activities){
        List<TransactionReportDTO> reportDTOS = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        activities.forEach(agentNibbsActivity -> {
            TransactionReportDTO reportDTO = new TransactionReportDTO();
            reportDTO.setAccountOpeningCount(agentNibbsActivity.getAcctVolume());
            reportDTO.setAccountOpeningValue("0");
            reportDTO.setAdditionalService1Count("0");
            reportDTO.setAdditionalService1Value("0");
            reportDTO.setAdditionalService2Count("0");
            reportDTO.setAdditionalService2Value("0");
            reportDTO.setAirtimeRechargeCount(agentNibbsActivity.getAirtimeVolume());
            reportDTO.setAirtimeRechargeValue(agentNibbsActivity.getAirtimeValue());
            reportDTO.setBillsPaymentCount(agentNibbsActivity.getBillsPaymentVolume());
            reportDTO.setBillsPaymentValue(agentNibbsActivity.getBillsPaymentValue());
            reportDTO.setBvnEnrollmentCount("0");
            reportDTO.setBvnEnrollmentValue("0");
            reportDTO.setCashInCount(agentNibbsActivity.getCashInVolume());
            reportDTO.setCashInValue(agentNibbsActivity.getCashInValue());
            reportDTO.setCashOutCount(agentNibbsActivity.getCashOutVolume());
            reportDTO.setCashOutValue(agentNibbsActivity.getCashOutValue());
//            reportDTO.setTransactionDate(dateFormat.parse(agentNibbsActivity.getDate()) );//yyyy-mm-dd
            reportDTO.setFundTransferCount(agentNibbsActivity.getFtVolume());
            reportDTO.setFundTransferValue(agentNibbsActivity.getFtValue());


            reportDTO.setAgentCode(agentNibbsActivity.getAgentCode());//99988498

            if(agentNibbsActivity.getAgentCode()!=null && !agentNibbsActivity.getAgentCode().equals("")){
                reportDTOS.add(reportDTO);
            }

        });

        return reportDTOS;
    }

    public List<TransactionHistoryRespDTO> converttodto(List<Transactions> transactions) {
        List<TransactionHistoryRespDTO> resp = new ArrayList<TransactionHistoryRespDTO>();
        for (Transactions t : transactions) {
            TransactionHistoryRespDTO historyRespDTO = modelMapper.map(t, TransactionHistoryRespDTO.class);

            Calendar calender = Calendar.getInstance();
            calender.setTime(t.getTranDate());
            historyRespDTO.setItexTranId(t.getItexTranId());

            BankDetails bankDetails = bankDetailsService.findByCode(t.getBankTo());
            historyRespDTO.setBankTo(bankDetails==null?t.getBankTo():bankDetails.getBankName());

            bankDetails = bankDetailsService.findByCode(t.getBankFrom());
            historyRespDTO.setBankFrom(bankDetails==null?t.getBankFrom():bankDetails.getBankName());


            if (t.getTransactionType().trim().equalsIgnoreCase("bill payment") ||
                    t.getTransactionType().trim().equalsIgnoreCase("recharge")) {

                UtiltyDTO utiltyDTO = new UtiltyDTO();
                utiltyDTO.setAccountName(t.getCustomerName());
                utiltyDTO.setPaymentPlan("Prepaid");
                utiltyDTO.setPaidWith("Wallet");
                utiltyDTO.setTranDate(t.getTranDate());
                utiltyDTO.setAmount(t.getAmount());
                utiltyDTO.setTransactionRef(String.valueOf(t.getTranId()));
                if(t.getTransactionReference()!=null){
                    utiltyDTO.setTransactionRef(t.getTransactionReference());
                }
                BillPayment billPayment = billPaymentRepo.findByTransactionId(String.valueOf(t.getTranId()));
                if(billPayment!=null){
                    utiltyDTO.setElectricityToken(billPayment.getPin());
                    utiltyDTO.setTransactionRef(billPayment.getTransactionRef());
                }
                historyRespDTO.setUtilityDTO(utiltyDTO);

            }else if (t.getTransactionType().trim().toLowerCase().equals("deposit")) {
                DepositDTO depositDTO = new DepositDTO();
                depositDTO.setRecipientName(t.getCustomerName());
                depositDTO.setAgentCommission("NA");
                if (t.getFee() == null) {
                    depositDTO.setFreedomCharge(0.00);
                } else {
                    depositDTO.setFreedomCharge(Double.valueOf(t.getFee()));
                }
                depositDTO.setRecipientAccount(t.getAccountNumberTo());
                depositDTO.setRecipientBank(t.getBankTo());
                depositDTO.setTranDate(t.getTranDate());
                depositDTO.setAmount(t.getAmount());
                depositDTO.setTransactionRef(t.getTransactionReference());

                historyRespDTO.setDepositDTO(depositDTO);
            }else if ( t.getTransactionType().trim().toLowerCase().equals("withdrawal") ){
                WithdrawalDTO withdrawalDTO  = new WithdrawalDTO();
                withdrawalDTO.setAmount(historyRespDTO.getAmount());
                withdrawalDTO.setCardNumber(historyRespDTO.getMaskedPan());
                withdrawalDTO.setPaidWith("Card");
//                withdrawalDTO.setSourceBank(historyRespDTO.getBankFrom());
//                withdrawalDTO.setSourceAcctName(historyRespDTO.getCustomerName());
                withdrawalDTO.setStan(historyRespDTO.getStan());
                withdrawalDTO.setTranDate(historyRespDTO.getTranDate());
                withdrawalDTO.setTransactionRef(historyRespDTO.getItexTranId());
                historyRespDTO.setWithdrawalDTO(withdrawalDTO);
            }
            resp.add(historyRespDTO);
        }
        return resp;
    }
}