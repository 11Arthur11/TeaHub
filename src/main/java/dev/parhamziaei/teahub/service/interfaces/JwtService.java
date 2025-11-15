package dev.parhamziaei.teahub.service.interfaces;

import dev.parhamziaei.teahub.entity.jpa.RefreshToken;
import dev.parhamziaei.teahub.entity.jpa.User;
import dev.parhamziaei.teahub.enums.JwtType;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

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
    String generateRefreshToken(User user);
    String generateTwoFactorLoginToken(String phoneNumber, String sessionId, boolean rememberMe);
    String generateForgotPasswordToken(String phoneNumber, String sessionId);
    void deActivateRefreshToken(String token);
    Optional<String> extractPhoneNumber(String token);

}
