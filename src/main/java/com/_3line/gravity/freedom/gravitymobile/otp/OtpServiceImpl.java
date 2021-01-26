//package com._3line.gravity.freedom.gravitymobile.otp;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
///**
// * @author FortunatusE
// * @date 3/21/2019
// */
//
//@Service
//public class OtpServiceImpl implements OtpService {
//
//    private Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);
//
//    private final OtpRequestRepository otpRequestRepository;
//    private final AuthService authService;
//    private final AgentRepository agentRepository;
//
//
//    @Autowired
//    public OtpServiceImpl(OtpRequestRepository otpRequestRepository, AuthService authService, AgentRepository agentRepository) {
//        this.otpRequestRepository = otpRequestRepository;
//        this.authService = authService;
//        this.agentRepository = agentRepository;
//    }
//
//    @Async
//    @Override
//    @Auditable(action = "OTP_REQUEST")
//    public void generateAndSendOtp(String username) {
//
//        logger.debug("Generating OTP for [{}]", username);
//
//        try {
//            Agent agent = agentRepository.findByAgentId(username);
//
//            if(agent == null){
//                throw new AgencyBankingException("Agent ["+username+"] not found");
//            }
//            String otp = CharacterUtil.generateOtp(4);
//            OtpRequest otpRequest = new OtpRequest();
//            otpRequest.setOtp(otp);
//            otpRequest.setAgentId(username);
//            otpRequest.setGeneratedOn(LocalDateTime.now());
//            otpRequest.setExpiredOn(LocalDateTime.now().plusMinutes(15));
//            otpRequestRepository.save(otpRequest);
//
//
//            String message = "Use the OTP: " + otp + " to complete the process";
//            logger.info("Message: {}", message);
//            SendSmsRequest sendSmsRequest = new SendSmsRequest();
//            sendSmsRequest.setMobileNumber(agent.getPhoneNumber());
//            sendSmsRequest.setMessage(message);
//            logger.debug("Message to agent: {}", sendSmsRequest);
//            authService.sendSMS(sendSmsRequest);
//        }
//
//        catch (AgencyBankingException e){
//            logger.error(e.getMessage());
//            throw e;
//        }
//        catch (Exception e){
//            logger.error(e.getMessage(), e);
//            throw new AgencyBankingException("Error sending OTP");
//        }
//
//
//
//    }
//
//    @Override
//    @Auditable(action = "OTP_VALIDATION")
//    public boolean validateOTP(OtpRequest otpRequest, boolean checkExpirationTime) {
//
//        logger.debug("Verifying OTP for [{}]", otpRequest.getAgentId());
//
//        try{
//            OtpRequest request = otpRequestRepository.findFirstByAgentIdOrderByDateCreatedDesc(otpRequest.getAgentId());
//
//            if(request == null){
//                throw new AgencyBankingException("Could not find agent with Agent ID ["+otpRequest.getAgentId()+"]");
//            }
//
//            if(!request.getOtp().equals(otpRequest.getOtp())){
//                request = otpRequestRepository.findFirstByAgentIdAndOtp(otpRequest.getAgentId(), otpRequest.getOtp());
//
//                if(request == null){
//                    throw new AgencyBankingException("Invalid OTP ["+otpRequest.getOtp()+"]");
//                }
//
//                if(!request.getOtp().equals(otpRequest.getOtp())){
//                    throw new AgencyBankingException("Invalid OTP ["+otpRequest.getOtp()+"]");
//                }
//            }
//
//            if(checkExpirationTime) {
//                if (LocalDateTime.now().isAfter(request.getExpiredOn())) {
//                    throw new AgencyBankingException("Expired OTP [" + request.getOtp() + "]");
//                }
//            }
//
//        }
//
//        catch (AgencyBankingException e){
//            throw e;
//        }
//        catch (Exception e){
//            logger.error(e.getMessage(), e);
//            throw new AgencyBankingException("Could not verify OTP");
//
//        }
//        return true;
//    }
//}
