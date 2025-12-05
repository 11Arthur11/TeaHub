package dev.parhamziaei.teahub.enums;

public enum InstanceStatus {
    DEPLOYING("instance-status.deploying"),
    ONLINE("instance-status.online"),
    INVOICE_PENDING("instance-status.invoice-pending"),;

    private final String key;
    InstanceStatus(String key) {
        this.key = key;
    }
    public String key() {
        return this.key;
    }
}
