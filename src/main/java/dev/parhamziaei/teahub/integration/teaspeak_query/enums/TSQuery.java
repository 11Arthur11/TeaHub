package dev.parhamziaei.teahub.integration.teaspeak_query.enums;

public enum TSQuery {
    PRIVILEGE_KEY_ADD("privilegekeyadd"),
    USE("use"),
    SERVER_CREATE("servercreate"),
    SERVER_INFO("serverinfo"),
    PRIVILEGE_KEY_LIST("privilegekeylist"),
    SERVER_START("serverstart"),
    SERVER_STOP("serverstop"),
    PRIVILEGE_KEY_DELETE("privilegekeydelete"),;



    private final String cmd;

    TSQuery(String cmd) {
        this.cmd = cmd;
    }

    public String cmd() {
        return this.cmd;
    }
}
