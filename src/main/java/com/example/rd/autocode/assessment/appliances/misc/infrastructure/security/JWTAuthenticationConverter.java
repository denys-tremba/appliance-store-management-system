package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

import java.util.Arrays;
import java.util.Optional;

public class JWTAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> jwtHolder = Arrays.stream(cookies).filter(c -> c.getName().equals("jwt-auth")).findAny();
        if (jwtHolder.isEmpty()) {
            throw new PreAuthenticatedCredentialsNotFoundException("jwt is missing");
        }
        return JwtAuthenticationToken.unauthenticated(jwtHolder.get().getValue());
    }
}
