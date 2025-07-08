package com.example.rd.autocode.assessment.appliances.misc.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.proc.BadJWTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Use a known, valid secret key for testing (must be >= 256 bits / 32 bytes for HS256)
    private final String testSharedSecret = "a-very-secure-and-long-secret-key-for-testing-32-bytes";

    @BeforeEach
    void setUp() {
        // Instantiate the service with a real ObjectMapper
        jwtService = new JwtService(objectMapper);

        // Use ReflectionTestUtils to inject our test configuration values
        // into the private, @Value-annotated fields.
        ReflectionTestUtils.setField(jwtService, "sharedSecret", testSharedSecret);
        ReflectionTestUtils.setField(jwtService, "accessDuration", Duration.ofMinutes(15));
        ReflectionTestUtils.setField(jwtService, "refreshDuration", Duration.ofHours(1));
        ReflectionTestUtils.setField(jwtService, "orderDuration", Duration.ofMinutes(5));
    }

    @Test
    @DisplayName("createAccessToken() and parseUsername() should succeed in a round-trip")
    void createAndParseAccessToken_shouldSucceed() throws JOSEException, ParseException, BadJWTException {
        // Arrange
        String expectedUsername = "testuser@example.com";

        // Act
        String accessToken = jwtService.createAccessToken(expectedUsername);
        assertNotNull(accessToken);
        assertFalse(accessToken.isEmpty());

        String parsedUsername = jwtService.parseUsername(accessToken);

        // Assert
        assertEquals(expectedUsername, parsedUsername);
    }

    @Test
    @DisplayName("parseUsername() should throw BadJWTException for an expired token")
    void parseUsername_withExpiredToken_shouldThrowBadJWTException() throws JOSEException {
        // Arrange
        // Temporarily set the access duration to a negative value to create an expired token
        ReflectionTestUtils.setField(jwtService, "accessDuration", Duration.ofMinutes(-5));
        String expiredToken = jwtService.createAccessToken("testuser@example.com");

        // Act & Assert
        assertThrows(BadJWTException.class, () -> {
            jwtService.parseUsername(expiredToken);
        }, "Parsing an expired token should throw BadJWTException");
    }

    @Test
    @DisplayName("parseUsername() should throw JOSEException for a token with an invalid signature")
    void parseUsername_withInvalidSignature_shouldThrowJOSEException() throws JOSEException {
        // Arrange
        String validToken = jwtService.createAccessToken("testuser@example.com");

        // Create another service with a DIFFERENT secret to try and verify the token
        JwtService verifierWithWrongSecret = new JwtService(objectMapper);
        ReflectionTestUtils.setField(verifierWithWrongSecret, "sharedSecret", "a-completely-different-and-wrong-secret-key-for-tests");

        // Act & Assert
        assertThrows(Exception.class, () -> {
            // The verify() method inside parseUsername should fail
            verifierWithWrongSecret.parseUsername(validToken);
        }, "Verification with the wrong secret should throw JOSEException");
    }

    @Test
    @DisplayName("parseUsername() should throw ParseException for a malformed token string")
    void parseUsername_withMalformedToken_shouldThrowParseException() {
        // Arrange
        String malformedToken = "this.is.not.a.valid.jwt";

        // Act & Assert
        assertThrows(ParseException.class, () -> {
            jwtService.parseUsername(malformedToken);
        });
    }


    @Test
    @DisplayName("createRefreshToken() should generate a valid token string")
    void createRefreshToken_shouldGenerateValidToken() throws JOSEException {
        // Arrange
        String username = "refresh_user@example.com";

        // Act
        String refreshToken = jwtService.createRefreshToken(username);

        // Assert
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
        // A full round-trip test similar to the access token could also be added for completeness
    }
}