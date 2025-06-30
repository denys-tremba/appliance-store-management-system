package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

@Component
@Slf4j
public class FailedLoginAttemptsCache {
    @Value("${login.attempts.penalty}")
    private Duration ttl;

    private Map<String, Integer> cache;

    @PostConstruct
    public void postConstruct() {
        cache = Collections.synchronizedMap(new PassiveExpiringMap<>(ttl.toMillis()));
    }

    public long getTtlS() {
        return ttl.toSeconds();
    }

    public Integer get(Authentication authentication) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        return cache.getOrDefault(details.getRemoteAddress(), 0);
    }

    public void increment(Authentication authentication) {
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String remoteAddr = details.getRemoteAddress();
        Integer attempts = cache.get(remoteAddr);
        if (attempts == null) {
            attempts = 1;
        } else {
            attempts++;
        }
        cache.put(remoteAddr, attempts);
        log.warn("👀 ip: {}, failed login attempt #{}", remoteAddr, attempts);
    }
}
