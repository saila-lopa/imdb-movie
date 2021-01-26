package com.shaan.api.myapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class CustomAppInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger logger = LoggerFactory.getLogger(CustomAppInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext ac) {
        try {
            ConfigurableEnvironment environment = ac.getEnvironment();
            logger.info("param.store.prefix: {}", environment.getProperty("param.store.prefix"));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}