package dev.parhamziaei.teahub.integration.yatqa.enums;

public enum TSQuery {
    CREATE_SERVER("createserver");



    private final String cmd;

    TSQuery(String cmd) {
        this.cmd = cmd;
    }

    public String cmd() {
        return this.cmd;
    }
}
