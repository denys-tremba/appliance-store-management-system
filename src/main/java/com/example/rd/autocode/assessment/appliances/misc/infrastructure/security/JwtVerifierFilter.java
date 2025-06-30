package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.proc.BadJWTException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Optional;
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtVerifierFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.debug("Cookie is empty");
            filterChain.doFilter(request, response);
            return;
        }
        Optional<Cookie> jwtHolder =Arrays.stream(cookies).filter(c -> c.getName().equals("jwt-auth")).findAny();
        if (jwtHolder.isEmpty()) {
            log.debug("Token is absent");
            filterChain.doFilter(request, response);
            return;
        }
        String compactForm = jwtHolder.get().getValue();

        if (!StringUtils.hasLength(compactForm)) {
            log.debug("Token value is empty");
            filterChain.doFilter(request, response);
            return;
        }


        try {
            String username = jwtService.username(compactForm);
            UserDetails user = userDetailsService.loadUserByUsername(username);
            Authentication auth = UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            filterChain.doFilter(request, response);
            return;
        } catch (ParseException | JOSEException e) {
            log.debug("Token failed verification");
            filterChain.doFilter(request, response);
            return;
        } catch (BadJWTException e) {
            log.debug("Token is expired");
            filterChain.doFilter(request, response);
            return;
        }
    }

}
