package com.shaan.api.myapp.controller;

import com.google.common.collect.ImmutableMap;
import com.shaan.api.myapp.BaseComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static com.shaan.api.myapp.constant.CustomJsonTagName.*;

@Component
public abstract class BaseController extends BaseComponent {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected ResponseEntity<?> getResponse(String statusType, String message, Object data, Integer status) {
        if (statusType.equals(ERROR))
            return ResponseEntity.status(status).body(
                    ImmutableMap.of(statusType, ImmutableMap.of(STATUS, status, MESSAGE, message, DATA, data)));

        return ResponseEntity.status(status).body(
                ImmutableMap.of(STATUS_TYPE, statusType, STATUS, status, MESSAGE, message, DATA, data));
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
}
