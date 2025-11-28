package dev.parhamziaei.teahub.exception.handler;

import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.messages.Message;
import dev.parhamziaei.teahub.enums.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.exception.custom.authentication.BrokenJwtException;
import dev.parhamziaei.teahub.exception.custom.authorization.NoSuchRoleException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.service.storage.FileStorageServiceException;
import dev.parhamziaei.teahub.exception.custom.service.storage.MediaSizeTooLargeException;
import dev.parhamziaei.teahub.exception.custom.service.storage.MediaTypeNotAllowedException;
import dev.parhamziaei.teahub.exception.custom.service.teaspeak.QueryInstanceAlreadyInitiatedException;
import dev.parhamziaei.teahub.exception.custom.service.ticket.TicketMaxAttachmentReachedException;
import dev.parhamziaei.teahub.exception.custom.service.ticket.TicketServiceException;
import dev.parhamziaei.teahub.service.MessageService;
import dev.parhamziaei.teahub.utils.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageService messageService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SimpleResponse> generalException() {
        return ResponseBuilder.buildError(
                messageService.get(Message.SERVER_INTERNAL_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<SimpleResponse> noResourceFoundException(HttpServletResponse response, HttpServletRequest request) {
        return ResponseBuilder.buildError(
                messageService.get(Message.SERVER_RESOURCE_NOT_FOUND),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<SimpleResponse> ioException() {
        return ResponseBuilder.buildError(
                messageService.get(Message.SERVER_IO_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SimpleResponse> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
//        String validationError = Optional.ofNullable(exception.getBindingResult().getFieldError())
//                .map(FieldError::getDefaultMessage)
//                .orElse("REQUEST_ARGUMENT_ERROR");

        return ResponseBuilder.buildError(
                messageService.get(Message.SERVER_VALIDATION_ERROR),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(TicketServiceException.class)
    public ResponseEntity<SimpleResponse> handleTicketServiceException() {
        return ResponseBuilder.buildError(
                messageService.get(Message.DEFAULT_FAILED),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(TicketMaxAttachmentReachedException.class)
    public ResponseEntity<SimpleResponse> handleTicketMaxAttachmentReachedException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.TICKET_MAX_ATTACHMENT_REACHED),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(FileStorageServiceException.class)
    public ResponseEntity<SimpleResponse> handleFileStorageService() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.FILE_STORAGE_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MediaTypeNotAllowedException.class)
    public ResponseEntity<SimpleResponse> handleUnsupportedMediaTypeException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.MEDIA_TYPE_NOT_ALLOWED),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE
        );
    }

    @ExceptionHandler(MediaSizeTooLargeException.class)
    public ResponseEntity<SimpleResponse> handleMediaSizeTooLargeException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.PAYLOAD_TOO_LARGE),
                HttpStatus.PAYLOAD_TOO_LARGE
        );
    }

    @ExceptionHandler(NoSuchDataException.class)
    public ResponseEntity<SimpleResponse> handleNoSuchDataException() {
        return ResponseBuilder.buildFailed(
                ResponseType.NO_DATA,
                "",
                HttpStatus.OK
        );
    }

    @ExceptionHandler(NoSuchRoleException.class)
    public ResponseEntity<SimpleResponse> handleNoSuchRoleException() {
        log.error("Error on finding role, did you removed roles from database?");
        return ResponseBuilder.buildError(
                messageService.get(Message.SERVER_INTERNAL_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(QueryInstanceAlreadyInitiatedException.class)
    public ResponseEntity<SimpleResponse> handleQueryInstanceAlreadyInitiatedException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.QUERY_INSTANCE_ALREADY_EXIST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BrokenJwtException.class)
    public ResponseEntity<Void> handleBrokenJwtException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
