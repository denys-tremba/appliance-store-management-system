package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginLimitingAuthProviderProxy implements AuthenticationProvider {
    private final DaoAuthenticationProvider delegate;
    private final FailedLoginAttemptsCache cache;
    @Value("${login.attempts.fails}")
    @Getter
    private int attempts;

    @Override
    public Authentication authenticate( Authentication authentication) throws AuthenticationException {
        int attempts = cache.get(authentication);
        if (attempts > this.attempts) {
            throw new DisabledException("Not so fast🤠. Cool down for " + cache.getTtlS() + "s");
        }
        return delegate.authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return delegate.supports(authentication);
    }
}
