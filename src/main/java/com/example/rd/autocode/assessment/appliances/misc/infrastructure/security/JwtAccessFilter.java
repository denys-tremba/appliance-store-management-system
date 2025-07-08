package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.proc.ExpiredJWTException;
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
import org.springframework.security.web.savedrequest.RequestCache;
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
public class JwtAccessFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RequestCache requestCache;
    private final JwtCookieFactory jwtCookieFactory;

    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.debug("Cookie is empty");
            filterChain.doFilter(request, response);
            return;
        }
        Optional<Cookie> jwtHolder = Arrays.stream(cookies).filter(c -> c.getName().equals(JwtCookieFactory.JWT_ACCESS)).findAny();
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
            String username = jwtService.parseUsername(compactForm);
            Authentication auth;
            UserDetails user = userDetailsService.loadUserByUsername(username);
            auth = UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            filterChain.doFilter(request, response);
            return;
        } catch (ParseException | JOSEException e) {
            log.error("Unexpected access token processing failure");
            filterChain.doFilter(request, response);
            return;
        } catch (ExpiredJWTException e) {
            commenceToRefreshEndpoint(request, response, filterChain);
            return;
        }
    }

    private void commenceToRefreshEndpoint(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.debug("Access token is expired. Saving request to cache to replay it later");
        requestCache.saveRequest(request, response);
        log.debug("Deleting access token cookie");
        Cookie cookie = jwtCookieFactory.createDeletedForAccessToken();
        response.addCookie(cookie);
        response.sendRedirect(JwtRefreshFilter.JWT_REFRESH_PATH);
    }

}
