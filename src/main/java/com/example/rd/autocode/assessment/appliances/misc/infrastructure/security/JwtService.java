package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import com.example.rd.autocode.assessment.appliances.order.complete.CompleteOrderSessionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.shared-secret:dummy}")
    String sharedSecret;
    @Value("${jwt.access.exp}")
    private Duration accessDuration;
    @Value("${jwt.refresh.exp}")
    private Duration refreshDuration;
    @Value("${jwt.order.exp}")
    private Duration orderDuration;
    private final ObjectMapper objectMapper;


    public String parseUsername(String compactForm) throws ParseException, JOSEException, BadJWTException {
        SignedJWT jwt = SignedJWT.parse(compactForm);

        JWSVerifier verifier = new MACVerifier(sharedSecret);

        boolean verification = jwt.verify(verifier);
        if (!verification) {
            throw new JOSEException();
        }
        JWTClaimsSet claims = jwt.getJWTClaimsSet();
        DefaultJWTClaimsVerifier<SecurityContext> claimsVerifier = new DefaultJWTClaimsVerifier<>();
        claimsVerifier.verify(claims, null);

        String username = claims.getSubject();
        return username;
    }

    public String createAccessToken(String username) throws JOSEException {
        JWSSigner signer = new MACSigner(sharedSecret);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(username)
                .expirationTime(new Date(new Date().getTime() + accessDuration.toMillis()))
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        signedJWT.sign(signer);
        String compactForm = signedJWT.serialize();
        return compactForm;
    }

    public String createOrderToken(CompleteOrderSessionHandler sessionHandler) throws JOSEException, JsonProcessingException {
        JWSSigner signer = new MACSigner(sharedSecret);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .expirationTime(new Date(new Date().getTime() + orderDuration.toMillis()))
                .claim("sessionHandler", objectMapper.writeValueAsString(sessionHandler))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        signedJWT.sign(signer);



        String jwt = signedJWT.serialize();

        return jwt;
    }

    public CompleteOrderSessionHandler parseSessionHandler(String compactForm) throws ParseException, JsonProcessingException {
        SignedJWT jwt = SignedJWT.parse(compactForm);
        CompleteOrderSessionHandler sessionHandler = objectMapper.readValue(jwt.getJWTClaimsSet().getClaimAsString("sessionHandler"), CompleteOrderSessionHandler.class);
        return sessionHandler;
    }

    public String createRefreshToken(String username) throws JOSEException {
        JWSSigner signer = new MACSigner(sharedSecret);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(username)
                .expirationTime(new Date(new Date().getTime() + refreshDuration.toMillis()))
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        signedJWT.sign(signer);
        String compactForm = signedJWT.serialize();
        return compactForm;
    }
}
