package dev.parhamziaei.teahub.enums.shop;

public enum ResourceStatus {
    DEPLOYING("instance-status.deploying"),
    ACTIVE("instance-status.active"),
    PENDING_PROLONG("instance-status.pending-prolong"),;

    private final String key;
    ResourceStatus(String key) {
        this.key = key;
    }
    public String key() {
        return this.key;
    }
}
