package com.shaan.api.myapp.authentication;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class UserAuthBean implements Serializable {

    @ApiModelProperty(value = "usernname", example = "sailacse@gmail.com")
    private String username;
    @ApiModelProperty(value = "password", example = "Pass1234")
    private String password;
    @ApiModelProperty(hidden = true)
    private String accessToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
