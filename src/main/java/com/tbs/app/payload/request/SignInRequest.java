package com.tbs.app.payload.request;

import javax.validation.constraints.NotBlank;

public class SignInRequest {

    public SignInRequest() {
    }

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    
    
    @NotBlank(message = "email must not blank")
    private String email;

    @NotBlank(message = "password must not blank")
    private String password;

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
