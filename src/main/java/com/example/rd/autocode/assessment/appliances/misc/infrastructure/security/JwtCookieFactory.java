package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtCookieFactory {
    public static final String JWT_ACCESS = "jwt-auth-access";
    public static final String JWT_ORDER = "jwt-order";
    public static final String JWT_REFRESH = "jwt-auth-refresh";
    public static final String JWT_REFRESH_PATH = "/jwt/refresh";
    @Value("${jwt.access.exp}")
    private Duration accessDuration;
    @Value("${jwt.order.exp}")
    private Duration orderDuration;
    @Value("${jwt.refresh.exp}")
    private Duration refreshDuration;
    public Cookie createForAccessToken(String compactForm) {
        Cookie cookie = new Cookie(JWT_ACCESS, compactForm);
        cookie.setPath("/");
//        cookie.setMaxAge((int) accessDuration.toSeconds());
        cookie.setMaxAge(-1);
//        cookie.setHttpOnly(true);
        return cookie;
    }
    public Cookie createForOrder(String compactForm) {
        Cookie cookie = new Cookie(JWT_ORDER, compactForm);
        cookie.setPath("/orders/");
        cookie.setMaxAge((int) orderDuration.toSeconds());
        return cookie;
    }

    public Optional<Cookie> findForOrder(List<Cookie> cookies) {
        return cookies.stream().filter(c -> c.getName().equals(JWT_ORDER)).findAny();
    }

    public Cookie createForRefreshToken(String compactForm) {
        Cookie cookie = new Cookie(JWT_REFRESH, compactForm);
        cookie.setPath(JWT_REFRESH_PATH);
        cookie.setMaxAge((int) refreshDuration.toSeconds());
//        cookie.setHttpOnly(true);
        return cookie;
    }

    public Cookie createDeletedForAccessToken() {
        Cookie cookie = new Cookie(JwtCookieFactory.JWT_ACCESS, "");
        cookie.setMaxAge(0);
        return cookie;
    }
}
