package com.tbs.app.controller;

import com.tbs.app.payload.request.DepositRequest;
import com.tbs.app.payload.request.WithdrawalRequest;
import com.tbs.app.security.CurrentUser;
import com.tbs.app.security.UserPrincipal;
import com.tbs.app.service.TransactionService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trx")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deposit(@Valid @RequestBody DepositRequest body,
            @CurrentUser UserPrincipal currentUser) {
        return transactionService.deposit(body, currentUser);
    }

    @PostMapping("/withdrawal")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> withdrawal(@Valid @RequestBody WithdrawalRequest body,
            @CurrentUser UserPrincipal currentUser) {
        return transactionService.withdrawal(body, currentUser);
    }
}
