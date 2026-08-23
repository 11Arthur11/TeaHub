package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.configuration.properties.JwtProperties;
import dev.parhamziaei.teahub.entity.jpa.user.RefreshToken;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.user.JwtType;
import dev.parhamziaei.teahub.exception.custom.authentication.JwtValidationException;
import dev.parhamziaei.teahub.repository.jpa.RefreshTokenRepository;
import dev.parhamziaei.teahub.service.implement.JwtServiceImpl;
import dev.parhamziaei.teahub.support.TestFixtures;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private JwtServiceImpl jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        String secret = Base64.getEncoder().encodeToString(new byte[32]);
        JwtProperties properties = new JwtProperties(
                Duration.ofMinutes(3),
                Duration.ofMinutes(5),
                Duration.ofDays(1),
                Duration.ofMinutes(30),
                secret
        );
        jwtService = new JwtServiceImpl(refreshTokenRepository, properties);
        user = TestFixtures.user(1L, 10L, java.math.BigDecimal.ZERO);
    }

    @Test
    void validatesAccessTokenAndExtractsClaims() {
        String token = jwtService.generateAccessToken(
                user,
                Date.from(Instant.now().plusSeconds(30))
        );

        Claims claims = jwtService.extractAllClaims(token);

        assertTrue(jwtService.isTokenValid(token, JwtType.ACCESS_TOKEN));
        assertEquals(user.getPhone(), claims.getSubject());
        assertEquals(user.getFullName(), claims.get("full_name"));
        assertEquals(JwtType.ACCESS_TOKEN.value(), claims.get("purpose"));
    }

    @Test
    void rejectsExpiredAccessToken() {
        String token = jwtService.generateAccessToken(
                user,
                Date.from(Instant.now().minusSeconds(1))
        );

        assertFalse(jwtService.isTokenValid(token, JwtType.ACCESS_TOKEN));
    }

    @Test
    void refreshTokenMustBeActiveAndOwnedByItsSubject() {
        String token = jwtService.generateRefreshToken(user);
        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());
        RefreshToken stored = captor.getValue();
        when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(stored));

        assertTrue(jwtService.isTokenValid(token, JwtType.REFRESH_TOKEN));

        stored.setActive(false);
        assertFalse(jwtService.isTokenValid(token, JwtType.REFRESH_TOKEN));
    }

    @Test
    void rotatingRefreshTokenDeletesOldTokenAndKeepsOriginalExpiry() {
        String original = jwtService.generateRefreshToken(user);
        Date originalExpiry = jwtService.extractExpiration(original);
        RefreshToken persisted = new RefreshToken(original, user.getPhone());
        when(refreshTokenRepository.findByToken(original)).thenReturn(Optional.of(persisted));

        String rotated = jwtService.rotateRefreshToken(user, original);

        verify(refreshTokenRepository).delete(persisted);
        assertEquals(originalExpiry, jwtService.extractExpiration(rotated));
        assertNotEquals(original, rotated);
    }

    @Test
    void extractsConfiguredCookieAndIgnoresMissingCookies() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie(JwtType.ACCESS_TOKEN.value(), "token"));

        assertEquals(Optional.of("token"), jwtService.extractJwtFromRequest(request, JwtType.ACCESS_TOKEN));
        assertEquals(Optional.empty(), jwtService.extractJwtFromRequest(request, JwtType.REFRESH_TOKEN));
        assertEquals(Optional.empty(), jwtService.extractJwtFromRequest(new MockHttpServletRequest(), JwtType.ACCESS_TOKEN));
    }

    @Test
    void rejectsMalformedTokenStructure() {
        assertThrows(JwtValidationException.class, () -> jwtService.extractAllClaims("not-a-jwt"));
    }
}
