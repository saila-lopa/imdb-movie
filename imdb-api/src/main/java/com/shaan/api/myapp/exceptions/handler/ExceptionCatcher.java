package com.shaan.api.myapp.exceptions.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.collect.ImmutableMap;
import com.shaan.api.myapp.exceptions.ApiException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

import static com.shaan.api.myapp.constant.AppConstant.SC_BAD_REQUEST;
import static com.shaan.api.myapp.constant.AppConstant.SC_UNSUPPORTED_MEDIA_TYPE;
import static com.shaan.api.myapp.constant.AppConstant.SC_INTERNAL_SERVER_ERROR;
import static com.shaan.api.myapp.constant.CustomJsonTagName.ERROR;
import static com.shaan.api.myapp.constant.CustomJsonTagName.STATUS;
import static com.shaan.api.myapp.constant.CustomJsonTagName.MESSAGE;
import static com.shaan.api.myapp.constant.CustomJsonTagName.DETAILS;
import static com.shaan.api.myapp.constant.CustomJsonTagName.FIELD_NAME;
import static com.shaan.api.myapp.constant.CustomJsonTagName.STATUS_TYPE;

@ControllerAdvice
public class ExceptionCatcher {
    String message;

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleBadRequest(final Exception ex) {
        if (ex instanceof ApiException) {
            Integer status = ((ApiException) ex).getServiceStatus();
            return ResponseEntity.status(status).body(ImmutableMap.of(ERROR, ImmutableMap.of(STATUS, status, MESSAGE, ex.getMessage())));
        }

        if (ex instanceof MethodArgumentNotValidException) {
            return ResponseEntity.status(SC_BAD_REQUEST).body(ImmutableMap.of(ERROR, ImmutableMap.of(STATUS, SC_BAD_REQUEST,
                    DETAILS, ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().parallelStream().map(fieldError ->
                            ImmutableMap.of(FIELD_NAME, fieldError.getField(), MESSAGE, fieldError.getDefaultMessage())).collect(Collectors.toList()))));
        }

        if (ex instanceof HttpMessageNotReadableException) {
            Throwable mostSpecificCause = ((HttpMessageNotReadableException) ex).getMostSpecificCause();

            if (mostSpecificCause instanceof InvalidFormatException) {
                InvalidFormatException invalidEx = ((InvalidFormatException) mostSpecificCause);

                if (invalidEx.getPath() != null && invalidEx.getPath().size() > 0) {
                    message = "Invalid value for " + invalidEx.getPath().get(0).getFieldName();

                    for (int i = 1; i < invalidEx.getPath().size(); i++)
                        message += "->" + invalidEx.getPath().get(i).getFieldName();
                } else
                    message = mostSpecificCause.getMessage();
            } else {
                if (mostSpecificCause != null)
                    message = mostSpecificCause.getMessage();
                else
                    message = ex.getMessage();
            }

            return ResponseEntity.status(SC_BAD_REQUEST).body(ImmutableMap.of(ERROR, ImmutableMap.of(STATUS, SC_BAD_REQUEST, MESSAGE, message)));
        }

        if (ex instanceof HttpMediaTypeNotSupportedException) {
            message = "Unsupported content type: " + ((HttpMediaTypeNotSupportedException) ex).getContentType()
                    + "\nSupported content types are: " + MediaType.toString(((HttpMediaTypeNotSupportedException) ex).getSupportedMediaTypes());

            return ResponseEntity.status(SC_UNSUPPORTED_MEDIA_TYPE).body(ImmutableMap.of(ERROR, ImmutableMap.of(STATUS, SC_UNSUPPORTED_MEDIA_TYPE, STATUS_TYPE, ERROR, MESSAGE, message)));
        }

        return ResponseEntity.status(SC_INTERNAL_SERVER_ERROR).body(ImmutableMap.of(ERROR, ImmutableMap.of(STATUS, SC_INTERNAL_SERVER_ERROR, MESSAGE, ex.getMessage())));

    }
}