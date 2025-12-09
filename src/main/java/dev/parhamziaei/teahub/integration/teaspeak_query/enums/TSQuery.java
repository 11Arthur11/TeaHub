package dev.parhamziaei.teahub.integration.teaspeak_query.enums;

public enum TSQuery {
    PRIVILEGE_KEY_ADD("privilegekeyadd"),
    USE("use"),
    CREATE_SERVER("createserver"),
    SERVER_INFO("serverinfo");



    private final String cmd;

    TSQuery(String cmd) {
        this.cmd = cmd;
    }

    public String cmd() {
        return this.cmd;
    }
}
