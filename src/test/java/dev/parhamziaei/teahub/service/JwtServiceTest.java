package dev.parhamziaei.teahub.service;

import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.user.JwtType;
import dev.parhamziaei.teahub.service.interfaces.JwtService;
import dev.parhamziaei.teahub.test_util.UserTestUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserTestUtil userTestUtil;

    @Test
    public void testTokenIsValid_ValidAccessToken() {
        String validToken = jwtService.generateAccessToken(
                UserTestUtil.dummyUser(),
                Date.from(Instant.now().plus(Duration.ofSeconds(5)))
        );

        boolean isTokenValid = jwtService.isTokenValid(validToken, JwtType.ACCESS_TOKEN);
        Assertions.assertTrue(isTokenValid);
    }

    @Test
    public void testTokenIsValid_InvalidAccessToken() {
        String invalidToken = jwtService.generateAccessToken(
                UserTestUtil.dummyUser(),
                Date.from(Instant.now().minus(Duration.ofSeconds(5)))
        );

        boolean isTokenValid = jwtService.isTokenValid(invalidToken, JwtType.ACCESS_TOKEN);
        Assertions.assertFalse(isTokenValid);
    }

    @Test
    public void testJwtExtractClaim() {
        User user = UserTestUtil.dummyUser();
        String token = jwtService.generateAccessToken(
                user,
                Date.from(Instant.now().minus(Duration.ofSeconds(5)))
        );

        Claims claims = jwtService.extractAllClaims(token);
        Assertions.assertEquals(user.getPhone(), claims.getSubject());
        Assertions.assertEquals(user.getFullName(), claims.get("full_name"));
    }

    @Test
    public void testGenerateRefreshToken() {
        User user = UserTestUtil.dummyUser();
        String token = jwtService.generateRefreshToken(
                user
        );

        Claims claims = jwtService.extractAllClaims(token);
        Assertions.assertTrue(jwtService.isTokenValid(token, JwtType.REFRESH_TOKEN));
        Assertions.assertEquals(JwtType.REFRESH_TOKEN.value(), claims.get("purpose"));
        Assertions.assertTrue(jwtService.isTokenValid(token, JwtType.REFRESH_TOKEN));
        jwtService.deActivateRefreshToken(token);
    }

    @Test
    public void testRotateRefreshToken() {
        User user = userTestUtil.persistedDummyUser();
        String token = jwtService.generateRefreshToken(user);
        String rotated = jwtService.rotateRefreshToken(user, token);
        Assertions.assertTrue(jwtService.isTokenValid(rotated, JwtType.REFRESH_TOKEN));
        jwtService.deActivateRefreshToken(token);
    }

}
