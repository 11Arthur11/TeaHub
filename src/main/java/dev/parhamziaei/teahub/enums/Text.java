package dev.parhamziaei.teahub.enums;

@Deprecated
public enum Text {

    MAIL_TITLE_EMAIL_VERIFICATION("mail.title.email_verification"),
    MAIL_TITLE_EMAIL_TWO_FACTOR("mail.title.two_factor_code"),
    MAIL_TITLE_FORGOT_PASSWORD("mail.title.forgot_password");

    private final String key;

    Text(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
