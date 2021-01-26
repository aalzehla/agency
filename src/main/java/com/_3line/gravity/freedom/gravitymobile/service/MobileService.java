package com._3line.gravity.freedom.gravitymobile.service;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.api.users.agents.dto.AgentSetupDto;
import com._3line.gravity.api.auth.dto.LoginDto;
import com._3line.gravity.freedom.gravitymobile.oauth.model.AgentsDetails;
import com._3line.gravity.freedom.gravitymobile.oauth.model.OAuth2Response;
import com._3line.gravity.api.users.agents.dto.AgentSetupResponse;
import com._3line.gravity.freedom.utility.ApiCallLogs;
import com._3line.gravity.freedom.utility.PropertyResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class MobileService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MobileResponseService auth;
    @Autowired
    private LoginAndSetupService logon;
    private Gson json = new Gson();
    @Autowired
    PropertyResource pr;
    @Autowired
    private OAuth2Service oauth2;

    public String getBearerToken(HttpServletRequest request) {

        OAuth2Response response = new OAuth2Response();
        try {
            AgentsDetails usernameuserid = oauth2.validateOauthAccessRequestHeaders(request);

            response.setAccess_token(oauth2.generateOauthToken(usernameuserid));
            response.setRespCode("00");
            response.setRespDescription(pr.getV("00", "response.properties"));
            logger.warn(response.getRespDescription());
        } catch (GravityException e) {
            response = (OAuth2Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String agentSetup(AgentSetupDto request) {

        AgentSetupResponse response = null;
        try {
//            response = logon.agentSetup(request);
        } catch (GravityException e) {
            response = (AgentSetupResponse) e.getObj();
        }
        return json.toJson(response);

    }

    public String changePassword(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = logon.changePassword(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String forgotPassword(HttpServletRequest request) {

        Response response ;
        try {
            response = logon.forgotPassword(request, oauth2.validateRequestHeaders(request, false));
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String pinChange(HttpServletRequest request) {

        Response response ;
        try {
            response = logon.pinChange(request, oauth2.validateRequestHeaders(request, false));
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public Response logon(LoginDto loginDto) {
        Response response = null;
//        try {
//            response = logon.logon(loginDto);
//        } catch (GravityException e) {
//            response = (Response) e.getObj();
//        }
        return response;

    }

    public String savings(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.savings(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String cardWithdrawals(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.cardWithdrawals(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String cardWithdrawalsOTP(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.cardWithdrawalsOTP(authorization, oauth2.validateRequestHeaders(request, false), callLog);

        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String nameEnquiry(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.nameEnquiry(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String agentBalance(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.agentBalance(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String interBankTransfer(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.interBankTransfer(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String walletBalance(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.getWalletBalance(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String walletHistory(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.getWalletTranHistory(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String incomeWalletHistory(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.getInWalletTranHistory(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String disburseWallet(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.disburseWallet(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String disburseIncomeWallet(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.disburseIncomeWallet(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String transferBetweenWallet(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response = null ;
//        try {
//            response = auth.transferBetweenWallet(authorization, oauth2.validateRequestHeaders(request, false), callLog);
//        } catch (GravityException e) {
//            response = (Response) e.getObj();
//        }
        return json.toJson(response);

    }

    public String creditRequest(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.creditRequest(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String creditRequestHistory(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.creditRequestHistory(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

    public String getAllBanks(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.getAllBanks(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        logger.info("response will be cconverted here");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(response);
            logger.info("response is {}", res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getBankDetails(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.getBankDetails(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        logger.info("response will be converted here");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(response);
            logger.info("response is {}", res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String logIssues(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.logIssues(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        logger.info("response will be converted here");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(response);
            logger.info("response is {}", res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getCommissionFee(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.getCommissionFee(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        logger.info("response will be converted here");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(response);
            logger.info("response is {}", res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String registerForThrift(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.registerForThrift(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        logger.info("response will be converted here");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(response);
            logger.info("response is {}", res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String contributeThrift(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.contributeThrift(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        logger.info("response will be converted here");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(response);
            logger.info("response is {}", res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String thriftDetails(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.thriftDetails(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        logger.info("response will be converted here");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(response);
            logger.info("response is {}", res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String prematureLiquidation(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.prematureLiquidation(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        logger.info("response will be converted here");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(response);
            logger.info("response is {}", res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String openAccount(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.openAccount(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        logger.info("response will be converted here");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(response);
            logger.info("response is {}", res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getBillerCategoryies(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.getBillerCategory(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        logger.info("response will be converted here");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(response);
            logger.info("response is {}", res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getBillerCategoryServices(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.getBillServiceForCategory(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        logger.info("response will be converted here");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(response);
            logger.info("response is {}", res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getBillerServiceOptions(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.getBillOptionsForService(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        logger.info("response will be converted here");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(response);
            logger.info("response is {}", res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String paybills(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.payBills(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        logger.info("response will be converted here");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(response);
            logger.info("response is {}", res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String validateBillerCustomer(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.validateBillerCustomer(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        logger.info("response will be converted here");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String res = mapper.writeValueAsString(response);
            logger.info("response is {}", res);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String transactionHistory(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.getTranHistory(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }


    public String getTranHistorySendEmail(String authorization, HttpServletRequest request, ApiCallLogs callLog) {

        Response response ;
        try {
            response = auth.getTranHistorySendEmail(authorization, oauth2.validateRequestHeaders(request, false), callLog);
        } catch (GravityException e) {
            response = (Response) e.getObj();
        }
        return json.toJson(response);

    }

}
