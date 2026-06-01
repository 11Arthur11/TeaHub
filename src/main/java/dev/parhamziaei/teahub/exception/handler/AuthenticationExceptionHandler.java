package dev.parhamziaei.teahub.exception.handler;

import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.messages.AuthMessage;
import dev.parhamziaei.teahub.exception.custom.authentication.*;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class AuthenticationExceptionHandler {

    private final MessageService messageService;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<SimpleResponse> badCredentialsException() {
        return ResponseBuilder.buildError(
                messageService.get(AuthMessage.AUTH_BAD_CREDENTIALS),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<SimpleResponse> usernameNotFoundException() {
        return ResponseBuilder.buildError(
                messageService.get(AuthMessage.ACCOUNT_NOT_FOUND),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<SimpleResponse> lockedException() {
        return ResponseBuilder.buildError(
                messageService.get(AuthMessage.ACCOUNT_LOCKED),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AlreadyLoggedInException.class)
    public ResponseEntity<SimpleResponse> alreadyLoggedInException() {
        return ResponseBuilder.buildError(
                messageService.get(AuthMessage.ALREADY_LOGGED_IN),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(InvalidTwoFactorException.class)
    public ResponseEntity<SimpleResponse> invalidTwoFactorException(InvalidTwoFactorException e) {
        log.debug("Invalid Two Factor: ", e);
        return ResponseBuilder.buildError(
                messageService.get(AuthMessage.TWO_FACTOR_INVALID),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<SimpleResponse> jwtValidationException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<SimpleResponse> disabledException() {
        return ResponseBuilder.buildError(
                messageService.get(AuthMessage.ACCOUNT_DISABLED),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PhoneNumberAlreadyTakenException.class)
    public ResponseEntity<SimpleResponse> handleEmailAlreadyTakenException(PhoneNumberAlreadyTakenException e) {
        return ResponseBuilder.buildError(
                messageService.get(AuthMessage.ACCOUNT_ALREADY_EXIST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BrokenJwtException.class)
    public ResponseEntity<Void> handleBrokenJwtException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


}
