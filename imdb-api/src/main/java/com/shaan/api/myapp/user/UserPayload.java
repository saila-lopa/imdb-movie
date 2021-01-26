package com.shaan.api.myapp.user;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class UserPayload {

    @Length(min = 1, max = 127)
    String firstName;

    @Length(min = 1, max = 127)
    String lastName;

    @NotNull
    @Length(min = 3, max = 255)
    String email;

    @NotNull
    @Length(min = 8, max = 2000)
    String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
