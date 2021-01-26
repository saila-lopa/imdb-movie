package com.shaan.api.myapp.exceptions;

import java.util.Set;

import static com.shaan.api.myapp.constant.AppConstant.SC_BAD_REQUEST;

public class ApiBadRequestException extends ApiException {

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ApiBadRequestException() {
    }

    public ApiBadRequestException(String message) {
        super(message);
    }

    public ApiBadRequestException(String message, Object extraMessage) {
        super(message, extraMessage);
    }

    public ApiBadRequestException(Set errors) {
        super(errors);
    }

    public ApiBadRequestException(Object error) {
        super(error);
    }

    /**
     * @param cause
     */
    public ApiBadRequestException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getServiceStatus() {
        return SC_BAD_REQUEST;
    }

    public String getServiceMessage() {
        return "Bad Request";
    }

}
