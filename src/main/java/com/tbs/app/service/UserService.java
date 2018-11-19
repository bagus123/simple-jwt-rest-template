package com.tbs.app.service;

import com.tbs.app.model.User;
import com.tbs.app.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public User getCurrentUser(Long id);

    public ResponseEntity<?> balance(UserPrincipal currentUser);

}
