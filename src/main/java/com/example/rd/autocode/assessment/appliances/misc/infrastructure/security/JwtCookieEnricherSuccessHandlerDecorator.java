package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Slf4j
public class JwtCookieEnricherSuccessHandlerDecorator implements AuthenticationSuccessHandler {
    private final AuthenticationSuccessHandler delegate;
    private final JwtService jwtService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String compactForm;
        try {
            Duration duration = Duration.ofDays(1);
            compactForm = jwtService.create(authentication.getName(), duration.toMillis());
            Cookie cookie = new Cookie("jwt-auth", compactForm);
            cookie.setMaxAge((int) duration.toSeconds());
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        } catch (JOSEException e) {
            log.error("Error while creating jwt after successful authentication");
        }
        delegate.onAuthenticationSuccess(request, response, authentication);
    }
}
