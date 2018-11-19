package com.tbs.app.service;

import com.tbs.app.constants.HttpStatusCode;
import com.tbs.app.model.User;
import com.tbs.app.payload.response.ApiResponse;
import com.tbs.app.payload.response.BalanceResponse;
import com.tbs.app.security.UserPrincipal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.tbs.app.repository.UserRepository;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    public User getCurrentUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            return null;
        }
        return userOpt.get();
    }

    @Override
    public ResponseEntity<?> balance(UserPrincipal currentUser) {
        User user = getCurrentUser(currentUser.getId());
        if (user == null) {
            return new ResponseEntity(new ApiResponse(false, HttpStatusCode.BAD_REQUEST.asText(), "bad request", null),
                    HttpStatus.BAD_REQUEST);
        }

        BalanceResponse res = new BalanceResponse();
        res.setCurrency("USD");
        res.setBalance(user.getAcount().getBalance());
        res.setTransactions(user.getAcount().getTransactions());
        
        return ResponseEntity.ok(new ApiResponse(true, null, "", res));
    }

}
