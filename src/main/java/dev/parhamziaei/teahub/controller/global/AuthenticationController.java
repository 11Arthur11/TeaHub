package dev.parhamziaei.teahub.controller.global;

import dev.parhamziaei.teahub.component.CookieFactory;
import dev.parhamziaei.teahub.dto.request.authentication.AuthEntryRequest;
import dev.parhamziaei.teahub.dto.response.SimpleResponse;
import dev.parhamziaei.teahub.enums.JwtType;
import dev.parhamziaei.teahub.enums.ResponseType;
import dev.parhamziaei.teahub.service.TwoFactorService;
import dev.parhamziaei.teahub.service.interfaces.JwtService;
import dev.parhamziaei.teahub.service.interfaces.UserService;
import dev.parhamziaei.teahub.utils.PhoneNumbers;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/init")
    public ResponseEntity<SimpleResponse> authEntry(
            @Valid @RequestBody AuthEntryRequest entryRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String phoneNumber = PhoneNumbers.formatedOf(entryRequest.getPhoneNumber());
        final Optional<String> oldTwoFactorToken = jwtService.extractJwtFromRequest(request, JwtType.TWO_FACTOR_TOKEN);
        final Optional<String> oldPhoneVerifyToken = jwtService.extractJwtFromRequest(request, JwtType.PHONE_VERIFY_TOKEN);

        if (oldTwoFactorToken.isPresent() && twoFactorService.hasActiveTwoFactorSession(oldTwoFactorToken.get())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        if (oldPhoneVerifyToken.isPresent() && twoFactorService.hasActivePhoneVerifySession(oldPhoneVerifyToken.get())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        if (userService.isUserRegistered(phoneNumber)) {
            //reminder continue from here
        } else {
            final String sessionId = twoFactorService.sendPhoneVerify(phoneNumber);
            final String phoneVerifyToken = jwtService.generatePhoneVerifyToken(phoneNumber, sessionId);
            Cookie sessionCookie = cookieFactory.phoneVerifyCookie(phoneVerifyToken);
            response.addCookie(sessionCookie);
            return ResponseBuilder.buildSuccess(
                    ResponseType.REGISTER_INITIATED.name(),
                    "",
                    HttpStatus.OK
            );
        }

        return null;

    }

}
