package dev.parhamziaei.teahub.exception.handler;

import dev.parhamziaei.teahub.dto.response.global.SimpleResponse;
import dev.parhamziaei.teahub.enums.messages.Message;
import dev.parhamziaei.teahub.enums.internal.ResponseType;
import dev.parhamziaei.teahub.enums.messages.ServiceMessage;
import dev.parhamziaei.teahub.exception.custom.authorization.NoSuchRoleException;
import dev.parhamziaei.teahub.exception.custom.global.ConflictEntityException;
import dev.parhamziaei.teahub.exception.custom.global.EntityInUseException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchDataException;
import dev.parhamziaei.teahub.exception.custom.global.NoSuchEntityException;
import dev.parhamziaei.teahub.exception.custom.service.audio_bot.AudioBotAlreadyInitiatedException;
import dev.parhamziaei.teahub.exception.custom.service.audio_bot.AudioBotGatewayException;
import dev.parhamziaei.teahub.exception.custom.service.audio_bot.AudioBotMustBeConnectedException;
import dev.parhamziaei.teahub.exception.custom.service.audio_bot.AudioBotSynchronizationException;
import dev.parhamziaei.teahub.exception.custom.service.payment.*;
import dev.parhamziaei.teahub.exception.custom.service.resource.ActionNotExecutableException;
import dev.parhamziaei.teahub.exception.custom.service.resource.ResourceProvisionException;
import dev.parhamziaei.teahub.exception.custom.service.resource.ResourceSuspendedException;
import dev.parhamziaei.teahub.exception.custom.service.shop.CategoryNotFoundException;
import dev.parhamziaei.teahub.exception.custom.service.storage.FileStorageServiceException;
import dev.parhamziaei.teahub.exception.custom.service.storage.MediaSizeTooLargeException;
import dev.parhamziaei.teahub.exception.custom.service.storage.MediaTypeNotAllowedException;
import dev.parhamziaei.teahub.exception.custom.service.teaspeak.*;
import dev.parhamziaei.teahub.exception.custom.service.ticket.TicketMaxAttachmentReachedException;
import dev.parhamziaei.teahub.exception.custom.service.ticket.TicketServiceException;
import dev.parhamziaei.teahub.exception.custom.service.user.InsufficientBalanceException;
import dev.parhamziaei.teahub.exception.custom.service.user.WalletChargeAmountTooSmallException;
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

    // TODO <Global, Default Exceptions>
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

    @ExceptionHandler(NoSuchDataException.class)
    public ResponseEntity<SimpleResponse> handleNoSuchDataException() {
        return ResponseBuilder.buildFailed(
                ResponseType.NO_DATA,
                messageService.get(ServiceMessage.DEFAULT_NO_SUCH_DATA),
                HttpStatus.OK
        );
    }

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<SimpleResponse> handleNoSuchEntityException() {
        return ResponseBuilder.buildFailed(
                ResponseType.NO_DATA,
                messageService.get(ServiceMessage.DEFAULT_NO_SUCH_ENTITY),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConflictEntityException.class)
    public ResponseEntity<SimpleResponse> handleConflictEntityException(ConflictEntityException e) {
        return ResponseBuilder.buildFailed(
                ResponseType.FAILURE,
                messageService.get(ServiceMessage.DEFAULT_CONFLICTION) + e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(EntityInUseException.class)
    public ResponseEntity<SimpleResponse> handleEntityInUseException(EntityInUseException e) {
        return ResponseBuilder.buildFailed(
                ResponseType.FAILURE,
                messageService.get(ServiceMessage.DEFAULT_IN_USE) +  e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    // ? <Ticket Service Exceptions>
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

    // ? <File storage , storing exceptions>
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



    // ? <Authorization Exceptions>
    @ExceptionHandler(NoSuchRoleException.class)
    public ResponseEntity<SimpleResponse> handleNoSuchRoleException() {
        log.error("Error on finding role, did you removed roles from database?");
        return ResponseBuilder.buildError(
                messageService.get(Message.SERVER_INTERNAL_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // ? <Query & TeaSpeak Service Exceptions>
    @ExceptionHandler(QueryInstanceAlreadyInitiatedException.class)
    public ResponseEntity<SimpleResponse> handleQueryInstanceAlreadyInitiatedException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.QUERY_INSTANCE_ALREADY_EXIST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InstancePortRangeNotValidException.class)
    public ResponseEntity<SimpleResponse> handleInstancePortRangeNotValidException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.QUERY_INSTANCE_PORT_RANGE_INVALID),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(QueryInstanceException.class)
    public ResponseEntity<SimpleResponse> handleQueryInstanceException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.QUERY_INSTANCE_ERROR),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(QueryInstanceNotFoundException.class)
    public ResponseEntity<SimpleResponse> handleQueryInstanceNotFoundException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.QUERY_INSTANCE_NOT_FOUND),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(ActionNotExecutableException.class)
    public ResponseEntity<SimpleResponse> handleActionNotExecutableException(ActionNotExecutableException e) {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.DEFAULT_ACTION_FAILED) + e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    // ? <AudioBot & Audio Bot Node Exceptions>
    @ExceptionHandler(AudioBotAlreadyInitiatedException.class)
    public ResponseEntity<SimpleResponse> handleAudioBotAlreadyInitiatedException(AudioBotAlreadyInitiatedException e) {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.AUDIO_BOT_WEB_ADDRESS_ALREADY_INITIATED) + e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AudioBotSynchronizationException.class)
    public ResponseEntity<SimpleResponse> handleAudioBotSynchronizationException(AudioBotSynchronizationException e) {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.DEFAULT_EXTERNAL_SERVICE_UNAVAILABLE),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(AudioBotMustBeConnectedException.class)
    public ResponseEntity<SimpleResponse> handleAudioBotMustBeConnectedException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.AUDIO_BOT_MUST_BE_CONNECTED),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(AudioBotGatewayException.class)
    public ResponseEntity<SimpleResponse> handleAudioBotGatewayException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.DEFAULT_ACTION_FAILED),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    //TODO <Gateway & Payment Exceptions>
    @ExceptionHandler(GatewayException.class)
    public ResponseEntity<SimpleResponse> handleGatewayException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.PAYMENT_GATEWAY_ERROR),
                HttpStatus.GATEWAY_TIMEOUT
        );
    }

    @ExceptionHandler(GatewayConfigException.class)
    public ResponseEntity<SimpleResponse> handleGatewayConfigException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.PAYMENT_GATEWAY_CONFIG_ERROR),
                HttpStatus.BAD_GATEWAY
        );
    }

    @ExceptionHandler(GatewayNotFoundException.class)
    public ResponseEntity<SimpleResponse> handleGatewayNotFoundException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.PAYMENT_GATEWAY_NOT_FOUND),
                HttpStatus.GATEWAY_TIMEOUT
        );
    }

    @ExceptionHandler(InvoiceException.class)
    public ResponseEntity<SimpleResponse> handleInvoiceException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.PAYMENT_INVOICE_ERROR),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<SimpleResponse> handlePaymentFailedException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.PAYMENT_FAILED),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<SimpleResponse> handleInsufficientBalanceException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.PAYMENT_INSUFFICIENT_BALANCE),
                HttpStatus.BAD_REQUEST
        );
    }

    //TODO <Resource & Deployment Exceptions>
    @ExceptionHandler(ResourceProvisionException.class)
    public ResponseEntity<SimpleResponse> handleResourceProvisionException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.RESOURCE_PROVISION_ERROR),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ResourceSuspendedException.class)
    public ResponseEntity<SimpleResponse> handleResourceSuspendedException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.RESOURCE_SUSPENDED),
                HttpStatus.BAD_REQUEST
        );
    }

    //TODO <Shop Exceptions>
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<SimpleResponse> handleCategoryNotFoundException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.SHOP_CATEGORY_NOT_FOUND),
                HttpStatus.BAD_REQUEST
        );
    }

    //TODO <User Exceptions>
    @ExceptionHandler(WalletChargeAmountTooSmallException.class)
    public ResponseEntity<SimpleResponse> handleWalletChargeAmountTooSmallException() {
        return ResponseBuilder.buildError(
                messageService.get(ServiceMessage.USER_WALLET_CHARGE_AMOUNT_TOO_SMALL),
                HttpStatus.BAD_REQUEST
        );
    }

}
