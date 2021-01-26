package com._3line.gravity.freedom._3linecms.service.impl;

import com._3line.gravity.freedom._3linecms.dtos.CMSRequest;
import com._3line.gravity.freedom._3linecms.dtos.CMSResponse;
import com._3line.gravity.freedom._3linecms.quicktellercms.QuickTellerCMService;
import com._3line.gravity.freedom._3linecms.quicktellercms.QuickTellerCMServiceService;
import com._3line.gravity.freedom._3linecms.saopservice.CustomerAccountService;
import com._3line.gravity.freedom._3linecms.saopservice.CustomerAccountServiceService;
import com._3line.gravity.freedom._3linecms.service.CMSService;
import com._3line.gravity.freedom._3linecms.service.CreditRequest;
import com._3line.gravity.freedom.utility.CustomSoapHandler;
import com._3line.gravity.freedom.utility.SOAPLogger;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import javax.xml.ws.BindingProvider;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class CMSServiceImpl implements CMSService , InitializingBean {

    @Autowired
    private TemplateEngine templateEngine;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${cms.qt.user}")
    private String qtUser;

    @Value("${cms.qt.pass}")
    private String qtPass;

    @Value("${cms.qt.curr_code}")
    private String qtCurrCode;




    @Override
    public CMSResponse uploadCardDetails(CMSRequest cmsRequest) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYY-MM-dd");
        CustomerAccountServiceService accountServiceService = new CustomerAccountServiceService();
        CustomerAccountService customerAccountService = accountServiceService.getCustomerAccountService() ;

        CustomSoapHandler soapHandler = new CustomSoapHandler() ;
        SOAPLogger.dumpRequestResponse((BindingProvider)customerAccountService,soapHandler);

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        Map<String, Object> model = new HashMap<>();
        model.put("idnumber",cmsRequest.getUserid());
        model.put("userid",cmsRequest.getUserid());
        model.put("regnum",cmsRequest.getRegnum());
        model.put("surname",cmsRequest.getSurname());
        model.put("firstname",cmsRequest.getFirstname());
        model.put("dob",cmsRequest.getDob());
        model.put("accountOpenDate",simpleDateFormat.format(new Date()));
        model.put("residence",cmsRequest.getResidence());
        model.put("address",cmsRequest.getAddress());
        model.put("phone",cmsRequest.getPhone());
        model.put("email",cmsRequest.getEmail());
        model.put("accountNum",cmsRequest.getAccountNum());
        model.put("pan",cmsRequest.getPan());
        IContext context = new Context();
        ((Context) context).setVariables(model);

        String body =  templateEngine.process("cms/cardupload.html" ,context);

        //TODO fix velocity dependency issue
        logger.info("request  {}", body);
        String response  = customerAccountService.sendDataTo3Line(body);

        String converted  = "";

        if(response!=null && !response.isEmpty()){
            CharSequence lessThanEscaped ="&lt;";
            CharSequence lessThan ="<";
            CharSequence greaterThanEscaped ="&gt;";
            CharSequence greaterThan =">";
            converted = response.replace(lessThanEscaped,lessThan).replace(greaterThanEscaped, greaterThan);
        }

        logger.info("converted response {}", converted);

        CMSResponse cmsResponse = new CMSResponse();
        cmsResponse.setRespcode(converted);
        if (cmsResponse.getRespcode()==null){
            cmsResponse.setRespcode("ERR");
        }else if(cmsResponse.getRespcode().contains("OK :")) {
            if(StringUtils.substringBetween(converted, "<respdesc>", "</respdesc>")!=null &&
                    !StringUtils.substringBetween(converted, "<respdesc>", "</respdesc>").equals("")){
                cmsResponse.setRespdesc(StringUtils.substringBetween(converted, "<respdesc>", "</respdesc>").replace("OK", "").replace(":", ""));
            }else{
                cmsResponse.setRespdesc(converted);
            }

        }else if(cmsResponse.getRespcode().contains("ERR :")) {
            if(StringUtils.substringBetween(converted, "<respdesc>", "</respdesc>")!=null &&
                    !StringUtils.substringBetween(converted, "<respdesc>", "</respdesc>").equals("")){
                cmsResponse.setRespdesc(StringUtils.substringBetween(converted, "<respdesc>", "</respdesc>").replace("ERR", "").replace(":", ""));
            }else{
                cmsResponse.setRespdesc(converted);
            }
        }
        return cmsResponse;
    }

    @Override
    public CMSResponse fundCard(CreditRequest creditRequest) {


         QuickTellerCMServiceService quickTellerCMServiceService = new QuickTellerCMServiceService();
        QuickTellerCMService cmsCardService = quickTellerCMServiceService.getQuickTellerCMService();
        CustomSoapHandler soapHandler = new CustomSoapHandler() ;
        SOAPLogger.dumpRequestResponse((BindingProvider)cmsCardService,soapHandler);

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        Map<String, Object> model = new HashMap<>();
        model.put("refNum",creditRequest.getRefNumber());
        model.put("regNum",creditRequest.getRegNum());
        model.put("pan",creditRequest.getPan());
        model.put("amount",creditRequest.getAmount());
        model.put("senderInfo",creditRequest.getSenderInfo());

        model.put("qtUser",qtUser);
        model.put("qtPass",qtPass);
        model.put("qtCurrCode",qtCurrCode);


        IContext context = new Context();
        ((Context) context).setVariables(model);

        String body =  templateEngine.process("cms/qtfund.html" ,context);


        String response  = cmsCardService.executePayment(body);

        String converted  = "";

        if(response!=null && !response.isEmpty()){
            CharSequence lessThanEscaped ="&lt;";
            CharSequence lessThan ="<";
            CharSequence greaterThanEscaped ="&gt;";
            CharSequence greaterThan =">";
            converted = response.replace(lessThanEscaped,lessThan).replace(greaterThanEscaped, greaterThan);
        }

        logger.info("request {}", soapHandler.getRequest());
        logger.info("converted response {}", converted);

        CMSResponse cmsResponse = new CMSResponse();
        cmsResponse.setRespcode(StringUtils.substringBetween(converted, "<errCode>","</errCode>"));
        cmsResponse.setRespdesc(StringUtils.substringBetween(converted, "<errDesc>","</errDesc>"));
        return cmsResponse;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
