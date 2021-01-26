package com.shaan.api.myapp.utils.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

public class TokenManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenManager.class);

    public String getToken() {
        String token = null;

        try {
            LOGGER.info("Start generating a token.");
            token = UUID.randomUUID().toString() + UUID.randomUUID().toString() + LocalDateTime.now();
            LOGGER.debug("token: {}", token);
        } catch (Exception e) {
            LOGGER.error("error '{}'", e);
        }

        return token;
    }
}
