package dev.parhamziaei.teahub.component;

import dev.parhamziaei.teahub.configuration.properties.CookieFactoryProperties;
import dev.parhamziaei.teahub.configuration.properties.JwtProperties;
import dev.parhamziaei.teahub.enums.JwtType;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CookieFactory {

    private final JwtProperties jwtProperties;
    private final CookieFactoryProperties cookieProperties;


    public Cookie twoFactorCookie(String token) {
        Cookie cookie = new Cookie(JwtType.TWO_FACTOR_TOKEN.value(), token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) cookieProperties.twoFactorCookieTtl().toSeconds());
        cookie.setSecure(cookieProperties.secureCookie());
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }

    public Cookie phoneVerifyCookie(String token) {
        Cookie cookie = new Cookie(JwtType.PHONE_VERIFY_TOKEN.value(), token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) cookieProperties.phoneVerifyCookieTtl().toSeconds());
        cookie.setSecure(cookieProperties.secureCookie());
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }

    public Cookie emptyCookie(JwtType type) {
        Cookie cookie = new Cookie(type.value(), null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setSecure(cookieProperties.secureCookie());
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }

    public Cookie buildAccessTokenCookie(String token, Duration ttl) {
        Cookie jwtCookie = new Cookie(JwtType.ACCESS_TOKEN.value(), token);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge((int) ttl.toSeconds());
        jwtCookie.setSecure(cookieProperties.secureCookie());
        jwtCookie.setAttribute("SameSite", "Strict");
        return jwtCookie;
    }

    public Cookie buildRefreshTokenCookie(String token) {
        Cookie refreshTokenCookie = new Cookie(JwtType.REFRESH_TOKEN.value(), token);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge((int) jwtProperties.refreshTokenTtl().toSeconds());
        refreshTokenCookie.setSecure(cookieProperties.secureCookie());
        refreshTokenCookie.setAttribute("SameSite", "Strict");
        return refreshTokenCookie;
    }

    public long getRemainingSeconds(long exp) {
        long currentEpochSeconds = Instant.now().getEpochSecond();
        long remaining = exp - currentEpochSeconds;
        return Math.max(remaining, 0);
    }

}
