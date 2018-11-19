package com.tbs.app.service;

import com.tbs.app.payload.request.SignInRequest;
import com.tbs.app.payload.request.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    public ResponseEntity<?> signIn(SignInRequest body);

    public ResponseEntity<?> signUp(SignUpRequest body);

}
