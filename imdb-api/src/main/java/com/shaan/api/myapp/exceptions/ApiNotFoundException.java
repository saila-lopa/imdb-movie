package com.shaan.api.myapp.exceptions;

import java.util.Map;

import static com.shaan.api.myapp.constant.AppConstant.SC_NOT_FOUND;

/**
 * ApiNotFoundException
 *
 * @author hossaindoula<hossaindoula@gmail.com>
 */
public class ApiNotFoundException extends ApiException {
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ApiNotFoundException() {
    }

    /**
     * @param message
     */
    public ApiNotFoundException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ApiNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param response
     */
    public ApiNotFoundException(String message, Object response) {
        super(message, response);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param error
     */
    public ApiNotFoundException(Map<String, ?> error) {
        super(error);
    }

    /**
     * @param error
     */
    public ApiNotFoundException(Object error) {
        super(error);
    }

    public Integer getServiceStatus() {
        return SC_NOT_FOUND;
    }

    public String getServiceMessage() {
        return "Api Not Found";
    }
}
