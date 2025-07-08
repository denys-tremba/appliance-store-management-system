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
import org.springframework.http.HttpMethod;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
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
public class JwtRefreshFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final RequestCache requestCache;
    private final JwtCookieFactory jwtCookieFactory;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final RequestMatcher requestMatcher = PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, JWT_REFRESH_PATH);
    public static final String JWT_REFRESH_PATH = JwtCookieFactory.JWT_REFRESH_PATH;
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!requestMatcher.matches(request)) {
            log.debug("Skipping invocation of %s due to no match".formatted(this.getClass().getSimpleName()));
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.debug("Skipping invocation of %s due to cookie is empty".formatted(this.getClass().getSimpleName()));
            filterChain.doFilter(request, response);
            return;
        }
        Optional<Cookie> jwtHolder = Arrays.stream(cookies).filter(c -> c.getName().equals(JwtCookieFactory.JWT_REFRESH)).findAny();
        if (jwtHolder.isEmpty()) {
            log.debug("Skipping invocation of %s due to refresh token is absent".formatted(this.getClass().getSimpleName()));
            filterChain.doFilter(request, response);
            return;
        }
        String compactForm = jwtHolder.get().getValue();

        if (!StringUtils.hasLength(compactForm)) {
            log.debug("Skipping invocation of %s due to refresh token value is empty".formatted(this.getClass().getSimpleName()));
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("Processing invocation of {}", this.getClass());

        try {
            String username = jwtService.username(compactForm);
            String accessJwt = jwtService.createToken(username);
            log.debug("Created access token from refresh for {}", this.getClass());
            response.addCookie(jwtCookieFactory.createForAccessToken(accessJwt));
            log.debug("Setting refresh token into cookie for {}", username);
            SavedRequest savedRequest = requestCache.getRequest(request, response);
            String redirectUrl;
            if (savedRequest == null) {
                log.warn("Retrieved saved request from cache should not be null. Setting redirect url to context root");
                redirectUrl = "/";
            } else {
                redirectUrl = savedRequest.getRedirectUrl();
                log.debug("Removing saved request from cache: {}", savedRequest);
                requestCache.removeRequest(request, response);

            }
            log.debug("Before redirection to {}", redirectUrl);
            redirectStrategy.sendRedirect(request, response, redirectUrl);
            return;
        } catch (ParseException | JOSEException e) {
            log.error("Unexpected refresh token processing failure");
            filterChain.doFilter(request, response);
            return;
        } catch (ExpiredJWTException e) {
            log.debug("Skipping invocation of %s due to refresh token is expired".formatted(this.getClass().getSimpleName()));
            filterChain.doFilter(request, response);
            return;
        }
    }

}
