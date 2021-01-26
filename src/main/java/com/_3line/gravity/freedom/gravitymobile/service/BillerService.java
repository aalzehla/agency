package com._3line.gravity.freedom.gravitymobile.service;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.setting.dto.SettingDTO;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.sms.service.SmsService;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import com._3line.gravity.freedom.billpayment.dtos.BillPaymentDto;
import com._3line.gravity.freedom.billpayment.dtos.CustomerDetails;
import com._3line.gravity.freedom.billpayment.dtos.ValidateCustomerRequest;
import com._3line.gravity.freedom.billpayment.dtos.ValidateCustomerResponse;
import com._3line.gravity.freedom.billpayment.models.BillCategory;
import com._3line.gravity.freedom.billpayment.models.BillOption;
import com._3line.gravity.freedom.billpayment.models.BillPayment;
import com._3line.gravity.freedom.billpayment.models.BillType;
import com._3line.gravity.freedom.billpayment.repository.BillOptionsRepo;
import com._3line.gravity.freedom.billpayment.repository.BillPaymentRepo;
import com._3line.gravity.freedom.billpayment.repository.BillTypeRepo;
import com._3line.gravity.freedom.billpayment.service.Billservice;
import com._3line.gravity.freedom.commisions.services.GravityDailyCommissionService;
import com._3line.gravity.freedom.financialInstitutions.opay.service.OpayService;
import com._3line.gravity.freedom.financialInstitutions.service.BankProcessor;
import com._3line.gravity.api.bills.dto.PayBills;
import com._3line.gravity.freedom.institution.dto.InstitutionDTO;
import com._3line.gravity.freedom.institution.service.InstitutionService;
import com._3line.gravity.freedom.transactions.models.Transactions;
import com._3line.gravity.freedom.transactions.repositories.TransactionRepository;
import com._3line.gravity.freedom.utility.PropertyResource;
import com._3line.gravity.freedom.utility.Utility;
import com._3line.gravity.freedom.wallet.dto.WalletDTO;
import com._3line.gravity.freedom.wallet.exceptions.WalletException;
import com._3line.gravity.freedom.wallet.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BillerService {

    @Autowired
    GravityDailyCommissionService commissionService;
    @Autowired
    BankDetailsService bankDetailsService;
    @Autowired
    @Qualifier("magtipon")
    BankProcessor magtiponProcessor;
    @Autowired
    OpayService opayService;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AgentsRepository applicationUsersRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ObjectMapper mapper ;
    @Autowired
    private ModelMapper modelMapper ;

    @Autowired
    private Billservice billservice;
    @Autowired
    private BillOptionsRepo billOptionsRepo ;

    @Autowired
    private GravityDailyCommissionService gravityDailyCommissionService ;


    @Autowired
    private WalletService walletService ;
    @Autowired
    private SettingService settingService ;
    @Autowired
    private BillPaymentRepo billPaymentRepo;

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    BillTypeRepo  billTypeRepo;

    @Qualifier("IPIntgratedSMSImplementation")
    @Autowired
    SmsService smsService;

    @Autowired
    PropertyResource pr;


    public Response getCategories(){

        Response response = new Response() ;

        List<BillCategory> categoryList = billservice.getAllCategories() ;
        try {
            response.setRespCode("00");
            response.setRespBody(categoryList);
        }catch (Exception e){

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespBody("Fatal error occured");
        }

        return response ;
    }


    public Response getServiceForCategory(Long categoryId){

        Response response = new Response() ;

        try {
            response.setRespCode("00");
            response.setRespBody(billservice.getServicesForCategory(categoryId));
        }catch (Exception e){

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespBody("Fatal error occured");
        }

        return response ;

    }

    public Response updateBillServices() {

        Response response = new Response();
        String respCode = "";
        try {
            respCode  = billservice.updateBillServices();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            response.setRespCode(respCode);
            response.setRespBody("Successful");
        } catch (Exception e) {

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespBody("Fatal error occurred");
        }

        return response;
    }

    public Response getOptionsForService(Long serviceId){
        Response response = new Response() ;

        try {
            response.setRespCode("00");
            response.setRespBody(billservice.getOptionsByServiceId(serviceId));
        }catch (Exception e){

            e.printStackTrace();
            response.setRespCode("999");
            response.setRespBody("Fatal error occured");
        }

        return response ;
    }



    public Response payBills(PayBills payBills){
        Response response = new Response() ;

        BillOption billOptionCheck = billOptionsRepo.findByCode(payBills.getPaymentCode());
        if(billOptionCheck == null){
            throw new GravityException("Invalid PaymentCode entered");

        }

        Agents agent = applicationUsersRepository.findByUsername(payBills.getAgentName());

        InstitutionDTO  institutionDTO = institutionService.getInstitutionByAgentId(agent.getParentAgentId());

        Map<String,String> billAmountWithCharge;

        if(institutionDTO!=null){
            billAmountWithCharge = gravityDailyCommissionService.getInstBillAmountWithCharge
                    (Double.parseDouble(payBills.getAmount()),billOptionCheck,institutionDTO);
        }else{
            billAmountWithCharge = gravityDailyCommissionService.getBillAmountWithCharge
                    (Double.parseDouble(payBills.getAmount()),billOptionCheck,null);
        }

        Double charge = Double.valueOf(billAmountWithCharge.get("charge"));



//        BillPayment saved = new BillPayment() ;

        BillPaymentDto billPaymentDto = modelMapper.map(payBills , BillPaymentDto.class);

        BillPayment tosave = modelMapper.map(billPaymentDto , BillPayment.class) ;
        tosave.setCustomerName(payBills.getCustomerName());
        tosave.setCustomerEmail(payBills.getEmail());
        tosave.setAgentName(payBills.getAgentName());
        tosave.setCustomerPhone(payBills.getPhoneNumber());

        tosave.setBiller(billOptionCheck.getOptionName());
        Transactions transactions = new Transactions();
        transactions.setTranDate(new Date());
        transactions.setInnitiatorId(agent.getId());
        transactions.setApproval(0);
        transactions.setAmount(Double.valueOf(payBills.getAmount()));
        transactions.setBeneficiary(payBills.getCustomerId());
        transactions.setLatitude("");
        transactions.setLongitude("");
        transactions.setTerminalId("");

        transactions.setStatusdescription("SUCCESSFUL");
        transactions.setStatus((short) 0);
        transactions.setDescription("Processing");
        transactions.setBankFrom(agent.getBankCode());
        transactions.setBankTo(billOptionCheck.getOptionName());
        transactions.setAccountNumber(agent.getAccountNo());
        transactions.setAccountNumberTo(payBills.getCustomerId());
        transactions.setAgentName(payBills.getAgentName());
        transactions.setCustomerName(payBills.getCustomerName());
        transactions.setDepositorName(payBills.getCustomerName());
        transactions.setDepositorEmail(payBills.getEmail());
        transactions.setDepositorPhone(payBills.getPhoneNumber());
        transactions.setDescription("Bill Payment / Recharge");
        transactions.setMedia("MOBILE");


        transactions.setTransactionTypeDescription(billOptionCheck.getServiceOption().getServiceName());

        BillType  billType = billTypeRepo.findByCategoryCode(String.valueOf(billOptionCheck.getServiceOption().getCategory()));
        tosave.setBillType(billType.getCategoryName());

        if(billOptionCheck.getServiceOption().getRecharge()){
            transactions.setTransactionType("Recharge");
            tosave.setRecharge(BillPayment.Recharge.YES);
        }else{
            transactions.setTransactionType("Bill Payment");
        }

        Transactions transaction = transactionRepository.save(transactions);

        tosave.setTransactionId(String.valueOf(transaction.getTranId()));
        billPaymentDto.setRequestRef(String.valueOf(transaction.getTranId()));
        tosave = billPaymentRepo.save(tosave);

        try {

            CustomerDetails customerDetails = new CustomerDetails();
            customerDetails.setEmail(payBills.getEmail());
            customerDetails.setFullname(payBills.getCustomerName());
            customerDetails.setMobilePhone(payBills.getPhoneNumber());
            billPaymentDto.setCustomerDetails(customerDetails);


            SettingDTO magtiponAccount = settingService.getSettingByCode("MAGTIPON_GL_WALLET");
            walletService.debitWallet(String.valueOf(transaction.getTranId()),agent.getWalletNumber(), Double.valueOf(payBills.getAmount()) + charge, "MOBILE", "BILL PAYMENT " + billOptionCheck.getServiceOption().getServiceName() + "" + billOptionCheck.getOptionName());

            logger.info("Bill payment  -- > {}", billPaymentDto.toString());

            BillPaymentDto res = billservice.payBills(billPaymentDto);


            tosave.setTransactionRef(res.getTransactionRef());
            tosave.setTranRespPayload(new Gson().toJson(res));
            tosave.setResponseCode(res.getResponseCode());
            tosave.setPin(res.getPin());

            transaction.setTransactionReference(res.getTransactionRef());

            if (res.getResponseCode().equals("90000")) {
                response.setRespCode("00");
                //commission will be generated and split here
                if(institutionDTO!=null){
                    gravityDailyCommissionService.generateInstBillPaymentCommission(Double.valueOf(billPaymentDto.getAmount()),charge, agent, billOptionCheck.getServiceOption().getRecharge(), "BILL PAYMENT [" + billOptionCheck.getOptionName() + "]",institutionDTO);
                }else{
                    gravityDailyCommissionService.generateBillPaymentCommission(Double.valueOf(billPaymentDto.getAmount()),charge, agent, billOptionCheck.getServiceOption().getRecharge(), "BILL PAYMENT [" + billOptionCheck.getOptionName() + "]");
                }
                tosave.setStatus(BillPayment.Status.DONE);
                transaction.setStatusdescription("SUCCESSFUL");
                transaction.setStatus((short) 1);
                transactionRepository.save(transaction);
                logger.info("Bill Type is actually :: " + billType.getCategoryName());
                String message;
                if (billType.getCategoryName().equalsIgnoreCase("Utilities Bills")) {
                    if (billOptionCheck.getServiceOption().getServiceName().toLowerCase().contains("prepaid")) {
                        String template = pr.getV("agent.utility.pre_paid.success", "sms_messages.properties");
                        message = String.format(template,
                                billOptionCheck.getServiceOption().getServiceName(), tosave.getPin(),tosave.getTransactionRef());

                    } else {
                        String template = pr.getV("agent.utility.post_paid.success", "sms_messages.properties");
                        message = String.format(template,
                                billOptionCheck.getServiceOption().getServiceName(),tosave.getTransactionRef());
                    }
                    smsService.sendPlainSms(payBills.getPhoneNumber(), message);

                } else if (billType.getCategoryName().equalsIgnoreCase("Cable TV")) {
                    String template = pr.getV("agent.cable_tv.success", "sms_messages.properties");
                    message = String.format(template,
                            billOptionCheck.getServiceOption().getServiceName(), payBills.getCustomerId(),tosave.getTransactionRef());

                    smsService.sendPlainSms(payBills.getPhoneNumber(), message);

                }
            } else {
                response.setRespCode(res.getResponseCode());
                walletService.creditWallet(null,agent.getWalletNumber(), Double.valueOf(payBills.getAmount()) + charge, "MOBILE", "BILL PAYMENT REFUND " + billOptionCheck.getServiceOption().getServiceName() + "" + billOptionCheck.getOptionName());
                tosave.setStatus(BillPayment.Status.FAILED);
                transaction.setStatusdescription("FAILED");
                transactions.setStatus((short) 2);
                transactionRepository.save(transaction);
            }

            billPaymentRepo.save(tosave);
            Map data = new HashMap();
            WalletDTO trading = walletService.getWalletByNumber(agent.getWalletNumber());
            data.put("trading", trading.getAvailableBalance());
            WalletDTO income = walletService.getWalletByNumber(agent.getIncomeWalletNumber());
            data.put("income", income.getAvailableBalance());
            data.put("pin", res.getPin());
            data.put("ref", res.getTransactionRef());
            response.setRespBody(data);
            response.setRespDescription(res.getResponseDescription());

        }catch(WalletException e){
            e.printStackTrace();
            tosave.setStatus(BillPayment.Status.FAILED);
            billPaymentRepo.save(tosave);
            transaction.setStatusdescription("FAILED");
            transactions.setStatus((short) 2);
            transactionRepository.save(transaction);
            response.setRespCode("96");
            response.setRespDescription(e.getMessage());
        }catch (Exception e){
            tosave.setStatus(BillPayment.Status.FAILED);
            billPaymentRepo.save(tosave);
            e.printStackTrace();
            response.setRespCode("100");
            response.setRespDescription(e.getMessage());
        }

        return response ;
    }

    public Response validate(ValidateCustomerRequest request,Agents agent){
        Response response = new Response() ;
        try {



            ValidateCustomerResponse customerResponse = billservice.validate(request,agent);
            if(customerResponse.getResponseCode().equals("90000")){
                response.setRespCode("00");
                response.setRespDescription("success");
                response.setRespBody(customerResponse);
            }else{
                response.setRespCode("96");
                response.setRespDescription(customerResponse.getResponseDescription());
            }

        }catch (Exception e){
            e.printStackTrace();
            response.setRespCode("999");
            response.setRespBody("Fatal error occurred");
        }
       return response ;
    }

}
