package com.shaan.api.myapp.authentication;

public enum AuthenticationAndAuthorizationConstant {
    ACCESS_TOKEN_EXPIRED_MINUTES(30);

    private long value;

    AuthenticationAndAuthorizationConstant(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
