package dev.parhamziaei.teahub.enums;

public enum Message {

    AUTH_BAD_CREDENTIALS("error.auth.bad_credentials"),
    AUTH_ACCOUNT_DISABLED("error.auth.account_disabled"),
    AUTH_ACCOUNT_NOT_FOUND("error.auth.account_not_found"),
    AUTH_ACCOUNT_LOCKED("error.auth.account_locked"),
    AUTH_ALREADY_LOGGED_IN("error.auth.already_logged_in"),

    AUTH_LOGIN_SUCCESS("success.auth.login"),
    AUTH_LOGOUT_SUCCESS("success.auth.logout"),

    TWO_FACTOR_SENT("success.2fa.sent"),
    TWO_FACTOR_VERIFIED("success.2fa.verified"),
    TWO_FACTOR_INVALID("error.2fa.invalid"),

    REGISTER_SUCCESSFULLY_DONE("success.register.user_registered"),
    REGISTER_ACCOUNT_ALREADY_EXIST("error.register.account_already_exist"),
    REGISTER_ACCOUNT_NOT_VERIFIED("error.register.account_not_verified"),

    SERVER_INTERNAL_ERROR("error.server.internal"),
    SERVER_IO_ERROR("error.server.io"),
    SERVER_VALIDATION_ERROR("error.server.validation"),
    SERVER_RESOURCE_NOT_FOUND("error.server.resource_not_found"),
    DEFAULT_FAILED("error.default_failed"),

    USER_PASSWORD_CHANGE_SUCCESS("success.user.password_change"),
    USER_PASSWORD_CHANGE_FAILED("failed.user.password_change"),

    SERVICE_FILE_STORAGE_ERROR("error.service.file.storage"),
    SERVICE_PAYLOAD_TOO_LARGE("error.service.file.payload-too-large"),
    SERVICE_MEDIA_TYPE_NOT_ALLOWED("error.service.file.type-not-allowed"),

    SERVICE_TICKET_MAX_ATTACHMENT_REACHED("error.service.ticket.max_attachment_reached"),
    SERVICE_TICKET_MESSAGE_SENT("success.service.ticket.message_sent"),
    SERVICE_TICKET_SUBMITTED("success.service.ticket.submitted"),
    SERVICE_TICKET_EDITED("success.service.ticket.edited");

    private final String key;

    Message(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

}
