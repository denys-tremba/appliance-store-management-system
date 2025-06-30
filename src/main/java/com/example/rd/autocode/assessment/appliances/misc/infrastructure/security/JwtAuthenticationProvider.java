package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Value("${jwt.shared-secret}")
    private String sharedSecret;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String compactForm = (String) authentication.getCredentials();
            SignedJWT jwt = SignedJWT.parse(compactForm);
            JWSVerifier verifier = new MACVerifier(sharedSecret);
            boolean verification = jwt.verify(verifier);
            if (!verification) {
                return null;
            }
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            String subject = claims.getSubject();
            List<SimpleGrantedAuthority> authorities = claims.getListClaim("roles")
                    .stream()
                    .map(o->new SimpleGrantedAuthority((String) o))
                    .collect(Collectors.toList());
            return JwtAuthenticationToken.authenticated(subject, authorities);
        } catch (ParseException | JOSEException e) {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
