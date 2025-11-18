package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.entity.jpa.User;
import dev.parhamziaei.teahub.enums.JwtType;
import dev.parhamziaei.teahub.exception.custom.service.JwtValidationException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

public interface JwtService {

    Key getKey();
    Claims extractAllClaims(String token);
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String extractSessionId(String twoFactorToken);
    Date extractExpiration(String token);
    boolean isTokenNotExpired(String token);
    boolean isTokenValid(String token, JwtType tokenType);
    boolean isSignatureValid(String token);
    String generateAccessToken(User user);
    String generateAccessToken(User user, Date expiration);
    String generateRefreshToken(User user);
    String rotateRefreshToken(User user, String oldRefreshToken);
    String generateTwoFactorLoginToken(String phoneNumber, String sessionId);
    String generatePhoneVerifyToken(String phoneNumber, String sessionId);
    void deActivateRefreshToken(String token);
    Optional<String> extractPhoneNumber(String token);
    String getPhoneNumber(String token);
    Optional<String> extractJwtFromRequest(HttpServletRequest request, JwtType type) throws JwtValidationException;

}
