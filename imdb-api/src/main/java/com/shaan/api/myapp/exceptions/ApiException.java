package com.shaan.api.myapp.exceptions;

import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Set;


public class ApiException extends Exception {
    private Object extraMessage;
    private Set<ConstraintViolation<?>> errors;
    private Map<String, ?> error;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ApiException() {
    }

    /**
     * @param message
     */
    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Object extraMessage) {
        super(message);
        this.extraMessage = extraMessage;
    }

    public ApiException(Set<ConstraintViolation<?>> errors) {
        this.errors = errors;
    }

    public ApiException(Map<String, ?> error) {
        this.error = error;
    }

    /**
     * @param cause
     */
    public ApiException(Throwable cause) {
        super(cause);
    }

    /**
     * @param errors
     */
    public ApiException(Object errors) {
        extraMessage = errors;
    }

    /**
     * @param message
     * @param cause
     */
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public Object getExtraMessage() {
        return this.extraMessage;
    }

    public Integer getServiceStatus() {
        return this.getServiceStatus();
    }

    public String getServiceMessage() {
        return this.getMessage();
    }

    public Set<ConstraintViolation<?>> getErrors() {
        return errors;
    }

    public Map<String, ?> getError() {
        return error;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
