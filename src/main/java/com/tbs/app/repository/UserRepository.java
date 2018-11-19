package com.tbs.app.repository;

import com.tbs.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserNameOrEmail(String username, String email);

    Optional<User> findByUserName(String username);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);
    User save(User user);
    

}
