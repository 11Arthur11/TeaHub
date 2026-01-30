package dev.parhamziaei.teahub.enums.internal;

public enum KafkaTopic {

    INTERNAL_TELNET_ERROR_TOPIC("internal-telnet-error-topic"),
    INTERNAL_QUERY_INSTANCE_TOPIC("internal-query-instance-topic"),
    TEASPEAK_OPERATION_TOPIC("teaspeak-operation-topic"),
    AUDIO_BOT_OPERATION_TOPIC("audio-bot-operation-topic"),
    BILLABLE_RESOURCE_TOPIC("billable-resource-topic"),;

    private final String value;
    KafkaTopic(String value) {
        this.value = value;
    }
    public String value() {
        return value;
    }

}
