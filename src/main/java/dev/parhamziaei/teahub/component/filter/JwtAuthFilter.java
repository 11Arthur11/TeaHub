package dev.parhamziaei.teahub.component.filter;

import dev.parhamziaei.teahub.component.AuthenticationFactory;
import dev.parhamziaei.teahub.component.CookieFactory;
import dev.parhamziaei.teahub.component.CurrentUser;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.user.JwtType;
import dev.parhamziaei.teahub.exception.custom.authentication.BrokenJwtException;
import dev.parhamziaei.teahub.service.interfaces.JwtService;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final CookieFactory cookieFactory;
    private final ObjectFactory<CurrentUser> currentUserObjectFactory;
    private final AuthenticationFactory authFactory;

    public final static List<String> SKIP_URLs = Arrays.asList(
            "/v1/payments/gateway/callback/**",
            "/v1/auth/**",
            "/docs/**",
            "/swagger-ui/**",
            "/v1/global/**"
    );

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // note: if user want access to one of the urls in (SKIP_URLs) we skip this filter
        final String requestURI = request.getRequestURI();
        if (requestMatcher(requestURI)) {
            log.debug("Request URI: {} bypassed JwtAuthFilter", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        final String accessToken;
        final Optional<String> refreshToken = jwtService.extractJwtFromRequest(request, JwtType.REFRESH_TOKEN);
        final String phoneNumber;

        // note: collecting user tokens
        try {
            accessToken = jwtService.extractJwtFromRequest(request, JwtType.ACCESS_TOKEN)
                    .orElseThrow(() -> new BrokenJwtException("Could not extract access token from request " + requestURI));
            phoneNumber = jwtService.getPhoneNumber(accessToken);
        } catch (BrokenJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            log.info("Broken or empty jwt detected: {}", e.getMessage());
            return;
        }

        if (currentAuth == null || currentAuth instanceof AnonymousAuthenticationToken) {
            User user = userService.loadUserByPhoneNumber(phoneNumber);
            if (jwtService.isTokenValid(accessToken, JwtType.ACCESS_TOKEN)) {
                authFactory.buildAuthentication(user, request);
                buildCurrentUserContext(user, request);
                log.debug("JWT Token is valid, access granted for phone number: {} to URI: {}", phoneNumber, requestURI);
            } else if (refreshToken.isPresent() && jwtService.isTokenValid(refreshToken.get(), JwtType.REFRESH_TOKEN)) {
                // note: extracting remaining time to both access and refresh token to expire.
                Date remainingExpiration = jwtService.extractExpiration(refreshToken.get());
                Duration remainingDuration = Duration.ofSeconds(remainingExpiration.getTime() - System.currentTimeMillis() / 1000);

                String refreshedAccessToken = jwtService.generateAccessToken(user, remainingExpiration);
                String rotatedRefreshToken = jwtService.rotateRefreshToken(user, refreshToken.get());
                Cookie refreshTokenCookie = cookieFactory.buildRefreshTokenCookie(rotatedRefreshToken, remainingDuration);
                Cookie accessTokenCookie = cookieFactory.buildAccessTokenCookie(refreshedAccessToken, remainingDuration);

                response.addCookie(accessTokenCookie);
                response.addCookie(refreshTokenCookie);
                authFactory.buildAuthentication(user, request);
                buildCurrentUserContext(user, request);

                log.debug("User {} refreshed access token success", user.getUsername());
            }
        }

        currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth == null || currentAuth instanceof AnonymousAuthenticationToken) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            log.debug("JwtFilter unauthorized for request: {}", requestURI);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void buildCurrentUserContext(User user, HttpServletRequest request) {
        CurrentUser currentUser = currentUserObjectFactory.getObject();
        currentUser.setIp(request.getRemoteAddr());
        currentUser.setId(user.getId());
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setPhone(user.getPhone());
        currentUser.setWalletId(user.getWallet().getId());
    }

    private boolean requestMatcher(String requestUri) {
        for(String filteredUrl : SKIP_URLs) {
            if (filteredUrl.endsWith("/**")) {
                String baseFilteredPath = filteredUrl.substring(0, filteredUrl.length() - 3);
                if (requestUri.startsWith(baseFilteredPath)) {
                    return true;
                }
            }
            if (requestUri.equalsIgnoreCase(filteredUrl)) {return true;}
        }
        return false;
    }

//    public void buildAuthentication(UserDetails userDetails, HttpServletRequest request) {
//        UsernamePasswordAuthenticationToken authToken =
//                new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        userDetails.getAuthorities()
//                );
//        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//    }

}
