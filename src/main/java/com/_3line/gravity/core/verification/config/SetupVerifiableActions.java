package com._3line.gravity.core.verification.config;


import com._3line.gravity.core.verification.annotations.RequireApproval;
import com._3line.gravity.core.verification.exceptions.VerificableActionException;
import com._3line.gravity.core.verification.repository.VerifiableActionRepository;
import com._3line.gravity.core.verification.service.VerifiableActionService;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Set;

@Service
public class SetupVerifiableActions implements CommandLineRunner {

    @Autowired
    private VerifiableActionRepository verifiableActionRepository ;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private VerifiableActionService verifiableActionService;


    @Override
    public void run(String... args) throws Exception {
        logger.info("setting up verifiable entities .......");
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com._3line.gravity"))
                .setScanners(new MethodAnnotationsScanner()
                ));

        Set<Method> methods = reflections.getMethodsAnnotatedWith(RequireApproval.class);

        for (Method method : methods) {

            RequireApproval requireApproval = method.getAnnotation(RequireApproval.class);
            String code = requireApproval.code();
            logger.debug("approval: {}", code);

            try {
                verifiableActionService.addVerifiableAction(code);
            }catch (VerificableActionException a){
              // a.printStackTrace();
            }

        }
    }
}
