package com.tbs.app.security;

import com.tbs.app.exception.ResourceNotFoundException;
import com.tbs.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tbs.app.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userNameOrEmail)
            throws UsernameNotFoundException {
        User user = userRepository.findByUserNameOrEmail(userNameOrEmail, userNameOrEmail)
                .orElseThrow(()
                        -> new UsernameNotFoundException("User not found with username or email : " + userNameOrEmail)
                );

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}
