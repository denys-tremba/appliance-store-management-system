package com.example.rd.autocode.assessment.appliances.user.login;

import com.example.rd.autocode.assessment.appliances.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
