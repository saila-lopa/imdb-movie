package com.shaan.api.myapp.constant;

public class AppConstant {
    public static final String SSL_SCHEME                   = "https";
    public static final String NON_SSL_SCHEME               = "http";
    public static final String ROLE_ADMIN                   = "ROLE_ADMIN";
    public static final String ROLE_USER                    = "ROLE_USER";
    public static final Integer SC_INTERNAL_SERVER_ERROR    = 500;
    public static final Integer SC_OK                       = 200;
    public static final Integer SC_ACCEPTED                 = 202;
    public static final Integer SC_BAD_REQUEST              = 400;
    public static final Integer SC_UNAUTHORIZED             = 401;
    public static final Integer SC_PAYMENT_REQUIRED         = 402;
    public static final Integer SC_FORBIDDEN                = 403;
    public static final Integer SC_NOT_FOUND                = 404;
    public static final Integer SC_METHOD_NOT_ALLOWED       = 405;
    public static final Integer SC_NOT_ACCEPTABLE           = 406;
    public static final Integer SC_PRECONDITION_FAILED      = 412;
    public static final Integer SC_UNSUPPORTED_MEDIA_TYPE   = 415;
    public static final Integer SC_UNPROCESSABLE_ENTITY     = 422;
    public static final Integer SC_BAD_GATEWAY              = 502;
    public static final Integer SC_SERVICE_UNAVAILABLE      = 503;
    public static final Integer SC_GATEWAY_TIMEOUT          = 504;
}
