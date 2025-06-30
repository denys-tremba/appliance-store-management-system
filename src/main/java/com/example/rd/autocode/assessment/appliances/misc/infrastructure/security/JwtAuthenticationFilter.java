package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    protected JwtAuthenticationFilter() {
        super(PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST,"/login"));
        setAuthenticationConverter(new JWTAuthenticationConverter());
    }
}
