package dev.parhamziaei.teahub.service.implement;

import dev.parhamziaei.teahub.configuration.properties.JwtProperties;
import dev.parhamziaei.teahub.entity.jpa.RefreshToken;
import dev.parhamziaei.teahub.entity.jpa.User;
import dev.parhamziaei.teahub.enums.JwtType;
import dev.parhamziaei.teahub.repository.jpa.RefreshTokenRepository;
import dev.parhamziaei.teahub.service.interfaces.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private final String base64Secret;
    private final RefreshTokenRepository refreshTokenRepo;
    private final JwtProperties jwtProperties;

    public  JwtServiceImpl(RefreshTokenRepository refreshTokenRepo, JwtProperties jwtProperties) {
        this.refreshTokenRepo = refreshTokenRepo;
        this.jwtProperties = jwtProperties;
        this.base64Secret = this.jwtProperties.base64Secret();
    }

    @Override
    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Claims extractAllClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String extractSessionId(String twoFactorToken) {
        return extractClaim(twoFactorToken, claims -> claims.get("session_id", String.class));
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public boolean isTokenNotExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    @Override
    public boolean isTokenValid(String token, JwtType tokenType) {
        boolean notNull = token != null;
        boolean validUser = extractPhoneNumber(token).isPresent();
        boolean forTwoFactor = (extractClaim(token, claims -> claims.get("purpose", String.class).equals("2FA")));
        boolean forAccess = (extractClaim(token, claims -> claims.get("purpose", String.class).equals("ACCESS")));
        boolean forRefresh = (extractClaim(token, claims -> claims.get("purpose", String.class).equals("REFRESH")));
        switch (tokenType) {
            case ACCESS_TOKEN -> {
                return (
                        isSignatureValid(token) &&
                        isTokenNotExpired(token) &&
                        forAccess
                );
            }

            case REFRESH_TOKEN -> {
                if (isTokenNotExpired(token) && isSignatureValid(token) && forRefresh) {
                    Optional<RefreshToken> dbToken = refreshTokenRepo.findByToken(token);
                    Optional<String> phoneNumber = extractPhoneNumber(token);
                    if (dbToken.isPresent() && phoneNumber.isPresent()) {
                        RefreshToken refreshToken = dbToken.get();
                        return (
                                refreshToken.isActive() &&
                                refreshToken.getTokenOwner().equals(phoneNumber.get()) &&
                                refreshToken.getToken().equals(token)
                        );
                    }
                }
            }

            case TWO_FACTOR_TOKEN ->  {
                return (
                        isSignatureValid(token) &&
                        isTokenNotExpired(token) &&
                        forTwoFactor
                );
            }
        }
        return false;
    }

    @Override
    public boolean isSignatureValid(String token) {
        try {
            Key key = getKey();
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ignored) {
            return false;
        }
    }

    @Override
    public String generateAccessToken(User user) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtProperties.accessTokenTtl().toMillis());
        Map<String, Object> claims = new HashMap<>();
        claims.put("full_name", user.getFullName());
        claims.put("role", user.getHigherAuthority().getName());
        claims.put("purpose", "ACCESS");

        return buildToken(
                claims,
                user.getPhone(),
                expiryDate
        );
    }

    @Override
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("purpose", "REFRESH");
        Date expiryDate = new Date(System.currentTimeMillis() + jwtProperties.refreshTokenTtl().toMillis());
        String token = buildToken(
                claims,
                user.getPhone(),
                expiryDate
        );
        RefreshToken refreshToken = new RefreshToken(token, user.getPhone());
        refreshTokenRepo.save(refreshToken);
        return token;
    }

    @Override
    public String generateTwoFactorLoginToken(String phoneNumber, String sessionId, boolean rememberMe) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtProperties.twoFactorTokenTtl().toMillis());
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("purpose", "2FA");
        claims.put("remember_me", rememberMe);
        claims.put("session_id", sessionId);
        return buildToken(
                claims,
                phoneNumber,
                expiryDate
        );
    }

    @Override
    public String generateForgotPasswordToken(String phoneNumber, String sessionId) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtProperties.forgotPasswordTokenTtl().toMillis());
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("purpose", "2FA");
        claims.put("session_id", sessionId);
        return buildToken(
                claims,
                phoneNumber,
                expiryDate
        );
    }

    private String buildToken(Map<String, Object> claims, String subject, Date expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, getKey())
                .compact();
    }

    @Override
    public void deActivateRefreshToken(String token) {
        Optional<RefreshToken> dbToken = refreshTokenRepo.findByToken(token);
        dbToken.ifPresent(refreshToken -> refreshTokenRepo.delete(refreshToken));
    }

    @Override
    public Optional<String> extractPhoneNumber(String token) {
        return Optional.ofNullable(extractClaim(token, Claims::getSubject));
    }
}