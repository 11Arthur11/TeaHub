package dev.parhamziaei.teahub.exception.handler;

import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.Message;
import dev.parhamziaei.teahub.exception.custom.authentication.AlreadyLoggedInException;
import dev.parhamziaei.teahub.exception.custom.authentication.PhoneNumberAlreadyTakenException;
import dev.parhamziaei.teahub.exception.custom.authentication.InvalidTwoFactorException;
import dev.parhamziaei.teahub.exception.custom.authentication.JwtValidationException;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class AuthenticationExceptionHandler {

    private final MessageService messageService;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<SimpleResponse> badCredentialsException() {
        return ResponseBuilder.buildFailed(
                "ERROR",
                messageService.get(Message.AUTH_BAD_CREDENTIALS),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<SimpleResponse> usernameNotFoundException() {
        return ResponseBuilder.buildFailed(
                "ERROR",
                messageService.get(Message.AUTH_ACCOUNT_NOT_FOUND),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<SimpleResponse> lockedException() {
        return ResponseBuilder.buildFailed(
                "ERROR",
                messageService.get(Message.AUTH_ACCOUNT_LOCKED),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AlreadyLoggedInException.class)
    public ResponseEntity<SimpleResponse> alreadyLoggedInException() {
        return ResponseBuilder.buildFailed(
                "ERROR",
                messageService.get(Message.AUTH_ALREADY_LOGGED_IN),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(InvalidTwoFactorException.class)
    public ResponseEntity<SimpleResponse> invalidTwoFactorException() {
        return ResponseBuilder.buildFailed(
                "ERROR",
                messageService.get(Message.TWO_FACTOR_INVALID),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(JwtValidationException.class)
    public ResponseEntity<SimpleResponse> jwtValidationException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<SimpleResponse> disabledException() {
        return ResponseBuilder.buildFailed(
                "ERROR",
                messageService.get(Message.AUTH_ACCOUNT_DISABLED),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PhoneNumberAlreadyTakenException.class)
    public ResponseEntity<SimpleResponse> handleEmailAlreadyTakenException(PhoneNumberAlreadyTakenException e) {
        return ResponseBuilder.buildFailed(
                "ERROR",
                messageService.get(Message.REGISTER_ACCOUNT_ALREADY_EXIST),
                HttpStatus.BAD_REQUEST
        );
    }

}
