package dev.parhamziaei.teahub.component;

import dev.parhamziaei.teahub.configuration.properties.ApplicationSettingProperties;
import dev.parhamziaei.teahub.configuration.properties.CookieFactoryProperties;
import dev.parhamziaei.teahub.configuration.properties.JwtProperties;
import dev.parhamziaei.teahub.enums.user.JwtType;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class CookieFactory {

    private final JwtProperties jwtProperties;
    private final CookieFactoryProperties cookieProperties;

    public Cookie twoFactorCookie(String token) {
        Cookie cookie = new Cookie(JwtType.TWO_FACTOR_TOKEN.value(), token);
        cookie.setDomain(cookieProperties.domain().isEmpty() ? null : cookieProperties.domain());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) cookieProperties.twoFactorCookieTtl().toSeconds());
        cookie.setSecure(cookieProperties.secureCookie());
        cookie.setAttribute("SameSite", cookieProperties.sameSiteAttribute());
        return cookie;
    }

    public Cookie phoneVerifyCookie(String token) {
        Cookie cookie = new Cookie(JwtType.PHONE_VERIFY_TOKEN.value(), token);
        cookie.setDomain(cookieProperties.domain().isEmpty() ? null : cookieProperties.domain());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) cookieProperties.phoneVerifyCookieTtl().toSeconds());
        cookie.setSecure(cookieProperties.secureCookie());
        cookie.setAttribute("SameSite", cookieProperties.sameSiteAttribute());
        return cookie;
    }

    public Cookie emptyCookie(JwtType type) {
        Cookie cookie = new Cookie(type.value(), null);
        cookie.setDomain(cookieProperties.domain().isEmpty() ? null : cookieProperties.domain());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setSecure(cookieProperties.secureCookie());
        cookie.setAttribute("SameSite", cookieProperties.sameSiteAttribute());
        return cookie;
    }

    public Cookie buildAccessTokenCookie(String token) {
        return buildAccessTokenCookie(token, jwtProperties.accessTokenTtl());
    }

    public Cookie buildAccessTokenCookie(String token, Duration ttl) {
        Cookie jwtCookie = new Cookie(JwtType.ACCESS_TOKEN.value(), token);
        jwtCookie.setDomain(cookieProperties.domain().isEmpty() ? null : cookieProperties.domain());
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge((int) ttl.toSeconds());
        jwtCookie.setSecure(cookieProperties.secureCookie());
        jwtCookie.setAttribute("SameSite", cookieProperties.sameSiteAttribute());
        return jwtCookie;
    }

    public Cookie buildRefreshTokenCookie(String token) {
        return buildRefreshTokenCookie(token, jwtProperties.refreshTokenTtl());
    }

    public Cookie buildRefreshTokenCookie(String token, Duration ttl) {
        Cookie refreshTokenCookie = new Cookie(JwtType.REFRESH_TOKEN.value(), token);
        refreshTokenCookie.setDomain(cookieProperties.domain().isEmpty() ? null : cookieProperties.domain());
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge((int) ttl.toSeconds());
        refreshTokenCookie.setSecure(cookieProperties.secureCookie());
        refreshTokenCookie.setAttribute("SameSite", cookieProperties.sameSiteAttribute());
        return refreshTokenCookie;
    }

    public long getRemainingSeconds(long exp) {
        long currentEpochSeconds = Instant.now().getEpochSecond();
        long remaining = exp - currentEpochSeconds;
        return Math.max(remaining, 0);
    }

}
