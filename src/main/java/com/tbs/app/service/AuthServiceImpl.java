package com.tbs.app.service;

import com.tbs.app.constants.HttpStatusCode;
import com.tbs.app.exception.AppException;
import com.tbs.app.model.Account;
import com.tbs.app.model.Role;
import com.tbs.app.model.Transaction;
import com.tbs.app.model.User;
import com.tbs.app.model.enums.RoleName;
import com.tbs.app.model.enums.TrxSide;
import com.tbs.app.model.enums.TrxType;
import com.tbs.app.payload.request.SignInRequest;
import com.tbs.app.payload.request.SignUpRequest;
import com.tbs.app.payload.response.ApiResponse;
import com.tbs.app.payload.response.JwtAuthResponse;
import com.tbs.app.security.JwtTokenProvider;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.tbs.app.repository.AccountRepository;
import com.tbs.app.repository.RoleRepository;
import com.tbs.app.repository.TransactionRepository;
import com.tbs.app.repository.UserRepository;

@Service("AuthService")
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    private TransactionRepository transactionRep;

    @Override
    public ResponseEntity<?> signIn(SignInRequest body) {
        Optional<User> optUser = userRepository.findByEmail(body.getEmail());
        if (!optUser.isPresent()) {
            return new ResponseEntity(new ApiResponse(false, HttpStatusCode.BAD_REQUEST.asText(), "Email/Username or Password not valid!", null),
                    HttpStatus.BAD_REQUEST);
        }

        User user = optUser.get();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        body.getEmail(),
                        body.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        JwtAuthResponse res = new JwtAuthResponse(jwt);

        return ResponseEntity.ok(new ApiResponse(true, null, "token generated", res));
    }

    @Override
    public ResponseEntity<?> signUp(SignUpRequest body) {
        if (userRepository.existsByEmail(body.getEmail())) {
            logger.info("signup : email " + body.getEmail() + " already exist");
            return new ResponseEntity(new ApiResponse(false, HttpStatusCode.BAD_REQUEST.asText(), "Email Address already in use!", null),
                    HttpStatus.BAD_REQUEST);
        }

        String[] arrStr = body.getEmail().split("@");
        String userName = arrStr[0];

        User user = new User(userName,
                body.getEmail(), body.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));
        User userSaved = userRepository.save(user);

        Account account = new Account();
        account.setCurrency("USD");
        account.setBalance(100d);
        account.setUser(userSaved);
        Account accountSaved = accountRepository.save(account);

        Transaction trx = new Transaction();
        trx.setAccount(accountSaved);
        trx.setAmount(100d);
        trx.setRemark("initiate");
        trx.setSide(TrxSide.CREDIT);
        trx.setType(TrxType.DEPOSIT);
        trx.setTrxDate(new Date());
        transactionRep.save(trx);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(user.getUserName()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, null, "User registered successfully. Please login", null));
    }

}
