package com._3line.gravity.core.notification;

import com._3line.gravity.core.email.service.MailService;
import com._3line.gravity.core.setting.service.SettingService;
import com._3line.gravity.core.sms.service.SmsService;
import com._3line.gravity.core.usermgt.model.AbstractUser;
import com._3line.gravity.freedom.utility.PropertyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * @author FortunatusE
 * @date 11/14/2018
 */


@Service
public class NotificationServiceImpl implements NotificationService {


    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final SettingService settingService;
    private final MailService mailService;

    private final SmsService smsService;

    PropertyResource propertyResource;

    @Autowired
    public NotificationServiceImpl(SettingService settingService, MailService mailService, @Qualifier("kiaKiaSMSImplementation") SmsService smsService,PropertyResource propertyResource) {
        this.settingService = settingService;
        this.mailService = mailService;
        this.smsService = smsService;
        this.propertyResource = propertyResource;
    }

    @Async
    @Override
    public void sendUserCreationMessage(AbstractUser user , String pass) {

        if (settingService.isSettingAvailable("USER_CREATION_SEND_EMAIL")) {
            Map params = new HashMap();
            params.put("name", user.getFirstName() + " " + user.getLastName());
            params.put("username", user.getUserName());
            params.put("password", pass);
            mailService.sendMail("You have been profiled !", user.getEmail(), null, params, "user_creation");
        }

        if (settingService.isSettingAvailable("USER_CREATION_SEND_SMS")) {
            String template = propertyResource.getV("user_creation", "sms_messages.properties");
            String message = String.format(template,user.getFirstName() + " " + user.getLastName(),
                    user.getUserName(),pass);
            smsService.sendPlainSms(user.getPhoneNumber(), message);
        }
    }

    @Async
    @Override
    public void sendUserUpdateMessage(AbstractUser abstractUser) {

        //            if (settingService.isSettingAvailable("USER_UPDATE_SEND_EMAIL")) {
//                Map params = new HashMap();
//                params.put("name", applicationUser.getFirstName() + " " + applicationUser.getLastName());
//                params.put("username", applicationUser.getUserName());
//                params.put("password", "");
//                mailService.sendMail("Your profile has been updated!", applicationUser.getEmail(), null, params, "user_creation");
//            }
    }
}
