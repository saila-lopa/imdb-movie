package com.shaan.api.myapp.exceptions;


import static com.shaan.api.myapp.constant.AppConstant.SC_UNAUTHORIZED;

public class ApiAuthorizationException extends ApiException {

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ApiAuthorizationException() {
    }

    public ApiAuthorizationException(String message) {
        super(message);
    }

    public ApiAuthorizationException(String message, Object extraMessage) {
        super(message, extraMessage);
    }

    /**
     * @param cause
     */
    public ApiAuthorizationException(Throwable cause) {
        super(cause);
    }

    public ApiAuthorizationException(Object error) {
        super(error);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getServiceStatus() {
        return SC_UNAUTHORIZED;
    }

    public String getServiceMessage() {
        return "Unauthorized";
    }

}
