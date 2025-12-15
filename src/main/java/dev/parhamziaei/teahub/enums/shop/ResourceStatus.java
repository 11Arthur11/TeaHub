package dev.parhamziaei.teahub.enums.shop;

public enum ResourceStatus {
    DEPLOYING("instance-status.deploying"),
    ACTIVE("instance-status.active"),
    INVOICE_PENDING("instance-status.invoice-pending"),;

    private final String key;
    ResourceStatus(String key) {
        this.key = key;
    }
    public String key() {
        return this.key;
    }
}
