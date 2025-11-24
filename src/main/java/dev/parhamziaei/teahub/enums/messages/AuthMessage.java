package dev.parhamziaei.teahub.enums.messages;

public enum AuthMessage {
    ACCOUNT_DISABLED("error.auth.account_disabled"),
    ACCOUNT_NOT_FOUND("error.auth.account_not_found"),
    ACCOUNT_LOCKED("error.auth.account_locked"),

    //note: authentication success messages
    ALREADY_LOGGED_IN("error.auth.already_logged_in"),
    LOGIN_SUCCESS("success.auth.login"),
    LOGOUT_SUCCESS("success.auth.logout"),

    //note: two factor auth messages
    TWO_FACTOR_SENT("success.2fa.sent"),
    TWO_FACTOR_INVALID("error.2fa.invalid"),

    //note: registration messages
    REGISTER_SUCCESSFUL("success.register.user_registered"),
    ACCOUNT_ALREADY_EXIST("error.register.account_already_exist"),

    //note: authentication error messages
    AUTH_BAD_CREDENTIALS("error.auth.bad_credentials");

    private final String key;

    AuthMessage(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
