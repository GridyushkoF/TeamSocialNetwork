package ru.skillbox.authentication.authentication;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


public class AuthenticationRequest {

    private String email;

    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}