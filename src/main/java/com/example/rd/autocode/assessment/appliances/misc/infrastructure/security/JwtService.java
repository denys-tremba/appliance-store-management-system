package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import com.example.rd.autocode.assessment.appliances.order.Order;
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
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.shared-secret}")
    String sharedSecret;
    private final ObjectMapper objectMapper;

    public Order order(String compactForm) throws ParseException, JsonProcessingException {
        SignedJWT jwt = SignedJWT.parse(compactForm);
        Order order = objectMapper.readValue(jwt.getJWTClaimsSet().getClaimAsString("order"), Order.class);
        return order;

    }

    public String create(Order order) throws JsonProcessingException, JOSEException {
        JWSSigner signer = new MACSigner(sharedSecret);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .expirationTime(new Date(new Date().getTime() + 60 * 1000))
                .claim("order", objectMapper.writeValueAsString(order))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        signedJWT.sign(signer);



        String jwt = signedJWT.serialize();

        return jwt;
    }


    public String username(String compactForm) throws ParseException, JOSEException, BadJWTException {
        SignedJWT jwt = SignedJWT.parse(compactForm);

        JWSVerifier verifier = new MACVerifier(sharedSecret);

        boolean verification = jwt.verify(verifier);
        JWTClaimsSet claims = jwt.getJWTClaimsSet();
        DefaultJWTClaimsVerifier<SecurityContext> claimsVerifier = new DefaultJWTClaimsVerifier<>();
        claimsVerifier.verify(claims, null);

        String username = claims.getSubject();
        return username;
    }

    public String create(String username, long expiry) throws JOSEException {
        JWSSigner signer = new MACSigner(sharedSecret);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(username)
                .expirationTime(new Date(new Date().getTime() + expiry))
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        signedJWT.sign(signer);
        String compactForm = signedJWT.serialize();
        return compactForm;
    }
}
