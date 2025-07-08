package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtCookieEnricherSuccessHandlerDecorator implements AuthenticationSuccessHandler {
    private final AuthenticationSuccessHandler delegate;
    private final JwtService jwtService;
    private final JwtCookieFactory jwtCookieFactory;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            String compactFormAccess = jwtService.createToken(authentication.getName());
            String compactFormRefresh = jwtService.createRefreshToken(authentication.getName());
            response.addCookie(jwtCookieFactory.createForAccessToken(compactFormAccess));
            response.addCookie(jwtCookieFactory.createForRefreshToken(compactFormRefresh));
        } catch (JOSEException e) {
            log.error("Error while creating jwt after successful authentication");
        }
        delegate.onAuthenticationSuccess(request, response, authentication);
    }
}
