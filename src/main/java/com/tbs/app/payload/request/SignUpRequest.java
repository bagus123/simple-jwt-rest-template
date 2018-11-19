package com.tbs.app.payload.request;

import javax.validation.constraints.*;

public class SignUpRequest {

    public SignUpRequest() {
    }

    public SignUpRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    
    
    
    @NotBlank(message = "email must not blank")
    @Size(max = 255)
    @Email
    private String email;

    @NotBlank(message = "password must not blank, min 6 char and max 20 char")
    @Size(min = 6, max = 20)
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
