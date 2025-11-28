package dev.parhamziaei.teahub.enums;

public enum KafkaTopic {
    INTERNAL_TELNET_ERROR_TOPIC("internal-telnet-error-topic"),
    INTERNAL_QUERY_INSTANCE_TOPIC("internal-query-instance-topic");

    private final String value;
    KafkaTopic(String value) {
        this.value = value;
    }
    public String value() {
        return value;
    }

}
