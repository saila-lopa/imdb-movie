package com.shaan.api.myapp;

import com.google.common.collect.ImmutableMap;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static com.shaan.api.myapp.constant.CustomJsonTagName.*;


@Component
public abstract class BaseComponent {

    private Locale locale = LocaleContextHolder.getLocale();

    private static final Logger logger = LoggerFactory.getLogger(BaseComponent.class);

    @Autowired
    private MessageSource messageSource;

    protected String message;

    public String getMessage(String message) {
        try {
            return message.contains(".") ? messageSource.getMessage(message, null, locale) : message;
        } catch (NoSuchMessageException ex) {
            logger.warn("No such message found, the reason behind this was : ", ex.getMessage());
            return message;
        }
    }

    public String getMessage(String message, Object[] param) {
        try {
            return message.contains(".") ? messageSource.getMessage(message, param, locale) : message;
        } catch (NoSuchMessageException ex) {
            logger.warn("No such message found, the reason behind this was : ", ex.getMessage());
            return message;
        }
    }
    protected Tika getTikaParser(){
        return new Tika();
    }

    protected ResponseEntity<?> getResponse(String statusType, String message, Object data, Integer status) {
        if (statusType.equals(ERROR))
            return ResponseEntity.status(status).body(
                    ImmutableMap.of(statusType, ImmutableMap.of(STATUS, status, MESSAGE, message, DATA, data)));

        return ResponseEntity.status(status).body(
                ImmutableMap.of(STATUS_TYPE, statusType, STATUS, status, MESSAGE, message, DATA, data));
    }

    protected CompletableFuture<Object> getAsyncResponse(CompletableFuture<Object> data) {
        return data;
    }

    protected ResponseEntity<?> getResponse(String statusType, String message, Object data,  Integer size, Integer status) {
        return ResponseEntity.status(status).body(
                ImmutableMap.of(STATUS_TYPE, statusType, STATUS, status, MESSAGE, message, DATA, data, TOTAL, size));
    }

    public ResponseEntity<?> getResponse(String statusType, String message, Integer status) {
        if (statusType.equals(ERROR))
            return ResponseEntity.status(status).body(
                    ImmutableMap.of(statusType, ImmutableMap.of(STATUS, status, MESSAGE, message)));

        return ResponseEntity.status(status).body(
                ImmutableMap.of(STATUS, status, STATUS_TYPE, statusType, MESSAGE, message));

    }

    protected Function<Throwable, ResponseEntity> handleGetUsersFailure = throwable -> {
        logger.error("$$$ Unable to retrieve users to import $$$ ", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

    protected Function<Throwable, ResponseEntity> handleIgnored = throwable -> {
        logger.error("$$$ Unable to retrieve users to import $$$ ", throwable);
        return null;
    };

    protected Function<Optional<?>, ResponseEntity> mapMaybeToResponse = maybeUser -> maybeUser
            .<ResponseEntity>map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());

    public Function<String, Function<Throwable, ResponseEntity>> handleGetUserFailure = userId -> throwable -> {
        logger.error(String.format("Unable to retrieve user for id: %s", userId), throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };
}
