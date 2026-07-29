package dev.parhamziaei.teahub.controller.global;

import dev.parhamziaei.teahub.component.AuthenticationFactory;
import dev.parhamziaei.teahub.component.CookieFactory;
import dev.parhamziaei.teahub.configuration.properties.JwtProperties;
import dev.parhamziaei.teahub.dto.request.authentication.AuthEntryRequest;
import dev.parhamziaei.teahub.dto.request.authentication.LoginRequest;
import dev.parhamziaei.teahub.dto.request.authentication.RegisterRequest;
import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.entity.jpa.user.User;
import dev.parhamziaei.teahub.enums.user.JwtType;
import dev.parhamziaei.teahub.enums.messages.AuthMessage;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.exception.custom.authentication.BrokenJwtException;
import dev.parhamziaei.teahub.exception.custom.authentication.InvalidTwoFactorException;
import dev.parhamziaei.teahub.repository.redis.PhoneVerifyRepo;
import dev.parhamziaei.teahub.repository.redis.TwoFactorRepo;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.service.TwoFactorService;
import dev.parhamziaei.teahub.service.interfaces.JwtService;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
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
    private final TwoFactorRepo twoFactorRepo;
    private final PhoneVerifyRepo phoneVerifyRepo;

    @Operation(
            summary = "Authentication Check",
            tags = {"Auth"}
    )
    @RequestMapping(method = RequestMethod.HEAD, path = "/session")
    public ResponseEntity<?> checkAuthentication(HttpServletRequest request) {
        Optional<String> token = jwtService.extractJwtFromRequest(request, JwtType.ACCESS_TOKEN);
        if (token.isPresent() && jwtService.isTokenValid(token.get(), JwtType.ACCESS_TOKEN))
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "type -> LOGIN_INITIATED: user has a valid account and must log in, " +
                            "type -> REGISTER_INITIATED: user must register"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "type -> ERROR: something went wrong while initiating; message is translated to Farsi and should be shown directly to the user"
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "Too many requests: OTP session is still active"
            )
    })
    @Operation(
            summary = "Authentication Entry",
            description = "User sends their phone number to start the authentication process. " +
                    "The endpoint decides whether the user should log in or register. " +
                    "UI can then show the appropriate form based on the response.",
            tags = {"Auth"}
    )
    @PostMapping("/initiate")
    public ResponseEntity<?> authEntry(
            @Valid @RequestBody AuthEntryRequest entryRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String phoneNumber = entryRequest.getPhoneNumber();
        final Optional<String> oldTwoFactorToken = jwtService.extractJwtFromRequest(request, JwtType.TWO_FACTOR_TOKEN);
        final Optional<String> oldPhoneVerifyToken = jwtService.extractJwtFromRequest(request, JwtType.PHONE_VERIFY_TOKEN);

        // note: this section make sure no one with active session can spam this method
//!       if (oldTwoFactorToken.isPresent() && twoFactorService.hasActiveTwoFactorSession(oldTwoFactorToken.get())) {
//            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
//        }
//        if (oldPhoneVerifyToken.isPresent() && twoFactorService.hasActivePhoneVerifySession(oldPhoneVerifyToken.get())) {
//            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
//        }

        // note: this if-else decides which user must register or login.
        if (userService.isUserRegistered(phoneNumber)) {
            final String sessionId = twoFactorService.sendTwoFactor(phoneNumber);
            final String twoFactorToken = jwtService.generateTwoFactorLoginToken(phoneNumber, sessionId);
            Cookie sessionCookie = cookieFactory.twoFactorCookie(twoFactorToken);
            response.addCookie(sessionCookie);

            //REMINDER: THIS IS FOR DEV PHASE ONLY!!!!!
            String twoFactorCode = twoFactorRepo.get(sessionId).getCode();

            return ResponseBuilder.buildSuccess(
                    ResponseType.LOGIN_INITIATED,
                    messageService.get(AuthMessage.TWO_FACTOR_SENT),
                    twoFactorCode, //REMINDER: THIS IS FOR DEV PHASE ONLY!!!!!
                    HttpStatus.OK
            );
        } else {
            final String sessionId = twoFactorService.sendPhoneVerify(phoneNumber);
            final String phoneVerifyToken = jwtService.generatePhoneVerifyToken(phoneNumber, sessionId);
            Cookie sessionCookie = cookieFactory.phoneVerifyCookie(phoneVerifyToken);
            response.addCookie(sessionCookie);

            //REMINDER: THIS IS FOR DEV PHASE ONLY!!!!!
            String twoFactorCode = phoneVerifyRepo.get(sessionId).getCode();

            return ResponseBuilder.buildSuccess(
                    ResponseType.REGISTER_INITIATED,
                    messageService.get(AuthMessage.TWO_FACTOR_SENT),
                    twoFactorCode, //REMINDER: THIS IS FOR DEV PHASE ONLY!!!!!
                    HttpStatus.OK
            );
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "type -> LOGIN_SUCCESS: user entered the correct OTP and login was successful"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "type -> ERROR: something went wrong during login; the message is translated to Farsi and should be shown directly to the user"
            )
    })
    @Operation(
            summary = "Login Operation",
            description = "After initiating, the next step is receiving the OTP from the user. " +
                    "A session with a specified TTL is created on the backend, and the user receives a session-id cookie in their browser. " +
                    "If the login is successful, the JWT cookie will be applied. " +
                    "Remember-me functionality must be specified in this endpoint.",
            tags = {"Auth"}
    )
    @PostMapping("/login")
    public ResponseEntity<SimpleResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String code = loginRequest.getTwoFactorCode();
        final Optional<String> optionalSessionToken = jwtService.extractJwtFromRequest(request, JwtType.TWO_FACTOR_TOKEN);

        // note: if user try to log in with no active session this will handle it
        if (optionalSessionToken.isEmpty())
            throw new InvalidTwoFactorException("invalid login two factor token");
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

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "type -> REGISTER_SUCCESS: user submitted the correct OTP and a valid registration body; registration was successful"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "type -> ERROR: something went wrong during registration; the message is translated to Farsi and should be shown directly to the user"
            )
    })
    @Operation(
            summary = "Register Operation",
            description = "After initiating, if the user is not registered yet, the next step is to register the user by submitting the form along with the OTP that was already sent to their phone number. " +
                    "A session with a specified TTL is created on the backend, and the user receives a session-id cookie in their browser. " +
                    "If registration is successful, a JWT cookie will be applied, and the user will be automatically logged in without remember-me functionality.",
            tags = {"Auth"}
    )
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

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "type -> SUCCESS: user has successfully logged out"
            )
    })
    @Operation(
            summary = "Logout Operation",
            description = "Clears the SecurityContext on the backend and removes the JWT and refresh token cookies from the user's browser.",
            tags = {"Auth"}
    )
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
