package com.tbs.app.service;

import com.tbs.app.payload.request.DepositRequest;
import com.tbs.app.payload.request.WithdrawalRequest;
import com.tbs.app.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

public interface TransactionService {

    public ResponseEntity<?> deposit(DepositRequest body, UserPrincipal currentUser);

    public ResponseEntity<?> withdrawal(WithdrawalRequest body, UserPrincipal currentUser);
}
