package com._3line.gravity.freedom.agents.service;


import com._3line.gravity.api.auth.dto.LoginDto;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.otp.model.OtpRequest;
import com._3line.gravity.core.otp.services.OtpService;
import com._3line.gravity.core.security.jwt.JwtTokenUtil;
import com._3line.gravity.core.security.service.LoginService;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.models.Devicesetup;
import com._3line.gravity.freedom.agents.models.Password;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.agents.repository.DeviceSetupRepository;
import com._3line.gravity.freedom.agents.repository.PasswordRepository;
import com._3line.gravity.api.users.agents.dto.AgentSetupResponse;
import com._3line.gravity.freedom.wallet.dto.WalletDTO;
import com._3line.gravity.freedom.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author OlalekanW
 */
@Repository
public class SecurityDao {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder ;

    Logger logger = LoggerFactory.getLogger(this.getClass()) ;
    @Autowired
    AgentsRepository applicationUsersRepository;
    @Autowired
    DeviceSetupRepository deviceSetupRepository ;
    @Autowired
    PasswordRepository passwordRepository;

    @Autowired
    WalletService walletService;

    @Autowired
    LoginService loginService;

    @Qualifier("jwtservice")
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    OtpService  otpService;
    public boolean savePassword(String username, String password) {

        Agents applicationUsers = applicationUsersRepository.findByUsername(username);

        if(applicationUsers == null){
            throw new GravityException("User for password change does not exist {->}"+ username) ;
        }

            if (passwordEncoder.matches(password, applicationUsers.getPassword())) {
                return false;/*can not rest password with same password*/
            }
            applicationUsers.setLastPasswordChangeDate(new Date());
            applicationUsers.setPassword(passwordEncoder.encode(password));
            applicationUsers.setActivated(1);
            applicationUsersRepository.save(applicationUsers);

            Password history = new Password();
            history.setDate(new Date());
            history.setUsername(applicationUsers.getUsername());
            history.setPassword(applicationUsers.getPassword());
            passwordRepository.save(history) ;
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
            session.setAttribute("_MUSTRESETPASSWORD", false);
            return true;

    }

    public boolean savePin_(String userid, String pin) {

        try {
            Agents applicationUsers = applicationUsersRepository.findByUsername(userid);
            if (applicationUsers != null && passwordEncoder.matches(pin, applicationUsers.getUserPin())) {
                return false;/*can not rest password with same password*/
            }
            String newpin = passwordEncoder.encode(pin);
            logger.info("pin has been reset !!!!!! from {} to {}", applicationUsers.getUserPin(), newpin);
            applicationUsers.setUserPin(newpin);
            applicationUsersRepository.save(applicationUsers);
                  return true ;
        }catch (Exception e){
            e.printStackTrace();
            return false ;
        }

    }

    public Map getSecretkeyByClientId(String clientId) throws SQLException {
        logger.info("query  id: " + clientId);
         Devicesetup devicesetup = deviceSetupRepository.findByClientid(clientId);
        Agents applicationUsers = applicationUsersRepository.getOne(devicesetup.getAgentId());
        Map<String, Object> objectMap =  new HashMap<>();
        objectMap.put("secretkey", devicesetup.getSecretkey());
        objectMap.put("username",applicationUsers.getUsername());
        objectMap.put("agentId", devicesetup.getAgentId());
        objectMap.put("BelongTo", applicationUsers.getBelongsTo());
        objectMap.put("status", devicesetup.getStatus());
         if (objectMap.size() == 0) {
            throw new SQLException("No value retured from query");
        }
        return objectMap;
    }

    public AgentSetupResponse getAgentDetailsByUsername(String username)  {

         AgentSetupResponse response = new AgentSetupResponse();
        try {

            Agents agent = applicationUsersRepository.findByUsername(username);
            Devicesetup devicesetup = deviceSetupRepository.findByAgentId(agent.getId());
            WalletDTO wallet = walletService.getWalletByNumber(agent.getWalletNumber());

                    response.setAgentId(agent.getId().toString());
                    response.setClientId(devicesetup.getClientid());
                    response.setSecretKey(devicesetup.getSecretkey());
                    response.setLatitude(devicesetup.getLatitude());
                    response.setLongitude(devicesetup.getLongitude());
                    response.setDeviceSetupStatus(Integer.valueOf(devicesetup.getStatus()).toString());
                    response.setPin(agent.getUserPin());
                    response.setPassword(agent.getPassword());
                    if(wallet!=null){
                        response.setBalnce(wallet.getAvailableBalance() + "");
                    }
                    response.setBelongsto(agent.getBelongsTo() + "");
                    response.setName(agent.getFirstName() + " " +agent.getLastName());
                    response.setGeofenceradius(devicesetup.getGeofenceradius() + "");
                    response.setAccountNo(agent.getAccountNo() + "");
                    response.setStructureid(agent.getStructureid() + "");
                    response.setAddress(agent.getAddress());

                 logger.debug(response.toString());
                    return response;


        }catch (Exception e) {
            //connection or other errors
            e.printStackTrace();
        }
        return response;
    }

    public Map<String, String> getAgentLogonDetailsByUsername(LoginDto loginDto) {
        Map<String, String> response = new HashMap<String, String>();
        Agents agent = applicationUsersRepository.findByUsername(loginDto.getUsername());
        if(agent!=null){
            if(agent.isFirstTime()){
                response.put("isFirstTime","true");
                response.put("password", agent.getPassword());
                return response;
            }else{
                response.put("isFirstTime","false");
            }
        }
        Devicesetup devicesetup = deviceSetupRepository.findByDeviceidAndAgentId(loginDto.getDeviceId(),agent.getId());
        try {
            if(devicesetup==null){
                if(loginDto.getOtp()!=null && !loginDto.getOtp().equals("")){
                    //validate otp and add
                    OtpRequest otpRequest = new OtpRequest();
                    otpRequest.setAgentId(agent.getAgentId());
                    otpRequest.setOtp(loginDto.getOtp());
                    boolean isvalid = otpService.validateOTP(otpRequest,false);
                    if(isvalid){
//                        save device
                        System.out.println("OTP is valid");
                        Devicesetup devicesetup1 = deviceSetupRepository.findByAgentId(agent.getId());
                        if(devicesetup1==null){
                            devicesetup1 = new Devicesetup();
                            devicesetup1.setDeviceid(loginDto.getDeviceId());
                            devicesetup1.setAgentId(Long.parseLong(agent.getAgentId()));
                            devicesetup1.setStatus(0);
                        }else{
                            devicesetup1.setDeviceid(loginDto.getDeviceId());
                        }

                        deviceSetupRepository.save(devicesetup1);

                        final UserDetails userDetails = userDetailsService.loadUserByUsername(agent.getAgentId());
                        final String token = jwtTokenUtil.generateToken(userDetails, null);
                        response.put("auth_token",token);
                    }else{
                        //otp is invalid throw an exception
                        System.out.println("OTP is invalid");
                    }
                    response.put("isNewDevice","false");
                }else{
                    String otp = otpService.generateAndSendOtp(agent.getAgentId());
                    response.put("isNewDevice","true");
                    response.put("otp",otp);
                }
            }else{
                final UserDetails userDetails = userDetailsService.loadUserByUsername(agent.getAgentId());
                final String token = jwtTokenUtil.generateToken(userDetails, null);
                response.put("auth_token",token);
                response.put("secretKey", devicesetup.getSecretkey());
                response.put("latitude", devicesetup.getLatitude());
                response.put("longitude", devicesetup.getLongitude());
                response.put("isNewDevice","false");
            }

            response.put("password", agent.getPassword());
            response.put("activated", Integer.valueOf(agent.getActivated()).toString());
            response.put("accountNo", agent.getAccountNo());
            response.put("agentName", agent.getFirstName() + " "+ agent.getLastName()) ;
            response.put("userName", agent.getUsername()) ;
            response.put("agentId", agent.getAgentId()) ;
            response.put("phoneNumber", agent.getPhoneNumber()) ;
            response.put("address", agent.getAddress()) ;
            response.put("lga", agent.getLga()) ;
            response.put("state", agent.getState()) ;
            response.put("city", agent.getCity()) ;
            WalletDTO walletDTO = walletService.getWalletByNumber(agent.getWalletNumber());
            WalletDTO income = walletService.getWalletByNumber(agent.getIncomeWalletNumber());
            if(walletDTO!=null){
                response.put("walletBalance", walletDTO.getAvailableBalance().toString()) ;
                response.put("walletNumber", walletDTO.getWalletNumber()) ;
            }else{
                response.put("walletBalance", "0.00") ;
                response.put("walletNumber", "0.00") ;
            }

            if(income!=null){
                response.put("incomeBalance", income.getAvailableBalance().toString()) ;
                response.put("incomeNumber", income.getWalletNumber()) ;
            }else{
                response.put("incomeBalance", "0.00") ;
                response.put("incomeNumber", "0.00") ;
            }
            return response ;

        } catch (Exception e) {
            //connection or other errors
            e.printStackTrace();
        }
        return response;
    }

    public boolean UpdateAgentStatus(String agentId, String deviceId) {
        Devicesetup devicesetup = deviceSetupRepository.findByAgentId(Long.parseLong(agentId));
        devicesetup.setStatus(1);
        devicesetup.setDeviceid(deviceId);
        deviceSetupRepository.save(devicesetup);
        return true ;
    }

}
