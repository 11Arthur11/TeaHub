package dev.parhamziaei.teahub.enums.audio_bot;

public enum NodeStatus {

    DISABLED("status.disabled"),
    FULL("status.full"),
    UNREACHABLE("status.unreachable"),
    LOGIN_FAILED("status.login-failed"),
    DISPATCHED("status.dispatched"),;

    private final String key;

    NodeStatus(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

}
