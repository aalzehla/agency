package com._3line.gravity.core.otp.services.implementation;


import com._3line.gravity.core.exceptions.AgencyBankingException;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.otp.model.OtpRequest;
import com._3line.gravity.core.otp.repository.OtpRequestRepository;
import com._3line.gravity.core.otp.services.OtpService;
import com._3line.gravity.core.sms.model.SendSmsRequest;
import com._3line.gravity.core.sms.service.SmsService;
import com._3line.gravity.core.utils.CharacterUtil;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FortunatusE
 * @date 3/21/2019
 */

@Service
public class OtpServiceImpl implements OtpService {

    private Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

    @Autowired
    OtpRequestRepository otpRequestRepository;

    @Qualifier("IPIntgratedSMSImplementation")
    @Autowired
    SmsService smsService;

    @Autowired
    AgentsRepository agentsRepository;




//    @Autowired
//    public OtpServiceImpl(OtpRequestRepository otpRequestRepository, AgentsRepository agentsRepository) {
//        this.otpRequestRepository = otpRequestRepository;
//        this.authService = authService;
//        this.agentsRepository = agentsRepository;
//    }

    @Async
    @Override
//    @Auditable(action = "OTP_REQUEST")
    public String generateAndSendOtp(String agentid) {
        String otp;
        logger.debug("Generating OTP for [{}]", agentid);

        try {
            Agents agent = agentsRepository.findByAgentId(agentid);

            if(agent == null){
                throw new AgencyBankingException("Agent ["+agentid+"] not found");
            }
            otp = CharacterUtil.generateOtp(4);
            OtpRequest otpRequest = new OtpRequest();
            otpRequest.setOtp(otp);
            otpRequest.setAgentId(agentid);
            otpRequest.setGeneratedOn(LocalDateTime.now());
            otpRequest.setExpiredOn(LocalDateTime.now().plusMinutes(15));
            otpRequestRepository.save(otpRequest);

            Map<String,Object> messageContents = new HashMap<>();
            messageContents.put("name",agent.getFirstName());
            messageContents.put("token",otp);
            String message = "Use the OTP: " + otp + " to complete the process";
            logger.info("Message: {}", message);
            SendSmsRequest sendSmsRequest = new SendSmsRequest();
            sendSmsRequest.setMobileNumber(agent.getPhoneNumber());
            sendSmsRequest.setMessage(message);
            logger.info("Message to agent: {}", sendSmsRequest);
            smsService.sendPlainSms(agent.getPhoneNumber(),message);
        }

        catch (AgencyBankingException e){
            logger.error(e.getMessage());
            throw e;
        }
        catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new AgencyBankingException("Error sending OTP");
        }

        return otp;

    }

    @Override
//    @Auditable(action = "OTP_VALIDATION")
    public boolean validateOTP(OtpRequest otpRequest, boolean checkExpirationTime) throws GravityException {

        logger.debug("Verifying OTP for [{}]", otpRequest.getAgentId());

        try{
            OtpRequest request = otpRequestRepository.findFirstByAgentIdAndVerifiedOrderByGeneratedOnDesc(otpRequest.getAgentId(),false);

            if(request == null){
                throw new AgencyBankingException("Could not find agent with Agent ID ["+otpRequest.getAgentId()+"]");
            }

            if(!request.getOtp().equals(otpRequest.getOtp())){
                request = otpRequestRepository.findFirstByAgentIdAndOtpAndVerified(otpRequest.getAgentId(), otpRequest.getOtp(),false);

                if(request == null){
                    throw new AgencyBankingException("Invalid OTP ["+otpRequest.getOtp()+"]");
                }

                if(!request.getOtp().equals(otpRequest.getOtp())){
                    throw new AgencyBankingException("Invalid OTP ["+otpRequest.getOtp()+"]");
                }
            }

            if(checkExpirationTime) {
                if (LocalDateTime.now().isAfter(request.getExpiredOn())) {
                    throw new AgencyBankingException("Expired OTP [" + request.getOtp() + "]");
                }
            }

//            request.setVerified(true);
//            otpRequestRepository.save(request);

        }

        catch (AgencyBankingException e){
            throw e;
        }
        catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new AgencyBankingException("Could not verify OTP");

        }
        return true;
    }
}
