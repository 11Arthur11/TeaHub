package dev.parhamziaei.teahub.enums.dns;

public enum DnsProviderStatus {
    CONNECTED("dns-provider-status.connected"),
    API_KEY_REJECTED("dns-provider-status.api-key-rejected"),
    SERVER_ERROR("dns-provider-status.server-error"),
    UNKNOWN("dns-provider-status.unknown");

    private final String key;

    DnsProviderStatus(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
