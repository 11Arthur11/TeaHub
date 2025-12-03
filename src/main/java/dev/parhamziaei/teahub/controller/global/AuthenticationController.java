package dev.parhamziaei.teahub.controller.global;

import dev.parhamziaei.teahub.component.AuthenticationFactory;
import dev.parhamziaei.teahub.component.CookieFactory;
import dev.parhamziaei.teahub.configuration.properties.JwtProperties;
import dev.parhamziaei.teahub.dto.request.authentication.AuthEntryRequest;
import dev.parhamziaei.teahub.dto.request.authentication.LoginRequest;
import dev.parhamziaei.teahub.dto.request.authentication.RegisterRequest;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.JwtType;
import dev.parhamziaei.teahub.enums.messages.AuthMessage;
import dev.parhamziaei.teahub.enums.ResponseType;
import dev.parhamziaei.teahub.exception.custom.authentication.BrokenJwtException;
import dev.parhamziaei.teahub.exception.custom.authentication.InvalidTwoFactorException;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.TwoFactorService;
import dev.parhamziaei.teahub.service.interfaces.JwtService;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthenticationController {

    private final UserService userService;
    private final TwoFactorService twoFactorService;
    private final JwtService jwtService;
    private final CookieFactory cookieFactory;
    private final JwtProperties jwtProperties;
    private final MessageService messageService;
    private final AuthenticationFactory authFactory;

    @PostMapping("/initiate")
    public ResponseEntity<SimpleResponse> authEntry(
            @Valid @RequestBody AuthEntryRequest entryRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String phoneNumber = entryRequest.getPhoneNumber();
        final Optional<String> oldTwoFactorToken = jwtService.extractJwtFromRequest(request, JwtType.TWO_FACTOR_TOKEN);
        final Optional<String> oldPhoneVerifyToken = jwtService.extractJwtFromRequest(request, JwtType.PHONE_VERIFY_TOKEN);

        // note: this section make sure no one with active session can spam this method
        if (oldTwoFactorToken.isPresent() && twoFactorService.hasActiveTwoFactorSession(oldTwoFactorToken.get())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        if (oldPhoneVerifyToken.isPresent() && twoFactorService.hasActivePhoneVerifySession(oldPhoneVerifyToken.get())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        // note: this if-else decides which user must register or login.
        if (userService.isUserRegistered(phoneNumber)) {
            final String sessionId = twoFactorService.sendTwoFactor(phoneNumber);
            final String twoFactorToken = jwtService.generateTwoFactorLoginToken(phoneNumber, sessionId);
            Cookie sessionCookie = cookieFactory.twoFactorCookie(twoFactorToken);
            response.addCookie(sessionCookie);
            return ResponseBuilder.buildSuccess(
                    ResponseType.LOGIN_INITIATED,
                    messageService.get(AuthMessage.TWO_FACTOR_SENT),
                    HttpStatus.OK
            );
        } else {
            final String sessionId = twoFactorService.sendPhoneVerify(phoneNumber);
            final String phoneVerifyToken = jwtService.generatePhoneVerifyToken(phoneNumber, sessionId);
            Cookie sessionCookie = cookieFactory.phoneVerifyCookie(phoneVerifyToken);
            response.addCookie(sessionCookie);
            return ResponseBuilder.buildSuccess(
                    ResponseType.REGISTER_INITIATED,
                    messageService.get(AuthMessage.TWO_FACTOR_SENT),
                    HttpStatus.OK
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<SimpleResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String code = loginRequest.getTwoFactorCode();
        final Optional<String> optionalSessionToken = jwtService.extractJwtFromRequest(request, JwtType.TWO_FACTOR_TOKEN);

        // note: if user try to log in with no active session this will handle it
        if (optionalSessionToken.isEmpty()) {
            throw new InvalidTwoFactorException("invalid login two factor token");
        }
        final String loginTwoFactorToken = optionalSessionToken.get();
        final String phoneNumber = jwtService.getPhoneNumber(loginTwoFactorToken);

        // note: calling twoFactorService for validate and verify the received code with one that we stored in redis
        twoFactorService.verifyTwoFactorSession(loginTwoFactorToken, code);
        User user = userService.loadUserByPhoneNumber(phoneNumber);

        // note: this is for SpringSecurity to fill security context and also checks if user is disabled, locked etc... , also we're updating last login here
        authFactory.buildAuthentication(user, request);

        final String accessToken= jwtService.generateAccessToken(user);
        final Cookie accessTokenCookie;

        // note: this if-else decide what will happen if user uses rememberMe feature
        if (loginRequest.isRememberMe()) {
            final String refreshToken = jwtService.generateRefreshToken(user);
            final Cookie refreshTokenCookie = cookieFactory.buildRefreshTokenCookie(refreshToken);
            response.addCookie(refreshTokenCookie);
            // note: building access token cookie with refresh token expiration to refresh more securely, this only applies to cookie the jwt expiration still same as normal
            accessTokenCookie = cookieFactory.buildAccessTokenCookie(accessToken, jwtProperties.refreshTokenTtl());
        } else {
            // note: building access token cookie with normal expiration
            accessTokenCookie = cookieFactory.buildAccessTokenCookie(accessToken);
        }

        // note: removing two factor cookie from client
        response.addCookie(cookieFactory.emptyCookie(JwtType.TWO_FACTOR_TOKEN));
        response.addCookie(accessTokenCookie);
        return ResponseBuilder.buildSuccess(
                ResponseType.LOGIN_SUCCESS,
                messageService.get(AuthMessage.LOGIN_SUCCESS),
                HttpStatus.OK
        );
    }

    @PostMapping("/register")
    public ResponseEntity<SimpleResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String code = registerRequest.getTwoFactorCode();
        final Optional<String> optionalSessionToken = jwtService.extractJwtFromRequest(request, JwtType.PHONE_VERIFY_TOKEN);

        if (optionalSessionToken.isEmpty()) {
            throw new InvalidTwoFactorException("invalid phone verification token");
        }

        final String phoneVerifyToken = optionalSessionToken.get();
        final String phoneNumber = jwtService.getPhoneNumber(phoneVerifyToken);

        twoFactorService.verifyPhoneVerifySession(phoneVerifyToken, code);
        // reminder: we have UnsupportedOperationException from Here !!!!!
        userService.register(phoneNumber, registerRequest);
        User user = userService.loadUserByPhoneNumber(phoneNumber);
        // note: logging user in if register was successful, we don't have remember-me option here.
        authFactory.buildAuthentication(user, request);

        final String accessToken = jwtService.generateAccessToken(user);
        Cookie accessTokenCookie = cookieFactory.buildAccessTokenCookie(accessToken);
        response.addCookie(accessTokenCookie);
        // note: removing two factor cookie from client
        response.addCookie(cookieFactory.emptyCookie(JwtType.PHONE_VERIFY_TOKEN));

        return ResponseBuilder.buildSuccess(
                ResponseType.REGISTER_SUCCESS,
                messageService.get(AuthMessage.REGISTER_SUCCESSFUL),
                HttpStatus.OK
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponse> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        SecurityContextHolder.clearContext();
        final Optional<String> refreshToken = jwtService.extractJwtFromRequest(request, JwtType.REFRESH_TOKEN);
        jwtService.extractJwtFromRequest(request, JwtType.ACCESS_TOKEN)
                .orElseThrow(BrokenJwtException::new);

        response.addCookie(cookieFactory.emptyCookie(JwtType.ACCESS_TOKEN));
        if (refreshToken.isPresent()) {
            response.addCookie(cookieFactory.emptyCookie(JwtType.REFRESH_TOKEN));
            jwtService.deActivateRefreshToken(refreshToken.get());
        }

        return ResponseBuilder.buildSuccess(
                ResponseType.SUCCESS,
                messageService.get(AuthMessage.LOGOUT_SUCCESS),
                HttpStatus.OK
        );
    }

}
