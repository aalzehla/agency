package com._3line.gravity;

import com._3line.gravity.core.files.config.FileStorageProperties;
import com._3line.gravity.core.repository.CustomJpaRepositoryFactoryBean;

import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.thirdparty.service.ThirdPartyService;
import com._3line.gravity.core.utils.AppUtility;
import com._3line.gravity.core.utils.SmartShutDownAndStartUp;
import com._3line.gravity.freedom.agents.dtos.AgentDto;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.billpayment.service.Billservice;
//import com._3line.gravity.freedom.service.service.ItexFix;
import com._3line.gravity.freedom.dashboard.DashboardService;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.netflix.discovery.converters.Auto;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@SpringBootApplication
//@EnableCaching
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomJpaRepositoryFactoryBean.class)
@EnableConfigurationProperties({FileStorageProperties.class})
//@EnableEurekaClient
public class GravityApplication  extends SpringBootServletInitializer implements CommandLineRunner {


//    Logger logger = LoggerFactory.getLogger(GravityApplication.class);

//    @Autowired
//    Billservice billservice;
//    @Autowired
//    ItexFix itexFix;
//    @Autowired
//    DashboardService dashboardService;
//    @Autowired
//    ThirdPartyService partyService;
//    @Autowired
//    AgentsRepository agentsRepository;
//    @Autowired
//    AgentService agentService;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;

    @Autowired
    SettingService settingService;
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(GravityApplication.class, args);
        SmartShutDownAndStartUp myBean = context.getBean(SmartShutDownAndStartUp.class);
//        myBean.sendStartUpMail();
    }

    @Override
    public void run(String... strings) throws Exception {


//
//        String appKey = "446c2d2956fa225f1f56d2c425a233c5";
//
//        byte [] key = Hex.decodeHex(appKey.toCharArray());
//
//        Cipher cipher = Cipher.getInstance("AES");
//
//        SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
//
//        cipher.init(Cipher.ENCRYPT_MODE, originalKey);
//
//        String data = "n" +
//                "\t\"amount\": 1170000,\n" +
//                "\t\"terminalId\": \"2058LR00\",\n" +
//                "\t\"statusCode\": \"00\",\n" +
//                "\t\"pan\": \"418742******5676\",\n" +
//                "\t\"rrn\": \"100101660366\",\n" +
//                "\t\"reversal\": false,\n" +
//                "\t\"stan\": \"115233\",\n" +
//                "\t\"bank\": \"GTBank\",\n" +
//                "\t\"transactionType\": \"3Line\",\n" +
//                "\t\"productId\": \"3LINE001\",\n" +
//                "\t\"transactionTime\": \"2018-12-31 11:52:33\"\n" +
//                "}";
//        System.out.println("**********");
//
//        String encrypted = Base64.encodeBase64String(cipher.doFinal(data.getBytes("UTF-8")));
//        System.out.println("encrypted is "+ encrypted);




    }

//    public  String generateAgentId(Agents agent){
//        String AB = "1234567890" ;
//        Random rnd = new Random();
//        StringBuilder sb = new StringBuilder(4);
//        for (int i = 0; i < 4; i++) {
//            sb.append(AB.charAt(rnd.nextInt(AB.length())));
//        }
//        AB = StringUtils.substring(agent.getFirstName().toUpperCase(),0 , 2) + StringUtils.substring(agent.getLastName().toUpperCase(),0 , 2) + sb.toString() ;
//        return AB;
//    }

}
