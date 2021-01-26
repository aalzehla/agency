package com._3line.gravity.core.email.service.implementation;


import com._3line.gravity.core.email.model.Mail;
import com._3line.gravity.core.email.repository.MailRepository;
import com._3line.gravity.core.email.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MailServiceImpl  implements MailService {


    @Value("${mail.from}")
    private String mailFrom ;

    @Autowired
    private MailRepository mailRepository ;

    public JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    private final ExecutorService executors = Executors.newFixedThreadPool(100);

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender) {
        this.emailSender = mailSender;
    }


    @Async
    @Override
    public void sendMail(String mailSubject, String mailTo,String [] copy, Map<String, Object> parameters, String templateLocation) {

        executors.submit(() ->{
            Mail mail = new Mail();
            if(Objects.nonNull(copy)) mail.setCopy(Arrays.asList(copy));
            mail.setMailHeader(mailSubject);
            mail.setMailTo(mailTo);
            IContext context = new Context();
            ((Context) context).setVariables(parameters);
            mail.setMailContent(templateEngine.process("mail/"+templateLocation ,context));
            logger.debug("MAIL -> {}",mail.toString());
            logger.info("Sending mail to ->{}", mail.getMailTo());
            MimeMessagePreparator messagePreparator = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setFrom(mailFrom);
                messageHelper.setTo(mailTo);
                messageHelper.setSubject(mailSubject);
                messageHelper.setText(mail.getMailContent(),true);
            };
            try {
                mail.setLastSent(new Date());
                emailSender.send(messagePreparator);
                logger.info(" mail sent to ->{}", mail.getMailTo());
            } catch (MailException e) {
//            e.printStackTrace();
                logger.error("Error sending email", e);
//            mail.setFailureReason(e.getMessage());
//                logger.debug("Error sending mail {}", e.getMessage());
                // runtime exception; compiler will not force you to handle it
            }

            mailRepository.save(mail);
        });




    }

    @Async
    @Override
    public void sendMailAttachments(String mailSubject, String mailTo,String [] copy, Map<String, Object> parameters, String templateLocation, Map<String, Objects> attachements) {

    }
}
