package com._3line.gravity.freedom.agents.service.impl;

import com._3line.gravity.api.auth.dto.LoginRespDto;
import com._3line.gravity.api.users.agents.dto.*;
import com._3line.gravity.api.auth.dto.LoginDto;
import com._3line.gravity.api.users.aggregator.dto.AggregatorAgentDTO;
import com._3line.gravity.api.users.aggregator.dto.CurrentStats;
import com._3line.gravity.api.users.aggregator.dto.TotalDaysStats;
import com._3line.gravity.api.users.aggregator.dto.AggregatorDashBoardDTO;
import com._3line.gravity.api.utility.GeneralUtil;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.core.otp.services.OtpService;
import com._3line.gravity.core.security.jwt.JwtTokenUtil;
import com._3line.gravity.core.usermgt.exception.UserNotFoundException;
import com._3line.gravity.core.verification.models.Verification;
import com._3line.gravity.core.verification.models.VerificationStatus;
import com._3line.gravity.core.verification.repository.VerificationRepository;
import com._3line.gravity.freedom.NIBBS.service.AgentDataReportService;
import com._3line.gravity.freedom.agents.dtos.*;
import com._3line.gravity.freedom.agents.service.SecurityDao;
import com._3line.gravity.core.email.service.MailService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.files.service.FileService;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.sms.service.SmsService;
import com._3line.gravity.core.utils.AppUtility;
import com._3line.gravity.core.utils.EmailValidator;
import com._3line.gravity.core.utils.PhoneNumberValidator;
import com._3line.gravity.core.verification.annotations.RequireApproval;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.models.Devicesetup;
import com._3line.gravity.freedom.agents.models.Mandates;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.agents.repository.DeviceSetupRepository;
import com._3line.gravity.freedom.agents.repository.MandateRepository;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.repository.BankDetailsRepository;
import com._3line.gravity.freedom.device.service.DeviceService;
import com._3line.gravity.freedom.reports.dtos.AgentPerformance;
import com._3line.gravity.freedom.reports.service.ReportService;
import com._3line.gravity.freedom.transactions.service.TransactionService;
import com._3line.gravity.freedom.utility.DateUtil;
import com._3line.gravity.freedom.utility.MD5;
import com._3line.gravity.freedom.utility.PropertyResource;
import com._3line.gravity.freedom.utility.Utility;
import com._3line.gravity.freedom.wallet.dto.WalletDTO;
import com._3line.gravity.freedom.wallet.models.FreedomWallet;
import com._3line.gravity.freedom.wallet.models.FreedomWalletTransaction;
import com._3line.gravity.freedom.wallet.repository.FreedomWalletRepository;
import com._3line.gravity.freedom.wallet.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mapper.excel.ExcelMapper;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.*;

//@CacheConfig(cacheNames = "agents")
@Service
public class AgentServiceImplementation implements AgentService {


    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MessageSource messageSource;

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private WalletService walletService;
    @Autowired
    private AgentsRepository agentsRepository;
    @Autowired
    private DeviceSetupRepository deviceSetupRepository;
    @Autowired
    private MandateRepository mandateRepository;
    @Autowired
    private SettingService settingService;
    @Autowired
//    private PasswordEncoder passwordEncoder;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Qualifier("IPIntgratedSMSImplementation")
    @Autowired
    private SmsService smsService;

    @Autowired
    private FileService fileService;

    @Autowired
    private PropertyResource pr;

    @Autowired
    private SecurityDao dao;

    @Autowired
    private ReportService reportService;


    @Autowired
    DeviceService deviceService;

    @Autowired
    OtpService  otpService;

    @Autowired
    GeneralUtil  generalUtil;

    @Autowired
    AgentDataReportService  dataReportService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    JwtTokenUtil  jwtTokenUtil;

    @Autowired
    BankDetailsRepository  bankDetailsRepository;

    @Autowired
    VerificationRepository verificationRepository;

    @Autowired
    FreedomWalletRepository freedomWalletRepository;


    private static Logger logger = LoggerFactory.getLogger(AgentService.class);

    @RequireApproval(code = "CREATE_AGENT", entityType = Agents.class)
    @Override
    public String createNewAgent(AgentDto agentDto) throws GravityException {

        logger.debug("Creating Agent: {}", agentDto);
        createAgent(agentDto);
        return "agent.add.success";
    }


    @RequireApproval(code = "CREATE_AGENT", entityType = Agents.class)
    @Override
    public Map<String,Object> createAggregatorNewAgent(AgentDto agentDto) throws GravityException {

        logger.debug("Creating Agent: {}", agentDto);
        return createAgent(agentDto);
    }

    private Map<String, Object> createAgent(AgentDto agentDto){

        logger.info("Creating Agent: {}", agentDto);

        try {

            agentDto.setTerminalId(agentDto.getTerminalId().trim());

            agentDto.setFirstName(agentDto.getFirstName().trim());
            agentDto.setLastName(agentDto.getLastName().trim());
            if( agentDto.getTerminalId() != null && !agentDto.getTerminalId().trim().equals("")){
                BankDetails bankDetails = bankDetailsRepository.findByCbnCode(agentDto.getTerminalId().trim().substring(1,4));
                if(bankDetails !=null){
                    agentDto.setBankCode(bankDetails.getBankCode());
                }
            }

            validateNewAgent(agentDto);

            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            Agents applicationUsers = modelMapper.map(agentDto, Agents.class);
            applicationUsers.setEmail(agentDto.getEmail());
            applicationUsers.setPhoneNumber(agentDto.getPhoneNumber());
            applicationUsers.setAccountNo(agentDto.getAccountNo());
            if(applicationUsers.getUsername() == null) {
                applicationUsers.setUsername(agentDto.getFirstName() + "." + agentDto.getLastName());
            }
            applicationUsers.setDob(DateUtil.dateFullFormatYYMMdd(agentDto.getDateOfBirth()));
            applicationUsers.setPresentAddressDateOfEntry(DateUtil.dateFullFormatYYMMdd(agentDto.getBuisnessLocationEntryDate()));

            if(agentDto.getLga()!=null) {
                applicationUsers.setLga(agentDto.getLga().toUpperCase());
            }
            if(applicationUsers.getState()!=null) {
                applicationUsers.setState(agentDto.getState().toUpperCase());
            }
            applicationUsers.setComAccountNo(agentDto.getAccountNo());
            String wal = walletService.createWallet(null, "AGENT_BANKING_TRADING_ACCOUNT");
            String income = walletService.createWallet(null, "AGENT_BANKING_COMMISSION_ACCOUNT");
            logger.info("wallet is trading{} , income{}", wal, income);
            applicationUsers.setWalletNumber(wal);
            applicationUsers.setActivated(1);
            applicationUsers.setIncomeWalletNumber(income);
            applicationUsers.setParentAgentId(agentDto.getParentAgent());
            applicationUsers.setSubParentAgentId(agentDto.getSubParentAgent());
            logger.info("criteria is :: "+agentDto.getUsername());

            Agents checkNam = agentsRepository.findByUsername(applicationUsers.getUsername().replaceAll("\\s",""));
            if (Objects.nonNull(checkNam)) {
                logger.info("username already taken , generating new username");
                applicationUsers.setUsername(applicationUsers.getUsername() + Utility.randomNumber(3));
                logger.info("new username generated  {}", applicationUsers.getUsername());
            }
            String agentId = generateAgentId(agentDto);
            Agents agent = agentsRepository.findByAgentId(agentId);
            if (Objects.nonNull(agent)) {
                agentId = generateAgentId(agentDto);
            }
            applicationUsers.setAgentId(agentId);

            String newFormat = "0.00";
            if(agentDto.getPosTerminalTranFee()!=null && agentDto.getPosTerminalTranFee()!=""){
                newFormat =  new DecimalFormat("#0.##").format(Double.valueOf(agentDto.getPosTerminalTranFee()));
            }
            applicationUsers.setPosTerminalTranFee(newFormat);
            applicationUsers.setFirstTime(true);

            applicationUsers.setUsername(applicationUsers.getUsername().trim().replaceAll("\\s+", ""));
            applicationUsers.setTerminalId(applicationUsers.getTerminalId()==null || applicationUsers.getTerminalId().equals("")?null:applicationUsers.getTerminalId());
            Agents sn = agentsRepository.save(applicationUsers);
            Devicesetup devicesetup = new Devicesetup();
            devicesetup.setAgentId(sn.getId());
            devicesetup.setAgentParentId(agentDto.getParentAgent());
            devicesetup.setClientid(MD5.md5(sn + ""));
            devicesetup.setSecretkey(MD5.md5(Utility.randomNumber(10)));
            if (agentDto.getGeofenceoption() == 1) {
                devicesetup.setGeofenceoption(true);
                devicesetup.setGeofenceradius(agentDto.getGeofenceradius());

            }
            devicesetup.setLatitude(agentDto.getLatitude());
            devicesetup.setLongitude(agentDto.getLongitude());
            devicesetup.setAgentLocation(agentDto.getAgentLocation());
            devicesetup.setGeofencedLocation(agentDto.getGeofencedLocation());
            devicesetup.setStatus(0);
            deviceSetupRepository.save(devicesetup);


            Mandates mandates = new Mandates();
            mandates.setPicture(agentDto.getPicture());
            mandates.setSignature(agentDto.getSignature());
            mandates.setUtilityBill(agentDto.getUtilityBill());
            mandates.setIdcard(agentDto.getIdcard());
            mandates.setApplicationUsers(sn);
//            logger.info("mandate ---- {}", mandates);
            mandateRepository.save(mandates);

            String agentPassword = "";
            if (settingService.isSettingAvailable("AGENT_CREATION_GENERATE_PASSWORD")) {
                agentPassword = AppUtility.randomString(10);
                sn.setPassword(passwordEncoder.encode(agentPassword));
            }

            String agentPin = "";
            if (settingService.isSettingAvailable("AGENT_CREATION_GENERATE_PIN")) {

                Integer defaultPinLength = 4;
                if (settingService.isSettingAvailable("DEFAULT_PIN_LENGTH")) {
                    defaultPinLength = Integer.valueOf(settingService.getSettingByCode("DEFAULT_PIN_LENGTH").getValue());
                    logger.debug("default pin length -> {}", defaultPinLength);

                }

                agentPin = AppUtility.randomNumber(defaultPinLength);
                logger.debug("generate agent pin --> {}", agentPin);
                sn.setUserPin(passwordEncoder.encode(agentPin));
            }

            agentsRepository.save(sn);

            Map<String, Object> params = new HashMap<>();
            params.put("name", sn.getUsername());
            params.put("agentId", sn.getAgentId());
            params.put("password", agentPassword);
            params.put("pin", agentPin);


            sendSetupNotification(sn, params);

            //Send report to NIBBS
            try{
                dataReportService.addAgentDataReport(sn);
            }catch(GravityException e){
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }

//            return messageSource.getMessage("agent.add.success", null, locale);
            return params;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
//            throw new GravityException(messageSource.getMessage("agent.add.failure", null, locale), e);
            throw new GravityException(e.getMessage());
        }
    }

    @Override
    public void validateNewAgent(AgentDto agentDto)  throws GravityException{

        logger.info("Validating new agent: {}", agentDto);

        if(agentDto.getTerminalId().trim().length()>8){
            throw new GravityException("Invalid Terminal ID length: required length is 8");
        }

        /**
         * Ignore validation when terminal id is not supplied 01/07/2019
         */
        if(agentDto.getTerminalId()!=null && !agentDto.getTerminalId().equals("")){
            Agents agent = agentsRepository.findByTerminalId(agentDto.getTerminalId().trim());
            if (Objects.nonNull(agent)) {
                throw new GravityException("Terminal ID [" + agentDto.getTerminalId() + "] already exists");
            }
        }


        validateContactDetails(agentDto);

    }

    @Override
    public void manualCreateAgent(Agents agent) throws GravityException {

        Agents agentObj = agentsRepository.findByUsernameOrAgentIdOrWalletNumberOrIncomeWalletNumber(agent.getUsername(),agent.getAgentId(),agent.getWalletNumber().trim(),agent.getIncomeWalletNumber().trim());
        if(agentObj!=null){
            System.out.println("Username already exist:: "+agent.getUsername());
        }else{
            System.out.println("Saving :: "+agent);
            agentsRepository.save(agent);



            //add wallet recrd
            FreedomWallet tradingWallet = new FreedomWallet();
            tradingWallet.setDelFlag("N");
            tradingWallet.setIsGeneralLedger("NO");
            tradingWallet.setPurpose("AGENT_BANKING_TRADING_ACCOUNT");
            tradingWallet.setLedgerBalance(0.00);
            tradingWallet.setAvailableBalance(0.00);
            tradingWallet.setWalletNumber(agent.getWalletNumber());

            freedomWalletRepository.save(tradingWallet);



            FreedomWallet incomeWallet = new FreedomWallet();
            incomeWallet.setDelFlag("N");
            incomeWallet.setIsGeneralLedger("NO");
            incomeWallet.setPurpose("AGENT_BANKING_COMMISSION_ACCOUNT");
            incomeWallet.setLedgerBalance(0.00);
            incomeWallet.setAvailableBalance(0.00);
            incomeWallet.setWalletNumber(agent.getIncomeWalletNumber());

            freedomWalletRepository.save(incomeWallet);

            System.out.println("wallet created for :: "+agent.getUsername());
        }
    }

    @Override
    public Agents validateAgentCreds(LoginDto loginDto) throws GravityException {
        Agents agents = agentsRepository.findByUsername(loginDto.getUsername());
        if(agents ==null){
            throw new UserNotFoundException("Invalid Credentials");
        }else{
            if(!passwordEncoder.matches(loginDto.getPassword(),agents.getPassword())){
                throw new GravityException("Invalid Credentials.");
            }

            if(agents.getActivated() == 0){
                throw new GravityException("User not activated");
            }
        }
        return agents;
    }

    private void validateContactDetails(AgentDto agentDto) {
        //todo relax email check when empty or null
        if (agentDto.getEmail()!=null && !agentDto.getEmail().equals("") && !EmailValidator.isValid(agentDto.getEmail())) {
            //Invalid email address
            throw new GravityException("Invalid email address [" + agentDto.getEmail() + "]");
        }

        if (agentDto.getPhoneNumber() != null && !PhoneNumberValidator.isValid(agentDto.getPhoneNumber())) {
            //Invalid phone number
            throw new GravityException("Invalid phone number [" + agentDto.getPhoneNumber() + "]");
        }
    }


    @Async
    @Override
    public void sendSetupNotification(Agents sn, Map<String, Object> params) {

        if (settingService.isSettingAvailable("AGENT_CREATION_SEND_EMAIL")) {
            mailService.sendMail("You have been profiled !", sn.getEmail(), null, params, "agent_creation");
        }

        if (settingService.isSettingAvailable("AGENT_CREATION_SEND_SMS")) {
            String message = pr.getV("agent.creation.success", "sms_messages.properties");
            String finalMessage = String.format(message,params.get("name"),params.get("agentId"),params.get("pin"),params.get("password"));
            smsService.sendPlainSms(sn.getPhoneNumber(),finalMessage);
        }
    }

    @Override
    public String updateAgent(AgentDto agentDto) {

        try {
            Agents agent = agentsRepository.getOne(agentDto.getId());
            agent.setEmail(agentDto.getEmail());
            agent.setPhoneNumber(agentDto.getPhoneNumber());
            agent.setFirstName(agentDto.getFirstName());
            agent.setLastName(agentDto.getLastName());
            agent.setMiddleName(agentDto.getMiddleName());
            agent.setGender(agentDto.getGender());
            agent.setAgentType(agentDto.getAgentType());
            agent.setCity(agentDto.getAddressCity());
            agent.setLga(agentDto.getLga().toUpperCase());
            agent.setState(agentDto.getState().toUpperCase());
            agent.setCountry(agentDto.getCountry());
            agent.setAddress(agentDto.getAddress());
            agent.setAccountNo(agentDto.getAccountNo());
            agent.setComAccountNo(agentDto.getAccountNo());
            agent.setDob(DateUtil.dateFullFormatYYMMdd(agentDto.getDateOfBirth()));
            agent.setPresentAddressDateOfEntry(DateUtil.dateFullFormatYYMMdd(agentDto.getBuisnessLocationEntryDate()));
            agent.setParentAgentId(agentDto.getParentAgent());
            agent.setSubParentAgentId(agentDto.getSubParentAgent());
            agent.setBusinessName(agentDto.getBusinessName());
            agent.setBuisnessLocation(agentDto.getBuisnessLocation());
            agent.setComAccountBank(agentDto.getComAccountBank());
            agent.setBvn(agentDto.getBvn());
            agent.setTerminalId(agentDto.getTerminalId());
            if( agentDto.getTerminalId() != null && !agentDto.getTerminalId().equals("")){
                BankDetails bankDetails = bankDetailsRepository.findByCbnCode(agentDto.getTerminalId().substring(1,4));
                if(bankDetails !=null){
                    agent.setBankCode(bankDetails.getBankCode());
                }
            }
            String newFormat = "0.00";
            if(agentDto.getPosTerminalTranFee()!=null && agentDto.getPosTerminalTranFee()!=""){
                newFormat =  new DecimalFormat("#0.##").format(Double.valueOf(agentDto.getPosTerminalTranFee()));
            }
            agent.setPosTerminalTranFee(newFormat);
            agent.setCommissionFeePercentage(agentDto.getCommissionFeePercentage());
            agent.setIdentificationType(agentDto.getIdentificationType());
            agent.setIdNumber(agentDto.getIdNumber());
            agent.setHighestEducationalLevel(agentDto.getHighestEducationalLevel());
            agent.setAgentCode(agentDto.getNibssAgentCode());
            agent.setSanefAgentCode(agentDto.getSanefAgentCode());

//            try{
//                dataReportService.updateAgentDataReport(agent);
//            }catch(Exception e){
//                e.printStackTrace();
//            }


            agentsRepository.save(agent);


            return messageSource.getMessage("agent.update.success", null, locale);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new GravityException(e.getMessage());
        }

    }

    @Override
    public String updateAgent (Agents agent){

        agentsRepository.save(agent);

        return "Updated Succesfully";
    }

    @Override
    public String setNIBSSAgentCode(String agentId, String agentCode) {
        Agents  agents = agentsRepository.findByAgentId(agentId);
        if(agents!=null){
            agents.setAgentCode(agentCode);
            return agentCode;
        }else{
            return null;
        }
    }

    @Override
    public void validateUpdate(AgentDto agentDto) {

        logger.debug("Validating updated agent: {}", agentDto);
        Agents agents = getAgent(agentDto.getId());

        logger.info("agent name is {}", agents.getUsername());

        String terminalId = agents.getTerminalId();
        System.out.println(terminalId);

        if (StringUtils.isNotBlank(terminalId)) {
            if (!terminalId.equals(agentDto.getTerminalId().trim())) {
                //Terminal ID already exists
                if (Objects.nonNull(agentsRepository.findByTerminalId(agentDto.getTerminalId().trim()))) {
                    logger.error("Terminal ID [" + agentDto.getTerminalId() + "] already exists");
                    throw new GravityException("Terminal ID [" + agentDto.getTerminalId() + "] already exists", null);
                }



            }
        }

        logger.info("to validate contact");
        validateContactDetails(agentDto);

    }

    @RequireApproval(code = "RESET_AGENT_PIN", entityType = Agents.class)
    @Override
    public String resetAgentPin(PinResetDto pinResetDto) {

        try {

            Agents agents = agentsRepository.getOne(pinResetDto.getAgentId());

            String agentPin = "";

            Integer defaultPinLength = 4;
            if (settingService.isSettingAvailable("DEFAULT_PIN_LENGTH")) {
                defaultPinLength = Integer.valueOf(settingService.getSettingByCode("DEFAULT_PIN_LENGTH").getValue());
                logger.debug("default pin length -> {}", defaultPinLength);

            }

            agentPin = "1111";
            logger.debug("generate agent pin --> {}", agentPin);
            agents.setUserPin(passwordEncoder.encode(agentPin));


            agentsRepository.save(agents);

            {
                Map<String, Object> params = new HashMap<>();
                params.put("name", agents.getFirstName() + " " + agents.getLastName());
                params.put("agentId", agents.getUsername());
                params.put("pin", agentPin);
                mailService.sendMail("Your pin has been reset ", agents.getEmail(), null, params, "agent_pin_update");
            }

            if (settingService.isSettingAvailable("AGENT_PIN_RESET_SEND_SMS")) {

                String template = pr.getV("agent_pin_update", "sms_messages.properties");
                String message = String.format(template,agents.getFirstName() + " " + agents.getLastName(),
                        agentPin);
                smsService.sendPlainSms(agents.getPhoneNumber(),message);
            }

            return messageSource.getMessage("agent.pin.reset.success", null, locale);

        } catch (Exception e) {
            e.printStackTrace();
            throw new GravityException(e.getMessage(), e);
        }
    }

    public String resetPin(PinResetDto pinResetDto) {

        try {

            Agents agents = agentsRepository.findByUsername(pinResetDto.getAgentName());
            if(agents==null) {
                throw new UserNotFoundException("Record not Found for entered Agent Name");
            }

            String agentPin = Utility.randomNumber(4);//String.valueOf(new Random().nextInt(9999));

            logger.debug("generate agent pin --> {}", agentPin);
            agents.setUserPin(passwordEncoder.encode(agentPin));

            agentsRepository.save(agents);

            Map<String, Object> params = new HashMap<>();
            params.put("name", agents.getFirstName() + " " + agents.getLastName());
            params.put("pin", agentPin);
            params.put("agentId", agents.getUsername());
            mailService.sendMail("Your pin has been reset ", agents.getEmail(), null, params, "agent_pin_update");


            if (settingService.isSettingAvailable("AGENT_PIN_RESET_SEND_SMS")) {

                String template = pr.getV("agent_pin_update", "sms_messages.properties");
                String message = String.format(template,agents.getFirstName() + " " + agents.getLastName(),
                        agentPin);
                smsService.sendPlainSms(agents.getPhoneNumber(),message);
            }

            return messageSource.getMessage("agent.pin.reset.success", null, locale);

        } catch (Exception e) {
            e.printStackTrace();
            throw new GravityException(e.getMessage(), e);
        }
    }

    @RequireApproval(code = "RESET_AGENT_PASSWORD", entityType = Agents.class)
    @Override
    public String resetAgentPassword(PassWordResetDto passWordResetDto) {

        try {

            Agents agents = agentsRepository.getOne(passWordResetDto.getAgentId());
            if(agents==null){
                agents = agentsRepository.findByUsername(passWordResetDto.getAgentName());
            }

            if(agents==null){
                throw new UserNotFoundException("Record not Found for entered Agent ID and Name");
            }

            String agentPassword = "password";//AppUtility.randomString(10);
            agents.setPassword(passwordEncoder.encode(agentPassword));
            agentsRepository.save(agents);
            {
                Map<String, Object> params = new HashMap<>();
                params.put("name", agents.getFirstName() + " " + agents.getLastName());
                params.put("agentId", agents.getUsername());
                params.put("password", agentPassword);
                mailService.sendMail("Your password has been reset ", agents.getEmail(), null, params, "agent_password_update");
            }

            if (settingService.isSettingAvailable("AGENT_PASSWORD_RESET_SEND_SMS")) {
                String template = pr.getV("agent_password_update", "sms_messages.properties");
                String message = String.format(template,agents.getFirstName() + " " + agents.getLastName(),
                        agentPassword);
                smsService.sendPlainSms(agents.getPhoneNumber(), message);
            }

            return messageSource.getMessage("agent.password.reset.success", null, locale);

        } catch (Exception e) {
            e.printStackTrace();
            throw new GravityException(messageSource.getMessage("agent.password.reset.failure", null, locale), e);
        }

    }

    public String resetPassword(PassWordResetDto passWordResetDto) {

        try {
            Agents agents = agentsRepository.findByUsername(passWordResetDto.getAgentName());
            if(agents==null) {
                throw new UserNotFoundException("Record not Found for entered Agent Name");
            }
            String agentPassword = AppUtility.randomString(10);
            agents.setPassword(passwordEncoder.encode(agentPassword));
            agentsRepository.save(agents);
            {
                Map<String, Object> params = new HashMap<>();
                params.put("name", agents.getFirstName() + " " + agents.getLastName());
                params.put("password", agentPassword);
                params.put("agentId", agents.getUsername());
                mailService.sendMail("Your password has been reset ", agents.getEmail(), null, params, "agent_password_update");
            }

            if (settingService.isSettingAvailable("AGENT_PASSWORD_RESET_SEND_SMS")) {
                String template = pr.getV("agent_password_update", "sms_messages.properties");
                String message = String.format(template,agents.getFirstName() + " " + agents.getLastName(),
                        agentPassword);
                smsService.sendPlainSms(agents.getPhoneNumber(), message);
            }

            return messageSource.getMessage("agent.password.reset.success", null, locale);

        } catch (Exception e) {
            e.printStackTrace();
            throw new GravityException(messageSource.getMessage("agent.password.reset.failure", null, locale), e);
        }

    }

    @Override
    public String updatePassword(PasswordUpdateDto passwordUpdateDto) throws GravityException{

        if(passwordUpdateDto.getNewPassword()==null || passwordUpdateDto.getNewPassword().equals("")){
            throw new GravityException("Invalid New Password Entered");
        }else{
            Agents  agent = agentsRepository.findByUsername(passwordUpdateDto.getAgentUsername());
            if(passwordEncoder.matches(passwordUpdateDto.getOldPassword(),agent.getPassword())){
                agent.setPassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
                agentsRepository.save(agent);
                return "SUCCESS";
            }
            else{
                throw new GravityException("Wrong Password Entered");
            }
        }


    }

    @Override
    public String updatePin(PinUpdateDTO pinUpdateDTO) {
        Agents agent = agentsRepository.findByUsername(pinUpdateDTO.getAgentName());
        Boolean matches = passwordEncoder.matches(pinUpdateDTO.getOldPin(),agent.getUserPin());
        if(matches){
            String newPin = passwordEncoder.encode(pinUpdateDTO.getNewPin());
            agent.setUserPin(newPin);
            agentsRepository.save(agent);
            return "Update Successful";
        }else{
            throw new GravityException("Invalid Pin Entered");
        }
    }

    @Override
    public String disableAgent(Long id) {
        Agents agents = agentsRepository.getOne(id);
        agents.setActivated(0);
        agentsRepository.save(agents);
        return messageSource.getMessage("agent.disable.success", null, locale);
    }

    @Override
    public String enableAgent(Long id) {
        Agents agents = agentsRepository.getOne(id);
        agents.setActivated(1);
        agentsRepository.save(agents);
        return messageSource.getMessage("agent.enable.success", null, locale);
    }

    @Override
    public Agents getAgent(Long id) {
        return agentsRepository.getOne(id);
    }

    @Override
    public Agents getAgentByAgentId(String agentId) {
        Agents agents = agentsRepository.findByAgentId(agentId);
        return agents;
    }

    @Override
    public Agents getAgentByWalletNumber(String walletNumber) {
        Agents agents = agentsRepository.findByWalletNumber(walletNumber);
        return agents;
    }

    @Override
    public Agents getAgentByTerminalId(String terminalId) {
        Agents agents = agentsRepository.findByTerminalId(terminalId);
        return agents;
    }




    @Override
    public Mandates getAgentMandate(Agents agents) {
        Mandates mandates = mandateRepository.findByApplicationUsers(agents);
        if (Objects.isNull(mandates)) {
            Mandates mandates1 = new Mandates();
            mandates1.setApplicationUsers(agents);
            mandateRepository.save(mandates1);
        }
        return mandateRepository.findByApplicationUsers(agents);
    }

    @Override
    public List<Agents> getAggregatorsAgent(Agents agents) {
        return agentsRepository.findByParentAgentIdAndAgentTypeOrderByDatecreatedDesc(agents.getId(),"SUBAGENT");
    }

    @Override
    public Page<AggregatorAgentDTO> getPagedAggregatorsAgent(Agents agents,Pageable pageable,boolean findByParentAgent) {


        List<Agents> agents1;

        if(findByParentAgent){
            agents1 = agentsRepository.findByParentAgentIdAndAgentTypeOrderByDatecreatedDesc(agents.getId(),"SUBAGENT");
        }else{
            agents1 = agentsRepository.findByParentAgentIdAndAgentTypeOrderByDatecreatedDesc(agents.getId(),"SUBAGGREGATOR");
        }

        List<AggregatorAgentDTO> agentDTOS = new ArrayList<>();
        agents1.forEach(agents2 -> {
            AggregatorAgentDTO agentDTO  = modelMapper.map(agents2, AggregatorAgentDTO.class);
            agentDTO.setStatusOfCreations("APPROVED");
            agentDTOS.add(agentDTO);
        });

//        List<Verification> pendingVerification = verificationRepository.findByCodeAndStatusAndInitiatedBy("CREATE_AGENT", VerificationStatus.PENDING,agents.getUsername());
//
//        List<Verification> declinedVerifications = verificationRepository.findByCodeAndStatusAndInitiatedBy("CREATE_AGENT", VerificationStatus.DECLINED,agents.getUsername());



//        pendingVerification.forEach(verification -> {
//            ObjectMapper objectMapper = new ObjectMapper();
//            AgentDto object = null;
//            try {
//                object = objectMapper.readValue(verification.getOriginalObject(), AgentDto.class);
//                if(object !=null ){
//                    modelMapper.getConfiguration().setAmbiguityIgnored(true);
//                    AggregatorAgentDTO agentDTO  = modelMapper.map(object, AggregatorAgentDTO.class);
//                    agentDTO.setStatusOfCreations("PENDING");
//                    agentDTOS.add(agentDTO);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });

//        declinedVerifications.forEach(verification -> {
//            ObjectMapper objectMapper = new ObjectMapper();
//            AgentDto object = null;
//            try {
//                object = objectMapper.readValue(verification.getOriginalObject(), AgentDto.class);
//                if(object != null){
//                    modelMapper.getConfiguration().setAmbiguityIgnored(true);
//                    AggregatorAgentDTO agentDTO  = modelMapper.map(object, AggregatorAgentDTO.class);
//                    agentDTO.setStatusOfCreations("DECLINED");
//                    agentDTOS.add(agentDTO);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });

        long totalCount =  agents1.size();//verificationRepository.countByInitiatedBy(agents.getUsername();
        Page<AggregatorAgentDTO> aggregatorAgentDTOS = new PageImpl<>(agentDTOS,pageable,totalCount);

        return aggregatorAgentDTOS;
    }

    @Override
    public Page<AggregatorAgentDTO> getPagedSubAggregatorsAgent(Agents agents,Pageable pageable) {


        List<Agents> agents1 = agentsRepository.findBySubParentAgentIdOrderByDatecreatedDesc(agents.getId());

        List<AggregatorAgentDTO> agentDTOS = new ArrayList<>();
        agents1.forEach(agents2 -> {
            AggregatorAgentDTO agentDTO  = modelMapper.map(agents2, AggregatorAgentDTO.class);
            agentDTO.setStatusOfCreations("APPROVED");
            agentDTOS.add(agentDTO);
        });


        long totalCount =  agents1.size();//verificationRepository.countByInitiatedBy(agents.getUsername();
        Page<AggregatorAgentDTO> aggregatorAgentDTOS = new PageImpl<>(agentDTOS,pageable,totalCount);

        return aggregatorAgentDTOS;
    }

    @Override
    public Devicesetup getDevice(Long agentId) {
        return deviceSetupRepository.findByAgentId(agentId);
    }

    @RequireApproval(code = "RESET_AGENT_DEVICE", entityType = Agents.class)
    @Override
    public String refreshDevice(DeviceRefreshDto deviceRefreshDto) {

        Devicesetup devicesetup = deviceSetupRepository.getOne(deviceRefreshDto.getDeviceId());
        devicesetup.setStatus(0);
        devicesetup.setDeviceid("");
        deviceSetupRepository.save(devicesetup);
        return messageSource.getMessage("agent.deviceAudit.refresh.success", null, locale);
    }

    @Override
    public Page<AgentDto> getAgents(Pageable pageDetails) {

        logger.debug("Retrieving agents");
        Page<Agents> page = agentsRepository.findAll(pageDetails);
        List<AgentDto> dtOs = convertEntitiesToDTOs(page.getContent());
        long t = page.getTotalElements();
        Page<AgentDto> pageImpl = new PageImpl<AgentDto>(dtOs, pageDetails, t);
        return pageImpl;
    }

    @Override
    public Page<Agents> getUnSyncedSanefAgents(Pageable pageDetails) {

        logger.debug("Retrieving agents");
        Page<Agents> page = agentsRepository.findByActivatedAndSanefAgentCodeIsNull(1,pageDetails);
        return page;
    }

    @Override
    public List<AgentDto> aggregators() {
        List<Agents> agg = agentsRepository.findByAgentTypeContains("AGGREGATOR");
        return convertEntitiesToDTOs(agg);
    }

    @Override
    public List<AgentDto> subAggregators() {
        List<Agents> agg = agentsRepository.findByAgentTypeContains("SUBAGGREGATOR");
        return convertEntitiesToDTOs(agg);
    }



//    @Cacheable(cacheNames = "findAllAgents",sync = true)
    @Override
    public Page<Agents> fetchAgentViewDTO(Pageable pageable) {
        System.out.println("called me");
        Page<Agents> agg = agentsRepository.findAll(pageable);
        return agg;
    }

    @Override
    public Page<Agents> fetchAgentViewDTO(String username,Pageable pageable) {

        Page<Agents> agg = agentsRepository.findByUsernameContainsOrTerminalIdContains(username,username,pageable);
        return agg;
    }



    @Override
    public AgentDto convertEntityToDTO(Agents agents) {

        AgentDto dto = modelMapper.map(agents, AgentDto.class);
        if(dto ==null){
            return null;
        }else{
            if(dto.getTerminalId()==null || dto.getTerminalId().trim().equals("")){
                dto.setTerminalId(" ");
            }
            if(dto.getPhoneNumber()==null || dto.getPhoneNumber().trim().equals("")){
                dto.setPhoneNumber(" ");
            }
            if( agents.getTerminalId() != null && !agents.getTerminalId().trim().equals("")){
                String terminalId = agents.getTerminalId().trim();
                if(terminalId.length() > 4 ){
                    BankDetails bankDetails = bankDetailsRepository.findByCbnCode(agents.getTerminalId().trim().substring(1,4));
                    if(bankDetails !=null){
                        agents.setBankCode(bankDetails.getBankCode());
                    }
                }

            }
            dto.setNibssAgentCode(agents.getAgentCode());
            dto.setBuisnessLocationEntryDate(DateUtil.formatDateToreadable(agents.getPresentAddressDateOfEntry()));
            dto.setDateOfBirth(DateUtil.formatDateToreadable(agents.getDob()));
            dto.setSubParentAgent(agents.getSubParentAgentId()==null?0:agents.getSubParentAgentId());

            if(agents.getParentAgentId()==null){
                dto.setParentAgent(0);
            }else{
                dto.setParentAgent(agents.getParentAgentId());
            }
            return dto;
        }
    }


    public Agents convertDTOToEntity(AgentDto codeDTO) {
        Agents agents = modelMapper.map(codeDTO, Agents.class);
        Agents former = agentsRepository.getOne(codeDTO.getId());
        try {
            agents.setDob(DateUtil.dateFullFormatYYMMdd(codeDTO.getDateOfBirth()));
            agents.setPresentAddressDateOfEntry(DateUtil.dateFullFormatYYMMdd(codeDTO.getBuisnessLocationEntryDate()));
            agents.setDatecreated(former.getDatecreated());
        } catch (Exception e) {

        }

        return agents;
    }

    public List<AgentDto> convertEntitiesToDTOs(Iterable<Agents> codes) {

        List<AgentDto> codeDTOList = new ArrayList<>();
        for (Agents code : codes) {
            AgentDto codeDTO = convertEntityToDTO(code);
            codeDTOList.add(codeDTO);
        }

        return codeDTOList;
    }

    @Override
    public String generateAgentId(AgentDto agent) {
        String AB = "1234567890";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        AB = StringUtils.substring(agent.getFirstName().toUpperCase(), 0, 2) + StringUtils.substring(agent.getLastName().toUpperCase(), 0, 2) + sb.toString();
        return AB;
    }

    @Override
    public Long uploadAgents(MultipartFile multipartFile) {

        logger.debug("Uploading agents file: {}", multipartFile.getOriginalFilename());

        Long fileId = fileService.storeFile(multipartFile, "AGENTS_CREATION_FILE");

        File file;
        try {
            file = fileService.loadFileAsResource(fileId).getFile();

            List<AgentDto> dtos = mapAgentsDataFromFile(file);

            logger.info("Agents from file: {}", dtos);
            return fileId;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new GravityException("Error uploading file");

        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            throw new GravityException("Error uploading file");
        }


    }

    @Override
    public List<AgentDto> getUploadedAgents(Long fileId) {

        File file;
        try {
            file = fileService.loadFileAsResource(fileId).getFile();
            List<AgentDto> agentDtos = mapAgentsDataFromFile(file);

            logger.info("Agents from file: {}", agentDtos);
            return agentDtos;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new GravityException("Error fetching agents file");

        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            throw new GravityException("Error fetching agents file");
        }
    }

    @Override
    public List<AgentDto> createUploadedAgents(Long fileId) {

        logger.info("Creating agents in file with Id [{}]", fileId);
        List<AgentDto> uploadedAgents = getUploadedAgents(fileId);
        List<AgentDto> failedAgents = new ArrayList<>();

        for (AgentDto agentDto: uploadedAgents){

            try {
                agentDto.setUsername(agentDto.getFirstName()+"."+agentDto.getLastName());
                logger.info("Sent aggregator is :: "+agentDto.getAggregatorName());
                if(agentDto.getAggregatorName()!=null){
                    Agents aggregator = agentsRepository.findByUsername(agentDto.getAggregatorName());
                    if(aggregator!=null){
                        agentDto.setParentAgent(aggregator.getId());
                    }else{
                        logger.error("Aggregator username passed {} does not exist",agentDto.getAggregatorName());
                    }
                }
                createAgent(agentDto);
            }
            catch (Exception e){
                logger.error(e.getMessage(), e);
                failedAgents.add(agentDto);
            }
        }

        logger.info("Failed agents: {}", failedAgents);
        return failedAgents;
    }

    @Override
    public AgentSetupResponse agentSetup(AgentSetupDto request) throws GravityException{

        AgentSetupResponse response = new AgentSetupResponse();


        String Username = request.getUsername();
        String newPin = request.getNewPin();
        String newPassword = request.getNewPassword();
        String deviceid = request.getDeviceId();

        if (Username == null || newPin == null || deviceid == null) {
            response.setRespCode("118");
            response.setRespDescription(pr.getV("118", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }



        AgentSetupResponse setupresponse;
        try {
            setupresponse = dao.getAgentDetailsByUsername(Username);
        } catch (Exception e) {
            response.setRespCode("110");
            response.setRespDescription(pr.getV("110", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        if (setupresponse == null) {
            response.setRespCode("119");
            response.setRespDescription(pr.getV("119", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }
/**
 * Commented Out to avoid check when agent logs in from another device todo 27-08-2019
 */
//        if (setupresponse.getDeviceSetupStatus().equals("1")) {
//            response.setRespCode("121");
//            response.setRespDescription(pr.getV("121", "response.properties"));
//            logger.warn(response.getRespDescription());
//            return response;
//        }

        Agents agent = agentsRepository.findByUsername(Username);

        if ( passwordEncoder.matches(newPin, agent.getUserPin())  ||
                passwordEncoder.matches(newPassword, agent.getPassword())) {
            response.setRespCode("123");
            response.setRespDescription(pr.getV("123", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        boolean changesuccessful = false;
        try {
            changesuccessful = dao.savePassword(Username, newPassword);
            changesuccessful = dao.savePin_(Username, newPin);
        } catch (Exception e) {
            response.setRespCode("110");
            response.setRespDescription(pr.getV("110", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }
        if (!changesuccessful) {
            response.setRespCode("123");
            response.setRespDescription(pr.getV("123", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }
        try {
            if (dao.UpdateAgentStatus(setupresponse.getAgentId(), deviceid)) {
                setupresponse.setDeviceSetupStatus("1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setRespCode("110");
            response.setRespDescription(pr.getV("110", "response.properties"));
            logger.warn(response.getRespDescription());
            return response;
        }

        Agents agents = agentsRepository.findByUsername(request.getUsername());
        agents.setFirstTime(false);
        agentsRepository.save(agents);

        setupresponse.setDeviceSetupStatus("1");
        setupresponse.setPin(null); //removes from being display to user
        System.out.println("########################### NAME : " + setupresponse.getName());
        System.out.println("########################### BELONGSTO: " + setupresponse.getBelongsto());
        response = setupresponse;
        response.setRespCode("00");
        response.setRespDescription(pr.getV("00", "response.properties"));

        return response;
    }

    @Override
    public Response logon(LoginDto request) throws GravityException{

        Response response = new Response();

        Agents agent = this.validateAgentCreds(request);

        LoginRespDto respDto = generalUtil.converttodto(agent);

        if(agent.isFirstTime()==null || agent.isFirstTime()){
            respDto.setIsFirstTime("true");
        }else{
            respDto.setIsFirstTime("false");
            Boolean hasloggedInBefore = deviceService.hasLoggedInBefore(request.getDeviceId(),agent.getId());
            if(!hasloggedInBefore){
                if(request.getOtp() != null && !request.getOtp().equals("")){
                    boolean isvalid = passwordEncoder.matches(request.getOtp(),agent.getUserPin());
                    if(isvalid){
                        deviceService.attacheDeviceToUser(request.getDeviceId(), agent.getId());
                    }else{
                        throw new GravityException("Invalid Pin used");
                    }
                    respDto.setIsNewDevice("false");
                    respDto.setAuth_token(jwtTokenUtil.getToken(agent.getAgentId()));
                }else{
                    respDto.setIsNewDevice("true");
                    response.setRespBody(respDto);
                    response.setRespCode("00");
                    response.setRespDescription("success");
                    return response;
                }
            }else{
                respDto.setIsNewDevice("false");
                respDto.setAuth_token(jwtTokenUtil.getToken(agent.getAgentId()));
            }
        }

        response.setRespBody(respDto);
        response.setRespCode("00");
        response.setRespDescription("success");
        return response;
    }



    @Override
    public Agents fetchAgentByAgentName(String agentName) {
        Agents agent = agentsRepository.findByUsername(agentName);
        return agent;
    }

    @Override
    public List<Agents> fetchAgentByEmail(String email) {
        return agentsRepository.findByEmail(email);
    }

    @Override
    public Agents fetchAgentById(String agentId) {
        return agentsRepository.findByAgentId(agentId);
    }

    @Override
    public AggregatorDashBoardDTO fetchAggregatorDashborad(Agents agent) {


        TotalDaysStats totalDaysStats = new TotalDaysStats();

        List<String[]> totalStatsTranSummary = transactionService.getAllTimeStatsTransaction(agent.getUsername());

        for(String[] t:totalStatsTranSummary){
            totalDaysStats.setBillPayments(Utility.formatForView(roundValueForData(t[1])));
            totalDaysStats.setDeposits(Utility.formatForView(roundValueForData(t[3])));
            totalDaysStats.setWithDrawals(Utility.formatForView(roundValueForData(t[5])));
            totalDaysStats.setTotals(String.valueOf(roundValueForData(t[1]) + roundValueForData(t[3]) + roundValueForData(t[5])));

            totalDaysStats.setDepositCount(t[4]=="null"?"0":t[4]);
            totalDaysStats.setWithDrawalsCount(t[6]=="null"?"0":t[6]);
            totalDaysStats.setBillPaymentsCount(t[8]=="null"?"0":t[8]);

            totalDaysStats.setTotalCount(String.valueOf(Integer.parseInt(totalDaysStats.getDepositCount())
                    +Integer.parseInt(totalDaysStats.getWithDrawalsCount())
                    +Integer.parseInt(totalDaysStats.getBillPaymentsCount()))
            );
        }

        List<String[]> currentStatSummary = transactionService.getCurrentStatsTransaction(agent.getUsername());
        CurrentStats currentStats = new CurrentStats();
        for(String[] t:currentStatSummary){

            currentStats.setDeposits(Utility.formatForView(roundValueForData(t[3])));
            currentStats.setWithDrawals(Utility.formatForView(roundValueForData(t[5])));
            currentStats.setBillPayments(Utility.formatForView(roundValueForData(t[1])));
            currentStats.setTotals(String.valueOf(roundValueForData(t[1]) + roundValueForData(t[3]) + roundValueForData(t[5])));

            currentStats.setDepositCount(t[4]=="null"?"0":t[4]);
            currentStats.setWithDrawalsCount(t[6]=="null"?"0":t[6]);
            currentStats.setBillPaymentsCount(t[8]=="null"?"0":t[8]);

            currentStats.setTotalCount(String.valueOf(Integer.parseInt(currentStats.getDepositCount())
                    +Integer.parseInt(currentStats.getWithDrawalsCount())
                    +Integer.parseInt(currentStats.getBillPaymentsCount())));

        }


        AggregatorDashBoardDTO aggregatorDashBoardDTO = new AggregatorDashBoardDTO();
        aggregatorDashBoardDTO.setTotalDaysStats(totalDaysStats);
        aggregatorDashBoardDTO.setCurrentStats(currentStats);

        Date todaysDate = new Date();

        boolean isSubAggregator = agent.getAgentType().equalsIgnoreCase("SUBAGGREGATOR");
        List<AgentPerformance> agentPerformances = reportService.
                aggregatorsAgentPerformance(isSubAggregator,agent, todaysDate, com._3line.gravity.core.utils.DateUtil.AddDays(todaysDate,1));


        aggregatorDashBoardDTO.setAgentPerformances(agentPerformances);

        return aggregatorDashBoardDTO;


    }

    @Override
    public AgentDashBoardDTO fetchAgentDashBoard(String agentId) {
        Agents  agents = agentsRepository.findByAgentId(agentId);
        if(agents==null){
            throw new GravityException("Invalid agent id specified");
        }

        AgentDashBoardDTO agentDashBoardDTO = new AgentDashBoardDTO();


        WalletDTO incomeWallet = walletService.getWalletByNumber(agents.getIncomeWalletNumber());

        WalletDTO tradingWallet =  walletService.getWalletByNumber(agents.getWalletNumber());

        WalletStatsDTO walletStatsDTO = new WalletStatsDTO();
        walletStatsDTO.setIncomeWalletDTO(incomeWallet);
        walletStatsDTO.setTradingWalletDTO(tradingWallet);


        Pageable firstPageWithFiveElements = PageRequest.of(0, 5);



        List<FreedomWalletTransaction>  transactions = walletService.getWalletTransactionsWithPagination(agents.getWalletNumber(),firstPageWithFiveElements).getContent();



        AggregatorDashBoardDTO agentStat =  this.fetchAggregatorDashborad(agents);


        agents.setPassword(null);
        agentDashBoardDTO.setAgent(agents);
        agentDashBoardDTO.setWalletStatsDTO(walletStatsDTO);
        agentDashBoardDTO.setTransactions(transactions);
        agentDashBoardDTO.setCurrentStats(agentStat.getCurrentStats());
        agentDashBoardDTO.setTotalDaysStats(agentStat.getTotalDaysStats());

        return agentDashBoardDTO;
    }

    @Override
    public Agents fetchAgentByParentAgentAndAgentId(Agents parentAgent, String agentId) {
        Agents agent = null;
        if(parentAgent!=null){
            System.out.println("search with :: "+agentId+" and "+parentAgent.getId());
            agent = agentsRepository.findByAgentIdAndParentAgentId(agentId,parentAgent.getId());
        }

        return agent;
    }


    private List<AgentDto> mapAgentsDataFromFile(File file) throws Throwable{

        List<AgentDto> dtos = ExcelMapper.mapFromExcel(file)
                .toObjectOf(AgentDto.class)
                .fromSheet(0) // if this method not used , called all sheets
                .map();
        return dtos;
    }

    double roundValueForData (String data ){

        if(Objects.isNull(data) || data.equals("null")){
            return 0.0;
        }

        return  Utility.round(Double.valueOf(data),2);
    }

//    @Autowired
//    CacheManager cacheManager;
//
//    public void flushCache() {
//        for (String cacheName : cacheManager.getCacheNames()) {
//            cacheManager.getCache(cacheName).clear();
//            logger.info("Flushing cache with name: " + cacheName);
//        }
//    }
}
