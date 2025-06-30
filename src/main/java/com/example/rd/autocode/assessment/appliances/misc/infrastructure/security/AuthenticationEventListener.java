package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureDisabledEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationEventListener {
    private final FailedLoginAttemptsCache failedLoginAttemptsCache;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        log.debug("🐱‍👤 successful authn {}", success);
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failure) {
        Authentication auth = failure.getAuthentication();
        failedLoginAttemptsCache.increment(auth);
        int attempt = failedLoginAttemptsCache.get(auth);
        log.debug("👣 failed authn {}-th attempt: {}", attempt, failure);
    }
    @EventListener
    public void onFailure(AuthenticationFailureDisabledEvent failure) {
        log.warn("☠ disabled due to {}", failure);
    }
}
