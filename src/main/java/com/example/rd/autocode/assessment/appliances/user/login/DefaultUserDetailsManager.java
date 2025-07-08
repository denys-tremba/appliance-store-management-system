package com.example.rd.autocode.assessment.appliances.user.login;

import com.example.rd.autocode.assessment.appliances.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DefaultUserDetailsManager implements UserDetailsManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void changePassword(org.springframework.security.core.userdetails.User user, String newPassword) {
        String email = user.getUsername();
        User domainUser = userRepository.findByEmail(email).orElseThrow();
        String oldPassword = user.getPassword();
        String encoded = passwordEncoder.encode(newPassword);
        domainUser.setPassword(encoded);
        log.trace("⚠ Password changed for user {} [{}]->[{}]", email, oldPassword, encoded);
    }
    @Override
    public void createUser(UserDetails user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        userRepository.deleteById(user.getId());
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException(
                    "Can't change password as no Authentication object found in context " + "for current user.");
        }
        String username = currentUser.getName();
        User domainUser = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
        String encoded = passwordEncoder.encode(newPassword);
        domainUser.setPassword(encoded);
        log.trace("⚠ Password changed for user {} [{}]->[{}]", username, oldPassword, encoded);
    }

    @Override
    public boolean userExists(String username) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User %s could not be found".formatted(username)));
        String role = user.getClass().getSimpleName().toUpperCase();
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPassword()).roles(role).accountLocked(user.getLocked()).build();
    }
}
