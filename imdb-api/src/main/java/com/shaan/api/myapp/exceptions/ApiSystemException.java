package com.shaan.api.myapp.exceptions;


import static com.shaan.api.myapp.constant.AppConstant.SC_INTERNAL_SERVER_ERROR;

public class ApiSystemException extends ApiException {
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ApiSystemException() {
    }

    public ApiSystemException(String message) {
        super(message);
    }

    public ApiSystemException(String message, Object extraMessage) {
        super(message, extraMessage);
    }

    /**
     * @param cause
     */
    public ApiSystemException(Throwable cause) {
        super(cause);
    }

    public ApiSystemException(Object error) {
        super(error);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public Integer getServiceStatus() {
        return SC_INTERNAL_SERVER_ERROR;
    }

    public String getServiceMessage() {
        return "Internal Server Error";
    }

}
