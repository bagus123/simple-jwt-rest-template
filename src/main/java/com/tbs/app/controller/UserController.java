package com.tbs.app.controller;

import com.tbs.app.security.CurrentUser;
import com.tbs.app.security.UserPrincipal;
import com.tbs.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/balance")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> balance(@CurrentUser UserPrincipal currentUser) {
        return userService.balance(currentUser);
    }
}
