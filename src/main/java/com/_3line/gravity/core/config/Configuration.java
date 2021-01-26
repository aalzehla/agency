package com._3line.gravity.core.config;


import com._3line.gravity.core.utils.HibernateProxyTypeAdapter;
import com._3line.gravity.core.utils.SmartShutDownAndStartUp;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.TimeZone;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        String[] baseNames = new String[]{"i18n/messages", "i18n/menu"};
        source.setBasenames(baseNames);  // name of the resource bundle
        source.setCacheSeconds(1000);
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }


    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
            return modelMapper;
    }

    @Bean
    public Gson gson () {
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        return b.create() ;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    @Primary
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        final Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
        jackson2ObjectMapperBuilder.timeZone(TimeZone.getDefault());
//        jackson2ObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
        jackson2ObjectMapperBuilder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return jackson2ObjectMapperBuilder;
    }

    @Bean
    SmartShutDownAndStartUp smartShutDownAndStartUp()
    {
        return new SmartShutDownAndStartUp();
    }

    @Bean(name = "sanefTaskExecutor")
    public TaskExecutor specificTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        return executor;
    }
}
